package com.itcollege.radio2019;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SongDatabaseHelper extends SQLiteOpenHelper{

        public static final String DB_NAME = "SONGS.DB";
        public static final int DB_VERSION = 1;

        public static final String SONG_TABLE_NAME = "SONGS";

        public static final String SONG_ID = "_id";
        public static final String SONG_ARTIST = "artist";
        public static final String SONG_TITLE = "title";
        public static final String SONG_RADIO = "radio";




        public static final String SONG_CREATE_TABLE_SQL = "create table " + SONG_TABLE_NAME +
                        "(" +
                        SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + SONG_ARTIST + " TEXT NOT NULL, " + SONG_TITLE + " TEXT NOT NULL, " +
                       SONG_RADIO + " TEXT NOT NULL);";

        public SongDatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SONG_CREATE_TABLE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  SONG_TABLE_NAME);
            onCreate(db);
        }
}

