package com.sktt1.butters.data.database.tables;

public class ActivityTable {
    public static final String TABLE = "activites";

    public static final String COL_ID = "id";

    public static final String COL_MESSAGE = "message";

    public static final String COL_NOTIFIED_ON = "notified_on";

    public static final String COL_HAS_READ = "has_read";

    public static String getCreateQuery() {
        return String.format("CREATE TABLE %s (%s INTEGER NOT NULL PRIMARY, %s TEXT NOT NULL, %s LONG NOT NULL, %s BOOLEAN NOT NULL)", TABLE, COL_ID, COL_MESSAGE, COL_NOTIFIED_ON, COL_HAS_READ);
    }
}
