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

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> genreList;
    OnGenreClickListener onGenreClickListener;

    public GenreAdapter(Context context, ArrayList<String> genreList, OnGenreClickListener onGenreClickListener) {
        this.context = context;
        this.genreList = genreList;
        this.onGenreClickListener = onGenreClickListener;
    }

    public GenreAdapter(Context context, ArrayList<String> genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.each_genre, parent, false);
        return new MyViewHolder(view, onGenreClickListener);
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
        OnGenreClickListener onGenreClickListener;

        public MyViewHolder(@NonNull View itemView, OnGenreClickListener onGenreClickListener) {
            super(itemView);
            genre = itemView.findViewById(R.id.genre_tv);
            genre_cv = itemView.findViewById(R.id.genre_cv);
            this.onGenreClickListener = onGenreClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onGenreClickListener.OnGenreClick(getAdapterPosition(), genre_cv);
        }
    }

    public interface OnGenreClickListener {
        void OnGenreClick(int position, CardView genre_cv);
    }
}
