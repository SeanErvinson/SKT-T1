package com.sktt1.butters;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.sktt1.butters.data.receivers.TagBroadcastReceiver;

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

        Intent intentAction = new Intent(TagBroadcastReceiver.ACTION_STOP_SOUND);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context,6141,intentAction,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.ALERT_PHONE_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Find my phone alarm")
                .setContentText(context.getString(R.string.activity_alerted, tagName))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{250, 1000, 250, 1000, 250, 1000, 250, 1000})
                .addAction(R.mipmap.ic_launcher, "STOP SOUND", stopPendingIntent)
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SYSTEM)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }

    public void sendSitAlertedNotification(String tagName) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.ALERT_PHONE_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Find my phone alarm")
                .setContentText(context.getString(R.string.activity_sit_alerted, tagName))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{250, 1000, 250, 1000, 250, 1000, 250, 1000})
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_SYSTEM)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (notificationManager != null) {
            notificationManager.notify(4, builder.build());
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


    public void sendConnectionNotification(String tagName) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.TAG_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Tag disconnection")
                .setContentText(context.getString(R.string.activity_connection, tagName))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(3, builder.build());
    }

}
