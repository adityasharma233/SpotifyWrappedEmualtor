package com.example.cs2340_project2.TopItemsBackend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cs2340_project2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OnlyArtistsAdapter extends RecyclerView.Adapter<OnlyArtistsAdapter.OnlyArtistsViewHolder> {
    private List<SpotifyResponse.Artist> artists;

    public OnlyArtistsAdapter(List<SpotifyResponse.Artist> artists) {
        this.artists = artists.subList(0,5);
    }

    @NonNull
    @Override
    public OnlyArtistsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_single_list, parent, false);
        return new OnlyArtistsViewHolder(view);
    }

    public void onBindViewHolder(OnlyArtistsViewHolder holder, int position) {
        SpotifyResponse.Artist artist = artists.get(position);
        holder.bind(artist, position + 1);
    }

    public int getItemCount() {return artists.size();}

    public static class OnlyArtistsViewHolder extends RecyclerView.ViewHolder {
        private TextView artistNameTextView;
        private ImageView artistImageView;
        private TextView artistRank;

        public OnlyArtistsViewHolder(View itemView) {
            super(itemView);
            artistImageView = itemView.findViewById(R.id.artist_img_single);
            artistNameTextView = itemView.findViewById(R.id.artist_name_single);
            artistRank = itemView.findViewById(R.id.top_artist_single);
        }

        public void bind(SpotifyResponse.Artist artist, int rank) {
            artistNameTextView.setText(artist.getName());
            artistRank.setText(String.valueOf(rank));
            Picasso.get().load(artist.getImages().get(0).getUrl()).into(artistImageView);
        }
    }
}
