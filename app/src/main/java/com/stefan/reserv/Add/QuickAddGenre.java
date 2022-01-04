package com.stefan.reserv.Add;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.MainActivity;
import com.stefan.reserv.R;

public class QuickAddGenre extends AppCompatActivity {
    private EditText genre_name;
    private Button save_button, del_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_genre);
        genre_name = findViewById(R.id.genre_title);
        save_button = findViewById(R.id.saveGenreToDB);
        del_button = findViewById(R.id.delete_genre_data);

        del_button.setOnClickListener(v -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(QuickAddGenre.this);
            myDB.deleteMaterii();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                startActivity(new Intent(QuickAddGenre.this, MainActivity.class));
                finish();
            }, 500);
        });

        save_button.setOnClickListener(v -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(QuickAddGenre.this);
            if (myDB.insertMaterie(genre_name.getText().toString().trim())) {
                Handler handler = new Handler();
                handler.postDelayed(this::finish, 800);
            }
        });
    }
}