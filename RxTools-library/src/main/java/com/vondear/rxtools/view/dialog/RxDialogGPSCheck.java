package com.vondear.rxtools.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.vondear.rxtools.RxLocationUtils;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxDialogGPSCheck extends RxDialogSureCancel {
    
    private void initView() {
        getTvTitle().setBackgroundDrawable(null);
        setTitle("GPS未打开");
        getTvTitle().setTextSize(16f);
        getTvTitle().setTextColor(Color.BLACK);
        setContent("您需要在系统设置中打开GPS方可采集数据");
        getTvSure().setText("去设置");
        getTvCancel().setText("知道了");

        getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxLocationUtils.openGpsSettings(mContext);
                cancel();
            }
        });

        getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public RxDialogGPSCheck(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogGPSCheck(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogGPSCheck(Context context) {
        super(context);
        initView();
    }

    public RxDialogGPSCheck(Activity context) {
        super(context);
        initView();
    }

    public RxDialogGPSCheck(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

}
