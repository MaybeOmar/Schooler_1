package com.example.schoolmanagementsystem;

import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class AddSubjectActivity extends AppCompatActivity {
    private Spinner eduYearsp;
    private EditText subjectNameET, teacherNameET;
    private DatabaseReference databaseReference;
    private List<String> educationYearList;
    private Button addSubjectBtn;
    private String selectedEducationYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        teacherNameET = findViewById(R.id.teacherName);
        eduYearsp = findViewById(R.id.educationYearSp);
        subjectNameET = findViewById(R.id.subjectName);
        addSubjectBtn = findViewById(R.id.addSubjectBtn);
        educationYearList = new ArrayList<>();



        databaseReference = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                educationYearList.clear();
                educationYearList.add("Select Education Year");
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if(dataSnapshot1.hasChildren()){
                            String key=dataSnapshot1.getKey();
                            educationYearList.add(key);
                        }
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(AddSubjectActivity.this,android.R.layout.simple_list_item_1, educationYearList);
                    eduYearsp.setAdapter(arrayAdapter);
                    eduYearsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedEducationYear =parent.getItemAtPosition(position).toString();
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

        addSubjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacherName = teacherNameET.getText().toString().trim();
                String subjectName = subjectNameET.getText().toString().trim();
                /*String selectedYear = educationYearList.get(eduYearsp.getSelectedItemPosition());*/

                if (teacherName.isEmpty() || subjectName.isEmpty()) {
                    Toast.makeText(AddSubjectActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add subject to database
                DatabaseReference subjectsRef = databaseReference.child(selectedEducationYear).child("Subjects").child(subjectName);
                subjectsRef.child("teacher_name").setValue(teacherName);
                subjectsRef.child("name").setValue(subjectName);

                // Clear input fields
                teacherNameET.setText("");
                subjectNameET.setText("");

                Toast.makeText(AddSubjectActivity.this, "Subject added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
