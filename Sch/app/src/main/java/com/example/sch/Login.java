package com.example.sch;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText loginUsername, loginPassword,adminPassword;
    TextView signupRedirectText;
    Button loginButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPassword = findViewById(R.id.login_password);
        loginUsername = findViewById(R.id.login_username);
        loginButton = findViewById(R.id.login_button);
        adminPassword = findViewById(R.id.adminPassword);
        signupRedirectText = findViewById(R.id.SignupRedirectText);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> {
            Log.d(TAG,"clicked");
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            if (!ValidateUsername() | !ValidatePassword()) {
                // Handle validation errors
            } else {
                String username = loginUsername.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();
                String dummyEmail = username + "dummy@example.com";
                String adminPin = adminPassword.getText().toString().trim();
                // Sign in the user using Firebase Authentication
                firebaseAuth.signInWithEmailAndPassword(dummyEmail, password)
                        .addOnCompleteListener(Login.this, task -> {
                            if (task.isSuccessful()) {

                                if (adminPin.equals("143")) {
                                    storeUserRole("admin");
                                    startMainActivity();
                                } else if(adminPin.equals("")) {
                                    storeUserRole("student");
                                    startMainActivity();
                                }
                            } else {
                                // Handle login failure
                                loginPassword.setError("Invalid credentials");
                                loginPassword.requestFocus();
                            }
                        });
            }
        });


        signupRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
        });
    }

    public Boolean ValidateUsername() {
        String val = loginUsername.getText().toString();

        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean ValidatePassword() {
        String val = loginPassword.getText().toString();

        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }
    private void storeUserRole(String select) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userRole", select);
        editor.apply();
    }

    private void startMainActivity() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
