package com.stefan.reserv.Adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.core.widgets.Rectangle;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.Book;
import com.stefan.reserv.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
//        if(bookList.get(position).getMovie_poster()!=null) {
//            ByteArrayInputStream imageStream = new ByteArrayInputStream(bookList.get(position).getMovie_poster());
//            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
//            holder.movie_poster.setImageBitmap(theImage);
//        }else{
//            holder.movie_poster.setImageResource(R.drawable.book_placeholder);
//        }
        MyDatabaseHelper myDB = new MyDatabaseHelper(context);
        Context context_asset;
        AssetManager assetManager;
        InputStream inputStream = null;
        try { // Initialize context
            context_asset = context.createPackageContext("com.stefan.reserv", 0);
            // using AssetManager class we get assets
            assetManager = context_asset.getAssets();
            inputStream = assetManager.open("PDF/" + bookList.get(position).getTitle());
            /* videoFileName is file name that needs to be accessed. Here if you saved assets in sub-directory then access like : "subdirectory/asset-name" */
            // String[] list = assetManager.list(""); // returns entire list of assets in directory
        } catch (PackageManager.NameNotFoundException | IOException e) {
            e.printStackTrace();
        }
        holder.movie_poster.fromStream(inputStream).pages(0).load();
        Cursor cursor = myDB.viewMaterie(Integer.parseInt(bookList.get(position).getId_materie()));
        int clasa = Integer.parseInt(bookList.get(position).getId_clasa()) - 1;
        while (cursor.moveToNext()) {
            holder.movie_title.setText(cursor.getString(1));
        }
        holder.book_clasa.setText("Clasa a " + clasa + "-a");
        holder.movie_date.setText(bookList.get(position).getRelease_date());
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PDFView movie_poster;
        TextView movie_title,movie_date, book_clasa;
        OnBookClickListener onBookClickListener;
        public MyViewHolder(@NonNull View itemView, OnBookClickListener onBookClickListener) {
            super(itemView);
            this.onBookClickListener = onBookClickListener;
            movie_poster = itemView.findViewById(R.id.book_card_poster);
            movie_title = itemView.findViewById(R.id.book_card_title);
            movie_date = itemView.findViewById(R.id.book_card_date);
            book_clasa = itemView.findViewById(R.id.book_card_clasa);
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
