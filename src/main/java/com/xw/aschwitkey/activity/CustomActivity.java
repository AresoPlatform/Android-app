package com.xw.aschwitkey.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xw.aschwitkey.R;
import com.xw.aschwitkey.utils.GlideUtils;
import com.xw.aschwitkey.utils.ViewUtils;

import java.util.List;

public class CustomActivity extends AppCompatActivity implements QRCodeView.Delegate {

    //    private CaptureFragment captureFragment;
    private static final String TAG = CustomActivity.class.getSimpleName();

    @BindView(R.id.mTextView_bar)
    TextView mTextView_bar;
    @BindView(R.id.mZXingView)
    ZXingView mZXingView;
    @BindView(R.id.mLinearLayout_flash)
    LinearLayout mLinearLayout_flash;
    @BindView(R.id.mTextView_flash)
    TextView mTextView_flash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //设置沉浸式状态栏
        ViewUtils.setImmersionStateMode(CustomActivity.this);
        mTextView_bar.setHeight(ViewUtils.getStatusBarHeight(CustomActivity.this));
        mZXingView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
                mLinearLayout_flash.setVisibility(View.VISIBLE);
                mTextView_flash.setText("打开闪光灯");
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
                mLinearLayout_flash.setVisibility(View.VISIBLE);
                mTextView_flash.setText("打开闪光灯");
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        if(result == null){
            bundle.putInt("resultType", 0);
        }else{
            bundle.putInt("resultType", 1);
            bundle.putString("result", result);
        }
        resultIntent.putExtras(bundle);
        CustomActivity.this.setResult(RESULT_OK, resultIntent);
        CustomActivity.this.finish();

        mZXingView.startSpot(); // 开始识别
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    images = PictureSelector.obtainMultipleResult(data);
                    mZXingView.decodeQRCode(images.get(0).getCutPath());
                    break;
            }
        }
    }

    @OnClick({R.id.mImageView_Back, R.id.mImageView_Album,R.id.mTextView_flash})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mImageView_Back:
                finish();
                break;
            case R.id.mImageView_Album:
                //相册
                PictureSelector.create(CustomActivity.this)
                        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .imageEngine(GlideUtils.createGlideUtils())
                        .maxSelectNum(1)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                        .isPreviewImage(false)// 是否可预览图片
                        .isCamera(false)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .isEnableCrop(true)// 是否裁剪
                        .isCompress(false)// 是否压缩
                        .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                                .selectionMedia(selectList)// 是否传入已选图片
                        .isPreviewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.mTextView_flash:
                if(mTextView_flash.getText().toString().equals("关闭闪光灯")){
                    mTextView_flash.setText("打开闪光灯");
                    mZXingView.closeFlashlight();
                }else{
                    mTextView_flash.setText("关闭闪光灯");
                    mZXingView.openFlashlight();
                }
                break;
        }
    }

}
