package com.sktt1.butters.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.sktt1.butters.data.database.tables.ActivityTable;
import com.sktt1.butters.data.database.tables.LocationTable;
import com.sktt1.butters.data.database.tables.TagTable;
import com.sktt1.butters.data.models.Activity;
import com.sktt1.butters.data.models.Location;
import com.sktt1.butters.data.models.Tag;
import com.sktt1.butters.data.utilities.DateTimePattern;
import com.sktt1.butters.data.utilities.DateUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "skt.db";
    public static final int DB_VERSION = 1;


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
    public int activityCreateNotification(String message) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues activityValues = new ContentValues();
        activityValues.put(ActivityTable.COL_MESSAGE, message);
        activityValues.put(ActivityTable.COL_NOTIFIED_ON, new Date().getTime());
        activityValues.put(ActivityTable.COL_HAS_READ, false);
        return (int) sqLiteDatabase.insert(ActivityTable.TABLE, null, activityValues);
    }

    public void activityUpdateNotification(int id) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues activityValues = new ContentValues();
        activityValues.put(ActivityTable.COL_HAS_READ, "true");
        sqLiteDatabase.update(ActivityTable.TABLE, activityValues, "id = ?", new String[]{Long.toString(id)});
    }

    public Cursor activityFeedList() {
        sqLiteDatabase = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s ", ActivityTable.TABLE);
        return sqLiteDatabase.rawQuery(query, null);
    }

    // Tag table interactions

    public int tagCreateDevice(String name, String macAddress, int lastSendLocationId, long lastSeenTime) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues tagValues = new ContentValues();

        tagValues.put(TagTable.COL_NAME, name);
        tagValues.put(TagTable.COL_MAC_ADDRESS, macAddress);
        tagValues.put(TagTable.COL_LAST_SEEN_LOCATION_ID, lastSendLocationId);
        tagValues.put(TagTable.COL_LAST_SEEN_TIME, lastSeenTime);
        tagValues.put(TagTable.COL_ALARM, 1);

        return (int) sqLiteDatabase.insert(TagTable.TABLE, null, tagValues);
    }

    public void tagUpdateName(String name, int id) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues tagValues = new ContentValues();
        tagValues.put(TagTable.COL_NAME, name);
        sqLiteDatabase.update(TagTable.TABLE, tagValues, "id = ?", new String[]{Integer.toString(id)});
    }

    public void tagUpdateLocation(int tagId, int locationId, String lastSeenTime, String longitude, String latitude) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues tagValues = new ContentValues();
        tagValues.put(TagTable.COL_LAST_SEEN_TIME, lastSeenTime);
        sqLiteDatabase.update(TagTable.TABLE, tagValues, "id = ?", new String[]{Integer.toString(tagId)});

        ContentValues locationValues = new ContentValues();
        locationValues.put(LocationTable.COL_LATITUDE, latitude);
        locationValues.put(LocationTable.COL_LONGITUDE, longitude);
        sqLiteDatabase.update(LocationTable.TABLE, locationValues, "id = ?", new String[]{Integer.toString(locationId)});
    }

    public void tagUpdateSoundAlarm(int id, int alarm) {

        sqLiteDatabase = this.getWritableDatabase();
        ContentValues tagValues = new ContentValues();
        tagValues.put(TagTable.COL_ALARM, alarm);
        sqLiteDatabase.update(TagTable.TABLE, tagValues, "id = ?", new String[]{Integer.toString(id)});
    }

    public Cursor tagFeedList() {
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

    public Tag getTagByMacAddress(String macAddress){
        sqLiteDatabase = this.getReadableDatabase();
        String columnTitle = TagTable.COL_MAC_ADDRESS + " = ?";
        String[] columnValue = {macAddress};

        Cursor cursor = sqLiteDatabase.query(
                TagTable.TABLE,
                null,
                columnTitle,
                columnValue,
                null,
                null,
                null
        );
        Tag tag = new Tag();

        if (cursor.moveToNext()) {
            Date date = new Date(cursor.getLong(cursor.getColumnIndex(TagTable.COL_LAST_SEEN_TIME)));
            tag.setId(cursor.getInt(cursor.getColumnIndex(TagTable.COL_ID)));
            tag.setAlarm(cursor.getInt(cursor.getColumnIndex(TagTable.COL_ALARM)));
            tag.setName(cursor.getString(cursor.getColumnIndex(TagTable.COL_NAME)));
            tag.setLastSeenTime(date);
            tag.setMacAddress(cursor.getString(cursor.getColumnIndex(TagTable.COL_MAC_ADDRESS)));
        }
        return tag;
    }
    public Location getLocationById(int lastSeenLocationId) {
        sqLiteDatabase = this.getReadableDatabase();
        String columnTitle = LocationTable.COL_ID + " = ?";
        String[] columnValue = {Integer.toString(lastSeenLocationId)};

        Cursor cursor = sqLiteDatabase.query(
                LocationTable.TABLE,
                null,
                columnTitle,
                columnValue,
                null,
                null,
                null
        );

        Location location = new Location();

        if (cursor.moveToNext()) {
            location.setId(cursor.getInt(cursor.getColumnIndex(LocationTable.COL_ID)));
            location.setName(cursor.getString(cursor.getColumnIndex(LocationTable.COL_MESSAGE)));
            location.setLatitude(cursor.getColumnName(cursor.getColumnIndex(LocationTable.COL_LATITUDE)));
            location.setLongtitude(cursor.getColumnName(cursor.getColumnIndex(LocationTable.COL_LONGITUDE)));
        }

        return location;
    }

    public ArrayList<Tag> fetchTagData() {
        final Cursor data = tagFeedList();

        ArrayList<Tag> tags = new ArrayList<>();
        data.moveToFirst();
        while (data.moveToNext()) {
            tags.add(
                    new Tag() {{
                        Date date = new Date(data.getLong(data.getColumnIndex(TagTable.COL_LAST_SEEN_TIME)));

                        setId(data.getInt(data.getColumnIndex(TagTable.COL_ID)));
                        setName(data.getString(data.getColumnIndex(TagTable.COL_NAME)));
                        setMacAddress(data.getString(data.getColumnIndex(TagTable.COL_MAC_ADDRESS)));
                        setLastSeenLocationId(Integer.parseInt(data.getString(data.getColumnIndex(TagTable.COL_LAST_SEEN_LOCATION_ID))));
                        setLastSeenTime(date);
                        setAlarm(Integer.parseInt(data.getString(data.getColumnIndex(TagTable.COL_ALARM))));
                    }}
            );
        }

        return tags;
    }

    public ArrayList<Activity> fetchActivityData() {
        final Cursor data = activityFeedList();

        ArrayList<Activity> activities = new ArrayList<>();
        data.moveToFirst();
        while (data.moveToNext()) {
            if (data.getString(data.getColumnIndex(ActivityTable.COL_HAS_READ)).equals("false")) ;
            {
                activities.add(
                        new Activity() {{
                            Date date = new Date(data.getLong(data.getColumnIndex(ActivityTable.COL_NOTIFIED_ON)));

                            setId(data.getInt(data.getColumnIndex(ActivityTable.COL_ID)));
                            setMessage(data.getString(data.getColumnIndex(ActivityTable.COL_MESSAGE)));
                            setNotifiedOn(date);
                        }}
                );
            }
        }
        return activities;
    }
}
