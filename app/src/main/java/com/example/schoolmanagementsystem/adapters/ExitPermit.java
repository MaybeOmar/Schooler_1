package com.example.schoolmanagementsystem.adapters;
public class ExitPermit {
    private String date;
    private String parentID;
    private String time;

    // Constructors, getters, and setters
    public ExitPermit() {
        // Default constructor required for Firestore
    }

    public ExitPermit(String date, String parentID, String time) {
        this.date = date;
        this.parentID = parentID;
        this.time = time;
    }

    // Getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
