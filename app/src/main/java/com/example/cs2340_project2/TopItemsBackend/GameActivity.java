package com.example.cs2340_project2.TopItemsBackend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340_project2.MainActivity;
import com.example.cs2340_project2.R;
import com.example.cs2340_project2.TopItemsBackend.ParseJSON;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cs2340_project2.TopItemsBackend.ParseJSON;
import com.example.cs2340_project2.TopItemsBackend.SpotifyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private EditText guessEditText;
    private Button submitButton;
    private TextView resultTextView;
    private String favoriteArtistName;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guess_artist);

        guessEditText = findViewById(R.id.guessEditText);
        submitButton = findViewById(R.id.submitButton);
        resultTextView = findViewById(R.id.resultTextView);

        getFavoriteArtistName(SharedPreferencesHelper.getSpotifyResponse(this));

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = guessEditText.getText().toString().trim();

                if (userInput.equalsIgnoreCase(favoriteArtistName)) {
                    resultTextView.setText("Congratulations, you win!");
                    resultTextView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(GameActivity.this, "Wrong, try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getFavoriteArtistName(ParseJSON response) {
        try {
            if (response != null) {

                List<SpotifyResponse.Artist> topArtists = response.getList();
                System.out.println(topArtists);

                if (!topArtists.isEmpty()) {
                    favoriteArtistName = topArtists.get(0).getName();
                } else {
                    favoriteArtistName = "No favorite artist found";
                }
            } else {
                favoriteArtistName = "Default Favorite Artist";
            }
        } catch (Exception e) {
            e.printStackTrace();
            favoriteArtistName = "Default Favorite Artist";
        }
    }
}