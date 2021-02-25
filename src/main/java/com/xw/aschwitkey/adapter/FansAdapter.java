package com.xw.aschwitkey.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.FollowBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FansAdapter extends RecyclerView.Adapter<FansAdapter.ViewHolder> {

    private List<FollowBean.ResultBean> list;
    private Activity context;
    private LayoutInflater inflater;
    protected boolean isScrolling = false;
    private boolean isFans;

    public FansAdapter(List<FollowBean.ResultBean> list, Activity context,boolean isFans) {
        this.list = list;
        this.context = context;
        this.isFans = isFans;
        inflater = LayoutInflater.from(context);
    }

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
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
        return new ViewHolder(inflater.inflate(R.layout.item_follow_fans_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String resultString;
        if(isFans){
            holder.mTextView_Nick.setText(list.get(position).getFocusName());
            resultString = list.get(position).getFocusNameImage();
        }else{
            holder.mTextView_Nick.setText(list.get(position).getBeFocusName());
            resultString = list.get(position).getBeFocusNameImage();
        }
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v);
            }
        });

        Glide.with(context)
                .load(resultString)
                .apply(new RequestOptions().placeholder(R.mipmap.default_icon))
                .into(holder.mImageView_Head);
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
        ImageView mImageView_Head;
        TextView mTextView_Nick;
        LinearLayout mLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView_Head = itemView.findViewById(R.id.mImageView_Head);
            mTextView_Nick = itemView.findViewById(R.id.mTextView_Nick);
            mLinearLayout = itemView.findViewById(R.id.mLinearLayout);
        }
    }

    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<FollowBean.ResultBean> addMessageList) {
        //增加数据
        int position = list.size();
        list.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<FollowBean.ResultBean> newList) {
        //刷新数据
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public void UpData(int position) {
        notifyItemChanged(position); //更新
    }
}


