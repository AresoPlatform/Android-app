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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.DESUtil;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mEditText_phone)
    EditText mEditText_phone;
    @BindView(R.id.mTextView_phone)
    TextView mTextView_phone;
    @BindView(R.id.mEditText_password)
    EditText mEditText_password;
    @BindView(R.id.mTextView_password)
    TextView mTextView_password;
    @BindView(R.id.mEditText_verification)
    EditText mEditText_verification;
    @BindView(R.id.mTextView_getCode)
    TextView mTextView_getCode;
    @BindView(R.id.mEditText_inviteCode)
    EditText mEditText_inviteCode;
    @BindView(R.id.mTextView_btn_withdrawals)
    TextView mTextView_btn_withdrawals;
    @BindView(R.id.mImageView_winfo)
    ImageView mImageView_winfo;

    private Context mContext;
    private CountDownTimer countDownTimer;
    private boolean isPhone = false, isPassword = false, isCode = false, isAccess = true, isClick = false,isShow = false;
    private Gson gson;
    private MyDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        OtherUtils.config(this);
        init();
    }

    private void init() {
        mContext = RegisterActivity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(RegisterActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

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
                    mTextView_phone.setVisibility(View.VISIBLE);
                } else {
                    isPhone = true;
                    mTextView_phone.setVisibility(View.GONE);
                }
                isOk();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = mEditText_password.getText().toString();
                String reg = "^(?![^a-zA-Z]+$)(?!\\D+$).{6,12}$";
                if (password.matches(reg)) {
                    mTextView_password.setVisibility(View.GONE);
                    isPassword = true;
                } else {
                    mTextView_password.setVisibility(View.VISIBLE);
                    isPassword = false;
                }
                isOk();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditText_verification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isCode = true;
                isOk();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.mImageView_back, R.id.mImageView_winfo, R.id.mTextView_btn_withdrawals, R.id.mTextView_getCode, R.id.mTextView_btn_goLogin})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mImageView_back:
            case R.id.mTextView_btn_goLogin:
                finish();
                break;
            case R.id.mImageView_winfo:
                if (!isShow) {
                    mEditText_password.setInputType(128);
                    mImageView_winfo.setImageResource(R.mipmap.winfo_show);
                    isShow = true;
                } else {
                    mEditText_password.setInputType(129);
                    mImageView_winfo.setImageResource(R.mipmap.winfo_hide);
                    isShow = false;
                }
                break;
            case R.id.mTextView_getCode:
                if (isPhone && isPassword) {
                    getCode();
                }
                break;
            case R.id.mTextView_btn_withdrawals:
                if (!isClick) {
                    register();
                }
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
        OkHttpClient.initGet(Http.getCode + "?phone=" + phone + "&operationCode=2").enqueue(new Callback() {
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
                                isAccess = false;
                            } else {
                                isAccess = false;
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

    private void register() {
        myDialog = DialogUtils.createLoadingDialog(mContext, "注册中...");
        String pass = mEditText_password.getText().toString();
        for (int i = 0; i < pass.length(); i++) {
            if (i * 2 + 1 > pass.length() - 1) {
                break;
            }
            String s = OtherUtils.getRandomString(1);
            StringBuffer sb = new StringBuffer(pass);
            sb.insert(i * 2 + 1, s).toString();
            pass = sb.toString();
        }
        String content = "null" + "+" + mEditText_phone.getText().toString() + "+" + pass;
        Map postmap = new HashMap();
        postmap.put("phone", mEditText_phone.getText().toString());
        postmap.put("password", DESUtil.encrypt(content));
        postmap.put("verifyCode", mEditText_verification.getText().toString());
        postmap.put("invitationCode", mEditText_inviteCode.getText().toString());

        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.reg, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClick = false;
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，注册失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                isClick = false;
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DialogUtils.closeDialog(myDialog);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                ToastUtil.showShort(mContext, "注册成功");
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

    private void isOk() {
        if (isPhone && isPassword && !isAccess && isCode) {
            mTextView_btn_withdrawals.setEnabled(true);
            mTextView_btn_withdrawals.setBackgroundResource(R.drawable.selector_button_transfer);
            mTextView_btn_withdrawals.setTextColor(getResources().getColor(R.color.white));
        } else {
            mTextView_btn_withdrawals.setEnabled(false);
            mTextView_btn_withdrawals.setBackgroundResource(R.drawable.selector_button_gray);
            mTextView_btn_withdrawals.setTextColor(getResources().getColor(R.color.text_color_gray));
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
