package com.tamsiree.rxdemo.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxkit.RxBarTool;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.RxTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author tamsiree
 */
public class ActivityAutoImageView extends ActivityBase {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.activity_auto_image_view)
    RelativeLayout mActivityAutoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        RxBarTool.setTransparentStatusBar(this);
        setContentView(R.layout.activity_auto_image_view);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);

        mRxTitle.setLeftFinish(mContext);
    }
}
