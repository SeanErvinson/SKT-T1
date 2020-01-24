package com.sktt1.butters.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.sktt1.butters.data.database.tables.ActivityTable;
import com.sktt1.butters.data.database.tables.LocationTable;
import com.sktt1.butters.data.database.tables.TagTable;

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
    public long activityCreateNotification(DatabaseHelper databaseHelper, String message, String notified_on) {

        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues activityValues = new ContentValues();
        activityValues.put(ActivityTable.COL_MESSAGE, message);
        activityValues.put(ActivityTable.COL_NOTIFIED_ON, notified_on);
        activityValues.put(ActivityTable.COL_HAS_READ, "false");

        return sqLiteDatabase.insert("activities", null, activityValues);
    }

    public void activityUpdateNotification(DatabaseHelper databaseHelper, long id) {

        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues activityValues = new ContentValues();
        activityValues.put(ActivityTable.COL_HAS_READ, "true");

        sqLiteDatabase.update(ActivityTable.TABLE, activityValues, "id = ?", new String[] {Long.toString(id)});
    }

    // Tag table interactions


    // Location table interactions

    public long locationCreateRow(DatabaseHelper databaseHelper, long id, String name, String longitude, String latitude) {

        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues locationValues = new ContentValues();
        locationValues.put(LocationTable.COL_MESSAGE, name);
        locationValues.put(LocationTable.COL_LONGITUDE, longitude);
        locationValues.put(LocationTable.COL_LATITUDE, latitude);

        return sqLiteDatabase.insert(LocationTable.TABLE, null, locationValues);
    }
}
