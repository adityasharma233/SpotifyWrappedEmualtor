package com.example.cs2340_project2.TopItemsBackend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs2340_project2.R;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private List<SpotifyResponse.Artist> artists;

    public ArtistAdapter(List<SpotifyResponse.Artist> artists) {
        this.artists = artists.subList(0,5);
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_main, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        SpotifyResponse.Artist artist = artists.get(position);
        holder.bind(artist, position + 1); // Add 1 to position to start from 1
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }

        public void bind(SpotifyResponse.Artist artist, int rank) {
            String rankText = "#" + rank;
            nameTextView.setText(rankText + " " + artist.getName());
        }
    }
}
