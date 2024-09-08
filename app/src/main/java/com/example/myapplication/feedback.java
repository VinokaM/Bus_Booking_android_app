package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class feedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);// Set the layout for this activity
    }

    // Override the back button press to navigate to HomePage activity
    @Override
    public void onBackPressed() {

        super.onBackPressed();// Call the super class method
        Intent intent = new Intent(feedback.this, HomePage.class);// Create an intent to navigate to HomePage
        startActivity(intent); // Start the HomePage activity
        finish();// Finish the current activity
    }
}