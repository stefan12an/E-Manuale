package com.stefan.reserv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.stefan.reserv.Database.MyDatabaseHelper;

public class PreLogReg extends AppCompatActivity {
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_log_reg);
        login = findViewById(R.id.go_to_login);
        register = findViewById(R.id.go_to_register);
        login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        register.setOnClickListener(v -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(PreLogReg.this);
            myDB.insertClase();
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }
}