package com.example.numberbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ResultSlotHolder> {

    private List<SearchResult> resultCollection;

    public SearchResultAdapter(List<SearchResult> resultCollection) {
        this.resultCollection = resultCollection;
    }

    @NonNull
    @Override
    public ResultSlotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View slotView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_search_result, parent, false);
        return new ResultSlotHolder(slotView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultSlotHolder holder, int position) {
        SearchResult result = resultCollection.get(position);
        holder.resultName.setText(result.getDisplayName());
        holder.resultPhone.setText(result.getDialNumber());
        holder.resultStats.setText("Registered " + result.getOccurrence() + " time(s) with this name");
        holder.resultPercent.setText(result.getPercentage() + "%");
    }

    @Override
    public int getItemCount() {
        return resultCollection.size();
    }

    public void refreshResults(List<SearchResult> freshResults) {
        this.resultCollection = freshResults;
        notifyDataSetChanged();
    }

    static class ResultSlotHolder extends RecyclerView.ViewHolder {
        TextView resultName, resultPhone, resultStats, resultPercent;

        public ResultSlotHolder(@NonNull View slotView) {
            super(slotView);
            resultName    = slotView.findViewById(R.id.tvResultName);
            resultPhone   = slotView.findViewById(R.id.tvResultPhone);
            resultStats   = slotView.findViewById(R.id.tvResultStats);
            resultPercent = slotView.findViewById(R.id.tvResultPercent);
        }
    }
}