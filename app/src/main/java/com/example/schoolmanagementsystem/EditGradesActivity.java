package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.Models.Student;
import com.example.schoolmanagementsystem.Models.StudentGrade;
import com.example.schoolmanagementsystem.RecyclerViews.EnterGradesActivity;
import com.example.schoolmanagementsystem.adapters.EditGradesAdapter;
import com.example.schoolmanagementsystem.adapters.EnterGradesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditGradesActivity extends AppCompatActivity {
    private  String intentClass,intentYear,intentExam, intentSubject;
    private DatabaseReference studentRef, subjectRef,gradesRef;
    private List<Student> studentList = new ArrayList<>();
    private List<String> gradelist = new ArrayList<>();
    private List<String> studentIds = new ArrayList<>();
    private List<StudentGrade> studentGrades = new ArrayList<>();
    private RecyclerView studentRV;
    private EditGradesAdapter editGradesAdapter;
    private Button submitBtn;
    private String studentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_grades);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent=getIntent();
        intentClass=intent.getStringExtra("class");
        intentYear=intent.getStringExtra("eduYear");
        intentExam=intent.getStringExtra("exam");
        intentSubject=intent.getStringExtra("subject");
        submitBtn=findViewById(R.id.submitbtn);
        studentRV =findViewById(R.id.studentRV);
        studentRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intentYear).child("Classes").child(intentClass).child("Students");

        subjectRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intentYear).child("Classes").child(intentClass).child("Grades").child(intentExam).child(intentSubject);
        gradesRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intentYear).child("Classes").child(intentClass).child("Grades").child(intentExam).child("Subjects").child(intentSubject);



        editGradesAdapter = new EditGradesAdapter(studentGrades,studentList,getApplicationContext(), studentRV);
        studentRV.setLayoutManager(new LinearLayoutManager(EditGradesActivity.this));
        studentRV.setAdapter(editGradesAdapter);

        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        studentId = dataSnapshot1.child("id").getValue(String.class);
                        studentIds.add(studentId);
                        String studentName = dataSnapshot1.child("name").getValue(String.class);
                        String studentEmail = dataSnapshot1.child("email").getValue(String.class);

                        // Construct Student object
                        Student student = new Student(studentId, studentName,"","",intentYear, studentEmail,intentClass);
                        studentList.add(student);
                    }

                    editGradesAdapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gradesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentGrades.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String studentId = dataSnapshot1.getKey();
                        String grade = dataSnapshot1.getValue(String.class);
                        gradelist.add(grade);
                        StudentGrade studentGrade = new StudentGrade(studentId, grade);
                        studentGrades.add(studentGrade);

                        Log.d("StudentGrade", "Student ID: " + studentGrade.getStudentId() + ", Grade: " + studentGrade.getGrade());

                    }
                    editGradesAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });



        submitBtn.setOnClickListener(v -> {
            for (int i = 0; i < studentGrades.size(); i++) {
                Log.d("StudentGrade", "Student ID: " + studentIds.get(i) + ", Grade: " + studentGrades.get(i).toString());
            }
            for (int i = 0; i < studentRV.getChildCount(); i++) {
                View view = studentRV.getChildAt(i);
                if (view instanceof EditText) {
                    view.clearFocus();
                }
            }
            editGradesAdapter.notifyDataSetChanged();

            if (editGradesAdapter != null) {
                editGradesAdapter.uploadToFirebase(intentYear, intentClass, intentExam, intentSubject, studentIds);
            }
        });
    }
}