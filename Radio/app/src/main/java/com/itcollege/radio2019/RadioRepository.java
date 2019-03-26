package com.itcollege.radio2019;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RadioRepository {
    private RadioDatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public RadioRepository(Context context) {
        this.context = context;
    }
    public RadioRepository open() {
        dbHelper = new RadioDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        dbHelper.close();
    }
    public void add(Radio radio) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RadioDatabaseHelper.RADIO_NAME, radio.name);
        contentValues.put(RadioDatabaseHelper.RADIO_STREAMNAME, radio.streamName);
        contentValues.put(RadioDatabaseHelper.RADIO_JSONNAME, radio.jsonName);
        db.insert(RadioDatabaseHelper.RADIO_TABLE_NAME, null, contentValues);
    }
    public Cursor fetch() {
        String[] columns = new String[] {
                RadioDatabaseHelper.RADIO_ID,
                RadioDatabaseHelper.RADIO_NAME,
                RadioDatabaseHelper.RADIO_STREAMNAME,
                RadioDatabaseHelper.RADIO_JSONNAME

        };
        Cursor cursor = db.query(RadioDatabaseHelper.RADIO_TABLE_NAME, columns,
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public List<Radio> getAll() {
        ArrayList<Radio> radios = new ArrayList<>();
        Cursor cursor = fetch();
        if (cursor.moveToFirst()) {
            do{
                radios.add(new Radio(
                        cursor.getInt(cursor.getColumnIndex(RadioDatabaseHelper.RADIO_ID)),
                        cursor.getString(cursor.getColumnIndex(RadioDatabaseHelper.RADIO_NAME)),
                        cursor.getString(cursor.getColumnIndex(RadioDatabaseHelper.RADIO_STREAMNAME)),
                        cursor.getString(cursor.getColumnIndex(RadioDatabaseHelper.RADIO_JSONNAME))
                ));
            } while (cursor.moveToNext());
        }
        return radios;
    }

}
