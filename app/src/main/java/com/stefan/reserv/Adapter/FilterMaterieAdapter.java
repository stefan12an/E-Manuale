package com.stefan.reserv.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.stefan.reserv.R;

import java.util.ArrayList;

public class FilterMaterieAdapter extends RecyclerView.Adapter<FilterMaterieAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> genreList;
    OnFilterMaterieClickListener onFilterMaterieClickListener;

    public FilterMaterieAdapter(Context context, ArrayList<String> genreList, OnFilterMaterieClickListener onFilterMaterieClickListener) {
        this.context = context;
        this.genreList = genreList;
        this.onFilterMaterieClickListener = onFilterMaterieClickListener;
    }

    public FilterMaterieAdapter(Context context, ArrayList<String> genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.each_filter_materie, parent, false);
        return new MyViewHolder(view, onFilterMaterieClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.genre.setText(genreList.get(position));
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView genre;
        CardView genre_cv;
        OnFilterMaterieClickListener onFilterMaterieClickListener;

        public MyViewHolder(@NonNull View itemView, OnFilterMaterieClickListener onFilterMaterieClickListener) {
            super(itemView);
            genre = itemView.findViewById(R.id.each_filter_materie);
            genre_cv = itemView.findViewById(R.id.filter_materie_cv);
            this.onFilterMaterieClickListener = onFilterMaterieClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onFilterMaterieClickListener.OnFilterMaterieClick(getAdapterPosition(), genre_cv);
        }
    }

    public interface OnFilterMaterieClickListener {
        void OnFilterMaterieClick(int position, CardView genre_cv);
    }
}
