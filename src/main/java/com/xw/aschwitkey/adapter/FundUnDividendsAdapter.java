package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xw.aschwitkey.FloorCountDownLib.Center;
import com.xw.aschwitkey.FloorCountDownLib.ICountDownCenter;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.entity.FundDividendsBean;
import com.xw.aschwitkey.entity.TimeBean;
import com.xw.aschwitkey.utils.OtherUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FundUnDividendsAdapter extends RecyclerView.Adapter<FundUnDividendsAdapter.ViewHolder> {
    private Context context;
    private List<FundDividendsBean.FundDividends> list;
    private boolean isCouncil = false;
    private LayoutInflater inflater;
    private ICountDownCenter countDownCenter;

    public FundUnDividendsAdapter(Context context, List<FundDividendsBean.FundDividends> list, ICountDownCenter countDownCenter) {
        this.context = context;
        this.list = list;
        this.countDownCenter = countDownCenter;
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
    public FundUnDividendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fund_un_dividends_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        countDownCenter.addObserver(viewHolder);
        countDownCenter.startCountdown();
        return new ViewHolder(inflater.inflate(R.layout.item_fund_un_dividends_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FundUnDividendsAdapter.ViewHolder holder, int position) {
        holder.mTextView_name.setText(list.get(position).getFundName() + "第" + list.get(position).getFundExpect() + "期");
        try {
            if (OtherUtils.dateToStamp(list.get(position).getEndTime()) - (System.currentTimeMillis() + Content.timePoor) <= 0) {
                if (isCouncil) {
                    if ((OtherUtils.dateToStamp(list.get(position).getFundStartAccrual()) - (System.currentTimeMillis() + Content.timePoor)) / 1000 > 60 * 60 * 24) {
                        holder.mTextView_time_text.setText("距分红：");
                        holder.mTextView_time.setText(((OtherUtils.dateToStamp(list.get(position).getFundStartAccrual()) - (System.currentTimeMillis() + Content.timePoor)) / (1000 * 60 * 60 * 24)) + "天");
                    } else if ((OtherUtils.dateToStamp(list.get(position).getFundStartAccrual()) - (System.currentTimeMillis() + Content.timePoor)) / 1000 > 0) {
                        holder.mTextView_time_text.setText("距分红：");
                        holder.lastBindPositon = position;
                        TimeBean timeBean = new TimeBean((OtherUtils.dateToStamp(list.get(position).getFundStartAccrual()) - (System.currentTimeMillis() + Content.timePoor)) / 1000);
                        holder.timeBean = timeBean;
                        bindCountView(holder.mTextView_time, holder.timeBean);
                        if (!countDownCenter.containHolder(holder)) {
                            countDownCenter.addObserver(holder);
                        }
                    } else {
                        holder.mTextView_time_text.setText("已分红");
                    }
                } else {
                    if ((OtherUtils.dateToStamp(list.get(position).getMakeTime()) - (System.currentTimeMillis() + Content.timePoor)) / 1000 > 60 * 60 * 24) {
                        holder.mTextView_time_text.setText("距分红：");
                        holder.mTextView_time.setText(((OtherUtils.dateToStamp(list.get(position).getMakeTime()) - (System.currentTimeMillis() + Content.timePoor)) / (1000 * 60 * 60 * 24)) + "天");
                    } else if ((OtherUtils.dateToStamp(list.get(position).getMakeTime()) - (System.currentTimeMillis() + Content.timePoor)) / 1000 > 0) {
                        holder.mTextView_time_text.setText("距分红：");
                        holder.lastBindPositon = position;
                        TimeBean timeBean = new TimeBean((OtherUtils.dateToStamp(list.get(position).getMakeTime()) - (System.currentTimeMillis() + Content.timePoor)) / 1000);
                        holder.timeBean = timeBean;
                        bindCountView(holder.mTextView_time, holder.timeBean);
                        if (!countDownCenter.containHolder(holder)) {
                            countDownCenter.addObserver(holder);
                        }
                    } else {
                        holder.mTextView_time_text.setText("已分红");
                    }
                }
            } else if (list.get(position).getFundShare() - list.get(position).getSellAmount() == 0) {
                holder.mTextView_time_text.setText("认购已结束");
                holder.mTextView_time.setText("");
            } else {
                if ((OtherUtils.dateToStamp(list.get(position).getEndTime()) - (System.currentTimeMillis() + Content.timePoor)) / 1000 > 60 * 60 * 24) {
                    holder.mTextView_time_text.setText("距认购结束：");
                    holder.mTextView_time.setText(((OtherUtils.dateToStamp(list.get(position).getEndTime()) - (System.currentTimeMillis() + Content.timePoor)) / (1000 * 60 * 60 * 24)) + "天");
                } else if ((OtherUtils.dateToStamp(list.get(position).getEndTime()) - (System.currentTimeMillis() + Content.timePoor)) / 1000 > 0) {
                    holder.mTextView_time_text.setText("距认购结束：");
                    holder.lastBindPositon = position;
                    TimeBean timeBean = new TimeBean((OtherUtils.dateToStamp(list.get(position).getEndTime()) - (System.currentTimeMillis() + Content.timePoor)) / 1000);
                    holder.timeBean = timeBean;
                    bindCountView(holder.mTextView_time, holder.timeBean);
                    if (!countDownCenter.containHolder(holder)) {
                        countDownCenter.addObserver(holder);
                    }
                }
            }

        } catch (ParseException e) {
        }

        if (isCouncil) {
            holder.mTextView_allMany_text.setText("认购总额");
            holder.mTextView_totalAmount_text.setText("剩余份额");
            holder.mTextView_sellAmount.setVisibility(View.VISIBLE);
            holder.mTextView_allMany.setText(list.get(position).getFundShare() * list.get(position).getFundShareAmount() + "");
            holder.mTextView_allMany_many.setText(list.get(position).getFundShareAmount() + " × " + list.get(position).getFundShare());
            holder.mTextView_sellAmount.setText(list.get(position).getFundShare() - list.get(position).getSellAmount() + "");
            holder.mTextView_totalAmount.setText("/" + list.get(position).getFundShare());
            holder.mRelativeLayout_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 1);
                }
            });
        } else {
            holder.mTextView_allMany_text.setText("已购基金");
            holder.mTextView_totalAmount_text.setText("分红利息");
            holder.mTextView_sellAmount.setVisibility(View.GONE);
            holder.mTextView_allMany.setText(list.get(position).getBuyAmount() * list.get(position).getFundShareAmount() + "");
            holder.mTextView_allMany_many.setText(list.get(position).getFundShareAmount() + " × " + list.get(position).getBuyAmount());
            holder.mTextView_totalAmount.setText(list.get(position).getFundRate().multiply(new BigDecimal((list.get(position).getBuyAmount() * list.get(position).getFundShareAmount()))).doubleValue() + "");
            holder.mRelativeLayout_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v, 2);
                }
            });
        }
        holder.mTextView_fundsRate.setText(list.get(position).getFundRate().multiply(new BigDecimal("100")).doubleValue() + "%");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setCouncil(boolean isCouncil) {
        this.isCouncil = isCouncil;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements Observer {
        int lastBindPositon = -1;
        TimeBean timeBean;

        TextView mTextView_name, mTextView_time_text, mTextView_time, mTextView_allMany_text, mTextView_allMany, mTextView_allMany_many, mTextView_fundsRate, mTextView_totalAmount_text, mTextView_sellAmount, mTextView_totalAmount;
        RelativeLayout mRelativeLayout_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_name = itemView.findViewById(R.id.mTextView_name);
            mTextView_time_text = itemView.findViewById(R.id.mTextView_time_text);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_allMany_text = itemView.findViewById(R.id.mTextView_allMany_text);
            mTextView_allMany = itemView.findViewById(R.id.mTextView_allMany);
            mTextView_allMany_many = itemView.findViewById(R.id.mTextView_allMany_many);
            mTextView_fundsRate = itemView.findViewById(R.id.mTextView_fundsRate);
            mTextView_totalAmount_text = itemView.findViewById(R.id.mTextView_totalAmount_text);
            mTextView_sellAmount = itemView.findViewById(R.id.mTextView_sellAmount);
            mTextView_totalAmount = itemView.findViewById(R.id.mTextView_totalAmount);
            mRelativeLayout_btn = itemView.findViewById(R.id.mRelativeLayout_btn);
        }

        @Override
        public void update(Observable o, Object arg) {

            if (arg != null && arg instanceof Center.PostionFL) {
                Center.PostionFL postionFL = (Center.PostionFL) arg;
                if (lastBindPositon >= postionFL.frist && lastBindPositon <= postionFL.last) {
                    Log.e("lmtlmtupdate1", "update" + lastBindPositon);
                    bindCountView(mTextView_time, timeBean);
                }
            }


        }

    }

    //倒计时展示和结束逻辑
    private static void bindCountView(TextView textView, TimeBean timeBean) {
        if (timeBean == null) return;
        //倒计时结束
        if (timeBean.getRainTime() <= 0) {
            textView.setText("");
            return;
        }


        textView.setText(OtherUtils.secToTime((int) timeBean.getRainTime()));

    }
}