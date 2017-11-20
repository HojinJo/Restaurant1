package com.example.hojinjo.restaurant1;


import android.provider.BaseColumns;

public final class RContract {
    public static final String DB_NAME="restaurant.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private RContract() {}

    /* Inner class that defines the table contents */
    public static class Restaurant implements BaseColumns {
        public static final String TABLE_NAME="Restaurants";
        public static final String KEY_RESTAURANTIMG = "Restaurantimg";
        public static final String KEY_NAME = "Name";
        public static final String KEY_ADDRESS = "address";
        public static final String KEY_PHONE = "Phone";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP + KEY_RESTAURANTIMG + COMMA_SEP +
                KEY_NAME + TEXT_TYPE + COMMA_SEP + KEY_ADDRESS + TEXT_TYPE + COMMA_SEP +
                KEY_PHONE + TEXT_TYPE +  " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
