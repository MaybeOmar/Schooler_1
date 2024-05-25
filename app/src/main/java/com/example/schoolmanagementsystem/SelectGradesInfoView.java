package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.schoolmanagementsystem.RecyclerViews.ViewGradesActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectGradesInfoView extends AppCompatActivity {

    private Spinner eduYearSp, classSp, gradetypeSp, subjectSp;
    private Button nextBtn;
    private DatabaseReference eduYearRef, classRef, typeRef, subjectRef;
    private List<String> classList = new ArrayList<>();
    private List<String> eduYearList = new ArrayList<>();
    private List<String> gradeTypeList = new ArrayList<>();
    private List<String> subjectList = new ArrayList<>();
    private String SelectedYear, SelectedClass, SelectedType, SelectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_grades_info_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nextBtn = findViewById(R.id.tNextBtn);
        classSp = findViewById(R.id.classSp);
        eduYearSp = findViewById(R.id.eduYearSp);
        gradetypeSp = findViewById(R.id.typeSp);
        subjectSp = findViewById(R.id.subjectSp);

        eduYearRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years");

        eduYearRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eduYearList.clear();
                eduYearList.add("Select Education Year");
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.hasChildren()) {
                            String key = dataSnapshot1.getKey();
                            eduYearList.add(key);
                        }
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectGradesInfoView.this, android.R.layout.simple_list_item_1, eduYearList);
                    eduYearSp.setAdapter(arrayAdapter);
                    eduYearSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            SelectedYear = parent.getItemAtPosition(position).toString();
                            if (!SelectedYear.equals("Select Education Year")) {
                                classRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(SelectedYear).child("Classes");
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
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectGradesInfoView.this, android.R.layout.simple_list_item_1, classList);
                                            classSp.setAdapter(arrayAdapter);
                                            classSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    SelectedClass = parent.getItemAtPosition(position).toString();
                                                    if (!SelectedClass.equals("Select Classroom")) {
                                                        typeRef = FirebaseDatabase.getInstance().getReference()
                                                                .child("Schooler")
                                                                .child("Education Years")
                                                                .child(SelectedYear)
                                                                .child("Classes")
                                                                .child(SelectedClass)
                                                                .child("Grades");
                                                        typeRef.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                gradeTypeList.clear();
                                                                gradeTypeList.add("Select Exam");
                                                                if (snapshot.exists()) {
                                                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                                                        if (dataSnapshot1.hasChildren()) {
                                                                            String key = dataSnapshot1.getKey();
                                                                            gradeTypeList.add(key);
                                                                        }
                                                                    }
                                                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectGradesInfoView.this, android.R.layout.simple_list_item_1, gradeTypeList);
                                                                    gradetypeSp.setAdapter(arrayAdapter);
                                                                    gradetypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                        @Override
                                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                            SelectedType = parent.getItemAtPosition(position).toString();
                                                                            if (!SelectedType.equals("Select Exam")) {
                                                                                subjectRef = FirebaseDatabase.getInstance().getReference()
                                                                                        .child("Schooler")
                                                                                        .child("Education Years")
                                                                                        .child(SelectedYear)
                                                                                        .child("Classes")
                                                                                        .child(SelectedClass)
                                                                                        .child("Grades")
                                                                                        .child(SelectedType)
                                                                                        .child("Subjects");
                                                                                subjectRef.addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        subjectList.clear();
                                                                                        subjectList.add("Select Subject");
                                                                                        if (snapshot.exists()) {
                                                                                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                                                                                if (dataSnapshot1.hasChildren()) {
                                                                                                    String key = dataSnapshot1.getKey();
                                                                                                    subjectList.add(key);
                                                                                                }
                                                                                            }
                                                                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectGradesInfoView.this, android.R.layout.simple_list_item_1, subjectList);
                                                                                            subjectSp.setAdapter(arrayAdapter);

                                                                                            subjectSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                                                @Override
                                                                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                                                    SelectedSubject = parent.getItemAtPosition(position).toString();
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

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectedYear != null && !SelectedYear.equals("Select Education Year") && SelectedClass != null && !SelectedClass.equals("Select Classroom") && SelectedType != null && !SelectedType.equals("Select Exam") && SelectedSubject != null && !SelectedSubject.equals("Select Subject")) {
                    Intent intent = new Intent(SelectGradesInfoView.this, ViewGradesActivity.class);
                    intent.putExtra("eduYear", SelectedYear);
                    intent.putExtra("class", SelectedClass);
                    intent.putExtra("exam", SelectedType);
                    intent.putExtra("subject", SelectedSubject);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}