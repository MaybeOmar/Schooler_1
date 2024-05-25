package com.example.schoolmanagementsystem.RecyclerViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.schoolmanagementsystem.AddStudentActivity;
import com.example.schoolmanagementsystem.AddSubjectActivity;
import com.example.schoolmanagementsystem.Models.Student;
import com.example.schoolmanagementsystem.R;
import com.example.schoolmanagementsystem.adapters.StudentListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    private FloatingActionButton addStudentButton;
    private RecyclerView studentRV;
    private String intended_year, intended_class;
    private StudentListAdapter studentListAdapter;
    private List<Student> studentList =new ArrayList<>();
    private DatabaseReference studentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        studentRV =findViewById(R.id.studentRV);
        addStudentButton = findViewById(R.id.addStudent);
        final Intent intent=getIntent();
        intended_year =intent.getStringExtra("eduYear");
        intended_class = intent.getStringExtra("class");

        studentRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intended_year).child("Classes").child(intended_class).child("Students");
        studentListAdapter = new StudentListAdapter( studentList, StudentListActivity.this);
        studentRV.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));
        studentRV.setAdapter(studentListAdapter);

        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        /*Student student = dataSnapshot1.getValue(Student.class);*/
                        String studentId = dataSnapshot1.child("id").getValue(String.class);
                        String studentName = dataSnapshot1.child("name").getValue(String.class);
                        String studentEmail = dataSnapshot1.child("email").getValue(String.class);

                        // Construct Student object
                        Student student = new Student(studentId, studentName,"","",intended_year, studentEmail,intended_class);
                        studentList.add(student);
                    }
                    studentListAdapter.notifyDataSetChanged(); // Move notifyDataSetChanged() here
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addStudentButton.setOnClickListener(v -> {

            Intent intent1=new Intent(StudentListActivity.this, AddStudentActivity.class);
            startActivity(intent1);
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
