package com.example.schoolmanagementsystem;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login_page extends AppCompatActivity {

    Button login_button,signup;
    EditText email,password;
    boolean valid=true;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        fAuth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
       if (fAuth.getCurrentUser() != null) {
            // User is already logged in, check user access level
            checkUserAccessLevel(fAuth.getCurrentUser().getUid());}

        email = findViewById(R.id.id_et_username);
        password = findViewById(R.id.id_et_password);


        //Add button
        signup=findViewById(R.id.Signup);


        login_button = findViewById(R.id.id_button_login);
        login_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {


                checkField(email);
                checkField(password);

                //Log.d("TAG","onClick",+email.getText().toString())

                if(valid){
                    fAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(login_page.this,"Login successfully",Toast.LENGTH_SHORT).show();
                            checkUserAccessLevel(authResult.getUser().getUid());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(login_page.this, "Try again",Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(login_page.this, Registeration.class);
                startActivity(intent);
            }

        });
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fstore.collection("User").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onsuccess: " + documentSnapshot.getData());

                if (documentSnapshot.getString("isAdmin") != null) {
                    startActivity(new Intent(getApplicationContext(), admin_home_page.class));
                    finish();

                } else if (documentSnapshot.getString("isParent") != null) {
                    startActivity(new Intent(getApplicationContext(), parent_home_page.class));
                    finish();
                } else if (documentSnapshot.getString("isStudent") != null) {
                    startActivity(new Intent(getApplicationContext(), student_home_page.class));

                    finish();
                } else if (documentSnapshot.getString("isTeacher") != null) {
                    startActivity(new Intent(getApplicationContext(), teacher_home_page.class));
                    finish();

                } else {
                    Toast.makeText(login_page.this, "Sorry", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }
    public boolean checkField(EditText textField){
        valid = true; // Reset valid to true before checking each field
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }

        return valid;
    }

    /*    @Override
      protected void onStart() {
           super.onStart();
           if(FirebaseAuth.getInstance().getCurrentUser() !=null);
           startActivity(new Intent(getApplicationContext(),student_home_page.class));
           finish();
       }

      */
   /* protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, check their role
            String uid = currentUser.getUid();
            DocumentReference df = fstore.collection("User").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.getString("isAdmin") != null) {
                            startActivity(new Intent(getApplicationContext(), admin_home_page.class));
                        } else if (documentSnapshot.getString("isParent") != null) {
                            startActivity(new Intent(getApplicationContext(), parent_home_page.class));
                        } else if (documentSnapshot.getString("isTeacher") != null) {
                            startActivity(new Intent(getApplicationContext(), teacher_home_page.class));
                        } else if (documentSnapshot.getString("isUser") != null) {
                            startActivity(new Intent(getApplicationContext(), student_home_page.class));
                        } else {
                            // If the role is not specified, you can handle it accordingly
                            Toast.makeText(login_page.this, "Role not specified", Toast.LENGTH_SHORT).show();
                        }
                        // Finish the current activity to prevent returning back to login page
                        finish();
                    } else {
                        Toast.makeText(login_page.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(login_page.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
      */  }



