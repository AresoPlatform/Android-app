package com.xw.aschwitkey.fragment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.lihang.ShadowLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.adapter.RewardsHistoryAdapter;
import com.xw.aschwitkey.adapter.RewardsUserAdapter;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.entity.RewardsHistoryBean;
import com.xw.aschwitkey.entity.RewardsUserBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.view.MyClassicsHeader;
import com.xw.aschwitkey.view.RecyclerViewNoBugLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RewardsHistoryFragment extends NewLazyFragment implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.mShadowLayout_history)
    ShadowLayout mShadowLayout_history;
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mRecyclerView_rewardsHistory)
    RecyclerView mRecyclerView_rewardsHistory;

    private Activity mContext;
    private Gson gson;
    private RewardsHistoryAdapter adapter;
    private RewardsUserAdapter adapter1;
    private RecyclerViewNoBugLinearLayoutManager layoutManager = null;
    private RecyclerViewNoBugLinearLayoutManager layoutManager1 = null;
    private List<RewardsHistoryBean.History> list;
    private List<RewardsUserBean> list1;
    private int offset = 1;
    private int fundId = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_rewards_history_layout;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        gson = new Gson();
        adapter = new RewardsHistoryAdapter(mContext, list);
        layoutManager = new RecyclerViewNoBugLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView_rewardsHistory.setLayoutManager(layoutManager);
        mRecyclerView_rewardsHistory.setAdapter(adapter);

        setPullRefresher();
        invTRecord();
    }

    private void setPullRefresher() {
        mRefreshLayout.setRefreshHeader(new MyClassicsHeader(mContext));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessageBean messageEvent) {
        switch (messageEvent.getTag()) {
            case 7:
                fundId = (int) messageEvent.getMesssage().get("fundId");
                invTRecord();
                break;
        }
    }

    private void invTRecord() {
        Map map = new HashMap();
        if (fundId != 0) {
            map.put("fundId", fundId);
        }
        Map postmap = new HashMap();
        postmap.put("limit", 0);
        postmap.put("offset", 1);
        postmap.put("params", map);
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.invTRecord, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(mContext, "网络错误，查询奖励记录失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                RewardsHistoryBean bean = gson.fromJson(jsonObject.getString("data"), RewardsHistoryBean.class);
                                if (bean.getData().isEmpty()) {
                                    mShadowLayout_history.setVisibility(View.GONE);
                                } else {
                                    mShadowLayout_history.setVisibility(View.VISIBLE);
                                    if (!list.isEmpty()) {
                                        list.clear();
                                    }
                                    list.addAll(bean.getData());
                                    adapter.notifyDataSetChanged();
                                }
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        Map map = new HashMap();
        if (fundId != 0) {
            map.put("fundId", fundId);
        }
        Map postmap = new HashMap();
        postmap.put("limit", 0);
        postmap.put("offset", 1);
        postmap.put("params", map);
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.invTRecord, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                refreshLayout.finishRefresh(1000);
                ToastUtil.showShort(mContext, "网络错误，查询邀请记录失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                refreshLayout.finishRefresh(1000);
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                offset = 1;
                                RewardsHistoryBean bean = gson.fromJson(jsonObject.getString("data"), RewardsHistoryBean.class);
                                if (bean.getData().isEmpty()) {
                                    mShadowLayout_history.setVisibility(View.GONE);
                                } else {
                                    mShadowLayout_history.setVisibility(View.VISIBLE);
                                    list.clear();
                                    list.addAll(bean.getData());
                                    adapter.notifyDataSetChanged();
                                }
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
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        Map map = new HashMap();
        if (fundId != 0) {
            map.put("fundId", fundId);
        }
        Map postmap = new HashMap();
        postmap.put("limit", 0);
        postmap.put("offset", ++offset);
        postmap.put("params", map);
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.invTRecord, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                refreshLayout.finishLoadMore();
                --offset;
                ToastUtil.showShort(mContext, "网络错误，查询邀请记录失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                refreshLayout.finishLoadMore();
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                RewardsHistoryBean bean = gson.fromJson(jsonObject.getString("data"), RewardsHistoryBean.class);
                                if (bean.getData().isEmpty()) {
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                    return;
                                }
                                list.addAll(bean.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            --offset;
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
