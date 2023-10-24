package com.example.sch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Gallery extends AppCompatActivity {

    private String subjectName;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Create your RecyclerView adapter and set it to the RecyclerView
        ChatAdapter adapter = new ChatAdapter();
        recyclerView.setAdapter(adapter);

        // Create a reference to the Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chat_messages");

        // Set up a ValueEventListener to listen for new chat messages
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChatMessage> chatMessages = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    chatMessages.add(chatMessage);
                }
                adapter.setChatMessages(chatMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        };

        // Attach the ValueEventListener to the database reference
        databaseReference.addValueEventListener(valueEventListener);

        Button selectImageButton = findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(v -> {
            // Open an image picker or system intent to select an image from the gallery
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(intent);
        });

        Button sendButton = findViewById(R.id.sendButton);
        EditText messageEditText = findViewById(R.id.messageEditText);

        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();

            // Create a new ChatMessage object
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSenderId("v85XBgdSOgO2TYvgV8Aw0vGfB9u1"); // Your ID
            chatMessage.setMessage(message);
            chatMessage.setTimestamp(System.currentTimeMillis());

            if (selectedImageUri != null) {
                // Upload the image to Firebase Storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");
                UploadTask uploadTask = imageRef.putFile(selectedImageUri);

                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL for the uploaded image
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        chatMessage.setImageUrl(imageUrl);

                        // Save the chat message to the Firebase Realtime Database
                        DatabaseReference newChatMessageRef = databaseReference.push();
                        newChatMessageRef.setValue(chatMessage).addOnSuccessListener(unused -> {
                            // Clear the EditText
                            messageEditText.setText("");

                            // Retrieve the list of users from the Firebase Authentication database
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        String receiverId = userSnapshot.getKey();
                                        assert receiverId != null;
                                        if (!receiverId.equals(chatMessage.getSenderId())) {
                                            // Create a new ChatMessage object for each receiver
                                            ChatMessage receiverChatMessage = new ChatMessage();
                                            receiverChatMessage.setSenderId(chatMessage.getSenderId());
                                            receiverChatMessage.setReceiverId(receiverId);
                                            receiverChatMessage.setMessage(chatMessage.getMessage());
                                            receiverChatMessage.setImageUrl(chatMessage.getImageUrl());
                                            receiverChatMessage.setTimestamp(chatMessage.getTimestamp());

                                            // Save the chat message to the Firebase Realtime Database for the receiver
                                            DatabaseReference receiverChatMessageRef = FirebaseDatabase.getInstance().getReference("chat_messages").push();
                                            receiverChatMessageRef.setValue(receiverChatMessage);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle database error
                                }
                            });
                        });
                    });
                });
            } else {
                // Save the chat message to the Firebase Realtime Database without an image
                DatabaseReference newChatMessageRef = databaseReference.push();
                newChatMessageRef.setValue(chatMessage).addOnSuccessListener(unused -> {
                    // Clear the EditText
                    messageEditText.setText("");

                    // Retrieve the list of users from the Firebase Authentication database
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<ChatMessage> chatMessages = adapter.getChatMessages(); // Get the current chat messages from the adapter
                            String currentUserId = "v85XBgdSOgO2TYvgV8Aw0vGfB9u1"; // Your ID

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);

                                if (chatMessage != null && chatMessage.getSenderId() != null && chatMessage.getMessage() != null) {
                                    if (chatMessage.getSenderId().equals(currentUserId)) {
                                        // Update the existing message sent by the current user
                                        for (ChatMessage existingMessage : chatMessages) {
                                            if (existingMessage.getSenderId() != null && existingMessage.getMessage() != null &&
                                                    existingMessage.getSenderId().equals(chatMessage.getSenderId()) &&
                                                    existingMessage.getMessage().equals(chatMessage.getMessage())) {
                                                existingMessage.setTimestamp(chatMessage.getTimestamp()); // Update the timestamp
                                                break;
                                            }
                                        }
                                    } else {
                                        // Check if the chat message already exists in the list
                                        boolean exists = false;
                                        for (ChatMessage existingMessage : chatMessages) {
                                            if (existingMessage.getSenderId() != null && existingMessage.getMessage() != null &&
                                                    existingMessage.getSenderId().equals(chatMessage.getSenderId()) &&
                                                    existingMessage.getMessage().equals(chatMessage.getMessage())) {
                                                exists = true;
                                                break;
                                            }
                                        }
                                        // Add the chat message if it doesn't exist in the list
                                        if (!exists) {
                                            chatMessages.add(chatMessage);
                                        }
                                    }
                                }
                            }

                            adapter.setChatMessages(chatMessages); // Update the adapter with the modified chat messages
                        }




                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error
                        }
                    });
                });
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("subject")) {
            subjectName = intent.getStringExtra("subject");
        }
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        selectedImageUri = data.getData();
                        // Handle the selected image URI
                    }
                }
            }
    );
}
