package com.example.schoolmanagementsystem.adapters;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.EditMaterial;
import com.example.schoolmanagementsystem.Models.Student;
import com.example.schoolmanagementsystem.Models.StudentGrade;
import com.example.schoolmanagementsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditGradesAdapter extends RecyclerView.Adapter<EditGradesAdapter.ViewHolder> {
    private List<Student> studentList=new ArrayList<>();
    private List<String> inputData = new ArrayList<>();
    private List<StudentGrade> studentGrades = new ArrayList<>();
    private RecyclerView studentRV;

    private Context context;

    public EditGradesAdapter(List<StudentGrade> studentGrades,List<Student> studentList, Context context, RecyclerView studentRV) {
        this.studentList = studentList;
        this.studentGrades = studentGrades;
        this.context = context;
        this.studentRV = studentRV;
        for (int i = 0; i < studentList.size(); i++) {
            inputData.add("");
        }
    }

    public EditGradesAdapter(){

    }

    @NonNull
    @Override
    public EditGradesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.single_grade_layout,parent,false);
        return new EditGradesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Student_name.setText(studentList.get(position).getName());
        holder.Student_Id.setText(studentList.get(position).getId());

        // Check if position is within bounds of studentGrades
        if (position < studentGrades.size()) {
            StudentGrade studentGrade = studentGrades.get(position);
            holder.gradeET.setText(studentGrade.getGrade());
        } else {
            // Clear EditText if position exceeds studentGrades size
            holder.gradeET.setText("");
        }

        /*// Add onFocusChangeListener to prevent EditText from gaining focus again
        holder.gradeET.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                new Handler().postDelayed(() -> {
                    holder.gradeET.clearFocus();
                }, 5000);
            }
        });*/

        holder.gradeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("TextWatcher", "afterTextChanged called at position: " + holder.getAdapterPosition());



                if (holder.gradeET.hasFocus()) {
                    // Get the position of the ViewHolder
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition < inputData.size()) {
                        // Update the corresponding value in the inputData list
                        inputData.set(adapterPosition, s.toString());
                    }
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
        EditText gradeET;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Student_name=itemView.findViewById(R.id.takeattendanceStudentName);
            Student_Id =itemView.findViewById(R.id.takeattendanceStudentID);
            gradeET = itemView.findViewById(R.id.gradeET);
        }
    }
    public void uploadToFirebase(String intentYear, String intentClass, String intentExam, String intentSubject, List<String> studentIds) {
        DatabaseReference destinationRef = FirebaseDatabase.getInstance().getReference()
                .child("Schooler").child("Education Years").child(intentYear).child("Classes")
                .child(intentClass).child("Grades").child(intentExam).child("Subjects").child(intentSubject);

        // Update inputData list with the latest values from EditText fields
        for (int i = 0; i < studentList.size(); i++) {
            // Get the ViewHolder for the current position
            RecyclerView.ViewHolder viewHolder = studentRV.findViewHolderForAdapterPosition(i);
            if (viewHolder instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) viewHolder;
                String grade = holder.gradeET.getText().toString();
                inputData.set(i, grade);
            }
        }

        // Upload the updated values to Firebase
        for (int i = 0; i < inputData.size(); i++) {
            String studentID = studentIds.get(i);
            String grade = inputData.get(i);
            Log.d("GradeTracker", "Grades: " + grade);
            destinationRef.child(studentID).setValue(grade);
        }
    }

}
