package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.entity.PledgeMiningBean;
import com.xw.aschwitkey.utils.OtherUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PledgeMiningHistoryAdapter extends RecyclerView.Adapter<PledgeMiningHistoryAdapter.ViewHolder> {
    private Context context;
    private List<PledgeMiningBean.History> list;
    private LayoutInflater inflater;

    public PledgeMiningHistoryAdapter(Context context, List<PledgeMiningBean.History> list) {
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
    public PledgeMiningHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_pledge_mining_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PledgeMiningHistoryAdapter.ViewHolder holder, int position) {
        try {
            if (System.currentTimeMillis() + Content.timePoor - OtherUtils.dateToStamp(list.get(position).getPledgeTime()) <= 1000 * 60 * 2) {
                holder.mTextView_bg_l.setVisibility(View.VISIBLE);
            } else {
                holder.mTextView_bg_l.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mTextView_TXas.setText(list.get(position).getPledgeAmount().setScale(3, BigDecimal.ROUND_DOWN).toString() + " AP");
        holder.mTextView_pledgeTime.setText(list.get(position).getPledgeTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_TXas, mTextView_pledgeTime, mTextView_bg_l;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_bg_l = itemView.findViewById(R.id.mTextView_bg_l);
            mTextView_TXas = itemView.findViewById(R.id.mTextView_TXas);
            mTextView_pledgeTime = itemView.findViewById(R.id.mTextView_pledgeTime);
        }
    }
}