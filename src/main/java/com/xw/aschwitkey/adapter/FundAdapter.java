package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lihang.ShadowLayout;
import com.xw.aschwitkey.FloorCountDownLib.Center;
import com.xw.aschwitkey.FloorCountDownLib.ICountDownCenter;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.entity.FundBean;
import com.xw.aschwitkey.entity.TimeBean;
import com.xw.aschwitkey.utils.OtherUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FundAdapter extends RecyclerView.Adapter<FundAdapter.ViewHolder> {
    private Context context;
    private List<FundBean.Fund> list;
    private LayoutInflater inflater;
    private ICountDownCenter countDownCenter;

    public FundAdapter(Context context, List<FundBean.Fund> list, ICountDownCenter countDownCenter) {
        this.context = context;
        this.list = list;
        this.countDownCenter = countDownCenter;
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
    public FundAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fund_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        countDownCenter.addObserver(viewHolder);
        countDownCenter.startCountdown();
        return new ViewHolder(inflater.inflate(R.layout.item_fund_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FundAdapter.ViewHolder holder, int position) {
        try {
            if (list.get(position).getFundShare() - list.get(position).getSellAmount() == 0 || (OtherUtils.dateToStamp(list.get(position).getEndTime()) - (System.currentTimeMillis() + Content.timePoor)) <= 0) {
                holder.mTextView_type.setText("待分红");
                holder.mLinearLayout_endDay.setVisibility(View.GONE);
                holder.mTextView_btn.setEnabled(false);
                holder.mTextView_type.setTextColor(context.getResources().getColor(R.color.text_color_golden));
                holder.mTextView_type.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_golden));
                holder.mTextView_btn.setBackground(context.getResources().getDrawable(R.drawable.selector_btn_gray_type));
                holder.mTextView_btn.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            } else {
                holder.mTextView_type.setText("认购中");
                holder.mLinearLayout_endDay.setVisibility(View.VISIBLE);
                holder.mLinearLayout_accrualDay.setVisibility(View.VISIBLE);
                holder.mTextView_btn.setEnabled(true);
                holder.mTextView_type.setTextColor(context.getResources().getColor(R.color.text_color_red));
                holder.mTextView_type.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_red));
                holder.mTextView_btn.setBackground(context.getResources().getDrawable(R.drawable.selector_btn_golden_type));
                holder.mTextView_btn.setTextColor(context.getResources().getColor(R.color.text_color_golden));
            }
            holder.mTextView_name.setText(list.get(position).getFundName() + "第" + list.get(position).getFundExpect() + "期");
            holder.mTextView_rate.setText("分红利率：" + new DecimalFormat("0.0").format((list.get(position).getFundRate() * 100)) + "%" + "、邀请利率：" + new DecimalFormat("0.0").format((list.get(position).getFundInviteRate() * 100)) + "%");
            holder.mTextView_sellAmount.setText("您已购" + list.get(position).getBuyAmount() + "份");
            holder.mTextView_fundShareAmount.setText(list.get(position).getFundShareAmount() + " xas");
            holder.mTextView_fundRemaining.setText((list.get(position).getFundShare() - list.get(position).getSellAmount()) + "份");
            holder.mTextView_endTime.setText(OtherUtils.timedate(OtherUtils.dateToStamp(list.get(position).getEndTime()), "yyyy-MM-dd HH:mm"));
            if ((OtherUtils.dateToStamp(list.get(position).getEndTime()) - (System.currentTimeMillis() + Content.timePoor)) / 1000 > 60 * 60 * 24) {
                holder.mTextView_endDay.setText(((OtherUtils.dateToStamp(list.get(position).getEndTime()) - (System.currentTimeMillis() + Content.timePoor)) / (1000 * 60 * 60 * 24)) + "");
                holder.mTextView_endText.setText(" 天");
            } else if ((OtherUtils.dateToStamp(list.get(position).getEndTime()) - (System.currentTimeMillis() + Content.timePoor)) / 1000 > 60 * 60) {
                holder.mTextView_endDay.setText(((OtherUtils.dateToStamp(list.get(position).getEndTime()) - (System.currentTimeMillis() + Content.timePoor)) / (1000 * 60 * 60)) + "");
                holder.mTextView_endText.setText(" 小时");
            } else {
                holder.lastBindPositon = position;
                TimeBean timeBean = new TimeBean(((OtherUtils.dateToStamp(list.get(position).getEndTime()) - (System.currentTimeMillis() + Content.timePoor)) / 1000));
                holder.timeBean = timeBean;
                bindCountView(holder, context);
                if (!countDownCenter.containHolder(holder)) {
                    countDownCenter.addObserver(holder);
                }
                holder.mTextView_endText.setText(" 分钟");
            }
            holder.mTextView_fundStartAccrual.setText(OtherUtils.timedate(OtherUtils.dateToStamp(list.get(position).getFundStartAccrual()), "yyyy-MM-dd HH:mm"));
            if ((OtherUtils.dateToStamp(list.get(position).getFundStartAccrual()) - (System.currentTimeMillis() + Content.timePoor)) / 1000 > 60 * 60 * 24) {
                holder.mTextView_accrualDay.setText(((OtherUtils.dateToStamp(list.get(position).getFundStartAccrual()) - (System.currentTimeMillis() + Content.timePoor)) / (1000 * 60 * 60 * 24)) + "");
                holder.mTextView_accrualText.setText(" 天");
            } else if ((OtherUtils.dateToStamp(list.get(position).getFundStartAccrual()) - (System.currentTimeMillis() + Content.timePoor)) / 1000 > 60 * 60) {
                holder.mTextView_accrualDay.setText(((OtherUtils.dateToStamp(list.get(position).getFundStartAccrual()) - (System.currentTimeMillis() + Content.timePoor)) / (1000 * 60 * 60)) + "");
                holder.mTextView_accrualText.setText(" 小时");
            } else {
                holder.mTextView_accrualDay.setText(((OtherUtils.dateToStamp(list.get(position).getFundStartAccrual()) - (System.currentTimeMillis() + Content.timePoor)) / (1000 * 60)) + "");
                holder.mTextView_accrualText.setText(" 分钟");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mTextView_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListenerFace.OnClickTemp(position, v);
            }
        });
        holder.mShadowLayout_top.setOnClickListener(new View.OnClickListener() {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements Observer {
        int lastBindPositon = -1;
        TimeBean timeBean;

        TextView mTextView_name, mTextView_type, mTextView_rate, mTextView_btn, mTextView_sellAmount, mTextView_fundShareAmount, mTextView_fundRemaining, mTextView_endTime, mTextView_endText, mTextView_endDay, mTextView_fundStartAccrual, mTextView_accrualDay, mTextView_accrualText;
        ShadowLayout mShadowLayout_top;
        LinearLayout mLinearLayout_endDay, mLinearLayout_accrualDay;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_name = itemView.findViewById(R.id.mTextView_name);
            mTextView_type = itemView.findViewById(R.id.mTextView_type);
            mTextView_rate = itemView.findViewById(R.id.mTextView_rate);
            mTextView_btn = itemView.findViewById(R.id.mTextView_btn);
            mTextView_sellAmount = itemView.findViewById(R.id.mTextView_sellAmount);
            mTextView_fundShareAmount = itemView.findViewById(R.id.mTextView_fundShareAmount);
            mTextView_fundRemaining = itemView.findViewById(R.id.mTextView_fundRemaining);
            mTextView_endTime = itemView.findViewById(R.id.mTextView_endTime);
            mTextView_endDay = itemView.findViewById(R.id.mTextView_endDay);
            mTextView_endText = itemView.findViewById(R.id.mTextView_endText);
            mTextView_fundStartAccrual = itemView.findViewById(R.id.mTextView_fundStartAccrual);
            mTextView_accrualDay = itemView.findViewById(R.id.mTextView_accrualDay);
            mShadowLayout_top = itemView.findViewById(R.id.mShadowLayout_top);
            mLinearLayout_endDay = itemView.findViewById(R.id.mLinearLayout_endDay);
            mLinearLayout_accrualDay = itemView.findViewById(R.id.mLinearLayout_accrualDay);
            mTextView_accrualText = itemView.findViewById(R.id.mTextView_accrualText);
        }

        @Override
        public void update(Observable o, Object arg) {

            if (arg != null && arg instanceof Center.PostionFL) {
                Center.PostionFL postionFL = (Center.PostionFL) arg;
                if (lastBindPositon >= postionFL.frist && lastBindPositon <= postionFL.last) {
                    Log.e("lmtlmtupdate1", "update" + lastBindPositon);
                    bindCountView(this, context);
                }
            }


        }
    }

    //倒计时展示和结束逻辑
    private static void bindCountView(FundAdapter.ViewHolder holder, Context context) {
        if (holder.timeBean == null) return;
        //倒计时结束
        if (new BigDecimal(holder.timeBean.getRainTime()).divide(new BigDecimal(60),2,BigDecimal.ROUND_HALF_UP).doubleValue() <= 0d) {
            holder.mTextView_type.setText("已结束");
            holder.mLinearLayout_endDay.setVisibility(View.GONE);
            holder.mTextView_btn.setEnabled(false);
            holder.mTextView_type.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            holder.mTextView_type.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_gray));
            holder.mTextView_btn.setBackground(context.getResources().getDrawable(R.drawable.selector_btn_gray_type));
            holder.mTextView_btn.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            return;
        }

        if (new BigDecimal(holder.timeBean.getRainTime()).divide(new BigDecimal(60),2,BigDecimal.ROUND_HALF_UP).doubleValue() <= 1d) {
            holder.mTextView_endDay.setText("1");
        } else {
            holder.mTextView_endDay.setText((int) (holder.timeBean.getRainTime() / 60) + "");
        }


    }

}
