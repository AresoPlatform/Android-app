package com.xw.aschwitkey.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.db.SQLUtils;
import com.xw.aschwitkey.entity.CreateUserBean;
import com.xw.aschwitkey.entity.DBHelperBean;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.entity.UserBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.AESUtil;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImportAccountActivity extends AppCompatActivity {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mTextView_title)
    TextView mTextView_title;
    @BindView(R.id.mEditText_note)
    EditText mEditText_note;
    @BindView(R.id.mEditText_mnemonicWord)
    EditText mEditText_mnemonicWord;
    @BindView(R.id.mTextView_btn_import)
    TextView mTextView_btn_import;

    private Context mContext;
    private WebView mWebView;
    private boolean isImport, isClick = false, isOk = false;
    private Gson gson;
    private String secret;
    private MyDialog myDialog;
    private String tradePassword;
    private SPUtils spUtils, spUtils1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_account);
        ButterKnife.bind(this);
        OtherUtils.config(this);
        init();
    }

    private void init() {
        mContext = ImportAccountActivity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(ImportAccountActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

        spUtils = new SPUtils(mContext, "AschWallet");
        spUtils1 = new SPUtils(mContext, "AschImport");
        gson = new Gson();
        Intent intent = getIntent();
        isImport = intent.getBooleanExtra("isImport", true);
        tradePassword = intent.getStringExtra("tradePassword");
        if (isImport) {
            mTextView_title.setText("导入账户");
            mTextView_btn_import.setText("确认导入");
            mEditText_mnemonicWord.setVisibility(View.VISIBLE);
        } else {
            mTextView_title.setText("新建账户");
            mTextView_btn_import.setText("确认新建");
            mEditText_mnemonicWord.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.mImageView_back, R.id.mTextView_btn_import})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mImageView_back:
                finish();
                break;
            case R.id.mTextView_btn_import:
                if (isClick) {
                    return;
                }
                isClick = true;
                if (mEditText_note.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "账户备注不能为空", Toast.LENGTH_SHORT).show();
                    isClick = false;
                    return;
                }
                boolean isThere = false;
                List<DBHelperBean> bean = SQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                for (int i = 0; i < bean.size(); i++) {
                    if (bean.get(i).getAccount().equals(mEditText_note.getText().toString())) {
                        isThere = true;
                    }
                }
                if (isThere) {
                    Toast.makeText(mContext, "账户备注不能重复", Toast.LENGTH_SHORT).show();
                    isClick = false;
                    return;
                }
                if (isImport) {
                    if (mEditText_mnemonicWord.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "助记词不能为空", Toast.LENGTH_SHORT).show();
                        isClick = false;
                        return;
                    }
                    if (!OtherUtils.isPuBlick(mEditText_mnemonicWord.getText().toString())) {
                        Toast.makeText(mContext, "助记词格式不正确", Toast.LENGTH_SHORT).show();
                        isClick = false;
                        return;
                    }
                    secret = mEditText_mnemonicWord.getText().toString();
                    myDialog = DialogUtils.createLoadingDialog(mContext, "导入中...");
                    login();
                } else {
                    myDialog = DialogUtils.createLoadingDialog(mContext, "创建中...");
                    createAccount();
                }
                break;
        }
    }

    private void login() {
        mWebView = new WebView(ImportAccountActivity.this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.clearCache(true);
        JavaScriptInterface javaScriptInterface = new JavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "stub");
        mWebView.loadUrl("file:///android_asset/about.html");


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                JsParse(secret);
            }
        });
    }

    private void JsParse(String data) {
        String url = "javascript:stub.startFunction(encrypt('" + data + "'));";
        mWebView.loadUrl(url);
    }

    private class JavaScriptInterface {
        @JavascriptInterface
        public void startFunction(String str) {
            doLoginQ(str);
        }
    }

    private void doLoginQ(String str) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("publicKey", str);
        } catch (JSONException e) {
            isClick = false;
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.Accounts, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，导入账户失败");
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
                            UserBean userBean = gson.fromJson(result, UserBean.class);
                            if (userBean.isSuccess()) {
                                boolean isThere = false;
                                List<DBHelperBean> bean = SQLUtils.QuerySQLAll(mContext, "where state = '" + spUtils.getString("phone") + "@'");
                                for (int i = 0; i < bean.size(); i++) {
                                    if (bean.get(i).getAddress().equals(userBean.getAccount().getAddress())) {
                                        isThere = true;
                                    }
                                }
                                if (!isThere) {
                                    SQLUtils.AddSql(mContext, mEditText_note.getText().toString(), userBean.getAccount().getAddress(), AESUtil.encrypt(secret, tradePassword), spUtils.getString("phone") + "@");
                                    ToastUtil.showShort(mContext, "导入成功");
                                    setResult(RESULT_OK);
                                    spUtils1.putString("childAddress", userBean.getAccount().getAddress());
                                    finish();
                                } else {
                                    ToastUtil.showShort(mContext, "该账户已导入，请勿重复导入");
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
    }

    private void createAccount() {
        OkHttpClient.initGet(Http.newAccount).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，创建账户失败");
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
                            CreateUserBean createUserBean = gson.fromJson(result, CreateUserBean.class);
                            if (createUserBean.isSuccess()) {
                                SQLUtils.AddSql(mContext, mEditText_note.getText().toString(), createUserBean.getAddress(), AESUtil.encrypt(createUserBean.getSecret(), tradePassword), spUtils.getString("phone") + "@");
                                ToastUtil.showShort(mContext, "创建成功");
                                Intent intent = new Intent(mContext, BackupAccountActivity2.class);
                                intent.putExtra("secret", createUserBean.getSecret());
                                intent.putExtra("accountName", mEditText_note.getText().toString());
                                intent.putExtra("isCreator", true);
                                startActivityForResult(intent, 1);
                            } else {
                                ToastUtil.showShort(mContext, "创建失败");
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
    }

    private void getXasTestToken(String address) {
        OkHttpClient.initGet(Http.getXasTestToken + "?aschAddress=" + address).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，领取测试币失败");
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
                                ToastUtil.showShort(mContext, "领取测试币成功");
                                setResult(RESULT_OK);
                                finish();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
