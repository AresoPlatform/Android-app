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
import okhttp3.Response;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.adapter.BlockRewardHistoryAdapter;
import com.xw.aschwitkey.adapter.PledgeMiningHistoryAdapter;
import com.xw.aschwitkey.entity.BlockRewardBean;
import com.xw.aschwitkey.entity.PledgeMiningBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.utils.ViewUtils;
import com.xw.aschwitkey.view.MyClassicsHeader;
import com.xw.aschwitkey.view.RecyclerViewNoBugLinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApHistoryActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mRelativeLayout_noData)
    RelativeLayout mRelativeLayout_noData;

    private Activity mContext;
    private MyDialog myDialog;
    private SPUtils spUtils, spUtils1;
    private Gson gson;
    private PledgeMiningHistoryAdapter adapter;
    private RecyclerViewNoBugLinearLayoutManager layoutManager = null;
    private List<PledgeMiningBean.History> list;
    private String tronAddress = "";
    private int offset = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_history);
        ButterKnife.bind(this);
        OtherUtils.config(this);
        init();
    }

    private void init() {
        mContext = ApHistoryActivity.this;
        ViewUtils.setImmersionStateMode(ApHistoryActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(mContext));

        spUtils = new SPUtils(mContext, "AschWallet");
        spUtils1 = new SPUtils(mContext, "AschImport");
        list = new ArrayList<>();
        gson = new Gson();

        adapter = new PledgeMiningHistoryAdapter(mContext, list);
        layoutManager = new RecyclerViewNoBugLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        setPullRefresher();

        if (!spUtils1.getString("tronChildAddress", "").isEmpty()) {
            tronAddress = spUtils1.getString("tronChildAddress", "");
        } else {
            tronAddress = spUtils.getString("tronAddress");
        }

        if (!spUtils1.getString("tronChildAddress", "").isEmpty()) {
            tronAddress = spUtils1.getString("tronChildAddress", "");
        } else {
            tronAddress = spUtils.getString("tronAddress");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myDialog = DialogUtils.createLoadingDialog(mContext, "获取质押记录...");
                            pledgeRecords();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick({R.id.mImageView_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mImageView_back:
                finish();
                break;
        }
    }

    private void setPullRefresher() {
        mRefreshLayout.setRefreshHeader(new MyClassicsHeader(mContext));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
    }

    private void pledgeRecords() {
        OkHttpClient.initGet(Http.pledgeRecords + "?tronAddress=" + tronAddress + "&offset=1").enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，获取质押记录失败");
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
                            DialogUtils.closeDialog(myDialog);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                PledgeMiningBean bean = gson.fromJson(result, PledgeMiningBean.class);
                                list.clear();
                                list.addAll(bean.getData());
                                adapter.notifyDataSetChanged();
                                if (list.isEmpty()) {
                                    mRelativeLayout_noData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "获取质押记录错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        offset = 1;
        OkHttpClient.initGet(Http.pledgeRecords + "?tronAddress=" + tronAddress + "&offset=1").enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefresh(1000);
                        ToastUtil.showShort(mContext, "网络错误，获取质押记录失败");
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
                            mRefreshLayout.finishRefresh(1000);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                PledgeMiningBean bean = gson.fromJson(result, PledgeMiningBean.class);
                                list.clear();
                                list.addAll(bean.getData());
                                adapter.notifyDataSetChanged();
                                if (list.isEmpty()) {
                                    mRelativeLayout_noData.setVisibility(View.VISIBLE);
                                } else {
                                    mRelativeLayout_noData.setVisibility(View.GONE);
                                }
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "获取质押记录错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        ++offset;
        OkHttpClient.initGet(Http.pledgeRecords + "?tronAddress=" + tronAddress + "&offset=" + offset).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadMore();
                        --offset;
                        ToastUtil.showShort(mContext, "网络错误，获取质押记录失败");
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
                            refreshLayout.finishLoadMore();
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                PledgeMiningBean bean = gson.fromJson(result, PledgeMiningBean.class);
                                list.addAll(bean.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                --offset;
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            --offset;
                            ToastUtil.showShort(mContext, "获取质押记录错误：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

}
