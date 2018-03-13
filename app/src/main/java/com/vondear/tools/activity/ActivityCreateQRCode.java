package com.vondear.tools.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxBarCode;
import com.vondear.rxtools.view.RxQRCode;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vondear.tools.R.id.iv_linecode;

/**
 * @author vondear
 */
public class ActivityCreateQRCode extends ActivityBase implements View.OnClickListener {

    private static android.os.Handler Handler = new Handler();
    private static Runnable mRunnable = null;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(iv_linecode)
    ImageView mIvLinecode;
    @BindView(R.id.iv_code)
    ImageView mIvCode;
    @BindView(R.id.imageView1)
    ImageView mImageView1;
    @BindView(R.id.textView2)
    TextView mTextView2;
    @BindView(R.id.tv_time_second)
    TextView mTvTimeSecond;
    @BindView(R.id.ll_refresh)
    LinearLayout mLlRefresh;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 60000:
                    initData();
                    break;
                default:
                    break;
            }
        }
    };
    private int time_second = 0;
    private int second = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(mContext);
        setContentView(R.layout.activity_create_qrcode);
        RxBarTool.setTransparentStatusBar(mContext);
        ButterKnife.bind(this);
        initView();
        initData();
        AuthCode(mTvTimeSecond, second);
    }

    private void AuthCode(final TextView view, final int timeSecond) {

        mRunnable = new Runnable() {
            int mSumNum = timeSecond;

            @Override
            public void run() {
                Handler.postDelayed(mRunnable, 1000);
                view.setText(mSumNum + "");
                view.setEnabled(false);
                mSumNum--;
                if (mSumNum < 0) {
                    view.setText(0 + "");
                    view.setEnabled(true);
                    Message message = new Message();
                    message.what = 60000;
                    mHandler.sendMessage(message);
                    // 干掉这个定时器，下次减不会累加
                    Handler.removeCallbacks(mRunnable);
                    AuthCode(mTvTimeSecond, second);
                }
            }

        };
        Handler.postDelayed(mRunnable, 1000);
    }

    private void initView() {
        mRxTitle.setLeftFinish(mContext);
        mRxTitle.setTitle("动态生成码");
        mLlRefresh.setOnClickListener(this);
    }

    private void initData() {
        // TODO Auto-generated method stub
        RxQRCode.createQRCode("时间戳:" + System.currentTimeMillis(), 800, 800, mIvCode);
        mIvLinecode.setImageBitmap(RxBarCode.createBarCode("" + System.currentTimeMillis(), 1000, 300));
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.ll_refresh:
                Handler.removeCallbacks(mRunnable);
                initData();
                mTvTimeSecond.setText(second + "");
                AuthCode(mTvTimeSecond, second);
                break;
            default:
                break;
        }
    }
}
