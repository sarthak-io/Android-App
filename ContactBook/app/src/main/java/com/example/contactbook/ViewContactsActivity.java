package com.example.contactbook;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ViewContactsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> friendList;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        listView = findViewById(R.id.listViewContacts);
        friendList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendList);
        listView.setAdapter(adapter);

        // Initialize the DatabaseReference to the "friends" node in Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("friends");

        // Add a ValueEventListener to fetch and display contacts
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the friend object, which includes name and contact number
                    String friendName = snapshot.child("name").getValue(String.class);
                    String contactNumber = snapshot.child("contactNumber").getValue(String.class);

                    // Combine name and contact number for display
                    String friendInfo = "Name: " + friendName + "\nContact Number: " + contactNumber;
                    friendList.add(friendInfo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
