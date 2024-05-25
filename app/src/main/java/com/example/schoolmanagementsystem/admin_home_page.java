package com.example.schoolmanagementsystem;
import androidx.appcompat.app.AppCompatActivity;

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


public class admin_home_page extends AppCompatActivity {
    private List<Model> modelList;
    RecyclerView RV;
    Adapter adapter;
    Intent intentprofile,intentsettings;
    Button profile_btn,settings_btn;

    TextView displayusername;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        modelList = new ArrayList<>();
        modelList.add(new Model(R.drawable.addaccount, AddActivity.class,"Add"));
        modelList.add(new Model(R.drawable.deleteaccount, blank.class,"Delete Account"));
        modelList.add(new Model(R.drawable.announcement,add_announcement.class,"Announcements"));
        modelList.add(new Model(R.drawable.subjectwork1, SubjectWorkActivity.class,"Class Work"));
        modelList.add(new Model(R.drawable.a_attend, AttendanceActivity.class,"Attendance"));
        modelList.add(new Model(R.drawable.a_exit_permits, add_exit_permits.class,"Exit Permits"));
        modelList.add(new Model(R.drawable.tuitionfees,blank.class,"Tuition Fees"));



        //recyclerView
        RV = findViewById(R.id.id_recyclerview);
        RV.setLayoutManager(new GridLayoutManager(this,2));


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
                intentsettings = new Intent(admin_home_page.this,settings.class);
                startActivity(intentsettings);
            }
        });


//profile button

        profile_btn=findViewById(R.id.profile_button);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentprofile = new Intent(admin_home_page.this,profile.class);
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
