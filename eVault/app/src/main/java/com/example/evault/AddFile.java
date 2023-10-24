package com.example.evault;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class AddFile extends AppCompatActivity {
    private static final int PICK_DOCUMENT_REQUEST = 123;
    private Uri selectedFileUri;
    private EditText fileNameEditText;
    private EditText descriptionEditText;
    private Button selectFileButton;
    String selectedFileExtension;
    private static final String TAG = "SampleActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);
        ImageView back = findViewById(R.id.back_btn_upload);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(AddFile.this, MainActivity.class);
            startActivity(intent);
        });
        // Initialize the Spinner
        Spinner fileExtensionSpinner = findViewById(R.id.file_extension_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.file_extensions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fileExtensionSpinner.setAdapter(adapter);

        // Set an item selected listener for the Spinner
        fileExtensionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected file extension
                selectedFileExtension = fileExtensionSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected
            }
        });

        selectFileButton = findViewById(R.id.select_file);
        Button uploadButton = findViewById(R.id.upload_btn);
        fileNameEditText = findViewById(R.id.file_name);
        descriptionEditText = findViewById(R.id.decription_name);

        selectFileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, PICK_DOCUMENT_REQUEST);
        });

        uploadButton.setOnClickListener(v -> {
            String fileName = fileNameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            if (selectedFileUri != null && !fileName.isEmpty() && !description.isEmpty()) {
                uploadFile(selectedFileUri, fileName, description);
                sendFileToServer(selectedFileUri, fileName, description);
            } else {
                Toast.makeText(this, "Please fill in all fields and select a file", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_DOCUMENT_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                selectedFileUri = data.getData();
                String fileName = getFileNameAndExtensionFromUri(selectedFileUri);
                selectFileButton.setText(fileName); // Set the button text to the selected file name
            }
        }
    }

    private String getFileNameAndExtensionFromUri(Uri fileUri) {
        String fileName = "";
        if (fileUri != null) {
            fileName = fileUri.getLastPathSegment();
        }
        return fileName;
    }

    private void uploadFile(Uri fileUri, String fileName, String description) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Generate a unique file name with the file extension
        String uniqueFilename = "uploads/" + System.currentTimeMillis() + "_" + fileName;

        StorageReference fileRef = storageRef.child(uniqueFilename);
        UploadTask uploadTask = fileRef.putFile(fileUri);


        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // File uploaded successfully, get the download URL
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();
                saveFileData(fileName, description, downloadUrl, selectedFileExtension);
                Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                // Handle the case where getting the download URL fails
                Toast.makeText(this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            // Handle the case where the file upload fails
            Toast.makeText(this, "File upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void saveFileData(String fileName, String description, String fileUrl, String fileExtension) {
        String userId = getCurrentUserId();

        if (userId != null) {
            DatabaseReference userUploadsReference = FirebaseDatabase.getInstance().getReference().child("user_uploads").child(userId);
            String uploadId = userUploadsReference.push().getKey();

            if (uploadId != null) {
                Upload upload = new Upload(fileName, description, fileUrl, fileExtension);
                userUploadsReference.child(uploadId).setValue(upload);
            } else {
                Toast.makeText(this, "Failed to generate upload ID", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentUserId() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        }
        return null;
    }

    private void sendFileToServer(Uri fileUri, String fileName, String description) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String serverUrl = "https://sk45678.pythonanywhere.com/upload"; // Replace with the actual server URL
                    HttpURLConnection connection = (HttpURLConnection) new URL(serverUrl).openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    // Create a boundary for the multipart request
                    String boundary = "*****" + System.currentTimeMillis() + "*****";

                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    OutputStream outputStream = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                    // Add the file data
                    writer.write("--" + boundary + "\r\n");
                    writer.write("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n");
                    writer.write("Content-Type: " + getContentResolver().getType(fileUri) + "\r\n\r\n");

                    InputStream fileInputStream = getContentResolver().openInputStream(fileUri);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    fileInputStream.close();

                    writer.write("\r\n");
                    writer.write("--" + boundary + "--\r\n");
                    writer.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Successfully uploaded the file
                        InputStream responseStream = connection.getInputStream();
                        BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseStream));
                        StringBuilder responseStringBuilder = new StringBuilder();
                        String line;
                        while ((line = responseReader.readLine()) != null) {
                            responseStringBuilder.append(line);
                        }
                        responseReader.close();
                        responseStream.close();
                        return responseStringBuilder.toString();
                    } else {
                        // Handle the case where the server returns an error
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String response) {
                if (response != null) {
                    // Handle the server response here
                    Toast.makeText(AddFile.this, "Server Response: " + response, Toast.LENGTH_SHORT).show();
Log.d(TAG,response);

                } else {
                    Toast.makeText(AddFile.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }
        };

        asyncTask.execute();
    }
}
