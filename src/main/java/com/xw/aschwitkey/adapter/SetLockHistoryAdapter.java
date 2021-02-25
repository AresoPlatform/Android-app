package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.entity.SetLockHistoryBean;
import com.xw.aschwitkey.utils.OtherUtils;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SetLockHistoryAdapter extends RecyclerView.Adapter<SetLockHistoryAdapter.ViewHolder> {
    private Context context;
    private List<SetLockHistoryBean.History> list;
    private LayoutInflater inflater;

    public SetLockHistoryAdapter(Context context, List<SetLockHistoryBean.History> list) {
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
    public SetLockHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_set_lock_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SetLockHistoryAdapter.ViewHolder holder, int position) {
        if (position == list.size() - 1) {
            holder.mTextView_bottom.setVisibility(View.VISIBLE);
        } else {
            holder.mTextView_bottom.setVisibility(View.GONE);
        }

        holder.mTextView_address.setText(list.get(position).getAschAddress().substring(0, 6) + "***" + list.get(position).getAschAddress().substring(list.get(position).getAschAddress().length() - 6, list.get(position).getAschAddress().length()));
        holder.mTextView_xas.setText(list.get(position).getNum().setScale(3, BigDecimal.ROUND_DOWN).toString() + " XAS");
        holder.mTextView_TXas.setText(list.get(position).getTxasNum().setScale(3, BigDecimal.ROUND_DOWN).toString() + " AP");
        if (list.get(position).getLockTimeType() == 1) {
            holder.mTextView_time.setText("3个月");
        } else if (list.get(position).getLockTimeType() == 2) {
            holder.mTextView_time.setText("6个月");
        } else if (list.get(position).getLockTimeType() == 3) {
            holder.mTextView_time.setText("1年");
        } else {
            holder.mTextView_time.setText("2年");
        }
        holder.mTextView_height.setText(list.get(position).getEndHeight() + "");
        if (((OtherUtils.DateToMil(list.get(position).getCreateTime()) + (list.get(position).getEndHeight() - list.get(position).getStartHeight()) * 10 * 1000) - (System.currentTimeMillis() + Content.timePoor)) <= 0) {
            holder.mTextView_day.setVisibility(View.GONE);
            holder.mTextView_day_text.setVisibility(View.GONE);
            holder.mTextView_text.setText(OtherUtils.timedate(OtherUtils.DateToMil(list.get(position).getCreateTime()) + (list.get(position).getEndHeight() - list.get(position).getStartHeight()) * 10 * 1000, "yyyy-MM-dd HH:mm:ss"));
        } else {
            holder.mTextView_day.setVisibility(View.VISIBLE);
            holder.mTextView_day_text.setVisibility(View.VISIBLE);
            holder.mTextView_text.setText(OtherUtils.timedate(OtherUtils.DateToMil(list.get(position).getCreateTime()) + (list.get(position).getEndHeight() - list.get(position).getStartHeight()) * 10 * 1000, "yyyy-MM-dd HH:mm:ss") + " 剩 ");
            holder.mTextView_day.setText(((OtherUtils.DateToMil(list.get(position).getCreateTime()) + (list.get(position).getEndHeight() - list.get(position).getStartHeight()) * 10 * 1000) - (System.currentTimeMillis() + Content.timePoor)) / (24 * 60 * 60 * 1000) + "");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_address, mTextView_xas, mTextView_TXas, mTextView_time, mTextView_height, mTextView_text, mTextView_day, mTextView_day_text, mTextView_bottom;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_address = itemView.findViewById(R.id.mTextView_address);
            mTextView_TXas = itemView.findViewById(R.id.mTextView_TXas);
            mTextView_height = itemView.findViewById(R.id.mTextView_height);
            mTextView_xas = itemView.findViewById(R.id.mTextView_xas);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_text = itemView.findViewById(R.id.mTextView_text);
            mTextView_day = itemView.findViewById(R.id.mTextView_day);
            mTextView_day_text = itemView.findViewById(R.id.mTextView_day_text);
            mTextView_bottom = itemView.findViewById(R.id.mTextView_bottom);
        }
    }
}