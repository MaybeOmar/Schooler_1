package com.example.schoolmanagementsystem;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM message here
    }

    @Override
    public void onNewToken(String token) {
        // Handle new FCM token
    }
}
