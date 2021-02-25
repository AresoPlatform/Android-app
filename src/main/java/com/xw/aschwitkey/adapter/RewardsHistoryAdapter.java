package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.RewardsHistoryBean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RewardsHistoryAdapter extends RecyclerView.Adapter<RewardsHistoryAdapter.ViewHolder> {
    private Context context;
    private List<RewardsHistoryBean.History> list;
    private LayoutInflater inflater;

    public RewardsHistoryAdapter(Context context, List<RewardsHistoryBean.History> list) {
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
    public RewardsHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_rewards_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsHistoryAdapter.ViewHolder holder, int position) {
        if (list.get(position).getNickName().isEmpty()) {
            holder.mTextView_username.setText(list.get(position).getPhone().substring(0, 3) + "****" + list.get(position).getPhone().substring(7, list.get(position).getPhone().length()));
        } else {
            holder.mTextView_username.setText(list.get(position).getPhone().substring(0, 3) + "****" + list.get(position).getPhone().substring(7, list.get(position).getPhone().length()) + "(" + list.get(position).getNickName() + ")");
        }
        if (list.get(position).isStatus()) {
            holder.mTextView_currency.setText(new BigDecimal(list.get(position).getAmount()).setScale(4, BigDecimal.ROUND_DOWN).toString() + " xas");
            holder.mTextView_currency.setTextColor(context.getResources().getColor(R.color.text_color_red));
        } else {
            holder.mTextView_currency.setText("+" + new BigDecimal(list.get(position).getAmount()).setScale(4, BigDecimal.ROUND_DOWN).toString() + " xas");
            holder.mTextView_currency.setTextColor(context.getResources().getColor(R.color.text_color_green));
        }
        holder.mTextView_time.setText(list.get(position).getInvTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_username, mTextView_currency, mTextView_time;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_username = itemView.findViewById(R.id.mTextView_username);
            mTextView_currency = itemView.findViewById(R.id.mTextView_currency);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
        }
    }
}
