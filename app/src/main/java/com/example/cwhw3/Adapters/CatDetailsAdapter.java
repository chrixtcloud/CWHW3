package com.example.cwhw3.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cwhw3.CatDetails;
import com.example.cwhw3.cats.R;

import java.util.ArrayList;

public class CatDetailsAdapter extends RecyclerView.Adapter<CatDetailsAdapter.DetailsViewHolder> {
    private ArrayList<CatDetails> catStat;

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        public TextView statsValue;
        public TextView statsKey;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            statsValue = itemView.findViewById(R.id.value_stats);
            statsKey = itemView.findViewById(R.id.key_stats);
        }
    }

    public CatDetailsAdapter(ArrayList<CatDetails> stat) {
        catStat = stat;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_details, parent, false);
        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        CatDetails currentItem = catStat.get(position);

        holder.statsValue.setText(currentItem.getValue());
        holder.statsKey.setText(currentItem.getKey());
    }

    @Override
    public int getItemCount() {
        return catStat.size();
    }
}
