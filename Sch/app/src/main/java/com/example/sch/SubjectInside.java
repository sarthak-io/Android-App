package com.example.sch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;



public class SubjectInside extends AppCompatActivity {

    CardView gallery,attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjectinside);

        gallery = findViewById(R.id.gallery);
        attendance = findViewById(R.id.attendance);

        gallery.setOnClickListener(view -> {
            String subjectName = getIntent().getStringExtra("subject");
            Intent intent = new Intent(SubjectInside.this, Gallery.class);
            intent.putExtra("subject", subjectName);
            startActivity(intent);
            finish();
        });
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("userRole", "");



        if ("admin".equals(userRole)) {
            attendance.setVisibility(View.VISIBLE);
        } else {
            attendance.setVisibility(View.GONE);
        }
        attendance.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        String subjectName = getIntent().getStringExtra("subject");
        Intent intent = new Intent(SubjectInside.this, Attendance.class);
        intent.putExtra("subject", subjectName);
        startActivity(intent);
        finish();
    }
}
