package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.HistoryBean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    private List<HistoryBean.History> list;
    private LayoutInflater inflater;

    public HistoryAdapter(Context context, List<HistoryBean.History> list) {
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
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        if (list.get(position).getInOrout() == 1) {
            holder.mImageView_left.setImageResource(R.mipmap.zhuanru);
            holder.mTextView_currency.setText("+" + new BigDecimal(list.get(position).getAmount()).setScale(3, BigDecimal.ROUND_DOWN).toString());
            holder.mTextView_currency.setTextColor(context.getResources().getColor(R.color.text_color_green));
            holder.mTextView_address.setText(list.get(position).getSenderId().substring(0, 6) + "..." + list.get(position).getSenderId().substring(list.get(position).getSenderId().length() - 6, list.get(position).getSenderId().length()));
        } else {
            holder.mImageView_left.setImageResource(R.mipmap.zhuanchu);
            holder.mTextView_currency.setText("-" + new BigDecimal(list.get(position).getAmount()).setScale(3,BigDecimal.ROUND_DOWN).toString());
            holder.mTextView_currency.setTextColor(context.getResources().getColor(R.color.text_color_red));
            holder.mTextView_address.setText(list.get(position).getRecipientId().substring(0, 6) + "..." + list.get(position).getRecipientId().substring(list.get(position).getRecipientId().length() - 6, list.get(position).getRecipientId().length()));
        }
        holder.mTextView_time.setText(list.get(position).getDealTime());
        holder.mTextView_note.setText("备注：" + list.get(position).getMessage());
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