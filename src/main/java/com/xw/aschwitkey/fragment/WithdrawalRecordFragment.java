package com.xw.aschwitkey.fragment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lihang.ShadowLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.adapter.BlockRewardHistoryAdapter;
import com.xw.aschwitkey.adapter.DdzWithdrawalHistoryAdapter;
import com.xw.aschwitkey.entity.BlockRewardBean;
import com.xw.aschwitkey.entity.DdzWithdrawalBean;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.SPUtils;
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
import java.util.List;

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

public class WithdrawalRecordFragment extends NewLazyFragment implements OnRefreshListener,OnLoadMoreListener {

    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mRecyclerView_history)
    RecyclerView mRecyclerView_history;
    @BindView(R.id.mRelativeLayout_noData)
    RelativeLayout mRelativeLayout_noData;

    private Activity mContext;
    private Gson gson;
    private DdzWithdrawalHistoryAdapter adapter;
    private RecyclerViewNoBugLinearLayoutManager layoutManager = null;
    private List<DdzWithdrawalBean.Result> list;
    private int offset = 1;
    private MyDialog myDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_ddz_history_layout;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        gson = new Gson();
        adapter = new DdzWithdrawalHistoryAdapter(mContext, list);
        layoutManager = new RecyclerViewNoBugLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView_history.setLayoutManager(layoutManager);
        mRecyclerView_history.setAdapter(adapter);

        setPullRefresher();

        myDialog = DialogUtils.createLoadingDialog(mContext, "获取提现记录...");
        withdrawLists();
    }

    private void setPullRefresher() {
        mRefreshLayout.setRefreshHeader(new MyClassicsHeader(mContext));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
    }

    private void withdrawLists() {
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("limit",10);
            requestJsonObject.put("offset",1);
            requestJsonObject.put("params",new JSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), requestJsonObject.toString());
        OkHttpClient.initPost(Http.withdrawLists, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext,"网络错误，获取提现记录失败");
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
                            if (jsonObject.getInt("code")==1){
                                DdzWithdrawalBean bean = gson.fromJson(jsonObject.getString("data"),DdzWithdrawalBean.class);
                                list.addAll(bean.getData());
                                if (!list.isEmpty()) {
                                    mRecyclerView_history.setVisibility(View.VISIBLE);
                                    mRelativeLayout_noData.setVisibility(View.GONE);
                                } else {
                                    mRecyclerView_history.setVisibility(View.GONE);
                                    mRelativeLayout_noData.setVisibility(View.VISIBLE);
                                }
                                adapter.notifyDataSetChanged();
                            }else{
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "获取提现记录错误："+e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        offset = 1;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("limit",10);
            requestJsonObject.put("offset",1);
            requestJsonObject.put("params",new JSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), requestJsonObject.toString());
        OkHttpClient.initPost(Http.withdrawLists, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh(1000);
                        ToastUtil.showShort(mContext,"网络错误，获取提现记录失败");
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
                            refreshLayout.finishRefresh(1000);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code")==1){
                                DdzWithdrawalBean bean = gson.fromJson(jsonObject.getString("data"),DdzWithdrawalBean.class);
                                list.clear();
                                list.addAll(bean.getData());
                                if (!list.isEmpty()) {
                                    mRecyclerView_history.setVisibility(View.VISIBLE);
                                    mRelativeLayout_noData.setVisibility(View.GONE);
                                } else {
                                    mRecyclerView_history.setVisibility(View.GONE);
                                    mRelativeLayout_noData.setVisibility(View.VISIBLE);
                                }
                                adapter.notifyDataSetChanged();
                            }else{
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "获取提现记录错误："+e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        ++offset;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("limit",10);
            requestJsonObject.put("offset",offset);
            requestJsonObject.put("params",new JSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), requestJsonObject.toString());
        OkHttpClient.initPost(Http.withdrawLists, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        --offset;
                        refreshLayout.finishLoadMore();
                        ToastUtil.showShort(mContext,"网络错误，获取提现记录失败");
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
                            if (jsonObject.getInt("code")==1){
                                DdzWithdrawalBean bean = gson.fromJson(jsonObject.getString("data"),DdzWithdrawalBean.class);
                                list.addAll(bean.getData());
                                adapter.notifyDataSetChanged();
                            }else{
                                --offset;
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            --offset;
                            ToastUtil.showShort(mContext, "获取提现记录错误："+e.getMessage());
                        }
                    }
                });
            }
        });
    }
}
