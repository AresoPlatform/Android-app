package com.xw.aschwitkey.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xw.aschwitkey.R;
import com.xw.aschwitkey.activity.MainActivity;
import com.xw.aschwitkey.adapter.ImageAdapter;
import com.xw.aschwitkey.entity.DataBean;
import com.xw.aschwitkey.entity.EventMessageBean;
import com.xw.aschwitkey.utils.SPUtils;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletFragment extends NewLazyFragment {

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.mImageView_left)
    ImageView mImageView_left;
    @BindView(R.id.mImageView_right)
    ImageView mImageView_right;
    @BindView(R.id.mTextView_left)
    TextView mTextView_left;
    @BindView(R.id.mTextView_center)
    TextView mTextView_center;
    @BindView(R.id.mTextView_right)
    TextView mTextView_right;

    private Activity mContext;
    private List<Fragment> fragments;
    private MyViewPagerAdapter adapter;
    private SPUtils spUtils1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_wallet_layout;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        super.initData();
        spUtils1 = new SPUtils(mContext, "AschImport");
        fragments = new ArrayList<>();
        fragments.add(new XasWalletFragment());
        fragments.add(new PledgeMiningFragment());
        fragments.add(new TronWalletFragment());
        adapter = new MyViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mContext);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mTextView_left.setSelected(true);
                    mTextView_center.setSelected(false);
                    mTextView_right.setSelected(false);
                    mImageView_left.setVisibility(View.GONE);
                    mImageView_right.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    mTextView_left.setSelected(false);
                    mTextView_center.setSelected(true);
                    mTextView_right.setSelected(false);
                    mImageView_left.setVisibility(View.GONE);
                    mImageView_right.setVisibility(View.GONE);
                } else if (position == 2) {
                    mTextView_left.setSelected(false);
                    mTextView_center.setSelected(false);
                    mTextView_right.setSelected(true);
                    mImageView_left.setVisibility(View.VISIBLE);
                    mImageView_right.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(1);
        mTextView_center.setSelected(true);

        if (spUtils1.getBoolean("walletFirst", true)) {
        }
    }

    @OnClick({R.id.mImageView_left, R.id.mImageView_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mImageView_left:
                if (mViewPager.getCurrentItem() == 1) {
                    mViewPager.setCurrentItem(0);
                } else {
                    mViewPager.setCurrentItem(1);
                }
                break;
            case R.id.mImageView_right:
                if (mViewPager.getCurrentItem() == 1) {
                    mViewPager.setCurrentItem(2);
                } else {
                    mViewPager.setCurrentItem(1);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessageBean messageEvent) {
        switch (messageEvent.getTag()) {
            case 23:
                mViewPager.setCurrentItem(1);
                break;
            case 26:
                mViewPager.setCurrentItem((int) messageEvent.getMesssage().get("type"));
                break;
            case 27:
                mViewPager.setCurrentItem(1);
                break;
        }
    }

    private void walletDialogFirst() {
        Dialog imagedialog = new Dialog(mContext, R.style.DialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_image_layout, null);
        TextView mTextView_noShow = view.findViewById(R.id.mTextView_noShow);
        TextView mTextView_go = view.findViewById(R.id.mTextView_go);
        Banner mBanner = view.findViewById(R.id.mBanner);
        List<DataBean> Jsonimage = new ArrayList<>();
        DataBean bean = new DataBean(R.mipmap.asch_eg, "", 1);
        DataBean bean1 = new DataBean(R.mipmap.tron_eg, "", 1);
        Jsonimage.add(bean);
        Jsonimage.add(bean1);

        mBanner.setAdapter(new ImageAdapter(Jsonimage))
                .setIndicator(new CircleIndicator(mContext), false)
                .isAutoLoop(false)
                .start();
        imagedialog.setCancelable(false);
        mTextView_noShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spUtils1.putBoolean("walletFirst", false);
                imagedialog.dismiss();
            }
        });
        mTextView_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBanner.getCurrentItem() == 1) {
                    mBanner.setCurrentItem(2);
                } else {
                    imagedialog.dismiss();
                }
            }
        });
        imagedialog.setContentView(view);
        Window dialogWindow = imagedialog.getWindow();
        if (dialogWindow == null) {
            return;
        }

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.3f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        imagedialog.show();
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        private Context context;

        public MyViewPagerAdapter(FragmentManager fm, int behavior, Context context) {
            super(fm, behavior);
            this.context = context;
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

}
