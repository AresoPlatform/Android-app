package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.xw.aschwitkey.R;

import java.util.List;

public class AddRessAdapter extends BaseAdapter {

    private Context context;
    private List<PoiInfo> list;
    private LayoutInflater inflater;
    private int notifyTip ;
    protected boolean isLocation = false;

    public AddRessAdapter(Context context, List<PoiInfo> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
        notifyTip = -1 ;
    }

    public void setScrolling(boolean isLocations) {
        isLocation = isLocations;
    }

    public void setNotifyTip(int notifyTip) {
        this.notifyTip = notifyTip;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_address_layout,null);
            viewHolder.mTextView_A=convertView.findViewById(R.id.mTextView_A);
            viewHolder.mTextView_B=convertView.findViewById(R.id.mTextView_B);
            viewHolder.mTextView_clear = convertView.findViewById(R.id.mTextView_clear);
            viewHolder.mImageView=convertView.findViewById(R.id.mImageView);

            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        if (position==0){
            viewHolder.mTextView_clear.setVisibility(View.VISIBLE);
            viewHolder.mTextView_A.setVisibility(View.GONE);
            viewHolder.mTextView_B.setVisibility(View.GONE);
        }else {
            viewHolder.mTextView_A.setText(list.get(position).getName());
            viewHolder.mTextView_B.setText(list.get(position).getAddress());
            viewHolder.mTextView_clear.setVisibility(View.GONE);
            viewHolder.mTextView_A.setVisibility(View.VISIBLE);
            viewHolder.mTextView_B.setVisibility(View.VISIBLE);
        }

        if(!isLocation){
            if(position == 0){
                viewHolder.mImageView.setVisibility(View.VISIBLE);
            }else{
                viewHolder.mImageView.setVisibility(View.GONE);
            }
        }else{
            if(position == 1){
                viewHolder.mImageView.setVisibility(View.VISIBLE);
            }else{
                viewHolder.mImageView.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    class ViewHolder {

        TextView mTextView_A,mTextView_B,mTextView_clear;
        ImageView mImageView;
    }
}
