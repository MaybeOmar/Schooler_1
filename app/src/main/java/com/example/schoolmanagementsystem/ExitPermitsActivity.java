package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolmanagementsystem.adapters.ExitPermit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import android.widget.Button;

public class ExitPermitsActivity extends AppCompatActivity {

    Button logout_btn,back_btn;

    private static final String TAG = "ExitPermitsActivity";

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> permitList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth; // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_permits);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication
        db = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.listView);
        permitList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, permitList);
        listView.setAdapter(adapter);

        // Retrieve current user and their Unique_ID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserEmail = currentUser.getEmail();
            retrieveUserAndExitPermits(currentUserEmail);
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            // Handle scenario where user is not authenticated
        }
        back_btn=findViewById(R.id.back_button);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void retrieveUserAndExitPermits(String userEmail) {
        // Query to get the Unique_ID of the user based on their email
        db.collection("User")
                .whereEqualTo("Email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String currentUserId = document.getString("Unique_ID");

                                // Retrieve exit permits for the current user based on their Unique_ID
                                retrieveExitPermits(currentUserId);
                                return; // Exit after finding the user document
                            }
                            // Handle case where user document is not found
                            Toast.makeText(ExitPermitsActivity.this, "User document not found", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Error getting user document: ", task.getException());
                            Toast.makeText(ExitPermitsActivity.this, "Error fetching user document", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void retrieveExitPermits(String currentUserId) {
        db.collection("exit_permits")
                .whereEqualTo("parentID", currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            permitList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ExitPermit permit = document.toObject(ExitPermit.class);
                                String permitInfo = "Date: " + permit.getDate() + "\nTime: " + permit.getTime();
                                permitList.add(permitInfo);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(ExitPermitsActivity.this, "Error fetching exit permits", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
