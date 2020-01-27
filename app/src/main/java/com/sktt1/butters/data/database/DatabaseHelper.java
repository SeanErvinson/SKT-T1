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
import com.sktt1.butters.data.models.Location;
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
//        sqLiteDatabase.execSQL(LocationTable.getCreateQuery());
//        sqLiteDatabase.execSQL(TagTable.getCreateQuery());
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

    public void activityUpdateNotification( long id) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues activityValues = new ContentValues();
        activityValues.put(ActivityTable.COL_HAS_READ, "true");
        sqLiteDatabase.update(ActivityTable.TABLE, activityValues, "id = ?", new String[] {Long.toString(id)});
    }

    public Cursor activityFeedList () {
        sqLiteDatabase = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s ", ActivityTable.TABLE, "false");
        return sqLiteDatabase.rawQuery(query, null);
    }

    // Tag table interactions

    public long tagCreateDevice(String name, String mac_address, String last_send_location_id, String last_seen_time, boolean is_connected){

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues tagValues = new ContentValues();

        tagValues.put(TagTable.COL_NAME, name);
        tagValues.put(TagTable.COL_MAC_ADDRESS, mac_address);
        tagValues.put(TagTable.COL_LAST_SEEN_LOCATION_ID, last_send_location_id);
        tagValues.put(TagTable.COL_LAST_SEEN_TIME, last_seen_time);
        tagValues.put(TagTable.COL_IS_CONNECTED, Boolean.toString(is_connected));

        return sqLiteDatabase.insert(TagTable.TABLE, null, tagValues);
    }

    public void tagUpdateName(String name, long id){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues tagValues = new ContentValues();
        tagValues.put(TagTable.COL_NAME, name);
        sqLiteDatabase.update(TagTable.TABLE, tagValues, "id = ?", new String[] {Long.toString(id)});
    }

    public void tagUpdateLocation(long tagId, long locationId, String lastSeenTime, String longitude, String latitude){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues tagValues = new ContentValues();
        tagValues.put(TagTable.COL_LAST_SEEN_TIME, lastSeenTime);
        sqLiteDatabase.update(TagTable.TABLE,  tagValues, "id = ?", new String[] {Long.toString(tagId)});

        ContentValues locationValues = new ContentValues();
        locationValues.put(LocationTable.COL_LATITUDE, latitude);
        locationValues.put(LocationTable.COL_LONGITUDE, longitude);
        sqLiteDatabase.update(LocationTable.TABLE, locationValues, "id = ?", new String[] {Long.toString(locationId)});
    }
    // Location table interactions

    public long locationCreateRow(String name, String longitude, String latitude) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues locationValues = new ContentValues();
        locationValues.put(LocationTable.COL_MESSAGE, name);
        locationValues.put(LocationTable.COL_LONGITUDE, longitude);
        locationValues.put(LocationTable.COL_LATITUDE, latitude);

        return sqLiteDatabase.insert(LocationTable.TABLE, null, locationValues);
    }
}
