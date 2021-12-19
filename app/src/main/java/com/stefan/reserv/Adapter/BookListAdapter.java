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

import com.stefan.reserv.Model.Book;
import com.stefan.reserv.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.MyViewHolder> {

    Context context;
    ArrayList<Book> bookList;
    OnMovieClickListener onMovieClickListener;
    public BookListAdapter(Context context, ArrayList<Book> bookList, OnMovieClickListener onMovieClickListener) {
        this.context = context;
        this.bookList = bookList;
        this.onMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.each_book, parent, false);
        return new MyViewHolder(view, onMovieClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ByteArrayInputStream imageStream = new ByteArrayInputStream(bookList.get(position).getMovie_poster());
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        holder.movie_poster.setImageBitmap(theImage);
        holder.movie_title.setText(bookList.get(position).getTitle());
        holder.movie_date.setText(bookList.get(position).getRelease_date());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
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
