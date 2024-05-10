package com.example.cs2340_project2.TopItemsBackend;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;


public class SharedPreferencesHelper {

    private static final String SHARED_PREFS_NAME = "SpotifyResponse";
    private static final String KEY_SPOTIFY_RESPONSE = "spotifyResponse";

    public static void saveSpotifyResponse(Context context, ParseJSON response) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String tasksJson = gson.toJson(response);
        editor.putString(KEY_SPOTIFY_RESPONSE, tasksJson);
        editor.apply();
    }

    public static ParseJSON getSpotifyResponse(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_SPOTIFY_RESPONSE, null);
        Gson gson = new Gson();
        Type type = new TypeToken<ParseJSON>(){}.getType();

        ParseJSON artistJSON = gson.fromJson(json, type);

        if (artistJSON == null) {
            System.out.println("The save is NULL");
        }

        return artistJSON;
    }

    public static void clearSpotifyResponse(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_SPOTIFY_RESPONSE);
        editor.apply();
    }
}
