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
    OnBookClickListener onBookClickListener;
    public BookListAdapter(Context context, ArrayList<Book> bookList, OnBookClickListener onBookClickListener) {
        this.context = context;
        this.bookList = bookList;
        this.onBookClickListener = onBookClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.each_book, parent, false);
        return new MyViewHolder(view, onBookClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(bookList.get(position).getMovie_poster()!=null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(bookList.get(position).getMovie_poster());
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            holder.movie_poster.setImageBitmap(theImage);
        }else{
            holder.movie_poster.setImageResource(R.drawable.popular_4);
        }
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
        OnBookClickListener onBookClickListener;
        public MyViewHolder(@NonNull View itemView, OnBookClickListener onBookClickListener) {
            super(itemView);
            this.onBookClickListener = onBookClickListener;
            movie_poster = itemView.findViewById(R.id.book_card_poster);
            movie_title = itemView.findViewById(R.id.book_card_title);
            movie_date = itemView.findViewById(R.id.book_card_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBookClickListener.OnBookClick(getAdapterPosition());
        }
    }
    public interface OnBookClickListener {
        void OnBookClick(int position);
    }
}
