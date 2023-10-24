package com.example.sch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addstudent extends AppCompatActivity {
    EditText studentName,studentNumber,studentPassword;
     MultiAutoCompleteTextView subject;
    Button addStudentButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);
        subject = findViewById(R.id.multiAutoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getDropdownOptions());
        subject.setAdapter(adapter);
        subject.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer()); // Allow multiple subjects separated by commas
        studentName = findViewById(R.id.stududent_name);
        studentPassword = findViewById(R.id.std_password);
        studentNumber = findViewById(R.id.studentRollNumber);

        addStudentButton = findViewById(R.id.add_std_btn);



      

        firebaseAuth = FirebaseAuth.getInstance();

        addStudentButton.setOnClickListener(view -> {
            String name = studentName.getText().toString();
            String username = studentNumber.getText().toString();
            String password = studentPassword.getText().toString();
            String subjects = subject.getText().toString();
            String dummyEmail = username+"dummy@example.com";
            // Create the user in Firebase Authentication
            firebaseAuth.createUserWithEmailAndPassword(dummyEmail, password)
                    .addOnCompleteListener(addstudent.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(user!= null){
                                String userId = user.getUid();

                                // Save user information in the Realtime Database
                                database = FirebaseDatabase.getInstance();
                                reference = database.getReference("users").child(userId);

                                Helper helper = new Helper(name, username, password, subjects);
                                reference.setValue(helper);

                                Toast.makeText(addstudent.this, "You have added successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(addstudent.this, MainActivity.class);
                                startActivity(intent);}
                        } else {
                            Toast.makeText(addstudent.this, "Sign up failed :" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }


}