package com.example.sch;


import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Data_fetched {

    public void fetchData(final DataFetchCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Log.d(TAG, "Fetching data...");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Data fetched successfully");
                List<String> studentNames = new ArrayList<>();
                List<String> rollNumbers = new ArrayList<>();
                List<String> studentSubjects = new ArrayList<>();
                Map<String, Map<String, Map<String, String>>> attendanceMap = new HashMap<>();
                List<String> userIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the values from the snapshot
                    String userId = snapshot.getKey(); // Get the user ID
                    userIds.add(userId); // Add the user ID to the list
                    String username = snapshot.child("name").getValue(String.class);
                    String rollNumber = snapshot.child("username").getValue(String.class);
                    String subjects = snapshot.child("subject").getValue(String.class);
                    studentNames.add(username);
                    rollNumbers.add(rollNumber);
                    studentSubjects.add(subjects);

                    DataSnapshot attendanceSnapshot = snapshot.child("attendance");
                    Map<String, Map<String, String>> subjectAttendanceMap = new HashMap<>();
                    for (DataSnapshot subjectSnapshot : attendanceSnapshot.getChildren()) {
                        String subject = subjectSnapshot.getKey();

                        Map<String, String> dateAttendanceMap = new HashMap<>();
                        for (DataSnapshot dateSnapshot : subjectSnapshot.getChildren()) {
                            String date = dateSnapshot.getKey();
                            String attendance = dateSnapshot.getValue(String.class);
                            dateAttendanceMap.put(date, attendance);
                        }

                        subjectAttendanceMap.put(subject, dateAttendanceMap);
                    }

                    attendanceMap.put(userId, subjectAttendanceMap);
                    Log.d(TAG,userId);
                }

                // Pass the fetched data to the callback
                callback.onDataFetched(studentNames, rollNumbers, studentSubjects,  attendanceMap,userIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if needed
                Log.e(TAG, "Data fetch cancelled: " + databaseError.getMessage());
            }
        });
    }
}

