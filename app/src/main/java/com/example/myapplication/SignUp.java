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

// Define the SignUp activity class
public class SignUp extends AppCompatActivity {

    FirebaseAuth mAuth;

    // UI elements
    private EditText uPemail, uPpassword;
    private Button signup;

    TextView signupText;

    ProgressBar progressBar;

    // Firebase authentication instance
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up); // Set the layout for this activity

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase authentication

        // Initialize UI elements from layout
        uPemail = findViewById(R.id.email);
        uPpassword = findViewById(R.id.password);
        signup = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.pBar);
        signupText = findViewById(R.id.signupText);

        firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase authentication

        // Set click listener for already have an account text
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to login activity
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set click listener for sign up button
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE); // Show progress bar
                String email, password;
                email = String.valueOf(uPemail.getText()); // Get email from EditText
                password = String.valueOf(uPpassword.getText()); // Get password from EditText

                // Validate email field
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUp.this, "Plz enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate password field
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUp.this, "Plz enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create user with email and password using Firebase authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE); // Hide progress bar
                                    Toast.makeText(SignUp.this, "Account created",
                                            Toast.LENGTH_SHORT).show();

                                    // Navigate to login activity
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }


}