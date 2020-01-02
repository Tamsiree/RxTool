package com.vondear.rxdemo.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vondear.rxdemo.R;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.activity.ActivityBase;
import com.vondear.rxui.view.RxCaptcha;
import com.vondear.rxui.view.RxTitle;
import com.vondear.rxui.view.swipecaptcha.RxSwipeCaptcha;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vondear.rxui.view.RxCaptcha.TYPE.CHARS;


/**
 * @author vondear
 */
public class ActivityRxCaptcha extends ActivityBase {

    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.btn_get_code)
    Button btnGetCode;
    @BindView(R.id.swipeCaptchaView)
    RxSwipeCaptcha mRxSwipeCaptcha;
    @BindView(R.id.dragBar)
    SeekBar mSeekBar;
    @BindView(R.id.btnChange)
    Button mBtnChange;
    @BindView(R.id.activity_rx_captcha)
    LinearLayout mActivityRxCaptcha;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_captcha);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);
        initView();
    }

    private void initView() {
        mRxTitle.setLeftFinish(mContext);
        mRxSwipeCaptcha.setOnCaptchaMatchCallback(new RxSwipeCaptcha.OnCaptchaMatchCallback() {
            @Override
            public void matchSuccess(RxSwipeCaptcha rxSwipeCaptcha) {
                RxToast.success(mContext, "验证通过！", Toast.LENGTH_SHORT).show();
                //swipeCaptcha.createCaptcha();
                mSeekBar.setEnabled(false);
            }

            @Override
            public void matchFailed(RxSwipeCaptcha rxSwipeCaptcha) {
                Log.d("zxt", "matchFailed() called with: rxSwipeCaptcha = [" + rxSwipeCaptcha + "]");
                RxToast.error(mContext, "验证失败:拖动滑块将悬浮头像正确拼合", Toast.LENGTH_SHORT).show();
                rxSwipeCaptcha.resetCaptcha();
                mSeekBar.setProgress(0);
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mRxSwipeCaptcha.setCurrentSwipeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //随便放这里是因为控件
                mSeekBar.setMax(mRxSwipeCaptcha.getMaxSwipeValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [" + seekBar + "]");
                mRxSwipeCaptcha.matchCaptcha();
            }
        });

        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                mRxSwipeCaptcha.setImageDrawable(resource);
                mRxSwipeCaptcha.createCaptcha();
            }
        };

        //测试从网络加载图片是否ok
        Glide.with(this)
                .load(R.drawable.douyu)
                .into(simpleTarget);
    }

    @OnClick({R.id.btn_get_code, R.id.btnChange})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_code:

                RxCaptcha.build()
                        .backColor(0xffffff)
                        .codeLength(6)
                        .fontSize(60)
                        .lineNumber(0)
                        .size(200, 70)
                        .type(CHARS)
                        .into(ivCode);

                tvCode.setText(RxCaptcha.build().getCode());
                break;
            case R.id.btnChange:
                mRxSwipeCaptcha.createCaptcha();
                mSeekBar.setEnabled(true);
                mSeekBar.setProgress(0);
                break;
            default:
                break;
        }
    }
}
