package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.entity.LpPledgeMiningBean;
import com.xw.aschwitkey.entity.PledgeMiningBean;
import com.xw.aschwitkey.utils.OtherUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LpPledgeMiningHistoryAdapter extends RecyclerView.Adapter<LpPledgeMiningHistoryAdapter.ViewHolder> {
    private Context context;
    private List<LpPledgeMiningBean.History> list;
    private LayoutInflater inflater;

    public LpPledgeMiningHistoryAdapter(Context context, List<LpPledgeMiningBean.History> list) {
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
    public LpPledgeMiningHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_pledge_mining_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LpPledgeMiningHistoryAdapter.ViewHolder holder, int position) {
        try {
            if (System.currentTimeMillis() + Content.timePoor - OtherUtils.dateToStamp(list.get(position).getPledgeTime()) <= 1000 * 60 * 2) {
                holder.mTextView_bg_l.setVisibility(View.VISIBLE);
            } else {
                holder.mTextView_bg_l.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(list.get(position).getType()==1){
            holder.mTextView_type.setText("质押额度");
            holder.mTextView_TXas.setTextColor(context.getResources().getColor(R.color.text_color_red));
            holder.mTextView_TXas.setText("-"+list.get(position).getPledgeAmount().setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
        }else{
            holder.mTextView_type.setText("撤回额度");
            holder.mTextView_TXas.setTextColor(context.getResources().getColor(R.color.text_color_green));
            holder.mTextView_TXas.setText("+"+list.get(position).getPledgeAmount().setScale(3, BigDecimal.ROUND_DOWN).toString() + " LP");
        }
        holder.mTextView_pledgeTime.setText(list.get(position).getPledgeTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_type,mTextView_TXas, mTextView_pledgeTime, mTextView_bg_l;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_type = itemView.findViewById(R.id.mTextView_type);
            mTextView_bg_l = itemView.findViewById(R.id.mTextView_bg_l);
            mTextView_TXas = itemView.findViewById(R.id.mTextView_TXas);
            mTextView_pledgeTime = itemView.findViewById(R.id.mTextView_pledgeTime);
        }
    }
}