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

import com.example.schoolmanagementsystem.RecyclerViews.SubjectListActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectEduYearSub extends AppCompatActivity {
    private Spinner eduYearSp;
    private Button NextBtn;
    private DatabaseReference eduYearRef;
    private List<String> eduYearList =new ArrayList<>();
    private String SelectedYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_edu_year_sub);
        eduYearSp = findViewById(R.id.eduYearSp);
        NextBtn = findViewById(R.id.tNextBtn);

        eduYearRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years");
        eduYearRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eduYearList.clear();
                eduYearList.add("Select Education Year");
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if(dataSnapshot1.hasChildren()){
                            String key=dataSnapshot1.getKey();
                            eduYearList.add(key);
                        }
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(SelectEduYearSub.this,android.R.layout.simple_list_item_1, eduYearList);
                    eduYearSp.setAdapter(arrayAdapter);
                    eduYearSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            SelectedYear =parent.getItemAtPosition(position).toString();
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
        /*eduYearRef = FirebaseFirestore.getInstance().collection("Education Years").document("Years");
        eduYearRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                eduYearList.clear();
                eduYearList.add("Select Education Year");
                if (value.exists()) {
                    Map<String, Object> data = value.getData();
                    if (data != null) {
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            String key = entry.getKey();
                            // Assuming you want to add the key to eduYearList
                            eduYearList.add(key);
                        }
                    }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectEduYear.this, android.R.layout.simple_list_item_1, eduYearList);
                        eduYearSp.setAdapter(arrayAdapter);
                        eduYearSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                SelectedYear = parent.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                    }
                }
            });*/





        NextBtn.setOnClickListener(v -> {
            if(SelectedYear !=null && !SelectedYear.equals("Select Education Year")){
                Intent intent=new Intent(SelectEduYearSub.this, SubjectListActivity.class);
                intent.putExtra("eduYear", SelectedYear);
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