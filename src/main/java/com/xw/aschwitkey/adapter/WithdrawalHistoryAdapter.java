package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.AgentHistoryBean;
import com.xw.aschwitkey.entity.WithdrawalHistoryBean;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WithdrawalHistoryAdapter extends RecyclerView.Adapter<WithdrawalHistoryAdapter.ViewHolder> {
    private Context context;
    private List<WithdrawalHistoryBean.Result> list;
    private LayoutInflater inflater;

    public WithdrawalHistoryAdapter(Context context, List<WithdrawalHistoryBean.Result> list) {
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
    public WithdrawalHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_withdrawal_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawalHistoryAdapter.ViewHolder holder, int position) {
        holder.mTextView_number.setText(list.get(position).getWithdrawAmount().setScale(3, BigDecimal.ROUND_DOWN).toString() + " xas");
        holder.mTextView_time.setText(list.get(position).getTime().substring(0, list.get(position).getTime().length() - 2));
        if (list.get(position).getWithdrawStatus() == 1) {
            holder.mRelativeLayout_endNumber.setVisibility(View.VISIBLE);
            holder.mRelativeLayout_endTime.setVisibility(View.VISIBLE);
            holder.mTextView_type.setText("提现成功");
            holder.mTextView_type.setTextColor(context.getResources().getColor(R.color.text_color_green));
            holder.mTextView_endNumber.setText(list.get(position).getPracticalAmount().setScale(3, BigDecimal.ROUND_DOWN).toString() + " xas");
            holder.mTextView_endTime.setText(list.get(position).getAccountingDate().substring(0, list.get(position).getAccountingDate().length() - 2));
        } else {
            holder.mRelativeLayout_endNumber.setVisibility(View.GONE);
            holder.mRelativeLayout_endTime.setVisibility(View.GONE);
            holder.mTextView_type.setText("提现中");
            holder.mTextView_type.setTextColor(context.getResources().getColor(R.color.text_color_red));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_number, mTextView_time, mTextView_endNumber, mTextView_endTime, mTextView_type;
        RelativeLayout mRelativeLayout_endNumber, mRelativeLayout_endTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_number = itemView.findViewById(R.id.mTextView_number);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_endNumber = itemView.findViewById(R.id.mTextView_endNumber);
            mTextView_endTime = itemView.findViewById(R.id.mTextView_endTime);
            mRelativeLayout_endNumber = itemView.findViewById(R.id.mRelativeLayout_endNumber);
            mRelativeLayout_endTime = itemView.findViewById(R.id.mRelativeLayout_endTime);
            mTextView_type = itemView.findViewById(R.id.mTextView_type);
        }
    }
}
