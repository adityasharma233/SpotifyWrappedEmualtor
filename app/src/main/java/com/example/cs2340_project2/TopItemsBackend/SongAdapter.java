package com.example.cs2340_project2.TopItemsBackend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs2340_project2.R;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private List<SpotifyResponse.Track> tracks;

    public SongAdapter(List<SpotifyResponse.Track> tracks) {
        this.tracks = tracks.subList(0,5);
    }

    @NonNull
    @Override
    public SongAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_main, parent, false);
        return new SongViewHolder(view);
    }

    public void onBindViewHolder(SongViewHolder holder, int position) {
        SpotifyResponse.Track track = tracks.get(position);
        holder.bind(track, position + 1);
    }

    public int getItemCount() {return tracks.size();}

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;

        public SongViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }

        public void bind(SpotifyResponse.Track track, int rank) {
            String rankText = "#" + rank;
            nameTextView.setText(rankText + " " + track.getTrackName());
        }
    }
}
