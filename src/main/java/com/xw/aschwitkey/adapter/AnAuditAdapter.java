package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.AnAuditBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnAuditAdapter extends RecyclerView.Adapter<AnAuditAdapter.ViewHolder> {
    private Context context;
    private List<AnAuditBean.Notice> list;
    private LayoutInflater inflater;

    public AnAuditAdapter(Context context, List<AnAuditBean.Notice> list) {
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
    public AnAuditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_an_audit_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnAuditAdapter.ViewHolder holder, int position) {
        holder.mTextView_title.setText(list.get(position).getTitle());
        if (list.get(position).getReviewStatus() == 2) {
            holder.mTextView_type.setText("已通过");
            holder.mTextView_type.setTextColor(context.getResources().getColor(R.color.text_bg));
        } else if (list.get(position).getReviewStatus() == 1) {
            holder.mTextView_type.setText("已作废");
            holder.mTextView_type.setTextColor(context.getResources().getColor(R.color.text_color_gray));
        } else if (list.get(position).getReviewStatus() == 0) {
            holder.mTextView_type.setText("待审核");
            holder.mTextView_type.setTextColor(context.getResources().getColor(R.color.text_color_red));
        }

        Glide.with(context)
                .load(list.get(position).getCoverImg())
                .into(holder.mImageView_cover);

        holder.mRelativeLayout_an_audit.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        ImageView imageView = holder.mImageView_cover;
        if (imageView != null) {
            Glide.with(context).clear(imageView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_title, mTextView_type;
        ImageView mImageView_cover;
        RelativeLayout mRelativeLayout_an_audit;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_title = itemView.findViewById(R.id.mTextView_title);
            mTextView_type = itemView.findViewById(R.id.mTextView_type);
            mImageView_cover = itemView.findViewById(R.id.mImageView_cover);
            mRelativeLayout_an_audit = itemView.findViewById(R.id.mRelativeLayout_an_audit);
        }
    }
}
