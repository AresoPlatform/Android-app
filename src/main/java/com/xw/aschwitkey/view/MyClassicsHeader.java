package com.xw.aschwitkey.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.utils.OtherUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class MyClassicsHeader extends RelativeLayout implements RefreshHeader {

    private LottieAnimationView lottieAnimationView;

    public MyClassicsHeader(Context context) {
        super(context);
        initView(context);
    }

    public MyClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public MyClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        lottieAnimationView = new LottieAnimationView(context);
        lottieAnimationView.setAnimation(R.raw.t_loading);
        lottieAnimationView.setRepeatCount(-1);
        addView(lottieAnimationView, OtherUtils.dp2px(context, 60), OtherUtils.dp2px(context, 60));
        setMinimumHeight(OtherUtils.dp2px(context, 60));
    }

    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int maxDragHeight) {
        lottieAnimationView.playAnimation();//开始动画
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        lottieAnimationView.cancelAnimation();//停止动画
        lottieAnimationView.setProgress(0f);
//        if (success){
//            mHeaderText.setText("刷新完成");
//        } else {
//            mHeaderText.setText("刷新失败");
//        }
        return 100;//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
//                mHeaderText.setText("下拉开始刷新");
                break;
            case Refreshing:
//                mHeaderText.setText("正在刷新");
//                mProgressView.setVisibility(VISIBLE);//显示加载动画
                break;
            case ReleaseToRefresh:
//                mHeaderText.setText("释放立即刷新");
                break;
        }
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int maxDragHeight) {
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {
    }
}
