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

public class AddStudentActivity extends AppCompatActivity {
    private Spinner eduYearsp, classSp;
    private EditText studentNameET, studentIdET, studentEmailET;
    private DatabaseReference eduYearRef, classRef, destinationRef, destinationRef2;
    private List<String> educationYearList, classList;
    private Button addStudentBtn;
    private String selectedEducationYear, selectedClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        studentNameET = findViewById(R.id.studentName);
        studentIdET = findViewById(R.id.studentID);
        studentEmailET = findViewById(R.id.studentEmail);
        eduYearsp = findViewById(R.id.educationYearSp);
        classSp = findViewById(R.id.classSp);
        addStudentBtn = findViewById(R.id.addStudentBtn);
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddStudentActivity.this, android.R.layout.simple_list_item_1, educationYearList);
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
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddStudentActivity.this, android.R.layout.simple_list_item_1, classList);
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
        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentName = studentNameET.getText().toString().trim();
                String studentID = studentIdET.getText().toString().trim();
                String studentEmail = studentEmailET.getText().toString().trim();

                /*String selectedYear = educationYearList.get(eduYearsp.getSelectedItemPosition());*/

                if (studentName.isEmpty() || studentID.isEmpty() || studentEmail.isEmpty()) {
                    Toast.makeText(AddStudentActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                destinationRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(selectedEducationYear).child("Classes").child(selectedClass).child("Students").child(studentID);
                destinationRef2 = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(selectedEducationYear).child("Classes").child(selectedClass).child("Students").child(studentID);


                destinationRef.child("id").setValue(studentID);
                destinationRef.child("name").setValue(studentName);
                destinationRef.child("email").setValue(studentEmail);
                destinationRef.child("password").setValue("");
                destinationRef.child("class").setValue(selectedClass);
                destinationRef.child("phone").setValue("1234");
                destinationRef.child("year").setValue(selectedEducationYear);



                // Clear input fields
                studentIdET.setText("");
                studentNameET.setText("");
                studentEmailET.setText("");

                Toast.makeText(AddStudentActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }
}