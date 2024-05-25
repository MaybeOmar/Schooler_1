package com.example.schoolmanagementsystem.RecyclerViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.schoolmanagementsystem.Models.Student;
import com.example.schoolmanagementsystem.R;
import com.example.schoolmanagementsystem.adapters.TakeAttendanceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class TakeAttendanceActivity extends AppCompatActivity {
    private  String intentClass,intentYear,intentDate;
    private DatabaseReference studentRef,yearRef,classRef,attendanceRef,presentRef,absentRef;
    private List<Student> studentList=new ArrayList<>();
    private RecyclerView studentRV;
    private TakeAttendanceAdapter takeAttendanceAdapter;
    private Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        Intent intent=getIntent();
        intentClass=intent.getStringExtra("class");
        intentYear=intent.getStringExtra("eduYear");
        intentDate=intent.getStringExtra("DATE");
        //  SweetToast.success(getApplicationContext(),intentDate);
        submitBtn=findViewById(R.id.submitbtn);
        studentRV =findViewById(R.id.studentRV);
        studentRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intentYear).child("Classes").child(intentClass).child("Students");

        attendanceRef=FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intentYear).child("Classes").child(intentClass).child("Attendance").child(intentDate);

        presentRef=attendanceRef.child("Present");
        absentRef=attendanceRef.child("Absent");




        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String studentId = dataSnapshot1.child("id").getValue(String.class);
                        String studentName = dataSnapshot1.child("name").getValue(String.class);
                        String studentEmail = dataSnapshot1.child("email").getValue(String.class);

                        // Construct Student object
                        Student student = new Student(studentId, studentName,"","",intentYear, studentEmail,intentClass);
                        studentList.add(student);
                                }
                    takeAttendanceAdapter=new TakeAttendanceAdapter(studentList,getApplicationContext());
                    studentRV.setLayoutManager(new LinearLayoutManager(TakeAttendanceActivity.this));
                    takeAttendanceAdapter.notifyDataSetChanged();
                    studentRV.setAdapter(takeAttendanceAdapter);
                            }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        TakeAttendanceAdapter.presentList.clear();
        TakeAttendanceAdapter.absentList.clear();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog dialog=new AlertDialog.Builder(TakeAttendanceActivity.this).create();
                View view= LayoutInflater.from(TakeAttendanceActivity.this).inflate(R.layout.attendancepopup,null);
                TextView total,present,absent;
                Button cancleBtn, confirmBtn;
                total=view.findViewById(R.id.TotalStudentTV);
                present=view.findViewById(R.id.presentStudentTV);
                absent=view.findViewById(R.id.absentStudentTV);
                cancleBtn=view.findViewById(R.id.canclebtn);
                confirmBtn=view.findViewById(R.id.confirmbtn);
                total.setText(Integer.toString(studentList.size()));
                present.setText(Integer.toString(TakeAttendanceAdapter.presentList.size()));
                absent.setText(Integer.toString(TakeAttendanceAdapter.absentList.size()));
                dialog.setCancelable(true);

                dialog.setView(view);


                cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String presentstudentID = "";
                        for (int i = 0; i < TakeAttendanceAdapter.presentList.size(); i++) {
                            presentstudentID =TakeAttendanceAdapter.presentList.get(i);
                            presentRef.child(presentstudentID).setValue(presentstudentID);
                        }

                        String absentstudentID = "";
                        for (int i = 0; i < TakeAttendanceAdapter.absentList.size(); i++) {
                            absentstudentID =TakeAttendanceAdapter.absentList.get(i);
                            absentRef.child(absentstudentID).setValue(absentstudentID);
                        }
                        // studentList.clear();

                        TakeAttendanceAdapter.presentList.clear();
                        TakeAttendanceAdapter.absentList.clear();

                        dialog.cancel();




                    }
                });
                dialog.show();
            }
        });
    }
    private  void  popup(){

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}