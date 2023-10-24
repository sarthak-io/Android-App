package com.example.sch;

import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavigationHandler implements NavigationBarView.OnItemSelectedListener {
    private AppCompatActivity activity;

    public BottomNavigationHandler(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_home) {
            activity.startActivity(new Intent(activity, MainActivity.class));
            return true;
        } else if (itemId == R.id.action_subjects) {
            activity.startActivity(new Intent(activity, subjects.class));
            return true;
        } else if (itemId == R.id.action_faq) {
            activity.startActivity(new Intent(activity, fqa.class));
            return true;
        }

        return false;
    }
}
