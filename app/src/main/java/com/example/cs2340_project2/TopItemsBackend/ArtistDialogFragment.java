package com.example.cs2340_project2.TopItemsBackend;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.cs2340_project2.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class ArtistDialogFragment extends DialogFragment {

    private SpotifyResponse.Artist artist;

    public ArtistDialogFragment(SpotifyResponse.Artist artist) {
        this.artist = artist;
    }

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(requireContext(), R.style.CustomDialogTheme));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_artist, null);
        ImageView imageView = view.findViewById(R.id.dialogArtistImage);

                // Customize the view with artist information
        // For example:
        TextView artistNameTextView = view.findViewById(R.id.dialogNameTextView);
        artistNameTextView.setText(artist.getName());
        TextView popularityTextView = view.findViewById(R.id.dialogPopularityTextView);
        popularityTextView.setText("Popularity: " + artist.getPopularity());
        TextView followersTextView = view.findViewById(R.id.dialogFollowersTextView);
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        followersTextView.setText("Followers: " + numberFormat.format(artist.getFollowers().getTotal()));

        TextView genresTextView = view.findViewById(R.id.dialogGenresTextView);
        genresTextView.setText("Genres: " + TextUtils.join(", ", artist.getGenres()));

        Picasso.get().load(artist.getImages().get(0).getUrl()).into(imageView);

        builder.setView(view)
                .setTitle("Artist Information")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the dialog
                        dismiss();
                    }
                });
        return builder.create();
    }
}
