package com.example.schoolmanagementsystem.RecyclerViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.schoolmanagementsystem.AddTeacherActivity;
import com.example.schoolmanagementsystem.Models.Teacher;
import com.example.schoolmanagementsystem.R;
import com.example.schoolmanagementsystem.adapters.TeacherListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherListActivity extends AppCompatActivity {
    private FloatingActionButton addTeacherButton;
    private RecyclerView teacherRV;
    private String intended_year, intended_class;
    private TeacherListAdapter teacherListAdapter;
    private List<Teacher> teacherList =new ArrayList<>();
    private DatabaseReference teacherRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        teacherRV =findViewById(R.id.teacherRV);
        addTeacherButton = findViewById(R.id.addTeacher);
        final Intent intent=getIntent();
        intended_year =intent.getStringExtra("eduYear");
        intended_class = intent.getStringExtra("class");

        teacherRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intended_year).child("Classes").child(intended_class).child("Teachers");
        teacherListAdapter = new TeacherListAdapter( teacherList, TeacherListActivity.this);
        teacherRV.setLayoutManager(new LinearLayoutManager(TeacherListActivity.this));
        teacherRV.setAdapter(teacherListAdapter);

        teacherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        /*Student student = dataSnapshot1.getValue(Student.class);*/
                        String teacherId = dataSnapshot1.child("id").getValue(String.class);
                        String teacherName = dataSnapshot1.child("name").getValue(String.class);
                        String teacherEmail = dataSnapshot1.child("email").getValue(String.class);

                        // Construct Student object
                        Teacher teacher = new Teacher(teacherId, teacherName,"","",intended_year, teacherEmail);
                        teacherList.add(teacher);
                    }
                    teacherListAdapter.notifyDataSetChanged(); // Move notifyDataSetChanged() here
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addTeacherButton.setOnClickListener(v -> {

            Intent intent1=new Intent(TeacherListActivity.this, AddTeacherActivity.class);
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