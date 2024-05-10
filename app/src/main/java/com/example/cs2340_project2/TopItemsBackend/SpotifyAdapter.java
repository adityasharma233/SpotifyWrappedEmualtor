package com.example.cs2340_project2.TopItemsBackend;

import android.graphics.Typeface;
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

public class SpotifyAdapter extends RecyclerView.Adapter<SpotifyAdapter.ViewHolder> {

    private List<SpotifyResponse.Artist> artists;

    public SpotifyAdapter(List<SpotifyResponse.Artist> artists) {
        this.artists = artists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new ViewHolder(view);
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SpotifyResponse.Artist artist = artists.get(position);
        holder.nameTextView.setText(artist.getName());
        //holder.popularityTextView.setText("Popularity: " + artist.getPopularity());
        //holder.genresTextView.setText("Genres: " + TextUtils.join(", ", artist.getGenres()));

        int imageSize;
        int numberSize;
        int numberStyle;

        if (position == 0) {
            imageSize = 450; // Largest size for item 1
            numberSize = 40; // Largest size for item 1
            numberStyle = Typeface.BOLD; // Bold style for item 1
        } else if (position == 1) {
            imageSize = 400; // Smaller size for item 2
            numberSize = 32; // Smaller size for item 2
            numberStyle = Typeface.NORMAL; // Normal style for item 2
        } else if (position == 2) {
            imageSize = 300; // Smaller size for item 3
            numberSize = 24; // Smaller size for item 3
            numberStyle = Typeface.NORMAL; // Normal style for item 3

        } else {
            imageSize = 250; // Default size for items 4+
            numberSize = 16; // Default size for items 4+
            numberStyle = Typeface.NORMAL; // Normal style for items 4+
        }

        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.width = imageSize;
        layoutParams.height = imageSize;
        holder.imageView.setLayoutParams(layoutParams);

        holder.numberTextView.setText(String.valueOf(position + 1));
        holder.numberTextView.setTextSize(numberSize);
        holder.numberTextView.setTypeface(null, numberStyle);

        Picasso.get().load(artist.getImages().get(0).getUrl()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show dialog with artist information
                ArtistDialogFragment dialog = new ArtistDialogFragment(artist);
                dialog.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "ArtistDialogFragment");
            }
        });
    }


    @Override
    public int getItemCount() {
        return artists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, popularityTextView, genresTextView, numberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            //popularityTextView = itemView.findViewById(R.id.popularityTextView);
            //genresTextView = itemView.findViewById(R.id.genresTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
        }
    }
}
