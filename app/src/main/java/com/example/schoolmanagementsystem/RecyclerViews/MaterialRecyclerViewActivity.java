package com.example.schoolmanagementsystem.RecyclerViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.schoolmanagementsystem.R;
import com.example.schoolmanagementsystem.adapters.TeacherRVMaterialAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class MaterialRecyclerViewActivity extends AppCompatActivity implements TeacherRVMaterialAdapter.RefreshListener {
    private RecyclerView recyclerView;
    private TeacherRVMaterialAdapter adapter;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ArrayList<String> fileList;
    private ArrayList<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfrecycler_view);
        recyclerView = findViewById(R.id.recyclerView);
        storage = FirebaseStorage.getInstance();
        fileList = new ArrayList<>();
        urls = new ArrayList<>();

        adapter = new TeacherRVMaterialAdapter((Context) MaterialRecyclerViewActivity.this, (ArrayList<String>) fileList, (ArrayList<String>) urls,this);
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
                    ((TeacherRVMaterialAdapter) Objects.requireNonNull(recyclerView.getAdapter())).Update(fileName, url);
                }).addOnFailureListener(exception -> {
                    // Handle any errors
                });
            }
        }).addOnFailureListener(e -> {
            // Handle any errors
        });
    }
    @Override
    public void onRefresh() {
        // Add logic to refresh the activity here
        // For example, you can recreate the activity or reload data
        recreate(); // This recreates the activity
    }

}