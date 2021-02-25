package com.xw.aschwitkey.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.CommentBean;
import com.xw.aschwitkey.entity.PostBean;
import com.xw.aschwitkey.utils.FormatCurrentData;
import com.xw.aschwitkey.utils.FormatDataForMessage;
import com.xw.aschwitkey.view.CircleImageView;
import com.xw.aschwitkey.view.RecyclerViewNoBugLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Activity context;
    private List<CommentBean.ResultBean> list;
    private LayoutInflater inflater;
    private boolean isComment = false;

    public CommentAdapter(Activity context, List<CommentBean.ResultBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public interface OnClickListenerFace {
        void OnClickTemp(int p, View view, int type, int p1);
    }

    public OnClickListenerFace OnClickListenerFace;

    public void setOnClickListenerFace(OnClickListenerFace OnClickListenerFace) {
        this.OnClickListenerFace = OnClickListenerFace;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_comment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        holder.mTextView_nickname.setText(list.get(position).getAuthor());
        holder.mTextView_comment.setText(list.get(position).getComment());
        holder.mTextView_time.setText(FormatCurrentData.getTimeRange1(context, list.get(position).getCreateTime()));
        Glide.with(context)
                .load(list.get(position).getAuthorImage())
                .apply(new RequestOptions().placeholder(R.mipmap.default_icon))
                .error(R.mipmap.default_icon)
                .into(holder.mImageView_head);
        if (isComment) {
            isComment = false;
            if (!list.get(position).getComments().isEmpty()) {
                holder.mTextView_more.setVisibility(View.VISIBLE);
                holder.mTextView_more.setTag("close");
                holder.mRecyclerView_more.setVisibility(View.VISIBLE);
                MoreCommentAdapter adapter = new MoreCommentAdapter(context, list.get(position).getComments());
                RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                holder.mRecyclerView_more.setLayoutManager(layoutManager);
                holder.mRecyclerView_more.setAdapter(adapter);
                holder.mTextView_more.setText("— 收起回复 —");
                adapter.setOnClickListenerFace(new MoreCommentAdapter.OnClickListenerFace() {
                    @Override
                    public void OnClickTemp(int p, View view) {
                        OnClickListenerFace.OnClickTemp(position, view, 0, p);
                    }
                });
            } else {
                holder.mTextView_more.setVisibility(View.GONE);
                holder.mRecyclerView_more.setVisibility(View.GONE);
            }
        } else {
            if (!list.get(position).getComments().isEmpty()) {
                if (holder.mTextView_more.getTag() != null) {
                    if (holder.mTextView_more.getTag().toString().equals("close")) {
                        holder.mRecyclerView_more.setVisibility(View.VISIBLE);
                        MoreCommentAdapter adapter = new MoreCommentAdapter(context, list.get(position).getComments());
                        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        holder.mRecyclerView_more.setLayoutManager(layoutManager);
                        holder.mRecyclerView_more.setAdapter(adapter);
                        holder.mTextView_more.setText("— 收起回复 —");
                        adapter.setOnClickListenerFace(new MoreCommentAdapter.OnClickListenerFace() {
                            @Override
                            public void OnClickTemp(int p, View view) {
                                OnClickListenerFace.OnClickTemp(position, view, 0, p);
                            }
                        });
                    } else {
                        holder.mRecyclerView_more.setVisibility(View.GONE);
                        holder.mTextView_more.setText("— 展开回复 —");
                    }
                } else {
                    holder.mTextView_more.setTag("open");
                }
                holder.mTextView_more.setVisibility(View.VISIBLE);

            } else {
                holder.mTextView_more.setVisibility(View.GONE);
            }
        }

        holder.mImageView_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v, 1, -1);
            }
        });
        holder.mTextView_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v, 1, -1);
            }
        });
        holder.mTextView_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mTextView_more.getTag().toString().equals("open")) {
                    holder.mRecyclerView_more.setVisibility(View.VISIBLE);
                    MoreCommentAdapter adapter = new MoreCommentAdapter(context, list.get(position).getComments());
                    RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    holder.mRecyclerView_more.setLayoutManager(layoutManager);
                    holder.mRecyclerView_more.setAdapter(adapter);
                    holder.mTextView_more.setTag("close");
                    holder.mTextView_more.setText("— 收起回复 —");
                    adapter.setOnClickListenerFace(new MoreCommentAdapter.OnClickListenerFace() {
                        @Override
                        public void OnClickTemp(int p, View view) {
                            OnClickListenerFace.OnClickTemp(position, view, 0, p);
                        }
                    });
                } else {
                    holder.mTextView_more.setTag("open");
                    holder.mRecyclerView_more.setVisibility(View.GONE);
                    holder.mTextView_more.setText("— 展开回复 —");
                }
//                OnClickListenerFace.OnClickTemp(position, v);
            }
        });
        holder.mRelativeLayout_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v, 1, -1);
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

    public void upItem(int position) {
        isComment = true;
        notifyItemChanged(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_nickname, mTextView_comment, mTextView_time, mTextView_more;
        CircleImageView mImageView_head;
        RelativeLayout mRelativeLayout_comment;
        RecyclerView mRecyclerView_more;

        public ViewHolder(View itemView) {
            super(itemView);
            mRelativeLayout_comment = itemView.findViewById(R.id.mRelativeLayout_comment);
            mTextView_nickname = itemView.findViewById(R.id.mTextView_nickname);
            mTextView_comment = itemView.findViewById(R.id.mTextView_comment);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_more = itemView.findViewById(R.id.mTextView_more);
            mImageView_head = itemView.findViewById(R.id.mImageView_head);
            mRecyclerView_more = itemView.findViewById(R.id.mRecyclerView_more);
        }
    }
}