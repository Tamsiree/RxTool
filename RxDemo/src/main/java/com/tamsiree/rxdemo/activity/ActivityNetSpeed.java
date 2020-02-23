package com.tamsiree.rxdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.RxNetSpeedView;
import com.tamsiree.rxui.view.RxTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author tamsiree
 */
public class ActivityNetSpeed extends ActivityBase {

    @BindView(R.id.button2)
    Button mButton2;
    @BindView(R.id.button3)
    Button mButton3;
    @BindView(R.id.activity_net_speed)
    RelativeLayout mActivityNetSpeed;
    @BindView(R.id.rx_net_speed_view)
    RxNetSpeedView mRxNetSpeedView;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_speed);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
        RxDeviceTool.setPortrait(this);
    }

    @OnClick({R.id.button2, R.id.button3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button2:
                mRxNetSpeedView.setMulti(false);
                break;
            case R.id.button3:
                mRxNetSpeedView.setMulti(true);
                break;
        }
    }
}
