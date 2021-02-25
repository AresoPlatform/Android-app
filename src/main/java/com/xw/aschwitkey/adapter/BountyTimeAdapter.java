package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.AgentHistoryBean;
import com.xw.aschwitkey.entity.BountyTimeBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BountyTimeAdapter extends RecyclerView.Adapter<BountyTimeAdapter.ViewHolder> {
    private Context context;
    private List<BountyTimeBean.Result> list;
    private LayoutInflater inflater;

    public BountyTimeAdapter(Context context, List<BountyTimeBean.Result> list) {
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
    public BountyTimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_bounty_time_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BountyTimeAdapter.ViewHolder holder, int position) {
        if (position == list.size() - 1) {
            holder.mTextView_line.setVisibility(View.GONE);
        } else {
            holder.mTextView_line.setVisibility(View.VISIBLE);
        }
        holder.mTextView_year.setText(list.get(position).getYear());
        holder.mTextView_year.setOnClickListener(new View.OnClickListener() {
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
        TextView mTextView_year, mTextView_line;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_year = itemView.findViewById(R.id.mTextView_year);
            mTextView_line = itemView.findViewById(R.id.mTextView_line);
        }
    }
}
