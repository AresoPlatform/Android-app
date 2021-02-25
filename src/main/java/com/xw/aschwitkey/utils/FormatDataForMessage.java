package com.xw.aschwitkey.utils;

import android.content.Context;
import android.util.Log;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDataForMessage {

    public static String getTimeRange(Context context, String mTime) {
        long curDate = System.currentTimeMillis() + Content.timePoor;
        long ts;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy");

            Date dt = sdf.parse(mTime);
            ts = dt.getTime();

            long between = (curDate - ts) / 1000;
            int elapsedTime = (int) (between);
            if (elapsedTime / 3600 <= 24 && sdf3.format(dt).equals(sdf3.format(curDate))) {
                return sdf1.format(dt);
            }
            if (elapsedTime / 3600 <= 24 * 30 * 12 && sdf4.format(dt).equals(sdf4.format(curDate))) {
                return sdf2.format(dt);
            }
            if (elapsedTime / 3600 > 24 * 30 * 12 || !sdf4.format(dt).equals(sdf4.format(curDate))) {
                return sdf3.format(dt);
            }
            return "";
        } catch (ParseException e) {
            return context.getString(R.string.just);
        }
    }

}
