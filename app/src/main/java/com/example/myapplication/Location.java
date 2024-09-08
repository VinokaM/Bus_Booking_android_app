package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Location extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private SensorEventListener rotationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location); // Set the layout for this activity

        mapView = findViewById(R.id.mapView);// Initialize MapView
        mapView.onCreate(savedInstanceState);// Create the MapView
        mapView.getMapAsync(this);// Get the map asynchronously

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);// Initialize fused location client

        // Define the location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                // Update the UI with the location data
                for (android.location.Location location : locationResult.getLocations()) {
                    updateLocationUI(location);
                }
            }
        };

        // Check for location permission and request if not granted
        if (ContextCompat.checkSelfPermission(
                Location.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Location.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation(); // If permission is granted, get the current location
        }

        // Initialize sensor manager and rotation sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        // Define the sensor event listener for rotation sensor
        rotationListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                    float[] rotationMatrix = new float[9];
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                    float[] orientation = new float[3];
                    SensorManager.getOrientation(rotationMatrix, orientation);
                    float bearing = (float) Math.toDegrees(orientation[0]);
                    if (googleMap != null) {
                        // Update the camera bearing on the map
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(googleMap.getCameraPosition().target)
                                        .zoom(googleMap.getCameraPosition().zoom)
                                        .bearing(bearing)
                                        .build()
                        ));
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Do nothing
            }
        };
    }

    // Method to get the current location
    private void getCurrentLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // 5 seconds
        locationRequest.setFastestInterval(2000); // 2 seconds

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    // Method to update the UI with the current location
    private void updateLocationUI(android.location.Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            if (googleMap != null) {
                googleMap.clear();// Clear the map
                googleMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));// Add marker
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));// Move camera to current location
            }
        }
    }

    // Handle the result of the location permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation(); // If permission is granted, get the current location
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();// Show permission denied message
            }
        }
    }

    // Callback when the map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getCurrentLocation(); // Ensure we request location updates when the map is ready
    }

    // Lifecycle methods for the MapView
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        sensorManager.registerListener(rotationListener, rotationSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        sensorManager.unregisterListener(rotationListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    // Override the back button press to navigate to HomePage activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Location.this, HomePage.class);
        startActivity(intent);
        finish();
    }
}
