package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllBus extends AppCompatActivity {

    // Declare RecyclerView, Adapter, and a list to hold bus data
    private RecyclerView recyclerView;
    private BusAdapter busAdapter;
    private List<Bus> busList;

    // Declare a reference to the Firebase database
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bus);

        // Initialize RecyclerView and set its layout manager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize bus list and adapter, and set the adapter to the RecyclerView
        busList = new ArrayList<>();
        busAdapter = new BusAdapter(busList);
        recyclerView.setAdapter(busAdapter);

        // Reference to Firebase "buses" node
        databaseReference = FirebaseDatabase.getInstance().getReference("buses");

        // Listener to fetch and display bus data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the bus list to avoid duplication
                busList.clear();
                // Iterate over all children in the "buses" node and add each bus to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bus bus = snapshot.getValue(Bus.class);
                    if (bus != null) {
                        busList.add(bus);
                    }
                }

                // Notify the adapter that the data has changed
                busAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Display a toast message if data retrieval fails
                Toast.makeText(AllBus.this, "Failed to retrieve buses: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Override the back button press to navigate to HomePage activity
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(AllBus.this, HomePage.class);
        startActivity(intent);
        finish();
    }
}
