package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihang.ShadowLayout;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.entity.FundDividendsBean;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FundDividendsAdapter extends RecyclerView.Adapter<FundDividendsAdapter.ViewHolder> {
    private Context context;
    private List<FundDividendsBean.FundDividends> list;
    private LayoutInflater inflater;
    private boolean isCouncil;
    private SPUtils spUtils;

    public FundDividendsAdapter(Context context, List<FundDividendsBean.FundDividends> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        spUtils = new SPUtils(context, "AschWallet");
    }

    public interface OnClickListenerFace {
        void OnClickTemp(int p, View view, int type);
    }

    public OnClickListenerFace OnClickListenerFace;

    public void setOnClickListenerFace(OnClickListenerFace OnClickListenerFace) {
        this.OnClickListenerFace = OnClickListenerFace;
    }

    @NonNull
    @Override
    public FundDividendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_fund_dividends_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FundDividendsAdapter.ViewHolder holder, int position) {
        holder.mTextView_name.setText(list.get(position).getFundName() + "第" + list.get(position).getFundExpect() + "期");
        holder.mTextView_time.setText(list.get(position).getFundStartAccrual());
        holder.mTextView_fundsRate.setText(list.get(position).getFundRate().multiply(new BigDecimal("100")).doubleValue() + "%");
        if (isCouncil) {
            holder.mTextView_withdrawal.setText("提现分红余额");
            holder.mRelativeLayout_subscribeTotal.setVisibility(View.VISIBLE);
            holder.mRelativeLayout_totalDividends.setVisibility(View.GONE);
            holder.mRelativeLayout_remaining.setVisibility(View.VISIBLE);
            holder.mTextView_allMany_text.setText("认购总额");
            holder.mTextView_totalAmount_text.setText("分红地址总额");
            holder.mTextView_allMany.setText(list.get(position).getFundShare() * list.get(position).getFundShareAmount() + "");
            holder.mTextView_allMany_many.setText(list.get(position).getFundShareAmount() + " × " + list.get(position).getFundShare());
            holder.mTextView_totalAmount.setText(list.get(position).getFundGrantAddressTotal().toString() + "");
            holder.mTextView_subscribeTotal.setText(list.get(position).getSellAmount() * list.get(position).getFundShareAmount() + "");
            holder.mTextView_subscribeTotal_total.setText(list.get(position).getSellAmount() + " × " + list.get(position).getFundShareAmount());
            holder.mTextView_totalDividends.setText(list.get(position).getFundGrantAddressTotal().subtract(list.get(position).getShenyu()).toString());
            holder.mTextView_remaining.setText(list.get(position).getShenyu().toString());
            if (list.get(position).getShenyu().doubleValue() != 0d && list.get(position).getPublishAccount() == spUtils.getInt("userId")) {
                holder.mShadowLayout_withdrawal.setVisibility(View.VISIBLE);
            } else {
                holder.mShadowLayout_withdrawal.setVisibility(View.GONE);
            }
            holder.mShadowLayout_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 1);
                }
            });
        } else {
            try {
                if ((OtherUtils.dateToStamp(list.get(position).getFundEndAccrual()) - (System.currentTimeMillis() + Content.timePoor)) <= 0) {
                    holder.mTextView_withdrawal.setText("解冻本金");
                } else {
                    holder.mTextView_withdrawal.setText("领取收益");
                }
            } catch (ParseException e) {
            }
            holder.mRelativeLayout_subscribeTotal.setVisibility(View.GONE);
            holder.mRelativeLayout_totalDividends.setVisibility(View.GONE);
            holder.mRelativeLayout_remaining.setVisibility(View.GONE);
            holder.mTextView_allMany_text.setText("已购基金");
            holder.mTextView_totalAmount_text.setText("分红利息");
            holder.mTextView_allMany.setText(list.get(position).getBuyAmount() * list.get(position).getFundShareAmount() + "");
            holder.mTextView_allMany_many.setText(list.get(position).getFundShareAmount() + " × " + list.get(position).getBuyAmount());
            holder.mTextView_totalAmount.setText(list.get(position).getGetAmount() + "");
            if (list.get(position).getIsGet() == 1) {
                holder.mShadowLayout_withdrawal.setVisibility(View.VISIBLE);
            } else {
                holder.mShadowLayout_withdrawal.setVisibility(View.GONE);
            }
            holder.mShadowLayout_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 0);
                }
            });
        }

        holder.mShadowLayout_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setCouncil(boolean isCouncil) {
        this.isCouncil = isCouncil;
    }

    public void setIsGet(int type, int p) {
        list.get(p).setIsGet(type);
        notifyItemChanged(p);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShadowLayout mShadowLayout_withdrawal, mShadowLayout_top;
        RelativeLayout mRelativeLayout_subscribeTotal, mRelativeLayout_totalDividends, mRelativeLayout_remaining;
        TextView mTextView_withdrawal, mTextView_name, mTextView_time, mTextView_allMany_text, mTextView_allMany, mTextView_allMany_many, mTextView_fundsRate, mTextView_totalAmount_text, mTextView_totalAmount, mTextView_subscribeTotal, mTextView_subscribeTotal_total, mTextView_totalDividends, mTextView_remaining;
        ImageView mImageView_operation;

        public ViewHolder(View itemView) {
            super(itemView);
            mShadowLayout_top = itemView.findViewById(R.id.mShadowLayout_top);
            mShadowLayout_withdrawal = itemView.findViewById(R.id.mShadowLayout_withdrawal);
            mRelativeLayout_subscribeTotal = itemView.findViewById(R.id.mRelativeLayout_subscribeTotal);
            mRelativeLayout_totalDividends = itemView.findViewById(R.id.mRelativeLayout_totalDividends);
            mRelativeLayout_remaining = itemView.findViewById(R.id.mRelativeLayout_remaining);
            mTextView_withdrawal = itemView.findViewById(R.id.mTextView_withdrawal);
            mTextView_name = itemView.findViewById(R.id.mTextView_name);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_allMany_text = itemView.findViewById(R.id.mTextView_allMany_text);
            mTextView_allMany = itemView.findViewById(R.id.mTextView_allMany);
            mTextView_allMany_many = itemView.findViewById(R.id.mTextView_allMany_many);
            mTextView_fundsRate = itemView.findViewById(R.id.mTextView_fundsRate);
            mTextView_totalAmount_text = itemView.findViewById(R.id.mTextView_totalAmount_text);
            mTextView_totalAmount = itemView.findViewById(R.id.mTextView_totalAmount);
            mTextView_subscribeTotal = itemView.findViewById(R.id.mTextView_subscribeTotal);
            mTextView_subscribeTotal_total = itemView.findViewById(R.id.mTextView_subscribeTotal_total);
            mTextView_totalDividends = itemView.findViewById(R.id.mTextView_totalDividends);
            mTextView_remaining = itemView.findViewById(R.id.mTextView_remaining);
            mImageView_operation = itemView.findViewById(R.id.mImageView_operation);
        }
    }
}