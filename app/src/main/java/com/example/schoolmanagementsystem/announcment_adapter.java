package com.example.schoolmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.Models.announcment_model;

import java.util.ArrayList;
import java.util.List;

public class announcment_adapter extends RecyclerView.Adapter<ViewHolder> {

    annoouncment_List anns_list;
    ArrayList<announcment_model> modelList;
    LayoutInflater inflater;

    public announcment_adapter(annoouncment_List anns_list, List<announcment_model> modelList) {
        this.modelList = (ArrayList<announcment_model>) modelList;
        this.anns_list = anns_list;
        this.inflater = LayoutInflater.from(anns_list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = inflater.inflate(R.layout.announcment_model_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(itemView);
        holder.setonclicklistener(new ViewHolder.clicklistener() {
            @Override
            public void onitemclick(View view, int position) {
                // Handle item click
            }

            @Override
            public void onitemlongclick(View view, int position) {
                // Handle item long click
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        announcment_model model = modelList.get(position);
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.fullName.setText(model.getFullName()); // Set full name
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
