package com.example.sch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.util.Objects;

public class Signup extends AppCompatActivity {
    EditText signupName, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);

        FirebaseApp.initializeApp(this);

        signupName = findViewById(R.id.signup_name);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signupbtn);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            // User is already logged in, so start the main activity
            startActivity(new Intent(Signup.this, MainActivity.class));
            finish(); // Finish the Signup activity
        }

        firebaseAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(view -> {
            String name = signupName.getText().toString();
            String username = signupUsername.getText().toString();
            String password = signupPassword.getText().toString();
            String dummyEmail = username+"dummy@example.com";
            // Create the user in Firebase Authentication
            firebaseAuth.createUserWithEmailAndPassword(dummyEmail, password)
                    .addOnCompleteListener(Signup.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(user!= null){
                            String userId = user.getUid();

                            // Save user information in the Realtime Database
                            database = FirebaseDatabase.getInstance();
                            reference = database.getReference("users").child(userId);

                            Helper helper = new Helper(name, username, password, "");
                            reference.setValue(helper);

                            Toast.makeText(Signup.this, "You have signed up successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Signup.this, Login.class);
                            startActivity(intent);}
                        } else {
                            Toast.makeText(Signup.this, "Sign up failed :" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        loginRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(Signup.this, Login.class);
            startActivity(intent);
        });
    }
}
