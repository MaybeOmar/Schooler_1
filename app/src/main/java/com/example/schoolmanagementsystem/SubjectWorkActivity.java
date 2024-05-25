package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.Models.Model;
import com.example.schoolmanagementsystem.adapters.Adapter;

import java.util.ArrayList;
import java.util.List;

public class SubjectWorkActivity extends AppCompatActivity {

    Button /*upload,fetch,*/profile_btn,settings_btn;
    private List<Model> modelList;
    RecyclerView RV;
    Adapter adapter;
    Intent intentprofile,intentsettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_work);
       /* upload = findViewById(R.id.upload);
        fetch = findViewById(R.id.fetch);*/

        modelList = new ArrayList<>();
        modelList.add(new Model(R.drawable.material, EditMaterial.class,"Subject Material"));
        modelList.add(new Model(R.drawable.a_attend, AttendanceActivity.class,"Attendance"));
        modelList.add(new Model(R.drawable.a_grades, GradesActivity.class,"Grades"));
        modelList.add(new Model(R.drawable.assignment_kbeer, EditAssignment.class,"Assignment"));
        modelList.add(new Model(R.drawable.generateexam, student_home_page.class,"Generate Exam"));
        modelList.add(new Model(R.drawable.announcement, login_page.class,"Announcements"));

        //recyclerView
        RV = findViewById(R.id.id_recyclerview);
        RV.setLayoutManager(new GridLayoutManager(this,2));

        adapter = new Adapter(modelList);
        RV.setAdapter(adapter);

        //settings button
        settings_btn=findViewById(R.id.settings_button);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentsettings = new Intent(SubjectWorkActivity.this, settings.class);
                startActivity(intentsettings);
            }
        });

        //profile button
        profile_btn=findViewById(R.id.profile_button);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentprofile = new Intent(SubjectWorkActivity.this, profile.class);
                startActivity(intentprofile);
            }
        });
    }
}