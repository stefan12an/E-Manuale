package com.stefan.reserv.Add;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.stefan.reserv.Adapter.GenreAdapter;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.MainActivity;
import com.stefan.reserv.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

public class QuickAddBook extends AppCompatActivity implements GenreAdapter.OnGenreClickListener {
    private EditText movie_name;
    private Button save_button, del_button;
    private ImageView movie_poster;
    public Uri postImageUri;
    private byte[] photo;
    private MyDatabaseHelper myDB;
    private ArrayList<String> genreList, selectedGenres;
    private RecyclerView genre_recyclerView;
    private GenreAdapter genre_adapter;
    private int click_color;
    private int unclick_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_book);
        click_color = ContextCompat.getColor(this, R.color.grey_500);
        unclick_color = ContextCompat.getColor(this, R.color.warning_700);
        genreList = new ArrayList<>();
        selectedGenres = new ArrayList<>();
        movie_poster = findViewById(R.id.movie_photo);
        movie_name = findViewById(R.id.movie_title);
        save_button = findViewById(R.id.saveMovieToDB);
        del_button = findViewById(R.id.delete_movies_data);
        myDB = new MyDatabaseHelper(this);
        genre_adapter = new GenreAdapter(this, genreList, this);
        genre_recyclerView = findViewById(R.id.genre_recyclerView);
        genre_recyclerView.setAdapter(genre_adapter);
        displayGenres();
        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(QuickAddBook.this);
                myDB.deleteGradeData();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(QuickAddBook.this, MainActivity.class));
                        finish();
                    }
                }, 500);
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(QuickAddBook.this);
                myDB.insertBookData(movie_name.getText().toString().trim(), photo);
                myDB.insertBookGenreData(movie_name.getText().toString().trim(), selectedGenres);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 800);
            }
        });
        movie_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        Intent intent = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(320, 566)
                .setMinCropWindowSize(320, 566)
                .getIntent(QuickAddBook.this);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = QuickAddBook.this.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void displayGenres() {
        Cursor cursor = myDB.readAllGenreData();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                postImageUri = result.getUri();
                try {
                    bitmap = getBitmapFromUri(postImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                photo = baos.toByteArray();
                movie_poster.setImageURI(postImageUri);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(QuickAddBook.this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void OnGenreClick(int position, CardView genre_cv) {
        if (!selectedGenres.contains(genreList.get(position))) {
            selectedGenres.add(genreList.get(position));
            genre_cv.setCardBackgroundColor(click_color);
        } else {
            selectedGenres.remove(genreList.get(position));
            genre_cv.setCardBackgroundColor(unclick_color);
        }
        Log.e(TAG, "OnPopularGenreClick: " + selectedGenres);
    }
}