package com.example.schoolmanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.schoolmanagementsystem.Models.Student;
import com.example.schoolmanagementsystem.Models.Teacher;

import static android.content.Context.MODE_PRIVATE;

public class SaveUser {
    public void Student_saveData(Context context,Boolean b) {
        SharedPreferences pref = context.getSharedPreferences("SV", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isUserLogIn", b);
        editor.commit();

    }

    public void saveStudent(Context context, Student student){
        SharedPreferences sharedPreferences=context.getSharedPreferences("STUDENT",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("id",student.getId());
        editor.putString("name",student.getName());
        editor.putString("phone",student.getPhone());
        editor.putString("year",student.getYear());
        editor.putString("email",student.getEmail());
        editor.putString("class",student.getClassroom());
        editor.putString("password",student.getPassword());
        editor.apply();
    }

    public Student getStudent(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("STUDENT",Context.MODE_PRIVATE);
        Student student=new Student(sharedPreferences.getString("id",null),
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("password",null),
                sharedPreferences.getString("phone",null),
                sharedPreferences.getString("year",null),
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("class",null)
        );
        return student;
    }
}