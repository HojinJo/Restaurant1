package com.example.hojinjo.restaurant1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MDBHelper extends SQLiteOpenHelper {
    final static String TAG="SQLiteDBTest";

    public MDBHelper(Context context) {
        super(context, MContract.DB_NAME, null, MContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(MContract.Restaurant.CREATE_TABLE);       //데이터베이스가 없을때
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(MContract.Restaurant.DELETE_TABLE);
        onCreate(db);
    }

    public long insertUserByMethod(String name, String price, String menu) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MContract.Restaurant.KEY_NAME, name);
        values.put(MContract.Restaurant.KEY_PRICE, price);
        values.put(MContract.Restaurant.KEY_MENU,menu);

        return db.insert(MContract.Restaurant.TABLE_NAME,null,values);
    }
    public Cursor getAllUsersByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(MContract.Restaurant.TABLE_NAME,null,null,null,null,null,null);
    }
}
