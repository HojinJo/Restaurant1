package com.example.hojinjo.restaurant1;

/**
 * Created by user on 2017-11-16.
 */

import android.provider.BaseColumns;

public final class MContract {
    public static final String DB_NAME="menu.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private MContract() {}

    /* Inner class that defines the table contents */
    public static class Menu implements BaseColumns {
        public static final String TABLE_NAME="Menu";
        public static final String KEY_NAME = "Name";
        public static final String KEY_PRICE = "Price";
        public static final String KEY_DESCRIPTION = "Description";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_NAME + TEXT_TYPE + COMMA_SEP + KEY_PRICE + TEXT_TYPE + COMMA_SEP +
                KEY_DESCRIPTION + TEXT_TYPE +  " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}