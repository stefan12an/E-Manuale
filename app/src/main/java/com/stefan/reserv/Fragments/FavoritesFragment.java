package com.stefan.reserv.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stefan.reserv.Adapter.BookListAdapter;
import com.stefan.reserv.BookView;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.Book;
import com.stefan.reserv.Model.User;
import com.stefan.reserv.R;

import java.util.ArrayList;
import java.util.Objects;

public class FavoritesFragment extends Fragment implements BookListAdapter.OnBookClickListener {
    private ArrayList<Book> bookList;
    private BookListAdapter bookListAdapter;
    private RecyclerView bookRv;
    private MyDatabaseHelper myDB;
    private Book book;
    private User current_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        bookList = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            current_user = bundle.getParcelable("current_user");
        }
        myDB = new MyDatabaseHelper(getContext());
        bookRv = view.findViewById(R.id.favorite_book_list);
        bookListAdapter = new BookListAdapter(getContext(), bookList, this);
        if (current_user.getRole().equals("admin")) {
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(bookRv);
        }
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.devider));
        bookRv.setAdapter(bookListAdapter);
        bookRv.setLayoutManager(new LinearLayoutManager(getContext()));
        bookRv.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        bookList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayAllMovies();
    }

    void displayAllMovies() {
        Cursor cursor = myDB.readAllBookData(null, current_user);
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Boolean favorit;
                if (Integer.parseInt(cursor.getString(5)) == 1) {
                    favorit = true;
                } else {
                    favorit = false;
                }
                book = new Book(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getString(4), favorit, cursor.getString(6), cursor.getString(7));
                if (book.getFavorit()) {
                    bookList.add(book);
                }
            }
            bookListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnBookClick(int position) {
        Intent i = new Intent(getContext(), BookView.class);
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