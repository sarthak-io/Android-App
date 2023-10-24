package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
      TextView ans= findViewById(R.id.ans);
      EditText num1 = findViewById((R.id.num1));
      EditText num2 = findViewById((R.id.num2));
      Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(view -> {
            int a = Integer.parseInt(num1.getText().toString()) ;
            int b= Integer.parseInt(num2.getText().toString()) ;
            int answer = a+b;
            ans.setText(String.valueOf(answer));
        });

    }
}