package com.xw.aschwitkey.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.DataBean;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends BannerAdapter<DataBean, ImageAdapter.ImageHolder> {

    public ImageAdapter(List<DataBean> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
    }

    //更新数据
    public void updateData(List<DataBean> data) {
        //这里的代码自己发挥，比如如下的写法等等
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }


    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) BannerUtils.getView(parent, R.layout.banner_image);
        return new ImageHolder(imageView);
    }

    @Override
    public void onBindView(ImageHolder holder, DataBean data, int position, int size) {
        holder.imageView.setImageResource(data.imageRes);
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageHolder(@NonNull View view) {
            super(view);
            this.imageView = (ImageView) view;
        }
    }

}
