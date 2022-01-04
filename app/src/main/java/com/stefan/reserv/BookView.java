package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stefan.reserv.Adapter.MaterieAdapter;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.Book;
import com.stefan.reserv.Model.User;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class BookView extends AppCompatActivity implements MaterieAdapter.OnMaterieClickListener {
    private Book selected_book;
    private TextView movie_title, movie_date, movie_description;
    private ImageView movie_poster;
    private User current_user;
    private MyDatabaseHelper myDB;
    private ArrayList<String> genreList;
    private RecyclerView genre_recyclerView;
    private MaterieAdapter genre_adapter;
    private Button view_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        genreList = new ArrayList<>();
        view_pdf = findViewById(R.id.view_pdf);
        movie_title = findViewById(R.id.book_preview_title);
        movie_date = findViewById(R.id.book_preview_date);
        movie_description = findViewById(R.id.book_preview_description);
        movie_poster = findViewById(R.id.book_preview_poster);
        genre_adapter = new MaterieAdapter(this, genreList, this);
        genre_recyclerView = findViewById(R.id.genre_recyclerView);
        genre_recyclerView.setAdapter(genre_adapter);
        myDB = new MyDatabaseHelper(BookView.this);
        if (getIntent().hasExtra("movie")) {
            Bundle bundle = getIntent().getExtras();
            if (getIntent().hasExtra("current_user")) {
                current_user = bundle.getParcelable("current_user");
            }
            selected_book = bundle.getParcelable("movie");
            Log.e(TAG, "onCreate: " + current_user.getRole() + ", " + selected_book.getTitle());
            movie_title.setText(selected_book.getTitle());
            movie_date.setText(selected_book.getRelease_date());
            if(selected_book.getMovie_poster()!=null) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(selected_book.getMovie_poster());
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                movie_poster.setImageBitmap(theImage);
            }else{
                movie_poster.setImageResource(R.drawable.popular_4);
            }
        }
        displayMovieSpecificGenres();
        view_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookView.this, pdf_view.class);
                i.putExtra("selected_book", selected_book);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    public void displayMovieSpecificGenres() {
        Cursor cursor = myDB.readAllBookSpecificGenreData(selected_book);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No genres.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                genreList.add(cursor.getString(0));
            }
            genre_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnMaterieClick(int position, CardView genre_cv) {
        Intent i = new Intent(this, BookList.class);
        i.putExtra("current_user", current_user);
        i.putExtra("filter_materie", genreList.get(position));
        startActivity(i);
        finish();
    }
}