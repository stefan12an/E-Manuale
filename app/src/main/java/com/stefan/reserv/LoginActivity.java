package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.User;

public class LoginActivity extends AppCompatActivity {
    TextView goto_register;
    EditText login_email, login_password;
    Button login_send;
    MyDatabaseHelper myDB;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        goto_register = findViewById(R.id.jmp_register);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_send = findViewById(R.id.login_button);
        myDB = new MyDatabaseHelper(LoginActivity.this);
        login_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString().trim();
                String pass = login_password.getText().toString().trim();
                Cursor cursor = myDB.checkUserCredentials(email, pass);
                if (cursor != null) {
                    if (cursor.getCount() == 1) {
                        while (cursor.moveToNext()) {
                            user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        if (user != null) {
                            intent.putExtra("login_current_user", user);
                        }
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "This user is not registered", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, login_email.getText().toString().trim() + " " + login_password.getText().toString().trim());
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        goto_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }
}