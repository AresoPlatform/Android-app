package com.xw.aschwitkey.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "asch_city.db";

    public static final String TABLE_NAME = "T_ASCHCITY";

    public static final int DB_VERSION = 1;

    public static final String ACCOUNT = "account";
    public static final String ADDRESS = "address";
    public static final String SECRET = "secret";
    public static final String STATE = "state";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " +
                TABLE_NAME +
                "(_id integer primary key autoincrement, " +
                ACCOUNT + " varchar ," +
                ADDRESS + " varchar ," +
                SECRET + " varchar ," +
                STATE + " varchar " +
                ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
