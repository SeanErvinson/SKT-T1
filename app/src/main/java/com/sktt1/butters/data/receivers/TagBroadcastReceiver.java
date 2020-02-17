package com.sktt1.butters.data.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TagBroadcastReceiver extends BroadcastReceiver {

    public final static String ACTION_GATT_CONNECTED = "com.sktt1.butters.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.sktt1.butters.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.sktt1.butters.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.sktt1.butters.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.sktt1.butters.EXTRA_DATA";
    public static final String ACTION_PHONE_ALERTED= "com.sktt1.butters.FMP_DATA";
    public static final String GPS_LAT_DATA = "com.sktt1.butters.GPS_LAT_DATA";
    public static final String GPS_LNG_DATA = "com.sktt1.butters.GPS_LNG_DATA";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ACTION_PHONE_ALERTED.equals(action)) {
            // Send notification
            // Play sound
        } else if (ACTION_GATT_CONNECTED.equals(action)) {
            // Send notification
            // Get tag information through extra data
        } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
            // Send notification
            // Get tag information through extra data
        } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
        } else if (ACTION_DATA_AVAILABLE.equals(action)) {
        }
    }
}
