package com.stefan.reserv.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stefan.reserv.Model.Movie;
import com.stefan.reserv.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {

    Context context;
    ArrayList<Movie> movieList;
    OnMovieClickListener onMovieClickListener;
    public MovieListAdapter(Context context, ArrayList<Movie> movieList, OnMovieClickListener onMovieClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.onMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.each_movie, parent, false);
        return new MyViewHolder(view, onMovieClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ByteArrayInputStream imageStream = new ByteArrayInputStream(movieList.get(position).getMovie_poster());
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        holder.movie_poster.setImageBitmap(theImage);
        holder.movie_title.setText(movieList.get(position).getTitle());
        holder.movie_date.setText(movieList.get(position).getRelease_date());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movie_poster;
        TextView movie_title,movie_date;
        OnMovieClickListener onMovieClickListener;
        public MyViewHolder(@NonNull View itemView, OnMovieClickListener onMovieClickListener) {
            super(itemView);
            this.onMovieClickListener = onMovieClickListener;
            movie_poster = itemView.findViewById(R.id.movie_card_poster);
            movie_title = itemView.findViewById(R.id.movie_card_title);
            movie_date = itemView.findViewById(R.id.movie_card_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieClickListener.OnMovieClick(getAdapterPosition());
        }
    }
    public interface OnMovieClickListener{
        void OnMovieClick(int position);
    }
}
