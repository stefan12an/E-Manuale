package com.stefan.reserv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.stefan.reserv.Adapter.MovieListAdapter;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.Movie;
import com.stefan.reserv.Model.User;

import java.util.ArrayList;

public class MovieList extends AppCompatActivity implements MovieListAdapter.OnMovieClickListener {
    private ArrayList<Movie> movieList;
    private MovieListAdapter movieListAdapter;
    private RecyclerView movieRv;
    private MyDatabaseHelper myDB;
    private Movie movie;
    private User current_user;
    private Integer Movie_Fragment = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        movieList = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra("current_user")) {
            Bundle bundle = getIntent().getExtras();
            current_user = bundle.getParcelable("current_user");
            Toast.makeText(this, "Are user", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Nu are user", Toast.LENGTH_SHORT).show();
        }
        myDB = new MyDatabaseHelper(this);
        movieRv = findViewById(R.id.movie_list_recyclerView);
        movieListAdapter = new MovieListAdapter(this, movieList, this);
        movieRv.setAdapter(movieListAdapter);
        movieRv.setLayoutManager(new LinearLayoutManager(this));
        movieRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        displayAllMovies();
    }

    void displayAllMovies() {
        Cursor cursor = myDB.readAllMovieData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                movie = new Movie(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3));
                movieList.add(movie);
            }
            movieListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnMovieClick(int position) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("frgToLoad", Movie_Fragment);
        i.putExtra("movie",movieList.get(position));
        if (current_user != null) {
            i.putExtra("current_user", current_user);
        }
        startActivity(i);
    }
}