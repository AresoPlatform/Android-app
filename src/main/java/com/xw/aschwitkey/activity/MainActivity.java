package com.xw.aschwitkey.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.LocaleList;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.lihang.ShadowLayout;
import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.happybubble.BubbleLayout;
import com.xujiaji.happybubble.Util;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.adapter.ImageAdapter;
import com.xw.aschwitkey.adapter.TagsAdapter;
import com.xw.aschwitkey.db.SQLUtils;
import com.xw.aschwitkey.db.TronSQLUtils;
import com.xw.aschwitkey.entity.ChatBean;
import com.xw.aschwitkey.entity.DBHelperBean;
import com.xw.aschwitkey.entity.DataBean;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.entity.MessageBean;
import com.xw.aschwitkey.entity.MsgClassBean;
import com.xw.aschwitkey.entity.TagBean;
import com.xw.aschwitkey.entity.TronDBHelperBean;
import com.xw.aschwitkey.fragment.BBSFragment;
import com.xw.aschwitkey.fragment.HomeFragment;
import com.xw.aschwitkey.fragment.InvitationFragment;
import com.xw.aschwitkey.fragment.MyFragment;
import com.xw.aschwitkey.fragment.TradingFragment;
import com.xw.aschwitkey.fragment.WalletFragment;
import com.xw.aschwitkey.fragment.XasWalletFragment;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.service.JWebSocketClientService;
import com.xw.aschwitkey.utils.AESUtil;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.ViewUtils;
import com.xw.aschwitkey.view.CircleImageView;
import com.xw.aschwitkey.view.FullyGridLayoutManager;
import com.xw.aschwitkey.view.NoSrcollViewPage;
import com.youth.banner.Banner;
import com.youth.banner.config.BannerConfig;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mTextView_username)
    TextView mTextView_username;
    @BindView(R.id.mImageView_release)
    ShadowLayout mImageView_release;
    @BindView(R.id.mImageView_audit)
    ShadowLayout mImageView_audit;
    @BindView(R.id.mImageView_fund)
    ShadowLayout mImageView_fund;
    @BindView(R.id.mImageView_application)
    ShadowLayout mImageView_application;
    @BindView(R.id.mImageView_fundRedDot)
    ImageView mImageView_fundRedDot;
    @BindView(R.id.mImageView_redDot)
    ImageView mImageView_redDot;
    @BindView(R.id.ViewPager)
    NoSrcollViewPage ViewPager;
    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.mTextView_text)
    TextView mTextView_text;
    @BindView(R.id.mRelativeLayout_text)
    RelativeLayout mRelativeLayout_text;
    @BindView(R.id.mRelativeLayout_classification)
    RelativeLayout mRelativeLayout_classification;
    @BindView(R.id.mTextView_module)
    TextView mTextView_module;
    @BindView(R.id.mImageView_search)
    ImageView mImageView_search;
    @BindView(R.id.mLottieAnimationView_ranking)
    LottieAnimationView mLottieAnimationView_ranking;
    @BindView(R.id.mFrameLayout_notice)
    FrameLayout mFrameLayout_notice;
    @BindView(R.id.mLottieAnimationView_notice)
    LottieAnimationView mLottieAnimationView_notice;
    @BindView(R.id.mTextView_noticeNumber)
    TextView mTextView_noticeNumber;
    @BindView(R.id.mImageView_prompt)
    ImageView mImageView_prompt;
    @BindView(R.id.mImageView_head)
    CircleImageView mImageView_head;
    @BindView(R.id.mImageView_btn_refresh)
    ShadowLayout mImageView_btn_refresh;
    @BindView(R.id.mImageView_refresh)
    ImageView mImageView_refresh;
    @BindView(R.id.mTextView_refresh)
    TextView mTextView_refresh;

    private Context mContext;
    private SPUtils spUtils, spUtils1;
    private Gson gson;
    private RequestQueue mQueue;
    private long exitTime;
    private MyViewPagerAdapter adapter;
    private List<Fragment> fragments;
    private int[] tabList = new int[]{R.raw.index, R.raw.areso, R.raw.forum, R.raw.recommend, R.raw.user};
    private List<LottieAnimationView> mTabViewList = new ArrayList();
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private ChatMessageReceiver chatMessageReceiver;
    private Dialog dialog, dialog1, promptDialog;
    private boolean isAudit = false, isReward = false;
    private int fundId;
    private List<MessageBean> list;
    private List<TagBean.Result> tagsList;
    private boolean isShow = false, isOk, isBanned = true, isOver = false, isClick = false;
    private MyDialog myDialog;
    private BubbleDialog bubbleDialog;
    private String tag = "";
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        mContext = MainActivity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(MainActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

        gson = new Gson();
        spUtils = new SPUtils(mContext, "AschWallet");
        spUtils1 = new SPUtils(mContext, "AschImport");
        list = new ArrayList<>();
        tagsList = new ArrayList<>();
        mQueue = Volley.newRequestQueue(mContext);
        mLottieAnimationView_ranking.setAnimation(R.raw.top);
        mLottieAnimationView_ranking.setRepeatCount(-1);
        mLottieAnimationView_ranking.playAnimation();
        mLottieAnimationView_notice.setAnimation(R.raw.message);

        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new WalletFragment());
        fragments.add(new BBSFragment());
        fragments.add(new InvitationFragment());
        fragments.add(new MyFragment());
        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mContext);
        ViewPager.setAdapter(adapter);
        ViewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setupWithViewPager(ViewPager);
        ViewPager.setCurrentItem(0);

        ViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    if (!spUtils1.getBoolean("isIntroduction", false)) {
                        introductionDialog();
                        spUtils1.putBoolean("isIntroduction", true);
                    } else {
                        if (spUtils.getString("nickname", "").isEmpty()) {
                            nicknameDialog();
                        }
                    }
                    try {
                        if (spUtils.getInt("bbsStatus", 0) == 1 && isBanned && OtherUtils.dateToStamp(spUtils.getString("estoppelTime")) - (System.currentTimeMillis() + Content.timePoor) > 0) {
                            isBanned = false;
                            bannedDialog();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                showView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (spUtils1.getString("phoneJson", "").isEmpty() || !spUtils1.getString("phoneJson", "").contains(spUtils.getString("phone"))) {
            List<DBHelperBean> beanList = SQLUtils.QuerySQLAll(mContext, "");
            List<TronDBHelperBean> beanList1 = TronSQLUtils.QuerySQLAll(mContext, "");
            boolean isBind = false;
            for (DBHelperBean bean : beanList) {
                if (bean.getState().isEmpty() || !bean.getState().contains("@")) {
                    isBind = true;
                    break;
                }
            }
            for (TronDBHelperBean bean : beanList1) {
                if (bean.getState().isEmpty() || !bean.getState().contains("@")) {
                    isBind = true;
                    break;
                }
            }
            if (isBind) {
                tipsDialog(beanList, beanList1);
            }
        }

        if (spUtils1.getBoolean("isOutLogin", false)) {
            List<DBHelperBean> beanList3 = SQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
            List<TronDBHelperBean> beanList4 = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
            if (beanList3.isEmpty()) {
                spUtils1.putString("childAddress", "");
            } else {
                spUtils1.putString("childAddress", beanList3.get(0).getAddress());
            }
            if (beanList4.isEmpty()) {
                spUtils1.putString("tronChildAddress", "");
            } else {
                spUtils1.putString("tronChildAddress", beanList4.get(0).getAddress());
            }
            spUtils1.putBoolean("isOutLogin", false);
        }

        if (spUtils.getString("nickname", "").isEmpty()) {
            mTextView_username.setText("您好，" + spUtils.getString("phone").substring(7, 11));
        } else {
            mTextView_username.setText("您好，" + spUtils.getString("nickname"));
        }
        if (spUtils1.getBoolean("isPageRefresh", false)) {
            mImageView_refresh.setImageResource(R.mipmap.shuaxin);
            mTextView_refresh.setText("自动刷新");
        } else {
            mImageView_refresh.setImageResource(R.mipmap.shuaxin_no);
            mTextView_refresh.setText("手动刷新");
        }
        setUpTabBadge();

        getServerTime();
        getUserInfo();
        startJWebSClientService();
        bindService();
        doRegisterReceiver();
    }

    private void tipsDialog(List<DBHelperBean> beanList, List<TronDBHelperBean> beanList1) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_tips_layout, null);
        final MyDialog dialog = new MyDialog(mContext, inflate, R.style.DialogTheme);
        inflate.findViewById(R.id.mTextView_false).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                jWebSClientService.sendMsg("-1");
                spUtils.clear();
                finish();
            }
        });
        inflate.findViewById(R.id.mTextView_true).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogAuthentication(beanList, beanList1);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void dialogAuthentication(List<DBHelperBean> beanList, List<TronDBHelperBean> beanList1) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_authentication_layout, null);
        EditText mEditText_verification = v.findViewById(R.id.mEditText_verification);
        TextView mTextView_btn_cancel = v.findViewById(R.id.mTextView_btn_cancel);
        TextView mTextView_confirm = v.findViewById(R.id.mTextView_confirm);
        mTextView_btn_cancel.setVisibility(View.GONE);

        mTextView_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText_verification.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "交易密码不能为空", Toast.LENGTH_SHORT).show();
                }
                hintKeyBoard(v);
                myDialog = DialogUtils.createLoadingDialog(mContext, "验证中...");
                verifyAccount(mEditText_verification.getText().toString(), beanList, beanList1);
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

    private void verifyAccount(String password, List<DBHelperBean> beanList, List<TronDBHelperBean> beanList1) {
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
                DialogUtils.closeDialog(myDialog);
                ToastUtil.showShort(mContext, "网络错误，身份验证失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DialogUtils.closeDialog(myDialog);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                int xasNumber = 0, tronNumber = 0;
                                for (int i = 0; i < beanList.size(); i++) {
                                    try {
                                        AESUtil.decrypt(beanList.get(i).getSecret(), password);
                                        SQLUtils.updatePhoneSql(mContext, spUtils.getString("phone") + "@", beanList.get(i).getAddress());
                                        ++xasNumber;
                                    } catch (Exception e) {
                                        Log.i("cxy", "地址：" + beanList.get(i).getAddress() + "，解密失败：" + e.getMessage(), e);
                                    }
                                }
                                for (int i = 0; i < beanList1.size(); i++) {
                                    try {
                                        AESUtil.decrypt(beanList1.get(i).getSecret(), password);
                                        TronSQLUtils.updatePhoneSql(mContext, spUtils.getString("phone") + "@", beanList1.get(i).getAddress());
                                        ++tronNumber;
                                    } catch (Exception e) {
                                        Log.i("cxy", "地址：" + beanList1.get(i).getAddress() + "，解密失败：" + e.getMessage(), e);
                                    }
                                }
                                JSONArray jsonArray;
                                if (spUtils1.getString("phoneJson", "").isEmpty()) {
                                    jsonArray = new JSONArray();
                                } else {
                                    jsonArray = new JSONArray(spUtils1.getString("phoneJson", ""));
                                }
                                jsonArray.put(spUtils.getString("phone"));
                                spUtils1.putString("phoneJson", jsonArray.toString());
                                dialog.dismiss();
                                List<DBHelperBean> beanList3 = SQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                                List<TronDBHelperBean> beanList4 = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                                if (beanList3.isEmpty()) {
                                    spUtils1.putString("childAddress", "");
                                } else {
                                    spUtils1.putString("childAddress", beanList3.get(0).getAddress());
                                }
                                if (beanList4.isEmpty()) {
                                    spUtils1.putString("tronChildAddress", "");
                                } else {
                                    spUtils1.putString("tronChildAddress", beanList4.get(0).getAddress());
                                }
                                ToastUtil.showLong(mContext, "绑定成功，当前账户已绑定" + xasNumber + "个XAS账户，" + tronNumber + "个TRON账户");
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

    private void setUpTabBadge() {
        for (int i = 0; i < fragments.size(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }
            tab.setCustomView(adapter.getTabView(i));
            View tabview = (View) tab.getCustomView().getParent();
            tabview.setTag(i);
        }
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);
    }

    private void getServerTime() {
        JsonObjectRequest request = new JsonObjectRequest(Http.serverTime, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 1) {
                        Long serverTime = response.getJSONObject("data").getLong("serverTime");
                        Content.timePoor = serverTime - System.currentTimeMillis();
                    } else {
                    }
                } catch (Exception e) {
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(request);
    }

    private void getUserInfo() {
        OkHttpClient.initGet(Http.accountInfo).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(mContext, "网络异常，账号信息获取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject;
                        JSONObject object;
                        try {
                            jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                object = jsonObject.getJSONObject("data");
                                if (object.getBoolean("isCouncil") && tabLayout.getSelectedTabPosition() != 1) {
                                    mImageView_release.setVisibility(View.VISIBLE);
                                    mImageView_audit.setVisibility(View.VISIBLE);
                                } else {
                                    mImageView_release.setVisibility(View.GONE);
                                    mImageView_audit.setVisibility(View.GONE);
                                }
                                if (object.getString("nickName").isEmpty()) {
                                    mTextView_username.setText("您好，" + spUtils.getString("phone").substring(7, 11));
                                } else {
                                    mTextView_username.setText("您好，" + object.getString("nickName"));
                                }
                                spUtils.putInt("bbsStatus", object.getInt("bbsStatus"));
                                spUtils.putString("estoppelTime", object.getString("estoppelTime"));
                                spUtils.putString("nickname", object.getString("nickName"));
                                spUtils.putString("headImage", object.getString("headImage"));
                                spUtils.putString("address", object.getString("address"));
                                spUtils.putString("accountInvitationCode", object.getString("accountInvitationCode"));
                                spUtils.putInt("invitationAmount", object.getInt("invitationAmount"));
                                spUtils.putString("invitationAwardAmount", new BigDecimal(object.getDouble("invitationAwardAmount")).setScale(4, BigDecimal.ROUND_DOWN).toString());
                                spUtils.putBoolean("isCouncil", object.getBoolean("isCouncil"));
                                spUtils.putBoolean("isDeal", object.getBoolean("isDeal"));
                                Glide.with(mContext)
                                        .load(object.getString("headImage"))
                                        .apply(new RequestOptions()
                                                .placeholder(R.mipmap.default_icon)
                                                .skipMemoryCache(true)
                                                .dontAnimate())
                                        .into(mImageView_head);
                            } else if (jsonObject.getInt("code") == 3) {
                                overdueLogin();
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
    }

    private void showView(int position) {
        if (position == 0) {
            if (isShow) {
                mTextView_username.setVisibility(View.GONE);
                mTextView_text.setSelected(true);
                mRelativeLayout_text.setVisibility(View.VISIBLE);
                isShow = false;
            } else {
                mTextView_username.setVisibility(View.VISIBLE);
            }
            mRelativeLayout_classification.setVisibility(View.GONE);
            mImageView_search.setVisibility(View.GONE);
            mLottieAnimationView_ranking.setVisibility(View.GONE);
            mFrameLayout_notice.setVisibility(View.GONE);
            mImageView_prompt.setVisibility(View.GONE);
            mImageView_head.setVisibility(View.GONE);
            mTextView_username.setVisibility(View.VISIBLE);
            mImageView_fund.setVisibility(View.GONE);
            mImageView_btn_refresh.setVisibility(View.GONE);
            mImageView_application.setVisibility(View.GONE);
            if (spUtils.getBoolean("isCouncil", false)) {
                mImageView_release.setVisibility(View.VISIBLE);
                mImageView_audit.setVisibility(View.VISIBLE);
            } else {
                mImageView_release.setVisibility(View.GONE);
                mImageView_audit.setVisibility(View.GONE);
            }
            mTabViewList.get(0).playAnimation();
            mTabViewList.get(1).cancelAnimation();
            mTabViewList.get(2).cancelAnimation();
            mTabViewList.get(3).cancelAnimation();
            mTabViewList.get(4).cancelAnimation();
            mTabViewList.get(1).setProgress(0f);
            mTabViewList.get(2).setProgress(0f);
            mTabViewList.get(3).setProgress(0f);
            mTabViewList.get(4).setProgress(0f);
        } else if (position == 1) {
            if (isShow) {
                mTextView_username.setVisibility(View.GONE);
                mTextView_text.setSelected(true);
                mRelativeLayout_text.setVisibility(View.VISIBLE);
                isShow = false;
            } else {
                mTextView_username.setVisibility(View.VISIBLE);
            }
            mRelativeLayout_classification.setVisibility(View.GONE);
            mImageView_search.setVisibility(View.GONE);
            mLottieAnimationView_ranking.setVisibility(View.GONE);
            mFrameLayout_notice.setVisibility(View.GONE);
            mImageView_prompt.setVisibility(View.GONE);
            mImageView_head.setVisibility(View.GONE);
            mTextView_username.setVisibility(View.VISIBLE);
            mImageView_fund.setVisibility(View.GONE);
            mImageView_btn_refresh.setVisibility(View.VISIBLE);
            mImageView_application.setVisibility(View.VISIBLE);
            mImageView_release.setVisibility(View.GONE);
            mImageView_audit.setVisibility(View.GONE);
            mTabViewList.get(1).playAnimation();
            mTabViewList.get(0).cancelAnimation();
            mTabViewList.get(2).cancelAnimation();
            mTabViewList.get(3).cancelAnimation();
            mTabViewList.get(4).cancelAnimation();
            mTabViewList.get(0).setProgress(0f);
            mTabViewList.get(2).setProgress(0f);
            mTabViewList.get(3).setProgress(0f);
            mTabViewList.get(4).setProgress(0f);
        } else if (position == 2) {
            mTextView_text.setSelected(false);
            mRelativeLayout_text.setVisibility(View.GONE);

            mRelativeLayout_classification.setVisibility(View.VISIBLE);
            mImageView_search.setVisibility(View.VISIBLE);
            mLottieAnimationView_ranking.setVisibility(View.VISIBLE);
            mFrameLayout_notice.setVisibility(View.VISIBLE);
            mImageView_prompt.setVisibility(View.VISIBLE);
            mImageView_head.setVisibility(View.VISIBLE);
            mTextView_username.setVisibility(View.GONE);
            mImageView_fund.setVisibility(View.GONE);
            mImageView_release.setVisibility(View.GONE);
            mImageView_audit.setVisibility(View.GONE);
            mImageView_btn_refresh.setVisibility(View.GONE);
            mImageView_application.setVisibility(View.GONE);
            mTabViewList.get(2).playAnimation();
            mTabViewList.get(0).cancelAnimation();
            mTabViewList.get(1).cancelAnimation();
            mTabViewList.get(3).cancelAnimation();
            mTabViewList.get(4).cancelAnimation();
            mTabViewList.get(0).setProgress(0f);
            mTabViewList.get(1).setProgress(0f);
            mTabViewList.get(3).setProgress(0f);
            mTabViewList.get(4).setProgress(0f);
        } else if (position == 3) {
            if (isShow) {
                mTextView_username.setVisibility(View.GONE);
                mTextView_text.setSelected(true);
                mRelativeLayout_text.setVisibility(View.VISIBLE);
                isShow = false;
            } else {
                mTextView_username.setVisibility(View.VISIBLE);
            }
            mRelativeLayout_classification.setVisibility(View.GONE);
            mImageView_search.setVisibility(View.GONE);
            mLottieAnimationView_ranking.setVisibility(View.GONE);
            mFrameLayout_notice.setVisibility(View.GONE);
            mImageView_prompt.setVisibility(View.GONE);
            mImageView_head.setVisibility(View.GONE);
            mTextView_username.setVisibility(View.VISIBLE);
            mImageView_fund.setVisibility(View.GONE);
            mImageView_btn_refresh.setVisibility(View.GONE);
            mImageView_application.setVisibility(View.GONE);
            if (spUtils.getBoolean("isCouncil", false)) {
                mImageView_release.setVisibility(View.VISIBLE);
                mImageView_audit.setVisibility(View.VISIBLE);
            } else {
                mImageView_release.setVisibility(View.GONE);
                mImageView_audit.setVisibility(View.GONE);
            }
            mTabViewList.get(3).playAnimation();
            mTabViewList.get(0).cancelAnimation();
            mTabViewList.get(1).cancelAnimation();
            mTabViewList.get(2).cancelAnimation();
            mTabViewList.get(4).cancelAnimation();
            mTabViewList.get(0).setProgress(0f);
            mTabViewList.get(1).setProgress(0f);
            mTabViewList.get(2).setProgress(0f);
            mTabViewList.get(4).setProgress(0f);
        } else if (position == 4) {
            if (isShow) {
                mTextView_username.setVisibility(View.GONE);
                mTextView_text.setSelected(true);
                mRelativeLayout_text.setVisibility(View.VISIBLE);
                isShow = false;
            } else {
                mTextView_username.setVisibility(View.VISIBLE);
            }
            mRelativeLayout_classification.setVisibility(View.GONE);
            mImageView_search.setVisibility(View.GONE);
            mLottieAnimationView_ranking.setVisibility(View.GONE);
            mFrameLayout_notice.setVisibility(View.GONE);
            mImageView_prompt.setVisibility(View.GONE);
            mImageView_head.setVisibility(View.GONE);
            mImageView_fund.setVisibility(View.GONE);
            mImageView_btn_refresh.setVisibility(View.GONE);
            mImageView_application.setVisibility(View.GONE);
            if (spUtils.getBoolean("isCouncil", false)) {
                mImageView_release.setVisibility(View.VISIBLE);
                mImageView_audit.setVisibility(View.VISIBLE);
            } else {
                mImageView_release.setVisibility(View.GONE);
                mImageView_audit.setVisibility(View.GONE);
            }
            mTabViewList.get(4).playAnimation();
            mTabViewList.get(0).cancelAnimation();
            mTabViewList.get(1).cancelAnimation();
            mTabViewList.get(2).cancelAnimation();
            mTabViewList.get(3).cancelAnimation();
            mTabViewList.get(0).setProgress(0f);
            mTabViewList.get(1).setProgress(0f);
            mTabViewList.get(2).setProgress(0f);
            mTabViewList.get(3).setProgress(0f);
        }
    }

    private void promptDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_prompt_layout, null);
        v.findViewById(R.id.mLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog.dismiss();
                isClick = false;
            }
        });
        promptDialog = new Dialog(mContext, R.style.DialogStyle);
        promptDialog.setContentView(v);
        Window dialogWindow = promptDialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.3f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        promptDialog.show();
    }

    private void introductionDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_introduction_layout, null);
        TextView mTextView_one = v.findViewById(R.id.mTextView_one);
        TextView mTextView_two = v.findViewById(R.id.mTextView_two);
        mTextView_one.setText("① 本论坛是提供广大希客用户进行沟通、交流的网络社区，论坛讨论不得涉及政治、色情、暴力、迷信等内容。\n② 会员参与本论坛讨论必须遵守中华人民共和国相应法律法规，并由会员单独承担其所发表言论的责任。");
        mTextView_two.setText("① 违反中华人民共和国宪法和法律法规的言论。\n② 攻击中华人民共和国政府、中国共产党及其领导人的言论。\n③ 侮辱、中伤、恐吓他人的言论以及宣扬暴力、迷信和色情淫秽的言论。\n④ 泄露国家机密的言论和教唆犯罪的言论、技术资料。\n⑤ 其他有违国家法律法规的内容。\n⑥ 任何会员一经发现在论坛发表上述言论的，将被永久性取消会员资格。上述规则若与中华人民共和国现行法律法规不一致，以中华人民共和国现行法律法规为准。");
        TextView mTextView_close = v.findViewById(R.id.mTextView_close);
        final MyDialog dialog = new MyDialog(mContext, v, R.style.DialogTheme);
        mTextView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                promptDialog();
                if (spUtils.getString("nickname", "").isEmpty()) {
                    nicknameDialog();
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        countDownTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTextView_close.setEnabled(false);
                mTextView_close.setText((Math.round((double) millisUntilFinished / 1000) - 1) + "秒 后 即可关闭");
            }

            @Override
            public void onFinish() {
                mTextView_close.setEnabled(true);
                mTextView_close.setText("关闭");
            }
        }.start();
    }

    private void nicknameDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_nickname_layout, null);
        EditText mEditText_nickname = v.findViewById(R.id.mEditText_nickname);

        mEditText_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nickname = mEditText_nickname.getText().toString();
                String reg = "^[a-z]+[0-9a-z-]{2,15}$";
                if (nickname.matches(reg)) {
                    isOk = true;
                    mEditText_nickname.setTextColor(getResources().getColor(R.color.text_bg));
                } else {
                    mEditText_nickname.setTextColor(getResources().getColor(R.color.text_color_red));
                    isOk = false;
                    String regEx = "[^a-z0-9-]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(nickname);
                    String str = m.replaceAll("").trim();
                    if (!nickname.equals(str)) {
                        mEditText_nickname.setText(str);
                        mEditText_nickname.setSelection(str.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        v.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager.setCurrentItem(0);
                dialog1.dismiss();
                if (promptDialog != null) {
                    promptDialog.dismiss();
                }
            }
        });
        v.findViewById(R.id.mTextView_determine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText_nickname.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "昵称不能为空");
                    return;
                }
                if (!isOk) {
                    return;
                }
                myDialog = DialogUtils.createLoadingDialog(mContext, "提交中...");
                OkHttpClient.initGet(Http.setNickName + mEditText_nickname.getText().toString()).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogUtils.closeDialog(myDialog);
                                ToastUtil.showShort(mContext, "网络错误，昵称设置失败");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!spUtils.getString("address").isEmpty()) {
                                        DialogUtils.closeDialog(myDialog);
                                    }
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getInt("code") == 1) {
                                        spUtils.putString("nickname", mEditText_nickname.getText().toString());
                                        mTextView_username.setText("您好，" + mEditText_nickname.getText().toString());
                                        EventBus.getDefault().postSticky(new EventMessageBean(10, null));
                                        ToastUtil.showShort(mContext, "昵称设置成功");
                                        if (spUtils.getString("address").isEmpty()) {
                                            getRecharge();
                                        } else {
                                            dialog1.dismiss();
                                        }
                                    } else {
                                        DialogUtils.closeDialog(myDialog);
                                        ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                                    }
                                } catch (JSONException e) {
                                    DialogUtils.closeDialog(myDialog);
                                }
                            }
                        });
                    }
                });
            }
        });

        dialog1 = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog1.setContentView(v);
        dialog1.setCancelable(false);
        Window dialogWindow = dialog1.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.1f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog1.show();
    }

    private void getRecharge() {
        myDialog.setMsg("获取交易账户....");
        OkHttpClient.initGet(Http.recharge).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，获取交易账户失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DialogUtils.closeDialog(myDialog);
                            dialog1.dismiss();
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                JSONObject object = jsonObject.getJSONObject("data");
                                spUtils.putString("address", object.getString("address"));
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

    private void bannedDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_banned_layout, null);
        TextView mTextView_text = v.findViewById(R.id.mTextView_text);
        mTextView_text.setText("禁言截止时间：" + spUtils.getString("estoppelTime"));
        final MyDialog dialog = new MyDialog(mContext, v, R.style.DialogTheme);
        v.findViewById(R.id.mTextView_true).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    @OnClick({R.id.mImageView_release, R.id.mImageView_audit, R.id.mImageView_fund, R.id.mImageView_application, R.id.mRelativeLayout_text, R.id.mLottieAnimationView_ranking, R.id.mImageView_head, R.id.mFrameLayout_notice, R.id.mRelativeLayout_classification, R.id.mImageView_search, R.id.mImageView_prompt, R.id.mImageView_btn_refresh})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mImageView_release:
                View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_item_layout, null);
                TextView mTextView_announcement = inflate.findViewById(R.id.mTextView_announcement);
                TextView mTextView_fund = inflate.findViewById(R.id.mTextView_fund);

                mTextView_announcement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intentAn = new Intent(mContext, AnnouncementActivity.class);
                        startActivity(intentAn);
                    }
                });

                mTextView_fund.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intentFr = new Intent(mContext, FundReleaseActivity.class);
                        startActivity(intentFr);
                    }
                });

                dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
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
                break;
            case R.id.mImageView_audit:
                Intent intentIa = new Intent(mContext, IssuedAuditActivity.class);
                intentIa.putExtra("isAudit", isAudit);
                startActivity(intentIa);
                break;
            case R.id.mImageView_fund:
                Intent intentFd = new Intent(mContext, FundDividendsActivity.class);
                intentFd.putExtra("isReward", isReward);
                startActivity(intentFd);
                break;
            case R.id.mImageView_application:
                Intent intentMp = new Intent(mContext, MoreApplicationsActivity.class);
                startActivity(intentMp);
                break;
            case R.id.mRelativeLayout_text:
                Intent intentRh = new Intent(mContext, RewardHistoryActivity.class);
                intentRh.putExtra("fundId", fundId);
                startActivity(intentRh);
                break;
            case R.id.mLottieAnimationView_ranking:
                Intent intentR = new Intent(mContext, RankingActivity.class);
                startActivity(intentR);
                break;
            case R.id.mImageView_head:
                Intent intentUi = new Intent(mContext, UserInfoActivity.class);
                startActivity(intentUi);
                break;
            case R.id.mFrameLayout_notice:
                Intent intentN = new Intent(mContext, MessageActivity.class);
                startActivity(intentN);
                break;
            case R.id.mRelativeLayout_classification:
                if (tagsList.isEmpty()) {
                    ToastUtil.showShort(mContext, "暂未获取到板块分类列表，请稍候或刷新再试");
                    return;
                }
                showClassDialog();
                break;
            case R.id.mImageView_search:
                showSearch();
                break;
            case R.id.mImageView_prompt:
                if (!isClick) {
                    promptDialog();
                } else {
                    isClick = true;
                }
                break;
            case R.id.mImageView_btn_refresh:
                spUtils1.putBoolean("isPageRefresh", !spUtils1.getBoolean("isPageRefresh", false));
                if (spUtils1.getBoolean("isPageRefresh", false)) {
                    mImageView_refresh.setImageResource(R.mipmap.shuaxin);
                    mTextView_refresh.setText("自动刷新");
                } else {
                    mImageView_refresh.setImageResource(R.mipmap.shuaxin_no);
                    mTextView_refresh.setText("手动刷新");
                }
                break;
        }
    }

    private void showClassDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_class_layout, null);
        RecyclerView mRecyclerView_class = view.findViewById(R.id.mRecyclerView_class);
        TagsAdapter tagsAdapter = new TagsAdapter(mContext, tagsList);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView_class.setLayoutManager(manager);
        mRecyclerView_class.setAdapter(tagsAdapter);
        tagsAdapter.setOnClickListenerFace(new TagsAdapter.OnClickListenerFace() {
            @Override
            public void OnClickTemp(int p, View view) {
                for (int i = 0; i < tagsList.size(); i++) {
                    if (i == p) {
                        tagsList.get(i).setSelect(true);
                    } else {
                        tagsList.get(i).setSelect(false);
                    }
                }
                tagsAdapter.notifyDataSetChanged();
                tag = tagsList.get(p).getEnglish();
                mTextView_module.setText(tagsList.get(p).getChinese());
                Map map = new HashMap();
                map.put("tag", tag);
                EventBus.getDefault().postSticky(new EventMessageBean(18, map));
                bubbleDialog.dismiss();
            }
        });
        BubbleLayout bl = new BubbleLayout(mContext);
        bl.setBubbleColor(mContext.getResources().getColor(R.color.white));
        bl.setShadowColor(mContext.getResources().getColor(R.color.white));
        bl.setLookLength(0);
        bl.setLookWidth(0);
        bl.setBubbleRadius(0);
        bubbleDialog = new BubbleDialog(mContext)
                .setBubbleContentView(view)
                .setClickedView(mRelativeLayout_classification)
                .setPosition(BubbleDialog.Position.BOTTOM)
                .setTransParentBackground()
                .setOffsetY(2)
                .setBubbleLayout(bl)
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 0);
        bubbleDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessageBean messageEvent) {
        switch (messageEvent.getTag()) {
            case -1:
                jWebSClientService.sendMsg("-1");
                spUtils.clear();
                Intent ouLogin = new Intent(mContext, LoginActivity.class);
                startActivity(ouLogin);
                finish();
                break;
            case -2:
                overdueLogin();
                break;
            case 3:
                isAudit = false;
                mImageView_redDot.setVisibility(View.GONE);
                break;
            case 10:
                mTextView_username.setText("您好，" + spUtils.getString("nickname"));
                break;
            case 11:
                isReward = false;
                mImageView_fundRedDot.setVisibility(View.GONE);
                break;
            case 12:
                mTextView_username.setVisibility(View.VISIBLE);
                mTextView_text.setSelected(false);
                mRelativeLayout_text.setVisibility(View.GONE);
                break;
            case 13:
                if (messageEvent.getMesssage() != null) {
                    Glide.with(mContext)
                            .load(messageEvent.getMesssage().get("img").toString())
                            .apply(new RequestOptions()
                                    .placeholder(R.mipmap.default_icon)
                                    .skipMemoryCache(true)
                                    .dontAnimate())
                            .into(mImageView_head);
                }
                break;
            case 15:
                boolean isThere = false;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getSteemName().equals(messageEvent.getMesssage().get("hisName").toString())) {
                        isThere = true;
                        MessageBean bean = list.get(i);
                        bean.setContent(messageEvent.getMesssage().get("content").toString());
                        bean.setSendTime(messageEvent.getMesssage().get("time").toString());
                        bean.setCount(0);
                        list.set(i, bean);
                    }
                }
                if (!isThere) {
                    MessageBean bean = new MessageBean();
                    bean.setContent(messageEvent.getMesssage().get("content").toString());
                    bean.setSteemName(messageEvent.getMesssage().get("hisName").toString());
                    bean.setCount(0);
                    bean.setSendTime(messageEvent.getMesssage().get("time").toString());
                    bean.setHeadImage(messageEvent.getMesssage().get("hisImage").toString());
                    list.add(bean);
                }
                if (!Content.list.isEmpty()) {
                    Content.list.clear();
                }
                Content.list.addAll(list);
                break;
            case 16:
                if ((Integer.parseInt(mTextView_noticeNumber.getTag().toString()) == 0 || (Integer.parseInt(mTextView_noticeNumber.getTag().toString()) - (int) messageEvent.getMesssage().get("noReadNum")) <= 0)) {
                    if (Content.msgClassBean.getMsgClass() != null) {
                        for (int i = 0; i < Content.msgClassBean.getMsgClass().size(); i++) {
                            MsgClassBean.ResultBean bean = Content.msgClassBean.getMsgClass().get(i);
                            bean.setCount(0);
                            Content.msgClassBean.getMsgClass().set(i, bean);
                        }
                    }
                    mTextView_noticeNumber.setText("0");
                    mTextView_noticeNumber.setVisibility(View.GONE);
                    mTextView_noticeNumber.setTag("0");
                } else {
                    mTextView_noticeNumber.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(mTextView_noticeNumber.getTag().toString()) - (int) messageEvent.getMesssage().get("noReadNum") > 99) {
                        mTextView_noticeNumber.setText("99+");
                    } else {
                        mTextView_noticeNumber.setText((Integer.parseInt(mTextView_noticeNumber.getTag().toString()) - (int) messageEvent.getMesssage().get("noReadNum")) + "");
                    }
                    mTextView_noticeNumber.setTag((Integer.parseInt(mTextView_noticeNumber.getTag().toString()) - (int) messageEvent.getMesssage().get("noReadNum")) + "");
                }
                break;
            case 17:
                TagBean tagBean = gson.fromJson(messageEvent.getMesssage().get("tags").toString(), TagBean.class);
                if (!tagsList.isEmpty()) {
                    tagsList.clear();
                }
                TagBean.Result bean;
                for (int i = 0; i < tagBean.getData().size(); i++) {
                    bean = tagBean.getData().get(i);
                    if (i == 0) {
                        bean.setSelect(true);
                    } else {
                        bean.setSelect(false);
                    }
                    tagsList.add(bean);
                }
                break;
            case 25:
                ViewPager.setCurrentItem(1);
                break;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    private void bindService() {
        Intent bindIntent = new Intent(mContext, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void startJWebSClientService() {
        Intent intent = new Intent(mContext, JWebSocketClientService.class);
        startService(intent);
    }

    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xw.aschWitkey.serviceCallback.content");
        registerReceiver(chatMessageReceiver, filter);
    }

    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            if (null != message && !message.isEmpty()) {
                try {
                    JSONObject jsonType = new JSONObject(message);
                    if (message.contains("\"message_type\":4")) {
                        return;
                    }
                    if (jsonType.getInt("publish_type") == 3) {
                        jWebSClientService.sendMsg("-1");
                        spUtils.clear();
                        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_offline_layout, null);
                        final MyDialog dialog = new MyDialog(mContext, inflate, R.style.DialogTheme);
                        inflate.findViewById(R.id.mTextView_false).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                        inflate.findViewById(R.id.mTextView_true).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.show();
                    } else if (jsonType.getInt("publish_type") == 1) {
                        if (spUtils.getInt("noticeId", -1) != jsonType.getJSONObject("msg_body").getInt("id")) {
                            spUtils.putInt("noticeId", jsonType.getJSONObject("msg_body").getInt("id"));
                            EventBus.getDefault().postSticky(new EventMessageBean(8, null));
                            anDialog(jsonType);
                        }
                    } else if (jsonType.getInt("publish_type") == 2) {
                        anDialog(jsonType);
                    } else if (jsonType.getInt("publish_type") == 4) {
                        isAudit = true;
                        mImageView_redDot.setVisibility(View.VISIBLE);
                    } else if (jsonType.getInt("publish_type") == 5) {
                        fundId = jsonType.getJSONArray("msg_body").getJSONObject(0).getInt("fundId");
                        if (tabLayout.getSelectedTabPosition() != 0) {
                            mTextView_username.setVisibility(View.GONE);
                            mTextView_text.setSelected(true);
                            mRelativeLayout_text.setVisibility(View.VISIBLE);
                        } else {
                            isShow = true;
                        }
                    } else if (jsonType.getInt("publish_type") == 6) {
                        isReward = true;
                        mImageView_fundRedDot.setVisibility(View.VISIBLE);
                    } else if (jsonType.getInt("publish_type") == 7 || jsonType.getInt("publish_type") == 9 || jsonType.getInt("publish_type") == 10 || jsonType.getInt("publish_type") == 11) {
                        mLottieAnimationView_notice.playAnimation();
                        ChatBean.Chat cb = gson.fromJson(jsonType.getString("msg_body"), ChatBean.Chat.class);
                        Map map = new HashMap();
                        if (cb.getType() == 4) {
                            boolean isThere = false;
                            int position = -1;
                            for (int i = 0; i < Content.list.size(); i++) {
                                if (cb.getSendUser().equals(Content.list.get(i).getSteemName())) {
                                    isThere = true;
                                    MessageBean mb = Content.list.get(i);
                                    mb.setContent(gson.toJson(cb.getMsg()));
                                    mb.setCount(Content.list.get(i).getCount() + 1);
                                    mb.setSendTime(cb.getSendTime());
                                    Content.list.set(i, mb);
                                    position = i;
                                }
                            }
                            if (!isThere) {
                                MessageBean mb = new MessageBean();
                                mb.setContent(gson.toJson(cb.getMsg()));
                                mb.setCount(1);
                                mb.setSendTime(cb.getSendTime());
                                mb.setSteemName(cb.getSendUser());
                                mb.setHeadImage(cb.getSendHeadImage());
                                Content.list.add(mb);
                            }
                            map.put("position", position);
                        }
                        MsgClassBean.ResultBean resultBean;
                        if (cb.getType() == 1) {
                            resultBean = Content.msgClassBean.getMsgClass().get(3);
                            resultBean.setCount(resultBean.getCount() + 1);
                            Content.msgClassBean.getMsgClass().set(3, resultBean);
                        }
                        if (cb.getType() == 2) {
                            resultBean = Content.msgClassBean.getMsgClass().get(2);
                            resultBean.setCount(resultBean.getCount() + 1);
                            Content.msgClassBean.getMsgClass().set(2, resultBean);
                        }

                        if (cb.getType() == 3) {
                            resultBean = Content.msgClassBean.getMsgClass().get(1);
                            resultBean.setCount(resultBean.getCount() + 1);
                            Content.msgClassBean.getMsgClass().set(1, resultBean);
                        }

                        mTextView_noticeNumber.setVisibility(View.VISIBLE);
                        if ((Integer.parseInt(mTextView_noticeNumber.getTag().toString()) + 1) > 99) {
                            mTextView_noticeNumber.setText("99+");
                        } else {
                            mTextView_noticeNumber.setText((Integer.parseInt(mTextView_noticeNumber.getTag().toString()) + 1) + "");
                        }
                        mTextView_noticeNumber.setTag((Integer.parseInt(mTextView_noticeNumber.getTag().toString()) + 1) + "");

                        map.put("msg", jsonType.getString("msg_body"));
                        map.put("type", cb.getType());
                        EventBus.getDefault().postSticky(new EventMessageBean(14, map));
                    } else if (jsonType.getInt("publish_type") == 8) {
                        MsgClassBean bean = gson.fromJson(jsonType.getJSONObject("msg_body").toString(), MsgClassBean.class);
                        Content.msgClassBean = bean;
                        JSONArray jsonArray = jsonType.getJSONObject("msg_body").getJSONArray("msgClass").getJSONObject(0).getJSONArray("object");
                        MessageBean messageBean;
                        if (!list.isEmpty()) {
                            list.clear();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                messageBean = gson.fromJson(jsonArray.getString(i), MessageBean.class);
                                list.add(messageBean);
                            } catch (JSONException e) {
                            }
                        }
                        if (!Content.list.isEmpty()) {
                            Content.list.clear();
                        }
                        Content.list.addAll(list);

                        if (jsonType.getJSONObject("msg_body").getInt("totalCount") != 0) {
                            mTextView_noticeNumber.setVisibility(View.VISIBLE);
                            mLottieAnimationView_notice.playAnimation();
                        } else {
                            mTextView_noticeNumber.setVisibility(View.GONE);
                        }
                        if (jsonType.getJSONObject("msg_body").getInt("totalCount") > 99) {
                            mTextView_noticeNumber.setText("99+");
                        } else {
                            mTextView_noticeNumber.setText(jsonType.getJSONObject("msg_body").getString("totalCount"));
                        }
                        mTextView_noticeNumber.setTag(jsonType.getJSONObject("msg_body").getString("totalCount"));
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private void showSearch() {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_search_layout, null);
        EditText mEditText_search = dialogView.findViewById(R.id.mEditText_search);
        TextView mTextView_search = dialogView.findViewById(R.id.mTextView_search);
        mEditText_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String nickName = mEditText_search.getText().toString();
                    if (nickName.trim().isEmpty()) {
                        ToastUtil.showShort(mContext, "搜索用户不能为空");
                        return false;
                    }
                    if (nickName.equals(spUtils.getString("nickname"))) {
                        ToastUtil.showShort(mContext, "您不能搜索自己");
                        return false;
                    }
                    getUser(nickName);
                    return true;
                }
                return false;
            }
        });
        mTextView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = mEditText_search.getText().toString();
                if (nickName.trim().isEmpty()) {
                    ToastUtil.showShort(mContext, "搜索用户不能为空");
                    return;
                }
                if (nickName.equals(spUtils.getString("nickname"))) {
                    ToastUtil.showShort(mContext, "您不能搜索自己");
                    return;
                }
                getUser(nickName);
            }
        });
        BubbleLayout bl = new BubbleLayout(mContext);
        bl.setBubbleColor(mContext.getResources().getColor(R.color.white));
        bl.setShadowColor(Color.TRANSPARENT);
        bl.setLookLength(0);
        bl.setLookWidth(0);
        bl.setBubbleRadius(Util.dpToPx(mContext, 3));
        BubbleDialog bubbleDialog = new BubbleDialog(mContext)
                .setClickedView(mImageView_search)
                .setBubbleContentView(dialogView)
                .setPosition(BubbleDialog.Position.BOTTOM)
                .setOffsetY(-8)
                .setBubbleLayout(bl)
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 0);
        bubbleDialog.show();
    }

    private void getUser(String nickName) {
        myDialog = DialogUtils.createLoadingDialog(mContext, "搜索中...");
        OkHttpClient.initGet(Http.checkName + "?steemName=" + nickName).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，搜索用户失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DialogUtils.closeDialog(myDialog);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                if (jsonObject.getBoolean("data")) {
                                    Intent intent = new Intent(mContext, HisActivity.class);
                                    intent.putExtra("hisName", nickName);
                                    startActivity(intent);
                                } else {
                                    ToastUtil.showShort(mContext, "用户不存在，请重新输入");
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

    private void overdueLogin() {
        if (!isOver) {
            isOver = true;
            if (jWebSClientService != null) {
                jWebSClientService.sendMsg("-1");
            }
            spUtils.clear();
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_overdue_layout, null);
            final MyDialog dialog = new MyDialog(mContext, inflate, R.style.DialogTheme);
            inflate.findViewById(R.id.mTextView_true).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    private void anDialog(JSONObject jsonType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_announcement_layout, null);
        final MyDialog dialog = new MyDialog(mContext, inflate, R.style.DialogTheme);
        TextView mTextView_title = inflate.findViewById(R.id.mTextView_title);
        ImageView mImageView_cover = inflate.findViewById(R.id.mImageView_cover);
        try {
            JSONObject jsonObject = jsonType.getJSONObject("msg_body");
            mTextView_title.setText(jsonObject.getString("title"));
            Glide.with(mContext)
                    .load(jsonObject.getString("coverImg"))
                    .into(mImageView_cover);
            inflate.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            inflate.findViewById(R.id.mTextView_btn_determine).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    try {
                        if (jsonType.getInt("publish_type") == 1) {
                            Intent intent = new Intent(mContext, AnDetailsActivity.class);
                            intent.putExtra("title", jsonObject.getString("title"));
                            intent.putExtra("content", jsonObject.getString("content"));
                            intent.putExtra("img", jsonObject.getString("coverImg"));
                            intent.putExtra("time", jsonObject.getString("reviewEndTimea"));
                            startActivity(intent);
                        } else if (jsonType.getInt("publish_type") == 2) {
                            Intent intent = new Intent(mContext, AnAuditDetailsActivity.class);
                            intent.putExtra("noticeId", jsonObject.getInt("id"));
                            intent.putExtra("title", jsonObject.getString("title"));
                            intent.putExtra("content", jsonObject.getString("content"));
                            intent.putExtra("img", jsonObject.getString("coverImg"));
                            intent.putExtra("participants", jsonObject.getInt("participants"));
                            intent.putExtra("yes", jsonObject.getInt("yes"));
                            intent.putExtra("no", jsonObject.getInt("no"));
                            intent.putExtra("isShow", jsonObject.getBoolean("isShow"));
                            intent.putExtra("reviewStatus", jsonObject.getInt("reviewStatus"));
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                    }
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTabViewList.get(tabLayout.getSelectedTabPosition()).setProgress(1f);
        showView(tabLayout.getSelectedTabPosition());
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

        public View getTabView(int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.tab_content_layout, null);
            LottieAnimationView mAnimationView = view.findViewById(R.id.mAnimationView);
            mAnimationView.setAnimation(tabList[position]);
            mTabViewList.add(mAnimationView);
            return view;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        unregisterReceiver(chatMessageReceiver);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 3000) {
            Toast toast = Toast.makeText(MainActivity.this, null, Toast.LENGTH_LONG);
            toast.setText("再按一次退出应用");
            toast.show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    private void hintKeyBoard(View view) {
        InputMethodManager inputMgr = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
