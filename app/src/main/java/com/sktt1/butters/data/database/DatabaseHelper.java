package com.sktt1.butters.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import com.sktt1.butters.data.database.tables.ActivityTable;
import com.sktt1.butters.data.database.tables.LocationTable;
import com.sktt1.butters.data.database.tables.TagTable;
import com.sktt1.butters.data.models.Activity;
import com.sktt1.butters.data.models.Tag;

import java.lang.reflect.Array;
import java.sql.Date;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "skt.db";
    public static final int DB_VERSION=1;


    private SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(LocationTable.getCreateQuery());
        sqLiteDatabase.execSQL(TagTable.getCreateQuery());
        sqLiteDatabase.execSQL(ActivityTable.getCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Activity table interactions
    public long activityCreateNotification(String message, String notified_on) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues activityValues = new ContentValues();
        activityValues.put(ActivityTable.COL_MESSAGE, message);
        activityValues.put(ActivityTable.COL_NOTIFIED_ON, notified_on);
        activityValues.put(ActivityTable.COL_HAS_READ, "false");
        return sqLiteDatabase.insert(ActivityTable.TABLE, null, activityValues);
    }

    public void activityUpdateNotification(DatabaseHelper databaseHelper, long id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues activityValues = new ContentValues();
        activityValues.put(ActivityTable.COL_HAS_READ, "true");
        sqLiteDatabase.update(ActivityTable.TABLE, activityValues, "id = ?", new String[] {Long.toString(id)});
    }

//    public ArrayList<Activity> activityFeedList () {
//        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
//        String[] projection = {
//                ActivityTable.COL_ID,
//                ActivityTable.COL_MESSAGE,
//                ActivityTable.COL_HAS_READ,
//                ActivityTable.COL_NOTIFIED_ON
//        };
//        String selection = ActivityTable.COL_HAS_READ + " = ?";
//        String[] selectionArgs = {"false"};
//        String sortOrder = ActivityTable.COL_NOTIFIED_ON + "DESC";
//        Cursor cursor = sqLiteDatabase.query(
//                ActivityTable.TABLE,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                sortOrder
//        );
//        ArrayList<Activity> activity = new ArrayList<>();
//        while(cursor.moveToNext()){
//            final int id = Integer.parseInt(cursor.getString(0));
//            final String message = cursor.getString(1);
//            final Date notifiedOn = Date.valueOf(cursor.getString(2));
//            final boolean hasRead = Boolean.parseBoolean(cursor.getString(3));
//            activity.add(
//                    new Activity(){{
//                        setId(id);
//                        setMessage(message);
//                        setNotifiedOn(notifiedOn);
//                        setHasRead(hasRead);
//                    }}
//            );
//        }
//        cursor.close();
//        return activity;
//    }

    // Tag table interactions

    public long tagCreateDevice(DatabaseHelper databaseHelper, String name, String mac_address, String last_send_location_id, String last_seen_time, boolean is_connected){

        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues tagValues = new ContentValues();

        tagValues.put(TagTable.COL_NAME, name);
        tagValues.put(TagTable.COL_MAC_ADDRESS, mac_address);
        tagValues.put(TagTable.COL_LAST_SEEN_LOCATION_ID, last_send_location_id);
        tagValues.put(TagTable.COL_LAST_SEEN_TIME, last_seen_time);
        tagValues.put(TagTable.COL_IS_CONNECTED, Boolean.toString(is_connected));

        return sqLiteDatabase.insert(TagTable.TABLE, null, tagValues);
    }

    // Location table interactions

    public long locationCreateRow(DatabaseHelper databaseHelper, String name, String longitude, String latitude) {

        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues locationValues = new ContentValues();
        locationValues.put(LocationTable.COL_MESSAGE, name);
        locationValues.put(LocationTable.COL_LONGITUDE, longitude);
        locationValues.put(LocationTable.COL_LATITUDE, latitude);

        return sqLiteDatabase.insert(LocationTable.TABLE, null, locationValues);
    }
}
