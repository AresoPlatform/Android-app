package com.xw.aschwitkey.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDialog extends Dialog {

    private TextView textView;

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setMsg(String msg) {
       textView.setText(msg);
    }

    public MyDialog(@NonNull Context context) {
        super(context);
    }

    public MyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public MyDialog(Context context, View layout, int style) {
        super(context, style);

        setContentView(layout);

        Window window = getWindow();

        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.CENTER;

        params.width = context.getResources().getDisplayMetrics().widthPixels - OtherUtils.dp2px(context, 60);

        window.setAttributes(params);
    }
}
