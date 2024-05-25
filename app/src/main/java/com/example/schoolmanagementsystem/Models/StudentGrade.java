package com.example.schoolmanagementsystem.Models;

public class StudentGrade {
    private String studentId;
    private String grade;

    public StudentGrade(){}
    public StudentGrade(String studentId, String grade) {
        this.studentId = studentId;
        this.grade = grade;
    }

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId + ", Grade: " + grade;
    }
}

