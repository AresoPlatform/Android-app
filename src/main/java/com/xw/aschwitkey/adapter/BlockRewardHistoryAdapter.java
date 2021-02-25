package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.entity.BlockRewardBean;
import com.xw.aschwitkey.utils.OtherUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BlockRewardHistoryAdapter extends RecyclerView.Adapter<BlockRewardHistoryAdapter.ViewHolder> {
    private Context context;
    private List<BlockRewardBean.History> list;
    private LayoutInflater inflater;

    public BlockRewardHistoryAdapter(Context context, List<BlockRewardBean.History> list) {
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
    public BlockRewardHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_block_reward_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlockRewardHistoryAdapter.ViewHolder holder, int position) {
        try {
            if (System.currentTimeMillis() + Content.timePoor - OtherUtils.dateToStamp(list.get(position).getCreateTime()) <= 1000 * 60 * 2) {
                holder.mTextView_bg_l.setVisibility(View.VISIBLE);
            } else {
                holder.mTextView_bg_l.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mTextView_reward.setText(list.get(position).getNum().divide(new BigDecimal("1000000")).setScale(3, BigDecimal.ROUND_DOWN).toString() + " ASO");
        holder.mTextView_time.setText(list.get(position).getCreateTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_reward, mTextView_time, mTextView_bg_l;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_reward = itemView.findViewById(R.id.mTextView_reward);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_bg_l = itemView.findViewById(R.id.mTextView_bg_l);
        }
    }
}