package com.tamsiree.rxdemo.activity;

import android.os.Bundle;
import android.view.View;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.RxTitle;
import com.tamsiree.rxui.view.TUnReadView;
import com.tamsiree.rxui.view.other.TCrossView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityOtherEffect extends ActivityBase {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.tunreadview)
    TUnReadView mTunreadview;
    @BindView(R.id.tcross_view)
    TCrossView mTcrossView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_effect);
        ButterKnife.bind(this);

        initView();

    }

    private void initView() {
        mRxTitle.setLeftFinish(this);
        initTUnReadView();
        initTCrossView();

    }

    private void initTUnReadView() {
        mTunreadview.setText("14");          //设置未读信息数
//        mTunreadview.setNewText();     //设置有新的信息状态
    }

    private void initTCrossView() {
        mTcrossView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTcrossView.toggle();
            }
        });
        mTcrossView.setColor(getResources().getColor(R.color.green_xiaomi));
    }

}
