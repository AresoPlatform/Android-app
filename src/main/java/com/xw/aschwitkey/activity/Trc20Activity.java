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
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.adapter.HistoryAdapter;
import com.xw.aschwitkey.adapter.Trc20HistoryAdapter;
import com.xw.aschwitkey.db.TronSQLUtils;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.entity.HistoryBean;
import com.xw.aschwitkey.entity.Trc20HistoryBean;
import com.xw.aschwitkey.entity.TronDBHelperBean;
import com.xw.aschwitkey.fragment.TronWalletFragment;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.xw.aschwitkey.activity.Content.frozenEnergy;

public class Trc20Activity extends AppCompatActivity implements OnRefreshListener, Trc20HistoryAdapter.OnClickListenerFace {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mTextView_title)
    TextView mTextView_title;
    @BindView(R.id.mImageView_icon)
    ImageView mImageView_icon;
    @BindView(R.id.mTextView_balance)
    TextView mTextView_balance;
    @BindView(R.id.mRecyclerView_history)
    RecyclerView mRecyclerView_history;
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mRelativeLayout_noData)
    RelativeLayout mRelativeLayout_noData;

    private Activity mContext;
    private MyDialog myDialog;
    private SPUtils spUtils, spUtils1;
    private Trc20HistoryAdapter adapter;
    private RecyclerViewNoBugLinearLayoutManager layoutManager = null;
    private List<Trc20HistoryBean.History> list;
    private Gson gson;
    private String title, balance, tronAddress, contractAddress, pountContractAddress = "", txasContractAddress = "", asoTokenAddress = "", privateKey = "";
    private BigDecimal decimals, trxBalance, asoBalance, usdtBalance, usdjBalance, netBalance, energyBalance, energy, net, trxToEnergy, trxToNet;
    private WebView mWebView;
    private Dialog dialog;
    private boolean isClick = false, flag = true, isTransfer = false;
    private int number = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trc20);
        ButterKnife.bind(this);
        OtherUtils.config(this);
        init();
    }

    private void init() {
        mContext = Trc20Activity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(Trc20Activity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

        spUtils = new SPUtils(mContext, "AschWallet");
        spUtils1 = new SPUtils(mContext, "AschImport");
        gson = new Gson();
        list = new ArrayList<>();
        adapter = new Trc20HistoryAdapter(mContext, list);
        layoutManager = new RecyclerViewNoBugLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView_history.setLayoutManager(layoutManager);
        mRecyclerView_history.setAdapter(adapter);
        mRecyclerView_history.setNestedScrollingEnabled(false);
        adapter.setOnClickListenerFace(this);
        setPullRefresher();

        tronAddress = spUtils1.getString("tronChildAddress");
        List<TronDBHelperBean> beanList = TronSQLUtils.QuerySQLAll(mContext, "where state = '"+spUtils.getString("phone")+"@'");
        for (int i = 0; i < beanList.size(); i++) {
            if (tronAddress.equals(beanList.get(i).getAddress())) {
                privateKey = beanList.get(i).getSecret();
            }
        }
        title = getIntent().getStringExtra("title");
        balance = getIntent().getStringExtra("balance");
        contractAddress = getIntent().getStringExtra("contractAddress");
        trxBalance = new BigDecimal(getIntent().getStringExtra("trxBalance"));
        netBalance = new BigDecimal(getIntent().getStringExtra("netBalance"));
        energyBalance = new BigDecimal(getIntent().getStringExtra("energyBalance"));
        mTextView_title.setText(title);
        mTextView_balance.setText(balance);
        if (title.equals("TRX")) {
            mImageView_icon.setImageResource(R.mipmap.record_tron);
        } else if (title.equals("AP凭证")) {
            mImageView_icon.setImageResource(R.mipmap.record_aso);
        } else if (title.equals("Areso")) {
            mImageView_icon.setImageResource(R.mipmap.record_aso);
        } else if (title.equals("Tether USD")) {
            mImageView_icon.setImageResource(R.mipmap.record_usdt);
        } else if (title.equals("JUST Stablecoin")) {
            mImageView_icon.setImageResource(R.mipmap.record_usdj);
        }

        myDialog = DialogUtils.createLoadingDialog(mContext, "获取交易记录...");
        getHistory();
        pledgeInfo(0);
    }

    @OnClick({R.id.mImageView_back, R.id.mTextView_transfer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mImageView_back:
                finish();
                break;
            case R.id.mTextView_transfer:
                if (isClick) {
                    return;
                }
                isClick = true;
                if (title.equals("TRX")) {
                    myDialog = DialogUtils.createLoadingDialog(mContext, "查询TRX余额...");
                    getAddressInfo(1);
                } else if (title.equals("Areso")) {
                    if (trxBalance.floatValue() == -1) {
                        myDialog = DialogUtils.createLoadingDialog(mContext, "查询波场地址信息...");
                        pledgeInfo(4);
                    } else {
                        myDialog = DialogUtils.createLoadingDialog(mContext, "查询ASO余额...");
                        isTransfer = true;
                        pledgeInfo(1);
                    }
                } else if (title.equals("Tether USD")) {
                    myDialog = DialogUtils.createLoadingDialog(mContext, "查询USDT余额...");
                    getAddressInfo(2);
                } else if (title.equals("JUST Stablecoin")) {
                    myDialog = DialogUtils.createLoadingDialog(mContext, "查询USDJ余额...");
                    getAddressInfo(3);
                }
                break;
        }
    }

    private void setPullRefresher() {
        mRefreshLayout.setRefreshHeader(new MyClassicsHeader(mContext));
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void getHistory() {
        String url;
        if (title.equals("TRX")) {
            url = Http.transactionsTRX + tronAddress + "&direction=0&db_version=1";
        } else {
            url = Http.transactionsTrc20 + tronAddress + "/transactions/trc20?contract_address=" + contractAddress;
        }
        OkHttpClient.initGet(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，获取交易记录失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        Trc20HistoryBean bean = gson.fromJson(result, Trc20HistoryBean.class);
                        list.addAll(bean.getData());
                        adapter.notifyDataSetChanged();
                        if (list.isEmpty()) {
                            mRelativeLayout_noData.setVisibility(View.VISIBLE);
                        } else {
                            mRelativeLayout_noData.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void OnClickTemp(int p, View view) {
        switch (view.getId()) {
            case R.id.mTextView_address:
            case R.id.mImageView_copy:
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData;
                if (spUtils1.getString("tronChildAddress", "").equals(list.get(p).getTo())) {
                    mClipData = ClipData.newPlainText("address", list.get(p).getFrom());
                } else {
                    mClipData = ClipData.newPlainText("address", list.get(p).getTo());
                }
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort(mContext, "波场地址已复制到剪贴板");
                break;
            case R.id.mRelativeLayout_item:
                if (isClick) {
                    return;
                }
                isClick = true;
                myDialog = DialogUtils.createLoadingDialog(mContext, "获取链上交易详细信息");
                gettransactioninfobyid(p);
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refresh(refreshLayout);
    }

    private void refresh(@NonNull RefreshLayout refreshLayout) {
        pledgeInfo(1);
        String url;
        if (title.equals("TRX")) {
            url = Http.transactionsTRX + spUtils1.getString("tronChildAddress") + "&direction=0&db_version=1";
        } else {
            url = Http.transactionsTrc20 + spUtils1.getString("tronChildAddress") + "/transactions/trc20?tokenId=" + contractAddress;
        }
        OkHttpClient.initGet(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh(1000);
                        }
                        ToastUtil.showShort(mContext, "网络错误，获取交易记录失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh(1000);
                        }
                        Trc20HistoryBean bean = gson.fromJson(result, Trc20HistoryBean.class);
                        if (!list.isEmpty()) {
                            list.clear();
                        }
                        list.addAll(bean.getData());
                        adapter.notifyDataSetChanged();
                        if (list.isEmpty()) {
                            mRelativeLayout_noData.setVisibility(View.VISIBLE);
                        } else {
                            mRelativeLayout_noData.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private void pledgeInfo(int type) {
        OkHttpClient.initGet(Http.pledgeInfo).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
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
                            isClick = false;
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                decimals = new BigDecimal(jsonObject.getJSONObject("data").getString("decimals"));
                                pountContractAddress = jsonObject.getJSONObject("data").getString("pountContractAddress");
                                txasContractAddress = jsonObject.getJSONObject("data").getString("txasContractAddress");
                                asoTokenAddress = jsonObject.getJSONObject("data").getString("asoTokenAddress");
                                trxToEnergy = new BigDecimal(jsonObject.getJSONObject("data").getString("trxToEnergy"));
                                trxToNet = new BigDecimal(jsonObject.getJSONObject("data").getString("trxToNet"));
                                if (type != 0) {
                                    if (type == 4) {
                                        getAddressInfo(4);
                                    } else {
                                        if (title.equals("TRX") || title.equals("Tether USD") || title.equals("JUST Stablecoin")) {
                                            getAddressInfo(0);
                                        } else if (title.equals("AP凭证")) {
                                            getApBalance();
                                        } else {
                                            getAsoBalance();
                                        }
                                    }
                                }
                            } else {
                                DialogUtils.closeDialog(myDialog);
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "获取合约信息报错，请刷新再试");
                        }
                    }
                });
            }
        });
    }

    private void getApBalance() {
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
                        mTextView_balance.setText(new BigDecimal(str).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                    }
                }
            });
        }
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
            isTransfer = false;
            DialogUtils.closeDialog(myDialog);
        }
        mWebView.loadUrl(url);
    }

    private class BalanceFJavaScriptInterface {
        @JavascriptInterface
        public void selectFunction(String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogUtils.closeDialog(myDialog);
                    if (str.contains("错误")) {
                        isTransfer = false;
                        ToastUtil.showShort(mContext, str);
                    } else {
                        mTextView_balance.setText(new BigDecimal(str).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                        asoBalance = new BigDecimal(str).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                        if (isTransfer) {
                            isTransfer = false;
                            myDialog = DialogUtils.createLoadingDialog(mContext, "预估转账手续费...");
                            getTronTransferEnergy(0);
                        }
                    }
                }
            });
        }
    }

    private void getAddressInfo(int type) {
        OkHttpClient.initGet(Http.getTronInfo + tronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，查询" + title + "余额失败");
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
                            if (type == 0 || type == 4) {
                                BigDecimal freeNetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetLimit"));
                                BigDecimal freeNetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetUsed"));
                                BigDecimal NetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netUsed"));
                                BigDecimal NetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netLimit"));
                                BigDecimal energyLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyLimit"));
                                BigDecimal energyUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyUsed"));
                                netBalance = freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN);
                                energyBalance = energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN);
                                trxBalance = new BigDecimal(jsonObject.getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                if (title.equals("TRX")) {
                                    mTextView_balance.setText(new BigDecimal(jsonObject.getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                }
                                if (jsonObject.getJSONArray("trc20token_balances").length() == 0) {
                                    usdtBalance = new BigDecimal("0");
                                    usdjBalance = new BigDecimal("0");
                                    if (title.equals("Tether USD") || title.equals("JUST Stablecoin")) {
                                        mTextView_balance.setText("0.000");
                                    }
                                } else {
                                    for (int i = 0; i < jsonObject.getJSONArray("trc20token_balances").length(); i++) {
                                        if (contractAddress.equals(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("tokenId"))) {
                                            if (title.equals("Tether USD")) {
                                                mTextView_balance.setText(new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                            }
                                            usdtBalance = new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                        }
                                        if (contractAddress.equals(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("tokenId"))) {
                                            if (title.equals("JUST Stablecoin")) {
                                                mTextView_balance.setText(new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                            }
                                            usdjBalance = new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                        }
                                    }
                                }
                                if (type == 4) {
                                    myDialog.setMsg("查询ASO余额...");
                                    isTransfer = true;
                                    pledgeInfo(1);
                                } else {
                                    DialogUtils.closeDialog(myDialog);
                                }
                            } else if (type == 1) {
                                mTextView_balance.setText(new BigDecimal(jsonObject.getString("balance")).divide(new BigDecimal("1000000"), 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                trxBalance = new BigDecimal(jsonObject.getString("balance")).divide(new BigDecimal("1000000"), 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                myDialog.setMsg("预估转账手续费...");
                                getTronTransferEnergy(1);
                            } else {
                                usdtBalance = new BigDecimal("0");
                                usdjBalance = new BigDecimal("0");
                                if (jsonObject.getJSONArray("trc20token_balances").length() == 0) {
                                    usdtBalance = new BigDecimal("0");
                                    usdjBalance = new BigDecimal("0");
                                    if (title.equals("Tether USD") || title.equals("JUST Stablecoin")) {
                                        mTextView_balance.setText("0.000");
                                    }
                                } else {
                                    for (int i = 0; i < jsonObject.getJSONArray("trc20token_balances").length(); i++) {
                                        if (contractAddress.equals(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("tokenId"))) {
                                            if (title.equals("Tether USD")) {
                                                mTextView_balance.setText(new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                            }
                                            usdtBalance = new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                        }
                                        if (contractAddress.equals(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("tokenId"))) {
                                            if (title.equals("JUST Stablecoin")) {
                                                mTextView_balance.setText(new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                            }
                                            usdjBalance = new BigDecimal(jsonObject.getJSONArray("trc20token_balances").getJSONObject(i).getString("balance")).divide(decimals, 3, BigDecimal.ROUND_DOWN).setScale(3, BigDecimal.ROUND_DOWN);
                                        }
                                    }
                                }
                                if (type == 2 && usdtBalance.floatValue() == 0f) {
                                    DialogUtils.closeDialog(myDialog);
                                    ToastUtil.showShort(mContext, "USDT可用余额为0");
                                    mTextView_balance.setText("0.000");
                                    return;
                                }
                                if (type == 3 && usdjBalance.floatValue() == 0f) {
                                    DialogUtils.closeDialog(myDialog);
                                    ToastUtil.showShort(mContext, "USDJ可用余额为0");
                                    mTextView_balance.setText("0.000");
                                    return;
                                }
                                myDialog.setMsg("预估转账手续费...");
                                getTronTransferEnergy(type);
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "获取" + title + "余额错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void gettransactioninfobyid(int p) {
        String url;
        if (title.equals("TRX")) {
            url = Http.getTransactionInfoById + "?value=" + list.get(p).getHash();
        } else {
            url = Http.getTransactionInfoById + "?value=" + list.get(p).getTransaction_id();
        }
        OkHttpClient.initGet(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，请稍候再试");
                        isClick = false;
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
                            isClick = false;
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getJSONObject("receipt").isNull("energy_fee")) {
                                if (jsonObject.isNull("fee")) {
                                    if (jsonObject.getJSONObject("receipt").isNull("energy_usage_total")) {
                                        historyInfoDialog(p, "0", jsonObject.getJSONObject("receipt").getString("net_usage") + " 带宽" + "  0 能量", jsonObject.getString("blockNumber"));
                                    } else {
                                        historyInfoDialog(p, "0", jsonObject.getJSONObject("receipt").getString("net_usage") + " 带宽  " + jsonObject.getJSONObject("receipt").getString("energy_usage_total") + " 能量", jsonObject.getString("blockNumber"));
                                    }
                                } else {
                                    historyInfoDialog(p, new BigDecimal(jsonObject.getString("fee")).divide(new BigDecimal("1000000"), 5, BigDecimal.ROUND_DOWN).setScale(5, BigDecimal.ROUND_DOWN).toString(), new BigDecimal(jsonObject.getJSONObject("receipt").getString("net_fee")).divide(new BigDecimal("10"), 0, BigDecimal.ROUND_DOWN).setScale(0, BigDecimal.ROUND_DOWN).toString() + " 带宽" + "  0 能量", jsonObject.getString("blockNumber"));
                                }
                            } else {
                                if (jsonObject.getJSONObject("receipt").isNull("net_usage")) {
                                    historyInfoDialog(p, new BigDecimal(jsonObject.getString("fee")).divide(new BigDecimal("1000000"), 5, BigDecimal.ROUND_DOWN).setScale(5, BigDecimal.ROUND_DOWN).toString(), new BigDecimal(jsonObject.getJSONObject("receipt").getString("net_fee")).divide(new BigDecimal("10"), 0, BigDecimal.ROUND_DOWN).setScale(0, BigDecimal.ROUND_DOWN).toString() + " 带宽" + "  " + jsonObject.getJSONObject("receipt").getString("energy_usage_total") + " 能量", jsonObject.getString("blockNumber"));
                                } else {
                                    historyInfoDialog(p, new BigDecimal(jsonObject.getString("fee")).divide(new BigDecimal("1000000"), 5, BigDecimal.ROUND_DOWN).setScale(5, BigDecimal.ROUND_DOWN).toString(), jsonObject.getJSONObject("receipt").getString("net_usage") + " 带宽" + "  " + jsonObject.getJSONObject("receipt").getString("energy_usage_total") + " 能量", jsonObject.getString("blockNumber"));
                                }
                            }
                        } catch (Exception e) {
                            ToastUtil.showShort(mContext, "获取链上交易详细信息错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void historyInfoDialog(int p, String fee, String resources, String blockHeight) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_trc20_history_info_layout, null);
        TextView mTextView_from = view.findViewById(R.id.mTextView_from);
        TextView mTextView_to = view.findViewById(R.id.mTextView_to);
        TextView mTextView_type = view.findViewById(R.id.mTextView_type);
        TextView mTextView_hash = view.findViewById(R.id.mTextView_hash);
        TextView mTextView_time = view.findViewById(R.id.mTextView_time);
        TextView mTextView_height = view.findViewById(R.id.mTextView_height);
        TextView mTextView_fee = view.findViewById(R.id.mTextView_fee);
        TextView mTextView_resources = view.findViewById(R.id.mTextView_resources);

        mTextView_from.setText(list.get(p).getFrom().substring(0, 6) + "..." + list.get(p).getFrom().substring(list.get(p).getFrom().length() - 6, list.get(p).getFrom().length()));
        mTextView_to.setText(list.get(p).getTo().substring(0, 6) + "..." + list.get(p).getTo().substring(list.get(p).getTo().length() - 6, list.get(p).getTo().length()));
        if (title.equals("TRX")) {
            mTextView_hash.setText(list.get(p).getHash().substring(0, 6) + "..." + list.get(p).getHash().substring(list.get(p).getHash().length() - 6, list.get(p).getHash().length()));
            if (list.get(p).getContract_type().equals("TriggerSmartContract")) {
                mTextView_type.setText("调用智能合约");
            } else if (list.get(p).getContract_type().equals("TransferContract")) {
                mTextView_type.setText("TRX转账");
            }
        } else {
            mTextView_hash.setText(list.get(p).getTransaction_id().substring(0, 6) + "..." + list.get(p).getTransaction_id().substring(list.get(p).getTransaction_id().length() - 6, list.get(p).getTransaction_id().length()));
            if (list.get(p).getType().equals("Transfer")) {
                mTextView_type.setText("调用智能合约");
            } else if (list.get(p).getType().equals("Approval")) {
                mTextView_type.setText("TRC20 通证授权");
            }
        }
        mTextView_time.setText(OtherUtils.timedate(list.get(p).getBlock_timestamp(), "yyyy-MM-dd HH:mm:ss"));
        mTextView_height.setText(blockHeight);
        mTextView_fee.setText(fee + " TRX");
        mTextView_resources.setText(resources);

        view.findViewById(R.id.mRelativeLayout_from).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData;
                mClipData = ClipData.newPlainText("address", list.get(p).getFrom());
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort(mContext, "波场地址已复制到剪贴板");
            }
        });

        view.findViewById(R.id.mRelativeLayout_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData;
                mClipData = ClipData.newPlainText("address", list.get(p).getTo());
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort(mContext, "波场地址已复制到剪贴板");
            }
        });

        view.findViewById(R.id.mRelativeLayout_hash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData;
                if (title.equals("TRX")) {
                    mClipData = ClipData.newPlainText("hash", list.get(p).getHash());
                } else {
                    mClipData = ClipData.newPlainText("address", list.get(p).getTransaction_id());
                }
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort(mContext, "交易ID已复制到剪贴板");
            }
        });

        view.findViewById(R.id.mTextView_goTronScan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                if (title.equals("TRX")) {
                    intent.putExtra("url", "***" + list.get(p).getHash());
                } else {
                    intent.putExtra("url", "***" + list.get(p).getTransaction_id());
                }
                startActivity(intent);
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

    private void getTronTransferEnergy(int type) {
        String url = "";
        if (type == 0) {
            url = Http.tronTransferEnergy + "?type=aso";
        } else if (type == 1) {
            url = Http.tronTransferEnergy + "?type=trx";
        } else if (type == 2) {
            url = Http.tronTransferEnergy + "?type=usdt";
        } else if (type == 3) {
            url = Http.tronTransferEnergy + "?type=usdj";
        }
        OkHttpClient.initGet(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        net = new BigDecimal("0");
                        energy = new BigDecimal("0");
                        ToastUtil.showShort(mContext, "网络错误，预估转账手续费失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                net = new BigDecimal(jsonObject.getJSONObject("data").getString("net"));
                                energy = new BigDecimal(jsonObject.getJSONObject("data").getString("energy"));
                                BigDecimal burningTrx = new BigDecimal("0");
                                if (dialog != null && dialog.isShowing()) {
                                    return;
                                }
                                if (netBalance.floatValue() < net.floatValue()) {
                                    burningTrx = burningTrx.add(net.subtract(netBalance).divide(trxToNet, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (energyBalance.floatValue() < energy.floatValue()) {
                                    burningTrx = burningTrx.add(energy.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN)).setScale(5, BigDecimal.ROUND_DOWN);
                                }
                                if (trxBalance.floatValue() > burningTrx.floatValue()) {
                                    transferDialog();
                                } else {
                                    dialogTrx(burningTrx);
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
            }
        });
    }

    private void dialogTrx(BigDecimal burningTrx) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_tron_trx_layout, null);
        TextView mTextView_msg = view.findViewById(R.id.mTextView_msg);
        TextView mTextView_false = view.findViewById(R.id.mTextView_false);
        TextView mTextView_true = view.findViewById(R.id.mTextView_true);

        String frozenTrx = energy.divide(frozenEnergy, BigDecimal.ROUND_HALF_UP, 3).setScale(3, BigDecimal.ROUND_HALF_UP).toString();

        mTextView_msg.setText("当前操作需要" + energy.setScale(0,BigDecimal.ROUND_DOWN).toString() + "能量（能量可预先冻结大约" + frozenTrx + "个TRX长期供能）能量不足需燃烧大约" + burningTrx.toString() + "个TRX，您的TRX余额不足建议您准备足够能量或TRX");

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
                transferDialog();
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

    private void transferDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_tron_transfer_layout, null);
        TextView mTextView_from = v.findViewById(R.id.mTextView_from);
        EditText mEditText_to = v.findViewById(R.id.mEditText_to);
        EditText mEditText_amount = v.findViewById(R.id.mEditText_amount);
        EditText mEditText_message = v.findViewById(R.id.mEditText_message);
        EditText mEditText_password = v.findViewById(R.id.mEditText_password);
        TextView mTextView_net_text = v.findViewById(R.id.mTextView_net_text);
        TextView mTextView_net = v.findViewById(R.id.mTextView_net);
        TextView mTextView_energy_text = v.findViewById(R.id.mTextView_energy_text);
        TextView mTextView_energy = v.findViewById(R.id.mTextView_energy);
        TextView mTextView_btn_cancel = v.findViewById(R.id.mTextView_btn_cancel);
        LinearLayout mLinearLayout_btn_subscribe = v.findViewById(R.id.mLinearLayout_btn_subscribe);
        TextView mTextView_balance = v.findViewById(R.id.mTextView_balance);

        if (title.equals("TRX")) {
            mTextView_balance.setText(trxBalance.toString() + " TRX");
        } else if (title.equals("Areso")) {
            mTextView_balance.setText(asoBalance.toString() + " ASO");
        } else if (title.equals("Tether USD")) {
            mTextView_balance.setText(usdtBalance.toString() + " USDT");
        } else if (title.equals("JUST Stablecoin")) {
            mTextView_balance.setText(usdjBalance.toString() + " USDJ");
        }

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
            mTextView_energy.setText("=" + energy.subtract(energyBalance).divide(trxToEnergy, 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(trxToEnergy).setScale(0, BigDecimal.ROUND_DOWN).toString());
        } else {
            mTextView_energy_text.setText("预计消耗能量");
            mTextView_energy.setText("≈" + energy.setScale(0, BigDecimal.ROUND_DOWN).toString());
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
                if (title.equals("TRX")) {
                    if (Float.parseFloat(trxBalance.toString()) == 0f && flag) {
                        ToastUtil.showShort(mContext, "当前可用TRX为零");
                        flag = false;
                        s.clear();
                        flag = true;
                        return;
                    }
                } else if (title.equals("Areso")) {
                    if (Float.parseFloat(asoBalance.toString()) == 0f && flag) {
                        ToastUtil.showShort(mContext, "当前可用ASO为零");
                        flag = false;
                        s.clear();
                        flag = true;
                        return;
                    }
                } else if (title.equals("Tether USD")) {
                    if (Float.parseFloat(usdtBalance.toString()) == 0f && flag) {
                        ToastUtil.showShort(mContext, "当前可用USDT为零");
                        flag = false;
                        s.clear();
                        flag = true;
                        return;
                    }
                } else if (title.equals("JUST Stablecoin")) {
                    if (Float.parseFloat(usdjBalance.toString()) == 0f && flag) {
                        ToastUtil.showShort(mContext, "当前可用USDJ为零");
                        flag = false;
                        s.clear();
                        flag = true;
                        return;
                    }
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

                    if (title.equals("TRX")) {
                        if (result.compareTo(trxBalance) == 1) {
                            temp = new DecimalFormat("0.000").format(trxBalance);
                            ToastUtil.showShort(mContext, "最多可以转账" + new DecimalFormat("0.000").format(trxBalance) + "TRX");
                        }
                    } else if (title.equals("Areso")) {
                        if (result.compareTo(asoBalance) == 1) {
                            temp = new DecimalFormat("0.000").format(asoBalance);
                            ToastUtil.showShort(mContext, "最多可以转账" + new DecimalFormat("0.000").format(asoBalance) + "ASO");
                        }
                    } else if (title.equals("Tether USD")) {
                        if (result.compareTo(usdtBalance) == 1) {
                            temp = new DecimalFormat("0.000").format(usdtBalance);
                            ToastUtil.showShort(mContext, "最多可以转账" + new DecimalFormat("0.000").format(usdtBalance) + "USDT");
                        }
                    } else if (title.equals("JUST Stablecoin")) {
                        if (result.compareTo(usdjBalance) == 1) {
                            temp = new DecimalFormat("0.000").format(usdjBalance);
                            ToastUtil.showShort(mContext, "最多可以转账" + new DecimalFormat("0.000").format(usdjBalance) + "USDJ");
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
                try {
                    if (title.equals("TRX")) {
                        transferWeb(tronAddress, mEditText_to.getText().toString(), new BigDecimal(mEditText_amount.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString(), mEditText_password.getText().toString());
                    } else if (title.equals("Areso")) {
                        transferTonken20Web(mEditText_to.getText().toString(), new BigDecimal(mEditText_amount.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString(), mEditText_password.getText().toString());
                    } else {
                        transferTonken20Web(mEditText_to.getText().toString(), new BigDecimal(mEditText_amount.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString(), mEditText_password.getText().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    private void transferWeb(String fromAddress, String toAddress, String amount, String password) {
        myDialog = DialogUtils.createLoadingDialog(mContext, "转账中...");
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        JavaScriptInterface javaScriptInterface = new JavaScriptInterface();
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
            url = "javascript:transfer('" + fromAddress + "','" + toAddress + "'," + amount + ",'" + AESUtil.decrypt(privateKey, password) + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class JavaScriptInterface {
        @JavascriptInterface
        public void transferFunction(String str) {
            DialogUtils.closeDialog(myDialog);
            dialog.dismiss();
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
                                    refresh(null);
                                    EventBus.getDefault().postSticky(new EventMessageBean(29, null));
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
        myDialog = DialogUtils.createLoadingDialog(mContext, "转账中...");
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
            url = "javascript:transfer('" + contractAddress + "','" + toAddress + "'," + amount + ",'" + AESUtil.decrypt(privateKey, password) + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class Token20JavaScriptInterface {
        @JavascriptInterface
        public void transferFunction(String str) {
            dialog.dismiss();
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
                                    DialogUtils.closeDialog(myDialog);
                                    ToastUtil.showShort(mContext, "转账成功");
                                    refresh(null);
                                    EventBus.getDefault().postSticky(new EventMessageBean(29, null));
                                } else {
                                    DialogUtils.closeDialog(myDialog);
                                    ToastUtil.showShort(mContext, "验证失败：" + jsonObject1.getJSONObject("receipt").getString("result"));
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

}
