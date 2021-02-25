package com.xw.aschwitkey.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.happybubble.BubbleLayout;
import com.xujiaji.happybubble.Util;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.AccountManagementActivity;
import com.xw.aschwitkey.activity.ApHistoryActivity;
import com.xw.aschwitkey.activity.AsoHistoryActivity;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.activity.LpHistoryActivity;
import com.xw.aschwitkey.activity.WebViewActivity;
import com.xw.aschwitkey.db.SQLUtils;
import com.xw.aschwitkey.db.TronSQLUtils;
import com.xw.aschwitkey.entity.DBHelperBean;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.entity.TronDBHelperBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.AESUtil;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.TronUtils;
import com.xw.aschwitkey.view.MyClassicsHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.xw.aschwitkey.activity.Content.frozenEnergy;

public class PledgeMiningFragment extends NewLazyFragment implements OnRefreshListener {

    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mTextView_ap)
    TextView mTextView_ap;
    @BindView(R.id.mTextView_ap_message)
    TextView mTextView_ap_message;
    @BindView(R.id.mTextView_pledged_lp)
    TextView mTextView_pledged_lp;
    @BindView(R.id.mTextView_pledged)
    TextView mTextView_pledged;
    @BindView(R.id.mLottieAnimationView_click)
    LottieAnimationView mLottieAnimationView_click;
    @BindView(R.id.mTextView_btn_reward)
    TextView mTextView_btn_reward;
    @BindView(R.id.mLottieAnimationView_click_bottom)
    LottieAnimationView mLottieAnimationView_click_bottom;
    @BindView(R.id.mTextView_mining)
    TextView mTextView_mining;
    @BindView(R.id.mLottieAnimationView_mining)
    LottieAnimationView mLottieAnimationView_mining;
    @BindView(R.id.mTextView_reward)
    TextView mTextView_reward;
    @BindView(R.id.mTextView_startRewardBlock)
    TextView mTextView_startRewardBlock;
    @BindView(R.id.mTextView_getRewardsPerBlock)
    TextView mTextView_getRewardsPerBlock;
    @BindView(R.id.mRelativeLayout_mining)
    RelativeLayout mRelativeLayout_mining;
    @BindView(R.id.mLottieAnimationView_click_1)
    LottieAnimationView mLottieAnimationView_click_1;
    @BindView(R.id.mTextView_lp)
    TextView mTextView_lp;
    @BindView(R.id.mTextView_lp_message)
    TextView mTextView_lp_message;
    @BindView(R.id.mTextView_getRewardsPerBlock_lp)
    TextView mTextView_getRewardsPerBlock_lp;
    @BindView(R.id.mRelativeLayout_mining_lp)
    RelativeLayout mRelativeLayout_mining_lp;
    @BindView(R.id.mTextView_btn_reward_lp)
    TextView mTextView_btn_reward_lp;
    @BindView(R.id.mLottieAnimationView_click_bottom_lp)
    LottieAnimationView mLottieAnimationView_click_bottom_lp;
    @BindView(R.id.mTextView_mining_lp)
    TextView mTextView_mining_lp;
    @BindView(R.id.mLottieAnimationView_mining_lp)
    LottieAnimationView mLottieAnimationView_mining_lp;
    @BindView(R.id.mTextView_reward_lp)
    TextView mTextView_reward_lp;
    @BindView(R.id.mTextView_apMessage)
    TextView mTextView_apMessage;
    @BindView(R.id.mTextView_lpMessage)
    TextView mTextView_lpMessage;
    @BindView(R.id.mLinearLayout_ap)
    LinearLayout mLinearLayout_ap;
    @BindView(R.id.mLinearLayout_lp)
    LinearLayout mLinearLayout_lp;

    private Activity mContext;
    private boolean isClick = false, isAnimation = false, isAnimation1 = false, isPause = false, isPause1 = false, isSelect = true, isOk = false, isTron = false, flag = true, flag1 = true, isSecondSecret = false, isBinding = false, isSetLock = true, isFirst = true;
    private MyDialog myDialog;
    private SPUtils spUtils, spUtils1;
    private WebView mWebView, mWebView1;
    private Dialog dialog, dialog1;
    private String tronAddress = "", privateKey = "", editPrivateKey = "", creatorAddress = "", pountContractAddress = "", txasContractAddress = "", tradePassword = "", lockedBalance = "0", totalBalance = "0", usableBalance = "0", secret, addressNote = "", lpStartTime = "", minLiquidity = "", maxTokens = "", deadline = "", msgValue = "", valuesAggregatorAddress = "", lpExchangeAddress = "", lpPoolAddress = "", deleteAmount = "", minTrx = "", minTokens = "", revokeAmount = "", asoTokenAddress = "";
    private BigDecimal decimals, pledgeTotal, rewardBig, totalRewardBig, pledgedBig, energy_usage_total, net_usage, trxBalance, energyBalance, netBalance, trxToEnergy, trxToNet, exTokenBalace, exTrxBalance, trxToToken, tokenToTrx, userTrxAmount, userTokenAmount, lpBalance, asoBalance, totalLiquidity, allowance, slidingPoint, rewardLpBig, pledgedLpBig, totalRewardLpBig, getRewardsPerBlock;
    private double quota;
    private int number = 1, day = 90, xasLockQuarter = 1, xasLockHalfYear = 2, xasLockOneYear = 4, xasLockTwoYear = 16, xasLockLimit = 100, xasLockRate = 1;
    private long lockHeight = 0l, height = 0l, nowHeight = 0l, startLPBlock = 0l, nowBlock = 0l, startRewardBlock = 0l;
    private Timer timer;
    private TimerTask task;
    private CountDownTimer countDownTimer;
    private Gson gson;
    private EditText mEditText_tradePassword, mEditText_secondSecret;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            myDialog = DialogUtils.createLoadingDialog(mContext, "链上数据加载中...");
            getTronAddress(1);
            getDefiInfo();
        } else {
            if (spUtils1.getBoolean("isPageRefresh", false)) {
                myDialog = DialogUtils.createLoadingDialog(mContext, "链上数据加载中...");
                getTronAddress(1);
                getDefiInfo();
            }
        }
        isFirst = false;
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
                        if (tronAddress.isEmpty()) {
                            return;
                        }
                        getReward();
                        getDefiInfo();
                        getAddressInfo(null, true);
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getLpInfo();
                            }
                        });
                        while (isPause1) {
                            Thread.sleep(15000);
                        }
                        getRewardLp();
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
        return R.layout.fragment_pledge_mining;
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
        spUtils = new SPUtils(mContext, "AschWallet");
        spUtils1 = new SPUtils(mContext, "AschImport");
        gson = new Gson();
        slidingPoint = new BigDecimal("0.05");
        setPullRefresher();
        mLottieAnimationView_click.setAnimation(R.raw.wakuang_mini);
        mLottieAnimationView_click.setRepeatCount(-1);
        mLottieAnimationView_click.playAnimation();
        mLottieAnimationView_click_1.setAnimation(R.raw.wakuang_mini);
        mLottieAnimationView_click_1.setRepeatCount(-1);
        mLottieAnimationView_click_1.playAnimation();
        mLottieAnimationView_click_bottom.setAnimation(R.raw.click);
        mLottieAnimationView_click_bottom.setRepeatCount(-1);
        mLottieAnimationView_click_bottom.playAnimation();
        mLottieAnimationView_click_bottom_lp.setAnimation(R.raw.click);
        mLottieAnimationView_click_bottom_lp.setRepeatCount(-1);
        mLottieAnimationView_click_bottom_lp.playAnimation();
        netBalance = new BigDecimal("0");
        rewardBig = new BigDecimal("0");
        pledgedBig = new BigDecimal("0");
        totalRewardBig = new BigDecimal("0");
        rewardLpBig = new BigDecimal("0");
        pledgedLpBig = new BigDecimal("0");
        totalRewardLpBig = new BigDecimal("0");
    }

    @OnClick({R.id.mRelativeLayout_goAsch, R.id.mRelativeLayout_goTron, R.id.mTextView_btn_getAp, R.id.mTextView_btn_pledge_mining, R.id.mLinearLayout_pledged_lp, R.id.mLinearLayout_pledged, R.id.mTextView_btn_reward, R.id.mRelativeLayout_receive, R.id.mTextView_web_aso, R.id.mTextView_btn_getLp, R.id.mTextView_btn_mobility_mining, R.id.mTextView_btn_reward_lp, R.id.mRelativeLayout_receive_lp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mRelativeLayout_goAsch:
                Map map = new HashMap();
                map.put("type", 0);
                EventBus.getDefault().postSticky(new EventMessageBean(26, map));
                break;
            case R.id.mRelativeLayout_goTron:
                Map map1 = new HashMap();
                map1.put("type", 2);
                EventBus.getDefault().postSticky(new EventMessageBean(26, map1));
                break;
            case R.id.mTextView_btn_getAp:
                try {
                    if (System.currentTimeMillis() + Content.timePoor < OtherUtils.dateToStamp(Content.apStartTime)) {
                        ToastUtil.showLong(mContext, "AP凭证挖矿暂未开启，开启时间：" + Content.apStartTime);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    isSetLock = true;
                    if (!spUtils.getBoolean("isDeal", false)) {
                        dialogPassword(0);
                    } else if (spUtils1.getString("childAddress", "").isEmpty()) {
                        Intent ia = new Intent(mContext, AccountManagementActivity.class);
                        ia.putExtra("tradePassword", tradePassword);
                        startActivityForResult(ia, 1);
                        isClick = false;
                    } else if (!isTron) {
                        myDialog = DialogUtils.createLoadingDialog(mContext, "获取TRON绑定信息...");
                        getTronAddress(0);
                    } else if (spUtils.getString("tronAddress", "").isEmpty()) {
                        isBinding = true;
                        if (tradePassword.isEmpty()) {
                            dialogAuthentication();
                        } else {
                            bindingAddressDialog();
                        }
                    } else {
                        myDialog = DialogUtils.createLoadingDialog(mContext, "查询链上账户可用XAS");
                        getBalance();
                    }
                }
                break;
            case R.id.mTextView_btn_pledge_mining:
                try {
                    if (System.currentTimeMillis() + Content.timePoor < OtherUtils.dateToStamp(Content.apStartTime)) {
                        ToastUtil.showLong(mContext, "AP凭证挖矿暂未开启，开启时间：" + Content.apStartTime);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    isSetLock = false;
                    if (!spUtils.getBoolean("isDeal", false)) {
                        dialogPassword(1);
                    } else if (!isTron) {
                        myDialog = DialogUtils.createLoadingDialog(mContext, "获取TRON绑定信息...");
                        getTronAddress(3);
                    } else if (spUtils.getString("tronAddress", "").isEmpty()) {
                        isBinding = true;
                        if (tradePassword.isEmpty()) {
                            dialogAuthentication();
                        } else {
                            bindingAddressDialog();
                        }
                    } else if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                        isBinding = false;
                        if (tradePassword.isEmpty()) {
                            dialogAuthentication();
                        } else {
                            importAddressDialog();
                        }
                    } else {
                        isSelect = false;
                        myDialog = DialogUtils.createLoadingDialog(mContext, "正在查询链上可质押AP...");
                        getTxasBalance();
                    }
                }
                break;
            case R.id.mLinearLayout_pledged:
                if (!spUtils1.getString("tronChildAddress", "").isEmpty() || !spUtils.getString("tronAddress", "").isEmpty()) {
                    Intent intentAp = new Intent(mContext, ApHistoryActivity.class);
                    startActivity(intentAp);
                } else {
                    ToastUtil.showShort(mContext, "请先新建或导入TRON账户");
                }
                break;
            case R.id.mTextView_btn_reward:
                if (trxBalance == null) {
                    ToastUtil.showShort(mContext, "请稍候或刷新再试");
                    return;
                }
                if (trxBalance.floatValue() == 0f) {
                    ToastUtil.showShort(mContext, "TRX余额为0，无法领取收益");
                } else {
                    if (isClick) {
                        return;
                    } else {
                        if (rewardBig.floatValue() == -1f || rewardBig.floatValue() == 0f) {
                            ToastUtil.showShort(mContext, "未查询到可领取收益");
                        } else {
                            if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                                ToastUtil.showShort(mContext, "请先左滑至波场钱包导入已绑定账户");
                            } else {
                                isClick = true;
                                getWithdrawEnergy();
                            }
                        }
                    }
                }
                break;
            case R.id.mRelativeLayout_receive:
                if (!spUtils1.getString("tronChildAddress", "").isEmpty() || !spUtils.getString("tronAddress", "").isEmpty()) {
                    Intent intentAso = new Intent(mContext, AsoHistoryActivity.class);
                    intentAso.putExtra("type", 1);
                    startActivity(intentAso);
                } else {
                    ToastUtil.showShort(mContext, "请先新建或导入TRON账户");
                }
                break;
            case R.id.mTextView_web_aso:
                Intent intentWeb = new Intent(mContext, WebViewActivity.class);
                intentWeb.putExtra("url", "https://areso.io/#/");
                startActivity(intentWeb);
                break;
            case R.id.mTextView_btn_getLp:
                try {
                    if (System.currentTimeMillis() + Content.timePoor < OtherUtils.dateToStamp(lpStartTime)) {
                        ToastUtil.showLong(mContext, "LP凭证挖矿暂未开启，开启时间：" + lpStartTime);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                showButtonDialog();
                break;
            case R.id.mTextView_btn_mobility_mining:
                try {
                    if (System.currentTimeMillis() + Content.timePoor < OtherUtils.dateToStamp(lpStartTime)) {
                        ToastUtil.showLong(mContext, "LP凭证挖矿暂未开启，开启时间：" + lpStartTime);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (trxBalance == null) {
                    ToastUtil.showShort(mContext, "请稍候或刷新再试");
                    return;
                }
                if (trxBalance.floatValue() == 0f) {
                    ToastUtil.showShort(mContext, "TRX余额为0，无法质押LP");
                    return;
                }
                if (isClick) {
                    return;
                } else {
                    if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                        ToastUtil.showShort(mContext, "请先左滑至Tron钱包页面导入账号");
                        return;
                    }
                    if (exTokenBalace == null) {
                        ToastUtil.showShort(mContext, "正在加载LP矿池信息，请稍候或刷新再试");
                        return;
                    }
                    isClick = true;
                    myDialog = DialogUtils.createLoadingDialog(mContext, "预估LP质押手续费...");
                    getLpPledgeEnergy(1);
                }
                break;
            case R.id.mLinearLayout_pledged_lp:
                try {
                    if (System.currentTimeMillis() + Content.timePoor < OtherUtils.dateToStamp(lpStartTime)) {
                        ToastUtil.showLong(mContext, "LP凭证挖矿暂未开启，开启时间：" + lpStartTime);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!spUtils1.getString("tronChildAddress", "").isEmpty() || !spUtils.getString("tronAddress", "").isEmpty()) {
                    Intent intentLp = new Intent(mContext, LpHistoryActivity.class);
                    startActivity(intentLp);
                } else {
                    ToastUtil.showShort(mContext, "请先新建或导入TRON账户");
                }
                break;
            case R.id.mRelativeLayout_receive_lp:
                try {
                    if (System.currentTimeMillis() + Content.timePoor < OtherUtils.dateToStamp(lpStartTime)) {
                        ToastUtil.showLong(mContext, "LP凭证挖矿暂未开启，开启时间：" + lpStartTime);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!spUtils1.getString("tronChildAddress", "").isEmpty() || !spUtils.getString("tronAddress", "").isEmpty()) {
                    Intent intentAso = new Intent(mContext, AsoHistoryActivity.class);
                    intentAso.putExtra("type", 2);
                    startActivity(intentAso);
                } else {
                    ToastUtil.showShort(mContext, "请先新建或导入TRON账户");
                }
                break;
            case R.id.mTextView_btn_reward_lp:
                if (trxBalance == null) {
                    ToastUtil.showShort(mContext, "请稍候或刷新再试");
                    return;
                }
                if (trxBalance.floatValue() == 0f) {
                    ToastUtil.showShort(mContext, "TRX余额为0，无法领取收益");
                } else {
                    if (isClick) {
                        return;
                    } else {
                        if (rewardLpBig.floatValue() == -1f || rewardLpBig.floatValue() == 0f) {
                            ToastUtil.showShort(mContext, "未查询到可领取收益");
                        } else {
                            if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                                ToastUtil.showShort(mContext, "请先左滑至波场钱包导入已绑定账户");
                            } else {
                                isClick = true;
                                getLpWithdrawEnergy();
                            }
                        }
                    }
                }
                break;
        }
    }

    private void setPullRefresher() {
        mRefreshLayout.setRefreshHeader(new MyClassicsHeader(mContext));
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessageBean messageEvent) {
        switch (messageEvent.getTag()) {
            case 23:
                if ((boolean) messageEvent.getMesssage().get("isShow")) {
                    isSelect = true;
                    getTxasBalance();
                }
                break;
            case 27:
                isSelect = false;
                myDialog = DialogUtils.createLoadingDialog(mContext, "正在查询链上可质押AP...");
                getTxasBalance();
                break;
            case 28:
                mRelativeLayout_mining.setVisibility(View.GONE);
                getTronAddress(1);
                break;
        }
    }

    private void pledgeInfo(RefreshLayout refreshLayout) {
        OkHttpClient.initGet(Http.pledgeInfo).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh(1000);
                        }
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，请刷新重试");
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
                            if (jsonObject.getInt("code") == 1) {
                                decimals = new BigDecimal(jsonObject.getJSONObject("data").getString("decimals"));
                                pountContractAddress = jsonObject.getJSONObject("data").getString("pountContractAddress");
                                txasContractAddress = jsonObject.getJSONObject("data").getString("txasContractAddress");
                                asoTokenAddress = jsonObject.getJSONObject("data").getString("asoTokenAddress");
                                valuesAggregatorAddress = jsonObject.getJSONObject("data").getString("valuesAggregatorAddress");
                                lpExchangeAddress = jsonObject.getJSONObject("data").getString("lpExchangeAddress");
                                lpPoolAddress = jsonObject.getJSONObject("data").getString("lpPoolAddress");
                                trxToEnergy = new BigDecimal(jsonObject.getJSONObject("data").getString("trxToEnergy"));
                                trxToNet = new BigDecimal(jsonObject.getJSONObject("data").getString("trxToNet"));
                                startLPBlock = Long.parseLong(jsonObject.getJSONObject("data").getString("startLPBlock"));
                                mTextView_apMessage.setVisibility(View.GONE);
                                mTextView_lpMessage.setVisibility(View.GONE);
                                pledgeTotal = new BigDecimal("0");
                                lpBalance = new BigDecimal("0");
                                mTextView_ap.setText("？ AP");
                                mTextView_lp.setText("？ LP");
                                isSelect = true;
                                if (!tronAddress.isEmpty()) {
                                    List<TronDBHelperBean> beanList = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                                    for (int i = 0; i < beanList.size(); i++) {
                                        if (tronAddress.equals(beanList.get(i).getAddress())) {
                                            privateKey = beanList.get(i).getSecret();
                                        }
                                    }
                                    getTxasBalance();
                                    getAddressInfo(refreshLayout, true);
                                    getReward();
                                    getTotalReward();
                                    getPledged();
                                    getRewardLp();
                                    getTotalRewardLp();
                                    getPledgedLp();
                                    getLpInfo();
                                } else {
                                    if (refreshLayout != null) {
                                        refreshLayout.finishRefresh(1000);
                                    }
                                    mTextView_ap.setText("0.000 AP");
                                    mTextView_pledged.setText("0.000");
                                    mTextView_lp.setText("0.000 LP");
                                    mTextView_pledged_lp.setText("0.000");
                                    DialogUtils.closeDialog(myDialog);
                                }
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "获取合约信息报错，请刷新再试");
                        }
                    }
                });
            }
        });
    }

    private void getAddressInfo(RefreshLayout refreshLayout, boolean isSelect) {
        OkHttpClient.initGet(Http.getTronInfo + tronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh(1000);
                        }
                        if (!isSelect) {
                            DialogUtils.closeDialog(myDialog);
                        }
                        ToastUtil.showShort(mContext, "网络错误，加载账户信息失败");
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
                            if (refreshLayout != null) {
                                refreshLayout.finishRefresh(1000);
                            }
                            JSONObject jsonObject = new JSONObject(result);
                            BigDecimal freeNetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetLimit"));
                            BigDecimal freeNetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetUsed"));
                            BigDecimal NetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netUsed"));
                            BigDecimal NetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netLimit"));
                            BigDecimal energyLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyLimit"));
                            BigDecimal energyUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyUsed"));
                            trxBalance = new BigDecimal(jsonObject.getString("balance")).divide(new BigDecimal("1000000"), 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                            energyBalance = energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN);
                            netBalance = freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN);
                            if (!isSelect) {
                                myDialog.setMsg("预估AP挖矿手续费...");
                                getPledgeEnergy();
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "获取账户信息错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void getTxasBalance() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        BalanceJavaScriptInterface javaScriptInterface = new BalanceJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/selectBalance.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                selectBalanceWeb();
            }
        });
    }

    private void selectBalanceWeb() {
        String url = null;
        try {
            url = "javascript:selectBalance('" + txasContractAddress + "','" + tronAddress + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
            isClick = false;
        }
        mWebView.loadUrl(url);
    }

    private class BalanceJavaScriptInterface {
        @JavascriptInterface
        public void selectFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isClick = false;
                    DialogUtils.closeDialog(myDialog);
                    if (str.contains("错误")) {
                        ToastUtil.showShort(mContext, str);
                    } else {
                        pledgeTotal = new BigDecimal(str).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                        mTextView_ap.setText(new BigDecimal(str).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString() + " AP");
                        if (pledgeTotal.floatValue() == 0f) {
                            mTextView_apMessage.setVisibility(View.GONE);
                        } else {
                            mTextView_apMessage.setVisibility(View.VISIBLE);
                        }
                        if (!isSelect) {
                            if (pledgeTotal.floatValue() == 0f) {
                                ToastUtil.showShort(mContext, "暂无可质押额度，请先锁仓XAS获取AP凭证");

                                isSetLock = true;
                                if (!spUtils.getBoolean("isDeal", false)) {
                                    dialogPassword(0);
                                } else if (spUtils1.getString("childAddress", "").isEmpty()) {
                                    Intent ia = new Intent(mContext, AccountManagementActivity.class);
                                    ia.putExtra("tradePassword", tradePassword);
                                    startActivityForResult(ia, 1);
                                    isClick = false;
                                } else if (!isTron) {
                                    myDialog = DialogUtils.createLoadingDialog(mContext, "获取TRON绑定信息...");
                                    getTronAddress(0);
                                } else if (spUtils.getString("tronAddress", "").isEmpty()) {
                                    isBinding = true;
                                    if (tradePassword.isEmpty()) {
                                        dialogAuthentication();
                                    } else {
                                        bindingAddressDialog();
                                    }
                                } else {
                                    myDialog = DialogUtils.createLoadingDialog(mContext, "查询链上账户可用XAS");
                                    getBalance();
                                }

                            } else {
                                if (netBalance.floatValue() == 0f) {
                                    myDialog = DialogUtils.createLoadingDialog(mContext, "获取账户信息...");
                                    getAddressInfo(null, isSelect);
                                } else {
                                    myDialog = DialogUtils.createLoadingDialog(mContext, "预估AP挖矿手续费...");
                                    getPledgeEnergy();
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private void getPledgeEnergy() {
        OkHttpClient.initGet(Http.getPledgeEnergy).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        energy_usage_total = new BigDecimal("0");
                        net_usage = new BigDecimal("0");
                        ToastUtil.showShort(mContext, "网络错误，预估AP挖矿手续费失败");
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
                            DialogUtils.closeDialog(myDialog);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                energy_usage_total = new BigDecimal(jsonObject.getJSONObject("data").getString("energy_usage_total"));
                                net_usage = new BigDecimal(jsonObject.getJSONObject("data").getString("net_usage"));

                                BigDecimal burningTrx = new BigDecimal("0");
                                if ((dialog != null && dialog.isShowing()) || (dialog1 != null && dialog1.isShowing())) {
                                    return;
                                }
                                if (jsonObject.getJSONObject("data").getBoolean("isEnoughEnergy")) {
                                    if (netBalance.floatValue() < net_usage.floatValue()) {
                                        burningTrx = burningTrx.add(net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_DOWN)).setScale(5, BigDecimal.ROUND_DOWN);
                                    }
                                    if (trxBalance.floatValue() >= burningTrx.floatValue()) {
                                        pledgeMiningDialog(true);
                                    } else {
                                        dialogTrx(0, burningTrx, true);
                                    }
                                } else {
                                    if (netBalance.floatValue() < net_usage.floatValue()) {
                                        burningTrx = burningTrx.add(net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP)).setScale(5, BigDecimal.ROUND_DOWN);
                                    }
                                    if (energyBalance.floatValue() < energy_usage_total.floatValue()) {
                                        burningTrx = burningTrx.add(energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP)).setScale(5, BigDecimal.ROUND_DOWN);
                                    }
                                    if (trxBalance.floatValue() >= burningTrx.floatValue()) {
                                        pledgeMiningDialog(false);
                                    } else {
                                        dialogTrx(0, burningTrx, false);
                                    }
                                }
                            } else {
                                energy_usage_total = new BigDecimal("0");
                                net_usage = new BigDecimal("0");
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            energy_usage_total = new BigDecimal("0");
                            net_usage = new BigDecimal("0");
                            ToastUtil.showShort(mContext, "预估AP挖矿手续费错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void getWithdrawEnergy() {
        myDialog = DialogUtils.createLoadingDialog(mContext, "预估收矿手续费...");
        OkHttpClient.initGet(Http.getWithdrawEnergy).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        energy_usage_total = new BigDecimal("0");
                        net_usage = new BigDecimal("0");
                        ToastUtil.showShort(mContext, "网络错误，预估收矿手续费失败");
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
                            DialogUtils.closeDialog(myDialog);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                energy_usage_total = new BigDecimal(jsonObject.getJSONObject("data").getString("energy_usage_total"));
                                net_usage = new BigDecimal(jsonObject.getJSONObject("data").getString("net_usage"));
                                BigDecimal burningTrx = new BigDecimal("0");
                                if ((dialog != null && dialog.isShowing()) || (dialog1 != null && dialog1.isShowing())) {
                                    return;
                                }
                                if (netBalance.floatValue() < net_usage.floatValue()) {
                                    burningTrx = burningTrx.add(net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (energyBalance.floatValue() < energy_usage_total.floatValue()) {
                                    burningTrx = burningTrx.add(energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (trxBalance.floatValue() > burningTrx.floatValue()) {
                                    payDialog1();
                                } else {
                                    dialogTrx(1, burningTrx, false);
                                }
                            } else {
                                energy_usage_total = new BigDecimal("0");
                                net_usage = new BigDecimal("0");
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            energy_usage_total = new BigDecimal("0");
                            net_usage = new BigDecimal("0");
                            ToastUtil.showShort(mContext, "预估收矿手续费错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void dialogTrx(int type, BigDecimal burningTrx, boolean isEnoughEnergy) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_tron_trx_layout, null);
        TextView mTextView_msg = view.findViewById(R.id.mTextView_msg);
        TextView mTextView_false = view.findViewById(R.id.mTextView_false);
        TextView mTextView_true = view.findViewById(R.id.mTextView_true);

        String frozenTrx = energy_usage_total.divide(frozenEnergy, BigDecimal.ROUND_HALF_UP, 3).setScale(3, BigDecimal.ROUND_HALF_UP).toString();

        mTextView_msg.setText("当前操作需要" + energy_usage_total.setScale(0,BigDecimal.ROUND_DOWN).toString() + "能量（能量可预先冻结大约" + frozenTrx + "个TRX长期供能）能量不足需燃烧大约" + burningTrx.toString() + "个TRX，您的TRX余额不足建议您准备足够能量或TRX");

        mTextView_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        mTextView_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                if (type == 0) {
                    pledgeMiningDialog(isEnoughEnergy);
                } else if (type == 1) {
                    payDialog1();
                } else if (type == 2) {
                    if (allowance.floatValue() > 0f && allowance.floatValue() >= new BigDecimal(maxTokens).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP).floatValue()) {
                        myDialog = DialogUtils.createLoadingDialog(mContext, "提供流动性...");
                        addLiquidity();
                    } else {
                        myDialog = DialogUtils.createLoadingDialog(mContext, "授权ASO...");
                        approve();
                    }
                } else if (type == 3) {
                    lpPledgeMiningDialog();
                } else if (type == 4) {
                    payDialogLp();
                } else if (type == 5) {
                    deleteLpDialog();
                } else if (type == 6) {
                    revokeLpDialog();
                }
            }
        });
        dialog1 = new Dialog(mContext, R.style.inputDialog);
        dialog1.setContentView(view);
        Window dialogWindow = dialog1.getWindow();
        if (dialogWindow == null) {
            return;
        }
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.1f;
        lp.width = getResources().getDisplayMetrics().widthPixels - OtherUtils.dp2px(mContext, 60);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog1.show();
    }

    private void getTotalReward() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("owner_address", tronAddress);
            jsonObject.put("contract_address", pountContractAddress);
            jsonObject.put("function_selector", "selectTotalGain()");
            jsonObject.put("visible", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.triggerConstantContract, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(mContext, "网络错误，查询已收矿失败");
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
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (!result.contains("FAILED")) {
                                totalRewardBig = new BigDecimal(TronUtils.toBigInt(jsonObject1.getJSONArray("constant_result").getString(0))).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                mTextView_reward.setText(totalRewardBig.toString() + " ASO");
                                if (totalRewardBig.floatValue() != 0f || pledgedBig.floatValue() != 0f || rewardBig.floatValue() != 0f) {
                                    mRelativeLayout_mining.setVisibility(View.VISIBLE);
                                } else {
                                    mRelativeLayout_mining.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        });
    }

    private void getReward() {
        isPause = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("owner_address", tronAddress);
            jsonObject.put("contract_address", pountContractAddress);
            jsonObject.put("function_selector", "selectGain()");
            jsonObject.put("visible", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.triggerConstantContract, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isPause = false;
                        ToastUtil.showShort(mContext, "网络错误，查询待收取失败");
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
                            isPause = false;
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (!result.contains("FAILED")) {
                                rewardBig = new BigDecimal(TronUtils.toBigInt(jsonObject1.getJSONArray("constant_result").getString(0))).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                mTextView_btn_reward.setText(rewardBig.toString() + " ASO 待收取");
                                if (rewardBig.floatValue() != 0f || pledgedBig.floatValue() != 0f || totalRewardBig.floatValue() != 0f) {
                                    mRelativeLayout_mining.setVisibility(View.VISIBLE);
                                } else {
                                    mRelativeLayout_mining.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        });
    }

    private void getPledged() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("owner_address", tronAddress);
            jsonObject.put("contract_address", pountContractAddress);
            jsonObject.put("function_selector", "selectPrincipal()");
            jsonObject.put("visible", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.triggerConstantContract, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(mContext, "网络错误，查询已质押失败");
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
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (!result.contains("FAILED")) {
                                pledgedBig = new BigDecimal(TronUtils.toBigInt(jsonObject1.getJSONArray("constant_result").getString(0))).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                mTextView_pledged.setText(pledgedBig.toString());
                                if (pledgedBig.floatValue() != 0f || rewardBig.floatValue() != 0f || totalRewardBig.floatValue() != 0f) {
                                    mRelativeLayout_mining.setVisibility(View.VISIBLE);
                                    spUtils1.putBoolean("isPledge", true);
                                } else {
                                    mRelativeLayout_mining.setVisibility(View.GONE);
                                }
                                if (getRewardsPerBlock != null && getRewardsPerBlock.floatValue() == 0f) {
                                    mTextView_mining.setText("AP挖矿已结束");
                                    mLottieAnimationView_mining.cancelAnimation();
                                    mLottieAnimationView_mining.setVisibility(View.GONE);
                                    isAnimation = false;
                                } else {
                                    if (pledgedBig.floatValue() != 0f && !isAnimation) {
                                        mLottieAnimationView_mining.setVisibility(View.VISIBLE);
                                        mTextView_mining.setText("正在挖矿中（AP挖矿）");
                                        mLottieAnimationView_mining.setAnimation(R.raw.wakuang);
                                        mLottieAnimationView_mining.setRepeatCount(-1);
                                        mLottieAnimationView_mining.playAnimation();
                                        isAnimation = true;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        });
    }

    private void getDefiInfo() {
        OkHttpClient.initGet(Http.getDefiInfo).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(mContext, "网络错误，获取矿池信息失败");
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
                            if (jsonObject.getInt("code") == 1) {
                                Content.apStartTime = jsonObject.getJSONObject("data").getString("apStartTime");
                                try {
                                    if (System.currentTimeMillis() + Content.timePoor < OtherUtils.dateToStamp(Content.apStartTime)) {
                                        mLinearLayout_ap.setVisibility(View.GONE);
                                        mTextView_ap_message.setVisibility(View.VISIBLE);
                                        mTextView_ap.setVisibility(View.GONE);
                                        mTextView_ap_message.setText("开始时间：" + jsonObject.getJSONObject("data").getString("apStartTime"));
                                    } else {
                                        mLinearLayout_ap.setVisibility(View.VISIBLE);
                                        mTextView_ap_message.setVisibility(View.GONE);
                                        mTextView_ap.setVisibility(View.VISIBLE);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                getRewardsPerBlock = new BigDecimal(jsonObject.getJSONObject("data").getString("getRewardsPerBlock")).setScale(3, BigDecimal.ROUND_DOWN);
                                mTextView_startRewardBlock.setText(new BigDecimal(jsonObject.getJSONObject("data").getString("startRewardBlock")).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                mTextView_getRewardsPerBlock.setText(new BigDecimal(jsonObject.getJSONObject("data").getString("getRewardsPerBlock")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                mTextView_getRewardsPerBlock_lp.setText(new BigDecimal(jsonObject.getJSONObject("data").getString("lpRewardsPerBlock")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                lpStartTime = jsonObject.getJSONObject("data").getString("lpStartTime");
                                if (getRewardsPerBlock.floatValue() == 0f) {
                                    mTextView_mining.setText("AP挖矿已结束");
                                    mLottieAnimationView_mining.cancelAnimation();
                                    mLottieAnimationView_mining.setVisibility(View.GONE);
                                    isAnimation = false;
                                }
                                try {
                                    if (System.currentTimeMillis() + Content.timePoor < OtherUtils.dateToStamp(lpStartTime)) {
                                        mLinearLayout_lp.setVisibility(View.GONE);
                                        mTextView_lp_message.setVisibility(View.VISIBLE);
                                        mTextView_lp.setVisibility(View.GONE);
                                        mTextView_lp_message.setText("开始时间：" + lpStartTime);
                                    } else {
                                        mLinearLayout_lp.setVisibility(View.VISIBLE);
                                        mTextView_lp_message.setVisibility(View.GONE);
                                        mTextView_lp.setVisibility(View.VISIBLE);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
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

    private void pledgeMiningDialog(boolean isEnoughEnergy) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_pledge_mining_layout, null);
        TextView mTextView_quota = v.findViewById(R.id.mTextView_quota);
        TextView mTextView_net_text = v.findViewById(R.id.mTextView_net_text);
        TextView mTextView_net = v.findViewById(R.id.mTextView_net);
        TextView mTextView_energy_text = v.findViewById(R.id.mTextView_energy_text);
        TextView mTextView_energy = v.findViewById(R.id.mTextView_energy);
        TextView mTextView_btn_cancel = v.findViewById(R.id.mTextView_btn_cancel);
        LinearLayout mLinearLayout_btn_subscribe = v.findViewById(R.id.mLinearLayout_btn_subscribe);
        TextView mTextView_balance = v.findViewById(R.id.mTextView_balance);

        mTextView_quota.setText(new DecimalFormat("0.000").format(pledgeTotal));

        if (netBalance.floatValue() < net_usage.floatValue()) {
            mTextView_net_text.setText("预计燃烧 " + net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取带宽");
            mTextView_net.setText("=" + net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToNet).setScale(0, BigDecimal.ROUND_DOWN).toString());
        } else {
            mTextView_net_text.setText("预计消耗带宽");
            mTextView_net.setText("≈" + net_usage.setScale(0, BigDecimal.ROUND_DOWN).toString());
        }

        if (!isEnoughEnergy) {
            if (energyBalance.floatValue() < energy_usage_total.floatValue()) {
                mTextView_energy_text.setText("预计燃烧 " + energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取能量");
                mTextView_energy.setText("=" + energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
            } else {
                mTextView_energy_text.setText("预计消耗能量");
                mTextView_energy.setText("≈" + energy_usage_total.setScale(0, BigDecimal.ROUND_DOWN).toString());
            }
        }

        mTextView_balance.setText(pledgeTotal.setScale(3, BigDecimal.ROUND_DOWN).toString() + " AP");

        mTextView_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mLinearLayout_btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextView_quota.getText().toString().isEmpty() || Float.parseFloat(mTextView_quota.getText().toString()) == 0f) {
                    ToastUtil.showShort(mContext, "质押额度不能为0");
                    return;
                }
                quota = Double.parseDouble(mTextView_quota.getText().toString());
                payDialog();
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

    private void payDialog() {
        View v1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_pledge_mining_trade_password_layout, null);
        TextView mTextView_currency = v1.findViewById(R.id.mTextView_currency);
        TextView mTextView_address = v1.findViewById(R.id.mTextView_address);
        TextView mEditText_password = v1.findViewById(R.id.mEditText_password);
        mTextView_currency.setText(new DecimalFormat("0.000").format(quota) + " AP");
        mTextView_address.setText(tronAddress);
        v1.findViewById(R.id.mImageView_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        v1.findViewById(R.id.mTextView_btn_pledge_mining).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradePassword = mEditText_password.getText().toString();
                if (tradePassword.isEmpty()) {
                    ToastUtil.showShort(mContext, "交易密码不能为空");
                    return;
                }
                hintKeyBoard(v);
                dialog1.dismiss();
                dialog.dismiss();
                myDialog = DialogUtils.createLoadingDialog(mContext, "质押中...");
                pledgeMining();
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

    private void pledgeMining() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        PMJavaScriptInterface javaScriptInterface = new PMJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/pledgeMining.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                PMJsParseMainAssetTra();
            }
        });
    }

    private void PMJsParseMainAssetTra() {
        String url = null;
        BigDecimal quotaB = new BigDecimal(quota);
        try {
            url = "javascript:transfer('" + AESUtil.decrypt(privateKey, tradePassword) + "','" + txasContractAddress + "','" + pountContractAddress + "','" + quotaB.multiply(decimals).toString() + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class PMJavaScriptInterface {
        @JavascriptInterface
        public void transferFunction(String str) {
            if (str.contains("错误")) {
                DialogUtils.closeDialog(myDialog);
                ToastUtil.showShort(mContext, str);
            } else {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.setMsg("链上验证结果中...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                    number = 1;
                                    getTransactionInfoById(str);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
            }
        }
    }

    private void getTransactionInfoById(String TransactionId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("value", TransactionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.getTransactionInfoById, requestBodyPost).enqueue(new Callback() {
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
                                        getTransactionInfoById(TransactionId);
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
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (result.length() > 2) {
                                if (jsonObject1.getJSONObject("receipt").getString("result").equals("SUCCESS")) {
                                    uploadPledgeRecords(TransactionId);
                                } else {
                                    DialogUtils.closeDialog(myDialog);
                                    ToastUtil.showShort(mContext, "AP挖矿失败：" + jsonObject1.getJSONObject("receipt").getString("result"));
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
                                                getTransactionInfoById(TransactionId);
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

    private void uploadPledgeRecords(String hash) {
        OkHttpClient.initGet(Http.pledgeDo + "?tronAddress=" + tronAddress + "&hash=" + hash).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，AP挖矿失败");
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
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                ToastUtil.showShort(mContext, "AP挖矿成功");

                                pledgeInfo(null);
                                EventBus.getDefault().postSticky(new EventMessageBean(20, new HashMap()));
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "AP挖矿错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void payDialog1() {
        View v1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_pledge_mining_private_key_layout, null);
        TextView mTextView_address = v1.findViewById(R.id.mTextView_address);
        TextView mEditText_password = v1.findViewById(R.id.mEditText_password);
        TextView mTextView_net_text = v1.findViewById(R.id.mTextView_net_text);
        TextView mTextView_net = v1.findViewById(R.id.mTextView_net);
        TextView mTextView_energy_text = v1.findViewById(R.id.mTextView_energy_text);
        TextView mTextView_energy = v1.findViewById(R.id.mTextView_energy);
        mTextView_address.setText(tronAddress);

        if (netBalance.floatValue() < net_usage.floatValue()) {
            mTextView_net_text.setText("预计燃烧 " + net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取带宽");
            mTextView_net.setText("=" + net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToNet).setScale(0, BigDecimal.ROUND_DOWN).toString());
        } else {
            mTextView_net_text.setText("预计消耗带宽");
            mTextView_net.setText("≈" + net_usage.setScale(0, BigDecimal.ROUND_DOWN).toString());
        }
        if (energyBalance.floatValue() < energy_usage_total.floatValue()) {
            mTextView_energy_text.setText("预计燃烧 " + energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取能量");
            mTextView_energy.setText("=" + energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
        } else {
            mTextView_energy_text.setText("预计消耗能量");
            mTextView_energy.setText("≈" + energy_usage_total.setScale(0, BigDecimal.ROUND_DOWN).toString());
        }

        v1.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        v1.findViewById(R.id.mTextView_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradePassword = mEditText_password.getText().toString();
                if (tradePassword.isEmpty()) {
                    ToastUtil.showShort(mContext, "交易密码不能为空");
                    return;
                }
                hintKeyBoard(v);
                mEditText_password.setText("");
                myDialog = DialogUtils.createLoadingDialog(mContext, "收矿中...");
                receiveRewards();
            }
        });
        dialog = new Dialog(mContext, R.style.LeftDialogStyle);
        dialog.setContentView(v1);
        dialog.setCancelable(false);
        Window dialogWindow1 = dialog.getWindow();
        if (dialogWindow1 == null) {
            return;
        }
        dialogWindow1.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
        lp1.dimAmount = 0.1f;
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow1.setAttributes(lp1);
        dialog.show();
    }

    private void receiveRewards() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        RewJavaScriptInterface javaScriptInterface = new RewJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/receiveRewards.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                JsRew();
            }
        });
    }

    private void JsRew() {
        String url = null;
        try {
            url = "javascript:reward('" + AESUtil.decrypt(privateKey, tradePassword) + "','" + pountContractAddress + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class RewJavaScriptInterface {
        @JavascriptInterface
        public void startFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, str);
                    } else {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myDialog.setMsg("链上验证结果中...");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(5000);
                                            number = 1;
                                            getTransactionInfoByIdReward(str);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        });
                    }
                }
            });
        }
    }

    private void getTransactionInfoByIdReward(String TransactionId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("value", TransactionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.getTransactionInfoById, requestBodyPost).enqueue(new Callback() {
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
                                        getTransactionInfoByIdReward(TransactionId);
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
                                JSONObject jsonObject1 = new JSONObject(result);
                                if (jsonObject1.getJSONObject("receipt").getString("result").equals("SUCCESS")) {
                                    dialog.dismiss();
                                    getReward();
                                    getTotalReward();
                                    takeMineral(TransactionId);
                                } else {
                                    DialogUtils.closeDialog(myDialog);
                                    ToastUtil.showShort(mContext, "收矿失败：" + jsonObject1.getJSONObject("receipt").getString("result"));
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
                                                getTransactionInfoByIdReward(TransactionId);
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

    private void takeMineral(String TransactionId) {
        OkHttpClient.initGet(Http.takeMineral + "?hash=" + TransactionId + "&tronAddress=" + tronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，上传收矿记录失败");
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
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (jsonObject1.getInt("code") == 1) {
                                ToastUtil.showShort(mContext, "收矿成功");
                            } else {
                                ToastUtil.showShort(mContext, jsonObject1.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "上传收矿记录失败：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void dialogPassword(int type) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_trade_password_setting_layout, null);
        EditText mEditText_password = view.findViewById(R.id.mEditText_password);
        EditText mEditText_confirmPassword = view.findViewById(R.id.mEditText_confirmPassword);
        EditText mEditText_verification = view.findViewById(R.id.mEditText_verification);
        TextView mTextView_getCode = view.findViewById(R.id.mTextView_getCode);
        TextView mTextView_err = view.findViewById(R.id.mTextView_err);

        String regular = "^(?![^a-zA-Z]+$)(?!\\D+$).{6}$";
        mEditText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = mEditText_password.getText().toString();
                if (!password.matches(regular)) {
                    mTextView_err.setVisibility(View.VISIBLE);
                    isOk = false;
                } else {
                    mTextView_err.setVisibility(View.INVISIBLE);
                    isOk = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTextView_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText_password.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "请先填写交易密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                hintKeyBoard(v);
                getCode(mTextView_getCode);
            }
        });

        view.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.mTextView_btn_determine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKeyBoard(v);
                String password = mEditText_password.getText().toString();
                String verificationCode = mEditText_verification.getText().toString();

                if (!isOk) {
                    ToastUtil.showShort(mContext, "交易密码不符合规则");
                    return;
                }
                if (mEditText_confirmPassword.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "确认交易密码不能为空");
                    return;
                }
                if (!mEditText_password.getText().toString().equals(mEditText_confirmPassword.getText().toString())) {
                    ToastUtil.showShort(mContext, "交易密码和确认交易密码不一致");
                    return;
                }
                if (verificationCode.isEmpty()) {
                    ToastUtil.showShort(mContext, "验证码不能为空");
                    return;
                }
                myDialog = DialogUtils.createLoadingDialog(mContext, "提交中...");
                Map postmap = new HashMap();
                postmap.put("code", verificationCode);
                try {
                    postmap.put("password", AESUtil.encrypt(spUtils.getString("phone"), password));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
                OkHttpClient.initPost(Http.realPwd, requestBodyPost).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(mContext, "网络错误，交易密码设置失败");
                                DialogUtils.closeDialog(myDialog);
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
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getInt("code") == 1) {
                                        ToastUtil.showShort(mContext, "交易密码设置成功");
                                        tradePassword = password;
                                        spUtils.putBoolean("isDeal", true);
                                        if (countDownTimer != null) {
                                            countDownTimer.cancel();
                                            countDownTimer = null;
                                        }
                                        isClick = false;
                                        dialog.dismiss();
                                        if (type == 0) {
                                            if (spUtils1.getString("childAddress", "").isEmpty()) {
                                                Intent ia = new Intent(mContext, AccountManagementActivity.class);
                                                ia.putExtra("tradePassword", tradePassword);
                                                startActivityForResult(ia, 1);
                                            } else if (!isTron) {
                                                myDialog = DialogUtils.createLoadingDialog(mContext, "获取TRON绑定信息...");
                                                getTronAddress(0);
                                            } else if (spUtils.getString("tronAddress", "").isEmpty()) {
                                                bindingAddressDialog();
                                            } else {
                                                myDialog = DialogUtils.createLoadingDialog(mContext, "查询链上账户可用XAS");
                                                getBalance();
                                            }
                                        } else if (type == 1) {
                                            if (!isTron) {
                                                myDialog = DialogUtils.createLoadingDialog(mContext, "获取TRON绑定信息...");
                                                getTronAddress(3);
                                            } else if (spUtils.getString("tronAddress", "").isEmpty()) {
                                                isBinding = true;
                                                bindingAddressDialog();
                                            } else if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                                                isBinding = false;
                                                importAddressDialog();
                                            } else {
                                                isSelect = false;
                                                myDialog = DialogUtils.createLoadingDialog(mContext, "正在查询链上可质押AP...");
                                                getTxasBalance();
                                            }
                                        }
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

    private void getCode(TextView mTextView_getCode) {
        countDownTimer = new CountDownTimer(90000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTextView_getCode.setEnabled(false);
                mTextView_getCode.setText((Math.round((double) millisUntilFinished / 1000) - 1) + "秒");
                mTextView_getCode.setTextColor(getResources().getColor(R.color.text_bg_gray));
            }

            @Override
            public void onFinish() {
                mTextView_getCode.setEnabled(true);
                mTextView_getCode.setText("获取验证码");
                mTextView_getCode.setTextColor(getResources().getColor(R.color.text_bg));
            }
        }.start();

        OkHttpClient.initGet(Http.getCode + "?operationCode=3").enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(mContext, "网络错误，验证码获取失败");
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
                                ToastUtil.showShort(mContext, "验证码已发送至您的手机，请注意查收");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && !spUtils1.getString("childAddress", "").isEmpty()) {
            tradePassword = data.getStringExtra("tradePassword");
            if (!isTron) {
                myDialog = DialogUtils.createLoadingDialog(mContext, "获取TRON绑定信息...");
                getTronAddress(0);
            } else if (spUtils.getString("tronAddress", "").isEmpty()) {
                isBinding = true;
                bindingAddressDialog();
            } else {
                myDialog = DialogUtils.createLoadingDialog(mContext, "查询链上账户可用XAS");
                getBalance();
            }
        } else {
            ToastUtil.showShort(mContext, "您还没有添加账户，您还没有添加账户，请先到账户管理中导入或创建账户");
        }
    }

    private void getLockRate() {
        OkHttpClient.initGet(Http.getLockRate).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
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
                            DialogUtils.closeDialog(myDialog);
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

    private void getBalance() {
        OkHttpClient.initGet(Http.getAddressInfo + "?address=" + spUtils1.getString("childAddress")).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，查询可用XAS失败");
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
                            if (jsonObject.getInt("code") == 1) {
                                isSecondSecret = jsonObject.getJSONObject("data").getBoolean("isHaveSecond");
                                totalBalance = new BigDecimal(jsonObject.getJSONObject("data").getString("totalBalance")).setScale(3, BigDecimal.ROUND_DOWN).toString();
                                lockedBalance = new BigDecimal(jsonObject.getJSONObject("data").getString("lockedBalance")).setScale(3, BigDecimal.ROUND_DOWN).toString();
                                usableBalance = new BigDecimal(jsonObject.getJSONObject("data").getString("usableBalance")).setScale(3, BigDecimal.ROUND_DOWN).toString();
                                lockHeight = jsonObject.getJSONObject("data").getLong("lockHeight");
                                if (lockHeight == 0l) {
                                    myDialog.setMsg("获取置换比例...");
                                    getLockRate();
                                } else {
                                    myDialog.setMsg("获取区块高度...");
                                    getHeight();
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

    private void getTronAddress(int type) {
        OkHttpClient.initGet(Http.getTronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
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
                                        isBinding = true;
                                        if (tradePassword.isEmpty()) {
                                            dialogAuthentication();
                                        } else {
                                            bindingAddressDialog();
                                        }
                                    } else {
                                        myDialog.setMsg("查询链上账户可用XAS");
                                        getBalance();
                                    }
                                } else if (type == 1) {
                                    if (!spUtils.getString("tronAddress", "").isEmpty()) {
                                        if (!spUtils1.getString("tronChildAddress", "").isEmpty()) {
                                            tronAddress = spUtils1.getString("tronChildAddress", "");
                                        } else {
                                            tronAddress = spUtils.getString("tronAddress");
                                        }
                                    }
                                    pledgeInfo(null);
                                } else if (type == 3) {
                                    if (spUtils.getString("tronAddress", "").isEmpty()) {
                                        isBinding = true;
                                        if (tradePassword.isEmpty()) {
                                            dialogAuthentication();
                                        } else {
                                            bindingAddressDialog();
                                        }
                                    } else if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                                        isBinding = false;
                                        if (tradePassword.isEmpty()) {
                                            dialogAuthentication();
                                        } else {
                                            importAddressDialog();
                                        }
                                    } else {
                                        isSelect = false;
                                        myDialog.setMsg("正在查询链上可质押AP...");
                                        getTxasBalance();
                                    }
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
                                if (isBinding) {
                                    bindingAddressDialog();
                                } else {
                                    importAddressDialog();
                                }
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

    private void bindingAddressDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_bind_tron_layout, null);
        EditText mEditText_privateKey = v.findViewById(R.id.mEditText_privateKey);

        v.findViewById(R.id.mLinearLayout_creatorAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                myDialog = DialogUtils.createLoadingDialog(mContext, "新建中...");
                creatorAddress();
            }
        });
        v.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.mTextView_btn_binding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                if (mEditText_privateKey.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "私钥不能为空");
                    return;
                }
                myDialog = DialogUtils.createLoadingDialog(mContext, "绑定中...");
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
            DialogUtils.closeDialog(myDialog);
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
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "私钥不正确");
                    } else {
                        if (!isBinding) {
                            List<TronDBHelperBean> beanList = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                            for (int i = 0; i < beanList.size(); i++) {
                                if (str.equals(beanList.get(i).getAddress())) {
                                    ToastUtil.showShort(mContext, "该账户已导入，请勿重复导入");
                                    DialogUtils.closeDialog(myDialog);
                                    return;
                                }
                            }
                        }
                        tronAddress = str;
                        if (isBinding) {
                            privateKey = editPrivateKey;
                            bindTronAddress(tronAddress, editPrivateKey, false);
                        } else {
                            try {
                                privateKey = editPrivateKey;
                                TronSQLUtils.AddSql(mContext, addressNote, tronAddress, AESUtil.encrypt(editPrivateKey, tradePassword), spUtils.getString("phone") + "@");
                                dialog.dismiss();
                                ToastUtil.showShort(mContext, "导入成功");
                                spUtils1.putString("tronChildAddress", tronAddress);
                                isSelect = false;
                                myDialog.setMsg("正在查询链上可质押AP...");
                                getTxasBalance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
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
                        DialogUtils.closeDialog(myDialog);
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
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                dialog.dismiss();
                                ToastUtil.showShort(mContext, "绑定成功");
                                spUtils.putString("tronAddress", tronAddress);
                                spUtils1.putString("tronChildAddress", tronAddress);
                                pledgeInfo(null);
                                EventBus.getDefault().postSticky(new EventMessageBean(24, new HashMap()));
                                try {
                                    TronSQLUtils.AddSql(mContext, "TRON", tronAddress, AESUtil.encrypt(tronPrivateKey, tradePassword), spUtils.getString("phone") + "@");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    DialogUtils.closeDialog(myDialog);
                                }
                                if (!isCreator) {
                                    if (isSetLock) {
                                        myDialog.setMsg("查询链上账户可用XAS");
                                        getBalance();
                                    } else {
                                        isSelect = false;
                                        myDialog.setMsg("正在查询链上可质押AP...");
                                        getTxasBalance();
                                    }
                                } else {
                                    DialogUtils.closeDialog(myDialog);
                                    creatorAddressDialog(isSetLock);
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
                mTextView_text1.setText("XAS置换AP凭证规则：由锁仓时间决定，3个月 1:" + xasLockQuarter + " ，6个月 1:" + xasLockHalfYear + " ，1年 1:" + xasLockOneYear);
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
                List<DBHelperBean> beanList = SQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                for (int i = 0; i < beanList.size(); i++) {
                    if (spUtils1.getString("childAddress", "").equals(beanList.get(i).getAddress())) {
                        secret = beanList.get(i).getSecret();
                    }
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
                        tronAddress = creatorAddress;
                        privateKey = jsonObject.getString("privateKey");
                        spUtils.putString("tronAddress", creatorAddress);
                        dialog.dismiss();
                        myDialog.setMsg("绑定中...");
                        bindTronAddress(creatorAddress, privateKey, true);
                    } catch (JSONException e) {
                        DialogUtils.closeDialog(myDialog);
                    }
                }
            });
        }
    }

    private void creatorAddressDialog(boolean isSetLock) {
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
                dialogCreator(isSetLock);
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

    private void dialogCreator(boolean isSetLock) {
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
                if (isSetLock) {
                    myDialog = DialogUtils.createLoadingDialog(mContext, "查询链上账户可用XAS");
                    getBalance();
                } else {
                    isSelect = false;
                    myDialog = DialogUtils.createLoadingDialog(mContext, "正在查询链上可质押AP...");
                    getTxasBalance();
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
                myDialog = DialogUtils.createLoadingDialog(mContext, "锁仓中...");
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
                        DialogUtils.closeDialog(myDialog);
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
                                myDialog.setMsg("获取置换比例...");
                                getLockRate();
                            } else {
                                DialogUtils.closeDialog(myDialog);
                                ToastUtil.showShort(mContext, "获取区块高度失败，请稍候再试");
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
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
                        DialogUtils.closeDialog(myDialog);
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
                                DialogUtils.closeDialog(myDialog);
                                ToastUtil.showShort(mContext, "获取区块高度失败，请稍候再试");
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
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
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
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
                        DialogUtils.closeDialog(myDialog);
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
                                myDialog.setMsg("锁仓成功，正在置换AP凭证...");
                                lockedPosition(jsonObject.getString("transactionId"));
                            } else {
                                DialogUtils.closeDialog(myDialog);
                                if (jsonObject.getString("error").contains("Invalid second signature")) {
                                    ToastUtil.showShort(mContext, "二级密码错误");
                                } else if (jsonObject.getString("error").contains("Invalid lock height")) {
                                    ToastUtil.showShort(mContext, "锁仓时间早于上一次锁仓截止时间的后三十天");
                                } else {
                                    ToastUtil.showShort(mContext, "锁仓失败，请稍候再试");
                                }
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
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
                        DialogUtils.closeDialog(myDialog);
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
                            DialogUtils.closeDialog(myDialog);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                ToastUtil.showShort(mContext, "锁仓置换成功");
                                isSelect = true;
                                getTxasBalance();
                                Map map = new HashMap();
                                map.put("isShow", false);
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

    private void importAddressDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_import_tron_layout, null);
        EditText mEditText_note = v.findViewById(R.id.mEditText_note);
        EditText mEditText_privateKey = v.findViewById(R.id.mEditText_privateKey);
        LinearLayout mLinearLayout_creatorAddress = v.findViewById(R.id.mLinearLayout_creatorAddress);
        mLinearLayout_creatorAddress.setVisibility(View.GONE);

        v.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.mTextView_btn_import).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKeyBoard(v);
                isClick = false;
                if (mEditText_note.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "账户备注不能为空");
                    return;
                }
                List<TronDBHelperBean> beanList1 = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                for (int i = 0; i < beanList1.size(); i++) {
                    if (mEditText_note.getText().toString().equals(beanList1.get(i).getAccount())) {
                        ToastUtil.showShort(mContext, "账户备注不能重复");
                        return;
                    }
                }
                if (mEditText_privateKey.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "私钥不能为空");
                    return;
                }
                myDialog = DialogUtils.createLoadingDialog(mContext, "导入中...");
                addressNote = mEditText_note.getText().toString();
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

    private void showButtonDialog() {
        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_muen_liquidity_layout, null);
        inflate.findViewById(R.id.mTextView_btn_getLp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trxBalance == null) {
                    ToastUtil.showShort(mContext, "请稍候或刷新再试");
                    return;
                }
                if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                    ToastUtil.showShort(mContext, "请先左滑至Tron钱包页面导入账号");
                    return;
                }
                if (exTokenBalace == null) {
                    ToastUtil.showShort(mContext, "正在加载LP矿池信息，请稍候或刷新再试");
                    return;
                }
                dialog.dismiss();
                myDialog = DialogUtils.createLoadingDialog(mContext, "预估手续费...");
                getAddLiquidityEnergy(1);
            }
        });

        inflate.findViewById(R.id.mTextView_btn_deleteLp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trxBalance == null) {
                    ToastUtil.showShort(mContext, "请稍候或刷新再试");
                    return;
                }
                if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                    ToastUtil.showShort(mContext, "请先左滑至Tron钱包页面导入账号");
                    return;
                }
                if (exTokenBalace == null) {
                    ToastUtil.showShort(mContext, "正在加载LP矿池信息，请稍候或刷新再试");
                    return;
                }
                if (lpBalance.floatValue() == 0f) {
                    ToastUtil.showShort(mContext, "暂无流动性可移除");
                    return;
                }
                dialog.dismiss();
                myDialog = DialogUtils.createLoadingDialog(mContext, "预估手续费...");
                getAddLiquidityEnergy(2);
            }
        });

        inflate.findViewById(R.id.mTextView_btn_revokeLp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trxBalance == null) {
                    ToastUtil.showShort(mContext, "请稍候或刷新再试");
                    return;
                }
                if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                    ToastUtil.showShort(mContext, "请先左滑至Tron钱包页面导入账号");
                    return;
                }
                if (pledgedLpBig.floatValue() == 0f) {
                    ToastUtil.showShort(mContext, "暂无LP可撤销挖矿");
                    return;
                }
                dialog.dismiss();
                myDialog = DialogUtils.createLoadingDialog(mContext, "预估LP挖矿撤销手续费...");
                getLpPledgeEnergy(2);
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.alpha = 1f;
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private void getLpInfo() {
        mWebView1 = new WebView(mContext);
        mWebView1.getSettings().setJavaScriptEnabled(true);
        mWebView1.getSettings().setAppCacheEnabled(false);
        mWebView1.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView1.getSettings().setBlockNetworkImage(false);
        mWebView1.getSettings().setBlockNetworkLoads(false);
        mWebView1.clearCache(true);
        LpInfoJavaScriptInterface javaScriptInterface = new LpInfoJavaScriptInterface();
        mWebView1.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView1.loadUrl("file:///android_asset/selectLpInfo.html");

        mWebView1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                getLpInfoJsParseMainAssetTra();
            }
        });
    }

    private void getLpInfoJsParseMainAssetTra() {
        String url = null;
        try {
            url = "javascript:selectLpInfo('" + valuesAggregatorAddress + "','" + tronAddress + "','" + asoTokenAddress + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
        }
        mWebView1.loadUrl(url);
    }

    private class LpInfoJavaScriptInterface {
        @JavascriptInterface
        public void selectLpInfoFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        ToastUtil.showShort(mContext, str);
                    } else {
                        try {
                            JSONObject singleInfoObject = new JSONObject(str.split("@")[0]);
                            JSONObject balanceObject = new JSONObject(str.split("@")[1]);
                            exTokenBalace = new BigDecimal(TronUtils.toBigInt(singleInfoObject.getJSONObject("_exTokenBalace").getString("_hex")) + "").divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN);
                            exTrxBalance = new BigDecimal(TronUtils.toBigInt(singleInfoObject.getJSONObject("_exTrxBalance").getString("_hex")) + "").divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN);
                            trxToToken = exTokenBalace.divide(exTrxBalance, 12, BigDecimal.ROUND_DOWN).setScale(12, BigDecimal.ROUND_DOWN);
                            tokenToTrx = exTrxBalance.divide(exTokenBalace, 12, BigDecimal.ROUND_DOWN).setScale(12, BigDecimal.ROUND_DOWN);
                            userTrxAmount = new BigDecimal(TronUtils.toBigInt(singleInfoObject.getJSONObject("_userTrxAmount").getString("_hex")) + "").divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN);
                            userTokenAmount = new BigDecimal(TronUtils.toBigInt(singleInfoObject.getJSONObject("_userTokenAmount").getString("_hex")) + "").divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN);
                            lpBalance = new BigDecimal(TronUtils.toBigInt(singleInfoObject.getJSONObject("_userUniAmount").getString("_hex")) + "").divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN);
                            totalLiquidity = new BigDecimal(TronUtils.toBigInt(singleInfoObject.getJSONObject("_totalLiquidity").getString("_hex")) + "").divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN);
                            allowance = new BigDecimal(TronUtils.toBigInt(singleInfoObject.getJSONObject("_allowance").getString("_hex")) + "").divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN);
                            asoBalance = new BigDecimal(TronUtils.toBigInt(balanceObject.getJSONArray("info").getJSONObject(0).getString("_hex")) + "").divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN);
                            mTextView_lp.setText(lpBalance.setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
                            if (lpBalance.floatValue() == 0f) {
                                mTextView_lpMessage.setVisibility(View.GONE);
                            } else {
                                mTextView_lpMessage.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }
            });
        }
    }

    private void getLpDialog(BigDecimal burningTrx) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_mobility_layout, null);
        TextView mTextView_trxBalance = view.findViewById(R.id.mTextView_trxBalance);
        EditText mEditText_trx = view.findViewById(R.id.mEditText_trx);
        TextView mTextView_asoBalance = view.findViewById(R.id.mTextView_asoBalance);
        EditText mEditText_aso = view.findViewById(R.id.mEditText_aso);
        TextView mTextView_asoToTrx = view.findViewById(R.id.mTextView_asoToTrx);
        TextView mTextView_trxToAso = view.findViewById(R.id.mTextView_trxToAso);
        TextView mTextView_proportion = view.findViewById(R.id.mTextView_proportion);
        TextView mTextView_trx = view.findViewById(R.id.mTextView_trx);
        TextView mTextView_aso = view.findViewById(R.id.mTextView_aso);
        TextView mTextView_lp = view.findViewById(R.id.mTextView_lp);
        EditText mEditText_password = view.findViewById(R.id.mEditText_password);
        TextView mTextView_expectedAdd = view.findViewById(R.id.mTextView_expectedAdd);

        mTextView_trxBalance.setText(trxBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
        mTextView_asoBalance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
        mTextView_asoToTrx.setText(tokenToTrx.setScale(3, BigDecimal.ROUND_DOWN).toString());
        mTextView_trxToAso.setText(trxToToken.setScale(3, BigDecimal.ROUND_DOWN).toString());
        mTextView_trx.setText(userTrxAmount.setScale(3, BigDecimal.ROUND_DOWN).toString());
        mTextView_aso.setText(userTokenAmount.setScale(3, BigDecimal.ROUND_DOWN).toString());
        mTextView_lp.setText(lpBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());

        TextWatcher trxTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mEditText_trx.getText().toString().isEmpty()) {
                    BigDecimal edTrx = new BigDecimal(mEditText_trx.getText().toString());
                    mEditText_aso.setText(edTrx.multiply(trxToToken).setScale(3, BigDecimal.ROUND_DOWN).toString());
                    if (edTrx.divide(edTrx.add(exTrxBalance), 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).floatValue() < 0.01f) {
                        mTextView_proportion.setText("<0.01%");
                    } else {
                        mTextView_proportion.setText(edTrx.divide(edTrx.add(exTrxBalance), 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).toString() + "%");
                    }
                    mTextView_expectedAdd.setText(edTrx.multiply(totalLiquidity).divide(exTrxBalance, 3, BigDecimal.ROUND_DOWN).toString() + " LP");
                    minLiquidity = edTrx.multiply(totalLiquidity).divide(exTrxBalance, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                } else {
                    mEditText_aso.setText("");
                    mTextView_proportion.setText("0%");
                    mTextView_expectedAdd.setText("0.000 LP");
                    minLiquidity = "0";
                }
            }
        };

        TextWatcher asoTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mEditText_aso.getText().toString().isEmpty()) {
                    BigDecimal edAso = new BigDecimal(mEditText_aso.getText().toString());
                    mEditText_trx.setText(edAso.multiply(tokenToTrx).setScale(3, BigDecimal.ROUND_DOWN).toString());
                    if (edAso.divide(edAso.add(exTokenBalace), 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).floatValue() < 0.01f) {
                        mTextView_proportion.setText("<0.01%");
                    } else {
                        mTextView_proportion.setText(edAso.divide(edAso.add(exTokenBalace), 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).toString() + "%");
                    }
                    mTextView_expectedAdd.setText(edAso.multiply(tokenToTrx).multiply(totalLiquidity).divide(exTrxBalance, 3, BigDecimal.ROUND_DOWN).toString() + " LP");
                    minLiquidity = edAso.multiply(tokenToTrx).multiply(totalLiquidity).divide(exTrxBalance, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                } else {
                    mEditText_trx.setText("");
                    mTextView_proportion.setText("0%");
                    mTextView_expectedAdd.setText("0.000 LP");
                    minLiquidity = "0";
                }
            }
        };
        mEditText_trx.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEditText_trx.addTextChangedListener(trxTextWatcher);
                } else {
                    mEditText_trx.removeTextChangedListener(trxTextWatcher);
                }
            }
        });
        mEditText_aso.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEditText_aso.addTextChangedListener(asoTextWatcher);
                } else {
                    mEditText_aso.removeTextChangedListener(asoTextWatcher);
                }
            }
        });

        view.findViewById(R.id.mTextView_trxMax).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trxBalance.subtract(new BigDecimal("5")).floatValue() < 0f) {
                    mEditText_trx.setText("0.000");
                    mEditText_aso.setText("0.000");
                    mTextView_proportion.setText("0%");
                    mTextView_expectedAdd.setText("0.000 LP");
                    ToastUtil.showShort(mContext, "当前TRX余额小于5个");
                } else {
                    if (trxBalance.subtract(new BigDecimal("5")).multiply(trxToToken).floatValue() < asoBalance.floatValue()) {
                        mEditText_trx.setText(trxBalance.subtract(new BigDecimal("5")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mEditText_aso.setText(trxBalance.subtract(new BigDecimal("5")).multiply(trxToToken).setScale(3, BigDecimal.ROUND_DOWN).toString());
                        if (trxBalance.subtract(new BigDecimal("5")).divide(exTrxBalance, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).floatValue() < 0.01f) {
                            mTextView_proportion.setText("<0.01%");
                        } else {
                            mTextView_proportion.setText(trxBalance.subtract(new BigDecimal("5")).divide(exTrxBalance, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).toString() + "%");
                        }
                        mTextView_expectedAdd.setText(trxBalance.subtract(new BigDecimal("5")).setScale(3, BigDecimal.ROUND_DOWN).multiply(totalLiquidity).divide(exTrxBalance, 3, BigDecimal.ROUND_DOWN).toString() + " LP");
                        minLiquidity = trxBalance.subtract(new BigDecimal("5")).multiply(totalLiquidity).divide(exTrxBalance, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                    } else {
                        mEditText_trx.setText(asoBalance.multiply(tokenToTrx).setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mEditText_aso.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        if (asoBalance.divide(exTokenBalace, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).floatValue() < 0.01f) {
                            mTextView_proportion.setText("<0.01%");
                        } else {
                            mTextView_proportion.setText(asoBalance.divide(exTokenBalace, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).toString() + "%");
                        }
                        mTextView_expectedAdd.setText(asoBalance.multiply(tokenToTrx).setScale(3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).multiply(totalLiquidity).divide(exTrxBalance, 3, BigDecimal.ROUND_DOWN).toString() + " LP");
                        minLiquidity = asoBalance.multiply(tokenToTrx).multiply(totalLiquidity).divide(exTrxBalance, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                    }
                }
            }
        });

        view.findViewById(R.id.mTextView_asoMax).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trxBalance.subtract(new BigDecimal("5")).floatValue() < 0f) {
                    mEditText_trx.setText("0.000");
                    mEditText_aso.setText("0.000");
                    mTextView_proportion.setText("0%");
                    mTextView_expectedAdd.setText("0.000 LP");
                    ToastUtil.showShort(mContext, "当前TRX余额小于5个");
                } else {
                    if (trxBalance.subtract(new BigDecimal("5")).multiply(trxToToken).floatValue() < asoBalance.floatValue()) {
                        mEditText_trx.setText(trxBalance.subtract(new BigDecimal("5")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mEditText_aso.setText(trxBalance.subtract(new BigDecimal("5")).multiply(trxToToken).setScale(3, BigDecimal.ROUND_DOWN).toString());
                        if (trxBalance.subtract(new BigDecimal("5")).divide(exTrxBalance, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).floatValue() < 0.01f) {
                            mTextView_proportion.setText("<0.01%");
                        } else {
                            mTextView_proportion.setText(trxBalance.subtract(new BigDecimal("5")).divide(exTrxBalance, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).toString() + "%");
                        }
                        mTextView_expectedAdd.setText(trxBalance.subtract(new BigDecimal("5")).setScale(3, BigDecimal.ROUND_DOWN).multiply(totalLiquidity).divide(exTrxBalance, 3, BigDecimal.ROUND_DOWN).toString() + " LP");
                        minLiquidity = trxBalance.subtract(new BigDecimal("5")).multiply(totalLiquidity).divide(exTrxBalance, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                    } else {
                        mEditText_trx.setText(asoBalance.multiply(tokenToTrx).setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mEditText_aso.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        if (asoBalance.divide(exTokenBalace, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).floatValue() < 0.01f) {
                            mTextView_proportion.setText("<0.01%");
                        } else {
                            mTextView_proportion.setText(asoBalance.divide(exTokenBalace, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).toString() + "%");
                        }
                        mTextView_expectedAdd.setText(asoBalance.multiply(tokenToTrx).setScale(3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).multiply(totalLiquidity).divide(exTrxBalance, 3, BigDecimal.ROUND_DOWN).toString() + " LP");
                        minLiquidity = asoBalance.multiply(tokenToTrx).multiply(totalLiquidity).divide(exTrxBalance, 6, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                    }
                }
            }
        });

        view.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.mLinearLayout_btn_subscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                if (mEditText_trx.getText().toString().isEmpty() || mEditText_aso.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "提供的TRX或ASO不能为空");
                    return;
                }
                if (Float.parseFloat(mEditText_trx.getText().toString()) == 0f || Float.parseFloat(mEditText_aso.getText().toString()) == 0f) {
                    ToastUtil.showShort(mContext, "提供的TRX或ASO不能为0");
                    return;
                }
                if (Float.parseFloat(mEditText_trx.getText().toString()) > trxBalance.floatValue() || Float.parseFloat(mEditText_aso.getText().toString()) > asoBalance.floatValue()) {
                    ToastUtil.showShort(mContext, "TRX或ASO余额不足");
                    return;
                }
                if (mEditText_password.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "交易密码不能为空");
                    return;
                }
                tradePassword = mEditText_password.getText().toString();
                maxTokens = new BigDecimal(mEditText_aso.getText().toString()).multiply(new BigDecimal("1").add(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                deadline = (System.currentTimeMillis() + Content.timePoor + (60 * 1000)) / 1000 + "";
                msgValue = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                if (trxBalance.subtract(new BigDecimal(mEditText_trx.getText().toString())).subtract(burningTrx).floatValue() < 0f) {
                    dialogTrx(2, burningTrx, false);
                } else {
                    if (allowance.floatValue() > 0f && allowance.floatValue() >= new BigDecimal(mEditText_aso.getText().toString()).multiply(new BigDecimal("1").add(slidingPoint)).floatValue()) {
                        myDialog = DialogUtils.createLoadingDialog(mContext, "提供流动性...");
                        addLiquidity();
                    } else {
                        myDialog = DialogUtils.createLoadingDialog(mContext, "授权ASO...");
                        approve();
                    }
                }
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

    private void getAddLiquidityEnergy(int type) {
        OkHttpClient.initGet(Http.getAddLiquidityEnergy + "?type=" + type).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        energy_usage_total = new BigDecimal("0");
                        net_usage = new BigDecimal("0");
                        ToastUtil.showShort(mContext, "网络错误，预估手续费失败");
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
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                energy_usage_total = new BigDecimal(jsonObject.getJSONObject("data").getString("energy"));
                                net_usage = new BigDecimal(jsonObject.getJSONObject("data").getString("net"));

                                BigDecimal burningTrx = new BigDecimal("0");
                                if (netBalance.floatValue() < net_usage.floatValue()) {
                                    burningTrx = burningTrx.add(net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (energyBalance.floatValue() < energy_usage_total.floatValue()) {
                                    burningTrx = burningTrx.add(energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (type == 1) {
                                    getLpDialog(burningTrx);
                                } else {
                                    if (trxBalance.floatValue() >= burningTrx.floatValue()) {
                                        deleteLpDialog();
                                    } else {
                                        dialogTrx(5, burningTrx, false);
                                    }
                                }
                            } else {
                                energy_usage_total = new BigDecimal("0");
                                net_usage = new BigDecimal("0");
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            energy_usage_total = new BigDecimal("0");
                            net_usage = new BigDecimal("0");
                            ToastUtil.showShort(mContext, "预估AP挖矿手续费错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void approve() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        ApproveJavaScriptInterface javaScriptInterface = new ApproveJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/approve.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                approveJsParseMainAssetTra();
            }
        });
    }

    private void approveJsParseMainAssetTra() {
        String url = null;
        try {
            url = "javascript:approve('" + AESUtil.decrypt(privateKey, tradePassword) + "','" + asoTokenAddress + "','" + lpExchangeAddress + "','115792089237316195423570985008687907853269984665640564039457584007913129639935','" + Http.tronUrl + "')";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class ApproveJavaScriptInterface {
        @JavascriptInterface
        public void approveFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, str);
                    } else {
                        myDialog.setMsg("链上验证授权结果...");
                        number = 1;
                        getTransactionInfoByIdType(str, 1);
                    }
                }
            });
        }
    }

    private void addLiquidity() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        AddLiquidityJavaScriptInterface javaScriptInterface = new AddLiquidityJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/addLiquidity.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                addLiquidityJsParseMainAssetTra();
            }
        });
    }

    private void addLiquidityJsParseMainAssetTra() {
        String url = null;
        try {
            url = "javascript:addLiquidity('" + AESUtil.decrypt(privateKey, tradePassword) + "','" + lpExchangeAddress + "'," + minLiquidity + "," + maxTokens + "," + deadline + "," + msgValue + ",'" + Http.tronUrl + "')";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class AddLiquidityJavaScriptInterface {
        @JavascriptInterface
        public void addLiquidityFunction(String str) {
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

    private void getTransactionInfoByIdType(String TransactionId, int type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("value", TransactionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.getTransactionInfoById, requestBodyPost).enqueue(new Callback() {
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
                                JSONObject jsonObject1 = new JSONObject(result);
                                if (jsonObject1.getJSONObject("receipt").getString("result").equals("SUCCESS")) {
                                    if (type == 1) {
                                        myDialog.setMsg("提供流动性...");
                                        addLiquidity();
                                    } else if (type == 2) {
                                        DialogUtils.closeDialog(myDialog);
                                        ToastUtil.showShort(mContext, "提供流动性成功");
                                        dialog.dismiss();
                                    } else if (type == 3) {
                                        dialog.dismiss();
                                        lpPledgeDo(TransactionId, 1);
                                        getPledgedLp();
                                    } else if (type == 4) {
                                        dialog.dismiss();
                                        getRewardLp();
                                        getTotalRewardLp();
                                        lpTakeMineral(TransactionId);
                                    } else if (type == 5) {
                                        DialogUtils.closeDialog(myDialog);
                                        ToastUtil.showShort(mContext, "移除流动性成功");
                                        dialog.dismiss();
                                    } else if (type == 6) {
                                        dialog.dismiss();
                                        lpPledgeDo(TransactionId, 2);
                                        getPledgedLp();
                                    }
                                } else {
                                    DialogUtils.closeDialog(myDialog);
                                    ToastUtil.showShort(mContext, "验证结果失败：" + jsonObject1.getJSONObject("receipt").getString("result"));
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

    private void getLpPledgeEnergy(int type) {
        OkHttpClient.initGet(Http.getLpPledgeEnergy + "?type=" + type).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        energy_usage_total = new BigDecimal("0");
                        net_usage = new BigDecimal("0");
                        ToastUtil.showShort(mContext, "网络错误，预估手续费失败");
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
                            DialogUtils.closeDialog(myDialog);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                energy_usage_total = new BigDecimal(jsonObject.getJSONObject("data").getString("energy_usage_total"));
                                net_usage = new BigDecimal(jsonObject.getJSONObject("data").getString("net_usage"));

                                BigDecimal burningTrx = new BigDecimal("0");
                                if (netBalance.floatValue() < net_usage.floatValue()) {
                                    burningTrx = burningTrx.add(net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (energyBalance.floatValue() < energy_usage_total.floatValue()) {
                                    burningTrx = burningTrx.add(energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (trxBalance.floatValue() >= burningTrx.floatValue()) {
                                    if (type == 1) {
                                        lpPledgeMiningDialog();
                                    } else {
                                        revokeLpDialog();
                                    }
                                } else {
                                    if (type == 1) {
                                        dialogTrx(3, burningTrx, false);
                                    } else {
                                        dialogTrx(6, burningTrx, false);
                                    }
                                }
                            } else {
                                energy_usage_total = new BigDecimal("0");
                                net_usage = new BigDecimal("0");
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            energy_usage_total = new BigDecimal("0");
                            net_usage = new BigDecimal("0");
                            ToastUtil.showShort(mContext, "预估手续费错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void lpPledgeMiningDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_lp_pledge_mining_layout, null);
        EditText mEditText_quota = v.findViewById(R.id.mEditText_quota);
        EditText mEditText_password = v.findViewById(R.id.mEditText_password);
        TextView mTextView_net_text = v.findViewById(R.id.mTextView_net_text);
        TextView mTextView_net = v.findViewById(R.id.mTextView_net);
        TextView mTextView_energy_text = v.findViewById(R.id.mTextView_energy_text);
        TextView mTextView_energy = v.findViewById(R.id.mTextView_energy);
        TextView mTextView_btn_cancel = v.findViewById(R.id.mTextView_btn_cancel);
        LinearLayout mLinearLayout_btn_subscribe = v.findViewById(R.id.mLinearLayout_btn_subscribe);
        TextView mTextView_balance = v.findViewById(R.id.mTextView_balance);

        if (netBalance.floatValue() < net_usage.floatValue()) {
            mTextView_net_text.setText("预计燃烧 " + net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取带宽");
            mTextView_net.setText("=" + net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToNet).setScale(0, BigDecimal.ROUND_DOWN).toString());
        } else {
            mTextView_net_text.setText("预计消耗带宽");
            mTextView_net.setText("≈" + net_usage.setScale(0, BigDecimal.ROUND_DOWN).toString());
        }

        if (energyBalance.floatValue() < energy_usage_total.floatValue()) {
            mTextView_energy_text.setText("预计燃烧 " + energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取能量");
            mTextView_energy.setText("=" + energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
        } else {
            mTextView_energy_text.setText("预计消耗能量");
            mTextView_energy.setText("≈" + energy_usage_total.setScale(0, BigDecimal.ROUND_DOWN).toString());
        }

        mTextView_balance.setText(lpBalance.setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");

        v.findViewById(R.id.mTextView_max).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText_quota.setText(lpBalance.setScale(6, BigDecimal.ROUND_DOWN).toString());
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
                if (lpBalance.floatValue() == 0f && flag1) {
                    ToastUtil.showShort(mContext, "当前可用LP为零");
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

                    if (result.compareTo(lpBalance) == 1) {
                        temp = lpBalance.setScale(6, BigDecimal.ROUND_DOWN).toString();
                        ToastUtil.showShort(mContext, "最多可以质押" + lpBalance.setScale(6, BigDecimal.ROUND_DOWN).toString() + "LP");
                    }

                    flag1 = false;
                    s.clear();
                    if (posDot > 0 && temp.length() - posDot - 1 > 6) {
                        temp = temp.substring(0, posDot + 7);
                    }
                    s.append(temp);

                    flag1 = true;
                }
            }
        });

        mTextView_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mLinearLayout_btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText_quota.getText().toString().isEmpty() || Float.parseFloat(mEditText_quota.getText().toString()) == 0f) {
                    ToastUtil.showShort(mContext, "质押额度不能为0");
                    return;
                }
                if (mEditText_password.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "交易密码不能为空");
                    return;
                }
                tradePassword = mEditText_password.getText().toString();
                myDialog = DialogUtils.createLoadingDialog(mContext, "获取授权信息...");
                getApprove(new BigDecimal(mEditText_quota.getText().toString()).multiply(new BigDecimal("1000000")));
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

    private void getApprove(BigDecimal amount) {
        OkHttpClient.initGet(Http.getAllowance + "?tronAddress=" + tronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，获取授权失败");
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
                            if (jsonObject.getInt("code") == 1) {
                                myDialog.setMsg("质押中...");
                                if (new BigDecimal(jsonObject.getString("data")).floatValue() <= 0f || new BigDecimal(jsonObject.getString("data")).floatValue() < amount.floatValue()) {
                                    lpPledgeMining(amount.setScale(0, BigDecimal.ROUND_DOWN).toString(), true);
                                } else {
                                    lpPledgeMining(amount.setScale(0, BigDecimal.ROUND_DOWN).toString(), false);
                                }
                            } else {
                                DialogUtils.closeDialog(myDialog);
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "获取授权错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void lpPledgeMining(String amount, boolean isApprove) {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        LpPledgeMiningJavaScriptInterface javaScriptInterface = new LpPledgeMiningJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/lpPledgeMining.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                lpPledgeMiningJsParseMainAssetTra(amount, isApprove);
            }
        });
    }

    private void lpPledgeMiningJsParseMainAssetTra(String amount, boolean isApprove) {
        String url = null;
        try {
            url = "javascript:stake('" + AESUtil.decrypt(privateKey, tradePassword) + "','" + lpExchangeAddress + "','" + lpPoolAddress + "','115792089237316195423570985008687907853269984665640564039457584007913129639935'," + amount + "," + isApprove + ",'" + Http.tronUrl + "')";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class LpPledgeMiningJavaScriptInterface {
        @JavascriptInterface
        public void stakeFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, str);
                    } else {
                        myDialog.setMsg("链上验证结果中...");
                        number = 1;
                        getTransactionInfoByIdType(str, 3);
                    }
                }
            });
        }
    }

    private void lpPledgeDo(String hash, int type) {
        OkHttpClient.initGet(Http.lpPledgeDo + "?tronAddress=" + tronAddress + "&hash=" + hash).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，LP挖矿失败");
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
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                if (type == 1) {
                                    ToastUtil.showShort(mContext, "LP挖矿成功");
                                } else if (type == 2) {
                                    ToastUtil.showShort(mContext, "撤销LP挖矿成功");
                                }
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "LP挖矿错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void getRewardLp() {
        isPause1 = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("owner_address", tronAddress);
            jsonObject.put("contract_address", lpPoolAddress);
            jsonObject.put("function_selector", "selectGain()");
            jsonObject.put("visible", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.triggerConstantContract, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isPause1 = false;
                        ToastUtil.showShort(mContext, "网络错误，查询LP挖矿待收取失败");
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
                            isPause1 = false;
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (!result.contains("FAILED")) {
                                rewardLpBig = new BigDecimal(TronUtils.toBigInt(jsonObject1.getJSONArray("constant_result").getString(0))).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                mTextView_btn_reward_lp.setText(rewardLpBig.toString() + " ASO 待收取");
                                if (rewardLpBig.floatValue() != 0f || pledgedLpBig.floatValue() != 0f || totalRewardLpBig.floatValue() != 0f) {
                                    mRelativeLayout_mining_lp.setVisibility(View.VISIBLE);
                                } else {
                                    mRelativeLayout_mining_lp.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        });
    }

    private void getPledgedLp() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("owner_address", tronAddress);
            jsonObject.put("contract_address", lpPoolAddress);
            jsonObject.put("function_selector", "selectlpAmount()");
            jsonObject.put("visible", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.triggerConstantContract, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(mContext, "网络错误，查询LP挖矿已质押失败");
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
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (!result.contains("FAILED")) {
                                pledgedLpBig = new BigDecimal(TronUtils.toBigInt(jsonObject1.getJSONArray("constant_result").getString(0))).divide(decimals, 6, BigDecimal.ROUND_DOWN).setScale(6, BigDecimal.ROUND_DOWN);
                                mTextView_pledged_lp.setText(pledgedLpBig.setScale(3, BigDecimal.ROUND_DOWN).toString());
                                if (pledgedLpBig.floatValue() != 0f || rewardLpBig.floatValue() != 0f || totalRewardLpBig.floatValue() != 0f) {
                                    mRelativeLayout_mining_lp.setVisibility(View.VISIBLE);
                                } else {
                                    mRelativeLayout_mining_lp.setVisibility(View.GONE);
                                }
                                if (pledgedLpBig.floatValue() != 0f && !isAnimation1) {
                                    mTextView_mining_lp.setText("正在挖矿中（LP挖矿）");
                                    mLottieAnimationView_mining_lp.setVisibility(View.VISIBLE);
                                    mLottieAnimationView_mining_lp.setAnimation(R.raw.wakuang);
                                    mLottieAnimationView_mining_lp.setRepeatCount(-1);
                                    mLottieAnimationView_mining_lp.playAnimation();
                                    isAnimation1 = true;
                                }
                                if (pledgedLpBig.floatValue() == 0f) {
                                    mTextView_mining_lp.setText("待挖矿（LP挖矿）");
                                    mLottieAnimationView_mining_lp.cancelAnimation();
                                    mLottieAnimationView_mining_lp.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        });
    }

    private void getTotalRewardLp() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("owner_address", tronAddress);
            jsonObject.put("contract_address", lpPoolAddress);
            jsonObject.put("function_selector", "selectTotalGain()");
            jsonObject.put("visible", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.triggerConstantContract, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(mContext, "网络错误，查询LP挖矿已收矿失败");
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
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (!result.contains("FAILED")) {
                                totalRewardLpBig = new BigDecimal(TronUtils.toBigInt(jsonObject1.getJSONArray("constant_result").getString(0))).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                mTextView_reward_lp.setText(totalRewardLpBig.toString() + " ASO");
                                if (totalRewardLpBig.floatValue() != 0f || pledgedLpBig.floatValue() != 0f || rewardLpBig.floatValue() != 0f) {
                                    mRelativeLayout_mining_lp.setVisibility(View.VISIBLE);
                                } else {
                                    mRelativeLayout_mining_lp.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        });
    }

    private void getLpWithdrawEnergy() {
        myDialog = DialogUtils.createLoadingDialog(mContext, "预估收矿手续费...");
        OkHttpClient.initGet(Http.getLpWithdrawEnergy).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        energy_usage_total = new BigDecimal("0");
                        net_usage = new BigDecimal("0");
                        ToastUtil.showShort(mContext, "网络错误，预估收矿手续费失败");
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
                            DialogUtils.closeDialog(myDialog);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                energy_usage_total = new BigDecimal(jsonObject.getJSONObject("data").getString("energy_usage_total"));
                                net_usage = new BigDecimal(jsonObject.getJSONObject("data").getString("net_usage"));
                                BigDecimal burningTrx = new BigDecimal("0");
                                if (netBalance.floatValue() < net_usage.floatValue()) {
                                    burningTrx = burningTrx.add(net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (energyBalance.floatValue() < energy_usage_total.floatValue()) {
                                    burningTrx = burningTrx.add(energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (trxBalance.floatValue() > burningTrx.floatValue()) {
                                    payDialogLp();
                                } else {
                                    dialogTrx(4, burningTrx, false);
                                }
                            } else {
                                energy_usage_total = new BigDecimal("0");
                                net_usage = new BigDecimal("0");
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            energy_usage_total = new BigDecimal("0");
                            net_usage = new BigDecimal("0");
                            ToastUtil.showShort(mContext, "预估收矿手续费错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void payDialogLp() {
        View v1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_pledge_mining_private_key_layout, null);
        TextView mTextView_address = v1.findViewById(R.id.mTextView_address);
        TextView mEditText_password = v1.findViewById(R.id.mEditText_password);
        TextView mTextView_net_text = v1.findViewById(R.id.mTextView_net_text);
        TextView mTextView_net = v1.findViewById(R.id.mTextView_net);
        TextView mTextView_energy_text = v1.findViewById(R.id.mTextView_energy_text);
        TextView mTextView_energy = v1.findViewById(R.id.mTextView_energy);
        mTextView_address.setText(tronAddress);

        if (netBalance.floatValue() < net_usage.floatValue()) {
            mTextView_net_text.setText("预计燃烧 " + net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取带宽");
            mTextView_net.setText("=" + net_usage.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToNet).setScale(0, BigDecimal.ROUND_DOWN).toString());
        } else {
            mTextView_net_text.setText("预计消耗带宽");
            mTextView_net.setText("≈" + net_usage.setScale(0, BigDecimal.ROUND_DOWN).toString());
        }
        if (energyBalance.floatValue() < energy_usage_total.floatValue()) {
            mTextView_energy_text.setText("预计燃烧 " + energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取能量");
            mTextView_energy.setText("=" + energy_usage_total.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
        } else {
            mTextView_energy_text.setText("预计消耗能量");
            mTextView_energy.setText("≈" + energy_usage_total.setScale(0, BigDecimal.ROUND_DOWN).toString());
        }

        v1.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        v1.findViewById(R.id.mTextView_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradePassword = mEditText_password.getText().toString();
                if (tradePassword.isEmpty()) {
                    ToastUtil.showShort(mContext, "交易密码不能为空");
                    return;
                }
                hintKeyBoard(v);
                mEditText_password.setText("");
                myDialog = DialogUtils.createLoadingDialog(mContext, "收矿中...");
                receiveRewardsLp();
            }
        });
        dialog = new Dialog(mContext, R.style.LeftDialogStyle);
        dialog.setContentView(v1);
        dialog.setCancelable(false);
        Window dialogWindow1 = dialog.getWindow();
        if (dialogWindow1 == null) {
            return;
        }
        dialogWindow1.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
        lp1.dimAmount = 0.1f;
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow1.setAttributes(lp1);
        dialog.show();
    }

    private void receiveRewardsLp() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        RewLpJavaScriptInterface javaScriptInterface = new RewLpJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/receiveRewards.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LpJsRew();
            }
        });
    }

    private void LpJsRew() {
        String url = null;
        try {
            url = "javascript:reward('" + AESUtil.decrypt(privateKey, tradePassword) + "','" + lpPoolAddress + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class RewLpJavaScriptInterface {
        @JavascriptInterface
        public void startFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, str);
                    } else {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myDialog.setMsg("链上验证结果中...");
                                number = 1;
                                getTransactionInfoByIdType(str, 4);
                            }
                        });
                    }
                }
            });
        }
    }

    private void lpTakeMineral(String TransactionId) {
        OkHttpClient.initGet(Http.lpTakeMineral + "?hash=" + TransactionId + "&tronAddress=" + tronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，上传收矿记录失败");
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
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (jsonObject1.getInt("code") == 1) {
                                ToastUtil.showShort(mContext, "收矿成功");
                            } else {
                                ToastUtil.showShort(mContext, jsonObject1.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "上传收矿记录失败：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void deleteLpDialog() {
        View v1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete_mobility_layout, null);
        TextView mTextView_percentage = v1.findViewById(R.id.mTextView_percentage);
        SeekBar mSeekBar = v1.findViewById(R.id.mSeekBar);
        TextView mTextView_deleteTrx = v1.findViewById(R.id.mTextView_deleteTrx);
        TextView mTextView_trxToAso = v1.findViewById(R.id.mTextView_trxToAso);
        TextView mTextView_asoToTrx = v1.findViewById(R.id.mTextView_asoToTrx);
        TextView mTextView_deleteAso = v1.findViewById(R.id.mTextView_deleteAso);
        TextView mTextView_trx = v1.findViewById(R.id.mTextView_trx);
        TextView mTextView_aso = v1.findViewById(R.id.mTextView_aso);
        TextView mTextView_lp = v1.findViewById(R.id.mTextView_lp);
        EditText mEditText_password = v1.findViewById(R.id.mEditText_password);
        TextView mTextView_expectedDelete = v1.findViewById(R.id.mTextView_expectedDelete);

        mTextView_deleteTrx.setText(userTrxAmount.multiply(new BigDecimal("0.5")).setScale(3, BigDecimal.ROUND_DOWN).toString());
        mTextView_deleteAso.setText(userTokenAmount.multiply(new BigDecimal("0.5")).setScale(3, BigDecimal.ROUND_DOWN).toString());
        mTextView_trxToAso.setText("1 TRX = " + trxToToken.setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
        mTextView_asoToTrx.setText("1 ASO = " + tokenToTrx.setScale(3, BigDecimal.ROUND_DOWN).toString() + " TRX");
        mTextView_trx.setText(userTrxAmount.setScale(3, BigDecimal.ROUND_DOWN).toString());
        mTextView_aso.setText(userTokenAmount.setScale(3, BigDecimal.ROUND_DOWN).toString());
        mTextView_lp.setText(lpBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
        mTextView_expectedDelete.setText(lpBalance.multiply(new BigDecimal("0.5")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
        deleteAmount = lpBalance.multiply(new BigDecimal("0.5")).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BigDecimal progressBig = new BigDecimal(progress).divide(new BigDecimal("100"), 3, BigDecimal.ROUND_DOWN);
                mTextView_percentage.setText(progress + "%");
                mTextView_deleteTrx.setText(userTrxAmount.multiply(progressBig).setScale(3, BigDecimal.ROUND_DOWN).toString());
                mTextView_deleteAso.setText(userTokenAmount.multiply(progressBig).setScale(3, BigDecimal.ROUND_DOWN).toString());
                mTextView_expectedDelete.setText(lpBalance.multiply(progressBig).setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
                deleteAmount = lpBalance.multiply(progressBig).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        v1.findViewById(R.id.mTextView_25).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(25);
                mTextView_percentage.setText("25%");
                mTextView_deleteTrx.setText(userTrxAmount.multiply(new BigDecimal("0.25")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                mTextView_deleteAso.setText(userTokenAmount.multiply(new BigDecimal("0.25")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                mTextView_expectedDelete.setText(lpBalance.multiply(new BigDecimal("0.25")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
                deleteAmount = lpBalance.multiply(new BigDecimal("0.25")).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
            }
        });
        v1.findViewById(R.id.mTextView_50).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(50);
                mTextView_percentage.setText("50%");
                mTextView_deleteTrx.setText(userTrxAmount.multiply(new BigDecimal("0.5")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                mTextView_deleteAso.setText(userTokenAmount.multiply(new BigDecimal("0.5")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                mTextView_expectedDelete.setText(lpBalance.multiply(new BigDecimal("0.5")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
                deleteAmount = lpBalance.multiply(new BigDecimal("0.5")).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
            }
        });
        v1.findViewById(R.id.mTextView_75).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(75);
                mTextView_percentage.setText("75%");
                mTextView_deleteTrx.setText(userTrxAmount.multiply(new BigDecimal("0.75")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                mTextView_deleteAso.setText(userTokenAmount.multiply(new BigDecimal("0.75")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                mTextView_expectedDelete.setText(lpBalance.multiply(new BigDecimal("0.75")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
                deleteAmount = lpBalance.multiply(new BigDecimal("0.75")).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
            }
        });
        v1.findViewById(R.id.mTextView_100).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(100);
                mTextView_percentage.setText("100%");
                mTextView_deleteTrx.setText(userTrxAmount.setScale(3, BigDecimal.ROUND_DOWN).toString());
                mTextView_deleteAso.setText(userTokenAmount.setScale(3, BigDecimal.ROUND_DOWN).toString());
                mTextView_expectedDelete.setText(lpBalance.setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
                deleteAmount = lpBalance.multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
            }
        });

        v1.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        v1.findViewById(R.id.mLinearLayout_btn_subscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradePassword = mEditText_password.getText().toString();
                if (tradePassword.isEmpty()) {
                    ToastUtil.showShort(mContext, "交易密码不能为空");
                    return;
                }
                hintKeyBoard(v);
                mEditText_password.setText("");
                deadline = (System.currentTimeMillis() + Content.timePoor + (60 * 1000)) / 1000 + "";
                myDialog = DialogUtils.createLoadingDialog(mContext, "移除中...");
                deleteLp();
            }
        });
        dialog = new Dialog(mContext, R.style.LeftDialogStyle);
        dialog.setContentView(v1);
        dialog.setCancelable(false);
        Window dialogWindow1 = dialog.getWindow();
        if (dialogWindow1 == null) {
            return;
        }
        dialogWindow1.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
        lp1.dimAmount = 0.1f;
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow1.setAttributes(lp1);
        dialog.show();
    }

    private void deleteLp() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        DeleteLpJavaScriptInterface javaScriptInterface = new DeleteLpJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/deleteLiquidity.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                deleteLpJsParseMainAssetTra();
            }
        });
    }

    private void deleteLpJsParseMainAssetTra() {
        String url = null;
        try {
            url = "javascript:deleteLp('" + AESUtil.decrypt(privateKey, tradePassword) + "','" + lpExchangeAddress + "'," + deleteAmount + ",1,1," + deadline + ",'" + Http.tronUrl + "')";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class DeleteLpJavaScriptInterface {
        @JavascriptInterface
        public void deleteLpFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, str);
                    } else {
                        myDialog.setMsg("链上验证结果中...");
                        number = 1;
                        getTransactionInfoByIdType(str, 5);
                    }
                }
            });
        }
    }

    private void revokeLpDialog() {
        View v1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_revoke_mobility_layout, null);
        TextView mTextView_percentage = v1.findViewById(R.id.mTextView_percentage);
        SeekBar mSeekBar = v1.findViewById(R.id.mSeekBar);
        EditText mEditText_password = v1.findViewById(R.id.mEditText_password);
        TextView mTextView_expectedRevoke = v1.findViewById(R.id.mTextView_expectedRevoke);

        mTextView_expectedRevoke.setText(pledgedLpBig.multiply(new BigDecimal("0.5")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
        revokeAmount = pledgedLpBig.multiply(new BigDecimal("0.5")).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BigDecimal progressBig = new BigDecimal(progress).divide(new BigDecimal("100"), 3, BigDecimal.ROUND_DOWN);
                mTextView_percentage.setText(progress + "%");
                mTextView_expectedRevoke.setText(pledgedLpBig.multiply(progressBig).setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
                revokeAmount = pledgedLpBig.multiply(progressBig).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        v1.findViewById(R.id.mTextView_25).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(25);
                mTextView_percentage.setText("25%");
                mTextView_expectedRevoke.setText(pledgedLpBig.multiply(new BigDecimal("0.25")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
                revokeAmount = pledgedLpBig.multiply(new BigDecimal("0.25")).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
            }
        });
        v1.findViewById(R.id.mTextView_50).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(50);
                mTextView_percentage.setText("50%");
                mTextView_expectedRevoke.setText(pledgedLpBig.multiply(new BigDecimal("0.5")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
                revokeAmount = pledgedLpBig.multiply(new BigDecimal("0.5")).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
            }
        });
        v1.findViewById(R.id.mTextView_75).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(75);
                mTextView_percentage.setText("75%");
                mTextView_expectedRevoke.setText(pledgedLpBig.multiply(new BigDecimal("0.75")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
                revokeAmount = pledgedLpBig.multiply(new BigDecimal("0.75")).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
            }
        });
        v1.findViewById(R.id.mTextView_100).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(100);
                mTextView_percentage.setText("100%");
                mTextView_expectedRevoke.setText(pledgedLpBig.setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
                revokeAmount = pledgedLpBig.multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
            }
        });

        v1.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        v1.findViewById(R.id.mLinearLayout_btn_subscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradePassword = mEditText_password.getText().toString();
                if (tradePassword.isEmpty()) {
                    ToastUtil.showShort(mContext, "交易密码不能为空");
                    return;
                }
                hintKeyBoard(v);
                mEditText_password.setText("");
                myDialog = DialogUtils.createLoadingDialog(mContext, "撤销中...");
                revokeLp();
            }
        });
        dialog = new Dialog(mContext, R.style.LeftDialogStyle);
        dialog.setContentView(v1);
        dialog.setCancelable(false);
        Window dialogWindow1 = dialog.getWindow();
        if (dialogWindow1 == null) {
            return;
        }
        dialogWindow1.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
        lp1.dimAmount = 0.1f;
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow1.setAttributes(lp1);
        dialog.show();
    }

    private void revokeLp() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        RevokeLpJavaScriptInterface javaScriptInterface = new RevokeLpJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/revokeLp.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                revokeLpJsParseMainAssetTra();
            }
        });
    }

    private void revokeLpJsParseMainAssetTra() {
        String url = null;
        try {
            url = "javascript:revokeLp('" + AESUtil.decrypt(privateKey, tradePassword) + "','" + lpPoolAddress + "'," + revokeAmount + ",'" + Http.tronUrl + "')";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class RevokeLpJavaScriptInterface {
        @JavascriptInterface
        public void revokeLpFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, str);
                    } else {
                        myDialog.setMsg("链上验证结果中...");
                        number = 1;
                        getTransactionInfoByIdType(str, 6);
                    }
                }
            });
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pledgeInfo(refreshLayout);
        getTronAddress(2);
    }

    private void hintKeyBoard(View view) {
        InputMethodManager inputMgr = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
