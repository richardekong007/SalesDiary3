package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 6/14/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;



public class splashScreen extends AppCompatActivity {
    //splash time constant declaration
    private final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //create a handler to display the splash screen for 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //create an intent to the sign up activity
                Intent intent = new Intent(splashScreen.this, signUp.class);
                //start the signup activity after SPLASH_TIME
                startActivity(intent);
                //close the splash screen activity
                finish();
            }
        }, SPLASH_TIME);
    }
}

