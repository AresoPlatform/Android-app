package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.BountyTimeBean;
import com.xw.aschwitkey.entity.TagBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
    private Context context;
    private List<TagBean.Result> list;
    private LayoutInflater inflater;

    public TagsAdapter(Context context, List<TagBean.Result> list) {
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
    public TagsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_class_fication_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagsAdapter.ViewHolder holder, int position) {
        holder.mTextView_class.setSelected(list.get(position).isSelect());
        holder.mTextView_class.setText(list.get(position).getChinese());
        holder.mTextView_class.setOnClickListener(new View.OnClickListener() {
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
        TextView mTextView_class;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_class = itemView.findViewById(R.id.mTextView_class);
        }
    }
}
