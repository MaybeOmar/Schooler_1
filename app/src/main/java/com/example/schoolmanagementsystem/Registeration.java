package com.example.schoolmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;


public class Registeration extends AppCompatActivity {
    EditText fullName,email,password,phone,uID, Student_Parent_ID;
    Button registerBtn,goToLogin;
    CheckBox isTeacherbox,isStudentbox,isAdminbox,isParentbox;
    boolean valid = true;
    boolean fact;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    DatabaseReference realtimeDBRef;
    private String FCM_Token;
    private static final String TAG = "Registeration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        EditText parentID = findViewById(R.id.StudentParentID);
        parentID.setVisibility(View.GONE);

        fAuth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        retrieveFCMToken();
        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        Student_Parent_ID = parentID;
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);
        uID = findViewById(R.id.registerUID);
        isTeacherbox= findViewById(R.id.isTeacher);
        isAdminbox=findViewById(R.id.IsAdmin);
        isParentbox=findViewById(R.id.Isparent);
        isStudentbox=findViewById(R.id.isStudent);
        realtimeDBRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Users").child("Student");


        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login_page.class));
                finish();
            }
        });

        isStudentbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isStudentbox.isChecked()){
                    isAdminbox.setChecked(false);
                    isParentbox.setChecked(false);
                    isTeacherbox.setChecked(false);

                    parentID.setVisibility(View.VISIBLE);
                }
                else {
                    parentID.setVisibility(View.GONE);
                }
            }
        });
        isAdminbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isAdminbox.isChecked()){
                    isStudentbox.setChecked(false);
                    isParentbox.setChecked(false);
                    isTeacherbox.setChecked(false);
                }
            }
        });
        isParentbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isParentbox.isChecked()){
                    isAdminbox.setChecked(false);
                    isStudentbox.setChecked(false);
                    isTeacherbox.setChecked(false);
                }
            }
        });
        isTeacherbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isTeacherbox.isChecked()){
                    isAdminbox.setChecked(false);
                    isParentbox.setChecked(false);
                    isStudentbox.setChecked(false);
                }
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = uID.getText().toString();
                checkField(fullName);
                checkField(email);
                checkField(password);
                checkField(phone);
                checkField(uID);
                if (isStudentbox.isChecked()) {
                    checkField(parentID);
                }

                if (!(isTeacherbox.isChecked() || isAdminbox.isChecked() || isParentbox.isChecked() || isStudentbox.isChecked())) {
                    Toast.makeText(Registeration.this, "select the Account type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (valid) {
                    IDAlreadyExists(userID, new OnResultListener<Boolean>() {
                        @Override
                        public void onResult(Boolean result) {
                            if (!result) {
                                fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        FirebaseUser user = fAuth.getCurrentUser();
                                        DocumentReference df = fstore.collection("User").document(userID);
                                        Map<String, Object> userInfo = new HashMap<>();
                                        userInfo.put("FullName", fullName.getText().toString());
                                        userInfo.put("Email", email.getText().toString().trim());
                                        userInfo.put("Password", password.getText().toString());
                                        userInfo.put("Phone number", phone.getText().toString().trim());
                                        userInfo.put("Unique_ID", uID.getText().toString());
                                        userInfo.put("FCM_Token: ", FCM_Token);
                                        //userInfo.put("isuser","1");
                                        if (isAdminbox.isChecked()) {
                                            userInfo.put("isAdmin", "1");
                                        }
                                        if (isParentbox.isChecked()) {
                                            userInfo.put("isParent", "1");
                                        }
                                        if (isTeacherbox.isChecked()) {
                                            userInfo.put("isTeacher", "1");
                                        }
                                        if (isStudentbox.isChecked()) {
                                            userInfo.put("isStudent", "1");
                                            userInfo.put("Parent's ID:", parentID.getText().toString());
                                        }

                                        DatabaseReference dbRef = realtimeDBRef.child(uID.getText().toString());
                                        dbRef.setValue(userInfo);

                                        Toast.makeText(Registeration.this, "Account created", Toast.LENGTH_SHORT).show();
                                        df.set(userInfo);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Registeration.this, "Account creation failed.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // ID already exists, handle accordingly
                                Toast.makeText(Registeration.this, "This ID is already registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    private void retrieveFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(token -> {
                    // Get the FCM token
                    if (token != null) {
                        // Token retrieved successfully, store it in the FCM_Token variable
                        FCM_Token = token;
                        Log.d("FCMToken", "FCM token retrieved successfully: " + FCM_Token);

                        // Now you can use the FCM_Token variable wherever needed, such as storing it in Firestore
                    } else {
                        // Token is null, handle this case if necessary
                        Log.e("FCMToken", "FCM token is null");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure to retrieve token
                    Log.e("FCMToken", "Failed to retrieve FCM token: " + e.getMessage());
                });
    }


    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
    public void IDAlreadyExists(String uniqueID, OnResultListener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("User").document(uniqueID);

        // Check if the document exists
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    listener.onResult(true);
                } else {
                    // Document does not exist, unique ID is not registered
                    listener.onResult(false);
                }
            } else {
                // Error occurred while fetching document
                Log.d(TAG, "Error getting document: ", task.getException());
                listener.onResult(true); // Consider this as ID already exists to prevent registration
            }
        });
    }
    public interface OnResultListener<T> {
        void onResult(T result);
    }

        /*EditText fullName, email, password, phone, uID, studentParentID;
        Button registerBtn, goToLogin;
        CheckBox isTeacherBox, isStudentBox, isAdminBox, isParentBox;
        boolean valid = true;
        FirebaseAuth fAuth;
        DatabaseReference userRef;
        private String FCMToken;
        private static final String TAG = "RegistrationActivity";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registeration);

            fAuth = FirebaseAuth.getInstance();
            userRef = FirebaseDatabase.getInstance().getReference("Users");
            retrieveFCMToken();

            fullName = findViewById(R.id.registerName);
            email = findViewById(R.id.registerEmail);
            password = findViewById(R.id.registerPassword);
            phone = findViewById(R.id.registerPhone);
            uID = findViewById(R.id.registerUID);
            studentParentID = findViewById(R.id.StudentParentID);
            registerBtn = findViewById(R.id.registerBtn);
            goToLogin = findViewById(R.id.gotoLogin);
            isTeacherBox = findViewById(R.id.isTeacher);
            isAdminBox = findViewById(R.id.IsAdmin);
            isParentBox = findViewById(R.id.Isparent);
            isStudentBox = findViewById(R.id.isStudent);

            goToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), login_page.class));
                    finish();
                }
            });

            isStudentBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isAdminBox.setChecked(false);
                        isParentBox.setChecked(false);
                        isTeacherBox.setChecked(false);
                        studentParentID.setVisibility(View.VISIBLE);
                    } else {
                        studentParentID.setVisibility(View.GONE);
                    }
                }
            });

            isAdminBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isStudentBox.setChecked(false);
                        isParentBox.setChecked(false);
                        isTeacherBox.setChecked(false);
                    }
                }
            });

            isParentBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isAdminBox.setChecked(false);
                        isStudentBox.setChecked(false);
                        isTeacherBox.setChecked(false);
                    }
                }
            });

            isTeacherBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isAdminBox.setChecked(false);
                        isParentBox.setChecked(false);
                        isStudentBox.setChecked(false);
                    }
                }
            });

            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userID = uID.getText().toString();
                    checkField(fullName);
                    checkField(email);
                    checkField(password);
                    checkField(phone);
                    checkField(uID);
                    if (isStudentBox.isChecked()) {
                        checkField(studentParentID);
                    }

                    if (!(isTeacherBox.isChecked() || isAdminBox.isChecked() || isParentBox.isChecked() || isStudentBox.isChecked())) {
                        Toast.makeText(Registeration.this, "Select the Account type", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (valid) {
                        IDAlreadyExists(userID, new OnResultListener<Boolean>() {
                            @Override
                            public void onResult(Boolean result) {
                                if (!result) {
                                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            FirebaseUser user = fAuth.getCurrentUser();

                                            Map<String, Object> userInfo = new HashMap<>();
                                            userInfo.put("FullName", fullName.getText().toString());
                                            userInfo.put("Email", email.getText().toString().trim());
                                            userInfo.put("Password", password.getText().toString());
                                            userInfo.put("Phone number", phone.getText().toString().trim());
                                            userInfo.put("Unique_ID", uID.getText().toString());
                                            userInfo.put("FCM_Token", FCMToken);
                                            if (isAdminBox.isChecked()) {
                                                DatabaseReference userReference = userRef.child("Admin").child(userID);
                                                userInfo.put("isAdmin", true);
                                                userReference.setValue(userInfo);
                                            }
                                            if (isParentBox.isChecked()) {
                                                DatabaseReference userReference = userRef.child("Parent").child(userID);
                                                userInfo.put("isParent", true);
                                                userReference.setValue(userInfo);
                                            }
                                            if (isTeacherBox.isChecked()) {
                                                DatabaseReference userReference = userRef.child("Teacher").child(userID);
                                                userInfo.put("isTeacher", true);
                                                userReference.setValue(userInfo);
                                            }
                                            if (isStudentBox.isChecked()) {
                                                DatabaseReference userReference = userRef.child("Student").child("KG1").child(userID);
                                                userInfo.put("isStudent", true);
                                                userInfo.put("Parent's ID", studentParentID.getText().toString());
                                                userReference.setValue(userInfo);
                                            }
                                            Toast.makeText(Registeration.this, "Account created", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Registeration.this, "Account creation failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    // ID already exists, handle accordingly
                                    Toast.makeText(Registeration.this, "This ID is already registered", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }

        private void retrieveFCMToken() {
            FirebaseMessaging.getInstance().getToken()
                    .addOnSuccessListener(token -> {
                        if (token != null) {
                            FCMToken = token;
                            Log.d("FCMToken", "FCM token retrieved successfully: " + FCMToken);
                        } else {
                            Log.e("FCMToken", "FCM token is null");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FCMToken", "Failed to retrieve FCM token: " + e.getMessage());
                    });
        }

        public boolean checkField(EditText textField) {
            if (textField.getText().toString().isEmpty()) {
                textField.setError("Error");
                valid = false;
            } else {
                valid = true;
            }

            return valid;
        }

        public void IDAlreadyExists(String uniqueID, OnResultListener<Boolean> listener) {
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("User").child(uniqueID);

            // Check if the document exists
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listener.onResult(snapshot.exists());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error checking user ID existence: " + error.getMessage());
                    listener.onResult(true); // Consider this as ID already exists to prevent registration
                }
            });
        }

        public interface OnResultListener<T> {
            void onResult(T result);
        }*/
}
