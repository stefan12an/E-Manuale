package com.stefan.reserv.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.stefan.reserv.R;

import java.util.ArrayList;

public class MaterieAdapter extends RecyclerView.Adapter<MaterieAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> genreList;
    OnMaterieClickListener onMaterieClickListener;

    public MaterieAdapter(Context context, ArrayList<String> genreList, OnMaterieClickListener onMaterieClickListener) {
        this.context = context;
        this.genreList = genreList;
        this.onMaterieClickListener = onMaterieClickListener;
    }

    public MaterieAdapter(Context context, ArrayList<String> genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.each_genre, parent, false);
        return new MyViewHolder(view, onMaterieClickListener);
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
        OnMaterieClickListener onMaterieClickListener;

        public MyViewHolder(@NonNull View itemView, OnMaterieClickListener onMaterieClickListener) {
            super(itemView);
            genre = itemView.findViewById(R.id.genre_tv);
            genre_cv = itemView.findViewById(R.id.genre_cv);
            this.onMaterieClickListener = onMaterieClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMaterieClickListener.OnMaterieClick(getAdapterPosition(), genre_cv);
        }
    }

    public interface OnMaterieClickListener {
        void OnMaterieClick(int position, CardView genre_cv);
    }
}
