package com.example.sch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity  implements DataFetchCallback {

    List<String> studentNames = new ArrayList<>();
    List<String> rollNumbers = new ArrayList<>();
    List<String> studentSubjects = new ArrayList<>();
    Map<String, Map<String, Map<String, String>>>  attendanceMap = new HashMap<>();
    List<String> userIds = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private String Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button logoutButton = findViewById(R.id.logoutButton);




        if (currentUser != null) {
            Data_fetched data = new Data_fetched();
            data.fetchData(this); // Pass the callback to receive the fetched data

        }  // Handle the case when the user is not logged in
        // Redirect to the login screen or take appropriate action


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationHandler navigationHandler = new BottomNavigationHandler(this);
        bottomNavigationView.setOnItemSelectedListener(navigationHandler);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("userRole", "");

        Button addStudentButton = findViewById(R.id.addstudent_main_activity); // Replace with the actual ID of your "Add Student" button

        if ("admin".equals(userRole)) {
            addStudentButton.setVisibility(View.VISIBLE);
        } else {
            addStudentButton.setVisibility(View.GONE);
        }
        addStudentButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, addstudent.class);
            startActivity(intent);
            finish();
        });

        logoutButton.setOnClickListener(view -> {
            SharedPreferences sharedPreferences4 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences4.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        });





    }



    @Override
    public void onDataFetched(List<String> studentNames, List<String> rollNumbers, List<String> studentSubjects, Map<String, Map<String, Map<String, String>>> attendanceMap, List<String> userIds) {
        this.studentNames = studentNames;
        this.rollNumbers = rollNumbers;
        this.studentSubjects = studentSubjects;
        this.attendanceMap = attendanceMap;
        this.userIds = userIds;

        TextView chemistryAttendanceTextView = findViewById(R.id.AttendanceTextchemistry);
        TextView physicsAttendanceTextView = findViewById(R.id.AttendanceTextphysics);
        TextView mathsAttendanceTextView = findViewById(R.id.AttendanceTextmaths);
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            Map<String, Map<String, String>> userAttendanceMap = attendanceMap.get(currentUserId); // Retrieve user attendance map for the current user ID

            if (userAttendanceMap != null) {
                int chemistrySubjectPresentCount = 0;
                int  mathsSubjectPresentCount =0;
                int  physicsSubjectPresentCount=0;

                // Iterate through each date in the chemistry subject's attendance map
                Map<String, String> chemistryAttendance = userAttendanceMap.get("Chemistry");
                if (chemistryAttendance != null) {
                    for (String date : chemistryAttendance.keySet()) {
                        String attendanceStatus = chemistryAttendance.get(date);

                        if (attendanceStatus != null && attendanceStatus.equals("Present")) {
                            chemistrySubjectPresentCount++;
                        }
                        Log.d(Tag,attendanceStatus+ chemistrySubjectPresentCount);
                    }
                }
// Update for Physics
                Map<String, String> physicsAttendance = userAttendanceMap.get("Physics");
                if (physicsAttendance != null) {
                    for (String date : physicsAttendance.keySet()) {
                        String attendanceStatus = physicsAttendance.get(date);

                        if (attendanceStatus != null && attendanceStatus.equals("Present")) {
                            physicsSubjectPresentCount++;
                        }
                        Log.d(Tag, attendanceStatus + physicsSubjectPresentCount);
                    }
                }

// Update for Maths
                Map<String, String> mathsAttendance = userAttendanceMap.get("Math");
                if (mathsAttendance != null) {
                    for (String date : mathsAttendance.keySet()) {
                        String attendanceStatus = mathsAttendance.get(date);

                        if (attendanceStatus != null && attendanceStatus.equals("Present")) {
                            mathsSubjectPresentCount++;
                        }
                        Log.d(Tag, attendanceStatus + mathsSubjectPresentCount);
                    }
                }

                chemistryAttendanceTextView.setText(String.valueOf(chemistrySubjectPresentCount));
                physicsAttendanceTextView.setText(String.valueOf(physicsSubjectPresentCount));
                mathsAttendanceTextView.setText(String.valueOf(mathsSubjectPresentCount));

            }
        }
    }






}



