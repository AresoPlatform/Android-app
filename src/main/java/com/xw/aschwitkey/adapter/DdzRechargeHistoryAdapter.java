package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.DdzRechargeBean;
import com.xw.aschwitkey.entity.DdzWithdrawalBean;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DdzRechargeHistoryAdapter extends RecyclerView.Adapter<DdzRechargeHistoryAdapter.ViewHolder> {
    private Context context;
    private List<DdzRechargeBean.Result> list;
    private LayoutInflater inflater;

    public DdzRechargeHistoryAdapter(Context context, List<DdzRechargeBean.Result> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public interface OnClickListenerFace {
        void OnClickTemp(int p, View view);
    }

    public OnClickListenerFace OnClickListenerFace;

    public void setOnClickListenerFace(OnClickListenerFace OnClickListenerFace) {
        this.OnClickListenerFace = OnClickListenerFace;
    }

    @NonNull
    @Override
    public DdzRechargeHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_ddz_recharge_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DdzRechargeHistoryAdapter.ViewHolder holder, int position) {
        holder.mTextView_payTron.setText(list.get(position).getPayTron().setScale(6, BigDecimal.ROUND_DOWN).toString()+" ASO");
        holder.mTextView_createTime.setText(list.get(position).getCreateTime());
        if (list.get(position).getIsPay() == 1) {
            holder.mRelativeLayout_getGold.setVisibility(View.VISIBLE);
            holder.mRelativeLayout_updateTime.setVisibility(View.VISIBLE);
            holder.mTextView_isPay.setText("充值成功");
            holder.mTextView_isPay.setTextColor(context.getResources().getColor(R.color.text_color_green));
            holder.mTextView_getGold.setText(list.get(position).getGetGold().setScale(3, BigDecimal.ROUND_DOWN).toString());
            holder.mTextView_updateTime.setText(list.get(position).getUpdateTime());
        } else {
            holder.mRelativeLayout_getGold.setVisibility(View.GONE);
            holder.mRelativeLayout_updateTime.setVisibility(View.GONE);
            holder.mTextView_isPay.setText("充值中");
            holder.mTextView_isPay.setTextColor(context.getResources().getColor(R.color.text_color_red));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_payTron, mTextView_createTime, mTextView_getGold, mTextView_updateTime, mTextView_isPay;
        RelativeLayout mRelativeLayout_getGold, mRelativeLayout_updateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_payTron = itemView.findViewById(R.id.mTextView_payTron);
            mTextView_createTime = itemView.findViewById(R.id.mTextView_createTime);
            mTextView_getGold = itemView.findViewById(R.id.mTextView_getGold);
            mTextView_updateTime = itemView.findViewById(R.id.mTextView_updateTime);
            mTextView_isPay = itemView.findViewById(R.id.mTextView_isPay);
            mRelativeLayout_getGold = itemView.findViewById(R.id.mRelativeLayout_getGold);
            mRelativeLayout_updateTime = itemView.findViewById(R.id.mRelativeLayout_updateTime);
        }
    }
}