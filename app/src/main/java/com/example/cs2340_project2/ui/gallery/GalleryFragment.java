package com.example.cs2340_project2.ui.gallery;


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
import com.example.cs2340_project2.TopItemsBackend.SpotifyAdapter;
import com.example.cs2340_project2.TopItemsBackend.SpotifyWrapped;
import com.example.cs2340_project2.databinding.FragmentGalleryBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class GalleryFragment extends Fragment implements SpotifyAdapter.OnItemClickListener{

    private FragmentGalleryBinding binding;
    private ParseJSON parse;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel slideshowViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button hiddenButton = root.findViewById(R.id.hidden_button);

        hiddenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parse = loadTasksFromSharedPreferences();
                createAdapter(root);
                SpotifyWrapped gpt = new SpotifyWrapped(parse.getList());
                gpt.gptDescription(gpt.getTopArtists(), gpt.getTopGenres());
            }
        });

        Button navBack = root.findViewById(R.id.artist_nav_back);

        navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.action_nav_gallery_to_nav_home);
            }
        });

        return root;
    }


    private void createAdapter(View v) {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing); // Define your spacing in resources
        recyclerView.addItemDecoration(new ItemDecoration(requireContext(), spacingInPixels));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SpotifyAdapter adapter = new SpotifyAdapter(parse.getList());
        adapter.setOnItemClickListener(this); // Assuming your fragment/activity implements OnItemClickListener
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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


    @Override
    public void onItemClick(int position) {
        //
    }
}