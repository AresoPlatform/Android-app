package com.xw.aschwitkey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.happybubble.BubbleLayout;
import com.xujiaji.happybubble.Util;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.adapter.AccountAdapter;
import com.xw.aschwitkey.adapter.UnFreezeAdapter;
import com.xw.aschwitkey.db.TronSQLUtils;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.entity.MessageBean;
import com.xw.aschwitkey.entity.TronDBHelperBean;
import com.xw.aschwitkey.entity.UnfreezeBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.AESUtil;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.ViewUtils;
import com.xw.aschwitkey.view.MyClassicsHeader;
import com.xw.aschwitkey.view.RecyclerViewNoBugLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TronResourceActivity extends AppCompatActivity implements OnRefreshListener, UnFreezeAdapter.OnClickListenerFace {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mImageView_arrowEnergy)
    ImageView mImageView_arrowEnergy;
    @BindView(R.id.mSeekBar_energy)
    SeekBar mSeekBar_energy;
    @BindView(R.id.mTextView_nowEnergy)
    TextView mTextView_nowEnergy;
    @BindView(R.id.mTextView_allEnergy)
    TextView mTextView_allEnergy;
    @BindView(R.id.mLinearLayout_energy)
    LinearLayout mLinearLayout_energy;
    @BindView(R.id.mTextView_myFreezeEnergy)
    TextView mTextView_myFreezeEnergy;
    @BindView(R.id.mTextView_heFreezeEnergy)
    TextView mTextView_heFreezeEnergy;
    @BindView(R.id.mTextView_freezeAllEnergy)
    TextView mTextView_freezeAllEnergy;
    @BindView(R.id.mImageView_arrowBandwidth)
    ImageView mImageView_arrowBandwidth;
    @BindView(R.id.mSeekBar_bandwidth)
    SeekBar mSeekBar_bandwidth;
    @BindView(R.id.mTextView_nowBandwidth)
    TextView mTextView_nowBandwidth;
    @BindView(R.id.mTextView_allBandwidth)
    TextView mTextView_allBandwidth;
    @BindView(R.id.mLinearLayout_bandwidth)
    LinearLayout mLinearLayout_bandwidth;
    @BindView(R.id.mTextView_myFreezeBandwidth)
    TextView mTextView_myFreezeBandwidth;
    @BindView(R.id.mTextView_heFreezeBandwidth)
    TextView mTextView_heFreezeBandwidth;
    @BindView(R.id.mTextView_freezeAllBandwidth)
    TextView mTextView_freezeAllBandwidth;
    @BindView(R.id.mTextView_voteNum)
    TextView mTextView_voteNum;
    @BindView(R.id.mTextView_freeze)
    TextView mTextView_freeze;
    @BindView(R.id.mTextView_unfreeze)
    TextView mTextView_unfreeze;
    @BindView(R.id.mTextView_bg)
    TextView mTextView_bg;
    @BindView(R.id.mImageView_energy)
    ImageView mImageView_energy;
    @BindView(R.id.mTextView_energy)
    TextView mTextView_energy;
    @BindView(R.id.mImageView_bandwidth)
    ImageView mImageView_bandwidth;
    @BindView(R.id.mTextView_bandwidth)
    TextView mTextView_bandwidth;
    @BindView(R.id.mLinearLayout_freeze)
    LinearLayout mLinearLayout_freeze;
    @BindView(R.id.mTextView_trxBalance)
    TextView mTextView_trxBalance;
    @BindView(R.id.mEditText_freezeNumber)
    EditText mEditText_freezeNumber;
    @BindView(R.id.mImageView_question)
    ImageView mImageView_question;
    @BindView(R.id.mTextView_estimateBandwidth)
    TextView mTextView_estimateBandwidth;
    @BindView(R.id.mTextView_voteRight)
    TextView mTextView_voteRight;
    @BindView(R.id.mEditText_receiveAddress)
    EditText mEditText_receiveAddress;
    @BindView(R.id.mEditText_tradePassword)
    EditText mEditText_tradePassword;
    @BindView(R.id.mRecyclerView_unfreeze)
    RecyclerView mRecyclerView_unfreeze;

    private Activity mContext;
    private boolean isAnimation = false, isFreeze = true, isEnergy = true, isClick = false;
    private MyDialog myDialog;
    private Gson gson;
    private SPUtils spUtils, spUtils1;
    private String tronAddress, privateKey;
    private BigDecimal trxBalance, energyBalance, netBalance, totalEnergyLimit, totalEnergyWeight, totalNetLimit, totalNetWeight;
    private Dialog dialog;
    private WebView mWebView;
    private int number = 1;
    private UnFreezeAdapter adapter;
    private List<UnfreezeBean> energyList, bandwidthList;
    private RecyclerViewNoBugLinearLayoutManager layoutManager = null;
    private Comparator<UnfreezeBean> beanComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tron_resource);
        ButterKnife.bind(this);
        OtherUtils.config(this);
        init();
    }

    private void init() {
        mContext = TronResourceActivity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(TronResourceActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

        spUtils = new SPUtils(mContext, "AschWallet");
        spUtils1 = new SPUtils(mContext, "AschImport");
        gson = new Gson();
        trxBalance = new BigDecimal("0");
        energyBalance = new BigDecimal("0");
        netBalance = new BigDecimal("0");
        totalEnergyLimit = new BigDecimal("0");
        totalEnergyWeight = new BigDecimal("0");
        totalNetLimit = new BigDecimal("0");
        totalNetWeight = new BigDecimal("0");
        energyList = new ArrayList<>();
        bandwidthList = new ArrayList<>();
        Content.unFreezeType = 1;

        setPullRefresher();
        mSeekBar_energy.setEnabled(false);
        mSeekBar_bandwidth.setEnabled(false);
        tronAddress = spUtils1.getString("tronChildAddress");
        List<TronDBHelperBean> beanList = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
        for (int i = 0; i < beanList.size(); i++) {
            if (tronAddress.equals(beanList.get(i).getAddress())) {
                privateKey = beanList.get(i).getSecret();
            }
        }

        beanComparator = new Comparator<UnfreezeBean>() {
            @Override
            public int compare(UnfreezeBean o1, UnfreezeBean o2) {
                if (o1.getUnFreezeTime() <= o2.getUnFreezeTime()) {
                    return -1;
                }
                return 1;
            }
        };

        adapter = new UnFreezeAdapter(mContext, energyList, bandwidthList, tronAddress);
        layoutManager = new RecyclerViewNoBugLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView_unfreeze.setLayoutManager(layoutManager);
        mRecyclerView_unfreeze.setAdapter(adapter);
        adapter.setOnClickListenerFace(this);

        mEditText_receiveAddress.setText(tronAddress);
        mEditText_freezeNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEditText_freezeNumber.getText().toString().isEmpty()) {
                    mTextView_voteRight.setText("= 0 投票权");
                    if (isEnergy) {
                        mTextView_estimateBandwidth.setText("≈ 0 能量");
                    } else {
                        mTextView_estimateBandwidth.setText("≈ 0 带宽");
                    }
                } else {
                    mTextView_voteRight.setText("= " + mEditText_freezeNumber.getText().toString() + " 投票权");
                    BigDecimal freezeResource = new BigDecimal(mEditText_freezeNumber.getText().toString());
                    if (isEnergy) {
                        mTextView_estimateBandwidth.setText("≈ " + freezeResource.divide(freezeResource.add(totalEnergyWeight), 24, BigDecimal.ROUND_DOWN).multiply(totalEnergyLimit).setScale(0, BigDecimal.ROUND_DOWN).toString() + " 能量");
                    } else {
                        mTextView_estimateBandwidth.setText("≈ " + freezeResource.divide(freezeResource.add(totalNetWeight), 24, BigDecimal.ROUND_DOWN).multiply(totalNetLimit).setScale(0, BigDecimal.ROUND_DOWN).toString() + " 带宽");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myDialog = DialogUtils.createLoadingDialog(mContext, "获取资源信息...");
                            getAddressInfo();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick({R.id.mImageView_back, R.id.mLinearLayout_question, R.id.mRelativeLayout_energy, R.id.mRelativeLayout_bandwidth, R.id.mTextView_freeze, R.id.mTextView_unfreeze, R.id.mImageView_energy, R.id.mTextView_energy, R.id.mImageView_bandwidth, R.id.mTextView_bandwidth, R.id.mImageView_question, R.id.mImageView_close, R.id.mTextView_btn_freeze})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mImageView_back:
                finish();
                break;
            case R.id.mLinearLayout_question:
                Intent intentFR = new Intent(mContext, FreezingRulesActivity.class);
                startActivity(intentFR);
                break;
            case R.id.mRelativeLayout_energy:
                if (mLinearLayout_energy.getVisibility() == View.GONE) {
                    mImageView_arrowEnergy.setImageResource(R.mipmap.ic_close);
                    mLinearLayout_energy.setVisibility(View.VISIBLE);
                } else {
                    mImageView_arrowEnergy.setImageResource(R.mipmap.ic_open);
                    mLinearLayout_energy.setVisibility(View.GONE);
                }
                break;
            case R.id.mRelativeLayout_bandwidth:
                if (mLinearLayout_bandwidth.getVisibility() == View.GONE) {
                    mImageView_arrowBandwidth.setImageResource(R.mipmap.ic_close);
                    mLinearLayout_bandwidth.setVisibility(View.VISIBLE);
                } else {
                    mImageView_arrowBandwidth.setImageResource(R.mipmap.ic_open);
                    mLinearLayout_bandwidth.setVisibility(View.GONE);
                }
                break;
            case R.id.mTextView_freeze:
                if (!isFreeze && !isAnimation) {
                    isFreeze = true;
                    TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE, mTextView_bg.getWidth(),
                            Animation.ABSOLUTE, -1f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isAnimation = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isAnimation = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    animation.setDuration(300);
                    animation.setFillAfter(true);
                    animation.setRepeatCount(0);
                    mTextView_bg.startAnimation(animation);
                    mTextView_freeze.setTextColor(getResources().getColor(R.color.text_bg));
                    mTextView_unfreeze.setTextColor(getResources().getColor(R.color.TextColor1));
                    mLinearLayout_freeze.setVisibility(View.VISIBLE);
                    mRecyclerView_unfreeze.setVisibility(View.GONE);
                }
                break;
            case R.id.mTextView_unfreeze:
                if (isFreeze && !isAnimation) {
                    isFreeze = false;
                    TranslateAnimation animation1 = new TranslateAnimation(Animation.ABSOLUTE, 0.0f,
                            Animation.ABSOLUTE, mTextView_bg.getWidth(),
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isAnimation = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isAnimation = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    animation1.setDuration(300);
                    animation1.setFillAfter(true);
                    animation1.setRepeatCount(0);
                    mTextView_bg.startAnimation(animation1);
                    mTextView_freeze.setTextColor(getResources().getColor(R.color.TextColor1));
                    mTextView_unfreeze.setTextColor(getResources().getColor(R.color.text_bg));
                    mLinearLayout_freeze.setVisibility(View.GONE);
                    mRecyclerView_unfreeze.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.mImageView_energy:
            case R.id.mTextView_energy:
                if (!isEnergy) {
                    Content.unFreezeType = 1;
                    adapter.notifyDataSetChanged();
                    isEnergy = true;
                    mImageView_energy.setImageResource(R.mipmap.resources_select);
                    mTextView_energy.setTextColor(getResources().getColor(R.color.text_bg));
                    mImageView_bandwidth.setImageResource(R.mipmap.resources_normal);
                    mTextView_bandwidth.setTextColor(getResources().getColor(R.color.main));
                    if (mEditText_freezeNumber.getText().toString().isEmpty()) {
                        mTextView_estimateBandwidth.setText("≈ 0 能量");
                    } else {
                        BigDecimal freezeResource = new BigDecimal(mEditText_freezeNumber.getText().toString());
                        mTextView_estimateBandwidth.setText("≈ " + freezeResource.divide(freezeResource.add(totalEnergyWeight), 24, BigDecimal.ROUND_DOWN).multiply(totalEnergyLimit).setScale(0, BigDecimal.ROUND_DOWN).toString() + " 能量");
                    }
                }
                break;
            case R.id.mImageView_bandwidth:
            case R.id.mTextView_bandwidth:
                if (isEnergy) {
                    Content.unFreezeType = 2;
                    adapter.notifyDataSetChanged();
                    isEnergy = false;
                    mImageView_energy.setImageResource(R.mipmap.resources_normal);
                    mTextView_energy.setTextColor(getResources().getColor(R.color.main));
                    mImageView_bandwidth.setImageResource(R.mipmap.resources_select);
                    mTextView_bandwidth.setTextColor(getResources().getColor(R.color.text_bg));
                    if (mEditText_freezeNumber.getText().toString().isEmpty()) {
                        mTextView_estimateBandwidth.setText("≈ 0 带宽");
                    } else {
                        BigDecimal freezeResource = new BigDecimal(mEditText_freezeNumber.getText().toString());
                        mTextView_estimateBandwidth.setText("≈ " + freezeResource.divide(freezeResource.add(totalNetWeight), 24, BigDecimal.ROUND_DOWN).multiply(totalNetLimit).setScale(0, BigDecimal.ROUND_DOWN).toString() + " 带宽");
                    }
                }
                break;
            case R.id.mImageView_question:
                View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_question_layout, null);
                TextView mTextView_text1 = dialogView1.findViewById(R.id.mTextView_text);
                mTextView_text1.setText("能量只能通过冻结TRX获取，获取的能量额度 = 为获取能量冻结的 TRX / 整个网络为获取能量冻结的 TRX 总额 * 波场网络总能量上限。也就是所有用户按冻结 TRX 平分固定额度的能量。");
                BubbleLayout bl1 = new BubbleLayout(mContext);
                bl1.setBubbleColor(mContext.getResources().getColor(R.color.bg_color_ccBlack));
                bl1.setShadowColor(mContext.getResources().getColor(R.color.bg_color_ccBlack));
                bl1.setLookLength(Util.dpToPx(mContext, 9));
                bl1.setBubbleRadius(Util.dpToPx(mContext, 3));
                BubbleDialog bubbleDialog = new BubbleDialog(mContext)
                        .setClickedView(mImageView_question)
                        .setBubbleContentView(dialogView1)
                        .setPosition(BubbleDialog.Position.BOTTOM)
                        .setOffsetY(-8)
                        .setTransParentBackground()
                        .setBubbleLayout(bl1)
                        .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 0);
                bubbleDialog.show();
                break;
            case R.id.mImageView_close:
                mEditText_receiveAddress.setText("");
                break;
            case R.id.mTextView_btn_freeze:
                if (isClick) {
                    return;
                }
                if (mEditText_freezeNumber.getText().toString().isEmpty() || Float.parseFloat(mEditText_freezeNumber.getText().toString()) <= 0) {
                    ToastUtil.showShort(mContext, "冻结资源数量不能为空或0");
                    return;
                }
                if (mEditText_receiveAddress.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "资源接收方账户不能为空");
                    return;
                }
                if (!mEditText_receiveAddress.getText().toString().substring(0, 1).equals("T") || mEditText_receiveAddress.getText().toString().length() != 34) {
                    ToastUtil.showLong(mContext, "资源接收方地址格式不正确，地址须以T开头，长度为34个字符");
                    return;
                }
                if (mEditText_tradePassword.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "交易密码不能为空");
                    return;
                }
                try {
                    String password = AESUtil.decrypt(privateKey, mEditText_tradePassword.getText().toString());
                    isClick = true;
                    confirmFreezeDialog(password);
                } catch (Exception e) {
                    ToastUtil.showShort(mContext, "交易密码错误");
                    return;
                }
                break;
        }
    }

    @Override
    public void OnClickTemp(int p, View view) {
        switch (view.getId()) {
            case R.id.mTextView_address:
            case R.id.mImageView_copy:
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData;
                if (isEnergy) {
                    mClipData = ClipData.newPlainText("address", energyList.get(p).getToAddress());
                } else {
                    mClipData = ClipData.newPlainText("address", bandwidthList.get(p).getToAddress());
                }
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort(mContext, "账户地址已复制到剪贴板");
                break;
            case R.id.mTextView_btn_unfreeze:
                if (isClick) {
                    return;
                }
                isClick = true;
                unFreezeDialog(p);
                break;
        }
    }

    private void setPullRefresher() {
        mRefreshLayout.setRefreshHeader(new MyClassicsHeader(mContext));
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void getAddressInfo() {
        OkHttpClient.initGet(Http.getTronInfo + tronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，获取资源信息失败");
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
                            DialogUtils.closeDialog(myDialog);
                            energyList.clear();
                            bandwidthList.clear();
                            JSONObject jsonObject = new JSONObject(result);
                            trxBalance = new BigDecimal(jsonObject.getString("balance")).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN).setScale(6, BigDecimal.ROUND_DOWN);
                            mTextView_trxBalance.setText("当前可用：" + trxBalance.toString() + " TRX");
                            BigDecimal myFreezeEnergy;
                            BigDecimal heFreezeEnergy;
                            BigDecimal myFreezeBandwidth = new BigDecimal(jsonObject.getJSONObject("frozen").getString("total")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN).setScale(0, BigDecimal.ROUND_DOWN);
                            BigDecimal heFreezeBandwidth;
                            BigDecimal voteNum = new BigDecimal("0");
                            if (myFreezeBandwidth.floatValue() != 0) {
                                UnfreezeBean bean = new UnfreezeBean();
                                bean.setToAddress(tronAddress);
                                bean.setType(2);
                                bean.setFreezeNumber(myFreezeBandwidth);
                                bean.setUnFreezeTime(jsonObject.getJSONObject("frozen").getJSONArray("balances").getJSONObject(0).getLong("expires"));
                                bandwidthList.add(bean);
                            }
                            if (jsonObject.getJSONObject("accountResource").getJSONObject("frozen_balance_for_energy").toString().length() > 2) {
                                mTextView_myFreezeEnergy.setText(new BigDecimal(jsonObject.getJSONObject("accountResource").getJSONObject("frozen_balance_for_energy").getString("frozen_balance")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                myFreezeEnergy = new BigDecimal(jsonObject.getJSONObject("accountResource").getJSONObject("frozen_balance_for_energy").getString("frozen_balance")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN);
                                UnfreezeBean bean = new UnfreezeBean();
                                bean.setToAddress(tronAddress);
                                bean.setType(1);
                                bean.setFreezeNumber(myFreezeEnergy);
                                bean.setUnFreezeTime(jsonObject.getJSONObject("accountResource").getJSONObject("frozen_balance_for_energy").getLong("expire_time"));
                                energyList.add(bean);
                            } else {
                                mTextView_myFreezeEnergy.setText("0");
                                myFreezeEnergy = new BigDecimal("0");
                            }
                            JSONArray receivedDelegatedResource = jsonObject.getJSONObject("delegated").getJSONArray("receivedDelegatedResource");
                            if (receivedDelegatedResource.length() > 0) {
                                BigDecimal frozen_balance_for_energy = new BigDecimal("0");
                                for (int i = 0; i < receivedDelegatedResource.length(); i++) {
                                    frozen_balance_for_energy = frozen_balance_for_energy.add(new BigDecimal(receivedDelegatedResource.getJSONObject(i).getString("frozen_balance_for_energy")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                }
                                mTextView_heFreezeEnergy.setText(frozen_balance_for_energy.setScale(0, BigDecimal.ROUND_DOWN).toString());
                                heFreezeEnergy = frozen_balance_for_energy;
                            } else {
                                mTextView_heFreezeEnergy.setText("0");
                                heFreezeEnergy = new BigDecimal("0");
                            }
                            mTextView_freezeAllEnergy.setText(myFreezeEnergy.add(heFreezeEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
                            mTextView_myFreezeBandwidth.setText(myFreezeBandwidth.toString());
                            JSONArray receivedDelegatedBandwidth = jsonObject.getJSONObject("delegated").getJSONArray("receivedDelegatedBandwidth");
                            if (receivedDelegatedBandwidth.length() > 0) {
                                BigDecimal frozen_balance_for_bandwidth = new BigDecimal("0");
                                for (int i = 0; i < receivedDelegatedBandwidth.length(); i++) {
                                    frozen_balance_for_bandwidth = frozen_balance_for_bandwidth.add(new BigDecimal(receivedDelegatedBandwidth.getJSONObject(i).getString("frozen_balance_for_bandwidth")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                }
                                mTextView_heFreezeBandwidth.setText(frozen_balance_for_bandwidth.setScale(0, BigDecimal.ROUND_DOWN).toString());
                                heFreezeBandwidth = frozen_balance_for_bandwidth;
                            } else {
                                mTextView_heFreezeBandwidth.setText("0");
                                heFreezeBandwidth = new BigDecimal("0");
                            }
                            mTextView_freezeAllBandwidth.setText(myFreezeBandwidth.add(heFreezeBandwidth).setScale(0, BigDecimal.ROUND_DOWN).toString());
                            JSONArray sentDelegatedBandwidth = jsonObject.getJSONObject("delegated").getJSONArray("sentDelegatedBandwidth");
                            if (sentDelegatedBandwidth.length() > 0) {
                                BigDecimal frozen_balance_for_bandwidth = new BigDecimal("0");
                                for (int i = 0; i < sentDelegatedBandwidth.length(); i++) {
                                    UnfreezeBean bean = new UnfreezeBean();
                                    bean.setToAddress(sentDelegatedBandwidth.getJSONObject(i).getString("to"));
                                    bean.setType(2);
                                    bean.setFreezeNumber(new BigDecimal(sentDelegatedBandwidth.getJSONObject(i).getString("frozen_balance_for_bandwidth")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                    bean.setUnFreezeTime(sentDelegatedBandwidth.getJSONObject(i).getLong("expire_time_for_bandwidth"));
                                    bandwidthList.add(bean);
                                    frozen_balance_for_bandwidth = frozen_balance_for_bandwidth.add(new BigDecimal(sentDelegatedBandwidth.getJSONObject(i).getString("frozen_balance_for_bandwidth")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                }
                                voteNum = voteNum.add(frozen_balance_for_bandwidth);
                            }
                            JSONArray sentDelegatedResource = jsonObject.getJSONObject("delegated").getJSONArray("sentDelegatedResource");
                            if (sentDelegatedResource.length() > 0) {
                                BigDecimal frozen_balance_for_energy = new BigDecimal("0");
                                for (int i = 0; i < sentDelegatedResource.length(); i++) {
                                    UnfreezeBean bean = new UnfreezeBean();
                                    bean.setToAddress(sentDelegatedResource.getJSONObject(i).getString("to"));
                                    bean.setType(1);
                                    bean.setFreezeNumber(new BigDecimal(sentDelegatedResource.getJSONObject(i).getString("frozen_balance_for_energy")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                    bean.setUnFreezeTime(sentDelegatedResource.getJSONObject(i).getLong("expire_time_for_energy"));
                                    energyList.add(bean);
                                    frozen_balance_for_energy = frozen_balance_for_energy.add(new BigDecimal(sentDelegatedResource.getJSONObject(i).getString("frozen_balance_for_energy")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                }
                                voteNum = voteNum.add(frozen_balance_for_energy);
                            }
                            mTextView_voteNum.setText(voteNum.add(myFreezeBandwidth).add(myFreezeEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
                            totalEnergyLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("totalEnergyLimit"));
                            totalEnergyWeight = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("totalEnergyWeight"));
                            totalNetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("totalNetLimit"));
                            totalNetWeight = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("totalNetWeight"));
                            BigDecimal freeNetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetLimit"));
                            BigDecimal freeNetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetUsed"));
                            BigDecimal NetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netUsed"));
                            BigDecimal NetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netLimit"));
                            BigDecimal energyLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyLimit"));
                            BigDecimal energyUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyUsed"));
                            if (energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN).floatValue() <= 0f) {
                                energyBalance = new BigDecimal("0");
                                mTextView_nowEnergy.setText("0");
                                mTextView_allEnergy.setText("/" + energyLimit.setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mSeekBar_energy.setProgress(0);
                            } else {
                                energyBalance = energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN);
                                mTextView_nowEnergy.setText(energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mTextView_allEnergy.setText("/" + energyLimit.setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mSeekBar_energy.setProgress(energyLimit.subtract(energyUsed).divide(energyLimit, 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).intValue());
                            }
                            if (freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN).floatValue() <= 0f) {
                                netBalance = new BigDecimal("0");
                                mTextView_nowBandwidth.setText("0");
                                mTextView_allBandwidth.setText("/" + freeNetLimit.add(NetLimit).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mSeekBar_bandwidth.setProgress(0);
                            } else {
                                netBalance = freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN);
                                mTextView_nowBandwidth.setText(freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mTextView_allBandwidth.setText("/" + freeNetLimit.add(NetLimit).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mSeekBar_bandwidth.setProgress(freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).divide(freeNetLimit.add(NetLimit), 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).intValue());
                            }
                            if (!energyList.isEmpty()) {
                                Collections.sort(energyList, beanComparator);
                            }
                            if (!bandwidthList.isEmpty()) {
                                Collections.sort(bandwidthList, beanComparator);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "获取资源信息错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        OkHttpClient.initGet(Http.getTronInfo + tronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh(1000);
                        ToastUtil.showShort(mContext, "网络错误，获取资源信息失败");
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
                            refreshLayout.finishRefresh(1000);
                            energyList.clear();
                            bandwidthList.clear();
                            JSONObject jsonObject = new JSONObject(result);
                            trxBalance = new BigDecimal(jsonObject.getString("balance")).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN).setScale(6, BigDecimal.ROUND_DOWN);
                            mTextView_trxBalance.setText("当前可用：" + trxBalance.toString() + " TRX");
                            BigDecimal myFreezeEnergy;
                            BigDecimal heFreezeEnergy;
                            BigDecimal myFreezeBandwidth = new BigDecimal(jsonObject.getJSONObject("frozen").getString("total")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN).setScale(0, BigDecimal.ROUND_DOWN);
                            BigDecimal heFreezeBandwidth;
                            BigDecimal voteNum = new BigDecimal("0");
                            if (myFreezeBandwidth.floatValue() != 0) {
                                UnfreezeBean bean = new UnfreezeBean();
                                bean.setToAddress(tronAddress);
                                bean.setType(2);
                                bean.setFreezeNumber(myFreezeBandwidth);
                                bean.setUnFreezeTime(jsonObject.getJSONObject("frozen").getJSONArray("balances").getJSONObject(0).getLong("expires"));
                                bandwidthList.add(bean);
                            }
                            if (jsonObject.getJSONObject("accountResource").getJSONObject("frozen_balance_for_energy").toString().length() > 2) {
                                mTextView_myFreezeEnergy.setText(new BigDecimal(jsonObject.getJSONObject("accountResource").getJSONObject("frozen_balance_for_energy").getString("frozen_balance")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                myFreezeEnergy = new BigDecimal(jsonObject.getJSONObject("accountResource").getJSONObject("frozen_balance_for_energy").getString("frozen_balance")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN);
                                UnfreezeBean bean = new UnfreezeBean();
                                bean.setToAddress(tronAddress);
                                bean.setType(1);
                                bean.setFreezeNumber(myFreezeEnergy);
                                bean.setUnFreezeTime(jsonObject.getJSONObject("accountResource").getJSONObject("frozen_balance_for_energy").getLong("expire_time"));
                                energyList.add(bean);
                            } else {
                                mTextView_myFreezeEnergy.setText("0");
                                myFreezeEnergy = new BigDecimal("0");
                            }
                            JSONArray receivedDelegatedResource = jsonObject.getJSONObject("delegated").getJSONArray("receivedDelegatedResource");
                            if (receivedDelegatedResource.length() > 0) {
                                BigDecimal frozen_balance_for_energy = new BigDecimal("0");
                                for (int i = 0; i < receivedDelegatedResource.length(); i++) {
                                    frozen_balance_for_energy = frozen_balance_for_energy.add(new BigDecimal(receivedDelegatedResource.getJSONObject(i).getString("frozen_balance_for_energy")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                }
                                mTextView_heFreezeEnergy.setText(frozen_balance_for_energy.setScale(0, BigDecimal.ROUND_DOWN).toString());
                                heFreezeEnergy = frozen_balance_for_energy;
                            } else {
                                mTextView_heFreezeEnergy.setText("0");
                                heFreezeEnergy = new BigDecimal("0");
                            }
                            mTextView_freezeAllEnergy.setText(myFreezeEnergy.add(heFreezeEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
                            mTextView_myFreezeBandwidth.setText(myFreezeBandwidth.toString());
                            JSONArray receivedDelegatedBandwidth = jsonObject.getJSONObject("delegated").getJSONArray("receivedDelegatedBandwidth");
                            if (receivedDelegatedBandwidth.length() > 0) {
                                BigDecimal frozen_balance_for_bandwidth = new BigDecimal("0");
                                for (int i = 0; i < receivedDelegatedBandwidth.length(); i++) {
                                    frozen_balance_for_bandwidth = frozen_balance_for_bandwidth.add(new BigDecimal(receivedDelegatedBandwidth.getJSONObject(i).getString("frozen_balance_for_bandwidth")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                }
                                mTextView_heFreezeBandwidth.setText(frozen_balance_for_bandwidth.setScale(0, BigDecimal.ROUND_DOWN).toString());
                                heFreezeBandwidth = frozen_balance_for_bandwidth;
                            } else {
                                mTextView_heFreezeBandwidth.setText("0");
                                heFreezeBandwidth = new BigDecimal("0");
                            }
                            mTextView_freezeAllBandwidth.setText(myFreezeBandwidth.add(heFreezeBandwidth).setScale(0, BigDecimal.ROUND_DOWN).toString());
                            JSONArray sentDelegatedBandwidth = jsonObject.getJSONObject("delegated").getJSONArray("sentDelegatedBandwidth");
                            if (sentDelegatedBandwidth.length() > 0) {
                                BigDecimal frozen_balance_for_bandwidth = new BigDecimal("0");
                                for (int i = 0; i < sentDelegatedBandwidth.length(); i++) {
                                    UnfreezeBean bean = new UnfreezeBean();
                                    bean.setToAddress(sentDelegatedBandwidth.getJSONObject(i).getString("to"));
                                    bean.setType(2);
                                    bean.setFreezeNumber(new BigDecimal(sentDelegatedBandwidth.getJSONObject(i).getString("frozen_balance_for_bandwidth")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                    bean.setUnFreezeTime(sentDelegatedBandwidth.getJSONObject(i).getLong("expire_time_for_bandwidth"));
                                    bandwidthList.add(bean);
                                    frozen_balance_for_bandwidth = frozen_balance_for_bandwidth.add(new BigDecimal(sentDelegatedBandwidth.getJSONObject(i).getString("frozen_balance_for_bandwidth")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                }
                                voteNum = voteNum.add(frozen_balance_for_bandwidth);
                            }
                            JSONArray sentDelegatedResource = jsonObject.getJSONObject("delegated").getJSONArray("sentDelegatedResource");
                            if (sentDelegatedResource.length() > 0) {
                                BigDecimal frozen_balance_for_energy = new BigDecimal("0");
                                for (int i = 0; i < sentDelegatedResource.length(); i++) {
                                    UnfreezeBean bean = new UnfreezeBean();
                                    bean.setToAddress(sentDelegatedResource.getJSONObject(i).getString("to"));
                                    bean.setType(1);
                                    bean.setFreezeNumber(new BigDecimal(sentDelegatedResource.getJSONObject(i).getString("frozen_balance_for_energy")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                    bean.setUnFreezeTime(sentDelegatedResource.getJSONObject(i).getLong("expire_time_for_energy"));
                                    energyList.add(bean);
                                    frozen_balance_for_energy = frozen_balance_for_energy.add(new BigDecimal(sentDelegatedResource.getJSONObject(i).getString("frozen_balance_for_energy")).divide(new BigDecimal("1000000"), 0, BigDecimal.ROUND_DOWN));
                                }
                                voteNum = voteNum.add(frozen_balance_for_energy);
                            }
                            mTextView_voteNum.setText(voteNum.add(myFreezeBandwidth).add(myFreezeEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
                            totalEnergyLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("totalEnergyLimit"));
                            totalEnergyWeight = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("totalEnergyWeight"));
                            totalNetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("totalNetLimit"));
                            totalNetWeight = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("totalNetWeight"));
                            BigDecimal freeNetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetLimit"));
                            BigDecimal freeNetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetUsed"));
                            BigDecimal NetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netUsed"));
                            BigDecimal NetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netLimit"));
                            BigDecimal energyLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyLimit"));
                            BigDecimal energyUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyUsed"));
                            if (energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN).floatValue() <= 0f) {
                                energyBalance = new BigDecimal("0");
                                mTextView_nowEnergy.setText("0");
                                mTextView_allEnergy.setText("/" + energyLimit.setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mSeekBar_energy.setProgress(0);
                            } else {
                                energyBalance = energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN);
                                mTextView_nowEnergy.setText(energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mTextView_allEnergy.setText("/" + energyLimit.setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mSeekBar_energy.setProgress(energyLimit.subtract(energyUsed).divide(energyLimit, 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).intValue());
                            }
                            if (freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN).floatValue() <= 0f) {
                                netBalance = new BigDecimal("0");
                                mTextView_nowBandwidth.setText("0");
                                mTextView_allBandwidth.setText("/" + freeNetLimit.add(NetLimit).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mSeekBar_bandwidth.setProgress(0);
                            } else {
                                netBalance = freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN);
                                mTextView_nowBandwidth.setText(freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mTextView_allBandwidth.setText("/" + freeNetLimit.add(NetLimit).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mSeekBar_bandwidth.setProgress(freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).divide(freeNetLimit.add(NetLimit), 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).intValue());
                            }

                            if (mEditText_freezeNumber.getText().toString().isEmpty()) {
                                mTextView_voteRight.setText("= 0 投票权");
                                if (isEnergy) {
                                    mTextView_estimateBandwidth.setText("≈ 0 能量");
                                } else {
                                    mTextView_estimateBandwidth.setText("≈ 0 带宽");
                                }
                            } else {
                                mTextView_voteRight.setText("= " + mEditText_freezeNumber.getText().toString() + " 投票权");
                                BigDecimal freezeResource = new BigDecimal(mEditText_freezeNumber.getText().toString());
                                if (isEnergy) {
                                    mTextView_estimateBandwidth.setText("≈ " + freezeResource.divide(freezeResource.add(totalEnergyWeight), 24, BigDecimal.ROUND_DOWN).multiply(totalEnergyLimit).setScale(0, BigDecimal.ROUND_DOWN).toString() + " 能量");
                                } else {
                                    mTextView_estimateBandwidth.setText("≈ " + freezeResource.divide(freezeResource.add(totalNetWeight), 24, BigDecimal.ROUND_DOWN).multiply(totalNetLimit).setScale(0, BigDecimal.ROUND_DOWN).toString() + " 带宽");
                                }
                            }

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "获取资源信息错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void confirmFreezeDialog(String password) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm_freeze_layout, null);
        TextView mTextView_trx = view.findViewById(R.id.mTextView_trx);
        TextView mTextView_type = view.findViewById(R.id.mTextView_type);
        TextView mTextView_sendAddress = view.findViewById(R.id.mTextView_sendAddress);
        TextView mTextView_trx_bottom = view.findViewById(R.id.mTextView_trx_bottom);
        TextView mTextView_fee = view.findViewById(R.id.mTextView_fee);

        mTextView_trx.setText(mEditText_freezeNumber.getText().toString() + " TRX");
        mTextView_type.setText(isEnergy ? "能量" : "带宽");
        mTextView_sendAddress.setText(mEditText_receiveAddress.getText().toString().substring(0, 14) + "..." + mEditText_receiveAddress.getText().toString().substring(mEditText_receiveAddress.getText().toString().length() - 14, mEditText_receiveAddress.getText().toString().length()));
        mTextView_trx_bottom.setText(mEditText_freezeNumber.getText().toString() + " TRX");

        view.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.mTextView_btn_freeze).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
                myDialog = DialogUtils.createLoadingDialog(mContext, "冻结中...");
                freezeBalance(password);
            }
        });

        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
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

    private void freezeBalance(String password) {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        FreezeBalanceJavaScriptInterface javaScriptInterface = new FreezeBalanceJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/freezeBalance.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                freezeBalanceJsParseMainAssetTra(password);
            }
        });
    }

    private void freezeBalanceJsParseMainAssetTra(String password) {
        String amount = mEditText_freezeNumber.getText().toString();
        String resource = isEnergy ? "ENERGY" : "BANDWIDTH";
        String receiverAddress = mEditText_receiveAddress.getText().toString();
        String url = "javascript:freezeBalance('" + password + "'," + amount + ",'" + resource + "','" + receiverAddress + "','" + Http.tronUrl + "')";
        mWebView.loadUrl(url);
    }

    private class FreezeBalanceJavaScriptInterface {
        @JavascriptInterface
        public void freezeBalanceFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, str);
                    } else {
                        myDialog.setMsg("链上验证结果中...");
                        number = 1;
                        getTransactionInfoByIdType(str, 1);
                    }
                }
            });
        }
    }

    private void getTransactionInfoByIdType(String TransactionId, int type) {
        OkHttpClient.initGet(Http.validateHash + TransactionId).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (number > 3) {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "网络错误，验证结果失败");
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(5000);
                                        ++number;
                                        getTransactionInfoByIdType(TransactionId, type);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
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
                            if (result.length() > 10) {
                                DialogUtils.closeDialog(myDialog);
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getString("contractRet").equals("SUCCESS")) {
                                    if (type == 1) {
                                        ToastUtil.showShort(mContext, "冻结成功，数据更新大约需要1分钟");
                                        mEditText_freezeNumber.setText("");
                                        mEditText_tradePassword.setText("");
                                    } else if (type == 2) {
                                        ToastUtil.showShort(mContext, "解冻成功，数据更新大约需要1分钟");
                                    }
                                    getAddressInfo();
                                    EventBus.getDefault().postSticky(new EventMessageBean(29, null));
                                } else {
                                    ToastUtil.showShort(mContext, "验证结果失败：" + jsonObject.getJSONObject("info").getString("resMessage"));
                                }
                            } else {
                                if (number > 3) {
                                    DialogUtils.closeDialog(myDialog);
                                    ToastUtil.showShort(mContext, "验证结果失败，未查到数据");
                                } else {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(5000);
                                                ++number;
                                                getTransactionInfoByIdType(TransactionId, type);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            }
                        } catch (Exception e) {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "验证结果失败，请稍候再试");
                        }
                    }
                });
            }
        });
    }

    private void unFreezeDialog(int position) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_tips_layout, null);
        final MyDialog dialog = new MyDialog(mContext, inflate, R.style.DialogTheme);
        TextView mTextView_title = inflate.findViewById(R.id.mTextView_title);
        TextView mTextView_false = inflate.findViewById(R.id.mTextView_false);
        TextView mTextView_true = inflate.findViewById(R.id.mTextView_true);

        mTextView_title.setText("解冻后，冻结所获取的投票权，带宽（或者能量）等资源对应部分均将被移除。");
        mTextView_false.setText("取消");
        mTextView_true.setText("确认解冻");

        mTextView_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });

        mTextView_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
                dialogAuthentication(position);
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void dialogAuthentication(int position) {
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
                try {
                    String password = AESUtil.decrypt(privateKey, mEditText_verification.getText().toString());
                    dialog.dismiss();
                    myDialog = DialogUtils.createLoadingDialog(mContext, "解冻中...");
                    unFreezeBalance(position, password);
                } catch (Exception e) {
                    ToastUtil.showShort(mContext, "交易密码错误");
                    return;
                }
            }
        });

        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
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

    private void unFreezeBalance(int position, String password) {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        UnFreezeBalanceJavaScriptInterface javaScriptInterface = new UnFreezeBalanceJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/freezeBalance.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                unFreezeBalanceJsParseMainAssetTra(position, password);
            }
        });
    }

    private void unFreezeBalanceJsParseMainAssetTra(int position, String password) {
        String resource = isEnergy ? "ENERGY" : "BANDWIDTH";
        String receiverAddress = isEnergy ? energyList.get(position).getToAddress() : bandwidthList.get(position).getToAddress();
        String url = "javascript:unfreezeBalance('" + password + "','" + resource + "','" + receiverAddress + "','" + Http.tronUrl + "')";
        mWebView.loadUrl(url);
    }

    private class UnFreezeBalanceJavaScriptInterface {
        @JavascriptInterface
        public void unFreezeBalanceFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, str);
                    } else {
                        myDialog.setMsg("链上验证结果中...");
                        number = 1;
                        getTransactionInfoByIdType(str, 2);
                    }
                }
            });
        }
    }

    private void hintKeyBoard(View view) {
        InputMethodManager inputMgr = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
