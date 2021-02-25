package com.xw.aschwitkey.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.luck.picture.lib.tools.ScreenUtils;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.Content;
import com.xw.aschwitkey.entity.ImageBean;
import com.xw.aschwitkey.entity.PostBean;
import com.xw.aschwitkey.entity.PurchasedBean;
import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.http.OkHttpClient;
import com.xw.aschwitkey.utils.FormatCurrentData;
import com.xw.aschwitkey.utils.SPUtils;
import com.xw.aschwitkey.utils.ToastUtil;
import com.xw.aschwitkey.view.CircleImageView;
import com.xw.aschwitkey.view.ExpandTextView;
import com.xw.aschwitkey.view.TypefaceTextView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnPageChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BBSAdapter extends RecyclerView.Adapter<BBSAdapter.ViewHolder> {
    private Activity context;
    private List<PostBean.ResultBean> list;
    private LayoutInflater inflater;
    protected boolean isScrolling = false;
    private SPUtils spUtils;
    private int type;

    public BBSAdapter(Activity context, List<PostBean.ResultBean> list, int type) {
        this.context = context;
        this.list = list;
        spUtils = new SPUtils(context, "AschWallet");
        this.type = type;
        inflater = LayoutInflater.from(context);
    }

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
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
    public BBSAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_bbs_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BBSAdapter.ViewHolder holder, int position) {
        if (!list.get(position).isAschWk()) {
            holder.setVisibility(false);
        } else {
            holder.setVisibility(true);
            if (type == 0) {
                holder.mLinearLayout_bottom.setVisibility(View.VISIBLE);
                //设置时间
                holder.mTextView_time.setTextColor(context.getResources().getColor(R.color.TextColor1));
                holder.mTextView_time.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_11));
                holder.mTextView_time.setTypeFace(context, "regular");
                holder.mTextView_time.setText(FormatCurrentData.getTimeRange(context, list.get(position).getCreated()));
            } else {
                holder.mLinearLayout_bottom.setVisibility(View.GONE);
                holder.mTextView_time.setTextColor(context.getResources().getColor(R.color.text_color_golden));
                holder.mTextView_time.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_12));
                holder.mTextView_time.setTypeFace(context, "heavy");
                if (type == 1) {
                    holder.mTextView_time.setText("≈ " + list.get(position).getPostValueXas().add(list.get(position).getXasAmount()).setScale(3, BigDecimal.ROUND_DOWN).toString() + " xas");
                } else if (type == 2) {
                    holder.mTextView_time.setText("≈ " + list.get(position).getPostValueXas().add(list.get(position).getXasAmount()).setScale(3, BigDecimal.ROUND_DOWN).toString() + " xas");
                } else if (type == 3) {
                    holder.mTextView_time.setText(list.get(position).getXasAmount().setScale(3, BigDecimal.ROUND_DOWN).toString() + " xas");
                }
            }
            holder.mLottieAnimationView_reward.setAnimation(R.raw.award);
            holder.mLottieAnimationView_reward.setRepeatCount(-1);
            holder.mLottieAnimationView_reward.setSpeed(1.5f);
            holder.mLottieAnimationView_reward.playAnimation();
            holder.mLottieAnimationView_comment.setAnimation(R.raw.comment);
            holder.mLottieAnimationView_comment.setProgress(0f);
            if (list.get(position).isComment()) {
                holder.mLottieAnimationView_comment.playAnimation();
            } else {
                holder.mLottieAnimationView_comment.setProgress(0f);
            }
            holder.mLottieAnimationView_upVote.setAnimation(R.raw.like);

            //设置昵称
            holder.mTextView_nickname.setText(list.get(position).getAuthor());
            holder.mTextView_comment.setText(list.get(position).getChildren() + "");
            holder.mTextView_title.setText(list.get(position).getTitle());
            //设置喜欢数
            int likeCount = 0;
            for (int i = 0; i < list.get(position).getActive_votes().size(); i++) {
                if (list.get(position).getActive_votes().get(i).getPercent() != 0) {
                    likeCount++;
                }
            }
            holder.mProgressBar.setVisibility(View.GONE);
            holder.mLottieAnimationView_upVote.setVisibility(View.VISIBLE);
            holder.mLottieAnimationView_upVote.setProgress(0f);
            holder.mLottieAnimationView_upVote.setTag("select");
            for (int i = 0; i < list.get(position).getActive_votes().size(); i++) {
                if (list.get(position).getActive_votes().get(i).getVoter().equals(spUtils.getString("nickname"))
                        && list.get(position).getActive_votes().get(i).getPercent() != 0) {
                    holder.mLottieAnimationView_upVote.playAnimation();
                    holder.mLottieAnimationView_upVote.setTag("unselect");
                    break;
                } else {
                    holder.mLottieAnimationView_upVote.setProgress(0f);
                    holder.mLottieAnimationView_upVote.setTag("select");
                }
            }
            holder.mTextView_comment.setText(list.get(position).getCommentNum() + "");
            if (list.get(position).getPostValueXas().doubleValue() >= 10000d) {
                holder.mTextView_xas.setText("≈ " + list.get(position).getPostValueXas().setScale(0, BigDecimal.ROUND_DOWN).toString() + " xas");
            } else {
                holder.mTextView_xas.setText("≈ " + list.get(position).getPostValueXas().setScale(3, BigDecimal.ROUND_DOWN).toString() + " xas");
            }
            if (list.get(position).getXasAmount().doubleValue() >= 10000d) {
                holder.mTextView_reward.setText(list.get(position).getXasAmount().setScale(1, BigDecimal.ROUND_DOWN).toString() + "w");
            } else {
                holder.mTextView_reward.setText(list.get(position).getXasAmount().setScale(0, BigDecimal.ROUND_DOWN).toString());
            }
            holder.mTextView_upVote.setText(likeCount + "");

            holder.mImageView_head.setImageResource(R.mipmap.default_icon);

            Glide.with(context)
                    .load(list.get(position).getHeadImage())
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.default_icon)
                            .skipMemoryCache(true)
                            .dontAnimate())
                    .error(R.mipmap.default_icon)
                    .into(holder.mImageView_head);

            holder.mImageView.setColorFilter(0x4c000000);
            holder.mImageView.setImageResource(R.mipmap.placeholder);
            List<String> Jsonimage = new ArrayList<>();
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(list.get(position).getJsonData());
                if (jsonObject.toString().contains("\"image\":[")) {
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("image"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Jsonimage.add(String.valueOf(jsonArray.get(i)));
                    }
                    if (Jsonimage.isEmpty()) {
                        holder.mImageView.setImageResource(R.mipmap.placeholder);
                    } else {
                        Glide.with(context)
                                .load(Jsonimage.get(0))
                                .apply(new RequestOptions()
                                        .placeholder(R.mipmap.placeholder))
                                .error(R.mipmap.placeholder)
                                .into(holder.mImageView);
                    }
                }else{
                    holder.mImageView.setImageResource(R.mipmap.placeholder);
                }
            } catch (JSONException e) {
            }

            holder.mImageView_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });

            holder.mTextView_nickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });

            holder.mRelativeLayout_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });

            holder.mLottieAnimationView_reward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });

            holder.mTextView_reward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });

            holder.mLottieAnimationView_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });

            holder.mTextView_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });

            holder.mLottieAnimationView_upVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });

            holder.mTextView_upVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });

            holder.mTextView_xas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListenerFace.OnClickTemp(position, v);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        try {
            ImageView imageView = holder.mImageView;
            if (imageView != null && !context.isDestroyed()) {
                Glide.with(context).clear(imageView);
            }
        } catch (Exception e) {
        }
    }

    public void UpData(int position, boolean isUpVote) {
        getData(list.get(position).getAuthor(), list.get(position).getPermlink(), position, isUpVote);
    }

    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<PostBean.ResultBean> addMessageList) {
        //增加数据
        int position = list.size();
        list.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView_nickname, mTextView_title, mTextView_xas, mTextView_reward, mTextView_comment, mTextView_upVote;
        TypefaceTextView mTextView_time;
        ImageView mImageView;
        CircleImageView mImageView_head;
        RelativeLayout mRelativeLayout_img;
        LottieAnimationView mLottieAnimationView_reward, mLottieAnimationView_comment, mLottieAnimationView_upVote;
        ProgressBar mProgressBar;
        LinearLayout mLinearLayout_bottom;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_nickname = itemView.findViewById(R.id.mTextView_nickname);
            mTextView_time = itemView.findViewById(R.id.mTextView_time);
            mTextView_title = itemView.findViewById(R.id.mTextView_title);
            mTextView_xas = itemView.findViewById(R.id.mTextView_xas);
            mLottieAnimationView_reward = itemView.findViewById(R.id.mLottieAnimationView_reward);
            mTextView_reward = itemView.findViewById(R.id.mTextView_reward);
            mLottieAnimationView_comment = itemView.findViewById(R.id.mLottieAnimationView_comment);
            mTextView_comment = itemView.findViewById(R.id.mTextView_comment);
            mLottieAnimationView_upVote = itemView.findViewById(R.id.mLottieAnimationView_upVote);
            mTextView_upVote = itemView.findViewById(R.id.mTextView_upVote);
            mImageView = itemView.findViewById(R.id.mImageView);
            mImageView_head = itemView.findViewById(R.id.mImageView_head);
            mRelativeLayout_img = itemView.findViewById(R.id.mRelativeLayout_img);
            mProgressBar = itemView.findViewById(R.id.mProgressBar);
            mLinearLayout_bottom = itemView.findViewById(R.id.mLinearLayout_bottom);
        }

        public android.widget.ProgressBar getProgressBar() {
            return mProgressBar;
        }

        public LottieAnimationView getLottieAnimationView() {
            return mLottieAnimationView_upVote;
        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }

    public void getData(String username, String link, final int position, boolean isUpVote) {
        OkHttpClient.initGet(Http.onePost + "?author=" + username + "&permlink=" + link).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PostBean.ResultBean bean = list.get(position);
                PostBean.ResultBean.ActiveVotesBean votesBean = new PostBean.ResultBean.ActiveVotesBean();
                votesBean.setVoter(spUtils.getString("nickname", ""));
                if (isUpVote) {
                    votesBean.setPercent(100 * 100);
                } else {
                    votesBean.setPercent(0);
                }

                List<PostBean.ResultBean.ActiveVotesBean> votesBeans = bean.getActive_votes();
                votesBeans.add(votesBean);

                bean.setActive_votes(votesBeans);
                list.set(position, bean);
                notifyItemChanged(position);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                Gson gson = new Gson();
                                PostBean.ResultBean bean = gson.fromJson(jsonObject.getString("data"), PostBean.ResultBean.class);
                                list.set(position, bean);
                                notifyItemChanged(position);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}