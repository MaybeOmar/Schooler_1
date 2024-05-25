package com.example.schoolmanagementsystem.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.R;

import java.util.ArrayList;

public class StudentRVAssignmentAdapter extends RecyclerView.Adapter<StudentRVAssignmentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> items;
    private ArrayList<String> urls;


    // Constructor
    public StudentRVAssignmentAdapter(Context context, ArrayList<String> items, ArrayList<String> urls) {
        this.context = context;
        this.items = items;
        this.urls = urls;
    }

    public void Update(String name, String url){
        items.add(name);
        urls.add(url);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdfitemsforstudent, parent, false);
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


    // ViewHolder class
    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview1);

            // Set onClickListener for item view
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(urls.get(position)), "application/pdf");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // Handle case where PDF viewer app is not available
                        Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
