package com.vondear.tools.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.vondear.rxtools.RxCaptcha;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.swipecaptcha.SwipeCaptchaView;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityRxCaptcha extends ActivityBase {

    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.btn_get_code)
    Button btnGetCode;
    @BindView(R.id.swipeCaptchaView)
    SwipeCaptchaView mSwipeCaptchaView;
    @BindView(R.id.dragBar)
    SeekBar mSeekBar;
    @BindView(R.id.btnChange)
    Button mBtnChange;
    @BindView(R.id.activity_rx_captcha)
    LinearLayout mActivityRxCaptcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_captcha);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mSwipeCaptchaView.setOnCaptchaMatchCallback(new SwipeCaptchaView.OnCaptchaMatchCallback() {
            @Override
            public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
                RxToast.success(mContext, "验证通过！", Toast.LENGTH_SHORT).show();
                //swipeCaptcha.createCaptcha();
                mSeekBar.setEnabled(false);
            }

            @Override
            public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
                Log.d("zxt", "matchFailed() called with: swipeCaptchaView = [" + swipeCaptchaView + "]");
                RxToast.error(mContext, "验证失败:拖动滑块将悬浮头像正确拼合", Toast.LENGTH_SHORT).show();
                swipeCaptchaView.resetCaptcha();
                mSeekBar.setProgress(0);
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSwipeCaptchaView.setCurrentSwipeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //随便放这里是因为控件
                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [" + seekBar + "]");
                mSwipeCaptchaView.matchCaptcha();
            }
        });

        //测试从网络加载图片是否ok
        Glide.with(this)
                .load(R.drawable.douyu)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mSwipeCaptchaView.setImageBitmap(resource);
                        mSwipeCaptchaView.createCaptcha();
                    }
                });
    }

    @OnClick({R.id.btn_get_code, R.id.btnChange})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_code:
                ivCode.setImageBitmap(RxCaptcha.getInstance().getBitmap());
                tvCode.setText(RxCaptcha.getInstance().getCode());
                break;
            case R.id.btnChange:
                mSwipeCaptchaView.createCaptcha();
                mSeekBar.setEnabled(true);
                mSeekBar.setProgress(0);
                break;
        }
    }
}
