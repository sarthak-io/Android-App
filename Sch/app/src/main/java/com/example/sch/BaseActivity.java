package com.example.sch;


import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.sch.MainActivity;
import com.example.sch.subjects;
import com.example.sch.fqa;
import com.google.android.material.navigation.NavigationBarView;

public class BaseActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (itemId == R.id.action_subjects) {
            startActivity(new Intent(this, subjects.class));
            return true;
        } else if (itemId == R.id.action_faq) {
            startActivity(new Intent(this, fqa.class));
            return true;
        }

        return false;
    }
}
