package com.example.sch;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Attendance extends AppCompatActivity implements DataFetchCallback {
    public List<String> studentNames;
    public String subjectName;
    public List<String> rollNumbers;
    public List<String> filteredNames;
    public List<String> filteredRollNumbers;
    public List<String> studentSubjects;
    List<String> userIds = new ArrayList<>();
    private DatePicker datePicker;
    public Map<String, Map<String, Map<String, String>>>  attendanceMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Intent intent = getIntent();
        if (intent.hasExtra("subject")) {
            subjectName = intent.getStringExtra("subject");
        }




        Data_fetched data = new Data_fetched();
        data.fetchData(this); // Pass the callback to receive the fetched data
    }

    // Callback method to receive the fetched data
    @Override
    public void onDataFetched(List<String> studentNames, List<String> rollNumbers, List<String> studentSubjects ,Map<String, Map<String, Map<String, String>>>  attendanceMap,List<String> userIds) {
        this.studentNames = studentNames;
        this.rollNumbers = rollNumbers;
        this.studentSubjects = studentSubjects;
        this.attendanceMap=attendanceMap;
        this.userIds=userIds;
        // Assuming you have a list of selected subjects stored in a variable called 'selectedSubjects'
        List<String> selectedSubjects = new ArrayList<>();
        selectedSubjects.add("Math");
        selectedSubjects.add("Physics");
        selectedSubjects.add("Chemistry");

        filterData(selectedSubjects);

        ListView listView = findViewById(R.id.AttendanceList);
        CustomListAdapter adapter = new CustomListAdapter(Attendance.this, filteredNames, filteredRollNumbers, subjectName, datePicker);
        listView.setAdapter(adapter);

    }

    private void filterData(List<String> subjects) {
        filteredNames = new ArrayList<>();
        filteredRollNumbers = new ArrayList<>();

        for (int i = 0; i < studentNames.size(); i++) {
            // Assuming the subjects are stored as a List<String> in the 'studentSubjects' variable of the Student object
            List<String> studentSubjectList = Arrays.asList(studentSubjects.get(i).split(",")); // Split the subjects by comma

            // Check if any of the student's subjects are present in the provided subjects list
            boolean hasMatchingSubject = false;
            for (String subject : subjects) {
                if (studentSubjectList.contains(subject)) {
                    hasMatchingSubject = true;
                    break;
                }
            }

            if (hasMatchingSubject) {
                filteredNames.add(studentNames.get(i));
                filteredRollNumbers.add(rollNumbers.get(i));
                Log.d(TAG, studentNames.get(i) + "  " + studentSubjects.get(i));
            }
        }
    }


}
