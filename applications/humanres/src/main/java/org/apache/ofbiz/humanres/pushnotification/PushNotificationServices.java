/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.humanres.pushnotification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PushNotificationServices {

    public static String sendMessage(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, UnsupportedEncodingException, ExecutionException {

        try {
            FileInputStream serviceAccount
                    = new FileInputStream("framework\\images\\webapp\\images\\fcmfiles\\pushtest-232ad-firebase-adminsdk-ujtij-b33273d849.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://pushtest-232ad.firebaseio.com")
                    .build();

            FirebaseApp firebaseApp = null;
            List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
            if (firebaseApps != null && !firebaseApps.isEmpty()) {
                for (FirebaseApp app : firebaseApps) {
                    if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                        firebaseApp = app;
                    }
                }
            } else {
                firebaseApp = FirebaseApp.initializeApp(options);
            }

        } catch (IOException ex) {
            System.out.println(ex);
            Logger.getLogger(PushNotificationServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        // This registration token comes from the client FCM SDKs.
        String registrationToken = "dlaUx_JDQUI:APA91bE4cLHoWno5ZXsf_H2BFUQkGAi97etq4Vvbq9EYCxrtPxF98GLms30nXxjRo5mHkn68LKR2kKgCOD9mBk3sR1EV8GcgeID9kzxSXhDQ_dMmz4ecNXfpltUusDz7YEy-SrGS9HTi";

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(registrationToken)
                .build();

        // Send a message to the device corresponding to the provided
// registration token.
        String responseMessage;
        try {
            responseMessage = FirebaseMessaging.getInstance().sendAsync(message).get();
            // Response is a message ID string.
            System.out.println("Successfully sent message: " + responseMessage);

        } catch (InterruptedException
                | ExecutionException ex) {
            System.out.println(ex);
            Logger.getLogger(PushNotificationServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "success";
    }

}

