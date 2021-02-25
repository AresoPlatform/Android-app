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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.adapter.AgentHistoryAdapter;
import com.xw.aschwitkey.adapter.WithdrawalHistoryAdapter;
import com.xw.aschwitkey.entity.AgentHistoryBean;
import com.xw.aschwitkey.entity.WithdrawalHistoryBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
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

public class WithdrawalHistoryActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.mRelativeLayout_noData)
    RelativeLayout mRelativeLayout_noData;
    @BindView(R.id.mImageView_noData)
    ImageView mImageView_noData;
    @BindView(R.id.mTextView_noData)
    TextView mTextView_noData;

    private Activity mContext;
    private int page = 1;
    private Gson gson;
    private SPUtils spUtils;
    private List<WithdrawalHistoryBean.Result> list;
    private WithdrawalHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_history);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContext = WithdrawalHistoryActivity.this;
        //设置沉浸式状态栏并且字体为黑色
        ViewUtils.setImmersionStateMode(WithdrawalHistoryActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(WithdrawalHistoryActivity.this));

        spUtils = new SPUtils(mContext, "AschWallet");
        gson = new Gson();
        list = new ArrayList<>();
        adapter = new WithdrawalHistoryAdapter(mContext, list);
        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        setPullRefresher();
        getHistory();
    }

    @OnClick({R.id.mImageView_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.mImageView_back:
                finish();
                break;
        }
    }

    private void setPullRefresher() {
        refreshLayout.setRefreshHeader(new MyClassicsHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    private void getHistory() {
        OkHttpClient.initGet(Http.trRecord + "?steemName=" + spUtils.getString("nickname") + "&page=1").enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageView_noData.setImageResource(R.mipmap.no_net);
                        mTextView_noData.setText("网络连接失败，请检查网络");
                        mRelativeLayout_noData.setVisibility(View.VISIBLE);
                        ToastUtil.showShort(mContext, "网络错误，获取提现历史记录失败");
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
                                WithdrawalHistoryBean bean = gson.fromJson(result, WithdrawalHistoryBean.class);
                                list.addAll(bean.getData());
                                if (list.isEmpty()) {
                                    mRelativeLayout_noData.setVisibility(View.VISIBLE);
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        OkHttpClient.initGet(Http.trRecord + "?steemName=" + spUtils.getString("nickname") + "&page=1").enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh(1000);
                        ToastUtil.showShort(mContext, "网络错误，获取提现历史记录失败");
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
                            page = 1;
                            refreshLayout.finishRefresh(1000);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                WithdrawalHistoryBean bean = gson.fromJson(result, WithdrawalHistoryBean.class);
                                if (!list.isEmpty()) {
                                    list.clear();
                                }
                                list.addAll(bean.getData());
                                if (list.isEmpty()) {
                                    mImageView_noData.setImageResource(R.mipmap.no_data);
                                    mTextView_noData.setText("暂无提现历史记录");
                                    mRelativeLayout_noData.setVisibility(View.VISIBLE);
                                } else {
                                    mRelativeLayout_noData.setVisibility(View.GONE);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        ++page;
        OkHttpClient.initGet(Http.trRecord + "?steemName=" + spUtils.getString("nickname") + "&page=" + page).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        --page;
                        refreshLayout.finishLoadMore();
                        ToastUtil.showShort(mContext, "网络错误，获取提现历史记录失败");
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
                            refreshLayout.finishLoadMore();
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                WithdrawalHistoryBean bean = gson.fromJson(result, WithdrawalHistoryBean.class);
                                list.addAll(bean.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                --page;
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            --page;
                        }
                    }
                });
            }
        });
    }

}
