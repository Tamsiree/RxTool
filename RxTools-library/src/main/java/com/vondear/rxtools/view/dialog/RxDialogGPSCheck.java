package com.vondear.rxtools.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.vondear.rxtools.RxLocationTool;


/**
 * @author vondear
 * @date 2016/7/19
 * 检测Gps状态
 */
public class RxDialogGPSCheck extends RxDialogSureCancel {
    
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

    private void initView() {
        getTitleView().setBackgroundDrawable(null);
        setTitle("GPS未打开");
        getTitleView().setTextSize(16f);
        getTitleView().setTextColor(Color.BLACK);
        setContent("您需要在系统设置中打开GPS方可采集数据");
        getSureView().setText("去设置");
        getCancelView().setText("知道了");

        getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxLocationTool.openGpsSettings(mContext);
                cancel();
            }
        });

        getCancelView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

}
