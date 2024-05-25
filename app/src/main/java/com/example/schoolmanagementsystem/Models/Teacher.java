package com.example.schoolmanagementsystem.Models;

public class Teacher {
    String id;
    String name;
    String password;
    String phone;
    String age;
    String email;

    public Teacher(String id, String name, String password, String phone, String year, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.age = year;
        this.email = email;
    }

    /*public Student(String id, String name,String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }*/

    public Teacher(){

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
