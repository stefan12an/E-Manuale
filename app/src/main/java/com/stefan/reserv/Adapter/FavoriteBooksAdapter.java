package com.stefan.reserv.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.stefan.reserv.BookView;
import com.stefan.reserv.Model.Book;
import com.stefan.reserv.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class FavoriteBooksAdapter extends RecyclerView.Adapter<FavoriteBooksAdapter.MyViewHolder> {

    Context context;
    ArrayList<Book> bookList;
    OnFavoriteBookClickListener onFavoriteBookClickListener;

    public FavoriteBooksAdapter(Context context, ArrayList<Book> bookList, OnFavoriteBookClickListener onFavoriteBookClickListener) {
        this.context = context;
        this.bookList = bookList;
        this.onFavoriteBookClickListener = onFavoriteBookClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.each_favorite_book, parent, false);
        return new MyViewHolder(view, onFavoriteBookClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        if(bookList.get(position).getMovie_poster()!=null) {
//            ByteArrayInputStream imageStream = new ByteArrayInputStream(bookList.get(position).getMovie_poster());
//            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
//            holder.cinema_photo_imv.setImageBitmap(theImage);
//        }else{
//            holder.cinema_photo_imv.setImageResource(R.drawable.book_placeholder);
//        }
        Context context_asset;
        AssetManager assetManager;
        InputStream inputStream = null;
        try { // Initialize context
            context_asset = context.createPackageContext("com.stefan.reserv", 0);
            // using AssetManager class we get assets
            assetManager = context_asset.getAssets();
            Log.e(TAG, "onBindViewHolder: " + Arrays.toString(assetManager.list("PDF/")));
            inputStream = assetManager.open("PDF/" + bookList.get(position).getTitle());
            /* videoFileName is file name that needs to be accessed. Here if you saved assets in sub-directory then access like : "subdirectory/asset-name" */
            // String[] list = assetManager.list(""); // returns entire list of assets in directory
        } catch (PackageManager.NameNotFoundException | IOException e) {
            e.printStackTrace();
        }
        InputStream finalInputStream = inputStream;
        Thread thread = new Thread(() -> holder.cinema_photo_imv.fromStream(finalInputStream).pages(0).onTap(new OnTapListener() {
            @Override
            public boolean onTap(MotionEvent e) {
                Intent i = new Intent(context, BookView.class);
                i.putExtra("movie", bookList.get(holder.getAdapterPosition()));
                context.startActivity(i);
                Log.e(TAG, "onTap: " + bookList.get(holder.getAdapterPosition()).getTitle());
                return true;
            }
        }).load());
        thread.start();

    }

    @Override
    public int getItemCount() {
        return Math.min(bookList.size(), 4);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PDFView cinema_photo_imv;
        OnFavoriteBookClickListener onFavoriteBookClickListener;

        public MyViewHolder(@NonNull View itemView, OnFavoriteBookClickListener onFavoriteBookClickListener) {
            super(itemView);
            cinema_photo_imv = itemView.findViewById(R.id.popular_movie);
            this.onFavoriteBookClickListener = onFavoriteBookClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onFavoriteBookClickListener.OnFavoriteBookClick(getAdapterPosition());
        }
    }

    public interface OnFavoriteBookClickListener {
        void OnFavoriteBookClick(int position);
    }
}
