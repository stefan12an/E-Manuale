package com.stefan.reserv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stefan.reserv.Database.MyDatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    TextView goto_login;
    EditText register_email, register_password;
    Button register_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        goto_login = findViewById(R.id.jmp_login);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_send = findViewById(R.id.register_button);
        register_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = register_email.getText().toString().trim();
                String pass = register_password.getText().toString().trim();
                if (!email.isEmpty() && !pass.isEmpty()) {
                    if (isValidEmail(email)) {
                        MyDatabaseHelper userDB = new MyDatabaseHelper(RegisterActivity.this);
                        if (!userDB.checkForDuplicateAccount(email, pass)) {
                            userDB.insertUserData(email, pass);
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "There is already an account registered to this email address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Please enter a valid e-mail address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please enter an e-mail and a password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}