package com.example.cs2340_project2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cs2340_project2.TopItemsBackend.ParseJSON;
import com.example.cs2340_project2.TopItemsBackend.GameActivity;
import com.example.cs2340_project2.TopItemsBackend.SharedPreferencesHelper;
import com.example.cs2340_project2.TopItemsBackend.SpotifyAdapter;
import com.example.cs2340_project2.TopItemsBackend.SpotifyAdapterTracks;
import com.example.cs2340_project2.TopItemsBackend.SpotifyResponse;
import com.example.cs2340_project2.TopItemsBackend.SpotifyWrapped;
import com.example.cs2340_project2.databinding.ActivityMainBinding;
import com.example.cs2340_project2.ui.WrappedDisplay1Fragment;
import com.example.cs2340_project2.ui.WrappedDisplay2Fragment;
import com.example.cs2340_project2.ui.WrappedDisplay3Fragment;
import com.example.cs2340_project2.ui.WrappedDisplay4Fragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SpotifyAdapter.OnItemClickListener, SpotifyAdapterTracks.OnItemClickListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public static final String CLIENT_ID = "ff04c4a521a04209a39365b9eb1f769c";
    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
    public static final int AUTH_CODE_REQUEST_CODE = 0x11;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken;
    private String mAccessCode;
    private Call mCall;

    private SpotifyWrapped wrapped;

    private ParseJSON parse;

    private ParseJSON parseTrack;

    private List<SpotifyResponse.Track> tracks;
    private List<SpotifyResponse.Artist> artists;
    private List<String> LLMPerson;
    private List<String> LLMDesc;

    @Override
    public void onItemClick(int position) {
        // Handle item click here
        // For example, you can open a new fragment or activity
    }

    public void openSpotifyGame(View view) {
        SharedPreferencesHelper.saveSpotifyResponse(this, parse);
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onGetUserProfileClicked(View view) {
        if (mAccessToken == null) {
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.fragment_home), R.string.warning_need_token, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.black));
            snackbar.show();
            return;
        }

        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?limit=10")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                setResponse("Failed to fetch data: " + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    parse = new ParseJSON(jsonObject);
                    saveTaskToSharedPreferences(parse);
                    artists = parse.getList();

                    wrapped = new SpotifyWrapped(parse.getList());
                    wrapped.gptDescription(wrapped.getTopArtists(), wrapped.getTopGenres());

                    System.out.println(jsonObject.toString(3));

                    // Perform the second request here
                    final Request requestTracks = new Request.Builder()
                            .url("https://api.spotify.com/v1/me/top/tracks?limit=10")
                            .addHeader("Authorization", "Bearer " + mAccessToken)
                            .build();

                    cancelCall();
                    mCall = mOkHttpClient.newCall(requestTracks);

                    mCall.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            setResponse("Failed to fetch data: " + e);
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            try {
                                final JSONObject jsonObject = new JSONObject(response.body().string());
                                parseTrack = new ParseJSON(jsonObject, parse.getSpotifyResponse());
                                saveTracksToSharedPreferences(parseTrack);
                                tracks = parseTrack.getTrackList();

                                System.out.println(jsonObject.toString(3));
                            } catch (JSONException e) {
                                System.out.println(e);
                            }

                        }
                    });
                } catch (JSONException e) {
                    //setResponse("Failed to parse data: " + e);
                }
            }
        });
        Button hiddenButton = findViewById(R.id.hidden_button);
        hiddenButton.setVisibility(View.VISIBLE);
    }

    private void saveTaskToSharedPreferences(ParseJSON parse) {
        SharedPreferences preferences = this.getSharedPreferences("MyArtists", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String tasksJson = gson.toJson(parse);
        editor.putString("artists", tasksJson);
        editor.apply();
    }

    private void saveTracksToSharedPreferences(ParseJSON parse) {
        SharedPreferences preferences = this.getSharedPreferences("MyTracks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String tasksJson = gson.toJson(parse);
        editor.putString("tracks", tasksJson);
        editor.apply();
    }

    public void createAdapter(View view) {
        Button game = findViewById(R.id.btn_spotify_game);
        game.setVisibility(View.VISIBLE);

        Button hiddenButton = findViewById(R.id.hidden_button);
        hiddenButton.setVisibility(View.GONE);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        LLMPerson = wrapped.getAdjectives();
        LLMDesc = wrapped.getDescriptions();

        if (user != null) {
            String userId = user.getUid();

            // Get the Firestore instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a reference to the collection where the data is stored
            CollectionReference userCollectionRef = db.collection("users").document(userId).collection("data");

            // Get the current date
            Date currentDate = new Date();

            // Convert the current date to a string in the format "MM/dd/yyyy"
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            String dateString = dateFormat.format(currentDate);

            // Check if data with the same date exists
            Query query = userCollectionRef.whereEqualTo("date", dateString);

            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (queryDocumentSnapshots.isEmpty()) {
                    // Data with the same date does not exist, so add the new data
                    Map<String, Object> newData = new HashMap<>();
                    newData.put("artists", artists);
                    newData.put("llmperson", LLMPerson);
                    newData.put("llmDesc", LLMDesc);
                    newData.put("tracks", tracks);
                    newData.put("date", dateString);

                    userCollectionRef.add(newData)
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "Data added with ID: " + documentReference.getId());
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error adding document", e);
                            });
                } else {
                    Log.d(TAG, "Data with the same date already exists.");
                }
            }).addOnFailureListener(e -> {
                Log.w(TAG, "Error getting documents.", e);
            });

        }

        findViewById(R.id.viewPager).setVisibility(View.VISIBLE);
        findViewById(R.id.imageButton2).setVisibility(View.VISIBLE);
        findViewById(R.id.imageButton).setVisibility(View.VISIBLE);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        MyPagerAdapter flip = new MyPagerAdapter(this);
        flip.adjectives = wrapped.getAdjectives();
        flip.descriptions = wrapped.getDescriptions();
        viewPager.setAdapter(flip);
        ImageButton previous = findViewById(R.id.imageButton);
        ImageButton next = findViewById(R.id.imageButton2);



        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() < viewPager.getAdapter().getItemCount() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });
    }

    private static class MyPagerAdapter extends FragmentStateAdapter {

        private List<String> adjectives;
        private List<String> descriptions;

        public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new WrappedDisplay1Fragment();
                case 1:
                    return new WrappedDisplay2Fragment();
                case 2:
                    return new WrappedDisplay3Fragment();
                case 3:
                    return new WrappedDisplay4Fragment(adjectives, descriptions);
                default:
                    throw new IllegalArgumentException("Invalid position: " + position);
            }
        }

        @Override
        public int getItemCount() {
            return 4; // Number of fragments
        }
    }

    public void onTopArtistClicked(View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_gallery);
    }

    public void onTopTrackClicked(View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_slideshow);
    }


    public void onRequestTokenClicked(View view) {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        System.out.println(getRedirectUri().toString());
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[]{"user-read-email", "user-top-read"})
                .setCampaign("your-campaign-token")
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);
        if (response.getError() != null && !response.getError().isEmpty()) {
            System.out.println(response.getError());
        }
        if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            mAccessToken = response.getAccessToken();
            //updateTokenView();
        } else if (requestCode == AUTH_CODE_REQUEST_CODE) {
            mAccessCode = response.getCode();
            //updateCodeView();
        }
    }

    private void setResponse(final String text) {
        runOnUiThread(() -> {
            //final TextView responseView = findViewById(R.id.response_text_view);
            //responseView.setText(text);
        });
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    private Uri getRedirectUri() {
        return new Uri.Builder()
                .scheme("spotify-sdk")
                .authority("auth")
                .build();
    }
}

