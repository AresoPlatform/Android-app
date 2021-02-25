package com.xw.aschwitkey.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maning.pswedittextlibrary.MNPasswordEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xw.aschwitkey.FloorCountDownLib.Center;
import com.xw.aschwitkey.FloorCountDownLib.ICountDownCenter;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.AnDetailsActivity;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.activity.SubscribeActivity;
import com.xw.aschwitkey.adapter.FundAdapter;
import com.xw.aschwitkey.adapter.ImageNetAdapter;
import com.xw.aschwitkey.entity.AnAuditBean;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.entity.FundBean;
import com.xw.aschwitkey.entity.ImageBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.AESUtil;
import com.xw.aschwitkey.utils.DialogUtils;
import com.xw.aschwitkey.utils.MyDialog;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.view.MyClassicsHeader;
import com.xw.aschwitkey.view.RecyclerViewNoBugLinearLayoutManager;
import com.youth.banner.Banner;
import com.youth.banner.indicator.RoundLinesIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.util.BannerUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends NewLazyFragment implements OnRefreshListener, OnLoadMoreListener, FundAdapter.OnClickListenerFace {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mRecyclerView_fund)
    RecyclerView mRecyclerView_fund;
    @BindView(R.id.mRelativeLayout_noData)
    RelativeLayout mRelativeLayout_noData;
    @BindView(R.id.mTextView_totalSupply)
    TextView mTextView_totalSupply;
    @BindView(R.id.mTextView_asoPrice)
    TextView mTextView_asoPrice;
    @BindView(R.id.mTextView_getTotalPendingAso)
    TextView mTextView_getTotalPendingAso;
    @BindView(R.id.mTextView_totalPledge_lp)
    TextView mTextView_totalPledge_lp;
    @BindView(R.id.mTextView_startRewardBlock)
    TextView mTextView_startRewardBlock;
    @BindView(R.id.mTextView_getRewardsPerBlock)
    TextView mTextView_getRewardsPerBlock;
    @BindView(R.id.mTextView_getRewardsPerBlock_lp)
    TextView mTextView_getRewardsPerBlock_lp;
    @BindView(R.id.mTextView_totalPledge)
    TextView mTextView_totalPledge;
    @BindView(R.id.mTextView_apProportion)
    TextView mTextView_apProportion;
    @BindView(R.id.mTextView_lpProportion)
    TextView mTextView_lpProportion;

    private FundAdapter adapter;
    private RecyclerViewNoBugLinearLayoutManager layoutManager = null;
    private List<FundBean.Fund> list;
    private List<AnAuditBean.Notice> notices;
    private Activity mContext;
    private SPUtils spUtils,spUtils1;
    private Gson gson;
    private int offset = 1;
    private Dialog dialog;
    private Dialog dialog1;
    private boolean flag = true;
    private Double money = 0d;
    private int share;
    private MyDialog myDialog;
    private MNPasswordEditText mEditText_password;
    private TextView mTextView_balance;
    private ICountDownCenter countDownCenter;
    private boolean isClick = false, isPause = false,isFirst = true;
    private Timer timer;
    private TimerTask task;

    @Override
    public void onResume() {
        super.onResume();
        if(isFirst) {
            myDialog = DialogUtils.createLoadingDialog(mContext, "矿池信息加载中...");
            getDefiInfo();
        }else{
            if (spUtils1.getBoolean("isPageRefresh", false)) {
                myDialog = DialogUtils.createLoadingDialog(mContext, "矿池信息加载中...");
                getDefiInfo();
            }
        }
        isFirst = false;
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        while (isPause) {
                            Thread.sleep(3000);
                        }
                        getDefiInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        timer.schedule(task, 3000, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home_layout;
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
        notices = new ArrayList<>();
        spUtils = new SPUtils(mContext, "AschWallet");
        spUtils1 = new SPUtils(mContext,"AschImport");
        gson = new Gson();
        countDownCenter = new Center(1000 * 10, true);
        adapter = new FundAdapter(mContext, list, countDownCenter);
        layoutManager = new RecyclerViewNoBugLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView_fund.setLayoutManager(layoutManager);
        mRecyclerView_fund.setAdapter(adapter);
        mRecyclerView_fund.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();
        adapter.setOnClickListenerFace(this);

        countDownCenter.bindRecyclerView(mRecyclerView_fund);
        setPullRefresher();

        getNotice();
    }

    @OnClick({R.id.mTextView_go_defi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTextView_go_defi:
                EventBus.getDefault().postSticky(new EventMessageBean(25, null));
                break;
        }
    }


    private void getNotice() {
        Map map = new HashMap();
        map.put("code", 1);
        Map postmap = new HashMap();
        postmap.put("limit", 0);
        postmap.put("offset", 1);
        postmap.put("params", map);
        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.getNotice, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(mContext, "网络异常，公告获取失败");
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
                                AnAuditBean bean = gson.fromJson(jsonObject.getString("data"), AnAuditBean.class);
                                if (bean.getData().isEmpty()) {
                                    banner.setVisibility(View.GONE);
                                } else {
                                    banner.setVisibility(View.VISIBLE);
                                    if (!notices.isEmpty()) {
                                        notices.clear();
                                    }
                                    notices.addAll(bean.getData());

                                    ImageBean imageBean;
                                    List<ImageBean> imageBeans = new ArrayList<>();
                                    for (int i = 0; i < notices.size(); i++) {
                                        imageBean = new ImageBean();
                                        imageBean.setImg(notices.get(i).getCoverImg());
                                        imageBeans.add(imageBean);
                                    }
                                    banner.setAdapter(new ImageNetAdapter(imageBeans, true));
                                    banner.setIndicator(new RoundLinesIndicator(mContext));
                                    banner.setBannerRound(BannerUtils.dp2px(12));
                                    banner.setDelayTime(5000);
                                    banner.setIndicatorSelectedWidth((int) BannerUtils.dp2px(30));
                                    banner.setIndicatorSelectedColorRes(R.color.white);
                                    banner.setOnBannerListener(new OnBannerListener() {
                                        @Override
                                        public void OnBannerClick(Object data, int position) {
                                            if (!notices.isEmpty() && position < notices.size()) {
                                                Intent intent = new Intent(mContext, AnDetailsActivity.class);
                                                intent.putExtra("title", notices.get(position).getTitle());
                                                intent.putExtra("img", notices.get(position).getCoverImg());
                                                intent.putExtra("content", notices.get(position).getContent());
                                                intent.putExtra("time", notices.get(position).getReviewEndTime());
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                    banner.start();
                                }
                            } else if (jsonObject.getInt("code") == 3) {
                                EventBus.getDefault().postSticky(new EventMessageBean(-2, null));
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
    }

    private void getDefiInfo() {
        isPause = true;
        OkHttpClient.initGet(Http.getDefiInfo).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isPause = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DialogUtils.closeDialog(myDialog);
                                    ToastUtil.showShort(mContext, "网络错误，获取矿池信息失败");
                                }
                            });
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        isPause = false;
                                        DialogUtils.closeDialog(myDialog);
                                        JSONObject jsonObject = new JSONObject(result);
                                        if (jsonObject.getInt("code") == 1) {
                                            Content.apStartTime = jsonObject.getJSONObject("data").getString("apStartTime");
                                            mTextView_totalSupply.setText(new BigDecimal(jsonObject.getJSONObject("data").getString("totalSupply")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                            mTextView_asoPrice.setText(new BigDecimal(jsonObject.getJSONObject("data").getString("asoPrice")).setScale(4, BigDecimal.ROUND_DOWN).toString() + " TRX");
                                            mTextView_getTotalPendingAso.setText(new BigDecimal(jsonObject.getJSONObject("data").getString("getTotalPendingAso")).add(new BigDecimal(jsonObject.getJSONObject("data").getString("lpTotalPendingAso"))).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                            mTextView_totalPledge_lp.setText(new BigDecimal(jsonObject.getJSONObject("data").getString("lpTotalPledge")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                            mTextView_startRewardBlock.setText(new BigDecimal(jsonObject.getJSONObject("data").getString("startRewardBlock")).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                            mTextView_getRewardsPerBlock.setText(new BigDecimal(jsonObject.getJSONObject("data").getString("getRewardsPerBlock")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                            mTextView_getRewardsPerBlock_lp.setText(new BigDecimal(jsonObject.getJSONObject("data").getString("lpRewardsPerBlock")).setScale(3, BigDecimal.ROUND_DOWN).toString());
                                            mTextView_totalPledge.setText(new BigDecimal(jsonObject.getJSONObject("data").getString("totalPledge")).setScale(0, BigDecimal.ROUND_DOWN).toString());
                                            mTextView_apProportion.setText(jsonObject.getJSONObject("data").getString("apProportion"));
                                            mTextView_lpProportion.setText(jsonObject.getJSONObject("data").getString("lpProportion"));
                                        } else {
                                            ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void getFund() {
        Map map = new HashMap();
        map.put("code", 1);
        Map postmap = new HashMap();
        postmap.put("limit", 0);
        postmap.put("offset", 1);
        postmap.put("params", map);

        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.getFund, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(mContext, "网络异常，基金信息获取失败");
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
                                FundBean bean = gson.fromJson(jsonObject.getString("data"), FundBean.class);
                                if (bean.getData().isEmpty()) {
                                    mRelativeLayout_noData.setVisibility(View.GONE);
                                } else {
                                    mRelativeLayout_noData.setVisibility(View.GONE);
                                    if (!list.isEmpty()) {
                                        list.clear();
                                    }
                                    list.addAll(bean.getData());
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
    }

    private void setPullRefresher() {
        mRefreshLayout.setRefreshHeader(new MyClassicsHeader(mContext));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getNotice();
        Map map = new HashMap();
        map.put("code", 1);
        Map postmap = new HashMap();
        postmap.put("limit", 0);
        postmap.put("offset", 1);
        postmap.put("params", map);

        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.getFund, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh(1000);
                        ToastUtil.showShort(mContext, "网络异常，基金信息获取失败");
                    }
                });
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
                                FundBean bean = gson.fromJson(jsonObject.getString("data"), FundBean.class);
                                if (!bean.getData().isEmpty()) {
                                    mRelativeLayout_noData.setVisibility(View.GONE);
                                } else {
                                    mRelativeLayout_noData.setVisibility(View.GONE);
                                }
                                offset = 1;
                                list.clear();
                                list.addAll(bean.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        Map map = new HashMap();
        map.put("code", 1);
        Map postmap = new HashMap();
        postmap.put("limit", 0);
        postmap.put("offset", ++offset);
        postmap.put("params", map);

        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.getFund, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        --offset;
                        refreshLayout.finishLoadMore();
                        ToastUtil.showShort(mContext, "网络异常，基金信息获取失败");
                    }
                });
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
                                FundBean bean = gson.fromJson(jsonObject.getString("data"), FundBean.class);
                                if (bean.getData().isEmpty()) {
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                    return;
                                }
                                list.addAll(bean.getData());
                                adapter.notifyDataSetChanged();
                            } else if (jsonObject.getInt("code") == 3) {
                                EventBus.getDefault().postSticky(new EventMessageBean(-2, null));
                            } else {
                                ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                            }
                        } catch (Exception e) {
                            --offset;
                        }
                    }
                });
            }
        });
    }

    @Override
    public void OnClickTemp(int p, View view) {
        switch (view.getId()) {
            case R.id.mTextView_btn:
                if (isClick) {
                    return;
                } else {
                    isClick = true;
                    if (!spUtils.getBoolean("isDeal", false)) {
                        ToastUtil.showLong(mContext, "请先到账户中心设置交易密码");
                        isClick = false;
                        return;
                    }
                    showDialog(p);
                }
                break;
            case R.id.mShadowLayout_top:
                Intent intent = new Intent(mContext, SubscribeActivity.class);
                intent.putExtra("fundId", list.get(p).getId());
                intent.putExtra("fundName", list.get(p).getFundName());
                intent.putExtra("fundExpect", list.get(p).getFundExpect());
                intent.putExtra("totalAmount", list.get(p).getFundShareAmount() * list.get(p).getFundShare());
                intent.putExtra("fundShare", list.get(p).getFundShare());
                intent.putExtra("sellAmount", list.get(p).getSellAmount());
                intent.putExtra("fundShareAmount", list.get(p).getFundShareAmount());
                intent.putExtra("fundEndTime", list.get(p).getFundEndTime());
                intent.putExtra("fundStartAccrual", list.get(p).getFundStartAccrual());
                intent.putExtra("fundEndAccrual", list.get(p).getFundEndAccrual());
                intent.putExtra("fundRate", list.get(p).getFundRate());
                try {
                    if (list.get(p).getFundShare() - list.get(p).getSellAmount() == 0 || OtherUtils.dateToStamp(list.get(p).getEndTime()) - (System.currentTimeMillis() + Content.timePoor) <= 0) {
                        intent.putExtra("type", 3);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessageBean messageEvent) {
        switch (messageEvent.getTag()) {
            case 5:
                getFund();
                break;
            case 8:
                getNotice();
                break;
        }
    }

    private void showDialog(final int p) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_subscription_fund_layout, null);
        TextView mTextView_fundName = v.findViewById(R.id.mTextView_fundName);
        EditText mEditText_share = v.findViewById(R.id.mEditText_share);
        TextView mTextView_number = v.findViewById(R.id.mTextView_number);
        TextView mTextView_total = v.findViewById(R.id.mTextView_total);
        mTextView_balance = v.findViewById(R.id.mTextView_balance);

        getRecharge();

        mTextView_fundName.setText(list.get(p).getFundName() + "第" + list.get(p).getFundExpect() + "期");
        mEditText_share.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
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

                    int se = list.get(p).getFundShare() - list.get(p).getSellAmount();
                    if (result.compareTo(new BigDecimal(se)) == 1) {
                        temp = se + "";
                        mTextView_number.setText(list.get(p).getFundShareAmount() * se + "");
                        mTextView_total.setText((list.get(p).getFundRate() * (list.get(p).getFundShareAmount() * se) + (list.get(p).getFundShareAmount() * se)) + "");
                        ToastUtil.showShort(mContext, "最多可以认购" + se + "份");
                        money = Double.parseDouble((list.get(p).getFundShareAmount() * se) + "");
                        share = se;
                    } else {
                        mTextView_number.setText(list.get(p).getFundShareAmount() * Integer.parseInt(mEditText_share.getText().toString()) + "");
                        mTextView_total.setText((list.get(p).getFundRate() * (list.get(p).getFundShareAmount() * Integer.parseInt(mEditText_share.getText().toString())) + (list.get(p).getFundShareAmount() * Integer.parseInt(mEditText_share.getText().toString()))) + "");
                        money = Double.parseDouble(list.get(p).getFundShareAmount() * Integer.parseInt(mEditText_share.getText().toString()) + "");
                        share = Integer.parseInt(mEditText_share.getText().toString());
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

        v.findViewById(R.id.mTextView_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.mLinearLayout_btn_subscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = false;
                if (mEditText_share.getText().toString().isEmpty() || mEditText_share.getText().toString().equals("0")) {
                    Toast.makeText(mContext, "购买份额不能为空或0", Toast.LENGTH_SHORT).show();
                    return;
                }
                payDialog(p);
            }
        });
        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog.setContentView(v);
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

    private void payDialog(int p) {
        View v1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_trade_password_layout, null);
        TextView mTextView_currency = v1.findViewById(R.id.mTextView_currency);
        mEditText_password = v1.findViewById(R.id.mEditText_password);
        mTextView_currency.setText(new DecimalFormat("0.000").format(money) + " xas");
        mEditText_password.setOnTextChangeListener(new MNPasswordEditText.OnTextChangeListener() {
            @Override
            public void onTextChange(String s, boolean b) {
                if (b) {
                    String password = mEditText_password.getText().toString();
                    try {
                        subscriptionFund(share, list.get(p).getId(), AESUtil.encrypt(spUtils.getString("phone"), password));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        v1.findViewById(R.id.mImageView_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1 = new Dialog(mContext, R.style.LeftDialogStyle);
        dialog1.setContentView(v1);
        dialog1.setCancelable(false);
        Window dialogWindow1 = dialog1.getWindow();
        if (dialogWindow1 == null) {
            return;
        }
        dialogWindow1.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
        lp1.dimAmount = 0.1f;
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow1.setAttributes(lp1);
        dialog1.show();
    }

    private void getRecharge() {
        OkHttpClient.initGet(Http.recharge).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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
                                JSONObject object = jsonObject.getJSONObject("data");
                                mTextView_balance.setText(new BigDecimal(object.getString("balance")).subtract(new BigDecimal(object.getString("blockedBalances"))).subtract(new BigDecimal(object.getString("bbsBlockedBalances"))).setScale(3, BigDecimal.ROUND_DOWN).toString() + " xas");
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

    private void subscriptionFund(int amount, int fundId, String realPwd) {
        myDialog = DialogUtils.createLoadingDialog(mContext, "认购中...");
        Map postmap = new HashMap();
        postmap.put("amount", amount);
        postmap.put("fundId", fundId);
        postmap.put("realPwd", realPwd);

        RequestBody requestBodyPost = FormBody.create(MediaType.parse("application/json"), gson.toJson(postmap));
        OkHttpClient.initPost(Http.buyFund, requestBodyPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEditText_password.setText("");
                        DialogUtils.closeDialog(myDialog);
                        ToastUtil.showShort(mContext, "网络错误，认购基金失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEditText_password.setText("");
                        DialogUtils.closeDialog(myDialog);
                        try {
                            JSONObject jsonObject = new JSONObject(result);

                            if (jsonObject.getInt("code") == 1) {
                                getFund();
                                dialog1.dismiss();
                                dialog.dismiss();
                                ToastUtil.showShort(mContext, "认购成功");
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
    public void onDestroy() {
        super.onDestroy();
        if (countDownCenter != null) {
            countDownCenter.deleteObservers();
        }
    }

}
