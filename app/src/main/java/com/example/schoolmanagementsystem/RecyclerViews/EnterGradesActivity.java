package com.example.schoolmanagementsystem.RecyclerViews;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.Models.Student;
import com.example.schoolmanagementsystem.R;
import com.example.schoolmanagementsystem.adapters.EnterGradesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EnterGradesActivity extends AppCompatActivity {
    private  String intentClass,intentYear,intentExam, intentSubject;
    private DatabaseReference studentRef, subjectRef;
    private List<Student> studentList = new ArrayList<>();
    private List<String> studentIds = new ArrayList<>();
    private RecyclerView studentRV;
    private EnterGradesAdapter enterGradesAdapter;
    private Button submitBtn;
    private String studentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enter_grades);
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
                    enterGradesAdapter = new EnterGradesAdapter(studentList,getApplicationContext());
                    studentRV.setLayoutManager(new LinearLayoutManager(EnterGradesActivity.this));
                    enterGradesAdapter.notifyDataSetChanged();
                    studentRV.setAdapter(enterGradesAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submitBtn.setOnClickListener(v -> {
            if (enterGradesAdapter != null) {
                enterGradesAdapter.uploadToFirebase(intentYear, intentClass, intentExam, intentSubject, studentIds);
            }
        });
    }
}