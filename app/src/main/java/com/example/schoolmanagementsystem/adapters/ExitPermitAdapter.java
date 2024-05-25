package com.example.schoolmanagementsystem.adapters;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExitPermitAdapter {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference permitsRef = db.collection("exit_permits");

    public Task<DocumentReference> addExitPermit(String date, String parentID, String time) {
        ExitPermit permit = new ExitPermit(date, parentID, time);
        return permitsRef.add(permit);
    }
}
