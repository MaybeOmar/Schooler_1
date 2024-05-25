package com.example.schoolmanagementsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.Models.Student;
import com.example.schoolmanagementsystem.Models.Teacher;
import com.example.schoolmanagementsystem.R;

import java.util.List;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.ViewHolder> {
    private List<Teacher> teacherList;
    private Context context;

    public TeacherListAdapter(List<Teacher> teacherList, Context context) {
        this.teacherList = teacherList;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.single_student_layout,parent,false);
        return new TeacherListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherListAdapter.ViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);
        holder.teacherName.setText(teacher.getName());
        holder.id.setText(teacher.getId());
        holder.email.setText(teacher.getEmail());
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName, id, email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.studentNameTV);
            id = itemView.findViewById(R.id.studentIDv);
            email = itemView.findViewById(R.id.studentEmailTv);
        }
    }
}
