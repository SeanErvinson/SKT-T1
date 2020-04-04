package com.sktt1.butters;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.sktt1.butters.data.preference.SharedPreferenceHelper;

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
            phoneAlertChannel.setVibrationPattern(new long[]{250, 1000, 250, 1000, 250, 1000, 250, 1000});
            phoneAlertChannel.setDescription("Phone alert");

            SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
            Uri ringtoneSound = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
            if (sharedPreferenceHelper.getUserFindMyPhoneAlarm() != null) {
                ringtoneSound = Uri.parse(sharedPreferenceHelper.getUserFindMyPhoneAlarm());
            }
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();

            phoneAlertChannel.setSound(ringtoneSound, audioAttributes);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(generalChannel);
            notificationManager.createNotificationChannel(alertChannel);
            notificationManager.createNotificationChannel(phoneAlertChannel);
        }
    }
}
