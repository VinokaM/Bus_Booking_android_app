package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class updateBus extends AppCompatActivity {

    // Define UI elements
    private EditText etBusNumber, etStartCity, etEndCity, etSeatCount,etTime;
    private Button btnSave;

    // Define Firebase Authentication and Database Reference
    private FirebaseAuth mAuth;
    private DatabaseReference busesRef;

    private String busId; // For editing existing bus

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bus);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // If no user is logged in, show a message and close the activity
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase Database reference
        busesRef = FirebaseDatabase.getInstance().getReference("buses");

        // Initialize UI elements
        etBusNumber = findViewById(R.id.etBusNumber);
        etStartCity = findViewById(R.id.etStartCity);
        etEndCity = findViewById(R.id.etEndCity);
        etSeatCount = findViewById(R.id.etSeatCount);
        btnSave = findViewById(R.id.btnSave);
        etTime = findViewById(R.id.etTime);


        // Get the bus ID from the intent extras (if editing an existing bus)
        busId = getIntent().getStringExtra("busId");
        if (busId != null) {
            // Editing existing bus
            loadBusDetails();
        }

        // Set the click listener for the save button
        btnSave.setOnClickListener(v -> {
            saveBus();
        });
    }

    // Method to load bus details from the database
    private void loadBusDetails() {
        busesRef.child(busId).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                Bus bus = snapshot.getValue(Bus.class);
                if (bus != null) {
                    // Populate the UI fields with the bus details
                    etBusNumber.setText(bus.getBusNumber());
                    etStartCity.setText(bus.getStartCity());
                    etEndCity.setText(bus.getEndCity());
                    etSeatCount.setText(String.valueOf(bus.getSeatCount()));
                    etTime.setText(bus.getTime());
                }
            }
        }).addOnFailureListener(e -> {
            // Show a message if failed to load bus details
            Toast.makeText(updateBus.this, "Failed to load bus details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    // Method to save or update bus details in the database
    private void saveBus() {
        String busNum = etBusNumber.getText().toString().trim();
        String start = etStartCity.getText().toString().trim();
        String end = etEndCity.getText().toString().trim();
        String seatsStr = etSeatCount.getText().toString().trim();
        String busTime = etTime.getText().toString().trim();

        // Check if any of the fields are empty
        if (TextUtils.isEmpty(busNum) || TextUtils.isEmpty(start) || TextUtils.isEmpty(end) || TextUtils.isEmpty(seatsStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse seat count to integer
        int seats = Integer.parseInt(seatsStr);
        // Get the current user ID
        String userId = mAuth.getCurrentUser().getUid();


        // Create a new Bus object
        Bus bus = new Bus(busId, busNum, start, end, seats, busTime,userId);
        if (busId == null) {
            // If busId is null, we are adding a new bus, generate a new ID
            busId = busesRef.push().getKey();
        }

        // Save the bus details in the database
        busesRef.child(busId).setValue(bus)
                .addOnSuccessListener(aVoid -> {
                    // Show a success message and close the activity
                    Toast.makeText(updateBus.this, "Bus saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Show an error message if failed to save the bus
                    Toast.makeText(updateBus.this, "Failed to save bus: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
