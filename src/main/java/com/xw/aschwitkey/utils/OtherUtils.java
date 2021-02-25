package com.xw.aschwitkey.utils;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.LocaleList;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtherUtils {

    public static boolean checkzhongx(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }


    public static boolean checkEnglishandxian(String eng) {
        String regex = "[a-zA-Z-]+";
        return eng.matches(regex);
    }


    public static boolean checkEnglish(String eng) {
        String regex = "[a-zA-Z]+";
        return eng.matches(regex);
    }

    public static boolean checkEnglishornum(String eng) {
        String regex = "[a-zA-Z0-9]+";
        return eng.matches(regex);
    }

    public static boolean checkEmaile(String emaile) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(RULE_EMAIL);
        Matcher m = p.matcher(emaile);
        return m.matches();
    }


    public static boolean isNumeric(String str) {
        String zengze = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        return !str.matches(zengze);
    }

    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static void SHOWView(View view) {
        TranslateAnimation showAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        showAnim.setDuration(500);
        view.startAnimation(showAnim);
        view.setVisibility(View.VISIBLE);
    }

    public static void HIDDView(View view) {
        TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        hideAnim.setDuration(500);
        view.startAnimation(hideAnim);
        view.setVisibility(View.GONE);
    }

    public static boolean isOddNumber(int num) {
        return (num & 1) != 0;
    }


    public static String PhoneM(String mb) {
        if (mb.isEmpty()) {
            return "";
        } else {
            String mobile = mb.substring(0, 3) + "****" + mb.substring(7, 11);
            return mobile;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    public static boolean isCAMERA(Context constent) {
        PackageManager pm = constent.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CAMERA", constent.getPackageName()));
        if (permission) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean isPhone(String phone) {
        String regex = "^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[1|8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }

    public static String toLowerCase(String string) {
        return string.toLowerCase();
    }
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static long DateToMil(String date) {
        long ts = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = simpleDateFormat.parse(date);
            ts = date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ts;
    }

    public static String timedate(long time,String pattern) {
        if (time == 0) {
            return "";
        }
        String result;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        result = simpleDateFormat.format(new Date(time));
        return result;
    }


    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static long dateToStamp(String s) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }

    public static void config(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public String getInsideString(String str, String strStart, String strEnd) {
        if (str.indexOf(strStart) < 0) {
            return "";
        }
        if (str.indexOf(strEnd) < 0) {
            return "";
        }
        return str.substring(str.indexOf(strStart) + strStart.length(), str.indexOf(strEnd));
    }


    public static boolean isStrContains(String maxstr, String minstr) {
        boolean status = maxstr.contains(minstr);
        if (status) {
            return true;
        } else {
            return false;
        }
    }

    public static long getLocalVersion(Context ctx) {
        long localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.getLongVersionCode();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void getSystemLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        if (!locale.getLanguage().equals("en")) {
            if (!locale.getLanguage().equals("zh")) {
                Locale myLocale = new Locale("en");
                locale = myLocale;
            }
        }
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, dm);
    }

    public static void switchLanguage(Context context, int type) {
        Locale myLocale;
        switch (type) {
            case 0:
                myLocale = new Locale("en");
                break;
            case 1:
            default:
                myLocale = new Locale("zh");
                break;
        }
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        config.locale = myLocale;
        res.updateConfiguration(config, dm);
    }

    private static String getSimOperator(Context c) {
        try {
            TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSimOperator();
        } catch (Exception e) {
            return null;
        }
    }


    private static boolean isOperatorEmpty(String operator) {
        if (operator == null) {
            return true;
        }


        if (operator.equals("") || operator.toLowerCase(Locale.ENGLISH).contains("null")) {
            return true;
        }


        return false;
    }


    public static boolean isChinaSimCard(Context c) {
        String mcc = getSimOperator(c);
        if (isOperatorEmpty(mcc)) {
            return true;
        } else {
            return mcc.startsWith("460");
        }
    }

    public static boolean isChinaLatLng(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.get(0).getCountryName().equals("中国") || addresses.get(0).getCountryName().equals("China")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static String getCountryCity(Context context, double lat, double lng, int type) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        if (!locale.getLanguage().equals("en")) {
            if (!locale.getLanguage().equals("zh")) {
                Locale myLocale = new Locale("en");
                locale = myLocale;
            }
        }
        Geocoder geocoder = new Geocoder(context, locale);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (type == 0) {
                return addresses.get(0).getCountryName();
            } else {
                return addresses.get(0).getLocality();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(52);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static boolean isPuBlick(String str){
        if (str.trim().isEmpty()){
            return false;
        }

        String a=str.trim().replace(" ","&");
        int count=0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i)=='&'){
                count++;
            }
        }
        if (count==11){
            Pattern p = Pattern.compile(".*\\d+.*");
            Matcher m = p.matcher(str);
            if (!m.matches()) {
                return true;
            }
        }
        return false;
    }

    public static boolean getPastDate(String data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        Date today = calendar.getTime();
        String result = format.format(today);
        try {
            data = data.replace("T", " ");
            Date dt = format.parse(data);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(dt);
            calendar1.add(Calendar.HOUR, 8);
            Date date1 = calendar1.getTime();
            Log.e(null, result);
            return compareDate(today, date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean compareDate(Date date1, Date date2) {
        if (date1.before(date2)) {
            System.out.println(date1 + "在" + date2 + "前面");
            return true;
        } else if (date1.after(date2)) {
            System.out.println(date1 + "在" + date2 + "后面");
            return false;
        } else {
            System.out.println("是同一天的同一时间");
            return false;
        }
    }

    public static boolean checkAppInstalled(Context context,String pkgName) {
        if (pkgName== null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
