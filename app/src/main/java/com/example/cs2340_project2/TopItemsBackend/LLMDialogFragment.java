package com.example.cs2340_project2.TopItemsBackend;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.cs2340_project2.R;

public class LLMDialogFragment extends DialogFragment {
    private String adjective;

    private String description;

    public LLMDialogFragment(String adjective, String description) {
        this.adjective = adjective;
        this.description = description;
    }

    @NonNull
    public AlertDialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(requireContext(), R.style.CustomDialogTheme));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_llm, null);

        TextView adjectiveTextView = view.findViewById(R.id.adjectives);
        adjectiveTextView.setText(adjective);

        TextView descriptionTextView = view.findViewById(R.id.description_llm);
        descriptionTextView.setText(description);

        builder.setView(view)
                .setTitle("More Information")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}
