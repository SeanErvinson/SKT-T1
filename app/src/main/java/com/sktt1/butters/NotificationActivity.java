package com.sktt1.butters;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationActivity {

    private Context context;

    public NotificationActivity(Context context) {
        this.context = context;
    }

    private NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

    public void sendFMPNotification(String tagName) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.ALERT_PHONE_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Find my phone alarm")
                .setContentText(context.getString(R.string.activity_alerted, tagName))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{250, 1000, 250, 1000, 250, 1000, 250, 1000})
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SYSTEM)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManager.notify(1, builder.build());
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
