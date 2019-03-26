package com.itcollege.radio2019;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RadioDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "RADIOS.DB";
    public static final int DB_VERSION = 1;

    public static final String RADIO_TABLE_NAME = "RADIOS";

    public static final String RADIO_ID = "_id";
    public static final String RADIO_NAME = "radioName";
    public static final String RADIO_STREAMNAME = "radioStreamName";
    public static final String RADIO_JSONNAME = "jsonName";

    public static final String RADIO_CREATE_TABLE_SQL = "create table " + RADIO_TABLE_NAME +
            "(" +
            RADIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + RADIO_NAME + " TEXT NOT NULL, " +
            RADIO_STREAMNAME + " TEXT NOT NULL, " +
            RADIO_JSONNAME + " TEXT NOT NULL)";

    public RadioDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RADIO_CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  RADIO_TABLE_NAME);
        onCreate(db);
    }
}
