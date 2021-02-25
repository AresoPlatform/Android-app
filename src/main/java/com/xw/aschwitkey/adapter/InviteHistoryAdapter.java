package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.InviteHistoryBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InviteHistoryAdapter extends RecyclerView.Adapter<InviteHistoryAdapter.ViewHolder> {
    private Context context;
    private List<InviteHistoryBean.HistoryBean> list;
    private LayoutInflater inflater;

    public InviteHistoryAdapter(Context context, List<InviteHistoryBean.HistoryBean> list) {
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
    public InviteHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_invite_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InviteHistoryAdapter.ViewHolder holder, int position) {
        if (list.get(position).getNickName().isEmpty()) {
            holder.mTextView_username.setText(list.get(position).getPhone().substring(0, 3) + "****" + list.get(position).getPhone().substring(7, list.get(position).getPhone().length()));
        } else {
            holder.mTextView_username.setText(list.get(position).getPhone().substring(0, 3) + "****" + list.get(position).getPhone().substring(7, list.get(position).getPhone().length()) + "(" + list.get(position).getNickName() + ")");
        }
        holder.mTextView_time.setText(list.get(position).getCreatetime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView_username, mTextView_time;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_username = itemView.findViewById(R.id.mTextView_username);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
        }
    }
}
