package com.xw.aschwitkey.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.ConfigBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.DownloadUtil;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.ViewUtils;
import com.xw.aschwitkey.view.VersionDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mLottieAnimationView)
    LottieAnimationView mLottieAnimationView;
    @BindView(R.id.mTextView_btn_login)
    TextView mTextView_btn_login;

    private Context mContext;
    private SPUtils spUtils;
    private boolean isClick = false;
    private RequestQueue mQueue;
    private long serverTime = 0;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        OtherUtils.config(this);
        init();
    }

    private void init() {
        mContext = StartActivity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(StartActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

        gson = new Gson();
        mQueue = Volley.newRequestQueue(mContext);
        spUtils = new SPUtils(mContext, "AschWallet");
        mLottieAnimationView.setAnimation(R.raw.start_aschwitkey);
        mLottieAnimationView.playAnimation();
        nextOne();
    }

    @OnClick({R.id.mTextView_btn_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTextView_btn_login:
                if (!isClick) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    private void nextOne() {
        if (AndPermission.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            verifyUpdate();
        } else {
            SDApply();
        }
    }

    private void SDApply() {
        AndPermission.with(this)
                .runtime()
                .permission(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .rationale(new Rationale<List<String>>() {
                    @Override
                    public void showRationale(Context context, List<String> data, final RequestExecutor executor) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                        builder.setTitle("权限已被拒绝");
                        builder.setMessage("您已经拒绝过我们申请授权，请您同意授权，否则功能无法正常使用");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                executor.cancel();
                            }
                        });
                        builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                executor.execute();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                })
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        verifyUpdate();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(StartActivity.this, permissions)) {
                            ToastUtil.showLong(StartActivity.this, "您没有允许部分权限，请到设置界面开启权限");
                        }
                    }
                })
                .start();
    }

    private void verifyUpdate() {
        OkHttpClient.initGet(Http.versionInfo).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowDialog("网络异常");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!result.isEmpty() && !result.substring(0, 1).equals("<")) {
                            ConfigBean configBean = gson.fromJson(result, ConfigBean.class);
                            if (configBean.getCode() == 1) {
                                Content.Android_Download_Url = configBean.getData().getAndroid_Download_Url();
                                if (configBean.getData().getAndroid_Version().equals(OtherUtils.getLocalVersionName(StartActivity.this))) {
                                    if (!spUtils.getString("expireTime").isEmpty() && spUtils.getInt("userId", -1) != -1) {
                                        getServerTime();
                                    } else {
                                        mTextView_btn_login.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    ShowDialogUp(configBean.getData().getAndroid_Download_Url(),
                                            configBean.getData().getAndroid_Version(),
                                            configBean.getData().getAndroid_Update_Content(),
                                            configBean.getData().getAndroid_Update_Date(),
                                            configBean.getData().getTag_Mapping_Version(),
                                            configBean.getData().getAndroid_Is_Force_Update(),
                                            configBean.getData().getAndroid_Force_Update_Reason());
                                }
                            } else {
                                ShowDialog(configBean.getMsg());
                            }
                        } else {
                            ShowDialog("服务器出小差了");
                        }
                    }
                });
            }
        });
    }

    private void ShowDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setTitle("提示")
                .setMessage(text)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        finish();
                    }
                });
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        if (!isDestroyed()) {
            alertDialog.show();
        }
    }

    private void ShowDialogUp(final String url, String version, String text, String time, final String Version, final String flag, final String buck) {
        View view = LayoutInflater.from(StartActivity.this).inflate(R.layout.show_version_layout, null);
        TextView mTextView_Version = view.findViewById(R.id.mTextView_Version);
        TextView mTextView_Text = view.findViewById(R.id.mTextView_Text);
        TextView mButton_T = view.findViewById(R.id.mButton_T);
        TextView mButton_C = view.findViewById(R.id.mButton_C);

        final VersionDialog builder = new VersionDialog(StartActivity.this, 0, 0, view, R.style.DialogTheme);
        builder.setCancelable(false);

        mTextView_Version.setText(version);
        if (null != text) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] str = text.split("\\|");
            for (int i = 0; i < str.length; i++) {
                stringBuilder.append(str[i] + "\n");
            }
            String string = String.valueOf(stringBuilder);
            mTextView_Text.setText(string.replace("\\n", "\n\n"));
        } else {
            mTextView_Text.setText("请升级到最新版本!");
        }

        if (flag.equals("true")) {
            mButton_C.setText("退出");
            mButton_C.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else{
            mButton_C.setText("忽略");
            mButton_C.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                    if (!spUtils.getString("expireTime").isEmpty() && spUtils.getInt("userId", -1) != -1) {
                        getServerTime();
                    } else {
                        mTextView_btn_login.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        mButton_T.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownLoadApk(url);
                builder.dismiss();
            }
        });
        builder.show();
    }

    private void getServerTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    JsonObjectRequest request = new JsonObjectRequest(Http.serverTime, null, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (response.getInt("code") == 1) {
                                            serverTime = response.getJSONObject("data").getLong("serverTime");
                                            if (serverTime < OtherUtils.dateToStamp(spUtils.getString("expireTime")) && spUtils.getInt("userId", -1) != -1) {
                                                if (!isDestroyed()) {
                                                    Content.timePoor = serverTime - System.currentTimeMillis();
                                                    Intent intent = new Intent(mContext, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            } else {
                                                mTextView_btn_login.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            mTextView_btn_login.setVisibility(View.VISIBLE);
                                        }
                                    } catch (Exception e) {
                                        mTextView_btn_login.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTextView_btn_login.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                    mQueue.add(request);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void DownLoadApk(String android_download_url) {
        progressDialog = new ProgressDialog(StartActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载更新包");
        progressDialog.show();
        progressDialog.setCancelable(false);
        DownloadUtil.get().download(android_download_url, Environment.getExternalStorageDirectory().getAbsolutePath() + "/AschWitkey", "aschwk.apk", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                install(StartActivity.this);
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
                        Toast.makeText(StartActivity.this, "下载失败，请稍候重试!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });

    }

    public void install(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AschWitkey/aschwk.apk");
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(context, "com.xw.aschwitkey.provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
        finish();
    }

}
