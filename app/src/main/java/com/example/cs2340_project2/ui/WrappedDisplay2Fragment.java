package com.example.cs2340_project2.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs2340_project2.ItemDecoration;
import com.example.cs2340_project2.R;
import com.example.cs2340_project2.TopItemsBackend.OnlyArtistsAdapter;
import com.example.cs2340_project2.TopItemsBackend.ParseJSON;
import com.example.cs2340_project2.TopItemsBackend.SpotifyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class WrappedDisplay2Fragment extends Fragment {
    List<SpotifyResponse.Artist> artists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wrapped_display_2, container, false);

        // You can initialize views and set up the fragment here
        artists = loadTasksFromSharedPreferences().getList();

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        RecyclerView onlyArtists = view.findViewById(R.id.only_artists_list);
        onlyArtists.addItemDecoration(new ItemDecoration(requireContext(), spacingInPixels));
        onlyArtists.setLayoutManager(new LinearLayoutManager(requireContext()));
        OnlyArtistsAdapter onlyArtistsAdapter = new OnlyArtistsAdapter(artists);
        onlyArtists.setAdapter(onlyArtistsAdapter);
        return view;
    }

    private ParseJSON loadTasksFromSharedPreferences() {
        SharedPreferences preferences = requireContext().getSharedPreferences("MyArtists", Context.MODE_PRIVATE);
        String tasksJson = preferences.getString("artists", "");

        Gson gson = new Gson();
        Type type = new TypeToken<ParseJSON>(){}.getType();

        ParseJSON artistJSON = gson.fromJson(tasksJson, type);

        if (artistJSON == null) {
            System.out.println("The save is NULL");
        }

        return artistJSON;
    }
}