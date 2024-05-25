package com.example.schoolmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.schoolmanagementsystem.RecyclerViews.ViewMaterialRV;

public class ViewSubjectMaterial extends AppCompatActivity {
    Button fetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subject_material);
        fetch = findViewById(R.id.fetch);

        fetch.setOnClickListener(v -> startActivity(new Intent(ViewSubjectMaterial.this, ViewMaterialRV.class)));
    }
}