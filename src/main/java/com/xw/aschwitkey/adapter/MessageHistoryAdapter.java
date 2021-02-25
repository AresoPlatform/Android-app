package com.xw.aschwitkey.adapter;

import android.app.Activity;
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
import com.xw.aschwitkey.entity.MessageBean;
import com.xw.aschwitkey.utils.FormatDataForMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageHistoryAdapter extends RecyclerView.Adapter<MessageHistoryAdapter.ViewHolder> {

    private List<MessageBean> list;
    private Activity context;
    private LayoutInflater inflater;

    public MessageHistoryAdapter(Activity context, List<MessageBean> list) {
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
        return new ViewHolder(inflater.inflate(R.layout.item_message_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mTextView_nickname.setText(list.get(position).getSteemName());
        if (list.get(position).getContent().contains("{\"content\":")) {
            try {
                JSONObject jsonObject = new JSONObject(list.get(position).getContent());
                holder.mTextView_message.setText(jsonObject.getString("content"));
            } catch (JSONException e) {
            }
        } else {
            holder.mTextView_message.setText(list.get(position).getContent());
        }
        holder.mTextView_time.setText(FormatDataForMessage.getTimeRange(context, list.get(position).getSendTime()));
        if (list.get(position).getCount() == 0) {
            holder.mTextView_messageNum.setVisibility(View.GONE);
        } else {
            holder.mTextView_messageNum.setVisibility(View.VISIBLE);
            if (list.get(position).getCount() > 99) {
                holder.mTextView_messageNum.setText("99+");
            } else {
                holder.mTextView_messageNum.setText(list.get(position).getCount() + "");
            }
        }

        Glide.with(context)
                .load(list.get(position).getHeadImage())
                .apply(new RequestOptions().placeholder(R.mipmap.default_icon))
                .into(holder.mImageView_head);


        holder.mRelativeLayout_message.setOnClickListener(new View.OnClickListener() {
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
        ImageView mImageView_head;
        TextView mTextView_nickname, mTextView_message, mTextView_time, mTextView_messageNum;
        RelativeLayout mRelativeLayout_message;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView_head = itemView.findViewById(R.id.mImageView_head);
            mTextView_nickname = itemView.findViewById(R.id.mTextView_nickname);
            mTextView_message = itemView.findViewById(R.id.mTextView_message);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_messageNum = itemView.findViewById(R.id.mTextView_messageNum);
            mRelativeLayout_message = itemView.findViewById(R.id.mRelativeLayout_message);
        }
    }

    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<MessageBean> addMessageList) {
        //增加数据
        int position = list.size();
        list.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<MessageBean> newList) {
        //刷新数据
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public void UpData(int position) {
        notifyItemChanged(position); //更新
    }
}