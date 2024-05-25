package com.example.schoolmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.schoolmanagementsystem.RecyclerViews.AssignmentRecyclerViewActivity;

public class EditAssignment extends AppCompatActivity {

    Button upload,fetch,profile_btn,settings_btn;
    Intent intentprofile,intentsettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assignment);
        upload = findViewById(R.id.upload);
        fetch = findViewById(R.id.fetch);


        //settings button
        settings_btn=findViewById(R.id.settings_button);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentsettings = new Intent(EditAssignment.this,settings.class);
                startActivity(intentsettings);
            }
        });

        //profile button
        profile_btn=findViewById(R.id.profile_button);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentprofile = new Intent(EditAssignment.this,profile.class);
                startActivity(intentprofile);
            }
        });

        // After Clicking on this we will be
        // redirected to choose pdf
        upload.setOnClickListener(v -> {
            startActivity(new Intent(EditAssignment.this, UploadAssignment.class));
        });

        fetch.setOnClickListener(v -> startActivity(new Intent(EditAssignment.this, AssignmentRecyclerViewActivity.class)));
    }
}