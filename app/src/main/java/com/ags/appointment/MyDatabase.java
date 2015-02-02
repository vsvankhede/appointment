package com.ags.appointment;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.sql.SQLException;

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "apnt_db.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "MyDatabase";
    private static final String FTS_VIRTUAL_TABLE = "AppointmentInfo";
    private static final String DATABASE_COPY = "INSERT INTO AppointmentInfo (title, desc, image, time, date, id) SELECT title, desc, image, time, date, id FROM tbl_main;";
    //Create a FTS3 Virtual Table for fast searches
    private static final String DATABASE_CREATE = "CREATE VIRTUAL TABLE IF NOT EXISTS "+FTS_VIRTUAL_TABLE+" USING " +
            "fts3(title,desc,image,time,date, id) ;";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //createFTS();
        //copyData();
    }
    // Create virtual db
    public void createFTS(){
        Log.w(TAG, DATABASE_CREATE);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DATABASE_CREATE);
    }
    // Copy data from tbl_main to virtual db
    public void copyData(){
        Log.w(TAG, DATABASE_COPY);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DATABASE_COPY);
    }
    // Delete data
    public boolean deleteAllAppointment() {

        int doneDelete = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        doneDelete = db.delete(FTS_VIRTUAL_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }
    // Select data from apnt_db.sqlite
    public Cursor getAppointment() {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"docid AS _id","title", "desc", "image", "time", "date","docid"};
        String sqlTables = "tbl_main";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        c.moveToFirst();

        return c;
    }

    // Insert data in apnt_db.sqlite
    public void setAppointment(String dt_title, String dt_desc, String dt_date, String dt_time, String dt_img){

        SQLiteDatabase db = this.getWritableDatabase();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String sqlTables = "tbl_main";

        try {
            ContentValues values = new ContentValues();

            values.put("title", dt_title);
            values.put("desc", dt_desc);
            values.put("image", dt_img);
            values.put("time", dt_time);
            values.put("date", dt_date);

            qb.setTables(sqlTables);
            db.insert(sqlTables, null, values);

            System.out.println("Data is stored...");
        }catch(Exception e){
            System.out.println(e);
        }

    }
    // Delete data
    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlTables = "tbl_main";
        try {
            db.execSQL("delete from " + sqlTables + " where docid='" + id + "'");
            System.out.println("Deleted......................................'" + id + "'");
        }catch (Exception e){}finally {
            try {
                db.close();
            }catch (Exception e){}
        }
    }
    // Search data
    public Cursor searchAppointment(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        String query = "SELECT docid as _id, title, desc, image,time,date, docid FROM tbl_main WHERE title MATCH '"+inputText+"';";
        Log.w(TAG, query);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}