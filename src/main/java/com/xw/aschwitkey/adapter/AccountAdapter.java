package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.DBHelperBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private Context context;
    private List<DBHelperBean> list;
    private LayoutInflater inflater;

    public AccountAdapter(Context context, List<DBHelperBean> list) {
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
    public AccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_account_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapter.ViewHolder holder, int position) {
        holder.mTextView_name.setText(list.get(position).getAccount());
        holder.mTextView_address.setText(list.get(position).getAddress());
        holder.mTextView_delete.setTag(position);
        if (list.get(position).isSelect()) {
            holder.mRelativeLayout_account.setSelected(true);
            holder.mTextView_name.setTextColor(context.getResources().getColor(R.color.text_bg));
            holder.mImageView_select.setImageResource(R.mipmap.selected);
        } else {
            holder.mRelativeLayout_account.setSelected(false);
            holder.mTextView_name.setTextColor(context.getResources().getColor(R.color.text_bg_gray));
            holder.mImageView_select.setImageResource(R.mipmap.unselected);
        }

        holder.mRelativeLayout_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v);
            }
        });

        holder.mTextView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp((Integer) v.getTag(), v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mRelativeLayout_account;
        TextView mTextView_name, mTextView_address, mTextView_delete;
        ImageView mImageView_select;

        public ViewHolder(View itemView) {
            super(itemView);
            mRelativeLayout_account = itemView.findViewById(R.id.mRelativeLayout_account);
            mTextView_name = itemView.findViewById(R.id.mTextView_name);
            mTextView_address = itemView.findViewById(R.id.mTextView_address);
            mImageView_select = itemView.findViewById(R.id.mImageView_select);
            mTextView_delete = itemView.findViewById(R.id.mTextView_delete);
        }
    }
}