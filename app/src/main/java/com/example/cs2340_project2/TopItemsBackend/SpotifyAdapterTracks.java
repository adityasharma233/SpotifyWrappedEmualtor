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

public class SpotifyAdapterTracks extends RecyclerView.Adapter<SpotifyAdapterTracks.ViewHolder>{
    private List<SpotifyResponse.Track> tracks;

    public SpotifyAdapterTracks(List<SpotifyResponse.Track> tracks) {
        this.tracks = tracks;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        return new ViewHolder(view);
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        SpotifyResponse.Track track = tracks.get(position);
        holder.nameTrackTextView.setText(track.getTrackName());
        holder.numberTrackTextView.setText(String.valueOf(position + 1));
        Picasso.get().load(track.getAlbum().getImages().get(0).getUrl()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackDialogFragment dialog = new TrackDialogFragment(track);
                dialog.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), "TrackDialogFragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        TextView nameTrackTextView, numberTrackTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageTrackView);
            nameTrackTextView = itemView.findViewById(R.id.nameTrackTextView);
            numberTrackTextView = itemView.findViewById(R.id.numberTrackTextView);
        }
    }
}
