package com.example.cs2340_project2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs2340_project2.ItemDecoration;
import com.example.cs2340_project2.R;
import com.example.cs2340_project2.TopItemsBackend.LLMAdapter;

import java.util.List;

public class WrappedDisplay4Fragment extends Fragment {
    private List<String> adjectives;
    private List<String> descriptions;

    public WrappedDisplay4Fragment(List<String> adjectives, List<String> descriptions) {
        this.adjectives = adjectives;
        this.descriptions = descriptions;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wrapped_display_4, container, false);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);

        RecyclerView personality = view.findViewById(R.id.recyclerPersonality);
        personality.addItemDecoration(new ItemDecoration(requireContext(), spacingInPixels));
        personality.setLayoutManager(new LinearLayoutManager(requireContext()));
        LLMAdapter adapter = new LLMAdapter(adjectives, descriptions);
        personality.setAdapter(adapter);
        return view;
    }
}
