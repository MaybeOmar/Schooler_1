package com.example.schoolmanagementsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.Models.Student;
import com.example.schoolmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class TakeAttendanceAdapter extends RecyclerView.Adapter<TakeAttendanceAdapter.ViewHolder> {
    private List<Student> studentList=new ArrayList<>();
    private Context context;
    public  static List<String> presentList=new ArrayList<>();
    public  static List<String> absentList=new ArrayList<>();

    public TakeAttendanceAdapter(List<Student> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;
    }

    public TakeAttendanceAdapter(){

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.single_attendance_layout,parent,false);
        return new TakeAttendanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Student_name.setText(studentList.get(holder.getAdapterPosition()).getName());
        holder.Student_Id.setText(studentList.get(holder.getAdapterPosition()).getId());

        holder.presentRBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!presentList.contains(studentList.get(holder.getAdapterPosition()).getId())){
                        presentList.add(studentList.get(holder.getAdapterPosition()).getId());

                    }

                }else {
                    presentList.remove(studentList.get(holder.getAdapterPosition()).getId());
                }
            }
        });
        holder.absentRBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    absentList.add(studentList.get(holder.getAdapterPosition()).getId());
                }
                else {
                    absentList.remove(studentList.get(holder.getAdapterPosition()).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Student_name;
        TextView Student_Id;
        RadioButton presentRBtn;
        RadioButton absentRBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Student_name=itemView.findViewById(R.id.takeattendanceStudentName);
            Student_Id =itemView.findViewById(R.id.takeattendanceStudentID);
            presentRBtn=itemView.findViewById(R.id.presentRadioBtn);
            absentRBtn=itemView.findViewById(R.id.absentRadioBtn);
        }

    }
}
