package com.xw.aschwitkey.utils;

import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtil {
    private static boolean isShow = true;
    private static Toast mToast = null;

    private ToastUtil() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    public static void controlShow(boolean isShowToast){
        isShow = isShowToast;
    }

    public void cancelToast() {
        if(isShow && mToast != null){
            mToast.cancel();
        }
    }

    public static void showShort(Context context, CharSequence message) {
        try {
            if (isShow) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(message);
                }
                mToast.show();
            }
        }catch (Exception e) {
            Looper.prepare();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    public static void showShort(Context context, int resId) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    public static void showLong(Context context, CharSequence message) {
        try {
            if (isShow) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                } else {
                    mToast.setText(message);
                }
                mToast.show();
            }
        }catch (Exception e) {
            Looper.prepare();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    public static void showLong(Context context, int resId) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    public static void show(Context context, CharSequence message, int duration) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    public static void show(Context context, int resId, int duration) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, duration);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    public static void customToastView(Context context, CharSequence message, int duration,View view) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            if(view != null){
                mToast.setView(view);
            }
            mToast.show();
        }
    }

    public static void customToastGravity(Context context, CharSequence message, int duration,int gravity, int xOffset, int yOffset) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xOffset, yOffset);
            mToast.show();
        }
    }

    public static void showToastWithImageAndText(Context context, CharSequence message, int iconResId,int duration,int gravity, int xOffset, int yOffset) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xOffset, yOffset);
            LinearLayout toastView = (LinearLayout) mToast.getView();
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(iconResId);
            toastView.addView(imageView, 0);
            mToast.show();
        }
    }

    public static void customToastAll(Context context, CharSequence message, int duration,View view,boolean isGravity,int gravity, int xOffset, int yOffset,boolean isMargin,float horizontalMargin, float verticalMargin) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            if(view != null){
                mToast.setView(view);
            }
            if(isMargin){
                mToast.setMargin(horizontalMargin, verticalMargin);
            }
            if(isGravity){
                mToast.setGravity(gravity, xOffset, yOffset);
            }
            mToast.show();
        }
    }

    public static void customToastAll(Context context, int resId, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, duration);
            } else {
                mToast.setText(resId);
            }
            if(view != null){
                mToast.setView(view);
            }
            if(isMargin){
                mToast.setMargin(horizontalMargin, verticalMargin);
            }
            if(isGravity){
                mToast.setGravity(gravity, xOffset, yOffset);
            }
            mToast.show();
        }
    }
}
