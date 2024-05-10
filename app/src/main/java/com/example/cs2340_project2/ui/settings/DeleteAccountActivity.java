package com.example.cs2340_project2.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.cs2340_project2.EnterApp.LoginActivity;
import com.example.cs2340_project2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteAccountActivity extends AppCompatActivity {

    private Button abortBtn, deleteBtn;
    private Animation rotateAnim;
    private ImageView loading_settings;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        mAuth = FirebaseAuth.getInstance();

        abortBtn = findViewById(R.id.abortDeletebtn);
        deleteBtn = findViewById(R.id.deleteAccountbtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteAccount();
            }
        });

        abortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setContentView(R.layout.settings_loading_layout);

                Fragment settingsFragment = new SettingsFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, settingsFragment) // Use android.R.id.content to replace the whole activity layout
                        .commit();

                getSupportActionBar().show();
                finish();
            }
        });

        getSupportActionBar().hide();

    }

    private Animation rotateAnimation() {
        return AnimationUtils.loadAnimation(this, R.anim.rotate);
    }

    private void deleteAccount() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userEmail = preferences.getString("userEmail", null);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(),
                                                "Account Deleted successfully!",
                                                Toast.LENGTH_LONG)
                                        .show();

                                Intent intent
                                        = new Intent(com.example.cs2340_project2.ui.settings.DeleteAccountActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(),
                                                "Account deletion failed!!"
                                                        + " Please try again later",
                                                Toast.LENGTH_LONG)
                                        .show();

                                getSupportFragmentManager().popBackStack();
                            }
                        }
                    });
        }
    }
}
