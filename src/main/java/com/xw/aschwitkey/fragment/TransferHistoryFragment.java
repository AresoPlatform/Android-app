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
import com.xw.aschwitkey.adapter.HistoryAdapter;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.entity.HistoryBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
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

public class TransferHistoryFragment extends NewLazyFragment implements OnLoadMoreListener ,HistoryAdapter.OnClickListenerFace{

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
    private HistoryAdapter adapter;
    private RecyclerViewNoBugLinearLayoutManager layoutManager = null;
    private List<HistoryBean.History> list;
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
         adapter = new HistoryAdapter(mContext, list);
        layoutManager = new RecyclerViewNoBugLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView_history.setLayoutManager(layoutManager);
        mRecyclerView_history.setAdapter(adapter);
        adapter.setOnClickListenerFace(this);

        setPullRefresher();

        if (!spUtils1.getString("childAddress", "").isEmpty()) {
            getHistory();
        }

    }

    private void setPullRefresher() {
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        mRefreshLayout.setOnLoadMoreListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessageBean messageEvent) {
        switch (messageEvent.getTag()) {
            case 20:
                getHistory();
                break;
        }
    }

    private void getHistory() {
        Map map = new HashMap();
        map.put("address", spUtils1.getString("childAddress"));
        Map postmap = new HashMap();
        postmap.put("limit", 0);
        postmap.put("offset", 1);
        postmap.put("params", map);

        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.getTransfers, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(mContext, "网络错误，转账记录查询失败");
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
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                offset = 1;
                                HistoryBean bean = gson.fromJson(jsonObject.getString("data"), HistoryBean.class);
                                if (!list.isEmpty()) {
                                    list.clear();
                                }
                                list.addAll(bean.getData());
                                if (!list.isEmpty()) {
                                    mShadowLayout_history.setVisibility(View.VISIBLE);
                                    mRelativeLayout_noData.setVisibility(View.GONE);
                                } else {
                                    mShadowLayout_history.setVisibility(View.GONE);
                                    mRelativeLayout_noData.setVisibility(View.VISIBLE);
                                }
                                adapter.notifyDataSetChanged();
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
        if (spUtils1.getString("childAddress", "").isEmpty()) {
            refreshLayout.finishLoadMore();
            ToastUtil.showLong(mContext, "您还没有添加账户，请先到账户管理中导入或创建账户");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 5);
                    Map map = new HashMap();
                    map.put("address", spUtils1.getString("childAddress"));
                    Map postmap = new HashMap();
                    postmap.put("limit", 0);
                    postmap.put("offset", ++offset);
                    postmap.put("params", map);

                    RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
                    OkHttpClient.initPost(Http.getTransfers, requestBodyPost).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            --offset;
                            refreshLayout.finishLoadMore();
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
                                            HistoryBean bean = gson.fromJson(jsonObject.getString("data"), HistoryBean.class);
                                            if (bean.getData().isEmpty()) {
                                                refreshLayout.finishLoadMoreWithNoMoreData();
                                                return;
                                            }
                                            list.addAll(bean.getData());
                                            adapter.notifyDataSetChanged();
                                        } else if (jsonObject.getInt("code") == 3) {
                                            EventBus.getDefault().postSticky(new EventMessageBean(-2, null));
                                        } else {
                                            --offset;
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData;
                if (list.get(p).getInOrout() == 1) {
                    mClipData = ClipData.newPlainText("address", list.get(p).getSenderId());
                } else {
                    mClipData = ClipData.newPlainText("address", list.get(p).getRecipientId());
                }
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort(mContext, "账户地址已复制到剪贴板");
                break;
        }
    }
}
