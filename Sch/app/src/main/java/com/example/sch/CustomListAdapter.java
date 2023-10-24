package com.example.sch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> studentNames;
    private final List<String> rollNumbers;
    private final String subjectName;

    private final DatePicker datePicker;
    private final List<String> attendanceList;


    public CustomListAdapter(Context context, List<String> studentNames, List<String> rollNumbers, String subjectName, DatePicker datePicker) {
        super(context, R.layout.list_item_layout, studentNames);
        this.context = context;
        this.studentNames = studentNames;
        this.rollNumbers = rollNumbers;
        this.subjectName = subjectName;
        this.attendanceList = new ArrayList<>();
        this.datePicker = datePicker;
        initAttendanceList();
    }

    private void initAttendanceList() {
        String defaultAttendance = "Absent";
        for (int i = 0; i < studentNames.size(); i++) {
            attendanceList.add(defaultAttendance);
        }
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.list_item_layout, parent, false);
        }

        TextView nameTextView = rowView.findViewById(R.id.name);
        TextView rollNumberTextView = rowView.findViewById(R.id.rollnumber);
        Button incrementButton = rowView.findViewById(R.id.incrementButton);
        Button decrementButton = rowView.findViewById(R.id.decrementButton);


        String studentName = studentNames.get(position);
        String rollNumber = rollNumbers.get(position);

        nameTextView.setText(studentName);
        rollNumberTextView.setText(rollNumber);

        // Set the attendance value and update the TextView


        // Set listeners for increment and decrement buttons
        incrementButton.setOnClickListener(v -> {
            incrementAttendance(position);
            updateAttendanceInDatabase(position);
        });

        decrementButton.setOnClickListener(v -> {
            decrementAttendance(position);
            updateAttendanceInDatabase(position);
        });

        return rowView;
    }

    private void incrementAttendance(int position) {
        String attendance = attendanceList.get(position);
        if (attendance.equals("Absent")) {
            attendance = "Present";
            attendanceList.set(position, attendance);
            notifyDataSetChanged();
        }
    }

    private void decrementAttendance(int position) {
        String attendance = attendanceList.get(position);
        if (attendance.equals("Present")) {
            attendance = "Absent";
            attendanceList.set(position, attendance);
            notifyDataSetChanged();
        }
    }


    private void updateAttendanceInDatabase(int position) {
        String studentName = studentNames.get(position);
        String attendance = String.valueOf(attendanceList.get(position));

        // Get the selected date from the DatePicker
        int year = datePicker.getYear();
        int month = datePicker.getMonth() + 1; // Month is zero-based, so add 1
        int day = datePicker.getDayOfMonth();
        String dateString = String.format(Locale.US, "%04d-%02d-%02d", year, month, day);

        // Get the user ID for the student
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.orderByChild("name").equalTo(studentName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();

                    // Update the attendance in the database for the respective student and date
                    assert userId != null;
                    DatabaseReference attendanceRef = userRef.child(userId).child("attendance").child(subjectName).child(dateString);
                    attendanceRef.setValue(attendance);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the database query
            }
        });
    }

}
