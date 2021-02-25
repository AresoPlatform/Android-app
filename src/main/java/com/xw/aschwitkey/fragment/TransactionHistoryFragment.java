package com.xw.aschwitkey.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.adapter.TransactionHistoryAdapter;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.entity.TransactionBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
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
import okhttp3.Response;

public class TransactionHistoryFragment extends NewLazyFragment implements OnLoadMoreListener, TransactionHistoryAdapter.OnClickListenerFace {

    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mRecyclerView_history)
    RecyclerView mRecyclerView_history;
    @BindView(R.id.mShadowLayout_history)
    ShadowLayout mShadowLayout_history;
    @BindView(R.id.mRelativeLayout_noData)
    RelativeLayout mRelativeLayout_noData;

    private Activity mContext;
    private Gson gson;
    private SPUtils spUtils1;
    private TransactionHistoryAdapter adapter;
    private RecyclerViewNoBugLinearLayoutManager layoutManager = null;
    private List<TransactionBean.History> list;
    private MyDialog myDialog;
    private int offset = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_history_layout;
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
        spUtils1 = new SPUtils(mContext, "AschImport");
        adapter = new TransactionHistoryAdapter(mContext, list);
        layoutManager = new RecyclerViewNoBugLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView_history.setLayoutManager(layoutManager);
        mRecyclerView_history.setAdapter(adapter);
        adapter.setOnClickListenerFace(this);
        setPullRefresher();


        myDialog = DialogUtils.createLoadingDialog(mContext, "获取链上锁仓记录...");
        getTransactionHistory();
    }

    private void setPullRefresher() {
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        mRefreshLayout.setOnLoadMoreListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessageBean messageEvent) {
        switch (messageEvent.getTag()) {
            case 21:
            case 23:
                getTransactionHistory();
                break;
        }
    }

    private void getTransactionHistory() {
        OkHttpClient.initGet(Http.getTransactions + "?limit=10&offset=0&senderId=" + spUtils1.getString("childAddress") + "&type=4").enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，查询锁仓记录失败");
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
                            if (!jsonObject.isNull("success") && jsonObject.getBoolean("success")) {
                                offset = 1;
                                TransactionBean bean = gson.fromJson(result, TransactionBean.class);
                                list.clear();
                                list.addAll(bean.getTransactions());
                                if (!list.isEmpty()) {
                                    mShadowLayout_history.setVisibility(View.VISIBLE);
                                    mRelativeLayout_noData.setVisibility(View.GONE);
                                } else {
                                    mShadowLayout_history.setVisibility(View.GONE);
                                    mRelativeLayout_noData.setVisibility(View.VISIBLE);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            ToastUtil.showShort(mContext, "查询锁仓记录错误：" + e.getMessage());
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        offset = offset * 10;
        OkHttpClient.initGet(Http.getTransactions + "?limit=10&offset=" + offset + "&senderId=" + spUtils1.getString("childAddress") + "&type=4").enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        offset = offset / 10;
                        refreshLayout.finishLoadMore();
                        ToastUtil.showShort(mContext, "网络错误，查询锁仓记录失败");
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
                            if (!jsonObject.isNull("success") && jsonObject.getBoolean("success")) {
                                TransactionBean bean = gson.fromJson(result, TransactionBean.class);
                                list.addAll(bean.getTransactions());
                                adapter.notifyDataSetChanged();
                            } else {
                                offset = offset / 10;
                                ToastUtil.showShort(mContext, "查询锁仓记录失败，请稍候再试");
                            }
                        } catch (JSONException e) {
                            offset = offset / 10;
                            ToastUtil.showShort(mContext, "查询锁仓记录错误：" + e.getMessage());
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void OnClickTemp(int p, View view) {
        switch (view.getId()) {
            case R.id.mImageView_copy:
            case R.id.mTextView_address:
                if (list.get(p).getType() == 1) {
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData;
                    mClipData = ClipData.newPlainText("address", list.get(p).getArgs().get(1));
                    cm.setPrimaryClip(mClipData);
                    ToastUtil.showShort(mContext, "账户地址已复制到剪贴板");
                }
                break;
        }
    }
}
