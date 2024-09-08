package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBus extends AppCompatActivity {

    // Declare UI elements and Firebase references
    private EditText busNumber, startCity, endCity, seatCount, time;
    private Button submitButton;
    private FirebaseDatabase database;
    private DatabaseReference busesRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        // Initialize UI elements
        busNumber = findViewById(R.id.busNumber);
        startCity = findViewById(R.id.startCity);
        endCity = findViewById(R.id.endCity);
        seatCount = findViewById(R.id.SeatCount);
        time = findViewById(R.id.time);
        submitButton = findViewById(R.id.btnadd);

        // Initialize Firebase instances
        database = FirebaseDatabase.getInstance();
        busesRef = database.getReference("buses");
        mAuth = FirebaseAuth.getInstance();

        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user is logged in
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    addBus();// If logged in, proceed to add bus
                } else {
                    Toast.makeText(AddBus.this, "Please log in to continue", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to add bus information to the database
    private void addBus() {
        // Get input values from the UI
        String busNum = busNumber.getText().toString().trim();
        String start = startCity.getText().toString().trim();
        String end = endCity.getText().toString().trim();
        String seatsStr = seatCount.getText().toString().trim();
        String busTime = time.getText().toString().trim();

        // Check if any field is empty
        if (TextUtils.isEmpty(busNum) || TextUtils.isEmpty(start) || TextUtils.isEmpty(end) || TextUtils.isEmpty(seatsStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert seat count to integer and handle potential number format exception
        int seats;
        try {
            seats = Integer.parseInt(seatsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid seat count", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get user ID and generate a unique key for the bus entry
        String userId = mAuth.getCurrentUser().getUid();
        String busId = busesRef.push().getKey();
        Bus bus = new Bus(busId, busNum, start, end, seats, busTime, userId);

        // Add the bus information to the Firebase database
        busesRef.child(busId).setValue(bus).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddBus.this, "Bus added successfully!", Toast.LENGTH_SHORT).show();
                // Navigate to HomePage activity
                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(AddBus.this, "Failed to add bus", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Override the back button press to navigate to HomePage activity
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(AddBus.this, HomePage.class);
        startActivity(intent);
        finish();
    }


}
