package com.vondear.vontools.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.vontools.R;


/**
 * Created by vonde on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class DialogSureCancle extends TransparentDialog {

    private TextView tv_title;
    private TextView tv_sure;
    private TextView tv_cancle;
    private ImageView iv_logo;

    public void setIv_logo(ImageView iv_logo) {
        this.iv_logo = iv_logo;
    }

    public ImageView getIv_logo() {
        return iv_logo;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_sure(TextView tv_sure) {
        this.tv_sure = tv_sure;
    }

    public TextView getTv_sure() {
        return tv_sure;
    }

    public void setTv_cancle(TextView tv_cancle) {
        this.tv_cancle = tv_cancle;
    }

    public TextView getTv_cancle() {
        return tv_cancle;
    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure_false, null);
        tv_sure = (TextView) dialog_view.findViewById(R.id.tv_sure);
        tv_cancle = (TextView) dialog_view.findViewById(R.id.tv_cancle);
        tv_title = (TextView) dialog_view.findViewById(R.id.tv_title);
        iv_logo = (ImageView) dialog_view.findViewById(R.id.iv_logo);
        setContentView(dialog_view);
    }

    public DialogSureCancle(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public DialogSureCancle(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public DialogSureCancle(Context context) {
        super(context);
        initView();
    }

    public DialogSureCancle(Activity context) {
        super(context);
        initView();
    }

    public DialogSureCancle(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public DialogSureCancle(Context context, float alpha, int gravity, boolean isOnScreen) {
        super(context, alpha, gravity, isOnScreen);
        initView();
    }
}
