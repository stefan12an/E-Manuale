package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
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

import com.github.barteksc.pdfviewer.PDFView;
import com.stefan.reserv.Adapter.MaterieAdapter;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.Book;
import com.stefan.reserv.Model.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class BookView extends AppCompatActivity{
    private Book selected_book;
    private TextView movie_title, movie_date, book_clasa;
    private PDFView movie_poster;
    private MyDatabaseHelper myDB;
    private Button view_pdf;
    private Boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        view_pdf = findViewById(R.id.view_pdf);
        movie_title = findViewById(R.id.book_preview_title);
        movie_date = findViewById(R.id.book_preview_date);
        movie_poster = findViewById(R.id.book_preview_poster);
        book_clasa = findViewById(R.id.book_preview_clasa);
        myDB = new MyDatabaseHelper(BookView.this);
        if (getIntent().hasExtra("movie")) {
            Bundle bundle = getIntent().getExtras();
            selected_book = bundle.getParcelable("movie");
            clicked = selected_book.getFavorit();
            Log.e(TAG, "onCreate: " + selected_book.getFavorit());
//            Log.e(TAG, "onCreate: " + current_user.getRole() + ", " + selected_book.getTitle());
            Cursor cursor = myDB.viewMaterie(Integer.parseInt(selected_book.getId_materie()));
            int clasa = Integer.parseInt(selected_book.getId_clasa()) - 1;
            while(cursor.moveToNext()) {
                String materie = cursor.getString(1);
                movie_title.setText(materie);
                book_clasa.setText( "Clasa a " + clasa + "-a");
            }
            movie_date.setText(selected_book.getRelease_date());
            Context context_asset;
            AssetManager assetManager;
            InputStream inputStream = null;
            try { // Initialize context
                context_asset = this.createPackageContext("com.stefan.reserv", 0);
                // using AssetManager class we get assets
                assetManager = context_asset.getAssets();
                inputStream = assetManager.open("PDF/" + selected_book.getTitle());
                /* videoFileName is file name that needs to be accessed. Here if you saved assets in sub-directory then access like : "subdirectory/asset-name" */
                // String[] list = assetManager.list(""); // returns entire list of assets in directory
            } catch (PackageManager.NameNotFoundException | IOException e) {
                e.printStackTrace();
            }
            movie_poster.fromStream(inputStream).pages(0).load();
        }
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
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.add_favorite:
                if (!clicked) {
                    item.setIcon(R.drawable.ic_favorite);
                    clicked = true;
                } else {
                    item.setIcon(R.drawable.ic_no_favorite);
                    clicked = false;
                }
                myDB.updateFavorite(selected_book.getId(), clicked);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.book_options, menu);
        if (clicked) {
            menu.getItem(0).setIcon(R.drawable.ic_favorite);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_no_favorite);
        }
        return true;
    }
}