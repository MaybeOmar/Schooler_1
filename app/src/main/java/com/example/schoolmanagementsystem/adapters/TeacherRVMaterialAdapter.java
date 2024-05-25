package com.example.schoolmanagementsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class TeacherRVMaterialAdapter extends RecyclerView.Adapter<TeacherRVMaterialAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> items;
    private ArrayList<String> urls;
    private RefreshListener refreshListener;

    public interface RefreshListener {
        void onRefresh();
    }

    // Constructor
    public TeacherRVMaterialAdapter(Context context, ArrayList<String> items, ArrayList<String> urls, RefreshListener refreshListener) {
        this.context = context;
        this.items = items;
        this.urls = urls;
        this.refreshListener = refreshListener;
    }

    // Method to update adapter with new data
    public void Update(String name, String url){
        items.add(name);
        urls.add(url);
        notifyDataSetChanged();
    }


    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            delete = itemView.findViewById(R.id.delete);

            // Set onClickListener for item view
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(urls.get(position)), "application/pdf");
                context.startActivity(intent);
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pdfname = textView.getText().toString();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference("Material");
                    StorageReference fileRef = storageRef.child(pdfname);


                    fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            refreshListener.onRefresh();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            refreshListener.onRefresh();
                        }
                    });
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdfitems, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
