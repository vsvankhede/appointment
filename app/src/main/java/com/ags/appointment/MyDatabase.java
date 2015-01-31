package com.ags.appointment;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "apnt_db.sqlite";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Select data from apnt_db.sqlite
    public Cursor getAppointment() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"rowid AS _id","title", "desc", "image", "time", "date"};
        String sqlTables = "tbl_main";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;
    }

    // Insert data in apnt_db.sqlite
    public void setAppointment(String dt_title, String dt_desc, String dt_date, String dt_time, String dt_img){

        //removed this.getWritableDatabase();
        SQLiteDatabase db = getWritableDatabase();
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
            db.insert(sqlTables,null,values);

            System.out.println("Data is stored...");
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
