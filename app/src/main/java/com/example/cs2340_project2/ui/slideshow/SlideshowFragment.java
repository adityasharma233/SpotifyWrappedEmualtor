package com.example.cs2340_project2.ui.slideshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs2340_project2.ItemDecoration;
import com.example.cs2340_project2.TopItemsBackend.ParseJSON;
import com.example.cs2340_project2.R;
import com.example.cs2340_project2.TopItemsBackend.SpotifyAdapterTracks;
import com.example.cs2340_project2.databinding.FragmentSlideshowBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SlideshowFragment extends Fragment implements SpotifyAdapterTracks.OnItemClickListener {

    private FragmentSlideshowBinding binding;

    private ParseJSON parse;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button showTracks = root.findViewById(R.id.show_tracks);

        showTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parse = loadTracksFromSharedPreferences();
                createTrackAdapter(root);
            }
        });

        Button navBack = root.findViewById(R.id.track_nav_back);

        navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.action_nav_slideshow_to_nav_home);
            }
        });

        return root;
    }

    private void createTrackAdapter(View v) {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewTracks);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing); // Define your spacing in resources
        recyclerView.addItemDecoration(new ItemDecoration(requireContext(), spacingInPixels));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SpotifyAdapterTracks adapter = new SpotifyAdapterTracks(parse.getTrackList());
        adapter.setOnItemClickListener(this); // Assuming your fragment/activity implements OnItemClickListener
        recyclerView.setAdapter(adapter);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        //click
    }
}