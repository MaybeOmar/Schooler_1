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

import com.example.schoolmanagementsystem.EditGradesActivity;
import com.example.schoolmanagementsystem.Models.StudentGrade;
import com.example.schoolmanagementsystem.R;
import com.example.schoolmanagementsystem.SelectGradesInfoView;
import com.example.schoolmanagementsystem.adapters.ViewGradesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewGradesActivity extends AppCompatActivity implements ViewGradesAdapter.GradeUpdateListener{
    private  String intentClass,intentYear,intentExam, intentSubject;
    private DatabaseReference studentRef, gradesRef;
    private List<String> gradesList = new ArrayList<>();
    private List<String> studentIds = new ArrayList<>();
    private List<StudentGrade> studentGrades = new ArrayList<>();
    private RecyclerView studentRV;
    private ViewGradesAdapter viewGradesAdapter;
    private Button editBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_grades);
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
        /*editBtn = findViewById(R.id.editbtn);*/
        studentRV =findViewById(R.id.studentRV);
        studentRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intentYear).child("Classes").child(intentClass).child("Students");

        gradesRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intentYear).child("Classes").child(intentClass).child("Grades").child(intentExam).child("Subjects").child(intentSubject);

        viewGradesAdapter = new ViewGradesAdapter(studentGrades, ViewGradesActivity.this,ViewGradesActivity.this);
        studentRV.setLayoutManager(new LinearLayoutManager(ViewGradesActivity.this));
        studentRV.setAdapter(viewGradesAdapter);

        gradesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentGrades.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String studentId = dataSnapshot1.getKey();
                        String grade = dataSnapshot1.getValue(String.class);
                        StudentGrade studentGrade = new StudentGrade(studentId, grade);
                        studentGrades.add(studentGrade);
                    }
                    viewGradesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });


        /*editBtn.setOnClickListener(v -> {
                Intent intent1 = new Intent(ViewGradesActivity.this, EditGradesActivity.class);
                intent1.putExtra("eduYear", intentYear);
                intent1.putExtra("class", intentClass);
                intent1.putExtra("exam", intentExam);
                intent1.putExtra("subject", intentSubject);
                startActivity(intent1);

        });*/
    }

    @Override
    public void onUpdateGrade(String studentId, String newGrade) {
        DatabaseReference destinationRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intentYear).child("Classes").child(intentClass).child("Grades").child(intentExam).child("Subjects").child(intentSubject);
        String studentID = studentId;
        String grade = newGrade;
        destinationRef.child(studentID).setValue(grade);
    }
}
