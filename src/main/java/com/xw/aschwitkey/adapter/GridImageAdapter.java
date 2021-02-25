package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.listener.OnItemClickListener;
import com.xw.aschwitkey.R;
import com.ypx.imagepicker.bean.ImageItem;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.ViewHolder> {
    public static final String TAG = "PictureSelector";
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private ArrayList<ImageItem> list;
    private int selectMax = 9;
    private boolean flag = false;

    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;

    public interface onAddPicClickListener {
        void onAddPicClick();
    }

    /**
     * 删除
     */
    public void delete(int position) {
        try {

            if (position != RecyclerView.NO_POSITION && list.size() > position) {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GridImageAdapter(Context context, onAddPicClickListener mOnAddPicClickListener, ArrayList<ImageItem> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
        this.list = list;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public ArrayList<ImageItem> getData() {
        return list == null ? new ArrayList<>() : list;
    }

    public void remove(int position) {
        if (list != null && position < list.size()) {
            list.remove(position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        ImageView mIvDel;
        ImageView mImageView_cover;
        TextView tvDuration;

        public ViewHolder(View view) {
            super(view);
            mImg = view.findViewById(R.id.fiv);
            mIvDel = view.findViewById(R.id.iv_del);
            mImageView_cover = view.findViewById(R.id.mImageView_cover);
            tvDuration = view.findViewById(R.id.tv_duration);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_grvidview_layout,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (position == 0) {
            viewHolder.mImageView_cover.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mImageView_cover.setVisibility(View.GONE);
        }
        if (flag) {
            viewHolder.mIvDel.setImageResource(R.mipmap.icon_succeed);
            flag = false;
        } else {
            viewHolder.mIvDel.setImageResource(R.mipmap.icon_delete);
        }
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setImageResource(R.mipmap.add_photo);
            viewHolder.mImg.setOnClickListener(v -> mOnAddPicClickListener.onAddPicClick());
            viewHolder.mIvDel.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mIvDel.setVisibility(View.VISIBLE);
            viewHolder.mIvDel.setOnClickListener(view -> {
                int index = viewHolder.getAdapterPosition();
                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                if (index != RecyclerView.NO_POSITION && list.size() > index) {
                    list.remove(index);
                    notifyItemRemoved(index);
                    notifyItemRangeChanged(index, list.size());
                }
            });
            ImageItem media = list.get(position);
            if (media == null
                    || TextUtils.isEmpty(media.getPath())) {
                return;
            }
            String path;
            if (media.isOriginalImage()) {
                // 裁剪过
                path = media.getCropUrl();
                if (path == null) {
                    path = media.getPath();
                }
            } else {
                // 原图
                path = media.getPath();
            }

            Log.i(TAG, "原图地址::" + media.getPath());

            if (media.isOriginalImage()) {
                Log.i(TAG, "裁剪地址::" + media.getCropUrl());
            }

            Glide.with(viewHolder.itemView.getContext())
                    .load(PictureMimeType.isContent(path) && !media.isOriginalImage() ? Uri.parse(path)
                            : path)
                    .centerCrop()
                    .placeholder(R.color.white)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.mImg);
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(v -> {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    mItemClickListener.onItemClick(v, adapterPosition);
                });
            }

        }
    }

    public void UpItem(int position) {
        flag = true;
        notifyItemChanged(position);
    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mItemClickListener = l;
    }
}