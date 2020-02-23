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

import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "skt.db";
    private static final int DB_VERSION = 1;

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

    public int activityCreateNotification(String message) {
        try (SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            ContentValues activityValues = new ContentValues();
            activityValues.put(ActivityTable.COL_MESSAGE, message);
            activityValues.put(ActivityTable.COL_NOTIFIED_ON, new Date().getTime());
            activityValues.put(ActivityTable.COL_HAS_READ, false);
            return (int) sqLiteDatabase.insert(ActivityTable.TABLE, null, activityValues);
        }
    }

    public void activityUpdateNotification(int id) {
        try (SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            ContentValues activityValues = new ContentValues();
            activityValues.put(ActivityTable.COL_HAS_READ, true);
            sqLiteDatabase.update(ActivityTable.TABLE, activityValues, "id = ?", new String[]{Long.toString(id)});
        }
    }

    public int tagCreateDevice(String name, String macAddress) {

        int lastSeenLocationId = locationCreateRow("", 0, 0);

        try (SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            ContentValues tagValues = new ContentValues();

            Date datetime = new Date();

            tagValues.put(TagTable.COL_NAME, name);
            tagValues.put(TagTable.COL_MAC_ADDRESS, macAddress);
            tagValues.put(TagTable.COL_LAST_SEEN_LOCATION_ID, lastSeenLocationId);
            tagValues.put(TagTable.COL_LAST_SEEN_TIME, datetime.getTime());
            tagValues.put(TagTable.COL_ALARM, 1);

            return (int) sqLiteDatabase.insert(TagTable.TABLE, null, tagValues);
        }
    }

    public void tagUpdateName(String name, int id) {
        try (SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            ContentValues tagValues = new ContentValues();
            tagValues.put(TagTable.COL_NAME, name);
            sqLiteDatabase.update(TagTable.TABLE, tagValues, "id = ?", new String[]{Integer.toString(id)});
        }
    }

    public void tagUpdateLocation(int tagId, String locationName, double longitude, double latitude) {
        try (SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            int locationId = tagId;
            Date lastSeenTime = new Date();
            
            ContentValues tagValues = new ContentValues();
            tagValues.put(TagTable.COL_LAST_SEEN_LOCATION_ID, locationId);
            tagValues.put(TagTable.COL_LAST_SEEN_TIME, lastSeenTime.getTime());
            sqLiteDatabase.update(TagTable.TABLE, tagValues, "id = ?", new String[]{Integer.toString(tagId)});

            ContentValues locationValues = new ContentValues();
            locationValues.put(LocationTable.COL_MESSAGE, locationName);
            locationValues.put(LocationTable.COL_LATITUDE, latitude);
            locationValues.put(LocationTable.COL_LONGITUDE, longitude);
            sqLiteDatabase.update(LocationTable.TABLE, locationValues, "id = ?", new String[]{Integer.toString(locationId)});
        }
    }

    public void tagUpdateSoundAlarm(int id, int alarm) {
        try (SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            ContentValues tagValues = new ContentValues();
            tagValues.put(TagTable.COL_ALARM, alarm);
            sqLiteDatabase.update(TagTable.TABLE, tagValues, "id = ?", new String[]{Integer.toString(id)});
        }
    }

    public int locationCreateRow(String name, double longitude, double latitude) {
        try (SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            ContentValues locationValues = new ContentValues();
            locationValues.put(LocationTable.COL_MESSAGE, name);
            locationValues.put(LocationTable.COL_LONGITUDE, longitude);
            locationValues.put(LocationTable.COL_LATITUDE, latitude);

            return (int) sqLiteDatabase.insert(LocationTable.TABLE, null, locationValues);
        }
    }

    public Tag getTagByMacAddress(String macAddress) {
        String columnTitle = TagTable.COL_MAC_ADDRESS + " = ?";
        String[] columnValue = {macAddress};
        try (SQLiteDatabase sqLiteDatabase = this.getReadableDatabase()) {
            try (Cursor cursor = sqLiteDatabase.query(
                    TagTable.TABLE,
                    null,
                    columnTitle,
                    columnValue,
                    null,
                    null,
                    null
            )) {
                if (cursor.moveToNext()) {
                    Tag tag = new Tag();
                    Date date = new Date(cursor.getLong(cursor.getColumnIndex(TagTable.COL_LAST_SEEN_TIME)));
                    tag.setId(cursor.getInt(cursor.getColumnIndex(TagTable.COL_ID)));
                    tag.setAlarm(cursor.getInt(cursor.getColumnIndex(TagTable.COL_ALARM)));
                    tag.setName(cursor.getString(cursor.getColumnIndex(TagTable.COL_NAME)));
                    tag.setLastSeenTime(date);
                    tag.setMacAddress(cursor.getString(cursor.getColumnIndex(TagTable.COL_MAC_ADDRESS)));
                    return tag;
                }
                return null;
            }
        }
    }

    public Location getLocationById(int lastSeenLocationId) {
        String columnTitle = LocationTable.COL_ID + " = ?";
        String[] columnValue = {Integer.toString(lastSeenLocationId)};
        try (SQLiteDatabase sqLiteDatabase = this.getReadableDatabase()) {
            try (Cursor cursor = sqLiteDatabase.query(
                    LocationTable.TABLE,
                    null,
                    columnTitle,
                    columnValue,
                    null,
                    null,
                    null
            )) {
                if (cursor.moveToNext()) {
                    Location location = new Location();
                    location.setId(cursor.getInt(cursor.getColumnIndex(LocationTable.COL_ID)));
                    location.setName(cursor.getString(cursor.getColumnIndex(LocationTable.COL_MESSAGE)));
                    location.setLatitude(cursor.getDouble(cursor.getColumnIndex(LocationTable.COL_LATITUDE)));
                    location.setLongitude(cursor.getDouble(cursor.getColumnIndex(LocationTable.COL_LONGITUDE)));
                    return location;
                }
                return null;
            }
        }
    }

    public ArrayList<Tag> fetchTagData() {
        ArrayList<Tag> tags = new ArrayList<>();
        try (SQLiteDatabase sqLiteDatabase = this.getReadableDatabase()) {
            String query = String.format("SELECT * FROM %s ", TagTable.TABLE);
            try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Tag newTag = new Tag();
                    Date date = new Date(cursor.getLong(cursor.getColumnIndex(TagTable.COL_LAST_SEEN_TIME)));

                    newTag.setId(cursor.getInt(cursor.getColumnIndex(TagTable.COL_ID)));
                    newTag.setName(cursor.getString(cursor.getColumnIndex(TagTable.COL_NAME)));
                    newTag.setMacAddress(cursor.getString(cursor.getColumnIndex(TagTable.COL_MAC_ADDRESS)));
                    newTag.setLastSeenLocationId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TagTable.COL_LAST_SEEN_LOCATION_ID))));
                    newTag.setLastSeenTime(date);
                    newTag.setAlarm(cursor.getInt(cursor.getColumnIndex(TagTable.COL_ALARM)));
                    tags.add(newTag);
                    cursor.moveToNext();
                }
            }
        }
        return tags;
    }

    public ArrayList<Activity> fetchActivityData() {
        ArrayList<Activity> activities = new ArrayList<>();
        try (SQLiteDatabase sqLiteDatabase = this.getReadableDatabase()) {
            String query = String.format("SELECT * FROM %s ", ActivityTable.TABLE);
            try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Activity newActivity = new Activity();
                    Date date = new Date(cursor.getLong(cursor.getColumnIndex(ActivityTable.COL_NOTIFIED_ON)));

                    newActivity.setId(cursor.getInt(cursor.getColumnIndex(ActivityTable.COL_ID)));
                    newActivity.setMessage(cursor.getString(cursor.getColumnIndex(ActivityTable.COL_MESSAGE)));
                    newActivity.setNotifiedOn(date);
                    activities.add(newActivity);
                    cursor.moveToNext();
                }
            }
        }
        return activities;
    }
}
