package com.stefan.reserv.Fragments;

import static android.content.ContentValues.TAG;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stefan.reserv.Adapter.CinemaAdapter;
import com.stefan.reserv.Adapter.GenreAdapter;
import com.stefan.reserv.Adapter.TopMoviesAdapter;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.Cinema;
import com.stefan.reserv.Model.Movie;
import com.stefan.reserv.Model.User;
import com.stefan.reserv.MovieList;
import com.stefan.reserv.MovieView;
import com.stefan.reserv.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements TopMoviesAdapter.OnPopularMovieClickListener, GenreAdapter.OnGenreClickListener {
    private TextView see_more_btn;
    private ImageView carousel_iv;
    private RecyclerView movie_recyclerView, genre_recyclerView;
    private MyDatabaseHelper myDB;
    private TopMoviesAdapter movie_adapter;
    private GenreAdapter genre_adapter;
    private ArrayList<Movie> movieList;
    private ArrayList<String> genreList;
    private Movie movie = null;
    private User current_user;
    private String genre = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        movieList = new ArrayList<>();
        genreList = new ArrayList<>();
        myDB = new MyDatabaseHelper(getContext());
        see_more_btn = view.findViewById(R.id.see_more_btn);
        carousel_iv = view.findViewById(R.id.carousel_iv);
        movie_adapter = new TopMoviesAdapter(getContext(), movieList, this);
        movie_recyclerView = view.findViewById(R.id.popular_movies);
        movie_recyclerView.setAdapter(movie_adapter);
        movie_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            current_user = bundle.getParcelable("current_user");
        }
        genre_adapter = new GenreAdapter(getContext(), genreList, this);
        genre_recyclerView = view.findViewById(R.id.genre_recyclerView);
        genre_recyclerView.setAdapter(genre_adapter);
        AnimationDrawable animationDrawable = (AnimationDrawable) carousel_iv.getDrawable();
        animationDrawable.start();
        see_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MovieList.class);
                i.putExtra("current_user", current_user);
                startActivity(i);
            }
        });
        return view;
    }

    public void displayTopMovieData() {
        Cursor cursor = myDB.readAllMovieData(genre);
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No popular movies.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                movie = new Movie(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3));
                movieList.add(movie);
            }
            movie_adapter.notifyDataSetChanged();
        }
    }

    public void displayGenres() {
        Cursor cursor = myDB.readAllGenreData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No genres.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                genreList.add(cursor.getString(1));
            }
            genre_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        movieList.clear();
        genreList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayTopMovieData();
        displayGenres();
    }

    @Override
    public void OnPopularMovieClick(int position) {
        Intent i = new Intent(getContext(), MovieView.class);
        i.putExtra("movie", movieList.get(position));
        if (current_user != null) {
            i.putExtra("current_user", current_user);
        }
        startActivity(i);

    }


    @Override
    public void OnGenreClick(int position) {
        Intent i = new Intent(getContext(), MovieList.class);
        i.putExtra("current_user", current_user);
        i.putExtra("filter_genre", genreList.get(position));
        startActivity(i);
    }
}