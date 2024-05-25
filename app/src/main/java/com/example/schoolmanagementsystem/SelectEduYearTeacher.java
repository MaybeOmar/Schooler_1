package com.example.schoolmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.schoolmanagementsystem.RecyclerViews.TeacherListActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectEduYearTeacher extends AppCompatActivity {
    private Spinner eduYearSp, classSp;
    private Button NextBtn;
    private DatabaseReference eduYearRef, classRef;
    private List<String> eduYearList =new ArrayList<>();
    private List<String> classList = new ArrayList<>();
    private String SelectedYear, SelectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_edu_year_teacher);

        classSp = findViewById(R.id.classSp);
        eduYearSp = findViewById(R.id.eduYearSp);
        NextBtn = findViewById(R.id.tNextBtn);

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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectEduYearTeacher.this, android.R.layout.simple_list_item_1, eduYearList);
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
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectEduYearTeacher.this, android.R.layout.simple_list_item_1, classList);
                                            classSp.setAdapter(arrayAdapter);
                                            classSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    SelectedClass =parent.getItemAtPosition(position).toString();
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



        /*classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classList.clear();
                classList.add("Select Classroom");
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                        if(dataSnapshot1.hasChildren()){
                            String key=dataSnapshot1.getKey();
                            classList.add(key);
                        }
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(SelectEduYearStu.this,android.R.layout.simple_list_item_1, classList);
                    classSp.setAdapter(arrayAdapter);


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/






        NextBtn.setOnClickListener(v -> {
            if(SelectedYear !=null && !SelectedYear.equals("Select Education Year") && SelectedClass != null && !SelectedClass.equals("Select Classroom")){
                Intent intent=new Intent(SelectEduYearTeacher.this, TeacherListActivity.class);
                intent.putExtra("eduYear", SelectedYear);
                intent.putExtra("class", SelectedClass);
                startActivity(intent);
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