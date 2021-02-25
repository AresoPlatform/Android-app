package com.xw.aschwitkey.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.happybubble.BubbleLayout;
import com.xujiaji.happybubble.Util;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.AccountManagementActivity;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.activity.CustomActivity;
import com.xw.aschwitkey.activity.SetLockInfoActivity;
import com.xw.aschwitkey.adapter.HistoryAdapter;
import com.xw.aschwitkey.db.SQLUtils;
import com.xw.aschwitkey.db.TronSQLUtils;
import com.xw.aschwitkey.entity.DBHelperBean;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.entity.HistoryBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.AESUtil;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.ZXingUtils;
import com.xw.aschwitkey.view.AutoHeightViewPager;
import com.xw.aschwitkey.view.MyClassicsHeader;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class XasWalletFragment extends NewLazyFragment implements OnRefreshListener, HistoryAdapter.OnClickListenerFace {

    @BindView(R.id.mTextView_account)
    TextView mTextView_account;
    @BindView(R.id.mTextView_address)
    TextView mTextView_address;
    @BindView(R.id.mTextView_allMoney)
    TextView mTextView_allMoney;
    @BindView(R.id.mTextView_available)
    TextView mTextView_available;
    @BindView(R.id.mTextView_freeze)
    TextView mTextView_freeze;
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mRelativeLayout_copy)
    RelativeLayout mRelativeLayout_copy;
    @BindView(R.id.mLinearLayout_import_xas)
    LinearLayout mLinearLayout_import_xas;
    @BindView(R.id.mViewPager)
    AutoHeightViewPager mViewPager;
    @BindView(R.id.mTextView_transferHistory)
    TextView mTextView_transferHistory;
    @BindView(R.id.mTextView_transactionHistory)
    TextView mTextView_transactionHistory;
    @BindView(R.id.mTextView_bg)
    TextView mTextView_bg;


    private Activity mContext;
    private MyViewPagerAdapter adapter;
    private List<HistoryBean.History> list;
    private Gson gson;
    private SPUtils spUtils;
    private SPUtils spUtils1;
    private int day = 90, xasLockQuarter = 1, xasLockHalfYear = 2, xasLockOneYear = 4, xasLockTwoYear = 16, xasLockLimit = 100, xasLockRate = 1;
    private MyDialog myDialog,myDialog1;
    private int RESULT_PTYE_CAM;
    private Dialog dialog;
    private Dialog dialog1;
    private String address = "", creatorAddress = "", privateKey = "", tradePassword, tronAddress = "", editPrivateKey = "";
    private long lockHeight = 0l, height = 0l, nowHeight = 0l;
    private EditText mEditText_address;
    private boolean isClick = false, isSecondSecret = false, flag = true, flag1 = true, isTron = false, isAnimation = false, isTransfer = true, isAnimation1 = false, isPause = false, isFirst = true;
    private String lockedBalance = "0", totalBalance = "0", usableBalance = "0", secret;
    private WebView mWebView;
    private EditText mEditText_tradePassword, mEditText_secondSecret;
    private List<Fragment> fragments;
    private Timer timer;
    private TimerTask task;


    @Override
    public void onResume() {
        super.onResume();
        if (!spUtils1.getString("childAddress", "").isEmpty()) {
            if (isFirst) {
                getUserInfo();
            } else {
                if (spUtils1.getBoolean("isPageRefresh", false)) {
                    myDialog = DialogUtils.createLoadingDialog(mContext, "链上数据加载中...");
                    getUserInfo();
                    if (mViewPager.getCurrentItem() == 0) {
                        EventBus.getDefault().postSticky(new EventMessageBean(20, new HashMap()));
                    } else {
                        EventBus.getDefault().postSticky(new EventMessageBean(21, new HashMap()));
                    }
                }
            }
            isFirst = false;
        }
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        while (isPause) {
                            Thread.sleep(15000);
                        }
                        if (spUtils1.getString("childAddress", "").isEmpty()) {
                            return;
                        }
                        getBalance(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        timer.schedule(task, 15000, 15000);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (task != null) {
            task.cancel();
            task = null;
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_xas_wallet_layout;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        super.initData();
        gson = new Gson();
        spUtils = new SPUtils(mContext, "AschWallet");
        spUtils1 = new SPUtils(mContext, "AschImport");
        list = new ArrayList<>();
        fragments = new ArrayList<>();

        fragments.add(new TransferHistoryFragment());
        fragments.add(new TransactionHistoryFragment());
        adapter = new MyViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mContext);

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setScanScroll(true);
        mViewPager.setCurrentItem(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.requestLayout();
                if (position == 0) {
                    isTransfer = true;
                } else {
                    isTransfer = false;
                }
                if (isTransfer && !isAnimation1) {
                    TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE, mTextView_bg.getWidth(),
                            Animation.ABSOLUTE, -1f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isAnimation1 = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isAnimation1 = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    animation.setDuration(300);
                    animation.setFillAfter(true);
                    animation.setRepeatCount(0);
                    mTextView_bg.startAnimation(animation);
                    mViewPager.setCurrentItem(0);
                    mTextView_transferHistory.setTextColor(getResources().getColor(R.color.text_bg));
                    mTextView_transactionHistory.setTextColor(getResources().getColor(R.color.TextColor1));
                }
                if (!isTransfer && !isAnimation1) {
                    TranslateAnimation animation1 = new TranslateAnimation(Animation.ABSOLUTE, 0.0f,
                            Animation.ABSOLUTE, mTextView_bg.getWidth(),
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isAnimation1 = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isAnimation1 = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    animation1.setDuration(300);
                    animation1.setFillAfter(true);
                    animation1.setRepeatCount(0);
                    mTextView_bg.startAnimation(animation1);
                    mTextView_transferHistory.setTextColor(getResources().getColor(R.color.TextColor1));
                    mTextView_transactionHistory.setTextColor(getResources().getColor(R.color.text_bg));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        setPullRefresher();
        getTronAddress(1);
        if (!spUtils1.getString("childAddress", "").isEmpty()) {
            mLinearLayout_import_xas.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            mLinearLayout_import_xas.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.GONE);
        }
    }

    private void getUserInfo() {
        List<DBHelperBean> beanList = SQLUtils.QuerySQLAll(mContext, "where state = '"+spUtils.getString("phone")+"@'");
        for (int i = 0; i < beanList.size(); i++) {
            if (spUtils1.getString("childAddress", "").equals(beanList.get(i).getAddress())) {
                mRelativeLayout_copy.setVisibility(View.VISIBLE);
                mTextView_account.setText(beanList.get(i).getAccount());
                mTextView_address.setText(beanList.get(i).getAddress());
                secret = beanList.get(i).getSecret();
            }
        }
        getBalance(null);
    }

    @OnClick({R.id.mRelativeLayout_scan, R.id.mRelativeLayout_code, R.id.mRelativeLayout_import, R.id.mTextView_btn_switch, R.id.mRelativeLayout_copy, R.id.mTextView_btn_transfer, R.id.mTextView_btn_setLock, R.id.mLinearLayout_locked, R.id.mImageView_import_xas, R.id.mTextView_transferHistory, R.id.mTextView_transactionHistory})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mRelativeLayout_scan:
                if (!spUtils.getBoolean("isDeal", false)) {
                    ToastUtil.showLong(mContext, "请先到账户中心设置交易密码");
                    return;
                }
                if (spUtils1.getString("childAddress", "").isEmpty()) {
                    ToastUtil.showLong(mContext, "您还没有添加账户，请先到账户管理中导入或创建账户");
                    return;
                }
                RESULT_PTYE_CAM = 1;
                cameraApply();
                break;
            case R.id.mRelativeLayout_code:
                if (!spUtils.getBoolean("isDeal", false)) {
                    ToastUtil.showLong(mContext, "请先到账户中心设置交易密码");
                    return;
                }
                if (spUtils1.getString("childAddress", "").isEmpty()) {
                    ToastUtil.showLong(mContext, "您还没有添加账户，请先到账户管理中导入或创建账户");
                    return;
                }
                myQRCode();
                break;
            case R.id.mImageView_import_xas:
            case R.id.mRelativeLayout_import:
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    if (!spUtils.getBoolean("isDeal", false)) {
                        isClick = false;
                        ToastUtil.showLong(mContext, "请先到账户中心设置交易密码");
                        return;
                    }
                    Intent ia = new Intent(mContext, AccountManagementActivity.class);
                    ia.putExtra("tradePassword", "");
                    startActivity(ia);
                    isClick = false;
                }
                break;
            case R.id.mTextView_btn_switch:
                List<DBHelperBean> beanList = SQLUtils.QuerySQLAll(mContext, "where state = '"+spUtils.getString("phone")+"@'");
                if (beanList.isEmpty()) {
                    ToastUtil.showLong(mContext, "当前没有账户可切换，请先到账户管理中导入或新建账户");
                } else {
                    List<String> items = new ArrayList<>();
                    for (int i = 0; i < beanList.size(); i++) {
                        items.add("[" + beanList.get(i).getAccount() + "]" + beanList.get(i).getAddress());
                    }
                    OptionPicker picker = new OptionPicker(mContext, items);
                    picker.setDividerRatio(WheelView.DividerConfig.WRAP);
                    picker.setShadowColor(getResources().getColor(R.color.bg_color3), 40);
                    picker.setCancelTextColor(getResources().getColor(R.color.text_bg));
                    picker.setSubmitTextColor(getResources().getColor(R.color.text_bg));
                    picker.setTopLineColor(getResources().getColor(R.color.white1));
                    picker.setLineSpaceMultiplier(4);
                    picker.setCycleDisable(true);
                    picker.setTextSize(11);
                    picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                        @Override
                        public void onOptionPicked(int index, String item) {
                            spUtils1.putString("childAddress", item.substring(item.indexOf("]") + 1, item.length()));
                            mTextView_address.setText(item.substring(item.indexOf("]") + 1, item.length()));
                            mTextView_account.setText(item.substring(1, item.indexOf("]")));
                            getUserInfo();
                            if (mViewPager.getCurrentItem() == 0) {
                                EventBus.getDefault().postSticky(new EventMessageBean(20, new HashMap()));
                            } else {
                                EventBus.getDefault().postSticky(new EventMessageBean(21, new HashMap()));
                            }
                        }
                    });
                    picker.show();
                }
                break;
            case R.id.mRelativeLayout_copy:
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("address", mTextView_address.getText().toString());
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort(mContext, "账户地址已复制到剪贴板");
                break;
            case R.id.mTextView_btn_transfer:
                if (!spUtils.getBoolean("isDeal", false)) {
                    ToastUtil.showLong(mContext, "请先到账户中心设置交易密码");
                    return;
                }
                if (spUtils1.getString("childAddress", "").isEmpty()) {
                    ToastUtil.showLong(mContext, "您还没有添加账户，请先到账户管理中导入或创建账户");
                    return;
                }
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    transferDialog();
                }
                break;
            case R.id.mTextView_btn_setLock:
                try {
                    if (System.currentTimeMillis() + Content.timePoor < OtherUtils.dateToStamp(Content.apStartTime)) {
                        ToastUtil.showLong(mContext, "AP凭证挖矿暂未开启，开启时间：" + Content.apStartTime);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!spUtils.getBoolean("isDeal", false)) {
                    ToastUtil.showLong(mContext, "请先到账户中心设置交易密码");
                    return;
                }
                if (spUtils1.getString("childAddress", "").isEmpty()) {
                    ToastUtil.showLong(mContext, "您还没有添加账户，请先到账户管理中导入或创建账户");
                    return;
                }
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    if (isTron) {
                        if (spUtils.getString("tronAddress", "").isEmpty()) {
                            dialogAuthentication();
                        } else {
                            if (lockHeight == 0l) {
                                myDialog1 = DialogUtils.createLoadingDialog(mContext, "获取置换比例...");
                                getLockRate();
                            } else {
                                myDialog1 = DialogUtils.createLoadingDialog(mContext, "获取区块高度...");
                                getHeight();
                            }
                        }
                    } else {
                        myDialog1 = DialogUtils.createLoadingDialog(mContext, "获取TRON绑定信息...");
                        getTronAddress(0);
                    }
                }
                break;
            case R.id.mLinearLayout_locked:
                if (isTron) {
                    if (spUtils.getString("tronAddress", "").isEmpty()) {
                        ToastUtil.showShort(mContext, "请先进行锁仓置换");
                    } else {
                        Intent intentLock = new Intent(mContext, SetLockInfoActivity.class);
                        startActivity(intentLock);
                    }
                } else {
                    myDialog1 = DialogUtils.createLoadingDialog(mContext, "获取TRON绑定信息...");
                    getTronAddress(2);
                }
                break;
            case R.id.mTextView_transferHistory:
                if (!isTransfer && !isAnimation1) {
                    isTransfer = true;
                    TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE, mTextView_bg.getWidth(),
                            Animation.ABSOLUTE, -1f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isAnimation1 = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isAnimation1 = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    animation.setDuration(300);
                    animation.setFillAfter(true);
                    animation.setRepeatCount(0);
                    mTextView_bg.startAnimation(animation);
                    mViewPager.setCurrentItem(0);
                    mTextView_transferHistory.setTextColor(getResources().getColor(R.color.text_bg));
                    mTextView_transactionHistory.setTextColor(getResources().getColor(R.color.TextColor1));
                }
                break;
            case R.id.mTextView_transactionHistory:
                if (isTransfer && !isAnimation1) {
                    isTransfer = false;
                    TranslateAnimation animation1 = new TranslateAnimation(Animation.ABSOLUTE, 0.0f,
                            Animation.ABSOLUTE, mTextView_bg.getWidth(),
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isAnimation1 = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isAnimation1 = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    animation1.setDuration(300);
                    animation1.setFillAfter(true);
                    animation1.setRepeatCount(0);
                    mTextView_bg.startAnimation(animation1);
                    mViewPager.setCurrentItem(1);
                    mTextView_transferHistory.setTextColor(getResources().getColor(R.color.TextColor1));
                    mTextView_transactionHistory.setTextColor(getResources().getColor(R.color.text_bg));
                }
                break;
        }
    }

    private void getLockRate() {
        OkHttpClient.initGet(Http.getLockRate).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog1);
                        ToastUtil.showShort(mContext, "网络错误，获取置换比例失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DialogUtils.closeDialog(myDialog1);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                xasLockQuarter = jsonObject.getJSONObject("data").getInt("xasLockQuarter");
                                xasLockHalfYear = jsonObject.getJSONObject("data").getInt("xasLockHalfYear");
                                xasLockOneYear = jsonObject.getJSONObject("data").getInt("xasLockOneYear");
                                xasLockTwoYear = jsonObject.getJSONObject("data").getInt("xasLockTwoYear");
                                xasLockLimit = jsonObject.getJSONObject("data").getInt("xasLockLimit");
                                xasLockRate = jsonObject.getJSONObject("data").getInt("xasLockRate");
                                setLockDialog();
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "获取置换比例错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void getTronAddress(int type) {
        OkHttpClient.initGet(Http.getTronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog1);
                        isTron = false;
                        isClick = false;
                        ToastUtil.showShort(mContext, "网络错误，获取TRON绑定信息失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            isTron = false;
                            isClick = false;
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                isTron = true;
                                spUtils.putString("tronAddress", jsonObject.getString("data"));
                                if (type == 0) {
                                    if (spUtils.getString("tronAddress", "").isEmpty()) {
                                        DialogUtils.closeDialog(myDialog1);
                                        ToastUtil.showShort(mContext, "请先左滑至波场钱包导入或新建波场账户并绑定");
                                    } else {
                                        if (lockHeight == 0l) {
                                            myDialog1.setMsg("获取置换比例...");
                                            getLockRate();
                                        } else {
                                            myDialog1.setMsg("获取区块高度...");
                                            getHeight();
                                        }
                                    }
                                } else if (type == 2) {
                                    DialogUtils.closeDialog(myDialog1);
                                    if (spUtils.getString("tronAddress", "").isEmpty()) {
                                        ToastUtil.showShort(mContext, "请先进行锁仓置换");
                                        return;
                                    } else {
                                        Intent intentLock = new Intent(mContext, SetLockInfoActivity.class);
                                        startActivity(intentLock);
                                    }
                                }
                            } else {
                                DialogUtils.closeDialog(myDialog1);
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog1);
                        }
                    }
                });
            }
        });
    }

    private void dialogAuthentication() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_authentication_layout, null);
        EditText mEditText_verification = v.findViewById(R.id.mEditText_verification);
        TextView mTextView_btn_cancel = v.findViewById(R.id.mTextView_btn_cancel);
        TextView mTextView_confirm = v.findViewById(R.id.mTextView_confirm);

        mTextView_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });

        mTextView_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                if (mEditText_verification.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "交易密码不能为空", Toast.LENGTH_SHORT).show();
                }
                hintKeyBoard(v);
                verifyAccount(mEditText_verification.getText().toString());
            }
        });

        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog.setCancelable(false);
        dialog.setContentView(v);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.1f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private void verifyAccount(String password) {
        Map postmap = new HashMap();
        try {
            postmap.put("password", AESUtil.encrypt(spUtils.getString("phone"), password));
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.verifyAccount, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(mContext, "网络错误，身份验证失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                dialog.dismiss();
                                tradePassword = password;
                                bindingAddressDialog();
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setPullRefresher() {
        mRefreshLayout.setRefreshHeader(new MyClassicsHeader(mContext));
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessageBean messageEvent) {
        switch (messageEvent.getTag()) {
            case 9:
                if (spUtils1.getString("childAddress", "").isEmpty()) {
                    mLinearLayout_import_xas.setVisibility(View.VISIBLE);
                    mRefreshLayout.setVisibility(View.GONE);
                    mRelativeLayout_copy.setVisibility(View.GONE);
                    mTextView_account.setText("");
                    mTextView_address.setText("");
                    secret = "";
                    mTextView_allMoney.setText("0.000 xas");
                    mTextView_freeze.setText("0.000");
                    mTextView_available.setText("0.000");
                    list.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    mLinearLayout_import_xas.setVisibility(View.GONE);
                    mRefreshLayout.setVisibility(View.VISIBLE);
                    getUserInfo();
                    if (mViewPager.getCurrentItem() == 0) {
                        EventBus.getDefault().postSticky(new EventMessageBean(20, new HashMap()));
                    } else {
                        EventBus.getDefault().postSticky(new EventMessageBean(21, new HashMap()));
                    }
                }
                break;
            case 23:
                if (!(boolean) messageEvent.getMesssage().get("isShow")) {
                    getBalance(null);
                    if (mViewPager.getCurrentItem() == 0) {
                        mViewPager.setCurrentItem(1);
                    }
                }
                break;
        }
    }

    private void transferDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_transfer_layout, null);
        LinearLayout mLinearLayout_scan = v.findViewById(R.id.mLinearLayout_scan);
        mEditText_address = v.findViewById(R.id.mEditText_address);
        EditText mEditText_amount = v.findViewById(R.id.mEditText_amount);
        EditText mEditText_message = v.findViewById(R.id.mEditText_message);
        TextView mTextView_btn_cancel = v.findViewById(R.id.mTextView_btn_cancel);
        LinearLayout mLinearLayout_btn_subscribe = v.findViewById(R.id.mLinearLayout_btn_subscribe);
        TextView mTextView_balance = v.findViewById(R.id.mTextView_balance);

        mTextView_balance.setText(usableBalance + " xas");
        mEditText_address.setText(address);

        mEditText_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Double.parseDouble(usableBalance) == 0d && flag) {
                    ToastUtil.showShort(mContext, "当前可用资产为零");
                    flag = false;
                    s.clear();
                    flag = true;
                    return;
                }
                if (!s.toString().isEmpty() && flag) {
                    BigDecimal result;
                    String temp = s.toString();
                    int posDot = temp.indexOf(".");
                    int zero = temp.indexOf("0");
                    try {
                        if (posDot == 0) {
                            s.clear();
                            s.append("0");
                            return;
                        }
                        if (posDot == s.length() - 1) {
                            return;
                        }
                        if (zero == 0 && posDot != 1 && temp.length() > 1) {
                            s.clear();
                            s.append(temp.substring(1, temp.length()));
                            return;
                        }
                        result = new BigDecimal(s.toString());
                    } catch (Exception e) {
                        return;
                    }

                    if (result.compareTo(new BigDecimal(usableBalance).subtract(new BigDecimal("1.1"))) == 1) {
                        temp = new DecimalFormat("0.000").format(new BigDecimal(usableBalance).subtract(new BigDecimal("1.1")));
                        ToastUtil.showShort(mContext, "最多可以转账" + new DecimalFormat("0.000").format(new BigDecimal(usableBalance).subtract(new BigDecimal("1.1"))) + "XAS");
                    }

                    flag = false;
                    s.clear();
                    if (posDot > 0 && temp.length() - posDot - 1 > 3) {
                        temp = temp.substring(0, posDot + 4);
                    }
                    s.append(temp);

                    flag = true;
                }
            }
        });

        mLinearLayout_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RESULT_PTYE_CAM = 2;
                cameraApply();
            }
        });

        mTextView_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });

        mLinearLayout_btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                if (mEditText_address.getText().toString().trim().isEmpty()) {
                    Toast.makeText(mContext, "收款地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEditText_amount.getText().toString().trim().isEmpty() || Float.valueOf(mEditText_amount.getText().toString()) == 0f) {
                    Toast.makeText(mContext, "转账金额不能为空或0", Toast.LENGTH_SHORT).show();
                    return;
                }
                payDialog(mEditText_address.getText().toString(), Double.parseDouble(mEditText_amount.getText().toString()), mEditText_message.getText().toString());
            }
        });

        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog.setContentView(v);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.1f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private void payDialog(String address, double amount, String message) {
        View v1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_transfer_trade_password_layout, null);
        TextView mTextView_currency = v1.findViewById(R.id.mTextView_currency);
        mEditText_tradePassword = v1.findViewById(R.id.mEditText_tradePassword);
        mEditText_secondSecret = v1.findViewById(R.id.mEditText_secondSecret);
        RelativeLayout mRelativeLayout_secondSecret = v1.findViewById(R.id.mRelativeLayout_secondSecret);
        if (isSecondSecret) {
            mRelativeLayout_secondSecret.setVisibility(View.VISIBLE);
        } else {
            mRelativeLayout_secondSecret.setVisibility(View.GONE);
        }
        mTextView_currency.setText(new DecimalFormat("0.000").format(amount) + " xas");
        v1.findViewById(R.id.mTextView_btn_transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKeyBoard(v);
                String password = mEditText_tradePassword.getText().toString();
                String secondSecret = mEditText_secondSecret.getText().toString();
                if (password.isEmpty()) {
                    ToastUtil.showShort(mContext, "交易密码不能为空");
                    return;
                }
                if (isSecondSecret && secondSecret.isEmpty()) {
                    ToastUtil.showShort(mContext, "二级密码不能为空");
                    return;
                }
                transferWeb(address, amount, message, password, secondSecret);
            }
        });
        v1.findViewById(R.id.mImageView_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1 = new Dialog(mContext, R.style.LeftDialogStyle);
        dialog1.setContentView(v1);
        dialog1.setCancelable(false);
        Window dialogWindow1 = dialog1.getWindow();
        if (dialogWindow1 == null) {
            return;
        }
        dialogWindow1.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
        lp1.dimAmount = 0.1f;
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow1.setAttributes(lp1);
        dialog1.show();
    }

    private void transferWeb(String address, double amount, String message, String password, String secondSecret) {
        myDialog1 = DialogUtils.createLoadingDialog(mContext, "转账中...");
        mWebView = new WebView(getActivity());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        JavaScriptInterface javaScriptInterface = new JavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/transfer.html");


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                JsParseMainAssetTra(address, amount, message, password, secondSecret);
            }
        });
    }

    private void JsParseMainAssetTra(String address, double amount, String message, String password, String secondSecret) {
        String url = null;
        try {
            url = "javascript:stub.startFunction(dappTrans('" + AESUtil.decrypt(secret, password) + "'," + amount + ",'" + address + "','" + secondSecret + "','" + message + "'" + "));";
        } catch (Exception e) {
            mEditText_tradePassword.setText("");
            mEditText_secondSecret.setText("");
            DialogUtils.closeDialog(myDialog1);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private void bindingAddressDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_bind_tron_layout, null);
        EditText mEditText_privateKey = v.findViewById(R.id.mEditText_privateKey);

        v.findViewById(R.id.mLinearLayout_creatorAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog1 = DialogUtils.createLoadingDialog(mContext, "新建中...");
                creatorAddress();
            }
        });
        v.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.mTextView_btn_binding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText_privateKey.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "私钥不能为空");
                    return;
                }
                myDialog1 = DialogUtils.createLoadingDialog(mContext, "绑定中...");
                editPrivateKey = mEditText_privateKey.getText().toString();
                verification();
            }
        });

        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog.setContentView(v);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.1f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private void verification() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        VerJavaScriptInterface javaScriptInterface = new VerJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/verificationPrivateKey.html");


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                JsParseMainAssetTra();
            }
        });
    }

    private void JsParseMainAssetTra() {
        String url = null;
        try {
            url = "javascript:stub.startFunction(verification('" + editPrivateKey + "'));";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog1);
            ToastUtil.showShort(mContext, "私钥不正确");
        }
        mWebView.loadUrl(url);
    }

    private class VerJavaScriptInterface {
        @JavascriptInterface
        public void startFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        DialogUtils.closeDialog(myDialog1);
                        ToastUtil.showShort(mContext, "私钥不正确");
                    }else{
                        tronAddress = str;
                        bindTronAddress(tronAddress, editPrivateKey, false);
                    }
                }
            });
        }
    }

    private void bindTronAddress(String tronAddress, String tronPrivateKey, boolean isCreator) {
        OkHttpClient.initGet(Http.setTronAddress + "?tronAddress=" + tronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog1);
                        ToastUtil.showShort(mContext, "网络错误，绑定失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DialogUtils.closeDialog(myDialog1);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                dialog.dismiss();
                                ToastUtil.showShort(mContext, "绑定成功");
                                spUtils.putString("tronAddress", tronAddress);
                                spUtils1.putString("tronChildAddress", tronAddress);
                                EventBus.getDefault().postSticky(new EventMessageBean(24, new HashMap()));
                                try {
                                    TronSQLUtils.AddSql(mContext, "TRON", tronAddress, AESUtil.encrypt(tronPrivateKey, tradePassword), spUtils.getString("phone")+"@");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (!isCreator) {
                                    if (lockHeight == 0l) {
                                        myDialog1 = DialogUtils.createLoadingDialog(mContext, "获取置换比例...");
                                        getLockRate();
                                    } else {
                                        myDialog1 = DialogUtils.createLoadingDialog(mContext, "获取区块高度...");
                                        getHeight();
                                    }
                                } else {
                                    creatorAddressDialog();
                                }
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        });
    }

    private void setLockDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_set_lock_layout, null);
        TextView mTextView_tronAddress = v.findViewById(R.id.mTextView_tronAddress);
        TextView mTextView_quota = v.findViewById(R.id.mTextView_quota);
        EditText mEditText_quota = v.findViewById(R.id.mEditText_quota);
        TextView mTextView_3months = v.findViewById(R.id.mTextView_3months);
        TextView mTextView_6months = v.findViewById(R.id.mTextView_6months);
        TextView mTextView_year = v.findViewById(R.id.mTextView_year);
        TextView mTextView_2year = v.findViewById(R.id.mTextView_2year);
        TextView mTextView_currentHeight = v.findViewById(R.id.mTextView_currentHeight);
        TextView mTextView_TXas = v.findViewById(R.id.mTextView_TXas);
        TextView mTextView_btn_cancel = v.findViewById(R.id.mTextView_btn_cancel);
        LinearLayout mLinearLayout_btn_subscribe = v.findViewById(R.id.mLinearLayout_btn_subscribe);
        TextView mTextView_balance = v.findViewById(R.id.mTextView_balance);
        ImageView mImageView_timeQuestion = v.findViewById(R.id.mImageView_timeQuestion);
        ImageView mImageView_rateQuestion = v.findViewById(R.id.mImageView_rateQuestion);

        mTextView_quota.setText("锁仓额度（最低" + xasLockLimit + "）");

        if (lockHeight == 0l) {
            mTextView_currentHeight.setVisibility(View.GONE);
            mImageView_timeQuestion.setVisibility(View.GONE);
        } else {
            mTextView_currentHeight.setVisibility(View.VISIBLE);
            mImageView_timeQuestion.setVisibility(View.VISIBLE);
            mTextView_currentHeight.setText("已锁仓截止时间：" + OtherUtils.timedate(System.currentTimeMillis() + Content.timePoor + (lockHeight - height) * 10 * 1000, "yyyy/MM/dd HH:mm:ss"));
        }
        mTextView_tronAddress.setText(spUtils.getString("tronAddress"));
        mTextView_balance.setText(usableBalance + " xas");
        mTextView_3months.setTextColor(getResources().getColor(R.color.white));
        mTextView_3months.setSelected(true);
        day = 90;

        mImageView_timeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_question_layout, null);
                TextView mTextView_text1 = dialogView1.findViewById(R.id.mTextView_text);
                mTextView_text1.setText("再次锁仓将在此截止时间基础上增加天数");
                BubbleLayout bl1 = new BubbleLayout(mContext);
                bl1.setBubbleColor(mContext.getResources().getColor(R.color.bg_color_ccBlack));
                bl1.setShadowColor(mContext.getResources().getColor(R.color.bg_color_ccBlack));
                bl1.setLookLength(Util.dpToPx(mContext, 9));
                bl1.setBubbleRadius(Util.dpToPx(mContext, 3));
                BubbleDialog bubbleDialog = new BubbleDialog(mContext)
                        .setClickedView(mImageView_timeQuestion)
                        .setBubbleContentView(dialogView1)
                        .setPosition(BubbleDialog.Position.BOTTOM)
                        .setOffsetY(-8)
                        .setTransParentBackground()
                        .setBubbleLayout(bl1)
                        .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 0);
                bubbleDialog.show();
            }
        });

        mImageView_rateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_question_layout, null);
                TextView mTextView_text1 = dialogView1.findViewById(R.id.mTextView_text);
                mTextView_text1.setText("XAS置换AP凭证规则：由锁仓时间决定，3个月 1:" + xasLockQuarter + " ，6个月 1:" + xasLockHalfYear + " ，1年 1:" + xasLockOneYear + " ，2年 1:" + xasLockTwoYear);
                BubbleLayout bl1 = new BubbleLayout(mContext);
                bl1.setBubbleColor(mContext.getResources().getColor(R.color.bg_color_ccBlack));
                bl1.setShadowColor(mContext.getResources().getColor(R.color.bg_color_ccBlack));
                bl1.setLookLength(Util.dpToPx(mContext, 9));
                bl1.setBubbleRadius(Util.dpToPx(mContext, 3));
                BubbleDialog bubbleDialog1 = new BubbleDialog(mContext)
                        .setClickedView(mImageView_rateQuestion)
                        .setBubbleContentView(dialogView1)
                        .setPosition(BubbleDialog.Position.BOTTOM)
                        .setOffsetY(-8)
                        .setTransParentBackground()
                        .setBubbleLayout(bl1)
                        .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 0);
                bubbleDialog1.show();
            }
        });

        mEditText_quota.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Double.parseDouble(usableBalance) == 0d && flag1) {
                    ToastUtil.showShort(mContext, "当前可用资产为零");
                    flag1 = false;
                    s.clear();
                    flag1 = true;
                    return;
                }
                if (!s.toString().isEmpty() && flag1) {
                    BigDecimal result;
                    String temp = s.toString();
                    int posDot = temp.indexOf(".");
                    int zero = temp.indexOf("0");
                    try {
                        if (posDot == 0) {
                            s.clear();
                            s.append("0");
                            return;
                        }
                        if (posDot == s.length() - 1) {
                            return;
                        }
                        if (zero == 0 && posDot != 1 && temp.length() > 1) {
                            s.clear();
                            s.append(temp.substring(1, temp.length()));
                            return;
                        }
                        result = new BigDecimal(s.toString());
                    } catch (Exception e) {
                        return;
                    }

                    if (result.compareTo(new BigDecimal(usableBalance).subtract(new BigDecimal("1.1"))) == 1) {
                        temp = new BigDecimal(usableBalance).subtract(new BigDecimal("1.1")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                        ToastUtil.showShort(mContext, "最多可以锁仓" + new BigDecimal(usableBalance).subtract(new BigDecimal("1.1")).setScale(0, BigDecimal.ROUND_DOWN).toString() + "XAS");
                    }

                    flag1 = false;
                    s.clear();
                    if (posDot > 0 && temp.length() - posDot - 1 > 0) {
                        temp = temp.substring(0, 0);
                    }
                    s.append(temp);

                    flag1 = true;
                }
            }
        });

        mEditText_quota.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEditText_quota.getText().toString().isEmpty()) {
                    mTextView_TXas.setText("0 AP");
                } else {
                    if (day == 90) {
                        mTextView_TXas.setText(Integer.parseInt(mEditText_quota.getText().toString()) * xasLockQuarter + " AP");
                    } else if (day == 180) {
                        mTextView_TXas.setText(Integer.parseInt(mEditText_quota.getText().toString()) * xasLockHalfYear + " AP");
                    } else if (day == 365) {
                        mTextView_TXas.setText(Integer.parseInt(mEditText_quota.getText().toString()) * xasLockOneYear + " AP");
                    } else if (day == 730) {
                        mTextView_TXas.setText(Integer.parseInt(mEditText_quota.getText().toString()) * xasLockTwoYear + " AP");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTextView_3months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = 90;
                if (mEditText_quota.getText().toString().isEmpty()) {
                    mTextView_TXas.setText("0 AP");
                } else {
                    mTextView_TXas.setText(Integer.parseInt(mEditText_quota.getText().toString()) * xasLockQuarter + " AP");
                }
                mTextView_3months.setTextColor(getResources().getColor(R.color.white));
                mTextView_3months.setSelected(true);
                mTextView_6months.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_6months.setSelected(false);
                mTextView_year.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_year.setSelected(false);
                mTextView_2year.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_2year.setSelected(false);
            }
        });

        mTextView_6months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = 180;
                if (mEditText_quota.getText().toString().isEmpty()) {
                    mTextView_TXas.setText("0 AP");
                } else {
                    mTextView_TXas.setText(Integer.parseInt(mEditText_quota.getText().toString()) * xasLockHalfYear + " AP");
                }
                mTextView_6months.setTextColor(getResources().getColor(R.color.white));
                mTextView_6months.setSelected(true);
                mTextView_3months.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_3months.setSelected(false);
                mTextView_year.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_year.setSelected(false);
                mTextView_2year.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_2year.setSelected(false);
            }
        });

        mTextView_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = 365;
                if (mEditText_quota.getText().toString().isEmpty()) {
                    mTextView_TXas.setText("0 AP");
                } else {
                    mTextView_TXas.setText(Integer.parseInt(mEditText_quota.getText().toString()) * xasLockOneYear + " AP");
                }
                mTextView_year.setTextColor(getResources().getColor(R.color.white));
                mTextView_year.setSelected(true);
                mTextView_3months.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_3months.setSelected(false);
                mTextView_6months.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_6months.setSelected(false);
                mTextView_2year.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_2year.setSelected(false);
            }
        });

        mTextView_2year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = 730;
                if (mEditText_quota.getText().toString().isEmpty()) {
                    mTextView_TXas.setText("0 AP");
                } else {
                    mTextView_TXas.setText(Integer.parseInt(mEditText_quota.getText().toString()) * xasLockTwoYear + " AP");
                }
                mTextView_2year.setTextColor(getResources().getColor(R.color.white));
                mTextView_2year.setSelected(true);
                mTextView_3months.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_3months.setSelected(false);
                mTextView_6months.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_6months.setSelected(false);
                mTextView_year.setTextColor(getResources().getColor(R.color.text_color_gray));
                mTextView_year.setSelected(false);
            }
        });

        mTextView_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });

        mLinearLayout_btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                if (mTextView_tronAddress.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "Tron地址不能为空");
                    return;
                }
                if (mEditText_quota.getText().toString().isEmpty() || Float.parseFloat(mEditText_quota.getText().toString()) == 0f || Float.parseFloat(mEditText_quota.getText().toString()) % xasLockRate != 0) {
                    ToastUtil.showShort(mContext, "锁仓额度需为" + xasLockRate + "的整数倍");
                    return;
                }
                if (Float.parseFloat(mEditText_quota.getText().toString()) < xasLockLimit) {
                    ToastUtil.showShort(mContext, "锁仓额度最低为" + xasLockLimit);
                    return;
                }
                if (day < 90) {
                    ToastUtil.showShort(mContext, "锁仓时间不能小于三个月");
                    return;
                }
                payDialog1(Integer.parseInt(mEditText_quota.getText().toString()));
            }
        });

        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog.setContentView(v);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.1f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
        if (lockHeight != 0l) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_question_layout, null);
                                TextView mTextView_text1 = dialogView1.findViewById(R.id.mTextView_text);
                                mTextView_text1.setText("再次锁仓将在此截止时间基础上增加天数");
                                BubbleLayout bl1 = new BubbleLayout(mContext);
                                bl1.setBubbleColor(mContext.getResources().getColor(R.color.bg_color_ccBlack));
                                bl1.setShadowColor(mContext.getResources().getColor(R.color.bg_color_ccBlack));
                                bl1.setLookLength(Util.dpToPx(mContext, 9));
                                bl1.setBubbleRadius(Util.dpToPx(mContext, 3));
                                BubbleDialog bubbleDialog = new BubbleDialog(mContext)
                                        .setClickedView(mImageView_timeQuestion)
                                        .setBubbleContentView(dialogView1)
                                        .setPosition(BubbleDialog.Position.BOTTOM)
                                        .setOffsetY(-8)
                                        .setTransParentBackground()
                                        .setBubbleLayout(bl1)
                                        .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 0);
                                bubbleDialog.show();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void creatorAddress() {
        mWebView = new WebView(getActivity());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        JavaScriptInterfaceCreator javaScriptInterface = new JavaScriptInterfaceCreator();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/creatorAccount.html");


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                JsCreatorAddress();
            }
        });
    }

    private void JsCreatorAddress() {
        String url = "javascript:tronCreator()";
        mWebView.loadUrl(url);
    }

    private class JavaScriptInterfaceCreator {
        @JavascriptInterface
        public void creatorFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(str);
                        creatorAddress = jsonObject.getJSONObject("address").getString("base58");
                        privateKey = jsonObject.getString("privateKey");
                        spUtils.putString("tronAddress", creatorAddress);
                        dialog.dismiss();
                        myDialog1.setMsg("绑定中...");
                        bindTronAddress(creatorAddress, privateKey, true);
                    } catch (JSONException e) {
                        DialogUtils.closeDialog(myDialog1);
                    }
                }
            });
        }
    }

    private void creatorAddressDialog() {
        View creatorDialog = LayoutInflater.from(mContext).inflate(R.layout.dialog_set_lock_creator_address_layout, null);
        TextView mTextView_address = creatorDialog.findViewById(R.id.mTextView_address);
        TextView mTextView_privateKey = creatorDialog.findViewById(R.id.mTextView_privateKey);
        mTextView_address.setText(creatorAddress);
        mTextView_privateKey.setText(privateKey);

        creatorDialog.findViewById(R.id.mLinearLayout_copyAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("tronAddress", mTextView_address.getText().toString());
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort(mContext, "TRON地址已复制到剪贴板");
            }
        });

        creatorDialog.findViewById(R.id.mLinearLayout_copyKey).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("privateKey", mTextView_privateKey.getText().toString());
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort(mContext, "地址私钥已复制到剪贴板");
            }
        });

        creatorDialog.findViewById(R.id.mTextView_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCreator();
            }
        });

        dialog1 = new Dialog(mContext, R.style.LeftDialogStyle);
        dialog1.setContentView(creatorDialog);
        dialog1.setCancelable(false);
        Window dialogWindow1 = dialog1.getWindow();
        if (dialogWindow1 == null) {
            return;
        }
        dialogWindow1.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
        lp1.dimAmount = 0.1f;
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow1.setAttributes(lp1);
        dialog1.show();
    }

    private void dialogCreator() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_creator_confirm_layout, null);
        TextView mTextView_false = view.findViewById(R.id.mTextView_false);
        TextView mTextView_true = view.findViewById(R.id.mTextView_true);
        mTextView_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mTextView_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog1.dismiss();
                if (lockHeight == 0l) {
                    myDialog1 = DialogUtils.createLoadingDialog(mContext, "获取置换比例...");
                    getLockRate();
                } else {
                    myDialog1 = DialogUtils.createLoadingDialog(mContext, "获取区块高度...");
                    getHeight();
                }
            }
        });
        dialog = new Dialog(mContext, R.style.inputDialog);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.1f;
        lp.width = getResources().getDisplayMetrics().widthPixels - OtherUtils.dp2px(mContext, 60);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private void payDialog1(int quota) {
        View v1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_set_lock_trade_password_layout, null);
        TextView mTextView_setLock = v1.findViewById(R.id.mTextView_setLock);
        TextView mTextView_tXas = v1.findViewById(R.id.mTextView_tXas);
        TextView mTextView_currency = v1.findViewById(R.id.mTextView_currency);
        mEditText_tradePassword = v1.findViewById(R.id.mEditText_tradePassword);
        mEditText_secondSecret = v1.findViewById(R.id.mEditText_secondSecret);
        RelativeLayout mRelativeLayout_secondSecret = v1.findViewById(R.id.mRelativeLayout_secondSecret);
        if (isSecondSecret) {
            mRelativeLayout_secondSecret.setVisibility(View.VISIBLE);
        } else {
            mRelativeLayout_secondSecret.setVisibility(View.GONE);
        }
        mTextView_setLock.setText(quota + " XAS");
        if (day == 90) {
            mTextView_tXas.setText(quota * xasLockQuarter + " AP");
        } else if (day == 180) {
            mTextView_tXas.setText(quota * xasLockHalfYear + " AP");
        } else if (day == 365) {
            mTextView_tXas.setText(quota * xasLockOneYear + " AP");
        } else if (day == 730) {
            mTextView_tXas.setText(quota * xasLockTwoYear + " AP");
        }
        mTextView_currency.setText(new DecimalFormat("0.000").format(quota) + " xas");
        v1.findViewById(R.id.mTextView_btn_transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mEditText_tradePassword.getText().toString();
                String secondSecret = mEditText_secondSecret.getText().toString();
                if (password.isEmpty()) {
                    ToastUtil.showShort(mContext, "交易密码不能为空");
                    return;
                }
                if (isSecondSecret && secondSecret.isEmpty()) {
                    ToastUtil.showShort(mContext, "二级密码不能为空");
                    return;
                }
                myDialog1 = DialogUtils.createLoadingDialog(mContext, "锁仓中...");
                if (lockHeight == 0l) {
                    getHeight(quota, password, secondSecret);
                } else {
                    BigDecimal height = new BigDecimal(lockHeight);
                    BigDecimal dayB = new BigDecimal(day * 24 * 60 * 6);
                    height = height.add(dayB);
                    setLockWeb(quota, height.toString(), password, secondSecret);
                }
            }
        });
        v1.findViewById(R.id.mImageView_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1 = new Dialog(mContext, R.style.LeftDialogStyle);
        dialog1.setContentView(v1);
        dialog1.setCancelable(false);
        Window dialogWindow1 = dialog1.getWindow();
        if (dialogWindow1 == null) {
            return;
        }
        dialogWindow1.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
        lp1.dimAmount = 0.1f;
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow1.setAttributes(lp1);
        dialog1.show();
    }

    private void getHeight() {
        OkHttpClient.initGet(Http.getHeight).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog1);
                        ToastUtil.showShort(mContext, "网络错误，获取区块高度失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            isClick = false;
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("success")) {
                                height = jsonObject.getLong("height");
                                myDialog1.setMsg("获取置换比例...");
                                getLockRate();
                            } else {
                                DialogUtils.closeDialog(myDialog1);
                                ToastUtil.showShort(mContext, "获取区块高度失败，请稍候再试");
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog1);
                        }
                    }
                });
            }
        });
    }

    private void getHeight(double quota, String password, String secondSecret) {
        OkHttpClient.initGet(Http.getHeight).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog1);
                        ToastUtil.showShort(mContext, "网络错误，获取区块高度失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("success")) {
                                nowHeight = jsonObject.getLong("height");
                                BigDecimal height = new BigDecimal(jsonObject.getString("height"));
                                BigDecimal dayB = new BigDecimal(day * 24 * 60 * 6);
                                height = height.add(dayB);
                                setLockWeb(quota, height.toString(), password, secondSecret);
                            } else {
                                DialogUtils.closeDialog(myDialog1);
                                ToastUtil.showShort(mContext, "获取区块高度失败，请稍候再试");
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog1);
                        }
                    }
                });
            }
        });
    }

    private void setLockWeb(double quota, String height, String password, String secondSecret) {
        mWebView = new WebView(getActivity());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        setLockJavaScriptInterface javaScriptInterface = new setLockJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/setLock.html");


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                JsParseSetLock(quota, height, password, secondSecret);
            }
        });
    }

    private void JsParseSetLock(double quota, String height, String password, String secondSecret) {
        String url = null;
        try {
            url = "javascript:stub.setLockFunction(dappTrans(" + quota + "," + height + ",'" + AESUtil.decrypt(secret, password) + "','" + secondSecret + "'" + "));";
        } catch (Exception e) {
            mEditText_tradePassword.setText("");
            mEditText_secondSecret.setText("");
            DialogUtils.closeDialog(myDialog1);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    @Override
    public void OnClickTemp(int p, View view) {
        switch (view.getId()) {
            case R.id.mImageView_copy:
            case R.id.mTextView_address:
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData;
                if (list.get(p).getInOrout() == 1) {
                    mClipData = ClipData.newPlainText("address", list.get(p).getSenderId());
                } else {
                    mClipData = ClipData.newPlainText("address", list.get(p).getRecipientId());
                }
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort(mContext, "账户地址已复制到剪贴板");
                break;
        }
    }

    private class setLockJavaScriptInterface {
        @JavascriptInterface
        public void setLockFunction(String str) {
            setLock(str);
        }
    }

    private void setLock(String str) {
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), str);
        OkHttpClient.initPost(Http.transactions, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEditText_tradePassword.setText("");
                        mEditText_secondSecret.setText("");
                        DialogUtils.closeDialog(myDialog1);
                        ToastUtil.showShort(mContext, "网络错误，锁仓失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("success")) {
                                dialog1.dismiss();
                                dialog.dismiss();
                                myDialog1.setMsg("锁仓成功，正在置换AP凭证...");
                                lockedPosition(jsonObject.getString("transactionId"));
                            } else {
                                DialogUtils.closeDialog(myDialog1);
                                if (jsonObject.getString("error").contains("Invalid second signature")) {
                                    ToastUtil.showShort(mContext, "二级密码错误");
                                } else if (jsonObject.getString("error").contains("Invalid lock height")) {
                                    ToastUtil.showShort(mContext, "锁仓时间早于上一次锁仓截止时间的后三十天");
                                } else {
                                    ToastUtil.showShort(mContext, "锁仓失败，请稍候再试");
                                }
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog1);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void lockedPosition(String transactionId) {
        String url;
        int type;
        if (day == 90) {
            type = 1;
        } else if (day == 180) {
            type = 2;
        } else if (day == 365) {
            type = 3;
        } else if (day == 730) {
            type = 4;
        } else {
            type = 1;
        }
        if (lockHeight == 0l) {
            url = Http.lockedPosition + "?transactionId=" + transactionId + "&lastTimeHeight=" + nowHeight + "&lockTimeType=" + type;
        } else {
            url = Http.lockedPosition + "?transactionId=" + transactionId + "&lastTimeHeight=" + lockHeight + "&lockTimeType=" + type;
        }
        OkHttpClient.initGet(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog1);
                        getBalance(null);
                        ToastUtil.showShort(mContext, "网络错误，提交置换请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DialogUtils.closeDialog(myDialog1);
                            getBalance(null);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                ToastUtil.showShort(mContext, "锁仓置换成功");
                                Map map = new HashMap();
                                map.put("isShow", true);
                                EventBus.getDefault().postSticky(new EventMessageBean(23, map));
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        });
    }

    private class JavaScriptInterface {
        @JavascriptInterface
        public void startFunction(String str) {
            transfer(str);
        }
    }

    private void transfer(String str) {
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), str);
        OkHttpClient.initPost(Http.transactions, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEditText_tradePassword.setText("");
                        mEditText_secondSecret.setText("");
                        DialogUtils.closeDialog(myDialog1);
                        ToastUtil.showShort(mContext, "网络错误，转账失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mEditText_tradePassword.setText("");
                            mEditText_secondSecret.setText("");
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("success")) {
                                DialogUtils.closeDialog(myDialog1);
                                ToastUtil.showShort(mContext, "转账成功");
                                dialog1.dismiss();
                                dialog.dismiss();
                            } else {
                                DialogUtils.closeDialog(myDialog1);
                                if (jsonObject.getString("error").contains("Invalid second signature")) {
                                    ToastUtil.showShort(mContext, "二级密码错误");
                                } else {
                                    ToastUtil.showShort(mContext, "转账失败，请稍候再试");
                                }
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog1);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void getBalance(RefreshLayout refreshLayout) {
        isPause = true;
        OkHttpClient.initGet(Http.getAddressInfo + "?address=" + spUtils1.getString("childAddress")).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isPause = false;
                        DialogUtils.closeDialog(myDialog);
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh(3000);
                        }
                        ToastUtil.showShort(mContext, "网络错误，查询可用资产失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                isPause = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        DialogUtils.closeDialog(myDialog);
                                        if (refreshLayout != null) {
                                            refreshLayout.finishRefresh(3000);
                                        }
                                        JSONObject jsonObject = new JSONObject(result);
                                        if (jsonObject.getInt("code") == 1) {
                                            isSecondSecret = jsonObject.getJSONObject("data").getBoolean("isHaveSecond");
                                            totalBalance = new BigDecimal(jsonObject.getJSONObject("data").getString("totalBalance")).setScale(3, BigDecimal.ROUND_DOWN).toString();
                                            lockedBalance = new BigDecimal(jsonObject.getJSONObject("data").getString("lockedBalance")).setScale(3, BigDecimal.ROUND_DOWN).toString();
                                            usableBalance = new BigDecimal(jsonObject.getJSONObject("data").getString("usableBalance")).setScale(3, BigDecimal.ROUND_DOWN).toString();
                                            lockHeight = jsonObject.getJSONObject("data").getLong("lockHeight");
                                            mTextView_allMoney.setText(totalBalance + " xas");
                                            mTextView_freeze.setText(lockedBalance);
                                            mTextView_available.setText(usableBalance);
                                        } else {
                                            ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                                        }
                                    } catch (JSONException e) {
                                    }
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void cameraApply() {
        if (AndPermission.hasPermissions(mContext, Permission.CAMERA)) {
            startActivityForResult(new Intent(mContext, CustomActivity.class), RESULT_PTYE_CAM);
        } else {
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.CAMERA)
                    .rationale(new Rationale<List<String>>() {
                        @Override
                        public void showRationale(Context context, List<String> data, final RequestExecutor executor) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("权限已被拒绝");
                            builder.setMessage("您已经拒绝过我们申请授权，请您同意授权，否则功能无法正常使用");
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    executor.cancel();
                                }
                            });
                            builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    executor.execute();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    })
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> permissions) {
                            startActivityForResult(new Intent(mContext, CustomActivity.class), RESULT_PTYE_CAM);
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> permissions) {
                            if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
                                ToastUtil.showLong(mContext, "您没有允许部分权限，请到设置界面开启权限");
                            }
                        }
                    })
                    .start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt("resultType") == 1) {
                    address = bundle.getString("result");
                    transferDialog();
                } else if (bundle.getInt("resultType") == 0) {
                    Toast.makeText(mContext, "解析二维码失败,请尽量保持二维码在正中心", Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == 2) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt("resultType") == 1) {
                    mEditText_address.setText(bundle.getString("result"));
                } else if (bundle.getInt("resultType") == 0) {
                    Toast.makeText(mContext, "解析二维码失败,请尽量保持二维码在正中心", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void myQRCode() {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_qr_layout, null);
        ImageView mImageView_QRCode = inflate.findViewById(R.id.mImageView_QRCode);
        Bitmap bitmap = ZXingUtils.createQRImage(spUtils1.getString("childAddress"), getResources().getDimensionPixelSize(R.dimen.dp_160), getResources().getDimensionPixelSize(R.dimen.dp_160));
        mImageView_QRCode.setImageBitmap(bitmap);

        Dialog dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.1f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (spUtils1.getString("childAddress", "").isEmpty()) {
            refreshLayout.finishRefresh();
            ToastUtil.showLong(mContext, "您还没有添加账户，请先到账户管理中导入或创建账户");
            return;
        }
        getBalance(refreshLayout);
        if (mViewPager.getCurrentItem() == 0) {
            EventBus.getDefault().postSticky(new EventMessageBean(20, new HashMap()));
        } else {
            EventBus.getDefault().postSticky(new EventMessageBean(21, new HashMap()));
        }
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        private Context context;

        public MyViewPagerAdapter(FragmentManager fm, int behavior, Context context) {
            super(fm, behavior);
            this.context = context;
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

    private void hintKeyBoard(View view) {
        InputMethodManager inputMgr = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
