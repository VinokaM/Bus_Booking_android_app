// SearchBusActivity.java
package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Define the searchBus activity class
public class searchBus extends AppCompatActivity {

    // Declare UI elements
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvSearchResults;
    private TextView tvNoResults;

    // Firebase database reference and adapter for RecyclerView
    private DatabaseReference databaseReference;
    private BusAdapter busAdapter;
    private List<Bus> busList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus);// Set the layout for this activity

        // Initialize UI elements
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        tvNoResults = findViewById(R.id.tvNoResults);

        // Configure RecyclerView
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));// Set layout manager
        busList = new ArrayList<>();// Initialize list to hold search results
        busAdapter = new BusAdapter(busList);// Create adapter for RecyclerView
        rvSearchResults.setAdapter(busAdapter);// Set adapter to RecyclerView

        // Firebase database reference to "buses" node
        databaseReference = FirebaseDatabase.getInstance().getReference("buses");

        // Set click listener for search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBuses();// Call method to perform bus search
            }
        });
    }

    // Method to search for buses based on user input
    private void searchBuses() {
        String searchText = etSearch.getText().toString().trim(); // Get search text from EditText

        if (TextUtils.isEmpty(searchText)) {
            Toast.makeText(this, "Please enter bus number or start city", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query Firebase database for buses matching search criteria
        Query query = databaseReference.orderByChild("startCity").equalTo(searchText);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                busList.clear();// Clear previous search results
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bus bus = snapshot.getValue(Bus.class); // Convert each snapshot to Bus object
                    if (bus != null) {
                        busList.add(bus);// Add bus to search results list
                    }
                }
                if (busList.isEmpty()) {
                    tvNoResults.setVisibility(View.VISIBLE);
                    rvSearchResults.setVisibility(View.GONE);
                } else {
                    tvNoResults.setVisibility(View.GONE);
                    rvSearchResults.setVisibility(View.VISIBLE);
                    busAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(searchBus.this, "Failed to search buses: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


    }

    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(searchBus.this, HomePage.class);
        startActivity(intent);
        finish();
    }
}
