package com.tamsiree.rxui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.TrafficStats;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tamsiree.rxui.R;

import java.text.DecimalFormat;


/**
 * @author tamsiree
 * @date 2017/2/15
 */

public class RxNetSpeedView extends FrameLayout {

    private RelativeLayout rlLayoutBig;
    private TextView tvMobileTx;
    private TextView tvMobileRx;
    private TextView tvWlanTx;
    private TextView tvWlanRx;
    private TextView tvSum;

    private TextView mobileTx;
    private TextView mobileRx;
    private TextView wlanTx;
    private TextView wlanRx;

    double timeSpan = 2000d;
    private long rxtxTotal = 0;
    private long mobileRecvSum = 0;
    private long mobileSendSum = 0;
    private long wlanRecvSum = 0;
    private long wlanSendSum = 0;
    private long exitTime = 0;

    private int mTextColor;

    private int mTextSize;

    private boolean isMulti = false;

    private DecimalFormat showFloatFormat = new DecimalFormat("0.00");

    private long timeInterval = 500;

    public RxNetSpeedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RxNetSpeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.rx_netspeed_view, this);
        rlLayoutBig = findViewById(R.id.rlLayoutBig);
        tvMobileTx = findViewById(R.id.tvMobileTx);
        tvMobileRx = findViewById(R.id.tvMobileRx);
        tvWlanTx = findViewById(R.id.tvWlanTx);
        tvWlanRx = findViewById(R.id.tvWlanRx);
        tvSum = findViewById(R.id.tvSum);

        mobileTx = findViewById(R.id.MobileTx);
        mobileRx = findViewById(R.id.MobileRx);
        wlanTx = findViewById(R.id.WlanTx);
        wlanRx = findViewById(R.id.WlanRx);

        //获得这个控件对应的属性。
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RxNetSpeedView);

        try {
            //获得属性值
            mTextColor = a.getColor(R.styleable.RxNetSpeedView_RxTextColor, getResources().getColor(R.color.white));
            //TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())
            mTextSize = a.getDimensionPixelSize(R.styleable.RxNetSpeedView_RxTextSize, 12);
            isMulti = a.getBoolean(R.styleable.RxNetSpeedView_isMulti, false);
        } finally {
            //回收这个对象
            a.recycle();
        }

        setTextColor(mTextColor);

        setTextSize(mTextSize);

        setMulti(isMulti);

        //延迟调用
        handler.postDelayed(task, timeInterval);
    }

    public void setTextSize(int textSize) {
        if (textSize != 0) {
            tvMobileTx.setTextSize(textSize);
            tvMobileRx.setTextSize(textSize);
            tvWlanTx.setTextSize(textSize);
            tvWlanRx.setTextSize(textSize);
            tvSum.setTextSize(textSize);

            mobileTx.setTextSize(textSize);
            mobileRx.setTextSize(textSize);
            wlanTx.setTextSize(textSize);
            wlanRx.setTextSize(textSize);

        }
    }

    public void setTextColor(int textColor) {
        if (textColor != 0) {
            tvMobileTx.setTextColor(textColor);
            tvMobileRx.setTextColor(textColor);
            tvWlanTx.setTextColor(textColor);
            tvWlanRx.setTextColor(textColor);
            tvSum.setTextColor(textColor);

            mobileTx.setTextColor(textColor);
            mobileRx.setTextColor(textColor);
            wlanTx.setTextColor(textColor);
            wlanRx.setTextColor(textColor);
        }
    }

    public RxNetSpeedView(Context context) {
        super(context);
    }

    public void updateViewData() {

        long tempSum = TrafficStats.getTotalRxBytes()
                + TrafficStats.getTotalTxBytes();
        long rxtxLast = tempSum - rxtxTotal;
        double totalSpeed = rxtxLast * 1000 / timeSpan;
        rxtxTotal = tempSum;
        long tempMobileRx = TrafficStats.getMobileRxBytes();
        long tempMobileTx = TrafficStats.getMobileTxBytes();
        long tempWlanRx = TrafficStats.getTotalRxBytes() - tempMobileRx;
        long tempWlanTx = TrafficStats.getTotalTxBytes() - tempMobileTx;
        long mobileLastRecv = tempMobileRx - mobileRecvSum;
        long mobileLastSend = tempMobileTx - mobileSendSum;
        long wlanLastRecv = tempWlanRx - wlanRecvSum;
        long wlanLastSend = tempWlanTx - wlanSendSum;
        double mobileRecvSpeed = mobileLastRecv * 1000 / timeSpan;
        double mobileSendSpeed = mobileLastSend * 1000 / timeSpan;
        double wlanRecvSpeed = wlanLastRecv * 1000 / timeSpan;
        double wlanSendSpeed = wlanLastSend * 1000 / timeSpan;
        mobileRecvSum = tempMobileRx;
        mobileSendSum = tempMobileTx;
        wlanRecvSum = tempWlanRx;
        wlanSendSum = tempWlanTx;
        //==========================================================
        if (isMulti) {
            if (mobileRecvSpeed >= 0d) {
                tvMobileRx.setText(showSpeed(mobileRecvSpeed));
            }
            if (mobileSendSpeed >= 0d) {
                tvMobileTx.setText(showSpeed(mobileSendSpeed));
            }
            if (wlanRecvSpeed >= 0d) {
                tvWlanRx.setText(showSpeed(wlanRecvSpeed));
            }
            if (wlanSendSpeed >= 0d) {
                tvWlanTx.setText(showSpeed(wlanSendSpeed));
            }
        } else {
            //==============================================================
            if (totalSpeed >= 0d) {
                tvSum.setText(showSpeed(totalSpeed));
            }
        }
        //==============================================================
    }

    private String showSpeed(double speed) {
        String speedString;
        if (speed >= 1048576d) {
            speedString = showFloatFormat.format(speed / 1048576d) + "MB/s";
        } else {
            speedString = showFloatFormat.format(speed / 1024d) + "KB/s";
        }
        return speedString;
    }

    private Handler handler = new Handler();

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            //设置延迟时间，此处是5秒
            handler.postDelayed(this, timeInterval);
            updateViewData();
            //需要执行的代码
        }
    };

    public boolean isMulti() {
        return isMulti;
    }

    public void setMulti(boolean multi) {
        isMulti = multi;
        if (isMulti) {
            tvSum.setVisibility(GONE);
            rlLayoutBig.setVisibility(VISIBLE);
        } else {
            tvSum.setVisibility(VISIBLE);
            rlLayoutBig.setVisibility(GONE);
        }
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(task);
    }
}
