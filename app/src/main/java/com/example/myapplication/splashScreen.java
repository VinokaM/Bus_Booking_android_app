package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

// Define the splashScreen activity class
public class splashScreen extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Set the activity to full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the layout for this activity
        setContentView(R.layout.activity_splash_screen);

        // Use a Handler to delay the transition to the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Create an Intent to start the MainActivity
                Intent i = new Intent(splashScreen.this, MainActivity.class);


                // Start the MainActivity
                startActivity(i);


                // Finish the splashScreen activity to remove it from the back stack
                finish();
            }
        }, 3000); // Delay of 3000 milliseconds (3 seconds)

    }
}