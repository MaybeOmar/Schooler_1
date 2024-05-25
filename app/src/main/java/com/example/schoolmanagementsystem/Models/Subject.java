// Subject.java
package com.example.schoolmanagementsystem.Models;

public class Subject {
    private String name;
    private String teacher_name;

    public Subject() {
        // Default constructor required for Firebase
    }

    public Subject(String name, String teacher_name) {
        this.name = name;
        this.teacher_name = teacher_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }
}

