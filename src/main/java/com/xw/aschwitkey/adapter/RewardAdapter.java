package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.RewardBean;
import com.xw.aschwitkey.entity.UpVoteBean;
import com.xw.aschwitkey.utils.FormatCurrentData;
import com.xw.aschwitkey.view.CircleImageView;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.ViewHolder> {
    private Context context;
    private List<RewardBean.ResultBean> list;
    private LayoutInflater inflater;

    public RewardAdapter(Context context, List<RewardBean.ResultBean> list) {
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
    public RewardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_reward_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RewardAdapter.ViewHolder holder, int position) {
        holder.mTextView_rewardNickname.setText(list.get(position).getFromName());
        holder.mTextView_time.setText(FormatCurrentData.getTimeRange1(context, list.get(position).getCreateTime()));
        holder.mTextView_m.setText(list.get(position).getRewardAmount().setScale(3, BigDecimal.ROUND_DOWN).toString() + " xas");
        Glide.with(context)
                .load(list.get(position).getFromImage())
                .apply(new RequestOptions().placeholder(R.mipmap.default_icon))
                .error(R.mipmap.default_icon)
                .into(holder.mImageView_rewardHead);
        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
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
        TextView mTextView_rewardNickname, mTextView_time, mTextView_m;
        CircleImageView mImageView_rewardHead;
        RelativeLayout mRelativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_rewardNickname = itemView.findViewById(R.id.mTextView_rewardNickname);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_m = itemView.findViewById(R.id.mTextView_m);
            mImageView_rewardHead = itemView.findViewById(R.id.mImageView_rewardHead);
            mRelativeLayout = itemView.findViewById(R.id.mRelativeLayout);
        }
    }
}