package com.example.hojinjo.restaurant1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final static String TAG="SQLiteDBTest";

    public DBHelper(Context context) {
        super(context, RContract.DB_NAME, null, RContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(RContract.Restaurant.CREATE_TABLE);       //데이터베이스가 없을때
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(RContract.Restaurant.DELETE_TABLE);
        onCreate(db);
    }

    public long insertUserByMethod(String restimg, String name, String address ,String phone , String lat ,String lon) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RContract.Restaurant.KEY_RESTAURANTIMG,restimg);
        values.put(RContract.Restaurant.KEY_NAME, name);
        values.put(RContract.Restaurant.KEY_ADDRESS, address);
        values.put(RContract.Restaurant.KEY_PHONE,phone);
        values.put(RContract.Restaurant.KEY_LATITUDE,lat);
        values.put(RContract.Restaurant.KEY_LONGTITUDE,lon);

        return db.insert(RContract.Restaurant.TABLE_NAME,null,values);
    }
    public Cursor getAllRestaurantsByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(RContract.Restaurant.TABLE_NAME,null,null,null,null,null,null);
    }

    public Cursor getAllRestaurants() {
        SQLiteDatabase db = getReadableDatabase();
        String [] projection = {
                RContract.Restaurant._ID,
                RContract.Restaurant.KEY_NAME,
                RContract.Restaurant.KEY_ADDRESS,
                RContract.Restaurant.KEY_PHONE,
                RContract.Restaurant.KEY_RESTAURANTIMG
        };
        return db.query( RContract.Restaurant.TABLE_NAME,  // 테이블이름
                projection,         // 프로젝션
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor getRestaurantIDByName(String name){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {RContract.Restaurant._ID};
        String selection= RContract.Restaurant.KEY_NAME + "=?";    //물어보기
        String[] selectionArgs ={name};
     return db.query(RContract.Restaurant.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
    }
}

