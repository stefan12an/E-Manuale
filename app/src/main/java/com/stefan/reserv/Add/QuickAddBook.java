package com.stefan.reserv.Add;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;
import static android.content.Intent.ACTION_PICK;
import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.stefan.reserv.Adapter.MaterieAdapter;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

import br.com.onimur.handlepathoz.HandlePathOz;
import br.com.onimur.handlepathoz.HandlePathOzListener;
import br.com.onimur.handlepathoz.model.PathOz;

public class QuickAddBook extends AppCompatActivity implements MaterieAdapter.OnMaterieClickListener, HandlePathOzListener.SingleUri {
    private EditText book_name, author_name;
    private Button save_button, pdf;
    private ImageView book_photo;
    public Uri postImageUri;
    private int SELECT_PDF = 12;
    private String SelectedPDF;
    private byte[] photo, byte_pdf;
    private MyDatabaseHelper myDB;
    private ArrayList<String> genreList, selectedGenres;
    private RecyclerView genre_recyclerView;
    private MaterieAdapter genre_adapter;
    private int click_color;
    private int unclick_color;
    private HandlePathOz handlePathOz;
    private static final int REQUEST_PERMISSION = 123;
    private static final int REQUEST_OPEN_GALLERY = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_book);
        handlePathOz = new HandlePathOz(this, this);
        click_color = ContextCompat.getColor(this, R.color.grey_500);
        unclick_color = ContextCompat.getColor(this, R.color.warning_700);
        genreList = new ArrayList<>();
        selectedGenres = new ArrayList<>();
        book_photo = findViewById(R.id.book_photo);
        book_name = findViewById(R.id.book_title);
        author_name = findViewById(R.id.author_name);
        save_button = findViewById(R.id.saveBookToDB);
        pdf = findViewById(R.id.choosePDF);
        myDB = new MyDatabaseHelper(this);
        genre_adapter = new MaterieAdapter(this, genreList, this);
        genre_recyclerView = findViewById(R.id.genre_recyclerView);
        genre_recyclerView.setAdapter(genre_adapter);
        displayGenres();
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(QuickAddBook.this);
                Log.e(TAG, "onClick: " + SelectedPDF);
                myDB.insertBookData(book_name.getText().toString().trim(), photo, author_name.getText().toString().trim());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 800);
            }
        });
        book_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        pdf.setOnClickListener(v -> openFile());
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
        Cursor cursor = myDB.viewMaterii();
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
    public void OnMaterieClick(int position, CardView genre_cv) {
        if (!selectedGenres.contains(genreList.get(position))) {
            selectedGenres.add(genreList.get(position));
            genre_cv.setCardBackgroundColor(click_color);
        } else {
            selectedGenres.remove(genreList.get(position));
            genre_cv.setCardBackgroundColor(unclick_color);
        }
        Log.e(TAG, "OnPopularGenreClick: " + selectedGenres);
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                photo = baos.toByteArray();
                book_photo.setImageURI(postImageUri);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(QuickAddBook.this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                handlePathOz.getRealPath(uri);
            }
        }
    }

    private void openFile() {
        Log.e(TAG, "openFile: " + checkSelfPermission());
        if (checkSelfPermission()) {
            Intent intent;
            Log.e(TAG, "openFile: " + Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                intent = new Intent(ACTION_PICK, EXTERNAL_CONTENT_URI);
            } else {
                intent = new Intent(ACTION_PICK, INTERNAL_CONTENT_URI);
            }

            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra("return-data", true);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            startActivityForResult(intent, REQUEST_OPEN_GALLERY);
        }
    }

    private boolean checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFile();
            } else {
                //TODO("show Message to the user")
            }
        }
    }

    @Override
    public void onRequestHandlePathOz(PathOz pathOz, Throwable throwable) {
        SelectedPDF = pathOz.getPath();
    }
}