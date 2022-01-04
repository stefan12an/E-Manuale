package com.stefan.reserv.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stefan.reserv.Adapter.MaterieAdapter;
import com.stefan.reserv.Adapter.FavoriteBooksAdapter;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.Book;
import com.stefan.reserv.Model.User;
import com.stefan.reserv.BookList;
import com.stefan.reserv.BookView;
import com.stefan.reserv.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment implements FavoriteBooksAdapter.OnFavoriteBookClickListener, MaterieAdapter.OnMaterieClickListener {
    private TextView see_more_btn;
    private ImageView carousel_iv;
    private RecyclerView movie_recyclerView, genre_recyclerView;
    private MyDatabaseHelper myDB;
    private FavoriteBooksAdapter movie_adapter;
    private MaterieAdapter genre_adapter;
    private ArrayList<Book> bookList;
    private ArrayList<String> genreList;
    private Book book = null;
    private User current_user;
    private Button add_books;
    private String[] files;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bookList = new ArrayList<>();
        genreList = new ArrayList<>();
        myDB = new MyDatabaseHelper(getContext());
        add_books = view.findViewById(R.id.add_books);
        see_more_btn = view.findViewById(R.id.see_more_btn);
        carousel_iv = view.findViewById(R.id.carousel_iv);
        movie_adapter = new FavoriteBooksAdapter(getContext(), bookList, this);
        movie_recyclerView = view.findViewById(R.id.popular_movies);
        movie_recyclerView.setAdapter(movie_adapter);
        movie_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            current_user = bundle.getParcelable("current_user");
        }
        genre_adapter = new MaterieAdapter(getContext(), genreList, this);
        genre_recyclerView = view.findViewById(R.id.genre_recyclerView);
        genre_recyclerView.setAdapter(genre_adapter);
        AnimationDrawable animationDrawable = (AnimationDrawable) carousel_iv.getDrawable();
        animationDrawable.start();
        see_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), BookList.class);
                i.putExtra("current_user", current_user);
                startActivity(i);
            }
        });
        add_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssetManager assetManager = getContext().getAssets();
                try {
                    files = assetManager.list("PDF");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "onClick: " + Arrays.toString(files));
                myDB.insertBooks(files,"Matematica", "7");
            }
        });
        return view;
    }

    public void displayFavoriteBooks() {
        Cursor cursor = myDB.readAllBookData(null, current_user);
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No popular movies.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                book = new Book(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getString(4), Boolean.getBoolean(cursor.getString(5)), cursor.getString(6), cursor.getString(7));
                bookList.add(book);
            }
            movie_adapter.notifyDataSetChanged();
        }
    }

    public void displayMaterii() {
        Cursor cursor = myDB.viewMaterii();
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
        bookList.clear();
        genreList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayFavoriteBooks();
        displayMaterii();
    }

    @Override
    public void OnFavoriteBookClick(int position) {
        Intent i = new Intent(getContext(), BookView.class);
        i.putExtra("movie", bookList.get(position));
        if (current_user != null) {
            i.putExtra("current_user", current_user);
        }
        startActivity(i);

    }


    @Override
    public void OnMaterieClick(int position, CardView genre_cv) {
        Intent i = new Intent(getContext(), BookList.class);
        i.putExtra("current_user", current_user);
        i.putExtra("filter_materie", genreList.get(position));
        startActivity(i);
    }
}