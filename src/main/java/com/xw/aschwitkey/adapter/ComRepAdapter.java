package com.xw.aschwitkey.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.ComRepBean;
import com.xw.aschwitkey.entity.FollowBean;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.view.UpRoundImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ComRepAdapter extends RecyclerView.Adapter<ComRepAdapter.ViewHolder> {

    private List<ComRepBean.ResultBean> list;
    private Activity context;
    private LayoutInflater inflater;
    private SPUtils spUtils;

    public ComRepAdapter(Activity context, List<ComRepBean.ResultBean> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        spUtils = new SPUtils(context, "AschWallet");
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
        return new ViewHolder(inflater.inflate(R.layout.item_com_rep_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mTextView_commentName.setText(list.get(position).getMsg().getCommentName());
        if (list.get(position).getMsg().getType() == 1) {
            holder.mTextView_type.setText("评论你");
            holder.mLinearLayout_bottom.setVisibility(View.GONE);
        } else {
            holder.mLinearLayout_bottom.setVisibility(View.VISIBLE);
            if (list.get(position).getMsg().getCommentContentName().equals(spUtils.getString("nickname"))) {
                holder.mTextView_type.setText("回复你");
            } else {
                holder.mTextView_type.setText("评论你");
            }
        }
        try {
            holder.mTextView_time.setText(OtherUtils.timedate(OtherUtils.dateToStamp(list.get(position).getMsg().getCommentDate()), "yyyy年MM月dd日 HH:mm"));
        } catch (ParseException e) {
        }
        holder.mTextView_comment.setText(list.get(position).getMsg().getComment());
        holder.mTextView_permlinkAuthor.setText(list.get(position).getMsg().getPermlinkAuthor());
        holder.mTextView_title.setText(list.get(position).getMsg().getTitle());
        holder.mTextView_commentContentName.setText(list.get(position).getMsg().getCommentContentName());
        holder.mTextView_commentContent.setText("：" + list.get(position).getMsg().getCommentContent());

        holder.mTextView_commentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v);
            }
        });

        holder.mImageView_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v);
            }
        });

        holder.mTextView_btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v);
            }
        });

        holder.mLinearLayout_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v);
            }
        });

        Glide.with(context)
                .load(list.get(position).getMsg().getHeadImage())
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
        ImageView mImageView_head;
        UpRoundImageView mUpRoundImageView_cover;
        TextView mTextView_commentName, mTextView_type, mTextView_time, mTextView_btn_reply, mTextView_comment, mTextView_permlinkAuthor, mTextView_title, mTextView_commentContentName, mTextView_commentContent, mTextView_bottom;
        LinearLayout mLinearLayout_bottom, mLinearLayout_blog;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView_head = itemView.findViewById(R.id.mImageView_head);
            mUpRoundImageView_cover = itemView.findViewById(R.id.mUpRoundImageView_cover);
            mTextView_commentName = itemView.findViewById(R.id.mTextView_commentName);
            mTextView_type = itemView.findViewById(R.id.mTextView_type);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_btn_reply = itemView.findViewById(R.id.mTextView_btn_reply);
            mTextView_comment = itemView.findViewById(R.id.mTextView_comment);
            mTextView_permlinkAuthor = itemView.findViewById(R.id.mTextView_permlinkAuthor);
            mTextView_title = itemView.findViewById(R.id.mTextView_title);
            mTextView_commentContentName = itemView.findViewById(R.id.mTextView_commentContentName);
            mTextView_commentContent = itemView.findViewById(R.id.mTextView_commentContent);
            mLinearLayout_bottom = itemView.findViewById(R.id.mLinearLayout_bottom);
            mLinearLayout_blog = itemView.findViewById(R.id.mLinearLayout_blog);
            mTextView_bottom = itemView.findViewById(R.id.mTextView_bottom);
        }
    }

    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<ComRepBean.ResultBean> addMessageList) {
        //增加数据
        int position = list.size();
        list.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<ComRepBean.ResultBean> newList) {
        //刷新数据
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public void UpData(int position) {
        notifyItemChanged(position); //更新
    }
}


