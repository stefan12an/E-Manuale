package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.User;
import com.stefan.reserv.Utils.PreferenceUtils;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextView goto_register;
    private TextInputEditText login_email, login_password;
    private Button login_send;
    private MyDatabaseHelper myDB;
    private User user;

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
                String email = Objects.requireNonNull(login_email.getText()).toString().trim();
                String pass = Objects.requireNonNull(login_password.getText()).toString().trim();
                Cursor cursor = myDB.checkUserCredentials(email, pass);
                if (cursor != null) {
                    if (cursor.getCount() == 1) {
                        while (cursor.moveToNext()) {
                            if (cursor.getBlob(5) == null) {
                                user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(6));
                            } else {
                                user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getBlob(5), cursor.getString(6));
                            }
                            PreferenceUtils.saveId(user.getId(), LoginActivity.this);
                            PreferenceUtils.saveUsername(user.getUsername(), LoginActivity.this);
                            PreferenceUtils.saveEmail(user.getEmail(), LoginActivity.this);
                            PreferenceUtils.savePassword(user.getPassword(), LoginActivity.this);
                            PreferenceUtils.saveRole(user.getRole(), LoginActivity.this);
                            PreferenceUtils.saveGradeId(user.getId_clasa(), LoginActivity.this);
                            if (user.getProfile_pic() != null) {
                                PreferenceUtils.savePic(user.getProfile_pic(), LoginActivity.this);
                            }
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        if (user != null) {
                            intent.putExtra("login_current_user", user);
                        }
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Nu există un cont cu această adresă de e-mail sau parolă", Toast.LENGTH_SHORT).show();
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