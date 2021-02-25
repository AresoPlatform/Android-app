package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihang.ShadowLayout;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.entity.UnfreezeBean;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UnFreezeAdapter extends RecyclerView.Adapter<UnFreezeAdapter.ViewHolder> {
    private Context context;
    private List<UnfreezeBean> energyList, bandwidthList;
    private LayoutInflater inflater;
    private String tronAddress;

    public UnFreezeAdapter(Context context, List<UnfreezeBean> energyList, List<UnfreezeBean> bandwidthList, String tronAddress) {
        this.context = context;
        this.energyList = energyList;
        this.bandwidthList = bandwidthList;
        this.tronAddress = tronAddress;
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
    public UnFreezeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_unfreeze_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnFreezeAdapter.ViewHolder holder, int position) {
        if (Content.unFreezeType == 1) {
            holder.mTextView_address.setText(energyList.get(position).getToAddress().substring(0, 10) + "..." + energyList.get(position).getToAddress().substring(energyList.get(position).getToAddress().length() - 10, energyList.get(position).getToAddress().length()));
            holder.mTextView_freezeType.setText("能量");
            holder.mTextView_freezeTrx.setText(energyList.get(position).getFreezeNumber().setScale(0, BigDecimal.ROUND_DOWN).toString());
            holder.mTextView_unfreezeTime.setText(OtherUtils.timedate(energyList.get(position).getUnFreezeTime(), "yyyy/MM/dd HH:mm:ss"));
            if (energyList.get(position).getToAddress().equals(tronAddress)) {
                holder.mTextView_isCurrent.setVisibility(View.VISIBLE);
            } else {
                holder.mTextView_isCurrent.setVisibility(View.GONE);
            }
            if (System.currentTimeMillis() + Content.timePoor >= energyList.get(position).getUnFreezeTime()) {
                holder.mTextView_notFreezeTime.setVisibility(View.GONE);
                holder.mTextView_btn_unfreeze.setVisibility(View.VISIBLE);
            } else {
                holder.mTextView_notFreezeTime.setVisibility(View.VISIBLE);
                holder.mTextView_btn_unfreeze.setVisibility(View.GONE);
            }
        } else {
            holder.mTextView_address.setText(bandwidthList.get(position).getToAddress().substring(0, 10) + "..." + bandwidthList.get(position).getToAddress().substring(bandwidthList.get(position).getToAddress().length() - 10, bandwidthList.get(position).getToAddress().length()));
            holder.mTextView_freezeType.setText("带宽");
            holder.mTextView_freezeTrx.setText(bandwidthList.get(position).getFreezeNumber().setScale(0, BigDecimal.ROUND_DOWN).toString());
            holder.mTextView_unfreezeTime.setText(OtherUtils.timedate(bandwidthList.get(position).getUnFreezeTime(), "yyyy/MM/dd HH:mm:ss"));
            if (bandwidthList.get(position).getToAddress().equals(tronAddress)) {
                holder.mTextView_isCurrent.setVisibility(View.VISIBLE);
            } else {
                holder.mTextView_isCurrent.setVisibility(View.GONE);
            }
            if (System.currentTimeMillis() + Content.timePoor >= bandwidthList.get(position).getUnFreezeTime()) {
                holder.mTextView_notFreezeTime.setVisibility(View.GONE);
                holder.mTextView_btn_unfreeze.setVisibility(View.VISIBLE);
            } else {
                holder.mTextView_notFreezeTime.setVisibility(View.VISIBLE);
                holder.mTextView_btn_unfreeze.setVisibility(View.GONE);
            }
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

        holder.mTextView_btn_unfreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Content.unFreezeType == 1 ? energyList.size() : bandwidthList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_notFreezeTime, mTextView_address, mTextView_isCurrent, mTextView_freezeType, mTextView_freezeTrx, mTextView_unfreezeTime, mTextView_btn_unfreeze;
        ImageView mImageView_copy;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_notFreezeTime = itemView.findViewById(R.id.mTextView_notFreezeTime);
            mTextView_address = itemView.findViewById(R.id.mTextView_address);
            mTextView_isCurrent = itemView.findViewById(R.id.mTextView_isCurrent);
            mTextView_freezeType = itemView.findViewById(R.id.mTextView_freezeType);
            mTextView_freezeTrx = itemView.findViewById(R.id.mTextView_freezeTrx);
            mTextView_unfreezeTime = itemView.findViewById(R.id.mTextView_unfreezeTime);
            mImageView_copy = itemView.findViewById(R.id.mImageView_copy);
            mTextView_btn_unfreeze = itemView.findViewById(R.id.mTextView_btn_unfreeze);
        }
    }

}
