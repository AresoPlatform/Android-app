package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.BBSRewardBean;
import com.xw.aschwitkey.entity.ChatBean;
import com.xw.aschwitkey.entity.FollowBean;
import com.xw.aschwitkey.entity.VoteBean;
import com.xw.aschwitkey.utils.FormatCurrentData;
import com.xw.aschwitkey.view.CircleImageView;
import com.xw.aschwitkey.view.UpRoundImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class BBSRewardHistoryAdapter extends RecyclerView.Adapter<BBSRewardHistoryAdapter.ViewHolder> {
    private Context context;
    private List<BBSRewardBean.ResultBean> list;
    private LayoutInflater inflater;

    public BBSRewardHistoryAdapter(Context context, List<BBSRewardBean.ResultBean> list) {
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
    public BBSRewardHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_bbs_reward_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BBSRewardHistoryAdapter.ViewHolder holder, int position) {
        holder.mTextView_rewardName.setText(list.get(position).getMsg().getRewardName());
        holder.mTextView_title.setText("打赏了 " + list.get(position).getMsg().getTitle());
        holder.mTextView_reward.setText("+ " + list.get(position).getMsg().getRewardAmount().setScale(3, BigDecimal.ROUND_DOWN).toString()+" xas");
        holder.mTextView_time.setText(FormatCurrentData.getTimeRange1(context, list.get(position).getMsg().getRewardDate()));

        Glide.with(context)
                .load(list.get(position).getMsg().getRewardHeadImage())
                .apply(new RequestOptions().placeholder(R.mipmap.default_icon))
                .into(holder.mImageView_head);

        List<String> Jsonimage = new ArrayList<>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(list.get(position).getMsg().getJson());
            JSONArray jsonArray = new JSONArray(jsonObject.getString("image"));
            for (int i = 0; i < jsonArray.length(); i++) {
                Jsonimage.add(String.valueOf(jsonArray.get(i)));
            }
            if (Jsonimage.isEmpty()) {
                holder.mUpRoundImageView_cover.setVisibility(View.GONE);
            } else {
                Glide.with(context)
                        .load(Jsonimage.get(0))
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.mUpRoundImageView_cover.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(holder.mUpRoundImageView_cover);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
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
        TextView mTextView_rewardName, mTextView_title, mTextView_reward, mTextView_time, mTextView_bottom;
        CircleImageView mImageView_head;
        UpRoundImageView mUpRoundImageView_cover;
        LinearLayout mLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_rewardName = itemView.findViewById(R.id.mTextView_rewardName);
            mTextView_title = itemView.findViewById(R.id.mTextView_title);
            mTextView_reward = itemView.findViewById(R.id.mTextView_reward);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_bottom = itemView.findViewById(R.id.mTextView_bottom);
            mImageView_head = itemView.findViewById(R.id.mImageView_head);
            mUpRoundImageView_cover = itemView.findViewById(R.id.mUpRoundImageView_cover);
            mLinearLayout = itemView.findViewById(R.id.mLinearLayout);
        }
    }
}