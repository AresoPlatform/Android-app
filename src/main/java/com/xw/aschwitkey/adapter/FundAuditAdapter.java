package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lihang.ShadowLayout;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.FundBean;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FundAuditAdapter extends RecyclerView.Adapter<FundAuditAdapter.ViewHolder> {
    private Context context;
    private List<FundBean.Fund> list;
    private LayoutInflater inflater;

    public FundAuditAdapter(Context context, List<FundBean.Fund> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public interface OnClickListenerFace {
        void OnClickTemp(int p, View view, int type);
    }

    public OnClickListenerFace OnClickListenerFace;

    public void setOnClickListenerFace(OnClickListenerFace OnClickListenerFace) {
        this.OnClickListenerFace = OnClickListenerFace;
    }

    @NonNull
    @Override
    public FundAuditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_fund_audit_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FundAuditAdapter.ViewHolder holder, int position) {
        holder.mTextView_name.setText(list.get(position).getFundName() + "第" + list.get(position).getFundExpect() + "期");
        if (list.get(position).getReviewStatus() == 0) {
            holder.mTextView_btn.setEnabled(true);
            holder.mTextView_btn.setText("待审核");
            holder.mTextView_btn.setBackground(context.getResources().getDrawable(R.drawable.selector_btn_golden_type));
            holder.mTextView_btn.setTextColor(context.getResources().getColor(R.color.text_color_golden));
            holder.mTextView_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 0);
                }
            });
            holder.mShadowLayout_fund_audit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 0);
                }
            });
        } else if (list.get(position).getReviewStatus() == 1) {
            holder.mTextView_btn.setEnabled(false);
            holder.mTextView_btn.setText("已作废");
            holder.mTextView_btn.setBackground(context.getResources().getDrawable(R.drawable.selector_btn_gray_type));
            holder.mTextView_btn.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            holder.mTextView_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 1);
                }
            });
            holder.mShadowLayout_fund_audit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 1);
                }
            });
        } else if (list.get(position).getReviewStatus() == 2) {
            holder.mTextView_btn.setEnabled(false);
            holder.mTextView_btn.setText("已通过");
            holder.mTextView_btn.setBackground(context.getResources().getDrawable(R.drawable.selector_btn_gray_type));
            holder.mTextView_btn.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            holder.mTextView_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 2);
                }
            });
            holder.mShadowLayout_fund_audit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 2);
                }
            });
        } else if (list.get(position).getReviewStatus() == 3) {
            holder.mTextView_btn.setEnabled(true);
            holder.mTextView_btn.setText("预发布");
            holder.mTextView_btn.setBackground(context.getResources().getDrawable(R.drawable.selector_btn_red_type));
            holder.mTextView_btn.setTextColor(context.getResources().getColor(R.color.text_color_red));
            holder.mTextView_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 3);
                }
            });
            holder.mShadowLayout_fund_audit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 3);
                }
            });
        }
        holder.mTextView_fundShareAmount.setText(list.get(position).getFundShareAmount() + " xas");
        holder.mTextView_totalNumber.setText(list.get(position).getFundShare() + "");
        holder.mTextView_buyStartTime.setText(list.get(position).getBuyStartTime());
        holder.mTextView_endTime.setText(list.get(position).getEndTime());
        holder.mTextView_fundStartAccrual.setText(list.get(position).getFundStartAccrual());
        holder.mTextView_fundEndAccrual.setText(list.get(position).getFundEndAccrual());
        holder.mTextView_rate.setText("分红利率：" + new DecimalFormat("0.0").format(((list.get(position).getFundRate() * 100))) + "%" + "、邀请利率：" + new DecimalFormat("0.0").format(((list.get(position).getFundInviteRate() * 100))) + "%");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_name, mTextView_rate, mTextView_btn, mTextView_fundShareAmount, mTextView_totalNumber, mTextView_buyStartTime, mTextView_endTime, mTextView_fundStartAccrual, mTextView_fundEndAccrual;
        ShadowLayout mShadowLayout_fund_audit;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_name = itemView.findViewById(R.id.mTextView_name);
            mTextView_rate = itemView.findViewById(R.id.mTextView_rate);
            mTextView_btn = itemView.findViewById(R.id.mTextView_btn);
            mTextView_fundShareAmount = itemView.findViewById(R.id.mTextView_fundShareAmount);
            mTextView_totalNumber = itemView.findViewById(R.id.mTextView_totalNumber);
            mTextView_buyStartTime = itemView.findViewById(R.id.mTextView_buyStartTime);
            mTextView_endTime = itemView.findViewById(R.id.mTextView_endTime);
            mTextView_fundStartAccrual = itemView.findViewById(R.id.mTextView_fundStartAccrual);
            mTextView_fundEndAccrual = itemView.findViewById(R.id.mTextView_fundEndAccrual);
            mShadowLayout_fund_audit = itemView.findViewById(R.id.mShadowLayout_fund_audit);
        }
    }
}
