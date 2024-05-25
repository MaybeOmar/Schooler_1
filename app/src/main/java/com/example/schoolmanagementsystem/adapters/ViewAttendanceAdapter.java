package com.example.schoolmanagementsystem.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolmanagementsystem.Models.Student;
import com.example.schoolmanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAttendanceAdapter extends RecyclerView.Adapter<ViewAttendanceAdapter.ViewHolder> {
    private List<Student> studentList=new ArrayList<>();
    private List<String> presentList=new ArrayList<>();
    private List<String> absentList=new ArrayList<>();

    private Context context;
    private DatabaseReference presentRef,absentRef;

    public ViewAttendanceAdapter(Context context, List<Student> studentList) {

        this.context = context;
        this.studentList = studentList;
    }
    public ViewAttendanceAdapter(){

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.single_student_layout,parent,false);
        return new ViewAttendanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewAttendanceViewHolder, final int i) {
        final Student currentStudent = studentList.get(viewAttendanceViewHolder.getAdapterPosition());
        viewAttendanceViewHolder.Student_name.setText(currentStudent.getName());
        viewAttendanceViewHolder.studentEmail.setText(currentStudent.getEmail());
        viewAttendanceViewHolder.id.setText(currentStudent.getId());

        viewAttendanceViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int adapterPosition = viewAttendanceViewHolder.getAdapterPosition();

                    presentRef = FirebaseDatabase.getInstance().getReference()
                            .child("Schooler")
                            .child("Education Years")
                            .child(currentStudent.getYear())
                            .child("Classes")
                            .child(currentStudent.getClassroom())
                            .child("Attendance");

                    presentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            presentList.clear();
                            absentList.clear();
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Present").getChildren()) {
                                        String key = dataSnapshot2.getKey();
                                        if (adapterPosition != -1 && key.equals(currentStudent.getId())) {
                                            presentList.add(key);
                                        }
                                    }

                                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Absent").getChildren()) {
                                        String key = dataSnapshot2.getValue().toString();
                                        if (key.equals(currentStudent.getId())) {
                                            absentList.add(key);
                                        }
                                    }
                                }

                                AlertDialog dialoga = new AlertDialog.Builder(context).create();
                                View view = LayoutInflater.from(context).inflate(R.layout.viewattendance, null);

                                TextView presentTV = view.findViewById(R.id.presentStudentTV1);
                                TextView absentTV = view.findViewById(R.id.absentStudentTV1);
                                TextView name = view.findViewById(R.id.vName);
                                TextView id = view.findViewById(R.id.vID);
                                Button button = view.findViewById(R.id.Okbtn);

                                name.setText(currentStudent.getName());
                                id.setText(currentStudent.getId());
                                presentTV.setText(String.valueOf(presentList.size()));
                                absentTV.setText(String.valueOf(absentList.size()));

                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialoga.dismiss();
                                    }
                                });

                                dialoga.setView(view);
                                dialoga.setCancelable(true);
                                dialoga.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled
                        }
                    });
                }
        });
    }


    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Student_name;
        TextView studentEmail;
        TextView id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Student_name=itemView.findViewById(R.id.studentNameTV);
            studentEmail =itemView.findViewById(R.id.studentEmailTv);
            id=itemView.findViewById(R.id.studentIDv);
        }
    }
}
