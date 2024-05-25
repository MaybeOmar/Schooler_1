package com.example.schoolmanagementsystem.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.Models.Student;
import com.example.schoolmanagementsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EnterGradesAdapter extends RecyclerView.Adapter<EnterGradesAdapter.ViewHolder> {
    private List<Student> studentList=new ArrayList<>();
    private List<String> inputData = new ArrayList<>();

    private Context context;

    public EnterGradesAdapter(List<Student> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;

        for (int i = 0; i < studentList.size(); i++) {
            inputData.add("");
        }
    }
    public EnterGradesAdapter(){

    }

    @NonNull
    @Override
    public EnterGradesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.single_grade_layout,parent,false);
        return new EnterGradesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnterGradesAdapter.ViewHolder holder, int position) {
        holder.Student_name.setText(studentList.get(holder.getAdapterPosition()).getName());
        holder.Student_Id.setText(studentList.get(holder.getAdapterPosition()).getId());


        holder.gradeET.setText(inputData.get(position));
        holder.gradeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Store user input data locally
                inputData.set(holder.getAdapterPosition(), s.toString());
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
        EditText gradeET;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Student_name=itemView.findViewById(R.id.takeattendanceStudentName);
            Student_Id =itemView.findViewById(R.id.takeattendanceStudentID);
            gradeET = itemView.findViewById(R.id.gradeET);
        }
    }
    public void uploadToFirebase(String intentYear, String intentClass, String intentExam, String intentSubject, List<String> studentIds){
       DatabaseReference destinationRef = FirebaseDatabase.getInstance().getReference().child("Schooler").child("Education Years").child(intentYear).child("Classes").child(intentClass).child("Grades").child(intentExam).child("Subjects").child(intentSubject);
        for (int i = 0; i < inputData.size(); i++) {
            String studentID = studentIds.get(i);
            String grade = inputData.get(i);
            destinationRef.child(studentID).setValue(grade);
        }
    }
}
