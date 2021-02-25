package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.HistoryBean;
import com.xw.aschwitkey.entity.TransactionBean;
import com.xw.aschwitkey.utils.OtherUtils;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {
    private Context context;
    private List<TransactionBean.History> list;
    private LayoutInflater inflater;

    public TransactionHistoryAdapter(Context context, List<TransactionBean.History> list) {
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
    public TransactionHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryAdapter.ViewHolder holder, int position) {
        holder.mImageView_left.setImageResource(R.mipmap.zhuanchu);
        holder.mTextView_time.setText(OtherUtils.timedate((list.get(position).getTimestamp() + 1467057600L) * 1000, "yyyy-MM-dd HH:mm:ss"));
        if (list.get(position).getType() == 1) {
            holder.mTextView_address.setText(list.get(position).getArgs().get(1).substring(0, 6) + "..." + list.get(position).getArgs().get(1).substring(list.get(position).getArgs().get(1).length() - 6, list.get(position).getArgs().get(1).length()));
            holder.mTextView_currency.setText("-" + new BigDecimal(list.get(position).getFee()).divide(new BigDecimal("100000000"), 1, BigDecimal.ROUND_HALF_DOWN).setScale(1, BigDecimal.ROUND_DOWN).toString());
            holder.mTextView_note.setText("类型：转账");
            holder.mImageView_copy.setVisibility(View.VISIBLE);
        } else if (list.get(position).getType() == 4) {
            if (list.get(position).getArgs() == null) {
                holder.mTextView_address.setText("数量：?，高度：?");
            } else {
                holder.mTextView_address.setText("数量：" + new BigDecimal(list.get(position).getArgs().get(1)).divide(new BigDecimal("100000000"), 0, BigDecimal.ROUND_HALF_DOWN).setScale(0, BigDecimal.ROUND_DOWN).toString() + "，高度：" + list.get(position).getArgs().get(0));
            }
            holder.mTextView_currency.setText("-" + new BigDecimal(list.get(position).getFee()).divide(new BigDecimal("100000000"), 1, BigDecimal.ROUND_HALF_DOWN).setScale(1, BigDecimal.ROUND_DOWN).toString());
            holder.mTextView_note.setText("类型：锁仓");
            holder.mImageView_copy.setVisibility(View.GONE);
        }
        holder.mTextView_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v);
            }
        });
        holder.mImageView_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView_left, mImageView_copy;
        TextView mTextView_address, mTextView_currency, mTextView_note, mTextView_time;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView_left = itemView.findViewById(R.id.mImageView_left);
            mImageView_copy = itemView.findViewById(R.id.mImageView_copy);
            mTextView_address = itemView.findViewById(R.id.mTextView_address);
            mTextView_currency = itemView.findViewById(R.id.mTextView_currency);
            mTextView_note = itemView.findViewById(R.id.mTextView_note);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
        }
    }
}