package com.example.schoolmanagementsystem.Models;

public class Model {
    private int buttonImageResourceId;
    private Class<?> destinationActivity;
    private String cardText; // Add this field

    public Model(int buttonImageResourceId, Class<?> destinationActivity, String cardText) {
        this.buttonImageResourceId = buttonImageResourceId;
        this.destinationActivity = destinationActivity;
        this.cardText = cardText;
    }

    public int getButtonImageResourceId() {
        return buttonImageResourceId;
    }

    public Class<?> getDestinationActivity() {
        return destinationActivity;
    }

    public String getCardText() {
        return cardText;
    }
}
