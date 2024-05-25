package com.example.schoolmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

public class profile extends AppCompatActivity {

    Button back_btn;
    TextView usernameTextView, emailTextView, phoneTextView,idTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.displayemail);
        phoneTextView = findViewById(R.id.displayphone);
        idTextView = findViewById(R.id.displayuserid);


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        DocumentReference userRef = db.collection("User").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    String fullName = documentSnapshot.getString("FullName");
                    String email = documentSnapshot.getString("Email");
                    String phoneNumber = documentSnapshot.getString("Phone number");
                    String unique_ID= documentSnapshot.getString("Unique_ID");


                    usernameTextView.setText(fullName);
                    emailTextView.setText(email);
                    phoneTextView.setText(phoneNumber);
                    idTextView.setText(unique_ID);
                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // Back button
        back_btn=findViewById(R.id.back_button);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
