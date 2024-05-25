package com.example.schoolmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.schoolmanagementsystem.Models.announcment_model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class annoouncment_List extends AppCompatActivity {

    private List<announcment_model> modelList = new ArrayList<>();
    private RecyclerView mRecyclerview;
    private announcment_adapter adapter;
    private FirebaseFirestore db;
    private FloatingActionButton addbtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annoouncment_list);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        addbtn = findViewById(R.id.add_btn);
        mRecyclerview = findViewById(R.id.recyclerView_list);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        showData();
    }

    private void showData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid(); // Get the UID of the current user
            db.collection("User").document(uid)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                // Get the fullName from the User document
                                String fullName = documentSnapshot.getString("fullName");

                                // Check isAdmin, isStudent, isTeacher, isParent and set visibility accordingly
                                String isAdmin = documentSnapshot.getString("isAdmin");
                                String isStudent = documentSnapshot.getString("isStudent");
                                String isTeacher = documentSnapshot.getString("isTeacher");
                                String isParent = documentSnapshot.getString("isParent");

                                if (isAdmin != null || isTeacher != null) {
                                    // User is admin, teacher, or parent - show add button
                                    addbtn.setVisibility(View.VISIBLE);
                                }
                                if(isParent != null || isStudent != null) {
                                    // User is student or doesn't have a role - hide add button
                                    addbtn.setVisibility(View.GONE);
                                }

                                // Now, fetch data from Announcment collection and pass fullName to adapter
                                fetchAnnouncementData(fullName);
                            } else {
                                // Document does not exist, handle as needed
                                Toast.makeText(annoouncment_List.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(annoouncment_List.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Handle the case where there's no signed-in user
            Toast.makeText(annoouncment_List.this, "No signed-in user", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to fetch announcement data from Firestore
    private void fetchAnnouncementData(final String fullName) {
        db.collection("Announcment")
                .orderBy("date")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            // Pass fullName to the model constructor
                            announcment_model model = new announcment_model(
                                    doc.getString("id"),
                                    doc.getString("Title"),
                                    doc.getString("description"),
                                    doc.getString("fullName")); // Pass fullName here
                            modelList.add(model);
                        }
                        adapter = new announcment_adapter(annoouncment_List.this, modelList);
                        mRecyclerview.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(annoouncment_List.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(annoouncment_List.this, add_announcement.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showData();
    }
}
