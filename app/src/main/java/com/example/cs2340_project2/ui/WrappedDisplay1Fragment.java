package com.example.cs2340_project2.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.TransitionRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs2340_project2.R;
import com.example.cs2340_project2.TopItemsBackend.ArtistAdapter;
import com.example.cs2340_project2.TopItemsBackend.ParseJSON;
import com.example.cs2340_project2.TopItemsBackend.SongAdapter;
import com.example.cs2340_project2.TopItemsBackend.SpotifyResponse;
import com.example.cs2340_project2.TopItemsBackend.SpotifyWrapped;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

public class WrappedDisplay1Fragment extends Fragment {

    List<SpotifyResponse.Track> tracks;
    List<SpotifyResponse.Artist> artists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wrapped_display_1, container, false);

        // You can initialize views and set up the fragment here
        tracks = loadTracksFromSharedPreferences().getTrackList();
        artists = loadTasksFromSharedPreferences().getList();

        ImageView topArtist = view.findViewById(R.id.viewPagerArtistImg);
        Picasso.get().load(artists.get(0).getImages().get(0).getUrl()).into(topArtist);

        RecyclerView recyclerView = view.findViewById(R.id.artistDisplay);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ArtistAdapter artistAdapter = new ArtistAdapter(artists);
        recyclerView.setAdapter(artistAdapter);

        RecyclerView recyclerView2 = view.findViewById(R.id.trackDisplay);
        recyclerView2.setLayoutManager(new LinearLayoutManager(requireContext()));
        SongAdapter songAdapter = new SongAdapter(tracks);
        recyclerView2.setAdapter(songAdapter);

        SpotifyWrapped wrapped = new SpotifyWrapped(artists);
        TextView genre = view.findViewById(R.id.textView4);
        genre.setText(wrapped.getTopGenres().get(0).toUpperCase());
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

    private ParseJSON loadTracksFromSharedPreferences() {
        SharedPreferences preferences = requireContext().getSharedPreferences("MyTracks", Context.MODE_PRIVATE);
        String tasksJson = preferences.getString("tracks", "");

        Gson gson = new Gson();
        Type type = new TypeToken<ParseJSON>(){}.getType();

        ParseJSON artistJSON = gson.fromJson(tasksJson, type);

        if (artistJSON == null) {
            System.out.println("The save is NULL");
        }

        return artistJSON;
    }
}