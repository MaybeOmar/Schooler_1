package com.example.schoolmanagementsystem.Models;

public class announcment_model {
    String id, title, description, fullName; // Adding fullName attribute

    public announcment_model(String id, String title, String description, String fullName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fullName = fullName; // Assigning fullName in constructor
    }

    public announcment_model(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
