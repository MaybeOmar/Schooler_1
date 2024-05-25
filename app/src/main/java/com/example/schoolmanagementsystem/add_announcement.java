package com.example.schoolmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class add_announcement extends AppCompatActivity {

    EditText titleET, descriptionET;
    Button save, view_list;

    ProgressDialog pd;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        titleET = findViewById(R.id.anns_TitleET);
        descriptionET = findViewById(R.id.anns_DiscriptionET);
        save = findViewById(R.id.anns_savebtn);
        view_list = findViewById(R.id.view_listbtn);

        pd = new ProgressDialog(this);

        view_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_announcement.this, annoouncment_List.class));
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAnnouncement();
            }
        });
    }

    private void uploadAnnouncement() {
        pd.setTitle("Adding data to Firestore");
        pd.show();

        String title = titleET.getText().toString().trim();
        String description = descriptionET.getText().toString().trim();

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            final String uid = currentUser.getUid();

            // Retrieve full name from Firestore
            db.collection("User").document(uid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String fullName = document.getString("FullName");
                                    // Upload announcement data with full name
                                    uploadAnnouncementData(title, description, fullName);
                                } else {
                                    Toast.makeText(add_announcement.this, "User data not found", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            } else {
                                Toast.makeText(add_announcement.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }
                    });
        } else {
            Toast.makeText(add_announcement.this, "User not logged in", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
    }

    private void uploadAnnouncementData(String title, String description, String fullName) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> announcement = new HashMap<>();
        announcement.put("id", id);
        announcement.put("Title", title);
        announcement.put("description", description);
        announcement.put("fullName", fullName);
        announcement.put("date", FieldValue.serverTimestamp());

        db.collection("Announcment").document(id)
                .set(announcement)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(add_announcement.this, "Announcement added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(add_announcement.this, "Failed to add announcement: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
