package com.example.map;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize your friends list (replace with your actual data)
        String[] friendsData = {
                "Dev Jadiya, vidisha",
                "Sarthak Khare, mandi bamora",
                "Harsh Tiwari, jabalpur",
                "Aryan Parashar, delhi",
                "Janvhi,indore",
                "Raj, bhopal",
                "Aadesh, indore",
                "Sakshi, ujjain",




                // Add more friends as needed
        };

        // Initialize the ListView and ArrayAdapter
        ListView listViewFriends = findViewById(R.id.listViewFriends);
        friendsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendsData);
        listViewFriends.setAdapter(friendsAdapter);

        // Set a click listener for the ListView items
        listViewFriends.setOnItemClickListener((adapterView, view, position, id) -> {
            String friendInfo = friendsAdapter.getItem(position);
            if (friendInfo != null) {
                String[] parts = friendInfo.split(", ");
                if (parts.length == 2) {
                    String cityName = parts[1]; // Extract the city name
                    sendPostRequest(cityName);
                }
            }
        });
    }

    private void sendPostRequest(String cityName) {
        new SendPostRequestTask().execute("https://sk45678.pythonanywhere.com/get_map", "city_name=" + cityName);
    }

    @SuppressLint("StaticFieldLeak")
    private class SendPostRequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String postData = params[1];
            return sendPostRequest(urlString, postData);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                // Parse the JSON response to get the map URL
                try {
                    JSONObject responseJson = new JSONObject(result);
//                    String mapUrl = responseJson.optString("map_url");

                    // Open a new fragment with the map URL
                    openMapFragment("https://sk45678.pythonanywhere.com/view");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Handle the error case
                Log.e("MyApp", "Error: Map not updated");
            }
        }
    }

    private String sendPostRequest(String urlString, String postData) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            osw.write(postData);
            osw.flush();
            osw.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString(); // Return the response as a string
            } else {
                // Handle the error case
                Log.e("MyApp", "Error: HTTP request failed with response code " + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MyApp", "Error in POST request: " + e.getMessage());
            return null;
        }
    }

    private void openMapFragment(String mapUrl) {
        Bundle bundle = new Bundle();
        bundle.putString("mapUrl", mapUrl);

        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mapFragment);
        transaction.addToBackStack(null); // Allows navigating back to the previous fragment
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}