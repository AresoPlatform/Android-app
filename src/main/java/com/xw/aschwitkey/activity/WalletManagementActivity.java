package com.xw.aschwitkey.activity;

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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.AESUtil;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class WalletManagementActivity extends AppCompatActivity {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mTextView_address)
    TextView mTextView_address;

    private Context mContext;
    private SPUtils spUtils;
    private SPUtils spUtils1;
    private Dialog dialog;
    private boolean isClick = false;
    private Gson gson;
    private MyDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_management);
        ButterKnife.bind(this);
        OtherUtils.config(this);
        init();
    }

    private void init() {
        mContext = WalletManagementActivity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(WalletManagementActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

        gson = new Gson();
        spUtils = new SPUtils(mContext, "AschWallet");
        spUtils1 = new SPUtils(mContext, "AschImport");
        mTextView_address.setText(spUtils1.getString("childAddress", ""));
    }

    @OnClick({R.id.mImageView_back, R.id.mRelativeLayout_accountManagement, R.id.mRelativeLayout_backupAccount, R.id.mRelativeLayout_secondPassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mImageView_back:
                finish();
                break;
            case R.id.mRelativeLayout_accountManagement:
                if (!spUtils.getBoolean("isDeal", false)) {
                    ToastUtil.showLong(mContext, "请先到账户中心设置交易密码");
                    return;
                }
                Intent am = new Intent(mContext, AccountManagementActivity.class);
                am.putExtra("tradePassword","");
                startActivityForResult(am, 1);
                break;
            case R.id.mRelativeLayout_backupAccount:
                if(spUtils1.getString("childAddress","").isEmpty()){
                    ToastUtil.showLong(mContext, "您还没有添加账户，请先到账户管理中导入或创建账户");
                    return;
                }
                if (!spUtils.getBoolean("isDeal", false)) {
                    ToastUtil.showLong(mContext, "请先到账户中心设置交易密码");
                    return;
                }
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    dialogAuthentication(0);
                }
                break;
            case R.id.mRelativeLayout_secondPassword:
                if(spUtils1.getString("childAddress","").isEmpty()){
                    ToastUtil.showLong(mContext, "您还没有添加账户，请先到账户管理中导入或创建账户");
                    return;
                }
                if (!spUtils.getBoolean("isDeal", false)) {
                    ToastUtil.showLong(mContext, "请先到账户中心设置交易密码");
                    return;
                }
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    dialogAuthentication(1);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mTextView_address.setText(spUtils1.getString("childAddress", ""));
        }
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

    private void verifyAccount(String password, int type) {
        Map postmap = new HashMap();
        try {
            postmap.put("password", AESUtil.encrypt(spUtils.getString("phone"), password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        myDialog = DialogUtils.createLoadingDialog(mContext, "请求中...");
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.verifyAccount, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，身份验证失败");
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
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                if (type == 0) {
                                    DialogUtils.closeDialog(myDialog);
                                    dialog.dismiss();
                                    Intent intent = new Intent(mContext, BackupAccountActivity.class);
                                    intent.putExtra("tradePassword", password);
                                    startActivity(intent);
                                } else {
                                    OkHttpClient.initGet(Http.getAddressInfo + "?address=" + spUtils1.getString("childAddress")).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    DialogUtils.closeDialog(myDialog);
                                                    dialog.dismiss();
                                                    ToastUtil.showShort(mContext,"获取账户信息失败，请稍候重试");
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
                                                        JSONObject jsonObject = new JSONObject(result);
                                                        if (jsonObject.getInt("code") == 1) {
                                                            dialog.dismiss();
                                                            DialogUtils.closeDialog(myDialog);
                                                            if(jsonObject.getJSONObject("data").getBoolean("isHaveSecond")){
                                                                ToastUtil.showShort(mContext,"您已设置二级密码");
                                                            }else{
                                                                Intent intent = new Intent(mContext, SecondSecretActivity.class);
                                                                intent.putExtra("tradePassword", password);
                                                                startActivity(intent);
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
                            } else {
                                DialogUtils.closeDialog(myDialog);
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

    private void hintKeyBoard(View view) {
        InputMethodManager inputMgr = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
