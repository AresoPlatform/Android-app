package com.xw.aschwitkey.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.ViewUtils;

public class FreezingRulesActivity extends AppCompatActivity {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;

    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freezing_rules);
        ButterKnife.bind(this);
        OtherUtils.config(this);
        init();
    }

    private void init() {
        mContext = FreezingRulesActivity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(FreezingRulesActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

    }

    @OnClick({R.id.mImageView_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mImageView_back:
                finish();
                break;
        }
    }

}
