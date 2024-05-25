package com.example.schoolmanagementsystem.RecyclerViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.schoolmanagementsystem.AddClassActivity;
import com.example.schoolmanagementsystem.R;
import com.example.schoolmanagementsystem.adapters.ClassListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassListActivity extends AppCompatActivity {
    private FloatingActionButton addClassButton;
    private RecyclerView classRV;
    private String intended_year;
    private ClassListAdapter classListAdapter;
    private List<String> classList =new ArrayList<>();
    private DatabaseReference classRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list_activity);
        classRV =findViewById(R.id.teacherRV);
        addClassButton = findViewById(R.id.addTeacher);
        final Intent intent=getIntent();
        intended_year =intent.getStringExtra("eduYear");

        classRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intended_year).child("Classes");
        classListAdapter = new ClassListAdapter( classList, ClassListActivity.this);
        classRV.setLayoutManager(new LinearLayoutManager(ClassListActivity.this));
        classRV.setAdapter(classListAdapter);

        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String className = dataSnapshot1.getKey();
                        classList.add(className);
                    }
                    classListAdapter.notifyDataSetChanged(); // Move notifyDataSetChanged() here
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addClassButton.setOnClickListener(v -> {

            Intent intent1=new Intent(ClassListActivity.this, AddClassActivity.class);
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