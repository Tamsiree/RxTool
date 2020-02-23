package com.tamsiree.rxdemo.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxkit.RxActivityTool;
import com.tamsiree.rxkit.RxBarTool;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.RxShoppingView;
import com.tamsiree.rxui.view.RxTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author tamsiree
 */
public class ActivityShoppingView extends ActivityBase {

    @BindView(R.id.sv_1)
    RxShoppingView mSv1;
    @BindView(R.id.sv_2)
    RxShoppingView mSv2;
    @BindView(R.id.btn_take_out)
    Button mBtnTakeOut;
    @BindView(R.id.activity_shopping_view)
    LinearLayout mActivityShoppingView;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_shopping_view);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);
        mRxTitle.setLeftFinish(mContext);

    }

    @OnClick(R.id.btn_take_out)
    public void onClick() {
        RxActivityTool.skipActivity(mContext, ActivityELMe.class);
    }
}
