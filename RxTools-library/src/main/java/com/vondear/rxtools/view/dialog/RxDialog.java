package com.vondear.rxtools.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.vondear.rxtools.R;
import com.vondear.rxtools.RxDeviceUtils;

public class RxDialog extends Dialog {

    public LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    private LayoutParams mLayoutParams;

    private Context mContext;

    public RxDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public RxDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public RxDialog(Context context) {
        super(context);
        initView(context);
    }

    public RxDialog(Activity activity) {
        super(activity);
        initView(activity);
    }

    private void initView(Context context) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawableResource(R.drawable.transparent_bg);
        mContext = context;
        Window window = this.getWindow();
        mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = 1f;
        window.setAttributes(mLayoutParams);
        if (mLayoutParams != null) {
            mLayoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.gravity = Gravity.CENTER;
        }
    }

    /**
     * @param context
     * @param alpha   透明度 0.0f--1f(不透明)
     * @param gravity 方向(Gravity.BOTTOM,Gravity.TOP,Gravity.LEFT,Gravity.RIGHT)
     */
    public RxDialog(Context context, float alpha, int gravity) {
        super(context);
        // TODO Auto-generated constructor stub
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawableResource(R.drawable.transparent_bg);
        mContext = context;
        Window window = this.getWindow();
        mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = 1f;
        window.setAttributes(mLayoutParams);
        if (mLayoutParams != null) {
            mLayoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.gravity = gravity;
        }
    }

    /**
     * 隐藏头部导航栏状态栏
     */
    public void skipTools() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
    }

    public void setFullScreen() {
        Window window = getWindow();
        LayoutParams lp = window.getAttributes();
        lp.width = RxDeviceUtils.getScreenWidth(mContext);
        lp.height = RxDeviceUtils.getScreenHeight(mContext);
        window.setAttributes(lp);
    }
}
