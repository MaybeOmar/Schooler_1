package com.example.schoolmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.schoolmanagementsystem.RecyclerViews.TakeAttendanceActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectAttendInfoActivity extends AppCompatActivity {

    private Spinner eduYearSp, classSp;
    private Button nextBtn;
    private DatabaseReference eduYearRef, classRef;
    private EditText dateET;
    private ImageButton dateIbtn;
    private List<String> classList = new ArrayList<>();
    private List<String> eduYearList =new ArrayList<>();
    private String date;
    private DatePickerDialog datePickerDialog;
    private String SelectedYear, SelectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_attend_info);
        dateET = findViewById(R.id.dateET);
        dateIbtn = findViewById(R.id.dateIbtn);
        nextBtn = findViewById(R.id.tNextBtn);
        classSp = findViewById(R.id.classSp);
        eduYearSp = findViewById(R.id.eduYearSp);

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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectAttendInfoActivity.this, android.R.layout.simple_list_item_1, eduYearList);
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
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectAttendInfoActivity.this, android.R.layout.simple_list_item_1, classList);
                                            classSp.setAdapter(arrayAdapter);
                                            classSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    SelectedClass = parent.getItemAtPosition(position).toString();
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

        dateIbtn.setOnClickListener(v -> {

            DatePicker datePicker = new DatePicker(SelectAttendInfoActivity.this);
            int currentDay = datePicker.getDayOfMonth();
            int currentMonth = datePicker.getMonth();
            int currentYear = datePicker.getYear();
            datePickerDialog = new DatePickerDialog(SelectAttendInfoActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(dayOfMonth + "-");
                            stringBuilder.append((month + 1) + "-");
                            stringBuilder.append(year);
                            dateET.setText(stringBuilder.toString());
                        }
                    }, currentYear, currentMonth, currentDay

            );
            datePickerDialog.show();
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date = dateET.getText().toString();
                DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(SelectedYear).child("Classes").child(SelectedClass).child("Attendance").child(date);

                attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(SelectAttendInfoActivity.this , "Date already saved", Toast.LENGTH_SHORT).show();
                        } else {
                            if (SelectedYear != null && !SelectedYear.equals("Select Education Year") && SelectedClass != null && !SelectedClass.equals("Select Classroom") && !date.isEmpty()) {
                                Intent intent = new Intent(SelectAttendInfoActivity.this, TakeAttendanceActivity.class);
                                intent.putExtra("eduYear", SelectedYear);
                                intent.putExtra("class", SelectedClass);
                                intent.putExtra("DATE", date);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors
                    }
                });

            }
        });
    }
                    @Override
                    public boolean onOptionsItemSelected (MenuItem item){

                        if (item.getItemId() == android.R.id.home) {
                            this.finish();
                        }
                        return super.onOptionsItemSelected(item);
                    }
}