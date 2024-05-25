package com.example.schoolmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.schoolmanagementsystem.Models.Model;
import com.example.schoolmanagementsystem.adapters.Adapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_add);

        modelList = new ArrayList<>();
        modelList.add(new Model(R.drawable.addaccount, Registeration.class,"Add Account"));
        modelList.add(new Model(R.drawable.addaccount, SelectEduYearSub.class,"Add Subject"));
        modelList.add(new Model(R.drawable.addaccount, SelectEduYearTeacher.class,"Add Teacher"));
        modelList.add(new Model(R.drawable.addaccount, SelectEduYearStu.class,"Add Student"));
        modelList.add(new Model(R.drawable.addaccount, SelectEduYearClass.class,"Add Class"));
        modelList.add(new Model(R.drawable.addaccount, SelectTeacherID.class,"Add Subject to Teacher"));



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
                intentsettings = new Intent(AddActivity.this,settings.class);
                startActivity(intentsettings);
            }
        });


//profile button

        profile_btn=findViewById(R.id.profile_button);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentprofile = new Intent(AddActivity.this,profile.class);
                startActivity(intentprofile);
            }
        });



        //exit bar button
        // logout_btn = findViewById(R.id.logout_button);
        //  logout_btn.setOnClickListener(new View.OnClickListener()

        // {
        //      @Override
        //     public void onClick(View view)
        //  {
        //    finish();
        //  }
        //  });
    }
}