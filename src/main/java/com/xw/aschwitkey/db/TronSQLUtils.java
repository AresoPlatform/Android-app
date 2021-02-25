package com.xw.aschwitkey.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xw.aschwitkey.entity.DBHelperBean;
import com.xw.aschwitkey.entity.TronDBHelperBean;

import java.util.ArrayList;
import java.util.List;

public class TronSQLUtils {

    private static SQLiteDatabase mDatabase;
    private static TronDBHelper mDBHelper;

    public static List<TronDBHelperBean> QuerySQL(Context context, String addRess) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        List<TronDBHelperBean> list = new ArrayList<>();
        String querysql = "select *from T_ASCHCITY where address = '" + addRess + "'";
        TronDBHelperBean helperBean = null;
        Cursor cursor = mDatabase.rawQuery(querysql, null);
        while (cursor.moveToNext()) {
            helperBean = new TronDBHelperBean();
            helperBean.setAccount(cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNT)));
            helperBean.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.ADDRESS)));
            helperBean.setSecret(cursor.getString(cursor.getColumnIndex(DBHelper.SECRET)));
            helperBean.setState(cursor.getString(cursor.getColumnIndex(DBHelper.STATE)));
            list.add(helperBean);
        }
        return list;
    }

    public static List<TronDBHelperBean> QuerySQL(Context context, String addRess,String phone) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        List<TronDBHelperBean> list = new ArrayList<>();
        String querysql = "select *from T_ASCHCITY where address = '" + addRess + "' and state='"+phone+"@'";
        TronDBHelperBean helperBean = null;
        Cursor cursor = mDatabase.rawQuery(querysql, null);
        while (cursor.moveToNext()) {
            helperBean = new TronDBHelperBean();
            helperBean.setAccount(cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNT)));
            helperBean.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.ADDRESS)));
            helperBean.setSecret(cursor.getString(cursor.getColumnIndex(DBHelper.SECRET)));
            helperBean.setState(cursor.getString(cursor.getColumnIndex(DBHelper.STATE)));
            list.add(helperBean);
        }
        return list;
    }

    public static List<TronDBHelperBean> QuerySQLAll(Context context, String where) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        List<TronDBHelperBean> list = new ArrayList<>();
        String querysql = "select *from T_ASCHCITY " + where;
        TronDBHelperBean helperBean = null;
        Cursor cursor = mDatabase.rawQuery(querysql, null);
        while (cursor.moveToNext()) {
            helperBean = new TronDBHelperBean();
            helperBean.setAccount(cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNT)));
            helperBean.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.ADDRESS)));
            helperBean.setSecret(cursor.getString(cursor.getColumnIndex(DBHelper.SECRET)));
            helperBean.setState(cursor.getString(cursor.getColumnIndex(DBHelper.STATE)));

            list.add(helperBean);
        }
        return list;
    }


    public static String QueryPublick(Context context, String address) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        List<TronDBHelperBean> list = new ArrayList<>();
        String querysql = "select *from T_ASCHCITY where address='" + address + "'";
        TronDBHelperBean helperBean = null;
        Cursor cursor = mDatabase.rawQuery(querysql, null);
        while (cursor.moveToNext()) {
            helperBean = new TronDBHelperBean();
            helperBean.setAccount(cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNT)));
            helperBean.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.ADDRESS)));
            helperBean.setSecret(cursor.getString(cursor.getColumnIndex(DBHelper.SECRET)));
            helperBean.setState(cursor.getString(cursor.getColumnIndex(DBHelper.STATE)));

            list.add(helperBean);
        }

        String publick = "a";
        if (!list.isEmpty()) {
            publick = list.get(0).getState();
        }
        return publick;
    }

    public static void AddSql(Context context, String Account, String Address, String Secret, String state) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        String addsql = "insert into T_ASCHCITY(account,address,secret,state) values('" +
                Account + "','" + Address + "','" + Secret + "','" + state + "')";
        mDatabase.execSQL(addsql);
    }

    public static void DaleteSql(Context context, String AddRess) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        String dalsql = "delete from T_ASCHCITY where address='" + AddRess + "'";
        mDatabase.execSQL(dalsql);
    }

    public static void DaleteSql(Context context, String AddRess, String phone) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        String dalsql = "delete from T_ASCHCITY where address='" + AddRess + "' and state='" + phone + "@'";
        mDatabase.execSQL(dalsql);
    }

    public static void DaleteSqlPhone(Context context, String phone) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        String dalsql = "delete from T_ASCHCITY where state='" + phone + "@'";
        mDatabase.execSQL(dalsql);
    }

    public static void updateSql(Context context, String phone) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        String updateSql = "update T_ASCHCITY set state = '" + phone + "'";
        mDatabase.execSQL(updateSql);
    }

    public static void updatePhoneSql(Context context, String phone,String address) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        String updateSql = "update T_ASCHCITY set state = '" + phone + "' where address='"+address+"'";
        mDatabase.execSQL(updateSql);
    }

    public static void updatePasswordSql(Context context, String address, String secret, String phone) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        String updateSql = "update T_ASCHCITY set secret = '" + secret + "' where address='" + address + "' and state='" + phone + "@'";
        mDatabase.execSQL(updateSql);
    }

    public static void DeleteSql(Context context) {
        mDBHelper = new TronDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
        String dalsql = "delete from T_ASCHCITY ";
        mDatabase.execSQL(dalsql);
    }

}
