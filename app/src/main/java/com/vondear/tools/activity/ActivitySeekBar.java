package com.vondear.tools.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxSeekBar;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.tools.R;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vondear.tools.R.id.seekbar1;
import static com.vondear.tools.R.id.seekbar2;

public class ActivitySeekBar extends ActivityBase {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(seekbar1)
    RxSeekBar mSeekbar1;
    @BindView(R.id.progress2_tv)
    TextView mProgress2Tv;
    @BindView(seekbar2)
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
