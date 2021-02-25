package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.PurchasedBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PurchasedAdapter extends RecyclerView.Adapter<PurchasedAdapter.ViewHolder> {
    private Context context;
    private List<PurchasedBean.Purchased> list;
    private LayoutInflater inflater;

    public PurchasedAdapter(Context context, List<PurchasedBean.Purchased> list) {
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
    public PurchasedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_purchased_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasedAdapter.ViewHolder holder, int position) {
        holder.mTextView_fundName.setText(list.get(position).getFundName()+"第"+list.get(position).getFundExpect()+"期");
        holder.mTextView_purchasedNumber.setText("只需加购 " + list.get(position).getPurchasedAmount() + " 份");
        holder.mTextView_invitedRewards.setText(list.get(position).getAwardAmount() + "");
        holder.mTextView_btn_invite.setOnClickListener(new View.OnClickListener() {
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
        TextView mTextView_fundName, mTextView_purchasedNumber, mTextView_invitedRewards, mTextView_btn_invite;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_fundName = itemView.findViewById(R.id.mTextView_fundName);
            mTextView_purchasedNumber = itemView.findViewById(R.id.mTextView_purchasedNumber);
            mTextView_invitedRewards = itemView.findViewById(R.id.mTextView_invitedRewards);
            mTextView_btn_invite = itemView.findViewById(R.id.mTextView_btn_invite);
        }
    }
}