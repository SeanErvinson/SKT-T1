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
        sqLiteDatabase.execSQL(LocationTable.getCreateQuery());
        sqLiteDatabase.execSQL(TagTable.getCreateQuery());
        sqLiteDatabase.execSQL(ActivityTable.getCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Activity table interactions
    public int activityCreateNotification(String message, String notifiedOn) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues activityValues = new ContentValues();
        activityValues.put(ActivityTable.COL_MESSAGE, message);
        activityValues.put(ActivityTable.COL_NOTIFIED_ON, notifiedOn);
        activityValues.put(ActivityTable.COL_HAS_READ, "false");
        return (int) sqLiteDatabase.insert(ActivityTable.TABLE, null, activityValues);
    }

    public void activityUpdateNotification(int id) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues activityValues = new ContentValues();
        activityValues.put(ActivityTable.COL_HAS_READ, "true");
        sqLiteDatabase.update(ActivityTable.TABLE, activityValues, "id = ?", new String[] {Long.toString(id)});
    }

    public Cursor activityFeedList () {
        sqLiteDatabase = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s ", ActivityTable.TABLE);
        return sqLiteDatabase.rawQuery(query, null);
    }

    // Tag table interactions

    public int tagCreateDevice(String name, String macAddress, int lastSendLocationId, String lastSeenTime, boolean isConnected){

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues tagValues = new ContentValues();

        tagValues.put(TagTable.COL_NAME, name);
        tagValues.put(TagTable.COL_MAC_ADDRESS, macAddress);
        tagValues.put(TagTable.COL_LAST_SEEN_LOCATION_ID, lastSendLocationId);
        tagValues.put(TagTable.COL_LAST_SEEN_TIME, lastSeenTime);
        tagValues.put(TagTable.COL_IS_CONNECTED, Boolean.toString(isConnected));

        return (int) sqLiteDatabase.insert(TagTable.TABLE, null, tagValues);
    }

    public void tagUpdateName(String name, int id){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues tagValues = new ContentValues();
        tagValues.put(TagTable.COL_NAME, name);
        sqLiteDatabase.update(TagTable.TABLE, tagValues, "id = ?", new String[] {Integer.toString(id)});
    }

    public void tagUpdateLocation(int tagId, int locationId, String lastSeenTime, String longitude, String latitude){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues tagValues = new ContentValues();
        tagValues.put(TagTable.COL_LAST_SEEN_TIME, lastSeenTime);
        sqLiteDatabase.update(TagTable.TABLE,  tagValues, "id = ?", new String[] {Integer.toString(tagId)});

        ContentValues locationValues = new ContentValues();
        locationValues.put(LocationTable.COL_LATITUDE, latitude);
        locationValues.put(LocationTable.COL_LONGITUDE, longitude);
        sqLiteDatabase.update(LocationTable.TABLE, locationValues, "id = ?", new String[] {Integer.toString(locationId)});
    }

    public void tagUpdateConnection(int id, boolean isConnected){

        sqLiteDatabase = this.getWritableDatabase();
        ContentValues tagValues = new ContentValues();
        tagValues.put(TagTable.COL_IS_CONNECTED, Boolean.toString(!isConnected));
        sqLiteDatabase.update(TagTable.TABLE, tagValues,"id = ?", new String[] {Integer.toString(id)});
    }

    public Cursor tagFeedList(){
        sqLiteDatabase = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s ", TagTable.TABLE);
        return sqLiteDatabase.rawQuery(query, null);
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
