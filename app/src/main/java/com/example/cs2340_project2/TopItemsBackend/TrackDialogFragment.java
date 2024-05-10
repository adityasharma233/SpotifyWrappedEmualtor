package com.example.cs2340_project2.TopItemsBackend;

import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
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

import java.io.IOException;

public class TrackDialogFragment extends DialogFragment {

    private SpotifyResponse.Track track;

    private MediaPlayer mediaPlayer;


    public TrackDialogFragment(SpotifyResponse.Track track) {
        this.track = track;
    }

    @NonNull
    public AlertDialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(requireContext(), R.style.CustomDialogTheme));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_track, null);
        ImageView imageView = view.findViewById(R.id.dialogTrackImage);

        TextView trackNameTextView = view.findViewById(R.id.dialogTrackTextView);
        trackNameTextView.setText(track.getTrackName());

        TextView albumName = view.findViewById(R.id.dialogAlbumTextView);
        albumName.setText("Album: "  +track.getAlbum().getAlbumName());

        TextView trackDuration = view.findViewById(R.id.dialogDurationTextView);
        int totalSeconds = track.getDuration();
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String formattedTime = String.format("%d:%02d", minutes, seconds);
        trackDuration.setText("Duration: " + formattedTime);

        TextView trackPopularity = view.findViewById(R.id.dialogPopularityTrackTextView);
        trackPopularity.setText("Popularity: " + track.getPopularity());

        Picasso.get().load(track.getAlbum().getImages().get(0).getUrl()).into(imageView);
        playAudioFromUrl(track.getPreviewURL());

        builder.setView(view)
                .setTitle("Track Information")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        dismiss();
                    }
                });
        return builder.create();
    }

    private void playAudioFromUrl(String url) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
