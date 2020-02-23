package com.tamsiree.rxdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxkit.RxActivityTool;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.RxTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author tamsiree
 */
public class ActivitySeat extends ActivityBase {

    @BindView(R.id.btn_movie)
    Button mBtnMovie;
    @BindView(R.id.btn_flight)
    Button mBtnFlight;
    @BindView(R.id.activity_seat)
    LinearLayout mActivitySeat;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);
        mRxTitle.setLeftFinish(mContext);
    }

    @OnClick({R.id.btn_movie, R.id.btn_flight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_movie:
                RxActivityTool.skipActivity(ActivitySeat.this, ActivityMovieSeat.class);
                break;
            case R.id.btn_flight:
                RxActivityTool.skipActivity(ActivitySeat.this, ActivityFlightSeat.class);
                break;
        }
    }
}
