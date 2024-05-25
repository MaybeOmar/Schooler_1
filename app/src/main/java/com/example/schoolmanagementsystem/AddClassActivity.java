package com.example.schoolmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddClassActivity extends AppCompatActivity {
    private Spinner eduYearsp;
    private EditText classNameET;
    private DatabaseReference eduYearRef, destinationRef;
    private List<String> educationYearList;
    private Button addClassBtn;
    private String selectedEducationYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        classNameET = findViewById(R.id.className);
        eduYearsp = findViewById(R.id.educationYearSp);
        addClassBtn = findViewById(R.id.addClassBtn);
        educationYearList = new ArrayList<>();



        eduYearRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years");

        eduYearRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                educationYearList.clear();
                educationYearList.add("Select Education Year");
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if(dataSnapshot1.hasChildren()){
                            String key=dataSnapshot1.getKey();
                            educationYearList.add(key);
                        }
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(AddClassActivity.this,android.R.layout.simple_list_item_1, educationYearList);
                    eduYearsp.setAdapter(arrayAdapter);
                    eduYearsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedEducationYear =parent.getItemAtPosition(position).toString();
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

        addClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = classNameET.getText().toString().trim();


                if (className.isEmpty()) {
                    Toast.makeText(AddClassActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add subject to database
                destinationRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(selectedEducationYear).child("Classes").child(className);
                destinationRef.child("Students").child("Example Student").child("name").setValue("Hoi");
                destinationRef.child("Teachers").child("Example Teacher").child("name").setValue("Hoi tany");



                // Clear input fields
                classNameET.setText("");


                Toast.makeText(AddClassActivity.this, "Subject added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}