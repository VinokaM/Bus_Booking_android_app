package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;// Firebase Authentication instance
    private EditText iNemail, iNpassword;// Input fields for email and password
    private Button login;// Login button

    TextView loginText;// TextView for navigating to SignUp activity

    ProgressBar progressBar; // Progress bar for showing login progress

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// Set the layout for MainActivity

        mAuth = FirebaseAuth.getInstance();// Initialize Firebase Authentication
        // Initialize UI elements
        iNemail = findViewById(R.id.email);
        iNpassword = findViewById(R.id.password);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.pBar);
        loginText = findViewById(R.id.loginText);


        // Set click listener for TextView to navigate to SignUp activity
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
                finish();// Finish current activity
            }
        });

        // Set click listener for Login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);// Show progress bar
                String email, password;
                email = String.valueOf(iNemail.getText());
                password = String.valueOf(iNpassword.getText());

                // Validate email and password fields
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this, "Plz enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Plz enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sign in user with Firebase Authentication
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(MainActivity.this, "success!!",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(),HomePage.class);
                                    startActivity(intent);
                                    finish(); // Finish current activity

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}