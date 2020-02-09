package com.sktt1.butters.data.database.tables;

public class TagTable {
    public static final String TABLE = "tags";

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_MAC_ADDRESS = "mac_address";

    public static final String COL_LAST_SEEN_LOCATION_ID = "last_seen_location_id";

    public static final String COL_LAST_SEEN_TIME = "last_seen_time";

    public static final String COL_ALARM = "alarm";

    public static final String COL_IS_CONNECTED = "is_connected";


    public static String getCreateQuery() {
        return String.format("CREATE TABLE %s " +
                "(%s INTEGER PRIMARY KEY, %s TEXT NOT NULL, %s TEXT NOT NULL, " +
                "%s INTEGER NOT NULL," +
                "%s LONG NOT NULL, %s BOOLEAN NOT NULL, " +
                "%s INTEGER NOT NULL," +
                " FOREIGN KEY(%s) REFERENCES %s (%s) ON DELETE SET NULL)", TABLE, COL_ID, COL_NAME, COL_MAC_ADDRESS, COL_LAST_SEEN_LOCATION_ID, COL_LAST_SEEN_TIME, COL_IS_CONNECTED, COL_ALARM, COL_LAST_SEEN_LOCATION_ID, LocationTable.TABLE, LocationTable.COL_ID);
    }
}
