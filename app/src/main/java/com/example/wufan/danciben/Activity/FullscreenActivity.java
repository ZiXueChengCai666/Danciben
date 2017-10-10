package com.example.wufan.danciben.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.example.wufan.danciben.R;


public class FullscreenActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private final Handler mHideHandler = new Handler();


    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            goHome();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mHideHandler.postDelayed(mHideRunnable,AUTO_HIDE_DELAY_MILLIS);

    }

    private void goHome(){
        Intent intent = new Intent(FullscreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
