package com.example.contactbook;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFriendActivity extends AppCompatActivity {

    private EditText etFriendName;
    private EditText etFriendContact;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        etFriendName = findViewById(R.id.etFriendName);
        etFriendContact = findViewById(R.id.etFriendContact);
        Button btnAddFriend = findViewById(R.id.btnAddFriend);

        // Initialize the DatabaseReference to the "friends" node in Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("friends");

        btnAddFriend.setOnClickListener(v -> addFriendToDatabase());
    }

    private void addFriendToDatabase() {
        String friendName = etFriendName.getText().toString().trim();
        String friendContact = etFriendContact.getText().toString().trim();

        if (!friendName.isEmpty() && !friendContact.isEmpty()) {
            // Push a new entry to the "friends" node with both name and contact number
            DatabaseReference friendRef = databaseReference.push();
            friendRef.child("name").setValue(friendName);
            friendRef.child("contactNumber").setValue(friendContact);

            etFriendName.setText(""); // Clear the EditText for name
            etFriendContact.setText(""); // Clear the EditText for contact number
            Toast.makeText(this, "Friend added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter both name and contact number", Toast.LENGTH_SHORT).show();
        }
    }
}
