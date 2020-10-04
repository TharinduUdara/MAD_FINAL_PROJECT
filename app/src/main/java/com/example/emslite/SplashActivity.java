package com.example.emslite;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sharedPreferences = getApplicationContext().getSharedPreferences("emsLite", MODE_PRIVATE);

        final boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (isLoggedIn) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}