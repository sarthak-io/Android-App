package com.example.sch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class subjects extends AppCompatActivity {
 CardView Math,Chemistry,Physics;
  public String maths,chemistry,physics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        Math= findViewById(R.id.MathSubject);
        Chemistry= findViewById(R.id.ChemistrySubjects);
        Physics= findViewById(R.id.physics);

        Math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(subjects.this, SubjectInside.class);
                intent.putExtra("subject", "Math");
                startActivity(intent);
                finish();

            }
        });
        Chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(subjects.this, SubjectInside.class);

                intent.putExtra("subject", "Chemistry");
                startActivity(intent);

            }
        });
        Physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(subjects.this, SubjectInside.class);
                intent.putExtra("subject", "Physics");
                startActivity(intent);

            }
        });

    }
}