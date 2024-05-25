package com.example.schoolmanagementsystem.RecyclerViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.schoolmanagementsystem.R;
import com.example.schoolmanagementsystem.adapters.StudentRVMaterialAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class ViewMaterialRV extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StudentRVMaterialAdapter adapter;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ArrayList<String> fileList;
    private ArrayList<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_material_rv);
        recyclerView = findViewById(R.id.recyclerview1);
        storage = FirebaseStorage.getInstance();
        fileList = new ArrayList<>();
        urls = new ArrayList<>();

        adapter = new StudentRVMaterialAdapter(ViewMaterialRV.this, fileList, urls);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Material");
        storageReference.child(System.currentTimeMillis() + "").getDownloadUrl().addOnSuccessListener(uri -> {
            // Got the download URL for 'users/me/profile.png'
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });
        storageReference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                // Get file name
                String fileName = item.getName();

                // Get download URL
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();

                    // Update the adapter
                    // Update the adapter
                    ((StudentRVMaterialAdapter) Objects.requireNonNull(recyclerView.getAdapter())).Update(fileName, url);

                }).addOnFailureListener(exception -> {
                    // Handle any errors
                });
            }
        }).addOnFailureListener(e -> {
            // Handle any errors
        });
    }
}