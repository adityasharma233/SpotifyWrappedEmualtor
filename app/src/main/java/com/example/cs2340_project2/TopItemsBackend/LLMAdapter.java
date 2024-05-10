package com.example.cs2340_project2.TopItemsBackend;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs2340_project2.R;

import java.util.List;

public class LLMAdapter extends RecyclerView.Adapter<LLMAdapter.ViewHolder> {
    private List<String> llmResponses;

    private List<String> llmDescriptions;

    public LLMAdapter(List<String> llmResponses, List<String> llmDescriptions) {
        this.llmResponses = llmResponses;
        this.llmDescriptions = llmDescriptions;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_llm, parent, false);
        return new ViewHolder(view);
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {this.listener = listener;}

    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.descriptionTextView.setText(llmResponses.get(position));

        holder.descriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LLMDialogFragment dialog = new LLMDialogFragment(llmResponses.get(position), llmDescriptions.get(position));
                dialog.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), "LLMDialogFragment");
            }
        });
    }

    public int getItemCount() {return llmResponses.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.llmDescriptionTextView);
        }
    }
}
