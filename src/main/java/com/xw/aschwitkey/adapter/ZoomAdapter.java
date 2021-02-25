package com.xw.aschwitkey.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.tools.ScreenUtils;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.entity.ImageBean;
import com.xw.aschwitkey.entity.PostBean;
import com.xw.aschwitkey.utils.FormatCurrentData;
import com.xw.aschwitkey.utils.OtherUtils;
import com.xw.aschwitkey.view.CircleImageView;
import com.xw.aschwitkey.view.ExpandTextView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.listener.OnPageChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ZoomAdapter extends RecyclerView.Adapter<ZoomAdapter.ViewHolder> {
    private Context context;
    private List<PostBean.ResultBean> list;
    private LayoutInflater inflater;
    private int width;

    public ZoomAdapter(Context context, List<PostBean.ResultBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);   //获取WindowManager服务
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
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
    public ZoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_zoom_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ZoomAdapter.ViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.mRelativeLayout_banner.getLayoutParams();
        layoutParams.width = width-OtherUtils.dp2px(context,20);
        layoutParams.height = width-OtherUtils.dp2px(context,20);
        holder.mRelativeLayout_banner.setLayoutParams(layoutParams);

        if (list.get(position).getLongitude() != 0d && list.get(position).getLatitude() != 0d) {
            holder.mTextView_location.setVisibility(View.VISIBLE);
            holder.mTextView_location.setText(list.get(position).getBuildingName());
            holder.mTextView_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });
        } else {
            holder.mTextView_location.setVisibility(View.GONE);
        }
        List<String> Jsonimage = new ArrayList<>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(list.get(0).getJsonData());
            holder.mTextView_content.setText(jsonObject.getString("text"));

            if(jsonObject.toString().contains("\"image\":[")){
                JSONArray jsonArray = new JSONArray(jsonObject.getString("image"));
                if (jsonArray.length() == 0) {
                    holder.mRelativeLayout_banner.setVisibility(View.GONE);
                } else {
                    holder.mRelativeLayout_banner.setVisibility(View.VISIBLE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Jsonimage.add(String.valueOf(jsonArray.get(i)));
                    }
                    if (Jsonimage.size() == 1) {
                        holder.mTextView_indicator.setVisibility(View.GONE);
                    } else {
                        holder.mTextView_indicator.setVisibility(View.VISIBLE);
                        holder.mTextView_indicator.setText("1/" + Jsonimage.size());
                    }

                    ImageBean imageBean;
                    List<ImageBean> imageBeans = new ArrayList<>();
                    for (int i = 0; i < Jsonimage.size(); i++) {
                        imageBean = new ImageBean();
                        imageBean.setImg(Jsonimage.get(i));
                        imageBeans.add(imageBean);
                    }
                    holder.banner.setAdapter(new ImageNetAdapter(imageBeans, false)).isAutoLoop(false).addOnPageChangeListener(new OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            if (Jsonimage.size() == 1) {
                                holder.mTextView_indicator.setVisibility(View.GONE);
                            } else {
                                holder.mTextView_indicator.setVisibility(View.VISIBLE);
                                holder.mTextView_indicator.setText((position + 1) + "/" + Jsonimage.size());
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    }).setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(Object data, int p) {
                            OnClickListenerFace.OnClickTemp(p, holder.banner);
                        }
                    }).start();
                }
            }else{
                holder.mRelativeLayout_banner.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_indicator, mTextView_location, mTextView_content;
        Banner banner;
        RelativeLayout mRelativeLayout_banner;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_location = itemView.findViewById(R.id.mTextView_location);
            mTextView_content = itemView.findViewById(R.id.mTextView_content);
            mTextView_indicator = itemView.findViewById(R.id.mTextView_indicator);
            banner = itemView.findViewById(R.id.banner);
            mRelativeLayout_banner = itemView.findViewById(R.id.mRelativeLayout_banner);
        }
    }
}