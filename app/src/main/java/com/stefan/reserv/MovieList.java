package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
    private String filter_genre;
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
            if(getIntent().hasExtra("filter_genre")){
                filter_genre = bundle.getString("filter_genre");
            }
        }
        Log.e(TAG, "onCreate: Filtered genre = " + filter_genre + ", " + current_user.getRole());
        myDB = new MyDatabaseHelper(this);
        movieRv = findViewById(R.id.movie_list_recyclerView);
        movieListAdapter = new MovieListAdapter(this, movieList, this);
        if(current_user.getRole().equals("admin")) {
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(movieRv);
        }
        movieRv.setAdapter(movieListAdapter);
        movieRv.setLayoutManager(new LinearLayoutManager(this));
        movieRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        displayAllMovies();
    }

    void displayAllMovies() {
        Cursor cursor = myDB.readAllMovieData(filter_genre);
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
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnMovieClick(int position) {
        Intent i = new Intent(this, MovieView.class);
        i.putExtra("movie", movieList.get(position));
        if (current_user != null) {
            i.putExtra("current_user", current_user);
        }
        startActivity(i);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    myDB.deleteSpecificMovie(movieList.get(viewHolder.getAdapterPosition()));
                    movieList.remove(viewHolder.getAdapterPosition());
                    movieListAdapter.notifyDataSetChanged();
                }
            };
}