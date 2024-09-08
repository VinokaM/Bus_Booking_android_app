package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {

    // Declare ImageView variables for different buttons
    ImageView btnBookBus, btnSearch, btnAddBus, btnYourBus, btnLocation, btnFeedback;
    TextView userEmail; // Added TextView for user email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page); // Set the layout for this activity

        userEmail = findViewById(R.id.userEmail); // Initialize TextView

        // Initialize ImageView variables by finding them by their IDs
        btnAddBus = findViewById(R.id.btnAddBus);
        btnSearch = findViewById(R.id.btnSearch);
        btnBookBus = findViewById(R.id.btnBookBus);
        btnYourBus = findViewById(R.id.btnYourBus);
        btnLocation = findViewById(R.id.btnLocation);
        btnFeedback = findViewById(R.id.btnFeedback);

        // Fetch and display user email
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            userEmail.setText(email);// Set the user email in the TextView
        }


        // Set onClick listeners for each ImageView to navigate to respective activities
        btnAddBus.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddBus.class);
            startActivity(intent);
            finish();
        });

        btnSearch.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AllBus.class);
            startActivity(intent);
            finish();
        });

        btnBookBus.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), searchBus.class);
            startActivity(intent);
            finish();
        });

        btnYourBus.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), usersBus.class);
            startActivity(intent);
            finish();
        });

        btnLocation.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Location.class);
            startActivity(intent);
            finish();
        });

        btnFeedback.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), feedback.class);
            startActivity(intent);
            finish();
        });
    }
}
