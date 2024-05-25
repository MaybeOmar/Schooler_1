package com.example.schoolmanagementsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.schoolmanagementsystem.Models.Subject;
import com.example.schoolmanagementsystem.R;

import java.util.List;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.ViewHolder> {
    private List<Subject> subjectList;
    private Context context;

    public SubjectListAdapter(Context context, List<Subject> subjectList) {
        this.subjectList = subjectList;
        this.context = context;
    }
    public SubjectListAdapter(){

    }

    @NonNull
    @Override
    public SubjectListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.single_subject_layout,parent,false);
        return new SubjectListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectListAdapter.ViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.subject_name.setText(subject.getName());
        holder.teacherName.setText(subject.getTeacher_name());
    }

    @Override
    public int getItemCount() {return subjectList.size();}

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView subject_name,teacherName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject_name=itemView.findViewById(R.id.studentNameTV);
            teacherName=itemView.findViewById(R.id.studentIDv);
        }
    }
}
