package com.xw.aschwitkey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.fragment.RechargeRecordFragment;
import com.xw.aschwitkey.fragment.WithdrawalRecordFragment;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.ViewUtils;
import com.xw.aschwitkey.view.AutoHeightViewPager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DdzHistoryActivity extends AppCompatActivity {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mViewPager)
    AutoHeightViewPager mViewPager;
    @BindView(R.id.mTextView_rechargeRecord)
    TextView mTextView_rechargeRecord;
    @BindView(R.id.mTextView_withdrawalsRecord)
    TextView mTextView_withdrawalsRecord;
    @BindView(R.id.mTextView_bg)
    TextView mTextView_bg;

    private Activity mContext;
    private List<Fragment> fragments;
    private MyViewPagerAdapter adapter;
    private boolean isRecharge = true, isAnimation = false, isClick = false;
    private Dialog dialog;
    private MyDialog myDialog;
    private String inTronAddress = "";
    private SPUtils spUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddz_history);
        ButterKnife.bind(this);
        OtherUtils.config(this);
        init();
    }

    private void init() {
        mContext = DdzHistoryActivity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(DdzHistoryActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

        spUtils = new SPUtils(mContext, "AschWallet");
        fragments = new ArrayList<>();
        fragments.add(new RechargeRecordFragment());
        fragments.add(new WithdrawalRecordFragment());
        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mContext);

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setScanScroll(true);
        mViewPager.setCurrentItem(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.requestLayout();
                if (position == 0) {
                    isRecharge = true;
                } else {
                    isRecharge = false;
                }
                if (isRecharge && !isAnimation) {
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
                    mViewPager.setCurrentItem(0);
                    mTextView_rechargeRecord.setTextColor(getResources().getColor(R.color.text_bg));
                    mTextView_withdrawalsRecord.setTextColor(getResources().getColor(R.color.TextColor1));
                }
                if (!isRecharge && !isAnimation) {
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
                    mTextView_rechargeRecord.setTextColor(getResources().getColor(R.color.TextColor1));
                    mTextView_withdrawalsRecord.setTextColor(getResources().getColor(R.color.text_bg));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.mImageView_back, R.id.mTextView_rechargeRecord, R.id.mTextView_withdrawalsRecord, R.id.mTextView_errSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mImageView_back:
                finish();
                break;
            case R.id.mTextView_rechargeRecord:
                if (!isRecharge && !isAnimation) {
                    isRecharge = true;
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
                    mViewPager.setCurrentItem(0);
                    mTextView_rechargeRecord.setTextColor(getResources().getColor(R.color.text_bg));
                    mTextView_withdrawalsRecord.setTextColor(getResources().getColor(R.color.TextColor1));
                }
                break;
            case R.id.mTextView_withdrawalsRecord:
                if (isRecharge && !isAnimation) {
                    isRecharge = false;
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
                    mViewPager.setCurrentItem(1);
                    mTextView_rechargeRecord.setTextColor(getResources().getColor(R.color.TextColor1));
                    mTextView_withdrawalsRecord.setTextColor(getResources().getColor(R.color.text_bg));
                }
                break;
            case R.id.mTextView_errSubmit:
                if (isClick) {
                    return;
                }
                isClick = true;
                appealDialog();
                break;
        }
    }

    private void appealDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_appeal_layout, null);
        EditText mEditText_hash = view.findViewById(R.id.mEditText_hash);

        view.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.mTextView_btn_appeal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                String hash = mEditText_hash.getText().toString();
                if (hash.trim().isEmpty()) {
                    ToastUtil.showShort(mContext, "哈希值不能为空");
                    return;
                }
                myDialog = DialogUtils.createLoadingDialog(mContext, "查询账户注册状态...");
                checkAccount(hash);
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

    private void checkAccount(String hash) {
        OkHttpClient.initGet(Http.checkAccount).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，查询账号注册状态失败");
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
                                if (jsonObject.getString("data").isEmpty()) {
                                    DialogUtils.closeDialog(myDialog);
                                    ToastUtil.showShort(mContext, "您暂未注册ASO娱乐账号");
                                } else {
                                    spUtils.putString("ddzAccount", jsonObject.getString("data"));
                                    myDialog.setMsg("获取服务器信息...");
                                    gameInfo(hash);
                                }
                            } else {
                                DialogUtils.closeDialog(myDialog);
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "查询账号注册状态错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void gameInfo(String hash) {
        OkHttpClient.initGet(Http.gameInfo).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，获取服务器信息失败");
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
                                inTronAddress = jsonObject.getJSONObject("data").getString("inTronAddress");
                                myDialog.setMsg("链上验证哈希值...");
                                validateHash(hash);
                            } else {
                                DialogUtils.closeDialog(myDialog);
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "服务器信息错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void validateHash(String hash) {
        OkHttpClient.initGet(Http.validateHash + hash).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，验证哈希值失败");
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
                            if (jsonObject.getString("contractRet").equals("SUCCESS") && !jsonObject.getString("data").isEmpty() && jsonObject.getJSONObject("trigger_info").getString("method").contains("transfer") && jsonObject.getJSONObject("trigger_info").getJSONObject("parameter").getString("recipient").equals(inTronAddress)) {
                                myDialog.setMsg("提交申诉...");
                                xsecaddBu(hash);
                            } else {
                                DialogUtils.closeDialog(myDialog);
                                ToastUtil.showShort(mContext, "该充值记录不存在");
                            }
                        } catch (JSONException e) {
                            DialogUtils.closeDialog(myDialog);
                            ToastUtil.showShort(mContext, "验证哈希值错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void xsecaddBu(String hashValue) {
        JSONObject jsonObject = new JSONObject();
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), jsonObject.toString());
        OkHttpClient.initPost(Http.xsecaddBu + "?hashValue=" + hashValue + "&correlationId=" + spUtils.getString("ddzAccount", ""), requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，提交申诉失败");
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
                                ToastUtil.showShort(mContext, "申诉成功");
                                EventBus.getDefault().postSticky(new EventMessageBean(30, null));
                            } else {
                                ToastUtil.showShort(mContext, jsonObject1.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "提交申诉错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        private Context context;

        public MyViewPagerAdapter(FragmentManager fm, int behavior, Context context) {
            super(fm, behavior);
            this.context = context;
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

}
