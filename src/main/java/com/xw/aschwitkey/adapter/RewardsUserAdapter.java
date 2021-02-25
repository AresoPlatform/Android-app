package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.RewardsUserBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RewardsUserAdapter extends RecyclerView.Adapter<RewardsUserAdapter.ViewHolder>{
    private Context context;
    private List<RewardsUserBean> list;
    private LayoutInflater inflater;

    public RewardsUserAdapter(Context context, List<RewardsUserBean> list) {
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
    public RewardsUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_user_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsUserAdapter.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView_head;
        TextView mTextView_username;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView_head = itemView.findViewById(R.id.mImageView_head);
            mTextView_username = itemView.findViewById(R.id.mTextView_username);
        }
    }
}