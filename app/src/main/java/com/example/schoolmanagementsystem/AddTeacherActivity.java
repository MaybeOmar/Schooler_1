package com.example.schoolmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTeacherActivity extends AppCompatActivity {
    private Spinner eduYearsp, classSp;
    private EditText teacherNameET, teacherIdET, teacherEmailET;
    private DatabaseReference eduYearRef, classRef, destinationRef;
    private List<String> educationYearList, classList;
    private Button addTeacherBtn;
    private String selectedEducationYear, selectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        teacherNameET = findViewById(R.id.teacherName);
        teacherIdET = findViewById(R.id.teacherID);
        teacherEmailET = findViewById(R.id.teacherEmail);
        eduYearsp = findViewById(R.id.educationYearSp);
        classSp = findViewById(R.id.classSp);
        addTeacherBtn = findViewById(R.id.addTeacherBtn);
        educationYearList = new ArrayList<>();
        classList = new ArrayList<>();



        eduYearRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years");

        eduYearRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                educationYearList.clear();
                educationYearList.add("Select Education Year");
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.hasChildren()) {
                            String key = dataSnapshot1.getKey();
                            educationYearList.add(key);
                        }
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddTeacherActivity.this, android.R.layout.simple_list_item_1, educationYearList);
                    eduYearsp.setAdapter(arrayAdapter);
                    eduYearsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedEducationYear = parent.getItemAtPosition(position).toString();
                            if (!selectedEducationYear.equals("Select Education Year")) {
                                classRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(selectedEducationYear).child("Classes");
                                classRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        classList.clear();
                                        classList.add("Select Classroom");
                                        if (snapshot.exists()) {
                                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                                if (dataSnapshot1.hasChildren()) {
                                                    String key = dataSnapshot1.getKey();
                                                    classList.add(key);
                                                }
                                            }
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddTeacherActivity.this, android.R.layout.simple_list_item_1, classList);
                                            classSp.setAdapter(arrayAdapter);
                                            classSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    selectedClass =parent.getItemAtPosition(position).toString();
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle onCancelled
                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacherName = teacherNameET.getText().toString().trim();
                String teacherID = teacherIdET.getText().toString().trim();
                String teacherEmail = teacherEmailET.getText().toString().trim();

                /*String selectedYear = educationYearList.get(eduYearsp.getSelectedItemPosition());*/

                if (teacherName.isEmpty() || teacherID.isEmpty() || teacherEmail.isEmpty()) {
                    Toast.makeText(AddTeacherActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                destinationRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(selectedEducationYear).child("Classes").child(selectedClass).child("Teachers").child(teacherID);

                destinationRef.child("id").setValue(teacherID);
                destinationRef.child("name").setValue(teacherName);
                destinationRef.child("email").setValue(teacherEmail);
                destinationRef.child("password").setValue("");
                destinationRef.child("phone").setValue("1234");
                destinationRef.child("age").setValue(50);



                // Clear input fields
                teacherIdET.setText("");
                teacherNameET.setText("");
                teacherEmailET.setText("");

                Toast.makeText(AddTeacherActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}