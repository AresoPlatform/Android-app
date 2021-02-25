package com.xw.aschwitkey.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.xw.aschwitkey.R;

public class DialogUtils {
    public static MyDialog createLoadingDialog(Context context, String msg) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);

        LinearLayout layout = view.findViewById(R.id.mLinearLayout);
        LottieAnimationView mLottieAnimationView = view.findViewById(R.id.mLottieAnimationView);
        mLottieAnimationView.setAnimation(R.raw.loading);
        mLottieAnimationView.setRepeatCount(-1);
        mLottieAnimationView.playAnimation();

        TextView textView = view.findViewById(R.id.mTextView);
        textView.setText(msg);

        MyDialog dialog = new MyDialog(context, R.style.MyDialogStyle);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));


        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);

        dialog.setTextView(textView);

        dialog.show();
        return dialog;
    }

    public static void closeDialog(Dialog dialog) {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }catch (Exception e){
        }finally {
            dialog = null;
        }
    }

    public static void visivble(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {

        }
    }
}
