/*
*   Contributed by Saurabh Dewangan
*   17CO140
*/

package com.example.sdew021.friendsofframers;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class animActivity extends AppCompatActivity {



    private int SPLASH_TIME_OUT = 1750;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(animActivity.this,MainActivity.class));
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
