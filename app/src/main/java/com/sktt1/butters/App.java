package com.sktt1.butters;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String TAG_NOTIFICATION_CHANNEL = "TAG_NOTIFICATION_CHANNEL";
    public static final String ALERT_NOTIFICATION_CHANNEL = "ALERT_NOTIFICATION_CHANNEL";
    public static final String ALERT_PHONE_NOTIFICATION_CHANNEL = "ALERT_PHONE_NOTIFICATION_CHANNEL";


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel generalChannel = new NotificationChannel(TAG_NOTIFICATION_CHANNEL, "General Tag", NotificationManager.IMPORTANCE_DEFAULT);
            generalChannel.setDescription("General alert");

            NotificationChannel alertChannel = new NotificationChannel(ALERT_NOTIFICATION_CHANNEL, "Alert Tag", NotificationManager.IMPORTANCE_HIGH);
            alertChannel.setDescription("Alert");

            NotificationChannel phoneAlertChannel = new NotificationChannel(ALERT_PHONE_NOTIFICATION_CHANNEL, "Phone Alert Tag", NotificationManager.IMPORTANCE_HIGH);
            phoneAlertChannel.enableLights(true);
            phoneAlertChannel.enableVibration(true);
            phoneAlertChannel.setVibrationPattern(new long[]{250,1000, 250,1000,250,1000,250,1000});
            phoneAlertChannel.setDescription("Phone alert");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(generalChannel);
            notificationManager.createNotificationChannel(alertChannel);
            notificationManager.createNotificationChannel(phoneAlertChannel);
        }
    }
}
