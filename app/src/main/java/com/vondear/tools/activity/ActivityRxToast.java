package com.vondear.tools.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityRxToast extends ActivityBase {

    @BindView(R.id.button_error_toast)
    Button mButtonErrorToast;
    @BindView(R.id.button_success_toast)
    Button mButtonSuccessToast;
    @BindView(R.id.button_info_toast)
    Button mButtonInfoToast;
    @BindView(R.id.button_warning_toast)
    Button mButtonWarningToast;
    @BindView(R.id.button_normal_toast_wo_icon)
    Button mButtonNormalToastWoIcon;
    @BindView(R.id.button_normal_toast_w_icon)
    Button mButtonNormalToastWIcon;
    @BindView(R.id.activity_main)
    RelativeLayout mActivityMain;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_toast);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mRxTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.button_error_toast, R.id.button_success_toast, R.id.button_info_toast, R.id.button_warning_toast, R.id.button_normal_toast_wo_icon, R.id.button_normal_toast_w_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_error_toast:
                RxToast.error(mContext, "This is an error toast.", Toast.LENGTH_SHORT, true).show();
                break;
            case R.id.button_success_toast:
                RxToast.success(mContext, "Success!", Toast.LENGTH_SHORT, true).show();
                break;
            case R.id.button_info_toast:
                RxToast.info(mContext, "Here is some info for you.", Toast.LENGTH_SHORT, true).show();
                break;
            case R.id.button_warning_toast:
                RxToast.warning(mContext, "Beware of the dog.", Toast.LENGTH_SHORT, true).show();
                break;
            case R.id.button_normal_toast_wo_icon:
                RxToast.normal(mContext, "Normal toast w/o icon").show();
                break;
            case R.id.button_normal_toast_w_icon:
                Drawable icon = getResources().getDrawable(R.mipmap.ic_launcher);
                RxToast.normal(mContext, "Normal toast w/ icon", icon).show();
                break;
        }
    }
}
