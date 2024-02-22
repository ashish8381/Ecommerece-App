package com.demo.ecommereceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intet = new Intent(SplashActivity.this, MainActivity.class);
                intet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intet);
                finish();
            }
        }, 3000);
    }
}