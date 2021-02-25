package com.xw.aschwitkey.activity;

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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lihang.ShadowLayout;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.DESUtil;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mTextView_btn_passLogin)
    TextView mTextView_btn_passLogin;
    @BindView(R.id.mTextView_btn_codeLogin)
    TextView mTextView_btn_codeLogin;
    @BindView(R.id.mShadowLayout_password)
    ShadowLayout mShadowLayout_password;
    @BindView(R.id.mShadowLayout_code)
    ShadowLayout mShadowLayout_code;
    @BindView(R.id.mEditText_phone)
    EditText mEditText_phone;
    @BindView(R.id.mEditText_password)
    EditText mEditText_password;
    @BindView(R.id.mEditText_code)
    EditText mEditText_code;
    @BindView(R.id.mTextView_getCode)
    TextView mTextView_getCode;
    @BindView(R.id.mImageView_winfo)
    ImageView mImageView_winfo;

    private Context mContext;
    private SPUtils spUtils,spUtils1;
    private Gson gson;
    private CountDownTimer countDownTimer;
    private boolean isPhone = false, isClick = false;
    private int type = 0;
    private MyDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        OtherUtils.config(this);
        init();
    }

    private void init() {
        mContext = LoginActivity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(LoginActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

        spUtils = new SPUtils(mContext, "AschWallet");
        spUtils1 = new SPUtils(mContext,"AschImport");
        mTextView_btn_passLogin.setSelected(true);
        gson = new Gson();

        mEditText_password.setInputType(129);

        mEditText_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = mEditText_phone.getText().toString();
                if (!OtherUtils.isPhone(phone)) {
                    isPhone = false;
                } else {
                    isPhone = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.mImageView_back, R.id.mImageView_winfo, R.id.mTextView_register, R.id.mTextView_btn_passLogin, R.id.mTextView_btn_codeLogin, R.id.mTextView_getCode, R.id.mTextView_btn_withdrawals, R.id.mTextView_forgotPassword})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mImageView_back:
                finish();
                break;
            case R.id.mImageView_winfo:
                if (!isClick) {
                    mEditText_password.setInputType(128);
                    mImageView_winfo.setImageResource(R.mipmap.winfo_show);
                    isClick = true;
                } else {
                    mEditText_password.setInputType(129);
                    mImageView_winfo.setImageResource(R.mipmap.winfo_hide);
                    isClick = false;
                }
                break;
            case R.id.mTextView_register:
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.mTextView_forgotPassword:
                ToastUtil.showShort(mContext, "开发中，请期待");
                break;
            case R.id.mTextView_btn_passLogin:
                type = 0;
                mTextView_btn_passLogin.setSelected(true);
                mTextView_btn_codeLogin.setSelected(false);
                mShadowLayout_password.setVisibility(View.VISIBLE);
                mShadowLayout_code.setVisibility(View.GONE);
                break;
            case R.id.mTextView_btn_codeLogin:
                type = 1;
                mTextView_btn_codeLogin.setSelected(true);
                mTextView_btn_passLogin.setSelected(false);
                mShadowLayout_password.setVisibility(View.GONE);
                mShadowLayout_code.setVisibility(View.VISIBLE);
                break;
            case R.id.mTextView_getCode:
                if (isPhone) {
                    getCode();
                }
                break;
            case R.id.mTextView_btn_withdrawals:
                if (mEditText_phone.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isPhone) {
                    Toast.makeText(mContext, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type == 0 && mEditText_password.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type == 1 && mEditText_code.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                login();
                break;
        }
    }

    private void getCode() {
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

        String phone = mEditText_phone.getText().toString();
        OkHttpClient.initGet(Http.getCode + "?phone=" + phone + "&operationCode=1").enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(mContext, "网络错误，验证码获取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                ToastUtil.showShort(mContext, "验证码已发送至您的手机，请注意查收");
                            } else {
                                countDownTimer.cancel();
                                mTextView_getCode.setEnabled(true);
                                mTextView_getCode.setText("获取验证码");
                                mTextView_getCode.setTextColor(getResources().getColor(R.color.text_bg));
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

    private void login() {
        myDialog = DialogUtils.createLoadingDialog(mContext, "登录中...");
        String phone = mEditText_phone.getText().toString();
        String password = mEditText_password.getText().toString();
        String code = mEditText_code.getText().toString();

        for (int i = 0; i < password.length(); i++) {
            if (i * 2 + 1 > password.length() - 1) {
                break;
            }
            String s = OtherUtils.getRandomString(1);
            StringBuffer sb = new StringBuffer(password);
            sb.insert(i * 2 + 1, s).toString();
            password = sb.toString();
        }
        String content = "null" + "+" + phone + "+" + password;
        Map postmap = new HashMap();
        postmap.put("phone", phone);
        postmap.put("password", type == 0 ? DESUtil.encrypt(content) : "");
        postmap.put("val", type == 1 ? code : "");

        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.login, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，登录失败");
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
                                JSONObject object = jsonObject.getJSONObject("data");
                                spUtils.putString("loginToken", object.getString("token"));
                                spUtils.putString("expireTime", object.getString("expireTime"));
                                spUtils.putInt("userId", object.getInt("userId"));
                                spUtils.putString("phone", phone);
                                spUtils1.putBoolean("isOutLogin",true);
                                if (spUtils.getString("phone", "").isEmpty()) {
                                    ToastUtil.showShort(mContext, "您的登录状态有误，请尝试重新登录");
                                } else {
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    finish();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

}
