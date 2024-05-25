package com.example.schoolmanagementsystem.RecyclerViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.schoolmanagementsystem.AddSubjectActivity;
import com.example.schoolmanagementsystem.Models.Subject;
import com.example.schoolmanagementsystem.R;
import com.example.schoolmanagementsystem.adapters.SubjectListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubjectListActivity extends AppCompatActivity {
    private FloatingActionButton addCourseButton;
    private RecyclerView subjectRV;
    private String intended_year;
    private SubjectListAdapter subjectListAdapter;
    private List<Subject> subjectList =new ArrayList<>();
    private DatabaseReference subjectRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        subjectRV =findViewById(R.id.subjectRV);
        final Intent intent=getIntent();
        intended_year =intent.getStringExtra("eduYear");

        addCourseButton=findViewById(R.id.addStudent);


        subjectRef= FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intended_year).child("Subjects");
        subjectListAdapter = new SubjectListAdapter(SubjectListActivity.this, subjectList);
        subjectRV.setLayoutManager(new LinearLayoutManager(SubjectListActivity.this));
        subjectRV.setAdapter(subjectListAdapter);

        subjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjectList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            Subject subject = dataSnapshot1.getValue(Subject.class);
                            subjectList.add(subject);
                    }
                    subjectListAdapter.notifyDataSetChanged(); // Move notifyDataSetChanged() here
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addCourseButton.setOnClickListener(v -> {

            Intent intent1=new Intent(SubjectListActivity.this, AddSubjectActivity.class);
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


     /*subjectRef= FirebaseDatabase.getInstance().getReference().child("Department").child(intented_subj);
        subjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if(dataSnapshot1.hasChildren()){
                            Course course=dataSnapshot1.getValue(Course.class);
                            courseList.add(course);
                        }
                    }
                    CourseListAdapter courseListAdapter=new CourseListAdapter(CourseListActivity.this,courseList);
                    courseRv.setLayoutManager(new LinearLayoutManager(CourseListActivity.this));
                    courseListAdapter.notifyDataSetChanged();
                    courseRv.setAdapter(courseListAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
