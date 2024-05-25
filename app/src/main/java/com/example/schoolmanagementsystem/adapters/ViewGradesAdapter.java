package com.example.schoolmanagementsystem.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.Models.StudentGrade;
import com.example.schoolmanagementsystem.Models.Subject;
import com.example.schoolmanagementsystem.R;

import java.util.List;

public class ViewGradesAdapter extends RecyclerView.Adapter<ViewGradesAdapter.ViewHolder> {

    private static List<StudentGrade> studentGrades;
    private Context context;
    private static GradeUpdateListener mListener;

    public void setGradeUpdateListener(GradeUpdateListener listener) {
        this.mListener = listener;
    }


    public ViewGradesAdapter(List<StudentGrade> studentGrades, Context context,GradeUpdateListener listener) {
        this.studentGrades = studentGrades;
        this.context = context;
        this.mListener = listener;
    }

    public ViewGradesAdapter(){}

    @NonNull
    @Override
    public ViewGradesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.single_editablegrade_layout,parent,false);
        return new ViewGradesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewGradesAdapter.ViewHolder holder, int position) {
        StudentGrade studentGrade = studentGrades.get(position);
        holder.studentID.setText(studentGrade.getStudentId());
        holder.grade.setText(studentGrade.getGrade());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show dialog to edit the grade
                showEditDialog(studentGrade.getStudentId(), studentGrade.getGrade());
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentGrades.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView studentID, grade;
        Button editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentID = itemView.findViewById(R.id.studentNameTV);
            grade = itemView.findViewById(R.id.studentIDv);
            editButton = itemView.findViewById(R.id.editbtn);
            editButton.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Log.d("ViewHolder", "Edit button clicked");
            if (v.getId() == R.id.editbtn) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onUpdateGrade(studentGrades.get(position).getStudentId(), studentGrades.get(position).getGrade());
                }
            }
        }
    }



    private void showEditDialog(String studentId, String currentGrade) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Grade");

        // Set up the input field
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER); // Allow only numeric input
        input.setText(currentGrade);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newGrade = input.getText().toString().trim();
                if (!newGrade.isEmpty()) {
                    // Update the grade in the database
                    mListener.onUpdateGrade(studentId, newGrade);
                } else {
                    Toast.makeText(context, "Please enter a valid grade", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public interface GradeUpdateListener {
        void onUpdateGrade(String studentId, String newGrade);
    }


}
