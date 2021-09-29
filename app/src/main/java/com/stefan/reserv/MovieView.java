package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stefan.reserv.Adapter.GenreAdapter;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.Movie;
import com.stefan.reserv.Model.User;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class MovieView extends AppCompatActivity implements GenreAdapter.OnGenreClickListener {
    private Movie selected_movie;
    private TextView movie_title,movie_date,movie_description;
    private ImageView movie_poster;
    private User current_user;
    private MyDatabaseHelper myDB;
    private ArrayList<String> genreList;
    private RecyclerView genre_recyclerView;
    private GenreAdapter genre_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        genreList = new ArrayList<>();
        movie_title = findViewById(R.id.movie_preview_title);
        movie_date = findViewById(R.id.movie_preview_date);
        movie_description = findViewById(R.id.movie_preview_description);
        movie_poster = findViewById(R.id.movie_preview_poster);
        genre_adapter = new GenreAdapter(this, genreList, this);
        genre_recyclerView = findViewById(R.id.genre_recyclerView);
        genre_recyclerView.setAdapter(genre_adapter);
        myDB = new MyDatabaseHelper(this);
        if (getIntent().hasExtra("movie")) {
            Bundle bundle = getIntent().getExtras();
            if(getIntent().hasExtra("current_user")) {
                current_user = bundle.getParcelable("current_user");
            }
            selected_movie = bundle.getParcelable("movie");
            Log.e(TAG, "onCreate: " + current_user.getRole() + ", " + selected_movie.getTitle());
            movie_title.setText(selected_movie.getTitle());
            movie_date.setText(selected_movie.getRelease_date());
            ByteArrayInputStream imageStream = new ByteArrayInputStream(selected_movie.getMovie_poster());
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            movie_poster.setImageBitmap(theImage);
        }
        displayMovieSpecificGenres();
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

    public void displayMovieSpecificGenres() {
        Cursor cursor = myDB.readAllMovieSpecificGenreData(selected_movie);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No genres.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                genreList.add(cursor.getString(1));
            }
            genre_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnGenreClick(int position) {
        Intent i = new Intent(this, MovieList.class);
        i.putExtra("current_user", current_user);
        i.putExtra("filter_genre", genreList.get(position));
        startActivity(i);
    }
}