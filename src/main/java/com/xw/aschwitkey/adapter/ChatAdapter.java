package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.ChatBean;
import com.xw.aschwitkey.utils.FormatDataForDisplay;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private List<ChatBean.Chat> list;
    private LayoutInflater inflater;
    private SPUtils spUtils;
    private Gson gson;

    public ChatAdapter(Context context, List<ChatBean.Chat> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        spUtils = new SPUtils(context, "AschWallet");
        gson = new Gson();
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
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_chat_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        try {
            if (position == list.size() - 1) {
                holder.mTextView_bottom.setVisibility(View.VISIBLE);
            } else {
                holder.mTextView_bottom.setVisibility(View.GONE);
            }
            JSONObject jsonObject = new JSONObject(gson.toJson(list.get(position).getMsg()));
            if (position - 1 != -1) {
                if (OtherUtils.DateToMil(list.get(position).getSendTime()) - OtherUtils.DateToMil(list.get(position - 1).getSendTime()) > 60 * 5 * 1000) {
                    holder.mTextView_time.setVisibility(View.VISIBLE);
                    holder.mTextView_time.setText(FormatDataForDisplay.getTimeRange(context, list.get(position).getSendTime()));
                } else {
                    holder.mTextView_time.setVisibility(View.GONE);
                }
            } else {
                holder.mTextView_time.setVisibility(View.GONE);
            }
            if (spUtils.getString("nickname").equals(list.get(position).getSendUser())) {
                Glide.with(context)
                        .load(list.get(position).getSendHeadImage())
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.default_icon)
                                .skipMemoryCache(true)
                                .dontAnimate())
                        .into(holder.mImageView_meHead);
                holder.mRelativeLayout_me.setVisibility(View.VISIBLE);
                holder.mLinearLayout_you.setVisibility(View.GONE);
                holder.mTextView_me.setText(jsonObject.getString("content"));
            } else {
                Glide.with(context)
                        .load(list.get(position).getSendHeadImage())
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.default_icon)
                                .skipMemoryCache(true)
                                .dontAnimate())
                        .into(holder.mImageView_youHead);
                holder.mRelativeLayout_me.setVisibility(View.GONE);
                holder.mLinearLayout_you.setVisibility(View.VISIBLE);
                holder.mTextView_you.setText(jsonObject.getString("content"));
            }
            holder.mImageView_youHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });

        } catch (JSONException e) {
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(ChatBean.Chat addMessage) {
        int position = list.size();
        list.add(position, addMessage);
        notifyDataSetChanged();
    }

    public void addList(List<ChatBean.Chat> addMessageList) {
        list.addAll(0, addMessageList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout_you;
        RelativeLayout mRelativeLayout_me;
        TextView mTextView_time, mTextView_you, mTextView_me, mTextView_bottom;
        ImageView mImageView_youHead, mImageView_meHead;

        public ViewHolder(View itemView) {
            super(itemView);
            mLinearLayout_you = itemView.findViewById(R.id.mLinearLayout_you);
            mRelativeLayout_me = itemView.findViewById(R.id.mRelativeLayout_me);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_you = itemView.findViewById(R.id.mTextView_you);
            mTextView_me = itemView.findViewById(R.id.mTextView_me);
            mTextView_bottom = itemView.findViewById(R.id.mTextView_bottom);
            mImageView_youHead = itemView.findViewById(R.id.mImageView_youHead);
            mImageView_meHead = itemView.findViewById(R.id.mImageView_meHead);
        }
    }
}