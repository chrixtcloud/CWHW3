package com.example.cwhw3.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cwhw3.CatBreed;
import com.example.cwhw3.cats.R;

import java.util.ArrayList;
import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> implements Filterable {
    private Context catContext;
    private ArrayList<CatBreed> catList;
    private ArrayList<CatBreed> catListFull;
    private OnItemClickListener catListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        catListener = listener;
    }

    public CatAdapter(Context context, ArrayList<CatBreed> list) {
        catContext = context;
        catList = list;
        catListFull = new ArrayList<>(catList);
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(catContext).inflate(R.layout.cat_item, parent, false);
        return new CatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        CatBreed currentCat = catList.get(position);

        String catName = currentCat.getName();

        holder.catName.setText(catName);

    }

    @Override
    public int getItemCount() {
        if (catList != null) {
            return catList.size();
        }
        return 0;
    }

    public class CatViewHolder extends RecyclerView.ViewHolder {
        public ImageView catImage;
        public TextView catName;


        public CatViewHolder(View itemView) {
            super(itemView);

            catName = itemView.findViewById(R.id.cat_name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (catListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            catListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }


    public Filter getFilter(){
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            List<CatBreed> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(catListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();


                for(CatBreed cat : catListFull){
                    if (cat.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(cat);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            catList.clear();
            System.out.println(results.values);
            if (results.values != null) {
                catList.addAll((List) results.values);
            }
            notifyDataSetChanged();

        }
   };
}
