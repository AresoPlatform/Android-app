package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.entity.SetLockHistoryBean;
import com.xw.aschwitkey.entity.Trc20HistoryBean;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Trc20HistoryAdapter extends RecyclerView.Adapter<Trc20HistoryAdapter.ViewHolder> {
    private Context context;
    private List<Trc20HistoryBean.History> list;
    private LayoutInflater inflater;
    private SPUtils spUtils1;

    public Trc20HistoryAdapter(Context context, List<Trc20HistoryBean.History> list) {
        this.context = context;
        this.list = list;
        spUtils1 = new SPUtils(context, "AschImport");
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
    public Trc20HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_trc20_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Trc20HistoryAdapter.ViewHolder holder, int position) {
        if (spUtils1.getString("tronChildAddress", "").equals(list.get(position).getTo())) {
            holder.mImageView_left.setImageResource(R.mipmap.zhuanru);
            if (list.get(position).getAmount() == null) {
                holder.mTextView_currency.setText("+" + new BigDecimal(list.get(position).getValue()).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN).toString());
            } else {
                holder.mTextView_currency.setText("+" + new BigDecimal(list.get(position).getAmount()).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN).toString());
            }
            holder.mTextView_currency.setTextColor(context.getResources().getColor(R.color.text_color_green));
            holder.mTextView_address.setText(list.get(position).getFrom().substring(0, 6) + "..." + list.get(position).getFrom().substring(list.get(position).getFrom().length() - 6, list.get(position).getFrom().length()));
        } else {
            holder.mImageView_left.setImageResource(R.mipmap.zhuanchu);
            if (list.get(position).getAmount() == null) {
                if (list.get(position).getType().equals("Approval")) {
                    holder.mTextView_currency.setText("-0.000000");
                } else {
                    holder.mTextView_currency.setText("-" + new BigDecimal(list.get(position).getValue()).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN).toString());
                }
            } else {
                holder.mTextView_currency.setText("-" + new BigDecimal(list.get(position).getAmount()).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_DOWN).toString());
            }
            holder.mTextView_currency.setTextColor(context.getResources().getColor(R.color.text_color_red));
            holder.mTextView_address.setText(list.get(position).getTo().substring(0, 6) + "..." + list.get(position).getTo().substring(list.get(position).getTo().length() - 6, list.get(position).getTo().length()));
        }
        holder.mTextView_time.setText(OtherUtils.timedate(list.get(position).getBlock_timestamp(), "yyyy-MM-dd HH:mm:ss"));
        holder.mRelativeLayout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v);
            }
        });
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
        RelativeLayout mRelativeLayout_item;
        ImageView mImageView_left, mImageView_copy;
        TextView mTextView_address, mTextView_currency, mTextView_note, mTextView_time;

        public ViewHolder(View itemView) {
            super(itemView);
            mRelativeLayout_item = itemView.findViewById(R.id.mRelativeLayout_item);
            mImageView_left = itemView.findViewById(R.id.mImageView_left);
            mImageView_copy = itemView.findViewById(R.id.mImageView_copy);
            mTextView_address = itemView.findViewById(R.id.mTextView_address);
            mTextView_currency = itemView.findViewById(R.id.mTextView_currency);
            mTextView_note = itemView.findViewById(R.id.mTextView_note);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
        }
    }
}