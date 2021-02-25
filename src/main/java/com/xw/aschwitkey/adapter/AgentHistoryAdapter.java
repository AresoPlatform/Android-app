package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.AgentHistoryBean;
import com.xw.aschwitkey.entity.RewardsHistoryBean;
import com.xw.aschwitkey.utils.FormatDataForDisplay;
import com.xw.aschwitkey.utils.FormatDataForMessage;
import com.xw.aschwitkey.utils.OtherUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AgentHistoryAdapter extends RecyclerView.Adapter<AgentHistoryAdapter.ViewHolder> {
    private Context context;
    private List<AgentHistoryBean.Result> list;
    private LayoutInflater inflater;

    public AgentHistoryAdapter(Context context, List<AgentHistoryBean.Result> list) {
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
    public AgentHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_agent_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AgentHistoryAdapter.ViewHolder holder, int position) {
        holder.mTextView_voteNumber.setText("+ " + list.get(position).getVoteCount().setScale(0, BigDecimal.ROUND_DOWN).toString());
        holder.mTextView_postNumber.setText("+ " + list.get(position).getPostCount().setScale(0, BigDecimal.ROUND_DOWN).toString());
        holder.mTextView_freeze.setText("- " + list.get(position).getBlackXas().setScale(3, BigDecimal.ROUND_DOWN).toString());
        holder.mTextView_time.setText(list.get(position).getDelegatorTime());
        holder.mTextView_endTime.setText(list.get(position).getExpireTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_voteNumber, mTextView_postNumber, mTextView_freeze, mTextView_time, mTextView_endTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_voteNumber = itemView.findViewById(R.id.mTextView_voteNumber);
            mTextView_postNumber = itemView.findViewById(R.id.mTextView_postNumber);
            mTextView_freeze = itemView.findViewById(R.id.mTextView_freeze);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_endTime = itemView.findViewById(R.id.mTextView_endTime);
        }
    }
}
