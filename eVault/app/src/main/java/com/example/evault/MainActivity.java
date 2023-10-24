package com.example.evault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardAdapter adapter;
    private FirebaseAuth auth;
    String title;
    String fileExtension;

    private CardView delButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        // Initialize the 'Upload' button
        CardView uploadFile = findViewById(R.id.upload_btn);

        // Set an OnClickListener for the 'Upload' button
        uploadFile.setOnClickListener(v -> openAddFileActivity());

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize the adapter
        List<CardModel> cardList = new ArrayList<>();
        adapter = new CardAdapter(cardList);

        // Set an item click listener
// Set an item long-press listener
        adapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, CardModel item) {
                // Handle the item click event for the card at the given position
                String fileUrl = item.getFileUrl();
                openFileBasedOnType(fileUrl);
            }

            @Override
            public void onItemLongClick(int position, CardModel item) {
                // Handle the long-press event here
                // Show the delete button and perform other actions as needed
                delButton.setVisibility(View.VISIBLE);
            }
        });

        // Set the adapter on the RecyclerView
        recyclerView.setAdapter(adapter);

        // Initialize the del_btn CardView
        delButton = findViewById(R.id.del_btn);

        // Set it initially invisible
        delButton.setVisibility(View.INVISIBLE);

        // Handle long press to show del_btn


        // Handle click on del_btn to delete the card
        delButton.setOnClickListener(v -> deleteCardFromFirebase());

        // Fetch and populate card data from Firebase
        populateCardData();
    }

    // Method to open the 'AddFile' activity
    private void openAddFileActivity() {
        Intent intent = new Intent(this, AddFile.class);
        startActivity(intent);
    }

    // Fetch and populate card data from Firebase
    private void populateCardData() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userUploadsReference = FirebaseDatabase.getInstance()
                    .getReference("user_uploads")
                    .child(currentUser.getUid());

            userUploadsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<CardModel> cardList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        title = snapshot.child("name").getValue(String.class);
                        fileExtension = snapshot.child("FileExtension").getValue(String.class);
                        String fileUrl = snapshot.child("url").getValue(String.class);

                        if (title != null && fileUrl != null) {
                            CardModel card = new CardModel(title, fileUrl, fileExtension);
                            cardList.add(card);
                        }
                    }

                    adapter.updateData(cardList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error
                    Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Method to open files based on their URLs
    private void openFileBasedOnType(String fileUrl) {
        if (fileExtension.toLowerCase().endsWith(".pdf")) {
            // Open PDF file using the PdfViewerActivity
            openPdfViewerActivity(fileUrl);
        } else if (fileExtension.toLowerCase().endsWith(".jpg") || fileExtension.toLowerCase().endsWith(".jpeg") || fileExtension.toLowerCase().endsWith(".png")) {
            // Open image file using the ImageViewerActivity
            openImageViewerActivity(fileUrl);
        } else {
            // Handle other file types or show an error message
            Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to open a PDF file using the PdfViewerActivity
    private void openPdfViewerActivity(String pdfUrl) {
        Intent intent = new Intent(this, PdfViewerActivity.class);
        intent.putExtra("pdf_url", pdfUrl);
        startActivity(intent);
    }

    // Method to open an image file using the ImageViewerActivity
    private void openImageViewerActivity(String imageUrl) {
        Intent intent = new Intent(this, ImageViewerActivity.class);
        intent.putExtra("image_url", imageUrl);
        startActivity(intent);
    }

    // Implement a method to delete the selected card from Firebase
    private void deleteCardFromFirebase() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userUploadsReference = FirebaseDatabase.getInstance()
                    .getReference("user_uploads")
                    .child(currentUser.getUid());

            userUploadsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Implement the logic to find the card you want to delete based on some condition,
                        // e.g., the card's title, URL, or a unique identifier.
                        String cardTitle = snapshot.child("name").getValue(String.class);
                        if (cardTitle != null && cardTitle.equals(title)) {
                            // Remove the card from Firebase
                            snapshot.getRef().removeValue();
                            // Update your UI and adapter to reflect the changes
                            // This might involve removing the card from your adapter's data list
                            // and calling adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "File deleted", Toast.LENGTH_SHORT).show();
                            delButton.setVisibility(View.INVISIBLE); // Hide the delete button
                            break; // Exit the loop since the card was found and deleted
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error
                    Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




}
