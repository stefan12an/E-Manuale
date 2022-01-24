package com.stefan.reserv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;

import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Utils.PreferenceUtils;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceUtils.getEmail(SplashActivity.this) != null && !PreferenceUtils.getEmail(SplashActivity.this).equals("")) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, PreLogReg.class));
                }
                finish();
            }
        }, 1000);
    }
}