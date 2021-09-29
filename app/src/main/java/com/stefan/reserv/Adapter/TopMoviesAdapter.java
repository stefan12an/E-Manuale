package com.stefan.reserv.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stefan.reserv.Model.Movie;
import com.stefan.reserv.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class TopMoviesAdapter extends RecyclerView.Adapter<TopMoviesAdapter.MyViewHolder> {

    Context context;
    ArrayList<Movie> movieList;
    OnPopularMovieClickListener onPopularMovieClickListener;

    public TopMoviesAdapter(Context context, ArrayList<Movie> movieList, OnPopularMovieClickListener onPopularMovieClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.onPopularMovieClickListener = onPopularMovieClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.each_popular_movie, parent, false);
        return new MyViewHolder(view, onPopularMovieClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ByteArrayInputStream imageStream = new ByteArrayInputStream(movieList.get(position).getMovie_poster());
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        holder.cinema_photo_imv.setImageBitmap(theImage);
    }

    @Override
    public int getItemCount() {
        if (movieList.size() > 4) {
            return 4;
        } else {
            return movieList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cinema_photo_imv;
        OnPopularMovieClickListener onPopularMovieClickListener;

        public MyViewHolder(@NonNull View itemView, OnPopularMovieClickListener onPopularMovieClickListener) {
            super(itemView);
            cinema_photo_imv = itemView.findViewById(R.id.popular_movie);
            this.onPopularMovieClickListener = onPopularMovieClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPopularMovieClickListener.OnPopularMovieClick(getAdapterPosition());
        }
    }

    public interface OnPopularMovieClickListener {
        void OnPopularMovieClick(int position);
    }
}
