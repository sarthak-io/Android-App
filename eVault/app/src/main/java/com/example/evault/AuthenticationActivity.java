package com.example.evault;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivity extends AppCompatActivity {
    private static final String TAG = "AuthenticationActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, proceed to your app's main activity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        Button signUpButton = findViewById(R.id.signup);
        signUpButton.setOnClickListener(v -> signUpWithEmailPassword());
    }

    private void signUpWithEmailPassword() {
        // Reference your EditText fields for email and password
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);

        // Get the email and password entered by the user
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Perform email and password sign-up
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-up success, update UI and possibly send verification email
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Sign-up successful.", Toast.LENGTH_SHORT).show();

                        // You can add additional logic here, like sending a verification email.
                    } else {
                        // If sign-up fails, display an error message
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
