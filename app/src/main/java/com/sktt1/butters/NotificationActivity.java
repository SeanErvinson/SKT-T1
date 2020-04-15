package com.sktt1.butters;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.sktt1.butters.data.preference.SharedPreferenceHelper;

public class NotificationActivity {

    private Context context;

    public NotificationActivity(Context context) {
        this.context = context;
        notificationManager = NotificationManagerCompat.from(this.context);
    }

    private NotificationManagerCompat notificationManager;
    private MediaPlayer mp;

    public void sendFMPNotification(String tagName) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(context);
        Uri ringtoneSound = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
        if (sharedPreferenceHelper.getUserFindMyPhoneAlarm() != null) {
            ringtoneSound = Uri.parse(sharedPreferenceHelper.getUserFindMyPhoneAlarm());
        }

        if (mp == null) {
            mp = MediaPlayer.create(context, ringtoneSound);
            mp.start();
        } else {
            mp.release();
            mp = null;
            mp = MediaPlayer.create(context, ringtoneSound);
            mp.start();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.ALERT_PHONE_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Find my phone alarm")
                .setContentText(context.getString(R.string.activity_alerted, tagName))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{250, 1000, 250, 1000, 250, 1000, 250, 1000})
                .setAutoCancel(true)
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SYSTEM)
                .setPriority(NotificationCompat.PRIORITY_HIGH);



        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setSound(ringtoneSound);
        }
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }

    public void sendDisconnectionNotification(String tagName) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.TAG_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Tag disconnection")
                .setContentText(context.getString(R.string.activity_disconnected, tagName))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(2, builder.build());
    }
}
