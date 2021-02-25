package com.xw.aschwitkey.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.CommentBean;
import com.xw.aschwitkey.utils.FormatCurrentData;
import com.xw.aschwitkey.utils.FormatDataForMessage;
import com.xw.aschwitkey.view.CircleImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoreCommentAdapter extends RecyclerView.Adapter<MoreCommentAdapter.ViewHolder> {

    private List<CommentBean.CmBean> list;
    private Activity context;
    private LayoutInflater inflater;

    public MoreCommentAdapter(Activity context, List<CommentBean.CmBean> list) {
        this.list = list;
        this.context = context;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_morecomment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mTextView_nick.setText(list.get(position).getAuthor());
        holder.mTextView_text.setText(list.get(position).getComment());
        holder.mTextView_data.setText(FormatCurrentData.getTimeRange1(context, list.get(position).getCreateTime()));
        holder.mTextView_commentUser.setText(list.get(position).getHuifuPer());

        Glide.with(context)
                .load(list.get(position).getAuthorImage())
                .apply(new RequestOptions().placeholder(R.mipmap.default_icon))
                .into(holder.mImageView_head);

        holder.mLinearLayout_comment.setOnClickListener(new View.OnClickListener() {
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


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mImageView_head;
        TextView mTextView_nick, mTextView_text, mTextView_data, mTextView_commentUser;
        LinearLayout mLinearLayout_comment;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextView_nick = itemView.findViewById(R.id.mTextView_nick);
            mTextView_text = itemView.findViewById(R.id.mTextView_text);
            mTextView_data = itemView.findViewById(R.id.mTextView_data);
            mTextView_commentUser = itemView.findViewById(R.id.mTextView_commentUser);
            mLinearLayout_comment = itemView.findViewById(R.id.mLinearLayout_comment);
            mImageView_head = itemView.findViewById(R.id.mImageView_head);
        }
    }
}


