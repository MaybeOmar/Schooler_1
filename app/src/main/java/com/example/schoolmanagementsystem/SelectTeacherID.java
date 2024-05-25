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

public class SelectTeacherID extends AppCompatActivity {
    private Spinner eduYearsp, classSp;
    private EditText subjectNameET, teacherIdET;
    private DatabaseReference eduYearRef, classRef, destinationRef;
    private List<String> educationYearList, classList;
    private Button addBtn;
    private String selectedEducationYear, selectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_teacher_id);
        subjectNameET = findViewById(R.id.subjectName);
        teacherIdET = findViewById(R.id.teacherID);
        eduYearsp = findViewById(R.id.educationYearSp);
        classSp = findViewById(R.id.classSp);
        addBtn = findViewById(R.id.enterBtn);
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectTeacherID.this, android.R.layout.simple_list_item_1, educationYearList);
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
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectTeacherID.this, android.R.layout.simple_list_item_1, classList);
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
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName = subjectNameET.getText().toString().trim();
                String teacherID = teacherIdET.getText().toString().trim();

                /*String selectedYear = educationYearList.get(eduYearsp.getSelectedItemPosition());*/

                if (subjectName.isEmpty() || teacherID.isEmpty()) {
                    Toast.makeText(SelectTeacherID.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                destinationRef = FirebaseDatabase.getInstance().getReference()
                        .child("Schooler")
                        .child("Education Years")
                        .child(selectedEducationYear)
                        .child("Classes")
                        .child(selectedClass)
                        .child("Teachers")
                        .child(teacherID)
                        .child("Teacher Subjects")
                        .child(subjectName);

                destinationRef.child("Material").child("mat1").setValue("example");
                destinationRef.child("Assignments").child("assign1").setValue("example");
                destinationRef.child("Attendance").child("att1").setValue("example");
                destinationRef.child("Grades").child("grad1").setValue("example");




                // Clear input fields
                teacherIdET.setText("");
                subjectNameET.setText("");


                Toast.makeText(SelectTeacherID.this, "Student added successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }
}