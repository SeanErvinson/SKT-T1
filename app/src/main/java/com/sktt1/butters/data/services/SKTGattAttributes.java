package com.sktt1.butters.data.services;

import java.util.HashMap;

public class SKTGattAttributes {
    private static HashMap<String, String> attributes = new HashMap<>();

    public final static String UUID_GPS_SERVICE = "0000DD00-0000-1000-8000-00805f9b34fb";
    public final static String UUID_LAT_CHAR = "0000DD02-0000-1000-8000-00805f9b34fb";
    public final static String UUID_LNG_CHAR = "0000DD01-0000-1000-8000-00805f9b34fb";

    public final static String UUID_FMP_SERVICE = "0000FF00-0000-1000-8000-00805f9b34fb";
    public final static String UUID_FMP_CHAR = "0000FF01-0000-1000-8000-00805f9b34fb";

    public final static String UUID_ALERT_SERVICE = "0000AA00-0000-1000-8000-00805f9b34fb";
    public final static String UUID_ALERT_CHAR = "0000AA01-0000-1000-8000-00805f9b34fb";

    final static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    static {
        attributes.put(UUID_GPS_SERVICE, "GPS Service");
        attributes.put(UUID_LAT_CHAR, "Latitude Data");
        attributes.put(UUID_LNG_CHAR, "Longitude Data");
        attributes.put(UUID_FMP_SERVICE, "Find My Phone Service");
        attributes.put(UUID_FMP_CHAR, "Find My Phone Data");
        attributes.put(UUID_ALERT_SERVICE, "Alert Service");
        attributes.put(UUID_ALERT_CHAR, "Alert Data");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
