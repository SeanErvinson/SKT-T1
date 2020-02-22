package com.sktt1.butters.data.database.tables;

public class LocationTable {
    public static final String TABLE = "locations";

    public static final String COL_ID = "id";

    public static final String COL_MESSAGE = "name";

    public static final String COL_LONGITUDE = "longitude";

    public static final String COL_LATITUDE = "latitude";

    public static String getCreateQuery() {
        return String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT NOT NULL, %s REAL NOT NULL, %s REAL NOT NULL)", TABLE, COL_ID, COL_MESSAGE, COL_LONGITUDE, COL_LATITUDE);
    }
}
