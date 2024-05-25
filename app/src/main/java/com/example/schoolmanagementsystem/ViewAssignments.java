package com.example.schoolmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.schoolmanagementsystem.RecyclerViews.ViewAssignmentRV;

public class ViewAssignments extends AppCompatActivity {
    Button fetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignments);
        fetch = findViewById(R.id.fetch);

        fetch.setOnClickListener(v -> startActivity(new Intent(ViewAssignments.this, ViewAssignmentRV.class)));
    }
}