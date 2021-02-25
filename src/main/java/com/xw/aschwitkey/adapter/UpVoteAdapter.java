package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.CommentBean;
import com.xw.aschwitkey.entity.PostBean;
import com.xw.aschwitkey.entity.UpVoteBean;
import com.xw.aschwitkey.utils.FormatCurrentData;
import com.xw.aschwitkey.view.CircleImageView;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UpVoteAdapter extends RecyclerView.Adapter<UpVoteAdapter.ViewHolder> {
    private Context context;
    private List<PostBean.ResultBean.ActiveVotesBean> list;
    private LayoutInflater inflater;
    private BigDecimal abs_rshares;
    private BigDecimal money;

    public UpVoteAdapter(Context context, List<PostBean.ResultBean.ActiveVotesBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setRshares(BigDecimal abs_rshares, BigDecimal money) {
        this.abs_rshares = abs_rshares;
        this.money = money;
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
    public UpVoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_upvote_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UpVoteAdapter.ViewHolder holder, int position) {
        holder.mTextView_nickname.setText(list.get(position).getVoter());
        holder.mTextView_time.setText(FormatCurrentData.getTimeRange(context, list.get(position).getTime()));
        if (abs_rshares.doubleValue() == 0d) {
            holder.mTextView_m.setText("0.000");
        } else {
            holder.mTextView_m.setText(money.multiply(new BigDecimal(list.get(position).getRshares())).divide(abs_rshares, 3, BigDecimal.ROUND_DOWN).toString());
        }
        String resultString = "***" + list.get(position).getVoter() + "/avatar/small";
        Glide.with(context)
                .load(resultString)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.default_icon).diskCacheStrategy(DiskCacheStrategy.NONE))
                .error(R.mipmap.default_icon)
                .into(holder.mImageView_head);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_nickname, mTextView_time, mTextView_m;
        CircleImageView mImageView_head;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_nickname = itemView.findViewById(R.id.mTextView_nickname);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_m = itemView.findViewById(R.id.mTextView_m);
            mImageView_head = itemView.findViewById(R.id.mImageView_head);
        }
    }
}