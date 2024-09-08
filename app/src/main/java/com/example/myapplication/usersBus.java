// UserBusesActivity.java
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class usersBus extends AppCompatActivity {

    // Define UI elements and Firebase references
    private RecyclerView recyclerViewBuses;
    private BusAdapter busAdapter;
    private List<Bus> busList;

    private DatabaseReference busesRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_bus);

        // Initialize RecyclerView and set its layout manager
        recyclerViewBuses = findViewById(R.id.recyclerViewBuses);
        recyclerViewBuses.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase Auth and check if user is logged in
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase Database reference
        busesRef = FirebaseDatabase.getInstance().getReference("buses");

        // Initialize bus list and adapter, set the adapter to the RecyclerView
        busList = new ArrayList<>();
        busAdapter = new BusAdapter(busList);
        recyclerViewBuses.setAdapter(busAdapter);

        // Fetch buses for the current user
        fetchUserBuses(currentUser.getUid());

        // Set item click listener for the adapter
        busAdapter.setOnItemClickListener(new BusAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bus bus = busList.get(position);
                showOptionsDialog(bus);
            }
        });
    }

    // Method to fetch buses for the current user from the database
    private void fetchUserBuses(String userId) {
        Query query = busesRef.orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                busList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bus bus = snapshot.getValue(Bus.class);
                    if (bus != null) {
                        busList.add(bus);
                    }
                }
                busAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(usersBus.this, "Failed to retrieve buses: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to show options dialog for a selected bus
    private void showOptionsDialog(final Bus bus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bus Options");
        builder.setItems(new CharSequence[]{"Edit Bus", "Delete Bus"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Edit Bus
                    Intent intent = new Intent(usersBus.this, updateBus.class);
                    intent.putExtra("busId", bus.getBusId());
                    startActivity(intent);
                    break;
                case 1:
                    // Delete Bus
                    showDeleteConfirmationDialog(bus);
                    break;
            }
        });
        builder.create().show();
    }

    // Method to show delete confirmation dialog
    private void showDeleteConfirmationDialog(final Bus bus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Bus");
        builder.setMessage("Are you sure you want to delete this bus?");
        builder.setPositiveButton("Delete", (dialog, which) -> deleteBus(bus));
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    // Method to delete a bus from the database
    private void deleteBus(Bus bus) {
        busesRef.child(bus.getBusId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(usersBus.this, "Bus deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(usersBus.this, "Failed to delete bus: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(usersBus.this, HomePage.class);
        startActivity(intent);
        finish();
    }
}
