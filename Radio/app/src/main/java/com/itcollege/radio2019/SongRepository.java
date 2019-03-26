package com.itcollege.radio2019;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

public class SongRepository {

        private SongDatabaseHelper dbHelper;
        private Context context;
        private SQLiteDatabase db;

        public SongRepository(Context context) {
            this.context = context;
        }
        public SongRepository open() {
            dbHelper = new SongDatabaseHelper(context);
            db = dbHelper.getWritableDatabase();
            return this;
        }
        public void close() {
            dbHelper.close();
        }
        public void add(Song song) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SongDatabaseHelper.SONG_ARTIST, song.artist);
            contentValues.put(SongDatabaseHelper.SONG_TITLE, song.title);
            contentValues.put(SongDatabaseHelper.SONG_RADIO, song.radio);
            db.insert(SongDatabaseHelper.SONG_TABLE_NAME, null, contentValues);
        }
        public Cursor fetch() {
            String[] columns = new String[] {
                    SongDatabaseHelper.SONG_ID,
                    SongDatabaseHelper.SONG_ARTIST,
                    SongDatabaseHelper.SONG_TITLE,
                    SongDatabaseHelper.SONG_RADIO,
            };
            Cursor cursor = db.query(SongDatabaseHelper.SONG_TABLE_NAME, columns, null,
                    null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            return cursor;
        }

    public Cursor fetchUnique(String s, String selection, String column) {
        String[] columns = new String[] {
                SongDatabaseHelper.SONG_ID,
                SongDatabaseHelper.SONG_ARTIST,
                SongDatabaseHelper.SONG_TITLE,
                SongDatabaseHelper.SONG_RADIO,
        };
        Cursor cursor = db.query(true, SongDatabaseHelper.SONG_TABLE_NAME, columns, selection+"=?", new String[] {column}, s, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public int getSongCount(String[] columnss) {
        String[] columns = new String[] {
                SongDatabaseHelper.SONG_ID,
                SongDatabaseHelper.SONG_ARTIST,
                SongDatabaseHelper.SONG_TITLE,
                SongDatabaseHelper.SONG_RADIO,
        };
        Cursor cursor = db.query(SongDatabaseHelper.SONG_TABLE_NAME, columns, "artist=? and title=? and radio=?", columnss, null, null, null);
        return cursor.getCount();
    }

    public String[] getSongsWithCount (String radio) {
        ArrayList<String> songWithCount = new ArrayList<>();
        Cursor cursor = fetchUnique(SongDatabaseHelper.SONG_TITLE, SongDatabaseHelper.SONG_RADIO, radio);
        if (cursor.moveToFirst()) {
            do{
                String artist = cursor.getString(cursor.getColumnIndex(SongDatabaseHelper.SONG_ARTIST));
                String song = cursor.getString(cursor.getColumnIndex(SongDatabaseHelper.SONG_TITLE));
                int count = getSongCount(new String[] {artist, song, radio});
                songWithCount.add(
                        artist + " - " + song + "  - x" + String.valueOf(count)
                );
            } while (cursor.moveToNext());
        }
        String[] songString = songWithCount.toArray(new String[0]);
        return songString;
    }


        public List<Song> getAll() {
            ArrayList<Song> songs = new ArrayList<>();
            Cursor cursor = fetch();
            if (cursor.moveToFirst()) {
                do{
                    songs.add(new Song(
                            cursor.getInt(cursor.getColumnIndex(SongDatabaseHelper.SONG_ID)),
                            cursor.getString(cursor.getColumnIndex(SongDatabaseHelper.SONG_ARTIST)),
                            cursor.getString(cursor.getColumnIndex(SongDatabaseHelper.SONG_TITLE)),
                            cursor.getString(cursor.getColumnIndex(SongDatabaseHelper.SONG_RADIO))
                    ));
                } while (cursor.moveToNext());
            }
            return songs;
        }
    public int getUniqueCount(String s, String selection, String column) {
        ArrayList<Song> songs = new ArrayList<>();
        Cursor cursor = fetchUnique(s, selection, column);

        return cursor.getCount();
    }
}

