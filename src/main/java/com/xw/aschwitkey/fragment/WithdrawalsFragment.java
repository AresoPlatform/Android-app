package com.xw.aschwitkey.fragment;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.AESUtil;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WithdrawalsFragment extends NewLazyFragment {

    @BindView(R.id.mEditText_address)
    EditText mEditText_address;
    @BindView(R.id.mEditText_number)
    EditText mEditText_number;
    @BindView(R.id.mEditText_password)
    EditText mEditText_password;

    private Activity mContext;
    private boolean flag = true;
    private boolean isLoading = false;
    private String balance = "0";
    private Gson gson;
    private MyDialog myDialog;
    private SPUtils spUtils;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_withdrawals_layout;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        spUtils = new SPUtils(mContext, "AschWallet");
        gson = new Gson();
        mEditText_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Double.parseDouble(balance) == 0d && flag) {
                    ToastUtil.showShort(mContext, "当前可用余额为零");
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

                    if (result.compareTo(new BigDecimal(balance).subtract(new BigDecimal("0.1"))) == 1) {
                        temp = new DecimalFormat("0.000").format(new BigDecimal(balance).subtract(new BigDecimal("0.1")));
                        ToastUtil.showShort(mContext, "最多可以提现" + new DecimalFormat("0.000").format(new BigDecimal(balance).subtract(new BigDecimal("0.1"))) + "XAS");
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
    }

    @OnClick({R.id.mTextView_btn_half, R.id.mTextView_btn_two, R.id.mTextView_btn_ten, R.id.mTextView_btn_withdrawals})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTextView_btn_half:
                if (mEditText_number.getText().toString().isEmpty()) {
                    return;
                }
                BigDecimal bd = new BigDecimal(0.5);
                BigDecimal bd1 = new BigDecimal(mEditText_number.getText().toString());
                bd1 = bd1.multiply(bd);
                mEditText_number.setText(bd1.toString());
                break;
            case R.id.mTextView_btn_two:
                if (mEditText_number.getText().toString().isEmpty()) {
                    return;
                }
                BigDecimal bd3 = new BigDecimal(2);
                BigDecimal bd4 = new BigDecimal(mEditText_number.getText().toString());
                bd4 = bd4.multiply(bd3);
                mEditText_number.setText(bd4.toString());
                break;
            case R.id.mTextView_btn_ten:
                if (mEditText_number.getText().toString().isEmpty()) {
                    return;
                }
                BigDecimal bd5 = new BigDecimal(10);
                BigDecimal bd6 = new BigDecimal(mEditText_number.getText().toString());
                bd6 = bd6.multiply(bd5);
                mEditText_number.setText(bd6.toString());
                break;
            case R.id.mTextView_btn_withdrawals:
                if (isLoading) {
                    return;
                }
                hintKeyBoard(v);
                if (!spUtils.getBoolean("isDeal", false)) {
                    ToastUtil.showLong(mContext, "请先到账户中心设置交易密码");
                    return;
                }
                if (mEditText_address.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "账户地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEditText_number.getText().toString().isEmpty() || Double.parseDouble(mEditText_number.getText().toString()) == 0d) {
                    Toast.makeText(mContext, "提现数额不能为空或0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Double.parseDouble(mEditText_number.getText().toString()) <= 0.1d) {
                    Toast.makeText(mContext, "提现数额小于0.1", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEditText_password.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "交易密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                withdrawal();
                break;
        }
    }

    private void withdrawal() {
        isLoading = true;
        myDialog = DialogUtils.createLoadingDialog(mContext, "提现中...");
        String password = mEditText_password.getText().toString();
        Map postmap = new HashMap();
        postmap.put("aschAddress", mEditText_address.getText().toString());
        try {
            postmap.put("dealPwd", AESUtil.encrypt(spUtils.getString("phone"), password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        postmap.put("amount", mEditText_number.getText().toString());

        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.transfer, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，提现请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                isLoading = false;
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DialogUtils.closeDialog(myDialog);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                mEditText_number.setText("");
                                mEditText_password.setText("");
                                ToastUtil.showShort(mContext, "提现成功");
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessageBean messageEvent) {
        switch (messageEvent.getTag()) {
            case 2:
                balance = messageEvent.getMesssage().get("balance") + "";
                break;
            case 6:
                mEditText_address.setText(messageEvent.getMesssage().get("address").toString());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void hintKeyBoard(View view) {
        InputMethodManager inputMgr = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
