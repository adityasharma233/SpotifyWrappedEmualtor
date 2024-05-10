package com.example.cs2340_project2.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.cs2340_project2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText passwordTextView1, passwordTextView2, currPasswordTextView;
    private Button Btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        passwordTextView1 = findViewById(R.id.newPassword);
        passwordTextView2 = findViewById(R.id.NewPasswordRepeat);
        currPasswordTextView = findViewById(R.id.currentPassword);
        Btn = findViewById(R.id.btnChangePassword);
        progressbar = findViewById(R.id.progressbar);

        // Set on Click Listener on Registration button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setNewPassword();
            }
        });

        getSupportActionBar().hide();
    }

    private void setNewPassword()
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String password1, password2, currPassword;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userEmail = preferences.getString("userEmail", null);
        password1 = passwordTextView1.getText().toString();
        password2 = passwordTextView2.getText().toString();
        currPassword = currPasswordTextView.getText().toString();

        // Validations for input password
        if (TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2) || TextUtils.isEmpty(currPassword)) {
            progressbar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (!password1.equals(password2)) {
            progressbar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),
                            "Please enter matching Passwords",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // create new user or register new user
        assert userEmail != null;
        mAuth
                .signInWithEmailAndPassword(userEmail, currPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            assert user != null;
                            user.updatePassword(password1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Password updated successfully
                                                Toast.makeText(getApplicationContext(),
                                                                "Password updated successfully!",
                                                                Toast.LENGTH_LONG)
                                                        .show();

                                                // hide the progress bar
                                                progressbar.setVisibility(View.GONE);

                                                // if the user created intent to login activity
                                                Intent intent
                                                        = new Intent(com.example.cs2340_project2.ui.settings.ChangePasswordActivity.this,
                                                        SettingsFragment.class);
                                                startActivity(intent);
                                            } else {
                                                progressbar.setVisibility(View.GONE);
                                                // Registration failed
                                                Toast.makeText(
                                                                getApplicationContext(),
                                                                "Registration failed!!"
                                                                        + " Please try again later",
                                                                Toast.LENGTH_LONG)
                                                        .show();

                                                // hide the progress bar
                                            }
                                        }
                                    });
                        }
                    }
                });
        }
    }