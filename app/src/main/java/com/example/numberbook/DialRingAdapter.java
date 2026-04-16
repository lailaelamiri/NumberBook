package com.example.numberbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DialRingAdapter extends RecyclerView.Adapter<DialRingAdapter.RingSlotHolder> {

    private List<RingEntry> ringCollection;

    public DialRingAdapter(List<RingEntry> ringCollection) {
        this.ringCollection = ringCollection;
    }

    @NonNull
    @Override
    public RingSlotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View slotView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new RingSlotHolder(slotView);
    }

    @Override
    public void onBindViewHolder(@NonNull RingSlotHolder holder, int position) {
        RingEntry entry = ringCollection.get(position);
        holder.entryTitle.setText(entry.getDisplayName());
        holder.entrySubtitle.setText(entry.getDialNumber());
    }

    @Override
    public int getItemCount() {
        return ringCollection.size();
    }

    public void refreshVault(List<RingEntry> freshEntries) {
        this.ringCollection = freshEntries;
        notifyDataSetChanged();
    }

    static class RingSlotHolder extends RecyclerView.ViewHolder {
        TextView entryTitle, entrySubtitle;

        public RingSlotHolder(@NonNull View slotView) {
            super(slotView);
            entryTitle = slotView.findViewById(android.R.id.text1);
            entrySubtitle = slotView.findViewById(android.R.id.text2);
        }
    }
}