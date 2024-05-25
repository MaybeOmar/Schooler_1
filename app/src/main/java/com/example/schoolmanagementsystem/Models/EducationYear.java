package com.example.schoolmanagementsystem.Models;

// EducationYear.java

import javax.security.auth.Subject;

public class EducationYear {
    private Subject Arabic;
    private Subject English;
    private Subject Math;

    public EducationYear() {
        // Default constructor required for Firebase
    }

    public EducationYear(Subject arabic, Subject english, Subject math) {
        Arabic = arabic;
        English = english;
        Math = math;
    }

    public Subject getArabic() {
        return Arabic;
    }

    public void setArabic(Subject arabic) {
        Arabic = arabic;
    }

    public Subject getEnglish() {
        return English;
    }

    public void setEnglish(Subject english) {
        English = english;
    }

    public Subject getMath() {
        return Math;
    }

    public void setMath(Subject math) {
        Math = math;
    }
}

/*
public class EducationYear {
    private String subject_name;

    private String teacher;


    public EducationYear(){

    }

    public EducationYear(String subject_name, String teacher) {
        this.subject_name = subject_name;
        this.teacher = teacher;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
*/
