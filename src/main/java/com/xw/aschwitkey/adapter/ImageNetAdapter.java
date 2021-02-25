package com.xw.aschwitkey.adapter;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.AnAuditBean;
import com.xw.aschwitkey.entity.ImageBean;
import com.youth.banner.util.BannerUtils;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义布局，网络图片
 */
public class ImageNetAdapter extends BannerAdapter<ImageBean, ImageNetAdapter.ImageHolder> {

    private boolean isAn;

    public ImageNetAdapter(List<ImageBean> mDatas, boolean isAn) {
        super(mDatas);
        this.isAn = isAn;
    }

    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) BannerUtils.getView(parent, R.layout.banner_image);
        //通过裁剪实现圆角
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BannerUtils.setBannerRound(imageView, 20);
        }
        return new ImageHolder(imageView);
    }

    @Override
    public void onBindView(ImageHolder holder, ImageBean data, int position, int size) {
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(holder.itemView)
                .load(data.getImg())
                .into(holder.imageView);
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageHolder(@NonNull View view) {
            super(view);
            this.imageView = (ImageView) view;
        }
    }

}
