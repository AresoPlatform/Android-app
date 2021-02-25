package com.xw.aschwitkey.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class SoftKeyBoardListener {
    private View rootView;
    int rootViewVisibleHeight;
    private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;

    public SoftKeyBoardListener(Activity activity){
        rootView = activity.getWindow().getDecorView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);
                int visibleHeight = rect.height();
                if(rootViewVisibleHeight == 0){
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                if(rootViewVisibleHeight == visibleHeight){
                    return;
                }

                if(rootViewVisibleHeight -visibleHeight > 200){
                    if(onSoftKeyBoardChangeListener !=null){
                        onSoftKeyBoardChangeListener.keyBoardShow(rootViewVisibleHeight - visibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                if(visibleHeight - rootViewVisibleHeight > 200){
                    if(onSoftKeyBoardChangeListener != null){
                        onSoftKeyBoardChangeListener.keyBoardHide(visibleHeight - rootViewVisibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
            }
        });

    }

    public interface OnSoftKeyBoardChangeListener{
        void keyBoardShow(int height);
        void keyBoardHide(int height);
    }

    public void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;
    }
}
