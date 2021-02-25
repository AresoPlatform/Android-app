package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.entity.DdzWithdrawalBean;
import com.xw.aschwitkey.entity.PledgeMiningBean;
import com.xw.aschwitkey.utils.OtherUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DdzWithdrawalHistoryAdapter extends RecyclerView.Adapter<DdzWithdrawalHistoryAdapter.ViewHolder> {
    private Context context;
    private List<DdzWithdrawalBean.Result> list;
    private LayoutInflater inflater;

    public DdzWithdrawalHistoryAdapter(Context context, List<DdzWithdrawalBean.Result> list) {
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
    public DdzWithdrawalHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_ddz_withdrawal_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DdzWithdrawalHistoryAdapter.ViewHolder holder, int position) {
        holder.mTextView_withdrawGoldAmount.setText(list.get(position).getWithdrawGoldAmount().setScale(3, BigDecimal.ROUND_DOWN).toString());
        holder.mTextView_createTime.setText(list.get(position).getCreateTime());
        holder.mTextView_serviceAmount.setText(list.get(position).getServiceAmount().setScale(6, BigDecimal.ROUND_DOWN).toString() + " ASO");
        if (list.get(position).getIsOk() == 1) {
            holder.mRelativeLayout_withdrawAsoAmount.setVisibility(View.GONE);
            holder.mRelativeLayout_updateTime.setVisibility(View.GONE);
            holder.mTextView_isOk.setText("审核中");
            holder.mTextView_isOk.setTextColor(context.getResources().getColor(R.color.text_color_red));
        } else {
            holder.mRelativeLayout_withdrawAsoAmount.setVisibility(View.VISIBLE);
            holder.mRelativeLayout_updateTime.setVisibility(View.VISIBLE);
            holder.mTextView_isOk.setText("提现成功");
            holder.mTextView_isOk.setTextColor(context.getResources().getColor(R.color.text_color_green));
            holder.mTextView_withdrawAsoAmount.setText(list.get(position).getWithdrawAsoAmount().setScale(6, BigDecimal.ROUND_DOWN).toString() + " ASO");
            holder.mTextView_updateTime.setText(list.get(position).getUpdateTime());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_withdrawGoldAmount, mTextView_createTime, mTextView_serviceAmount, mTextView_withdrawAsoAmount, mTextView_updateTime, mTextView_isOk;
        RelativeLayout mRelativeLayout_withdrawAsoAmount, mRelativeLayout_updateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_withdrawGoldAmount = itemView.findViewById(R.id.mTextView_withdrawGoldAmount);
            mTextView_createTime = itemView.findViewById(R.id.mTextView_createTime);
            mTextView_serviceAmount = itemView.findViewById(R.id.mTextView_serviceAmount);
            mTextView_withdrawAsoAmount = itemView.findViewById(R.id.mTextView_withdrawAsoAmount);
            mTextView_updateTime = itemView.findViewById(R.id.mTextView_updateTime);
            mTextView_isOk = itemView.findViewById(R.id.mTextView_isOk);
            mRelativeLayout_withdrawAsoAmount = itemView.findViewById(R.id.mRelativeLayout_withdrawAsoAmount);
            mRelativeLayout_updateTime = itemView.findViewById(R.id.mRelativeLayout_updateTime);
        }
    }
}