package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.Models.Student;
import com.example.schoolmanagementsystem.RecyclerViews.StudentListActivity;
import com.example.schoolmanagementsystem.adapters.StudentListAdapter;
import com.example.schoolmanagementsystem.adapters.ViewAttendanceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAttendanceActivity extends AppCompatActivity {
    private RecyclerView viewAttendanceRV;
    private  String intentClass, intentYear,intentDate;
    private DatabaseReference studentRef,attendanceRef;
    private List<Student> studentList=new ArrayList<>();
    private ViewAttendanceAdapter viewAttendanceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_attendance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewAttendanceRV=findViewById(R.id.studentRV);
        Intent intent=getIntent();
        intentClass =intent.getStringExtra("class");
        intentYear =intent.getStringExtra("eduYear");

        studentRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intentYear).child("Classes").child(intentClass).child("Students");
        viewAttendanceAdapter = new ViewAttendanceAdapter( ViewAttendanceActivity.this ,studentList);
        viewAttendanceRV.setLayoutManager(new LinearLayoutManager(ViewAttendanceActivity.this));
        viewAttendanceRV.setAdapter(viewAttendanceAdapter);

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
                        Student student = new Student(studentId, studentName,"","",intentYear, studentEmail,intentClass);
                        studentList.add(student);
                    }
                    viewAttendanceAdapter.notifyDataSetChanged(); // Move notifyDataSetChanged() here
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
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