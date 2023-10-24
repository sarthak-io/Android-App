package com.example.agecalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText etBirthYear;
    private Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etBirthYear = findViewById(R.id.etBirthYear);
        btnCalculate = findViewById(R.id.btnCalculate);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateAge();
            }
        });
    }

    private void calculateAge() {
        String birthYearText = etBirthYear.getText().toString().trim();

        if (!birthYearText.isEmpty()) {
            int birthYear = Integer.parseInt(birthYearText);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            if (birthYear <= currentYear) {
                int age = currentYear - birthYear;
                showToast("Your age is " + age + " years.");
            } else {
                showToast("Invalid birth year");
            }
        } else {
            showToast("Please enter your birth year");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
