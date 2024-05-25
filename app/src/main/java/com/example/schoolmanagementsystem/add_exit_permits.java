package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolmanagementsystem.adapters.ExitPermitAdapter;

public class add_exit_permits extends AppCompatActivity {
    private EditText etParentID, etDate, etTime;
    private Button btnAddPermit,back_btn;
    private ExitPermitAdapter permitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exit_permits);

        etParentID = findViewById(R.id.etParentID);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        btnAddPermit = findViewById(R.id.btnAddPermit);

        permitAdapter = new ExitPermitAdapter();

        btnAddPermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parentID = etParentID.getText().toString().trim();
                String date = etDate.getText().toString().trim();
                String time = etTime.getText().toString().trim();

                // Validate input
                if (parentID.isEmpty() || date.isEmpty() || time.isEmpty()) {
                    Toast.makeText(add_exit_permits.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add exit permit to Firestore
                permitAdapter.addExitPermit(date, parentID, time)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(add_exit_permits.this, "Exit permit added successfully", Toast.LENGTH_SHORT).show();
                            // Clear fields if needed
                            etParentID.setText("");
                            etDate.setText("");
                            etTime.setText("");
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(add_exit_permits.this, "Failed to add exit permit: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        back_btn=findViewById(R.id.back_button);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
