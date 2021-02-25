package com.xw.aschwitkey.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.activity.Trc20Activity;
import com.xw.aschwitkey.activity.TronResourceActivity;
import com.xw.aschwitkey.db.TronSQLUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
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

import static com.xw.aschwitkey.activity.Content.frozenEnergy;

public class TronWalletFragment extends NewLazyFragment implements OnRefreshListener {

    @BindView(R.id.mTextView_address)
    TextView mTextView_address;
    @BindView(R.id.mTextView_account)
    TextView mTextView_account;
    @BindView(R.id.mTextView_trx)
    TextView mTextView_trx;
    @BindView(R.id.mTextView_available)
    TextView mTextView_available;
    @BindView(R.id.mTextView_freeze)
    TextView mTextView_freeze;
    @BindView(R.id.mTextView_bandwidth)
    TextView mTextView_bandwidth;
    @BindView(R.id.mTextView_energy)
    TextView mTextView_energy;
    @BindView(R.id.mTextView_trx_bottom)
    TextView mTextView_trx_bottom;
    @BindView(R.id.mTextView_TXas)
    TextView mTextView_TXas;
    @BindView(R.id.mTextView_aso)
    TextView mTextView_aso;
    @BindView(R.id.mTextView_USDT)
    TextView mTextView_USDT;
    @BindView(R.id.mTextView_USDJ)
    TextView mTextView_USDJ;
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mRelativeLayout_copy)
    RelativeLayout mRelativeLayout_copy;
    @BindView(R.id.mLinearLayout_import_tron)
    LinearLayout mLinearLayout_import_tron;

    private Activity mContext;
    private SPUtils spUtils, spUtils1;
    private MyDialog myDialog, myDialog1, myDialog2;
    private Dialog dialog, dialog1;
    private WebView mWebView, mWebView1, mWebView2;
    private boolean isClick = false, isBinding = false, isOk = false, isPause = false, isFirst = true, isSwitch = false, isAddAddress = false, flag = true, isSwitchUsdt = false;
    private String tronAddress = "", privateKey = "", editPrivateKey = "", tradePassword = "", addressNote = "", pountContractAddress = "", txasContractAddress = "", asoTokenAddress = "", pledgeTotal = "0", valuesAggregatorAddress = "", lpExchangeAddress = "", minTokens = "", deadline = "", msgValue = "", tokensSold = "", minTrx = "", recipient = "", secret = "", contractAddress = "", usdtTokensSold = "", minTokensBought = "";
    private BigDecimal decimals, exTokenBalace, exTrxBalance, trxToToken, tokenToTrx, userTrxAmount, userTokenAmount, lpBalance, asoBalance, totalLiquidity, allowance, slidingPoint, energy, net, trxToEnergy, trxToNet, energyBalance, netBalance, trxBalance, usdtBalance, usdjBalance, usdtExTokenBalace, usdtExTrxBalance, usdtTrxToToken, usdtTokenToTrx, usdtAllowance;
    private Timer timer;
    private TimerTask task;
    private int number, exchangeType, transferType = -1;
    private TextView mTextView_balance, mTextView_net_text, mTextView_net, mTextView_energy_text, mTextView_energy1, mTextView_trxToAso, mTextView_minAso, mTextView_priceImpact, mTextView_fee, mTextView_expectedAdd, mTextView_asoExchange;
    private EditText mEditText_trx;

    @Override
    public void onResume() {
        super.onResume();
        if (!spUtils1.getString("tronChildAddress", "").isEmpty()) {
            if (isFirst) {
                getUserInfo(true, true);
            } else {
                if (spUtils1.getBoolean("isPageRefresh", false)) {
                    getUserInfo(true, true);
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
                        if (tronAddress.isEmpty()) {
                            return;
                        }
                        pledgeInfo(true);
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
        return R.layout.fragment_tron_wallet_layout;
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
        setPullRefresher();
        slidingPoint = new BigDecimal("0.005");
        energyBalance = new BigDecimal("0");
        netBalance = new BigDecimal("0");
        trxBalance = new BigDecimal("-1");
        if (!spUtils1.getString("tronChildAddress", "").isEmpty()) {
            mLinearLayout_import_tron.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            mLinearLayout_import_tron.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.GONE);
        }
    }

    private void getUserInfo(boolean isShow, boolean isTime) {
        List<TronDBHelperBean> beanList = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
        for (int i = 0; i < beanList.size(); i++) {
            if (spUtils1.getString("tronChildAddress", "").equals(beanList.get(i).getAddress())) {
                mRelativeLayout_copy.setVisibility(View.VISIBLE);
                mTextView_account.setText(beanList.get(i).getAccount());
                mTextView_address.setText(beanList.get(i).getAddress());
                secret = beanList.get(i).getSecret();
                tronAddress = beanList.get(i).getAddress();
            }
        }
        if (isShow) {
            myDialog = DialogUtils.createLoadingDialog(mContext, "链上数据加载中...");
        }
        pledgeInfo(isTime);
    }

    @OnClick({R.id.mRelativeLayout_delete, R.id.mRelativeLayout_privateKey, R.id.mRelativeLayout_import, R.id.mTextView_btn_switch, R.id.mRelativeLayout_copy, R.id.mImageView_import_tron, R.id.mRelativeLayout_aso, R.id.mRelativeLayout_USDT, R.id.mRelativeLayout_USDJ, R.id.mRelativeLayout_trx, R.id.mTextView_btn_transfer, R.id.mTextView_btn_exchange, R.id.mLinearLayout_locked, R.id.mShadowLayout_top_resource, R.id.mTextView_btn_freeze})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mRelativeLayout_delete:
                if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                    ToastUtil.showShort(mContext, "您还没有账户可删除");
                    return;
                }
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    dialogAuthentication(0);
                }
                break;
            case R.id.mRelativeLayout_privateKey:
                if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                    ToastUtil.showShort(mContext, "请先导入账户");
                    return;
                }
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    dialogAuthentication(1);
                }
                break;
            case R.id.mImageView_import_tron:
            case R.id.mRelativeLayout_import:
                if (!spUtils.getBoolean("isDeal", false)) {
                    ToastUtil.showLong(mContext, "请先到账户中心设置交易密码");
                    return;
                }
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    if (spUtils.getString("tronAddress", "").isEmpty()) {
                        getTronAddress();
                    } else {
                        isBinding = false;
                        dialogAuthentication(2);
                    }
                }
                break;
            case R.id.mTextView_btn_switch:
                List<TronDBHelperBean> beanList = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                if (beanList.isEmpty()) {
                    ToastUtil.showLong(mContext, "当前没有账户可切换，请先导入账户");
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
                            spUtils1.putString("tronChildAddress", item.substring(item.indexOf("]") + 1, item.length()));
                            mTextView_address.setText(item.substring(item.indexOf("]") + 1, item.length()));
                            mTextView_account.setText(item.substring(1, item.indexOf("]")));
                            asoBalance = null;
                            getUserInfo(true, false);
                            EventBus.getDefault().postSticky(new EventMessageBean(28, null));
                        }
                    });
                    picker.show();
                }
                break;
            case R.id.mRelativeLayout_copy:
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("TronAddress", mTextView_address.getText().toString());
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort(mContext, "Tron账户已复制到剪贴板");
                break;
            case R.id.mRelativeLayout_aso:
                Intent intentAso = new Intent(mContext, Trc20Activity.class);
                intentAso.putExtra("title", "Areso");
                intentAso.putExtra("balance", mTextView_aso.getText().toString());
                intentAso.putExtra("contractAddress", asoTokenAddress);
                intentAso.putExtra("trxBalance", trxBalance.toString());
                intentAso.putExtra("netBalance", netBalance.toString());
                intentAso.putExtra("energyBalance", energyBalance.toString());
                startActivity(intentAso);
                break;
            case R.id.mRelativeLayout_USDT:
                Intent intentUSDT = new Intent(mContext, Trc20Activity.class);
                intentUSDT.putExtra("title", "Tether USD");
                intentUSDT.putExtra("balance", mTextView_USDT.getText().toString());
                intentUSDT.putExtra("contractAddress", Content.usdtContractAddress);
                intentUSDT.putExtra("trxBalance", trxBalance.toString());
                intentUSDT.putExtra("netBalance", netBalance.toString());
                intentUSDT.putExtra("energyBalance", energyBalance.toString());
                startActivity(intentUSDT);
                break;
            case R.id.mRelativeLayout_USDJ:
                Intent intentUSDJ = new Intent(mContext, Trc20Activity.class);
                intentUSDJ.putExtra("title", "JUST Stablecoin");
                intentUSDJ.putExtra("balance", mTextView_USDJ.getText().toString());
                intentUSDJ.putExtra("contractAddress", "TMwFHYXLJaRUPeW6421aqXL4ZEzPRFGkGT");
                intentUSDJ.putExtra("trxBalance", trxBalance.toString());
                intentUSDJ.putExtra("netBalance", netBalance.toString());
                intentUSDJ.putExtra("energyBalance", energyBalance.toString());
                startActivity(intentUSDJ);
                break;
            case R.id.mRelativeLayout_trx:
                Intent intentTrx = new Intent(mContext, Trc20Activity.class);
                intentTrx.putExtra("title", "TRX");
                if (trxBalance.floatValue() == -1f) {
                    intentTrx.putExtra("balance", "0.000");
                } else {
                    intentTrx.putExtra("balance", trxBalance.toString());
                }
                intentTrx.putExtra("contractAddress", "");
                intentTrx.putExtra("trxBalance", trxBalance.toString());
                intentTrx.putExtra("netBalance", netBalance.toString());
                intentTrx.putExtra("energyBalance", energyBalance.toString());
                startActivity(intentTrx);
                break;
            case R.id.mTextView_btn_transfer:
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    transferType = 0;
                    myDialog2 = DialogUtils.createLoadingDialog(mContext, "查询余额...");
                    getAddressInfo(true);
                }
                break;
            case R.id.mTextView_btn_exchange:
                if (isClick) {
                    return;
                } else {
                    if (asoBalance == null || usdtExTokenBalace == null) {
                        ToastUtil.showShort(mContext, "正在加载数据，请稍候或刷新再试");
                    } else {
                        isClick = true;
                        exchangeDialog();
                    }
                }
                break;
            case R.id.mLinearLayout_locked:
            case R.id.mShadowLayout_top_resource:
            case R.id.mTextView_btn_freeze:
                Intent intentTR = new Intent(mContext, TronResourceActivity.class);
                startActivity(intentTR);
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
            case 29:
            case 23:
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!tronAddress.isEmpty()) {
                            pledgeInfo(false);
                        }
                    }
                });
                break;
            case 20:
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pledgeTotal = "0";
                        mTextView_TXas.setText("0.000");
                    }
                });
                break;
            case 9:
                if (spUtils1.getString("tronChildAddress", "").isEmpty()) {
                    mLinearLayout_import_tron.setVisibility(View.VISIBLE);
                    mRefreshLayout.setVisibility(View.GONE);
                } else {
                    getUserInfo(false, false);
                }
                break;
            case 24:
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!spUtils1.getString("tronChildAddress", "").isEmpty()) {
                            mLinearLayout_import_tron.setVisibility(View.GONE);
                            mRefreshLayout.setVisibility(View.VISIBLE);
                            getUserInfo(false, false);
                        }
                    }
                });
                break;
        }
    }

    private void getTronAddress() {
        myDialog1 = DialogUtils.createLoadingDialog(mContext, "获取TRON绑定信息中...");
        OkHttpClient.initGet(Http.getTronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog1);
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
                            DialogUtils.closeDialog(myDialog1);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                spUtils.putString("tronAddress", jsonObject.getString("data"));
                                if (jsonObject.getString("data").isEmpty()) {
                                    isBinding = true;
                                } else {
                                    isBinding = false;
                                }
                                dialogAuthentication(2);
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

    private void pledgeInfo(boolean isTime) {
        isPause = true;
        OkHttpClient.initGet(Http.pledgeInfo).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isPause = false;
                        DialogUtils.closeDialog(myDialog);
                        mRefreshLayout.finishRefresh(1000);
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
                                trxToEnergy = new BigDecimal(jsonObject.getJSONObject("data").getString("trxToEnergy"));
                                trxToNet = new BigDecimal(jsonObject.getJSONObject("data").getString("trxToNet"));
                                if (!isTime) {
                                    pledgeTotal = "0";
                                    mTextView_trx.setText("？TRX");
                                    mTextView_trx_bottom.setText("？");
                                    mTextView_TXas.setText("？");
                                    mTextView_USDT.setText("？");
                                    mTextView_USDJ.setText("？");
                                    mTextView_aso.setText("？");
                                    mTextView_available.setText("？");
                                    mTextView_TXas.setText("？");
                                    mTextView_energy.setText("？");
                                    mTextView_bandwidth.setText("？");
                                    mTextView_freeze.setText("？");
                                }
                                getLpInfo();
                                getUsdtInfo();
                                getAddressInfo(false);
                            } else {
                                mRefreshLayout.finishRefresh(1000);
                                isPause = false;
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            isPause = false;
                            mRefreshLayout.finishRefresh(1000);
                            ToastUtil.showShort(mContext, "获取合约信息报错，请刷新再试");
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
        }
        mWebView.loadUrl(url);
    }

    private class BalanceJavaScriptInterface {
        @JavascriptInterface
        public void selectFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        ToastUtil.showShort(mContext, str);
                    } else {
                        pledgeTotal = new BigDecimal(str).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString();
                        mTextView_TXas.setText(new BigDecimal(str).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                    }
                }
            });
        }
    }

    private void getAddressInfo(boolean isTransfer) {
        isPause = true;
        OkHttpClient.initGet(Http.getTronInfo + tronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isPause = false;
                        isClick = false;
                        mRefreshLayout.finishRefresh(1000);
                        DialogUtils.closeDialog(myDialog);
                        DialogUtils.closeDialog(myDialog2);
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
                            isPause = false;
                            isClick = false;
                            mRefreshLayout.finishRefresh(1000);
                            DialogUtils.closeDialog(myDialog);
                            DialogUtils.closeDialog(myDialog2);
                            JSONObject jsonObject = new JSONObject(result);
                            mTextView_trx.setText(new BigDecimal(jsonObject.getString("balance")).divide(new BigDecimal("1000000"), 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString() + " TRX");
                            mTextView_trx_bottom.setText(new BigDecimal(jsonObject.getString("balance")).divide(new BigDecimal("1000000"), 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                            mTextView_available.setText(new BigDecimal(jsonObject.getString("balance")).divide(new BigDecimal("1000000"), 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                            BigDecimal freeNetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetLimit"));
                            BigDecimal freeNetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetUsed"));
                            BigDecimal NetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netUsed"));
                            BigDecimal NetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netLimit"));
                            BigDecimal energyLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyLimit"));
                            BigDecimal energyUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyUsed"));
                            BigDecimal frozenTotal = new BigDecimal(jsonObject.getJSONObject("frozen").getString("total"));
                            BigDecimal frozen_balance;
                            if (!jsonObject.getJSONObject("accountResource").getJSONObject("frozen_balance_for_energy").isNull("frozen_balance")) {
                                frozen_balance = new BigDecimal(jsonObject.getJSONObject("accountResource").getJSONObject("frozen_balance_for_energy").getString("frozen_balance"));
                            } else {
                                frozen_balance = new BigDecimal("0");
                            }
                            trxBalance = new BigDecimal(jsonObject.getString("balance")).divide(new BigDecimal("1000000"), 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                            if (energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN).floatValue() <= 0f) {
                                energyBalance = new BigDecimal("0");
                                mTextView_energy.setText("0/" + energyLimit.setScale(0, BigDecimal.ROUND_DOWN).toString());
                            } else {
                                energyBalance = energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN);
                                mTextView_energy.setText(energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN).toString() + "/" + energyLimit.setScale(0, BigDecimal.ROUND_DOWN).toString());
                            }
                            if (freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN).floatValue() <= 0f) {
                                netBalance = new BigDecimal("0");
                                mTextView_bandwidth.setText("0/" + freeNetLimit.add(NetLimit).setScale(0, BigDecimal.ROUND_DOWN).toString());
                            } else {
                                netBalance = freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN);
                                mTextView_bandwidth.setText(freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN).toString() + "/" + freeNetLimit.add(NetLimit).setScale(0, BigDecimal.ROUND_DOWN).toString());
                            }
                            mTextView_freeze.setText(frozenTotal.add(frozen_balance).divide(new BigDecimal("1000000"), 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                            mTextView_USDT.setText("0.000");
                            mTextView_USDJ.setText("0.000");
                            usdtBalance = new BigDecimal("0");
                            usdjBalance = new BigDecimal("0");
                            if (jsonObject.getJSONArray("trc20token_balances").length() == 0) {
                                mTextView_USDT.setText("0");
                                mTextView_USDJ.setText("0");
                                usdtBalance = new BigDecimal("0");
                                usdjBalance = new BigDecimal("0");
                            } else {
                                for (int i = 0; i < jsonObject.getJSONArray("trc20token_balances").length(); i++) {
                                    if (Content.usdtContractAddress.equals(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("tokenId"))) {
                                        mTextView_USDT.setText(new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                        usdtBalance = new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                    }
                                    if ("TMwFHYXLJaRUPeW6421aqXL4ZEzPRFGkGT".equals(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("tokenId"))) {
                                        mTextView_USDJ.setText(new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                        usdjBalance = new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                    }
                                }
                            }
                            if (isTransfer) {
                                if (mTextView_balance != null && dialog != null && dialog.isShowing()) {
                                    if (transferType == 0) {
                                        mTextView_balance.setText(trxBalance.setScale(3, BigDecimal.ROUND_DOWN).toString() + " TRX");
                                    } else if (transferType == 2) {
                                        mTextView_balance.setText(usdtBalance.toString() + " USDT");
                                    } else if (transferType == 3) {
                                        mTextView_balance.setText(usdjBalance.toString() + " USDJ");
                                    }
                                }
                                myDialog1 = DialogUtils.createLoadingDialog(mContext, "预估转账手续费...");
                                getTronTransferEnergy();
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "获取账户信息错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void dialogDelete() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete_layout, null);
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
                TronSQLUtils.DaleteSql(mContext, spUtils1.getString("tronChildAddress"), spUtils.getString("phone"));
                dialog.dismiss();
                ToastUtil.showShort(mContext, "删除成功");
                List<TronDBHelperBean> beanList = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                if (beanList.isEmpty()) {
                    mLinearLayout_import_tron.setVisibility(View.VISIBLE);
                    mRefreshLayout.setVisibility(View.GONE);
                    spUtils1.putString("tronChildAddress", "");
                    mTextView_account.setText("");
                    mRelativeLayout_copy.setVisibility(View.GONE);
                    mTextView_trx.setText("0.000 TRX");
                    mTextView_bandwidth.setText("0/0");
                    mTextView_energy.setText("0/0");
                    mTextView_TXas.setText("0.000");
                    mTextView_aso.setText("0.000");
                    mTextView_USDT.setText("0.000");
                    mTextView_USDJ.setText("0.000");
                } else {
                    spUtils1.putString("tronChildAddress", beanList.get(0).getAddress());
                    getUserInfo(true, false);
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

    private void dialogAuthentication(int type) {
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
                myDialog1 = DialogUtils.createLoadingDialog(mContext, "验证中...");
                verifyAccount(mEditText_verification.getText().toString(), type);
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

    private void dialogCreator(boolean isExport) {
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
                if (!isExport) {
                    getUserInfo(true, false);
                }
                EventBus.getDefault().postSticky(new EventMessageBean(28, null));
                dialog.dismiss();
                dialog1.dismiss();
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

    private void verifyAccount(String password, int type) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("password", AESUtil.encrypt(spUtils.getString("phone"), password));
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject1.toString());
        OkHttpClient.initPost(Http.verifyAccount, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog1);
                        ToastUtil.showShort(mContext, "网络错误，身份验证失败");
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
                                tradePassword = password;
                                if (type == 0) {
                                    dialogDelete();
                                } else if (type == 1) {
                                    creatorAddressDialog(true);
                                } else {
                                    if (isBinding) {
                                        bindAddressDialog();
                                    } else {
                                        importAddressDialog();
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

    private void importAddressDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_import_tron_layout, null);
        EditText mEditText_note = v.findViewById(R.id.mEditText_note);
        EditText mEditText_privateKey = v.findViewById(R.id.mEditText_privateKey);

        v.findViewById(R.id.mLinearLayout_creatorAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText_note.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "账户备注不能为空");
                    return;
                }
                List<TronDBHelperBean> beanList = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                for (int i = 0; i < beanList.size(); i++) {
                    if (mEditText_note.getText().toString().equals(beanList.get(i).getAccount())) {
                        ToastUtil.showShort(mContext, "账户备注不能重复");
                        return;
                    }
                }
                addressNote = mEditText_note.getText().toString();
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
        v.findViewById(R.id.mTextView_btn_import).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKeyBoard(v);
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
                myDialog1 = DialogUtils.createLoadingDialog(mContext, "导入中...");
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

    private void bindAddressDialog() {
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
                        DialogUtils.closeDialog(myDialog1);
                        jsonObject = new JSONObject(str);
                        tronAddress = jsonObject.getJSONObject("address").getString("base58");
                        privateKey = jsonObject.getString("privateKey");
                        dialog.dismiss();
                        creatorAddressDialog(false);
                    } catch (JSONException e) {
                    }
                }
            });
        }
    }

    private void creatorAddressDialog(boolean isExport) {
        if (!isExport) {
            mLinearLayout_import_tron.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.VISIBLE);
            if (isBinding) {
                myDialog1.setMsg("绑定中...");
                bindTronAddress(tronAddress, privateKey, true);
            } else {
                try {
                    TronSQLUtils.AddSql(mContext, addressNote, tronAddress, AESUtil.encrypt(privateKey, tradePassword), spUtils.getString("phone") + "@");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                spUtils1.putString("tronChildAddress", tronAddress);
            }
        }
        View creatorDialog = LayoutInflater.from(mContext).inflate(R.layout.dialog_set_lock_creator_address_layout, null);
        TextView mTextView_address = creatorDialog.findViewById(R.id.mTextView_address);
        TextView mTextView_privateKey = creatorDialog.findViewById(R.id.mTextView_privateKey);
        TextView mTextView_title = creatorDialog.findViewById(R.id.mTextView_title);
        if (!isExport) {
            mTextView_address.setText(tronAddress);
            mTextView_privateKey.setText(privateKey);
            mTextView_title.setText("新建TRON地址");
        } else {
            List<TronDBHelperBean> beans = TronSQLUtils.QuerySQL(mContext, spUtils1.getString("tronChildAddress"), spUtils.getString("phone"));
            mTextView_address.setText(beans.get(0).getAddress());
            try {
                mTextView_privateKey.setText(AESUtil.decrypt(beans.get(0).getSecret(), tradePassword));
            } catch (Exception e) {
                e.printStackTrace();
            }
            mTextView_title.setText("导出密钥");
        }

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
                ToastUtil.showShort(mContext, "账户私钥已复制到剪贴板");
            }
        });

        creatorDialog.findViewById(R.id.mTextView_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCreator(isExport);
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

    private void verification() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        JavaScriptInterface javaScriptInterface = new JavaScriptInterface();
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

    private class JavaScriptInterface {
        @JavascriptInterface
        public void startFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        DialogUtils.closeDialog(myDialog1);
                        ToastUtil.showShort(mContext, "私钥不正确");
                    } else {
                        if (!isBinding) {
                            List<TronDBHelperBean> beanList = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                            for (int i = 0; i < beanList.size(); i++) {
                                if (str.equals(beanList.get(i).getAddress())) {
                                    ToastUtil.showShort(mContext, "该账户已导入，请勿重复导入");
                                    DialogUtils.closeDialog(myDialog1);
                                    return;
                                }
                            }
                        }
                        tronAddress = str;
                        mLinearLayout_import_tron.setVisibility(View.GONE);
                        mRefreshLayout.setVisibility(View.VISIBLE);
                        if (isBinding) {
                            bindTronAddress(tronAddress, editPrivateKey, false);
                        } else {
                            try {
                                TronSQLUtils.AddSql(mContext, addressNote, tronAddress, AESUtil.encrypt(editPrivateKey, tradePassword), spUtils.getString("phone") + "@");
                                DialogUtils.closeDialog(myDialog1);
                                dialog.dismiss();
                                ToastUtil.showShort(mContext, "导入成功");
                                spUtils1.putString("tronChildAddress", tronAddress);
                                EventBus.getDefault().postSticky(new EventMessageBean(28, null));
                                getUserInfo(true, false);
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
                                EventBus.getDefault().postSticky(new EventMessageBean(28, null));
                                try {
                                    TronSQLUtils.AddSql(mContext, "TRON", tronAddress, AESUtil.encrypt(tronPrivateKey, tradePassword), spUtils.getString("phone") + "@");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (!isCreator) {
                                    getUserInfo(true, false);
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
                            mTextView_aso.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                            if (!isSwitchUsdt && mEditText_trx != null && dialog != null && dialog.isShowing()) {
                                if (!mEditText_trx.getText().toString().isEmpty() && Float.parseFloat(mEditText_trx.getText().toString()) != 0f) {
                                    if (isSwitch) {
                                        BigDecimal inputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                                        BigDecimal numerator = inputAmountWithFee.multiply(exTrxBalance);
                                        BigDecimal denominator = exTokenBalace.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                                        BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);
                                        BigDecimal priceImpact = inputPrice.subtract(tokenToTrx).abs().divide(tokenToTrx, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                                        if (priceImpact.floatValue() < 0.01) {
                                            mTextView_priceImpact.setText("<0.01%");
                                        } else {
                                            mTextView_priceImpact.setText(priceImpact + "%");
                                        }
                                        mTextView_trxToAso.setText("1 ASO 兑换 " + inputPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " TRX");
                                        mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                        mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " TRX");
                                        mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.003")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                                        mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " ASO");
                                        tokensSold = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                                        minTrx = new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                                    } else {
                                        BigDecimal inputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                                        BigDecimal numerator = inputAmountWithFee.multiply(exTokenBalace);
                                        BigDecimal denominator = exTrxBalance.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                                        BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);
                                        BigDecimal priceImpact = inputPrice.subtract(trxToToken).abs().divide(trxToToken, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                                        if (priceImpact.floatValue() < 0.01) {
                                            mTextView_priceImpact.setText("<0.01%");
                                        } else {
                                            mTextView_priceImpact.setText(priceImpact + "%");
                                        }
                                        mTextView_trxToAso.setText("1 TRX 兑换 " + inputPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " ASO");
                                        mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                        mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                                        mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.003")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " TRX");
                                        mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " TRX");
                                        msgValue = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                                        minTokens = new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                }
            });
        }
    }

    private void getUsdtInfo() {
        mWebView2 = new WebView(mContext);
        mWebView2.getSettings().setJavaScriptEnabled(true);
        mWebView2.getSettings().setAppCacheEnabled(false);
        mWebView2.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView2.getSettings().setBlockNetworkImage(false);
        mWebView2.getSettings().setBlockNetworkLoads(false);
        mWebView2.clearCache(true);
        UsdtInfoJavaScriptInterface javaScriptInterface = new UsdtInfoJavaScriptInterface();
        mWebView2.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView2.loadUrl("file:///android_asset/selectUsdtInfo.html");

        mWebView2.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                getUsdtInfoJsParseMainAssetTra();
            }
        });
    }

    private void getUsdtInfoJsParseMainAssetTra() {
        String url = null;
        try {
            url = "javascript:selectUsdtInfo('" + valuesAggregatorAddress + "','" + tronAddress + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
        }
        mWebView2.loadUrl(url);
    }

    private class UsdtInfoJavaScriptInterface {
        @JavascriptInterface
        public void selectUsdtInfoFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        ToastUtil.showShort(mContext, str);
                    } else {
                        try {
                            JSONObject singleInfoObject = new JSONObject(str);
                            usdtExTokenBalace = new BigDecimal(TronUtils.toBigInt(singleInfoObject.getJSONObject("_exTokenBalace").getString("_hex")) + "").divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN);
                            usdtExTrxBalance = new BigDecimal(TronUtils.toBigInt(singleInfoObject.getJSONObject("_exTrxBalance").getString("_hex")) + "").divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN);
                            usdtTrxToToken = usdtExTokenBalace.divide(usdtExTrxBalance, 12, BigDecimal.ROUND_DOWN).setScale(12, BigDecimal.ROUND_DOWN);
                            usdtTokenToTrx = usdtExTrxBalance.divide(usdtExTokenBalace, 12, BigDecimal.ROUND_DOWN).setScale(12, BigDecimal.ROUND_DOWN);
                            usdtAllowance = new BigDecimal(TronUtils.toBigInt(singleInfoObject.getJSONObject("_allowance").getString("_hex")) + "").divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN);

                            if (isSwitchUsdt && mEditText_trx != null && dialog != null && dialog.isShowing()) {
                                if (!mEditText_trx.getText().toString().isEmpty() && Float.parseFloat(mEditText_trx.getText().toString()) != 0f) {
                                    if (isSwitch) {
                                        BigDecimal inputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                                        BigDecimal numerator = inputAmountWithFee.multiply(exTrxBalance);
                                        BigDecimal denominator = exTokenBalace.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                                        BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);

                                        BigDecimal usdtInputAmountWithFee = inputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())).multiply(new BigDecimal("997"));
                                        BigDecimal usdtNumerator = usdtInputAmountWithFee.multiply(usdtExTokenBalace);
                                        BigDecimal usdtDenominator = usdtExTrxBalance.multiply(new BigDecimal("1000")).add(usdtInputAmountWithFee);
                                        BigDecimal usdtInputPrice = usdtNumerator.divide(usdtDenominator, 12, BigDecimal.ROUND_DOWN).divide(inputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())), 12, BigDecimal.ROUND_DOWN);

                                        BigDecimal usdtPrice = inputPrice.multiply(usdtInputPrice);

                                        BigDecimal priceImpact = inputPrice.subtract(tokenToTrx).abs().divide(tokenToTrx, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                                        if (priceImpact.floatValue() < 0.01) {
                                            mTextView_priceImpact.setText("<0.01%");
                                        } else {
                                            mTextView_priceImpact.setText(priceImpact + "%");
                                        }
                                        mTextView_trxToAso.setText("1 ASO 兑换 " + usdtPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " USDT");
                                        mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                        mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " USDT");
                                        mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.006")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                                        mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " ASO");
                                        usdtTokensSold = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                                        minTokensBought = new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                                    } else {
                                        BigDecimal usdtInputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                                        BigDecimal usdtNumerator = usdtInputAmountWithFee.multiply(usdtExTrxBalance);
                                        BigDecimal usdtDenominator = usdtExTokenBalace.multiply(new BigDecimal("1000")).add(usdtInputAmountWithFee);
                                        BigDecimal usdtInputPrice = usdtNumerator.divide(usdtDenominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);

                                        BigDecimal inputAmountWithFee = usdtInputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())).multiply(new BigDecimal("997"));
                                        BigDecimal numerator = inputAmountWithFee.multiply(exTokenBalace);
                                        BigDecimal denominator = exTrxBalance.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                                        BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(usdtInputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())), 12, BigDecimal.ROUND_DOWN);

                                        BigDecimal usdtPrice = usdtInputPrice.multiply(inputPrice);

                                        BigDecimal priceImpact = inputPrice.subtract(trxToToken).abs().divide(trxToToken, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                                        if (priceImpact.floatValue() < 0.01) {
                                            mTextView_priceImpact.setText("<0.01%");
                                        } else {
                                            mTextView_priceImpact.setText(priceImpact + "%");
                                        }
                                        mTextView_trxToAso.setText("1 USDT 兑换 " + usdtPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " ASO");
                                        mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                        mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                                        mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.006")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " USDT");
                                        mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " USDT");
                                        usdtTokensSold = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                                        minTokensBought = new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                }
            });
        }
    }

    private void exchangeDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_exchange_layout, null);
        TextView mTextView_title = v.findViewById(R.id.mTextView_title);
        TextView mTextView_switch_usdt = v.findViewById(R.id.mTextView_switch_usdt);
        ImageView mImageView_trx = v.findViewById(R.id.mImageView_trx);
        TextView mTextView_trxTitle = v.findViewById(R.id.mTextView_trxTitle);
        TextView mTextView_trxBalance = v.findViewById(R.id.mTextView_trxBalance);
        mEditText_trx = v.findViewById(R.id.mEditText_trx);
        LottieAnimationView mLottieAnimationView_switch = v.findViewById(R.id.mLottieAnimationView_switch);
        mTextView_trxToAso = v.findViewById(R.id.mTextView_trxToAso);
        ImageView mImageView_aso = v.findViewById(R.id.mImageView_aso);
        TextView mTextView_asoTitle = v.findViewById(R.id.mTextView_asoTitle);
        TextView mTextView_asoBalance = v.findViewById(R.id.mTextView_asoBalance);
        mTextView_asoExchange = v.findViewById(R.id.mTextView_aso);
        RelativeLayout mRelativeLayout_addAddress = v.findViewById(R.id.mRelativeLayout_addAddress);
        EditText mEditText_receiverAddress = v.findViewById(R.id.mEditText_receiverAddress);
        TextView mTextView_addAddress = v.findViewById(R.id.mTextView_addAddress);
        mTextView_minAso = v.findViewById(R.id.mTextView_minAso);
        mTextView_priceImpact = v.findViewById(R.id.mTextView_priceImpact);
        mTextView_fee = v.findViewById(R.id.mTextView_fee);
        EditText mEditText_password = v.findViewById(R.id.mEditText_password);
        mTextView_expectedAdd = v.findViewById(R.id.mTextView_expectedAdd);

        exchangeType = 1;
        isSwitch = false;
        isAddAddress = false;
        isSwitchUsdt = false;

        mLottieAnimationView_switch.setAnimation(R.raw.trx_aso);
        mLottieAnimationView_switch.setSpeed(0.5f);
        mLottieAnimationView_switch.setRepeatCount(-1);
        mLottieAnimationView_switch.playAnimation();

        mTextView_trxBalance.setText(trxBalance.toString());
        mTextView_asoBalance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());

        mEditText_trx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditText_trx.getText().toString().isEmpty() || Float.parseFloat(mEditText_trx.getText().toString()) == 0f) {
                    mTextView_asoExchange.setText("0");
                    if (isSwitchUsdt) {
                        if (isSwitch) {
                            mTextView_trxToAso.setText("1 ASO 兑换 - USDT");
                            mTextView_minAso.setText("0.000000 USDT");
                            mTextView_fee.setText("0.000 ASO");
                            mTextView_priceImpact.setText("0.00%");
                            mTextView_expectedAdd.setText("0.000 ASO");
                            usdtTokensSold = "0";
                            minTokensBought = "0";
                        } else {
                            mTextView_trxToAso.setText("1 USDT 兑换 - ASO");
                            mTextView_minAso.setText("0.000000 ASO");
                            mTextView_fee.setText("0.000 USDT");
                            mTextView_priceImpact.setText("0.00%");
                            mTextView_expectedAdd.setText("0.000 USDT");
                            usdtTokensSold = "0";
                            minTokensBought = "0";
                        }
                    } else {
                        if (isSwitch) {
                            mTextView_trxToAso.setText("1 ASO 兑换 - TRX");
                            mTextView_minAso.setText("0.000000 TRX");
                            mTextView_fee.setText("0.000 ASO");
                            mTextView_priceImpact.setText("0.00%");
                            mTextView_expectedAdd.setText("0.000 ASO");
                            tokensSold = "0";
                            minTrx = "0";
                        } else {
                            mTextView_trxToAso.setText("1 TRX 兑换 - ASO");
                            mTextView_minAso.setText("0.000000 ASO");
                            mTextView_fee.setText("0.000 TRX");
                            mTextView_priceImpact.setText("0.00%");
                            mTextView_expectedAdd.setText("0.000 TRX");
                            minTokens = "0";
                            msgValue = "0";
                        }
                    }
                } else {
                    if (isSwitchUsdt) {
                        if (isSwitch) {
                            BigDecimal inputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                            BigDecimal numerator = inputAmountWithFee.multiply(exTrxBalance);
                            BigDecimal denominator = exTokenBalace.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                            BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);

                            BigDecimal usdtInputAmountWithFee = inputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())).multiply(new BigDecimal("997"));
                            BigDecimal usdtNumerator = usdtInputAmountWithFee.multiply(usdtExTokenBalace);
                            BigDecimal usdtDenominator = usdtExTrxBalance.multiply(new BigDecimal("1000")).add(usdtInputAmountWithFee);
                            BigDecimal usdtInputPrice = usdtNumerator.divide(usdtDenominator, 12, BigDecimal.ROUND_DOWN).divide(inputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())), 12, BigDecimal.ROUND_DOWN);

                            BigDecimal usdtPrice = inputPrice.multiply(usdtInputPrice);

                            BigDecimal priceImpact = inputPrice.subtract(tokenToTrx).abs().divide(tokenToTrx, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                            if (priceImpact.floatValue() < 0.01) {
                                mTextView_priceImpact.setText("<0.01%");
                            } else {
                                mTextView_priceImpact.setText(priceImpact + "%");
                            }
                            mTextView_trxToAso.setText("1 ASO 兑换 " + usdtPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " USDT");
                            mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                            mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " USDT");
                            mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.006")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " ASO");
                            usdtTokensSold = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                            minTokensBought = new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                        } else {
                            BigDecimal usdtInputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                            BigDecimal usdtNumerator = usdtInputAmountWithFee.multiply(usdtExTrxBalance);
                            BigDecimal usdtDenominator = usdtExTokenBalace.multiply(new BigDecimal("1000")).add(usdtInputAmountWithFee);
                            BigDecimal usdtInputPrice = usdtNumerator.divide(usdtDenominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);

                            BigDecimal inputAmountWithFee = usdtInputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())).multiply(new BigDecimal("997"));
                            BigDecimal numerator = inputAmountWithFee.multiply(exTokenBalace);
                            BigDecimal denominator = exTrxBalance.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                            BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(usdtInputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())), 12, BigDecimal.ROUND_DOWN);

                            BigDecimal usdtPrice = usdtInputPrice.multiply(inputPrice);

                            BigDecimal priceImpact = inputPrice.subtract(trxToToken).abs().divide(trxToToken, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                            if (priceImpact.floatValue() < 0.01) {
                                mTextView_priceImpact.setText("<0.01%");
                            } else {
                                mTextView_priceImpact.setText(priceImpact + "%");
                            }
                            mTextView_trxToAso.setText("1 USDT 兑换 " + usdtPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                            mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.006")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " USDT");
                            mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " USDT");
                            usdtTokensSold = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                            minTokensBought = new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                        }
                    } else {
                        if (isSwitch) {
                            BigDecimal inputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                            BigDecimal numerator = inputAmountWithFee.multiply(exTrxBalance);
                            BigDecimal denominator = exTokenBalace.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                            BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);
                            BigDecimal priceImpact = inputPrice.subtract(tokenToTrx).abs().divide(tokenToTrx, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                            if (priceImpact.floatValue() < 0.01) {
                                mTextView_priceImpact.setText("<0.01%");
                            } else {
                                mTextView_priceImpact.setText(priceImpact + "%");
                            }
                            mTextView_trxToAso.setText("1 ASO 兑换 " + inputPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " TRX");
                            mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                            mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " TRX");
                            mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.003")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " ASO");
                            tokensSold = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                            minTrx = new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                        } else {
                            BigDecimal inputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                            BigDecimal numerator = inputAmountWithFee.multiply(exTokenBalace);
                            BigDecimal denominator = exTrxBalance.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                            BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);
                            BigDecimal priceImpact = inputPrice.subtract(trxToToken).abs().divide(trxToToken, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                            if (priceImpact.floatValue() < 0.01) {
                                mTextView_priceImpact.setText("<0.01%");
                            } else {
                                mTextView_priceImpact.setText(priceImpact + "%");
                            }
                            mTextView_trxToAso.setText("1 TRX 兑换 " + inputPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                            mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.003")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " TRX");
                            mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " TRX");
                            msgValue = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                            minTokens = new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                        }
                    }
                }
            }
        });

        v.findViewById(R.id.mTextView_switch_usdt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSwitchUsdt) {
                    isSwitchUsdt = false;
                    mTextView_title.setText("TRX/ASO交易");
                    mTextView_switch_usdt.setText("USDT/ASO交易");
                    if (isSwitch) {
                        mImageView_trx.setImageResource(R.mipmap.icon_aso);
                        mTextView_trxTitle.setText("将支付ASO");
                        mTextView_trxBalance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mImageView_aso.setImageResource(R.mipmap.icon_trx);
                        mTextView_asoTitle.setText("可兑换TRX（约）");
                        mTextView_asoBalance.setText(trxBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());

                        if (mEditText_trx.getText().toString().isEmpty() || Float.parseFloat(mEditText_trx.getText().toString()) == 0f) {
                            mTextView_trxToAso.setText("1 ASO 兑换 - TRX");
                            mTextView_minAso.setText("0.000000 TRX");
                            mTextView_fee.setText("0.000 ASO");
                            mTextView_priceImpact.setText("0.00%");
                            mTextView_expectedAdd.setText("0.000 ASO");
                            tokensSold = "0";
                            minTrx = "0";
                        } else {
                            BigDecimal inputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                            BigDecimal numerator = inputAmountWithFee.multiply(exTrxBalance);
                            BigDecimal denominator = exTokenBalace.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                            BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);
                            BigDecimal priceImpact = inputPrice.subtract(tokenToTrx).abs().divide(tokenToTrx, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                            if (priceImpact.floatValue() < 0.01) {
                                mTextView_priceImpact.setText("<0.01%");
                            } else {
                                mTextView_priceImpact.setText(priceImpact + "%");
                            }
                            mTextView_trxToAso.setText("1 ASO 兑换 " + inputPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " TRX");
                            mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                            mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " TRX");
                            mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.003")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " ASO");
                            tokensSold = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                            minTrx = new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                        }
                    } else {
                        mImageView_trx.setImageResource(R.mipmap.icon_trx);
                        mTextView_trxTitle.setText("将支付TRX");
                        mTextView_trxBalance.setText(trxBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mImageView_aso.setImageResource(R.mipmap.icon_aso);
                        mTextView_asoTitle.setText("可兑换ASO（约）");
                        mTextView_asoBalance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());

                        if (mEditText_trx.getText().toString().isEmpty() || Float.parseFloat(mEditText_trx.getText().toString()) == 0f) {
                            mTextView_trxToAso.setText("1 TRX 兑换 - ASO");
                            mTextView_minAso.setText("0.000000 ASO");
                            mTextView_fee.setText("0.000 TRX");
                            mTextView_priceImpact.setText("0.00%");
                            mTextView_expectedAdd.setText("0.000 TRX");
                            minTokens = "0";
                            msgValue = "0";
                        } else {
                            BigDecimal inputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                            BigDecimal numerator = inputAmountWithFee.multiply(exTokenBalace);
                            BigDecimal denominator = exTrxBalance.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                            BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);
                            BigDecimal priceImpact = inputPrice.subtract(trxToToken).abs().divide(trxToToken, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                            if (priceImpact.floatValue() < 0.01) {
                                mTextView_priceImpact.setText("<0.01%");
                            } else {
                                mTextView_priceImpact.setText(priceImpact + "%");
                            }
                            mTextView_trxToAso.setText("1 TRX 兑换 " + inputPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                            mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.003")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " TRX");
                            mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " TRX");
                            msgValue = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                            minTokens = new BigDecimal(mEditText_trx.getText().toString()).multiply(inputPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                        }
                    }
                } else {
                    isSwitchUsdt = true;
                    mTextView_title.setText("USDT/ASO交易");
                    mTextView_switch_usdt.setText("TRX/ASO交易");
                    if (isSwitch) {
                        mImageView_trx.setImageResource(R.mipmap.icon_aso);
                        mTextView_trxTitle.setText("将支付ASO");
                        mTextView_trxBalance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mImageView_aso.setImageResource(R.mipmap.icon_usdt);
                        mTextView_asoTitle.setText("可兑换USDT（约）");
                        mTextView_asoBalance.setText(usdtBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        if (mEditText_trx.getText().toString().isEmpty() || Float.parseFloat(mEditText_trx.getText().toString()) == 0f) {
                            mTextView_trxToAso.setText("1 ASO 兑换 - USDT");
                            mTextView_minAso.setText("0.000000 USDT");
                            mTextView_fee.setText("0.000 ASO");
                            mTextView_priceImpact.setText("0.00%");
                            mTextView_expectedAdd.setText("0.000 ASO");
                            usdtTokensSold = "0";
                            minTokensBought = "0";
                        } else {
                            BigDecimal inputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                            BigDecimal numerator = inputAmountWithFee.multiply(exTrxBalance);
                            BigDecimal denominator = exTokenBalace.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                            BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);

                            BigDecimal usdtInputAmountWithFee = inputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())).multiply(new BigDecimal("997"));
                            BigDecimal usdtNumerator = usdtInputAmountWithFee.multiply(usdtExTokenBalace);
                            BigDecimal usdtDenominator = usdtExTrxBalance.multiply(new BigDecimal("1000")).add(usdtInputAmountWithFee);
                            BigDecimal usdtInputPrice = usdtNumerator.divide(usdtDenominator, 12, BigDecimal.ROUND_DOWN).divide(inputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())), 12, BigDecimal.ROUND_DOWN);

                            BigDecimal usdtPrice = inputPrice.multiply(usdtInputPrice);

                            BigDecimal priceImpact = inputPrice.subtract(tokenToTrx).abs().divide(tokenToTrx, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                            if (priceImpact.floatValue() < 0.01) {
                                mTextView_priceImpact.setText("<0.01%");
                            } else {
                                mTextView_priceImpact.setText(priceImpact + "%");
                            }
                            mTextView_trxToAso.setText("1 ASO 兑换 " + usdtPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " USDT");
                            mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                            mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " USDT");
                            mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.006")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " ASO");
                            usdtTokensSold = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                            minTokensBought = new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                        }
                    } else {
                        mImageView_trx.setImageResource(R.mipmap.icon_usdt);
                        mTextView_trxTitle.setText("将支付USDT");
                        mTextView_trxBalance.setText(usdtBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mImageView_aso.setImageResource(R.mipmap.icon_aso);
                        mTextView_asoTitle.setText("可兑换ASO（约）");
                        mTextView_asoBalance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        if (mEditText_trx.getText().toString().isEmpty() || Float.parseFloat(mEditText_trx.getText().toString()) == 0f) {
                            mTextView_trxToAso.setText("1 USDT 兑换 - ASO");
                            mTextView_minAso.setText("0.000000 ASO");
                            mTextView_fee.setText("0.000 USDT");
                            mTextView_priceImpact.setText("0.00%");
                            mTextView_expectedAdd.setText("0.000 USDT");
                            usdtTokensSold = "0";
                            minTokensBought = "0";
                        } else {
                            BigDecimal usdtInputAmountWithFee = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("997"));
                            BigDecimal usdtNumerator = usdtInputAmountWithFee.multiply(usdtExTrxBalance);
                            BigDecimal usdtDenominator = usdtExTokenBalace.multiply(new BigDecimal("1000")).add(usdtInputAmountWithFee);
                            BigDecimal usdtInputPrice = usdtNumerator.divide(usdtDenominator, 12, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mEditText_trx.getText().toString()), 12, BigDecimal.ROUND_DOWN);

                            BigDecimal inputAmountWithFee = usdtInputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())).multiply(new BigDecimal("997"));
                            BigDecimal numerator = inputAmountWithFee.multiply(exTokenBalace);
                            BigDecimal denominator = exTrxBalance.multiply(new BigDecimal("1000")).add(inputAmountWithFee);
                            BigDecimal inputPrice = numerator.divide(denominator, 12, BigDecimal.ROUND_DOWN).divide(usdtInputPrice.multiply(new BigDecimal(mEditText_trx.getText().toString())), 12, BigDecimal.ROUND_DOWN);

                            BigDecimal usdtPrice = usdtInputPrice.multiply(inputPrice);

                            BigDecimal priceImpact = inputPrice.subtract(trxToToken).abs().divide(trxToToken, 6, BigDecimal.ROUND_DOWN).subtract(new BigDecimal("0.003")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                            if (priceImpact.floatValue() < 0.01) {
                                mTextView_priceImpact.setText("<0.01%");
                            } else {
                                mTextView_priceImpact.setText(priceImpact + "%");
                            }
                            mTextView_trxToAso.setText("1 USDT 兑换 " + usdtPrice.setScale(6, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_asoExchange.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).setScale(3, BigDecimal.ROUND_DOWN).toString());
                            mTextView_minAso.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                            mTextView_fee.setText(new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("0.006")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " USDT");
                            mTextView_expectedAdd.setText(new BigDecimal(mEditText_trx.getText().toString()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + " USDT");
                            usdtTokensSold = new BigDecimal(mEditText_trx.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                            minTokensBought = new BigDecimal(mEditText_trx.getText().toString()).multiply(usdtPrice).multiply(new BigDecimal("1").subtract(slidingPoint)).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString();
                        }
                    }
                }
            }
        });

        v.findViewById(R.id.mTextView_trxMax).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSwitchUsdt) {
                    if (isSwitch) {
                        mEditText_trx.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                    } else {
                        mEditText_trx.setText(usdtBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                    }
                } else {
                    if (isSwitch) {
                        mEditText_trx.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                    } else {
                        if (trxBalance.subtract(new BigDecimal("5")).floatValue() < 0f) {
                            mEditText_trx.setText("0");
                            ToastUtil.showShort(mContext, "当前TRX余额小于5个");
                        } else {
                            mEditText_trx.setText(trxBalance.subtract(new BigDecimal("5")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                        }
                    }
                }
            }
        });

        v.findViewById(R.id.mRelativeLayout_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSwitch) {
                    if (isAddAddress) {
                        exchangeType = 3;
                    } else {
                        exchangeType = 1;
                    }
                } else {
                    if (isAddAddress) {
                        exchangeType = 4;
                    } else {
                        exchangeType = 2;
                    }
                }
                if (isSwitchUsdt) {
                    if (isSwitch) {
                        mImageView_trx.setImageResource(R.mipmap.icon_usdt);
                        mTextView_trxTitle.setText("将支付USDT");
                        mTextView_trxBalance.setText(usdtBalance.toString());
                        mEditText_trx.setText("");
                        mImageView_aso.setImageResource(R.mipmap.icon_aso);
                        mTextView_asoTitle.setText("可兑换ASO（约）");
                        mTextView_asoBalance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mTextView_asoExchange.setText("");
                        mTextView_minAso.setText("0.000000 ASO");
                        mTextView_fee.setText("0.000 USDT");
                        mTextView_expectedAdd.setText("0.000 USDT");
                    } else {
                        mImageView_trx.setImageResource(R.mipmap.icon_aso);
                        mTextView_trxTitle.setText("将支付ASO");
                        mTextView_trxBalance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mEditText_trx.setText("");
                        mImageView_aso.setImageResource(R.mipmap.icon_usdt);
                        mTextView_asoTitle.setText("可兑换USDT（约）");
                        mTextView_asoBalance.setText(usdtBalance.toString());
                        mTextView_asoExchange.setText("");
                        mTextView_minAso.setText("0.000000 USDT");
                        mTextView_fee.setText("0.000 ASO");
                        mTextView_expectedAdd.setText("0.000 ASO");
                    }
                } else {
                    if (isSwitch) {
                        mImageView_trx.setImageResource(R.mipmap.icon_trx);
                        mTextView_trxTitle.setText("将支付TRX");
                        mTextView_trxBalance.setText(trxBalance.toString());
                        mEditText_trx.setText("");
                        mImageView_aso.setImageResource(R.mipmap.icon_aso);
                        mTextView_asoTitle.setText("可兑换ASO（约）");
                        mTextView_asoBalance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mTextView_asoExchange.setText("");
                        mTextView_minAso.setText("0.000000 ASO");
                        mTextView_fee.setText("0.000 TRX");
                        mTextView_expectedAdd.setText("0.000 TRX");
                    } else {
                        mImageView_trx.setImageResource(R.mipmap.icon_aso);
                        mTextView_trxTitle.setText("将支付ASO");
                        mTextView_trxBalance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());
                        mEditText_trx.setText("");
                        mImageView_aso.setImageResource(R.mipmap.icon_trx);
                        mTextView_asoTitle.setText("可兑换TRX（约）");
                        mTextView_asoBalance.setText(trxBalance.toString());
                        mTextView_asoExchange.setText("");
                        mTextView_minAso.setText("0.000000 TRX");
                        mTextView_fee.setText("0.000 ASO");
                        mTextView_expectedAdd.setText("0.000 ASO");
                    }
                }
                isSwitch = !isSwitch;
            }
        });

        v.findViewById(R.id.mTextView_addAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddAddress) {
                    if (isSwitch) {
                        exchangeType = 2;
                    } else {
                        exchangeType = 1;
                    }
                    mRelativeLayout_addAddress.setBackgroundResource(0);
                    mTextView_addAddress.setText("+ 添加接收方");
                    mTextView_addAddress.setTextColor(mContext.getResources().getColor(R.color.text_bg));
                    mEditText_receiverAddress.setText("");
                    mEditText_receiverAddress.setVisibility(View.GONE);
                } else {
                    if (isSwitch) {
                        exchangeType = 4;
                    } else {
                        exchangeType = 3;
                    }
                    mRelativeLayout_addAddress.setBackgroundResource(R.drawable.shape_white_radius_stroke_12dp);
                    mTextView_addAddress.setText("- 移除接收方");
                    mTextView_addAddress.setTextColor(mContext.getResources().getColor(R.color.red2));
                    mEditText_receiverAddress.setVisibility(View.VISIBLE);
                }
                isAddAddress = !isAddAddress;
            }
        });

        v.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });

        v.findViewById(R.id.mLinearLayout_btn_subscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                if (mEditText_trx.getText().toString().isEmpty() || Float.parseFloat(mEditText_trx.getText().toString()) <= 0f) {
                    ToastUtil.showShort(mContext, "兑换数量不能为0或空");
                    return;
                }
                if (!isSwitchUsdt && (exchangeType == 1 || exchangeType == 3)) {
                    if (Float.parseFloat(mEditText_trx.getText().toString()) > trxBalance.floatValue()) {
                        ToastUtil.showShort(mContext, "TRX余额不足");
                        return;
                    }
                }
                if (exchangeType == 2 || exchangeType == 4) {
                    if (Float.parseFloat(mEditText_trx.getText().toString()) > asoBalance.floatValue()) {
                        ToastUtil.showShort(mContext, "ASO余额不足");
                        return;
                    }
                }
                if (mEditText_password.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "交易密码不能为空");
                    return;
                }
                if (isAddAddress && mEditText_receiverAddress.getText().toString().isEmpty()) {
                    ToastUtil.showShort(mContext, "接收方地址不能为空");
                    return;
                }
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    recipient = mEditText_receiverAddress.getText().toString();
                    tradePassword = mEditText_password.getText().toString();
                    deadline = (System.currentTimeMillis() + Content.timePoor + (60 * 1000)) / 1000 + "";
                    myDialog1 = DialogUtils.createLoadingDialog(mContext, "预估兑换交易手续费...");
                    getTronExchangeEnergy();
                }
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

    private void getTronExchangeEnergy() {
        String url;
        if(isSwitchUsdt){
            if (exchangeType == 1) {
                url = Http.tronTransferEnergy + "?type=usdtToAso";
            } else if (exchangeType == 2) {
                url = Http.tronTransferEnergy + "?type=asoToUsdt";
            } else {
                url = Http.tronTransferEnergy + "?type=usdtDeal";
            }
        }else {
            if (exchangeType == 1) {
                url = Http.tronTransferEnergy + "?type=trxToAso";
            } else if (exchangeType == 2) {
                url = Http.tronTransferEnergy + "?type=asoToTrx";
            } else {
                url = Http.tronTransferEnergy + "?type=dealToRecipient";
            }
        }
        OkHttpClient.initGet(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog1);
                        net = new BigDecimal("0");
                        energy = new BigDecimal("0");
                        ToastUtil.showShort(mContext, "网络错误，预估兑换交易手续费失败");
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
                            if (jsonObject.getInt("code") == 1) {
                                net = new BigDecimal(jsonObject.getJSONObject("data").getString("net"));
                                energy = new BigDecimal(jsonObject.getJSONObject("data").getString("energy"));
                                BigDecimal burningTrx = new BigDecimal("0");
                                if (netBalance.floatValue() < net.floatValue()) {
                                    burningTrx = burningTrx.add(net.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (energyBalance.floatValue() < energy.floatValue()) {
                                    burningTrx = burningTrx.add(energy.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (trxBalance.floatValue() > burningTrx.floatValue()) {
                                    myDialog1.setMsg("链上兑换交易中...");
                                    exchangeAso();
                                } else {
                                    DialogUtils.closeDialog(myDialog1);
                                    dialogTrx(burningTrx, 2);
                                }
                            } else {
                                DialogUtils.closeDialog(myDialog1);
                                net = new BigDecimal("0");
                                energy = new BigDecimal("0");
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog1);
                            net = new BigDecimal("0");
                            energy = new BigDecimal("0");
                            ToastUtil.showShort(mContext, "预估兑换交易手续费错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void dialogTrx(BigDecimal burningTrx, int type) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_tron_trx_layout, null);
        TextView mTextView_msg = view.findViewById(R.id.mTextView_msg);
        TextView mTextView_false = view.findViewById(R.id.mTextView_false);
        TextView mTextView_true = view.findViewById(R.id.mTextView_true);

        String frozenTrx = energy.divide(frozenEnergy, BigDecimal.ROUND_HALF_UP, 3).setScale(3, BigDecimal.ROUND_HALF_UP).toString();

        mTextView_msg.setText("当前操作需要" + energy.setScale(0, BigDecimal.ROUND_DOWN).toString() + "能量（能量可预先冻结大约" + frozenTrx + "个TRX长期供能）能量不足需燃烧大约" + burningTrx.toString() + "个TRX，您的TRX余额不足建议您准备足够能量或TRX");

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
                if (type == 1) {
                    if (mTextView_net_text != null && dialog != null && dialog.isShowing()) {
                        if (netBalance.floatValue() < net.floatValue()) {
                            mTextView_net_text.setText("预计燃烧 " + net.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取带宽");
                            mTextView_net.setText("=" + net.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToNet).setScale(0, BigDecimal.ROUND_DOWN).toString());
                        } else {
                            mTextView_net_text.setText("预计消耗带宽");
                            mTextView_net.setText("≈" + net.setScale(0, BigDecimal.ROUND_DOWN).toString());
                        }
                        if (energyBalance.floatValue() < energy.floatValue()) {
                            mTextView_energy_text.setText("预计燃烧 " + energy.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取能量");
                            mTextView_energy1.setText("=" + energy.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
                        } else {
                            mTextView_energy_text.setText("预计消耗能量");
                            mTextView_energy1.setText("≈" + energy.setScale(0, BigDecimal.ROUND_DOWN).toString());
                        }
                    } else {
                        transferDialog();
                    }
                } else if (type == 2) {
                    myDialog1 = DialogUtils.createLoadingDialog(mContext, "链上兑换交易中...");
                    exchangeAso();
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

    private void exchangeAso() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        ExchangeAsoJavaScriptInterface javaScriptInterface = new ExchangeAsoJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/exchangeAso.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                exchangeAsoJsParseMainAssetTra();
            }
        });
    }

    private void exchangeAsoJsParseMainAssetTra() {
        String url = null;
        boolean isApprove = false;
        try {
            if (exchangeType == 1) {
                if (isSwitchUsdt) {
                    if (usdtAllowance.floatValue() > 0f && usdtAllowance.floatValue() >= new BigDecimal(usdtTokensSold).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP).floatValue()) {
                        isApprove = false;
                    } else {
                        isApprove = true;
                    }
                    url = "javascript:exchangeAsoU('" + AESUtil.decrypt(secret, tradePassword) + "','" + Content.usdtExContractAddress + "'," + usdtTokensSold + "," + minTokensBought + "," + deadline + ",'" + asoTokenAddress + "'," + isApprove + ",'" + Content.usdtContractAddress + "','" + Content.usdtExContractAddress + "','115792089237316195423570985008687907853269984665640564039457584007913129639935','" + Http.tronUrl + "')";
                } else {
                    url = "javascript:exchangeAso('" + AESUtil.decrypt(secret, tradePassword) + "','" + lpExchangeAddress + "'," + minTokens + "," + deadline + "," + msgValue + ",'" + Http.tronUrl + "')";
                }
            } else if (exchangeType == 2) {
                if (isSwitchUsdt) {
                    if (allowance.floatValue() > 0f && allowance.floatValue() >= new BigDecimal(usdtTokensSold).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP).floatValue()) {
                        isApprove = false;
                    } else {
                        isApprove = true;
                    }
                    url = "javascript:exchangeAsoU('" + AESUtil.decrypt(secret, tradePassword) + "','" + lpExchangeAddress + "'," + usdtTokensSold + "," + minTokensBought + "," + deadline + ",'" + Content.usdtContractAddress + "'," + isApprove + ",'" + asoTokenAddress + "','" + lpExchangeAddress + "','115792089237316195423570985008687907853269984665640564039457584007913129639935','" + Http.tronUrl + "')";
                } else {
                    if (allowance.floatValue() > 0f && allowance.floatValue() >= new BigDecimal(tokensSold).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP).floatValue()) {
                        isApprove = false;
                    } else {
                        isApprove = true;
                    }
                    url = "javascript:exchangeTrx('" + AESUtil.decrypt(secret, tradePassword) + "','" + lpExchangeAddress + "'," + tokensSold + "," + minTrx + "," + deadline + "," + isApprove + ",'" + asoTokenAddress + "','" + lpExchangeAddress + "','115792089237316195423570985008687907853269984665640564039457584007913129639935','" + Http.tronUrl + "')";
                }
            } else if (exchangeType == 3) {
                if (isSwitchUsdt) {
                    if (usdtAllowance.floatValue() > 0f && usdtAllowance.floatValue() >= new BigDecimal(usdtTokensSold).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP).floatValue()) {
                        isApprove = false;
                    } else {
                        isApprove = true;
                    }
                    url = "javascript:exchangeAsoTransferU('" + AESUtil.decrypt(secret, tradePassword) + "','" + Content.usdtExContractAddress + "'," + usdtTokensSold + "," + minTokensBought + "," + deadline + ",'" + recipient + "','" + asoTokenAddress + "'," + isApprove + ",'" + Content.usdtContractAddress + "','" + Content.usdtExContractAddress + "','115792089237316195423570985008687907853269984665640564039457584007913129639935','" + Http.tronUrl + "')";
                } else {
                    url = "javascript:exchangeAsoTransfer('" + AESUtil.decrypt(secret, tradePassword) + "','" + lpExchangeAddress + "'," + minTokens + "," + deadline + ",'" + recipient + "'," + msgValue + ",'" + Http.tronUrl + "')";
                }
            } else {
                if (isSwitchUsdt) {
                    if (allowance.floatValue() > 0f && allowance.floatValue() >= new BigDecimal(usdtTokensSold).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP).floatValue()) {
                        isApprove = false;
                    } else {
                        isApprove = true;
                    }
                    url = "javascript:exchangeAsoTransferU('" + AESUtil.decrypt(secret, tradePassword) + "','" + lpExchangeAddress + "'," + usdtTokensSold + "," + minTokensBought + "," + deadline + ",'" + recipient + "','" + Content.usdtContractAddress + "'," + isApprove + ",'" + asoTokenAddress + "','" + lpExchangeAddress + "','115792089237316195423570985008687907853269984665640564039457584007913129639935','" + Http.tronUrl + "')";
                } else {
                    if (allowance.floatValue() > 0f && allowance.floatValue() >= new BigDecimal(tokensSold).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP).floatValue()) {
                        isApprove = false;
                    } else {
                        isApprove = true;
                    }
                    url = "javascript:exchangeTrxTransfer('" + AESUtil.decrypt(secret, tradePassword) + "','" + lpExchangeAddress + "'," + tokensSold + "," + minTrx + "," + deadline + ",'" + recipient + "'," + isApprove + ",'" + asoTokenAddress + "','" + lpExchangeAddress + "','115792089237316195423570985008687907853269984665640564039457584007913129639935','" + Http.tronUrl + "')";
                }
            }
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog1);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class ExchangeAsoJavaScriptInterface {
        @JavascriptInterface
        public void exchangeAsoFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (str.contains("错误")) {
                        DialogUtils.closeDialog(myDialog1);
                        ToastUtil.showShort(mContext, str);
                    } else {
                        myDialog1.setMsg("链上验证结果中...");
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
                            DialogUtils.closeDialog(myDialog1);
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
                                    DialogUtils.closeDialog(myDialog1);
                                    if (type == 1) {
                                        ToastUtil.showShort(mContext, "转账成功");
                                        pledgeInfo(true);
                                    } else if (type == 2) {
                                        ToastUtil.showShort(mContext, "兑换交易成功");
                                        dialog.dismiss();
                                    }
                                } else {
                                    DialogUtils.closeDialog(myDialog1);
                                    ToastUtil.showShort(mContext, "验证结果失败：" + jsonObject1.getJSONObject("receipt").getString("result"));
                                }
                            } else {
                                if (number > 3) {
                                    DialogUtils.closeDialog(myDialog1);
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
                            DialogUtils.closeDialog(myDialog1);
                            ToastUtil.showShort(mContext, "验证结果失败，请稍候再试");
                        }
                    }
                });
            }
        });
    }

    private void getTronTransferEnergy() {
        String url = "";
        if (transferType == 0) {
            url = Http.tronTransferEnergy + "?type=trx";
        } else if (transferType == 1) {
            url = Http.tronTransferEnergy + "?type=aso";
        } else if (transferType == 2) {
            url = Http.tronTransferEnergy + "?type=usdt";
        } else if (transferType == 3) {
            url = Http.tronTransferEnergy + "?type=usdj";
        }
        OkHttpClient.initGet(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog1);
                        net = new BigDecimal("0");
                        energy = new BigDecimal("0");
                        ToastUtil.showShort(mContext, "网络错误，预估转账手续费失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isClick = false;
                                    DialogUtils.closeDialog(myDialog1);
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        if (jsonObject.getInt("code") == 1) {
                                            net = new BigDecimal(jsonObject.getJSONObject("data").getString("net"));
                                            energy = new BigDecimal(jsonObject.getJSONObject("data").getString("energy"));
                                            BigDecimal burningTrx = new BigDecimal("0");
                                            if (netBalance.floatValue() < net.floatValue()) {
                                                burningTrx = burningTrx.add(net.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN)).setScale(5, BigDecimal.ROUND_DOWN);
                                            }
                                            if (energyBalance.floatValue() < energy.floatValue()) {
                                                burningTrx = burningTrx.add(energy.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN)).setScale(5, BigDecimal.ROUND_DOWN);
                                            }
                                            if (trxBalance.floatValue() > burningTrx.floatValue()) {
                                                if (mTextView_net_text != null && dialog != null && dialog.isShowing()) {
                                                    if (netBalance.floatValue() < net.floatValue()) {
                                                        mTextView_net_text.setText("预计燃烧 " + net.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取带宽");
                                                        mTextView_net.setText("=" + net.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToNet).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                                    } else {
                                                        mTextView_net_text.setText("预计消耗带宽");
                                                        mTextView_net.setText("≈" + net.setScale(0, BigDecimal.ROUND_DOWN).toString());
                                                    }
                                                    if (energyBalance.floatValue() < energy.floatValue()) {
                                                        mTextView_energy_text.setText("预计燃烧 " + energy.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取能量");
                                                        mTextView_energy1.setText("=" + energy.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                                    } else {
                                                        mTextView_energy_text.setText("预计消耗能量");
                                                        mTextView_energy1.setText("≈" + energy.setScale(0, BigDecimal.ROUND_DOWN).toString());
                                                    }
                                                } else {
                                                    transferDialog();
                                                }
                                            } else {
                                                dialogTrx(burningTrx, 1);
                                            }
                                        } else {
                                            net = new BigDecimal("0");
                                            energy = new BigDecimal("0");
                                            ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                                        }
                                    } catch (JSONException e) {
                                        net = new BigDecimal("0");
                                        energy = new BigDecimal("0");
                                        ToastUtil.showShort(mContext, "预估转账手续费错误：" + e.getMessage());
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

    private void transferDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_tron_transfer_layout, null);
        TextView mTextView_from = v.findViewById(R.id.mTextView_from);
        EditText mEditText_to = v.findViewById(R.id.mEditText_to);
        EditText mEditText_amount = v.findViewById(R.id.mEditText_amount);
        EditText mEditText_message = v.findViewById(R.id.mEditText_message);
        EditText mEditText_password = v.findViewById(R.id.mEditText_password);
        mTextView_net_text = v.findViewById(R.id.mTextView_net_text);
        mTextView_net = v.findViewById(R.id.mTextView_net);
        mTextView_energy_text = v.findViewById(R.id.mTextView_energy_text);
        mTextView_energy1 = v.findViewById(R.id.mTextView_energy);
        TextView mTextView_btn_cancel = v.findViewById(R.id.mTextView_btn_cancel);
        LinearLayout mLinearLayout_btn_subscribe = v.findViewById(R.id.mLinearLayout_btn_subscribe);
        mTextView_balance = v.findViewById(R.id.mTextView_balance);
        RelativeLayout mRelativeLayout_selectToken = v.findViewById(R.id.mRelativeLayout_selectToken);
        Spinner mSpinner_token = v.findViewById(R.id.mSpinner_token);
        mRelativeLayout_selectToken.setVisibility(View.VISIBLE);

        if (transferType == 0) {
            mTextView_balance.setText(trxBalance.setScale(3, BigDecimal.ROUND_DOWN).toString() + " TRX");
        } else if (transferType == 1) {
            mTextView_balance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
        } else if (transferType == 2) {
            mTextView_balance.setText(usdtBalance.toString() + " USDT");
        } else if (transferType == 3) {
            mTextView_balance.setText(usdjBalance.toString() + " USDJ");
        }

        List<String> dataList = new ArrayList<>();
        dataList.add("TRX");
        dataList.add("ASO");
        dataList.add("USDT");
        dataList.add("USDJ");
        ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item_my_layout, dataList);
        arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_token.setAdapter(arrAdapter);
        mSpinner_token.setDropDownVerticalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.dp_45));

        mSpinner_token.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != transferType) {
                    transferType = position;
                    mEditText_amount.setText("");
                    if (position == 1) {
                        myDialog1 = DialogUtils.createLoadingDialog(mContext, "查询余额...");
                        getAsoBalance();
                        contractAddress = asoTokenAddress;
                    } else {
                        myDialog2 = DialogUtils.createLoadingDialog(mContext, "查询余额...");
                        getAddressInfo(true);
                        if (position == 0) {
                            contractAddress = "";
                        } else if (position == 2) {
                            contractAddress = Content.usdtContractAddress;
                        } else if (position == 3) {
                            contractAddress = "TMwFHYXLJaRUPeW6421aqXL4ZEzPRFGkGT";
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTextView_from.setText(tronAddress);

        if (netBalance.floatValue() < net.floatValue()) {
            mTextView_net_text.setText("预计燃烧 " + net.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取带宽");
            mTextView_net.setText("=" + net.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToNet).setScale(0, BigDecimal.ROUND_DOWN).toString());
        } else {
            mTextView_net_text.setText("预计消耗带宽");
            mTextView_net.setText("≈" + net.setScale(0, BigDecimal.ROUND_DOWN).toString());
        }
        if (energyBalance.floatValue() < energy.floatValue()) {
            mTextView_energy_text.setText("预计燃烧 " + energy.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).toString() + " TRX获取能量");
            mTextView_energy1.setText("=" + energy.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
        } else {
            mTextView_energy_text.setText("预计消耗能量");
            mTextView_energy1.setText("≈" + energy.setScale(0, BigDecimal.ROUND_DOWN).toString());
        }

        mEditText_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && flag) {
                    if (transferType == 0) {
                        if (trxBalance.floatValue() == 0f && flag) {
                            ToastUtil.showShort(mContext, "当前可用TRX为零");
                            flag = false;
                            s.clear();
                            flag = true;
                            return;
                        }
                    } else if (transferType == 1) {
                        if (asoBalance.floatValue() == 0f && flag) {
                            ToastUtil.showShort(mContext, "当前可用ASO为零");
                            flag = false;
                            s.clear();
                            flag = true;
                            return;
                        }
                    } else if (transferType == 2) {
                        if (usdtBalance.floatValue() == 0f && flag) {
                            ToastUtil.showShort(mContext, "当前可用USDT为零");
                            flag = false;
                            s.clear();
                            flag = true;
                            return;
                        }
                    } else if (transferType == 3) {
                        if (usdjBalance.floatValue() == 0f && flag) {
                            ToastUtil.showShort(mContext, "当前可用USDJ为零");
                            flag = false;
                            s.clear();
                            flag = true;
                            return;
                        }
                    }
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

                    if (transferType == 0) {
                        if (result.compareTo(trxBalance) == 1) {
                            temp = trxBalance.setScale(3, BigDecimal.ROUND_DOWN).toString();
                            ToastUtil.showShort(mContext, "最多可以转账" + trxBalance.setScale(3, BigDecimal.ROUND_DOWN).toString() + "TRX");
                        }
                    } else if (transferType == 1) {
                        if (result.compareTo(asoBalance) == 1) {
                            temp = asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString();
                            ToastUtil.showShort(mContext, "最多可以转账" + asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString() + "ASO");
                        }
                    } else if (transferType == 2) {
                        if (result.compareTo(usdtBalance) == 1) {
                            temp = usdtBalance.setScale(3, BigDecimal.ROUND_DOWN).toString();
                            ToastUtil.showShort(mContext, "最多可以转账" + usdtBalance.setScale(3, BigDecimal.ROUND_DOWN).toString() + "USDT");
                        }
                    } else if (transferType == 3) {
                        if (result.compareTo(usdjBalance) == 1) {
                            temp = usdjBalance.setScale(3, BigDecimal.ROUND_DOWN).toString();
                            ToastUtil.showShort(mContext, "最多可以转账" + usdjBalance.setScale(3, BigDecimal.ROUND_DOWN).toString() + "USDJ");
                        }
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

        mTextView_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
                transferType = -1;
            }
        });

        mLinearLayout_btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                if (mEditText_to.getText().toString().trim().isEmpty()) {
                    Toast.makeText(mContext, "接收账户不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEditText_amount.getText().toString().trim().isEmpty() || Float.valueOf(mEditText_amount.getText().toString()) == 0f) {
                    Toast.makeText(mContext, "转账金额不能为空或0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEditText_password.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "交易密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    try {
                        if (transferType == 0) {
                            transferWeb(tronAddress, mEditText_to.getText().toString(), new BigDecimal(mEditText_amount.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString(), mEditText_password.getText().toString());
                        } else {
                            transferTonken20Web(mEditText_to.getText().toString(), new BigDecimal(mEditText_amount.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString(), mEditText_password.getText().toString());
                        }
                    } catch (Exception e) {
                        isClick = false;
                        e.printStackTrace();
                    }
                }
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

    private void getAsoBalance() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        BalanceFJavaScriptInterface javaScriptInterface = new BalanceFJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/selectBalance.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                selectBalanceWebF();
            }
        });
    }

    private void selectBalanceWebF() {
        String url = null;
        try {
            url = "javascript:selectBalance('" + pountContractAddress + "','" + tronAddress + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog1);
        }
        mWebView.loadUrl(url);
    }

    private class BalanceFJavaScriptInterface {
        @JavascriptInterface
        public void selectFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogUtils.closeDialog(myDialog1);
                    if (str.contains("错误")) {
                        ToastUtil.showShort(mContext, str);
                    } else {
                        asoBalance = new BigDecimal(str).divide(decimals, 3, BigDecimal.ROUND_DOWN);
                        mTextView_balance.setText(new BigDecimal(str).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
                        myDialog1 = DialogUtils.createLoadingDialog(mContext, "预估转账手续费...");
                        getTronTransferEnergy();
                    }
                }
            });
        }
    }

    private void transferWeb(String fromAddress, String toAddress, String amount, String password) {
        myDialog1 = DialogUtils.createLoadingDialog(mContext, "转账中...");
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        TransferJavaScriptInterface javaScriptInterface = new TransferJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/transferTron.html");


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                JsParseMainAssetTra(fromAddress, toAddress, amount, password);
            }
        });
    }

    private void JsParseMainAssetTra(String fromAddress, String toAddress, String amount, String password) {
        String url = null;
        try {
            url = "javascript:transfer('" + fromAddress + "','" + toAddress + "'," + amount + ",'" + AESUtil.decrypt(secret, password) + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
            isClick = false;
            DialogUtils.closeDialog(myDialog1);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class TransferJavaScriptInterface {
        @JavascriptInterface
        public void transferFunction(String str) {
            DialogUtils.closeDialog(myDialog1);
            dialog.dismiss();
            transferType = -1;
            isClick = false;
            if (!str.contains("错误：")) {
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getBoolean("result")) {
                        ToastUtil.showShort(mContext, "交易已被成功广播，等待区块确认");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    pledgeInfo(true);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        ToastUtil.showShort(mContext, "交易失败，请稍候再试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtil.showShort(mContext, str);
            }
        }
    }

    private void transferTonken20Web(String toAddress, String amount, String password) {
        myDialog1 = DialogUtils.createLoadingDialog(mContext, "转账中...");
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        Token20JavaScriptInterface javaScriptInterface = new Token20JavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/transferToken20.html");


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                JsParseTonken20Tra(toAddress, amount, password);
            }
        });
    }

    private void JsParseTonken20Tra(String toAddress, String amount, String password) {
        String url = null;
        try {
            url = "javascript:transfer('" + contractAddress + "','" + toAddress + "'," + amount + ",'" + AESUtil.decrypt(secret, password) + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
            isClick = false;
            DialogUtils.closeDialog(myDialog1);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class Token20JavaScriptInterface {
        @JavascriptInterface
        public void transferFunction(String str) {
            dialog.dismiss();
            transferType = -1;
            isClick = false;
            if (str.contains("错误")) {
                DialogUtils.closeDialog(myDialog1);
                ToastUtil.showShort(mContext, str);
            } else {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myDialog1.setMsg("链上验证结果中...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                    number = 1;
                                    getTransactionInfoByIdType(str, 1);
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

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (tronAddress.isEmpty()) {
            ToastUtil.showShort(mContext, "请先导入账户");
            refreshLayout.finishRefresh();
        } else {
            pledgeInfo(false);
        }
    }

    private void hintKeyBoard(View view) {
        InputMethodManager inputMgr = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
