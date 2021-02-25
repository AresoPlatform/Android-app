package com.xw.aschwitkey.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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
import android.widget.TextView;
import android.widget.Toast;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.db.TronSQLUtils;
import com.xw.aschwitkey.entity.DBHelperBean;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.entity.TronDBHelperBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.AESUtil;
import com.xw.aschwitkey.utils.DESUtil;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.DownloadUtil;
import com.xw.aschwitkey.utils.MD5Util;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xw.aschwitkey.activity.Content.frozenEnergy;

public class MoreApplicationsActivity extends AppCompatActivity {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mTextView_install)
    TextView mTextView_install;

    private Activity mContext;
    private ProgressDialog progressDialog;
    private MyDialog myDialog;
    private Dialog dialog;
    private SPUtils spUtils, spUtils1;
    private WebView mWebView;
    private String tronAddress = "", privateKey = "", inTronAddress = "", asoTokenAddress = "", ddzDownloadUrl = "";
    private BigDecimal decimals, asoBalance, energy, net, trxToEnergy, trxToNet, energyBalance, netBalance, trxBalance, ddzGameCoin, ddzWithdrawService, goldBalance;
    private int number = 1;
    private boolean isShow = false, isPassword = false, isClick = false, flag = true;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_applications);
        ButterKnife.bind(this);
        OtherUtils.config(this);
        init();
    }

    private void init() {
        mContext = MoreApplicationsActivity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(MoreApplicationsActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

        spUtils = new SPUtils(mContext, "AschWallet");
        spUtils1 = new SPUtils(mContext, "AschImport");
        getUserInfo();
    }

    @OnClick({R.id.mImageView_back, R.id.mRelativeLayout_ddz, R.id.mTextView_btn_recharge, R.id.mTextView_btn_withdrawal, R.id.mTextView_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mImageView_back:
                finish();
                break;
            case R.id.mRelativeLayout_ddz:
                if (isClick) {
                    return;
                }
                isClick = true;
                countDownDialog();
                break;
            case R.id.mTextView_btn_recharge:
                if (isClick) {
                    return;
                }
                if (tronAddress.isEmpty()) {
                    ToastUtil.showShort(mContext, "请先前往Tron钱包导入账号");
                    return;
                }
                isClick = true;
                tipsDialog(1);
                break;
            case R.id.mTextView_btn_withdrawal:
                if (isClick) {
                    return;
                }
                isClick = true;
                tipsDialog(2);
                break;
            case R.id.mTextView_history:
                Intent intentH = new Intent(mContext, DdzHistoryActivity.class);
                startActivity(intentH);
                break;
        }
    }

    private void getUserInfo() {
        List<TronDBHelperBean> beanList = TronSQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
        for (int i = 0; i < beanList.size(); i++) {
            if (spUtils1.getString("tronChildAddress", "").equals(beanList.get(i).getAddress())) {
                tronAddress = beanList.get(i).getAddress();
                privateKey = beanList.get(i).getSecret();
            }
        }
    }

    private void countDownDialog() {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_tips_count_down_layout, null);
        final MyDialog dialog = new MyDialog(mContext, inflate, R.style.DialogTheme);
        TextView mTextView_message = inflate.findViewById(R.id.mTextView_message);
        TextView mTextView_false = inflate.findViewById(R.id.mTextView_false);
        TextView mTextView_true = inflate.findViewById(R.id.mTextView_true);
        SpannableStringBuilder spannable = new SpannableStringBuilder(mTextView_message.getText().toString());
        spannable.setSpan(new AbsoluteSizeSpan(OtherUtils.dp2px(mContext, 13)), mTextView_message.getText().toString().indexOf("每局"), mTextView_message.getText().toString().indexOf("收，") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_red)), mTextView_message.getText().toString().indexOf("每局"), mTextView_message.getText().toString().indexOf("收，") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), mTextView_message.getText().toString().indexOf("每局"), mTextView_message.getText().toString().indexOf("收，") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView_message.setText(spannable);
        mTextView_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
            }
        });
        mTextView_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                checkAccount(0);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTextView_true.setEnabled(false);
                mTextView_true.setText((Math.round((double) millisUntilFinished / 1000) - 1) + "秒");
            }

            @Override
            public void onFinish() {
                mTextView_true.setEnabled(true);
                mTextView_true.setText("确定");
            }
        }.start();
    }

    private void checkAccount(int type) {
        if (!spUtils.getString("ddzAccount", "").isEmpty() && type == 0) {
            if (OtherUtils.checkAppInstalled(mContext, "com.jjylgames.jjyl")) {
                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.jjylgames.jjyl");
                startActivity(intent);
            } else {
                if (ddzDownloadUrl.isEmpty()) {
                    myDialog = DialogUtils.createLoadingDialog(mContext, "获取游戏配置信息...");
                    gameInfo(0);
                } else {
                    DownLoadApk();
                }
            }
            isClick = false;
        } else {
            myDialog = DialogUtils.createLoadingDialog(mContext, "查询账户注册状态...");
            OkHttpClient.initGet(Http.checkAccount).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "网络错误，查询账号注册状态失败");
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
                                isClick = false;
                                DialogUtils.closeDialog(myDialog);
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getInt("code") == 1) {
                                    if (jsonObject.getString("data").isEmpty()) {
                                        tipsDialog(0);
                                    } else {
                                        spUtils.putString("ddzAccount", jsonObject.getString("data"));
                                        if (type == 0) {
                                            if (OtherUtils.checkAppInstalled(mContext, "com.jjylgames.jjyl")) {
                                                PackageManager packageManager = getPackageManager();
                                                Intent intent = packageManager.getLaunchIntentForPackage("com.jjylgames.jjyl");
                                                startActivity(intent);
                                            } else {
                                                if (ddzDownloadUrl.isEmpty()) {
                                                    myDialog = DialogUtils.createLoadingDialog(mContext, "获取游戏配置信息...");
                                                    gameInfo(0);
                                                } else {
                                                    DownLoadApk();
                                                }
                                            }
                                        } else if (type == 1) {
                                            pledgeInfo();
                                        } else if (type == 2) {
                                            myDialog = DialogUtils.createLoadingDialog(mContext, "获取游戏配置信息...");
                                            gameInfo(2);
                                        }
                                    }
                                } else {
                                    ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                                }
                            } catch (JSONException e) {
                                ToastUtil.showShort(mContext, "查询账号注册状态错误：" + e.getMessage());
                            }
                        }
                    });
                }
            });
        }
    }

    private void tipsDialog(int type) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_tips_layout, null);
        final MyDialog dialog = new MyDialog(mContext, inflate, R.style.DialogTheme);
        TextView mTextView_title = inflate.findViewById(R.id.mTextView_title);
        TextView mTextView_false = inflate.findViewById(R.id.mTextView_false);
        TextView mTextView_true = inflate.findViewById(R.id.mTextView_true);
        if (type == 0) {
            mTextView_title.setText("当前账号暂未注册ASO娱乐，是否使用当前希客登录密码注册？");
            mTextView_false.setText("返回");
            mTextView_true.setText("登录密码注册");
        } else if (type == 1) {
            mTextView_title.setText("充值前请确保已登录过一次游戏，并已退出ASO娱乐应用，否则将操作失败");
            mTextView_false.setText("返回");
            mTextView_true.setText("我已退出");
        } else if (type == 2) {
            mTextView_title.setText("提现前请先退出ASO娱乐应用，否则将操作失败,提现成功后，ASO将在7天内到账");
            mTextView_false.setText("返回");
            mTextView_true.setText("我已退出");
        }
        mTextView_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isClick = false;
            }
        });
        mTextView_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isClick = false;
                if (type == 0) {
                    myDialog = DialogUtils.createLoadingDialog(mContext, "注册中...");
                    regConnectRegistry("");
                } else if (type == 1) {
                    if (!spUtils.getString("ddzAccount", "").isEmpty()) {
                        pledgeInfo();
                    } else {
                        checkAccount(1);
                    }
                } else if (type == 2) {
                    if (!spUtils.getString("ddzAccount", "").isEmpty()) {
                        myDialog = DialogUtils.createLoadingDialog(mContext, "获取游戏配置信息...");
                        gameInfo(2);
                    } else {
                        checkAccount(2);
                    }
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void registerDialog() {
        isShow = false;
        isPassword = false;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_register_ddz_layout, null);
        EditText mEditText_password = view.findViewById(R.id.mEditText_password);
        ImageView mImageView_winfo = view.findViewById(R.id.mImageView_winfo);
        TextView mTextView_err = view.findViewById(R.id.mTextView_err);

        mImageView_winfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShow) {
                    mEditText_password.setInputType(128);
                    mImageView_winfo.setImageResource(R.mipmap.winfo_show);
                    isShow = true;
                } else {
                    mEditText_password.setInputType(129);
                    mImageView_winfo.setImageResource(R.mipmap.winfo_hide);
                    isShow = false;
                }
            }
        });

        mEditText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = mEditText_password.getText().toString();
                String reg = "^.{6,18}$";
                if (password.matches(reg)) {
                    mTextView_err.setVisibility(View.GONE);
                    isPassword = true;
                } else {
                    mTextView_err.setVisibility(View.VISIBLE);
                    isPassword = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        view.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.mTextView_btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPassword) {
                    myDialog = DialogUtils.createLoadingDialog(mContext, "注册中...");
                    regConnectRegistry(mEditText_password.getText().toString());
                } else {
                    ToastUtil.showShort(mContext, "密码格式不正确");
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

    private void regConnectRegistry(String artificially) {
        JSONObject requestJsonObject = new JSONObject();
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), requestJsonObject.toString());
        OkHttpClient.initPost(Http.regConnectRegistry + "?artificially=" + MD5Util.digest(artificially), requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，注册失败");
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
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                successDialog(jsonObject.getString("data"));
                                spUtils.putString("ddzAccount", jsonObject.getString("data"));
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "注册错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void successDialog(String account) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_register_success_layout, null);
        final MyDialog dialog = new MyDialog(mContext, inflate, R.style.DialogTheme);
        TextView mTextView_message = inflate.findViewById(R.id.mTextView_message);
        mTextView_message.setText("注册成功，ASO娱乐登录账号为：" + account);
        TextView mTextView_true = inflate.findViewById(R.id.mTextView_true);
        mTextView_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                if (OtherUtils.checkAppInstalled(mContext, "com.jjylgames.jjyl")) {
                    PackageManager packageManager = getPackageManager();
                    Intent intent = packageManager.getLaunchIntentForPackage("com.jjylgames.jjyl");
                    startActivity(intent);
                } else {
                    if (ddzDownloadUrl.isEmpty()) {
                        myDialog = DialogUtils.createLoadingDialog(mContext, "获取游戏配置信息...");
                        gameInfo(0);
                    } else {
                        DownLoadApk();
                    }
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTextView_true.setEnabled(false);
                mTextView_true.setText((Math.round((double) millisUntilFinished / 1000) - 1) + "秒");
            }

            @Override
            public void onFinish() {
                mTextView_true.setEnabled(true);
                mTextView_true.setText("确定");
            }
        }.start();
    }

    private void DownLoadApk() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载ASO娱乐");
        progressDialog.show();
        progressDialog.setCancelable(false);
        DownloadUtil.get().download(ddzDownloadUrl, Environment.getExternalStorageDirectory().getAbsolutePath() + "/AresoDDZ", "AresoDdz.apk", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                install(mContext);
            }

            @Override
            public void onDownloading(int progress) {
                progressDialog.setProgress(progress);
            }

            @Override
            public void onDownloadFailed(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "下载失败，请稍候重试!", Toast.LENGTH_LONG).show();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void install(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AresoDDZ/AresoDdz.apk");
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(context, "com.xw.aschwitkey.provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    private void pledgeInfo() {
        myDialog = DialogUtils.createLoadingDialog(mContext, "获取合约信息...");
        OkHttpClient.initGet(Http.pledgeInfo).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，请稍候重试");
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
                                asoTokenAddress = jsonObject.getJSONObject("data").getString("asoTokenAddress");
                                trxToEnergy = new BigDecimal(jsonObject.getJSONObject("data").getString("trxToEnergy"));
                                trxToNet = new BigDecimal(jsonObject.getJSONObject("data").getString("trxToNet"));
                                myDialog.setMsg("获取游戏配置信息...");
                                gameInfo(1);
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

    private void gameInfo(int type) {
        OkHttpClient.initGet(Http.gameInfo).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，获取游戏配置信息失败");
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
                            isClick = false;
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                inTronAddress = jsonObject.getJSONObject("data").getString("inTronAddress");
                                ddzGameCoin = new BigDecimal(jsonObject.getJSONObject("data").getString("ddzGameCoin"));
                                ddzWithdrawService = new BigDecimal(jsonObject.getJSONObject("data").getString("ddzWithdrawService"));
                                ddzDownloadUrl = jsonObject.getJSONObject("data").getString("ddzDownloadUrl");
                                if (type == 0) {
                                    DialogUtils.closeDialog(myDialog);
                                    DownLoadApk();
                                } else if (type == 1) {
                                    getAddressInfo();
                                } else if (type == 2) {
                                    myDialog.setMsg("查询游戏金币余额...");
                                    searchsec(2);
                                }
                            } else {
                                DialogUtils.closeDialog(myDialog);
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "获取游戏配置信息错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void getAddressInfo() {
        myDialog.setMsg("获取Tron账户信息...");
        OkHttpClient.initGet(Http.getTronInfo + tronAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，获取Tron账户信息失败");
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
                            BigDecimal freeNetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetLimit"));
                            BigDecimal freeNetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("freeNetUsed"));
                            BigDecimal NetUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netUsed"));
                            BigDecimal NetLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("netLimit"));
                            BigDecimal energyLimit = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyLimit"));
                            BigDecimal energyUsed = new BigDecimal(jsonObject.getJSONObject("bandwidth").getString("energyUsed"));
                            trxBalance = new BigDecimal(jsonObject.getString("balance")).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN).setScale(6, BigDecimal.ROUND_DOWN);
                            if (energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN).floatValue() < 0f) {
                                energyBalance = new BigDecimal("0");
                            } else {
                                energyBalance = energyLimit.subtract(energyUsed).setScale(0, BigDecimal.ROUND_DOWN);
                            }
                            if (freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN).floatValue() < 0f) {
                                netBalance = new BigDecimal("0");
                            } else {
                                netBalance = freeNetLimit.add(NetLimit).subtract(freeNetUsed).subtract(NetUsed).setScale(0, BigDecimal.ROUND_DOWN);
                            }
                            getAsoBalance();
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "获取Tron账户信息错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void getAsoBalance() {
        myDialog.setMsg("获取ASO余额...");
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
            url = "javascript:selectBalance('" + asoTokenAddress + "','" + tronAddress + "','" + Http.tronUrl + "')";
        } catch (Exception e) {
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
                        ToastUtil.showShort(mContext, str);
                    } else {
                        asoBalance = new BigDecimal(str).divide(decimals, 6, BigDecimal.ROUND_DOWN).setScale(6, BigDecimal.ROUND_DOWN);
                        myDialog = DialogUtils.createLoadingDialog(mContext, "查询游戏金币余额...");
                        searchsec(1);
                    }
                }
            });
        }
    }

    private void getTronTransferEnergy() {
        String url = Http.tronTransferEnergy + "?type=aso";
        OkHttpClient.initGet(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        net = new BigDecimal("0");
                        energy = new BigDecimal("0");
                        ToastUtil.showShort(mContext, "网络错误，预估手续费失败");
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
                            ToastUtil.showShort(mContext, "预估手续费错误：" + e.getMessage());
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
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_ddz_transfer_layout, null);
        TextView mTextView_from = v.findViewById(R.id.mTextView_from);
        TextView mTextView_to = v.findViewById(R.id.mTextView_to);
        EditText mEditText_amount = v.findViewById(R.id.mEditText_amount);
        TextView mTextView_nowGold = v.findViewById(R.id.mTextView_nowGold);
        TextView mTextView_gold = v.findViewById(R.id.mTextView_gold);
        EditText mEditText_password = v.findViewById(R.id.mEditText_password);
        TextView mTextView_net_text = v.findViewById(R.id.mTextView_net_text);
        TextView mTextView_net = v.findViewById(R.id.mTextView_net);
        TextView mTextView_energy_text = v.findViewById(R.id.mTextView_energy_text);
        TextView mTextView_energy = v.findViewById(R.id.mTextView_energy);
        TextView mTextView_btn_cancel = v.findViewById(R.id.mTextView_btn_cancel);
        LinearLayout mLinearLayout_btn_subscribe = v.findViewById(R.id.mLinearLayout_btn_subscribe);
        TextView mTextView_balance = v.findViewById(R.id.mTextView_balance);

        mTextView_balance.setText(asoBalance.setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
        mTextView_nowGold.setText(goldBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());

        mTextView_from.setText(tronAddress);
        mTextView_to.setText(inTronAddress);

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
                if (mEditText_amount.getText().toString().isEmpty()) {
                    mTextView_gold.setText("0");
                } else {
                    mTextView_gold.setText(new BigDecimal(mEditText_amount.getText().toString()).multiply(ddzGameCoin).setScale(3, BigDecimal.ROUND_DOWN).toString());
                }
                if (Float.parseFloat(asoBalance.toString()) == 0f && flag) {
                    ToastUtil.showShort(mContext, "当前可用ASO为零");
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

                    if (result.compareTo(asoBalance) == 1) {
                        temp = asoBalance.setScale(0, BigDecimal.ROUND_DOWN).toString();
                        ToastUtil.showShort(mContext, "最多可以充值" + asoBalance.setScale(0, BigDecimal.ROUND_DOWN).toString() + "ASO");
                    }

                    flag = false;
                    s.clear();
                    if (posDot > 0 && temp.length() - posDot - 0 > 1) {
                        temp = temp.substring(0, posDot + 1);
                    }
                    s.append(temp);

                    flag = true;
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
                if (mEditText_amount.getText().toString().trim().isEmpty() || Float.valueOf(mEditText_amount.getText().toString()) == 0f) {
                    Toast.makeText(mContext, "充值金额不能为空或0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEditText_password.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "交易密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isClick) {
                    return;
                }
                isClick = true;
                transferTonken20Web(inTronAddress, new BigDecimal(mEditText_amount.getText().toString()).multiply(new BigDecimal("1000000")).setScale(0, BigDecimal.ROUND_DOWN).toString(), mEditText_password.getText().toString());
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

    private void transferTonken20Web(String toAddress, String amount, String password) {
        myDialog = DialogUtils.createLoadingDialog(mContext, "充值中...");
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        Token20JavaScriptInterface javaScriptInterface = new Token20JavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/ddzRecharge.html");

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
            url = "javascript:transfer('" + asoTokenAddress + "','" + toAddress + "'," + amount + ",'" + AESUtil.decrypt(privateKey, password) + "','-" + AESUtil.encrypt(spUtils.getInt("userId") + "", spUtils.getString("phone")) + "-','" + Http.tronUrl + "')";
        } catch (Exception e) {
            isClick = false;
            DialogUtils.closeDialog(myDialog);
            ToastUtil.showShort(mContext, "交易密码错误");
        }
        mWebView.loadUrl(url);
    }

    private class Token20JavaScriptInterface {
        @JavascriptInterface
        public void transferFunction(String str) {
            dialog.dismiss();
            isClick = false;
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
                                    xsecadd(TransactionId);
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

    private void xsecadd(String hashValue) {
        JSONObject jsonObject = new JSONObject();
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.xsecadd + "?hashValue=" + hashValue + "&correlationId=" + spUtils.getString("ddzAccount", ""), requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，充值失败");
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
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (jsonObject1.getInt("code") == 1) {
                                ToastUtil.showShort(mContext, "充值成功");
                            } else {
                                ToastUtil.showLong(mContext, jsonObject1.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "充值错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void searchsec(int type) {
        OkHttpClient.initGet(Http.searchsec + "?correlationId=" + spUtils.getString("ddzAccount", "")).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，查询游戏金币余额失败");
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
                                goldBalance = new BigDecimal(jsonObject.getString("data"));
                                if (type == 1) {
                                    myDialog.setMsg("预估手续费...");
                                    getTronTransferEnergy();
                                } else {
                                    withdrawalDialog();
                                }
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "查询游戏金币余额错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void withdrawalDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_ddz_withdrawal_layout, null);
        EditText mEditText_to = v.findViewById(R.id.mEditText_to);
        EditText mEditText_amount = v.findViewById(R.id.mEditText_amount);
        TextView mTextView_aso = v.findViewById(R.id.mTextView_aso);
        EditText mEditText_password = v.findViewById(R.id.mEditText_password);
        TextView mTextView_fee = v.findViewById(R.id.mTextView_fee);
        TextView mTextView_btn_cancel = v.findViewById(R.id.mTextView_btn_cancel);
        LinearLayout mLinearLayout_btn_subscribe = v.findViewById(R.id.mLinearLayout_btn_subscribe);
        TextView mTextView_balance = v.findViewById(R.id.mTextView_balance);

        mTextView_balance.setText(goldBalance.setScale(3, BigDecimal.ROUND_DOWN).toString());

        mEditText_to.setText(tronAddress);
        mTextView_fee.setText(ddzWithdrawService.setScale(6, BigDecimal.ROUND_DOWN).toString() + " ASO");

        mEditText_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditText_amount.getText().toString().isEmpty()) {
                    mTextView_aso.setText("0");
                } else if (new BigDecimal(mEditText_amount.getText().toString()).divide(ddzGameCoin, 6, BigDecimal.ROUND_DOWN).subtract(ddzWithdrawService).floatValue() < 0f) {
                    mTextView_aso.setText("0");
                    ToastUtil.showShort(mContext, "提现数量少于手续费，请重新输入");
                } else {
                    mTextView_aso.setText(new BigDecimal(mEditText_amount.getText().toString()).divide(ddzGameCoin, 6, BigDecimal.ROUND_DOWN).subtract(ddzWithdrawService).setScale(6, BigDecimal.ROUND_DOWN).toString());
                }
                if (Float.parseFloat(goldBalance.toString()) == 0f && flag) {
                    ToastUtil.showShort(mContext, "当前可用金币为零");
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

                    if (result.compareTo(goldBalance) == 1) {
                        temp = goldBalance.setScale(0, BigDecimal.ROUND_DOWN).toString();
                        ToastUtil.showShort(mContext, "最多可以提现" + goldBalance.setScale(0, BigDecimal.ROUND_DOWN).toString() + "金币");
                    }

                    flag = false;
                    s.clear();
                    if (posDot > 0 && temp.length() - posDot - 1 > 0) {
                        temp = temp.substring(0, posDot + 1);
                    }
                    s.append(temp);

                    flag = true;
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
                if (mEditText_amount.getText().toString().trim().isEmpty() || Float.valueOf(mEditText_amount.getText().toString()) == 0f) {
                    Toast.makeText(mContext, "提现金币不能为空或0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (new BigDecimal(mEditText_amount.getText().toString()).divide(ddzGameCoin, 6, BigDecimal.ROUND_DOWN).subtract(ddzWithdrawService).floatValue() < 0f) {
                    Toast.makeText(mContext, "提现数量少于手续费，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEditText_password.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "交易密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isClick) {
                    return;
                }
                isClick = true;
                myDialog = DialogUtils.createLoadingDialog(mContext, "验证Tron地址格式...");
                try {
                    validateaddress(mEditText_to.getText().toString(), mEditText_amount.getText().toString(), AESUtil.encrypt(spUtils.getString("phone"), mEditText_password.getText().toString()));
                } catch (Exception e) {
                    isClick = false;
                    ToastUtil.showShort(mContext, "提现错误：" + e.getMessage());
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

    private void validateaddress(String address, String amount, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("address", address);
        } catch (JSONException e) {
            isClick = false;
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.validateaddress, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，验证Tron地址格式失败");
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
                            isClick = false;
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (jsonObject1.getBoolean("result")) {
                                xsecsub(address, amount, password);
                            } else {
                                DialogUtils.closeDialog(myDialog);
                                ToastUtil.showShort(mContext, "输入的Tron地址格式不正确");
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "验证Tron地址格式错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void xsecsub(String address, String amount, String password) {
        myDialog.setMsg("提现中...");
        JSONObject jsonObject = new JSONObject();
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.xsecsub + "?confw=" + amount + "&correlationId=" + spUtils.getString("ddzAccount") + "&tronAddress=" + address + "&dealPwd=" + password, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，提现申请提交失败");
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
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (jsonObject1.getInt("code") == 1) {
                                dialog.dismiss();
                                ToastUtil.showShort(mContext, "提现申请提交成功");
                            } else {
                                ToastUtil.showLong(mContext, jsonObject1.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "提现申请提交错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OtherUtils.checkAppInstalled(mContext, "com.jjylgames.jjyl")) {
            mTextView_install.setText("点击打开");
        } else {
            mTextView_install.setText("点击安装");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
