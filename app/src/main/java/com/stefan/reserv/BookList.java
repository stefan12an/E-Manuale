package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.stefan.reserv.Adapter.BookListAdapter;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.Book;
import com.stefan.reserv.Model.User;

import java.util.ArrayList;

public class BookList extends AppCompatActivity implements BookListAdapter.OnBookClickListener {
    private ArrayList<Book> bookList;
    private BookListAdapter bookListAdapter;
    private RecyclerView bookRv;
    private MyDatabaseHelper myDB;
    private Book book;
    private User current_user;
    private String filter_materie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        bookList = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra("current_user")) {
            Bundle bundle = getIntent().getExtras();
            current_user = bundle.getParcelable("current_user");
            if (getIntent().hasExtra("filter_materie")) {
                filter_materie = bundle.getString("filter_materie");
            }
        }
        Log.e(TAG, "onCreate: Filtered genre = " + filter_materie + ", " + current_user.getRole());
        myDB = new MyDatabaseHelper(this);
        bookRv = findViewById(R.id.book_list_recyclerView);
        bookListAdapter = new BookListAdapter(this, bookList, this);
        if (current_user.getRole().equals("admin")) {
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(bookRv);
        }
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.devider));
        bookRv.setAdapter(bookListAdapter);
        bookRv.setLayoutManager(new LinearLayoutManager(this));
        bookRv.addItemDecoration(dividerItemDecoration);
        displayAllMovies();
    }

    void displayAllMovies() {
        Cursor cursor = myDB.readAllBookData(filter_materie, current_user);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                book = new Book(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getString(4), Boolean.getBoolean(cursor.getString(5)), cursor.getString(6), cursor.getString(7));
                bookList.add(book);
            }
            bookListAdapter.notifyDataSetChanged();
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
    public void OnBookClick(int position) {
        Intent i = new Intent(this, BookView.class);
        i.putExtra("movie", bookList.get(position));
        Log.e(TAG, "OnMovieClick " + bookList.get(position).getTitle());
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
                    myDB.deleteSpecificBook(bookList.get(viewHolder.getAdapterPosition()));
                    bookList.remove(viewHolder.getAdapterPosition());
                    bookListAdapter.notifyDataSetChanged();
                }
            };
}