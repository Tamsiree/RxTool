package com.tamsiree.rxdemo.activity;

import android.os.Bundle;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.RxTitle;
import com.tamsiree.rxui.view.TUnReadView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityOtherEffect extends ActivityBase {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.tunreadview)
    TUnReadView mTunreadview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_effect);
        ButterKnife.bind(this);

        mRxTitle.setLeftFinish(this);
        mTunreadview.setText("14");          //设置未读信息数
//        mTunreadview.setNewText();     //设置有新的信息状态

    }
}
