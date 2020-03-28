package com.sktt1.butters.data.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sktt1.butters.NotificationActivity;
import com.sktt1.butters.R;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.models.Tag;

public class TagBroadcastReceiver extends BroadcastReceiver {

    public final static String ACTION_GATT_CONNECTED = "com.sktt1.butters.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.sktt1.butters.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.sktt1.butters.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.sktt1.butters.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.sktt1.butters.EXTRA_DATA";
    public static final String ACTION_PHONE_ALERTED = "com.sktt1.butters.FMP_AVAILABLE";
    public static final String TAG_DATA = "com.sktt1.butters.TAG_DATA";
    public static final String GPS_LAT_DATA = "com.sktt1.butters.GPS_LAT_DATA";
    public static final String GPS_LNG_DATA = "com.sktt1.butters.GPS_LNG_DATA";
    public static final String FMP_DATA = "com.sktt1.butters.FMP_DATA";

    private final DatabaseHelper databaseHelper;

    public TagBroadcastReceiver(DatabaseHelper helper) {
        databaseHelper = helper;
    }


    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationActivity notificationActivity = new NotificationActivity(context);
        final String action = intent.getAction();

        if (ACTION_PHONE_ALERTED.equals(action)) {
            // TODO fmp
            final BluetoothDevice bluetoothDevice = intent.getParcelableExtra(TagBroadcastReceiver.EXTRA_DATA);
            Tag tag = databaseHelper.getTagByMacAddress(bluetoothDevice.getAddress());
            databaseHelper.activityCreateNotification(context.getString(R.string.activity_alerted,tag.getName()));
            notificationActivity.sendFMPNotification(tag.getName());
            // Play sound
            intent.getIntExtra(FMP_DATA, 0);
        } else if (ACTION_GATT_CONNECTED.equals(action)) {
            databaseHelper.activityCreateNotification(context.getString(R.string.activity_connected));
        } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
            final BluetoothDevice bluetoothDevice = intent.getParcelableExtra(TagBroadcastReceiver.EXTRA_DATA);
            Tag tag = databaseHelper.getTagByMacAddress(bluetoothDevice.getAddress());
            databaseHelper.activityCreateNotification(context.getString(R.string.activity_disconnected, tag.getName()));
            notificationActivity.sendDisconnectionNotification(tag.getName());
        } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
        } else if (ACTION_DATA_AVAILABLE.equals(action)) {
        }
    }
}
