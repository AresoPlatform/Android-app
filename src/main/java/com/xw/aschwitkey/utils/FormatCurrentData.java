package com.xw.aschwitkey.utils;

import android.content.Context;
import android.util.Log;


import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatCurrentData {


    private static final int seconds_of_1minute = 60;

    private static final int seconds_of_30minutes = 30 * 60;

    private static final int seconds_of_1hour = 60 * 60;

    private static final int seconds_of_1day = 24 * 60 * 60;

    private static final int seconds_of_15days = seconds_of_1day * 15;

    private static final int seconds_of_30days = seconds_of_1day * 30;

    private static final int seconds_of_6months = seconds_of_30days * 6;

    private static final int seconds_of_1year = seconds_of_30days * 12;

    public static String getTimeRange(Context context, String mTime) {
        mTime = mTime.replace("T", " ");
        long curDate = System.currentTimeMillis() + Content.timePoor;
        long ts;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");

            Date dt = sdf.parse(mTime);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.HOUR, 8);
            Date date = rightNow.getTime();


            ts = date.getTime();

            long between = (curDate - ts) / 1000;
            int elapsedTime = (int) (between);
            if (elapsedTime < seconds_of_1minute) {

                return context.getString(R.string.just);
            }
            if (elapsedTime < seconds_of_30minutes) {

                return elapsedTime / seconds_of_1minute + context.getString(R.string.minutes_ago);
            }
            if (elapsedTime < seconds_of_1hour) {

                return context.getString(R.string.half_an_hour_ago);
            }
            if (elapsedTime < seconds_of_1day) {

                return elapsedTime / seconds_of_1hour + context.getString(R.string.hour_ago);
            }
            if (elapsedTime < seconds_of_15days) {

                return sdf1.format(date);
            }
            if (elapsedTime < seconds_of_30days) {

                return sdf1.format(date);
            }
            if (elapsedTime < seconds_of_6months && sdf3.format(date).equals(sdf3.format(curDate))) {

                return sdf1.format(date);
            }
            if (elapsedTime < seconds_of_1year && sdf3.format(date).equals(sdf3.format(curDate))) {

                return sdf1.format(date);
            }
            if (elapsedTime >= seconds_of_1year || !sdf3.format(date).equals(sdf3.format(curDate))) {

                return sdf2.format(date);
            }
            return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return context.getString(R.string.just);
        }
    }

    public static String getTimeRange1(Context context, String mTime) {
        long curDate = System.currentTimeMillis() + Content.timePoor;
        long ts;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");

            Date date = sdf.parse(mTime);
            ts = date.getTime();

            long between = (curDate - ts) / 1000;
            int elapsedTime = (int) (between);
            if (elapsedTime < seconds_of_1minute) {

                return context.getString(R.string.just);
            }
            if (elapsedTime < seconds_of_30minutes) {

                return elapsedTime / seconds_of_1minute + context.getString(R.string.minutes_ago);
            }
            if (elapsedTime < seconds_of_1hour) {

                return context.getString(R.string.half_an_hour_ago);
            }
            if (elapsedTime < seconds_of_1day) {

                return elapsedTime / seconds_of_1hour + context.getString(R.string.hour_ago);
            }
            if (elapsedTime < seconds_of_15days) {

                return sdf1.format(date);
            }
            if (elapsedTime < seconds_of_30days) {

                return sdf1.format(date);
            }
            if (elapsedTime < seconds_of_6months && sdf3.format(date).equals(sdf3.format(curDate))) {

                return sdf1.format(date);
            }
            if (elapsedTime < seconds_of_1year && sdf3.format(date).equals(sdf3.format(curDate))) {

                return sdf1.format(date);
            }
            if (elapsedTime >= seconds_of_1year || !sdf3.format(date).equals(sdf3.format(curDate))) {

                return sdf2.format(date);
            }
            return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return context.getString(R.string.just);
        }
    }

}
