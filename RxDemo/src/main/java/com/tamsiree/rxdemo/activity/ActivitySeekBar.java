package com.tamsiree.rxdemo.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.RxSeekBar;
import com.tamsiree.rxui.view.RxTitle;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * @author tamsiree
 */
public class ActivitySeekBar extends ActivityBase {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.seekbar1)
    RxSeekBar mSeekbar1;
    @BindView(R.id.progress2_tv)
    TextView mProgress2Tv;
    @BindView(R.id.seekbar2)
    RxSeekBar mSeekbar2;
    @BindView(R.id.seekbar3)
    RxSeekBar mSeekbar3;
    @BindView(R.id.seekbar4)
    RxSeekBar mSeekbar4;
    @BindView(R.id.activity_main)
    LinearLayout mActivityMain;

    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);
        mRxTitle.setLeftFinish(mContext);

        mSeekbar1.setValue(10);
        mSeekbar2.setValue(-0.5f, 0.8f);

        mSeekbar1.setOnRangeChangedListener(new RxSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RxSeekBar view, float min, float max, boolean isFromUser) {
                mSeekbar1.setProgressDescription((int) min + "%");
            }
        });

        mSeekbar2.setOnRangeChangedListener(new RxSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RxSeekBar view, float min, float max, boolean isFromUser) {
                if (isFromUser) {
                    mProgress2Tv.setText(min + "-" + max);
                    mSeekbar2.setLeftProgressDescription(df.format(min));
                    mSeekbar2.setRightProgressDescription(df.format(max));
                }
            }
        });

    }
}
