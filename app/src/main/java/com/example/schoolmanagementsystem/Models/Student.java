package com.example.schoolmanagementsystem.Models;

public class Student {

    String id;
    String name;
    String password;
    String phone;
    String year;
    String email;
    String Classroom;

    public Student(String id, String name, String password, String phone, String year, String email, String classroom) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.year = year;
        this.email = email;
        this.Classroom = classroom;
    }

    /*public Student(String id, String name,String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }*/

    public Student(){

    }



    public String getClassroom() {
        return Classroom;
    }

    public void setClassroom(String classroom) {
        Classroom = classroom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
