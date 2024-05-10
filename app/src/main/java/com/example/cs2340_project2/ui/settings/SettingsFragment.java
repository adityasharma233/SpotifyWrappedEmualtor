package com.example.cs2340_project2.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.cs2340_project2.EnterApp.LoginActivity;
import com.example.cs2340_project2.EnterApp.RegistrationActivity;
import com.example.cs2340_project2.R;
import com.example.cs2340_project2.databinding.FragmentHomeBinding;
import com.example.cs2340_project2.databinding.FragmentSettingsBinding;
import com.example.cs2340_project2.ui.settings.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView userEmailTextView = root.findViewById(R.id.userEmail);

        Context context = getContext();
        if (context != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String userEmail = preferences.getString("userEmail", null);
            userEmailTextView.setText(userEmail);
        }

        Button changePasswordBtn = root.findViewById(R.id.newPassword_button);
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        Button deleteAccountBtn = root.findViewById(R.id.deleteAccount_button);
        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), DeleteAccountActivity.class);
                startActivity(intent);
            }
        });

        Button logoutBtn = root.findViewById(R.id.logOut_button);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String logoutUrl = "https://accounts.spotify.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(logoutUrl));
                startActivityForResult(intent, 123);

            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
