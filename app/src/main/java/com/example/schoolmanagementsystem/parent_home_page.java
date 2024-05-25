package com.example.schoolmanagementsystem;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.schoolmanagementsystem.Models.Model;
import com.example.schoolmanagementsystem.adapters.Adapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class parent_home_page extends AppCompatActivity {
    private List<Model> modelList;
    RecyclerView RV;
    Adapter adapter;
    Intent intentprofile,intentsettings;
    Button profile_btn,settings_btn;
    TextView displayusername;
    FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home_page);

        modelList = new ArrayList<>();
        modelList.add(new Model(R.drawable.subjectwork1, ViewSubjectMaterial.class,"Subject Work"));
        modelList.add(new Model(R.drawable.assignment_kbeer, ViewAssignments.class,"Assignments"));
        modelList.add(new Model(R.drawable.a_grades, blank.class,"Grades"));
        modelList.add(new Model(R.drawable.a_behavioral,blank.class,"Reports"));
        modelList.add(new Model(R.drawable.gradeprediction, blank.class,"Grade Prediction"));
        modelList.add(new Model(R.drawable.a_exit_permits, ExitPermitsActivity.class,"Exit Permits"));
        modelList.add(new Model(R.drawable.tuitionfees, blank.class,"Tuition Fees"));
        modelList.add(new Model(R.drawable.announcement, annoouncment_List.class,"Announcments"));
        modelList.add(new Model(R.drawable.generateexam, EnterQuestionsActivity.class,"Exam genrator"));



        //recyclerView
        RV = findViewById(R.id.id_recyclerview);
        RV.setLayoutManager(new GridLayoutManager(this,2));

// display l el username on home page
        adapter = new Adapter(modelList);
        RV.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        displayusername = findViewById(R.id.username);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("User").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String fullName = documentSnapshot.getString("FullName");
                    displayusername.setText(fullName);
                }
            }
        });


        //settings button
        settings_btn=findViewById(R.id.settings_button);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentsettings = new Intent(parent_home_page.this,settings.class);
                startActivity(intentsettings);
            }
        });

//profile button

        profile_btn=findViewById(R.id.profile_button);
       profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentprofile = new Intent(parent_home_page.this,profile.class);
                startActivity(intentprofile);
            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Close the whole application when back button is pressed
    }
}

