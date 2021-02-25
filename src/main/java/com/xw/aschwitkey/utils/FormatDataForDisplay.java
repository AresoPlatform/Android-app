package com.xw.aschwitkey.utils;

import android.content.Context;
import android.util.Log;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatDataForDisplay {

    public static String getTimeRange(Context context, String mTime) {
        long curDate = System.currentTimeMillis() + Content.timePoor;
        long ts;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

            Date dt = sdf.parse(mTime);
            ts = dt.getTime();

            long between = (curDate - ts) / 1000;
            int elapsedTime = (int) (between);
            if (elapsedTime / 3600 <= 24 && sdf2.format(dt).equals(sdf2.format(curDate))) {
                return sdf1.format(dt);
            }
            if (elapsedTime / (3600 * 24) < 2) {
                return "昨天 " + sdf1.format(dt);
            }
            if (elapsedTime / (3600 * 24) > 0 && elapsedTime / (3600 * 24) < 3) {
                return "前天 " + sdf1.format(dt);
            }
            if (elapsedTime / (3600 * 24) > 2) {
                return sdf2.format(dt);
            }
            return "";
        } catch (ParseException e) {
            return context.getString(R.string.just);
        }
    }

}
