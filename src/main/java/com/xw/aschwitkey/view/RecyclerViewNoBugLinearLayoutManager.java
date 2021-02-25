package com.xw.aschwitkey.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewNoBugLinearLayoutManager extends LinearLayoutManager {
    public RecyclerViewNoBugLinearLayoutManager(Context context) {
        super(context);
    }

    public RecyclerViewNoBugLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public RecyclerViewNoBugLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //try catch一下
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            /**
             * 捕获 IndexOutOfBoundsException  异常
             */
            return super.scrollVerticallyBy(dy, recycler, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return RecyclerViewNoBugLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            //控制速度。
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 0.03f / displayMetrics.density;
            }
            @Override
            protected int calculateTimeForScrolling(int dx) {
                if (dx > 3000) {
                    dx = 3000;
                }

                int time = super.calculateTimeForScrolling(dx);
                return time;
            }
        };

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}
