package com.example.cs2340_project2.TopItemsBackend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cs2340_project2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OnlySongsAdapter extends RecyclerView.Adapter<OnlySongsAdapter.OnlySongsViewHolder> {
    private List<SpotifyResponse.Track> tracks;

    public OnlySongsAdapter(List<SpotifyResponse.Track> tracks) {
        this.tracks = tracks.subList(0,5);
    }

    @NonNull
    @Override
    public OnlySongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_single_list, parent, false);
        return new OnlySongsViewHolder(view);
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {this.listener = listener;}

    public void onBindViewHolder(OnlySongsViewHolder holder, int position) {
        SpotifyResponse.Track track = tracks.get(position);
        holder.bind(track, position + 1);

        holder.artistImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackDialogFragment dialog = new TrackDialogFragment(track);
                dialog.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), "TrackDialogFragment");
            }
        });
    }

    public int getItemCount() {return tracks.size();}

    public static class OnlySongsViewHolder extends RecyclerView.ViewHolder {
        private TextView artistNameTextView;
        private ImageView artistImageView;
        private TextView artistRank;

        public OnlySongsViewHolder(View itemView) {
            super(itemView);
            artistImageView = itemView.findViewById(R.id.artist_img_single);
            artistNameTextView = itemView.findViewById(R.id.artist_name_single);
            artistRank = itemView.findViewById(R.id.top_artist_single);
        }

        public void bind(SpotifyResponse.Track track, int rank) {
            artistNameTextView.setText(track.getTrackName());
            artistRank.setText(String.valueOf(rank));
            Picasso.get().load(track.getAlbum().getImages().get(0).getUrl()).into(artistImageView);
        }
    }
}
