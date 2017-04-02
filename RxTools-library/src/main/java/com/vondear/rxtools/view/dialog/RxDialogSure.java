package com.vondear.rxtools.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxtools.R;
import com.vondear.rxtools.RxRegUtils;
import com.vondear.rxtools.RxTextUtils;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxDialogSure extends RxDialog {

    private ImageView iv_logo;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_sure;

    public ImageView getIv_logo() {
        return iv_logo;
    }

    public TextView getTvTitle() {
        return tv_title;
    }

    public TextView getTvSure() {
        return tv_sure;
    }

    public TextView getTvContent() {
        return tv_content;
    }

    public void setLogo(int resId) {
        iv_logo.setImageResource(resId);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setSure(String content) {
        tv_sure.setText(content);
    }


    public void setContent(String str) {
        if (RxRegUtils.isURL(str)) {
            // 响应点击事件的话必须设置以下属性
            tv_content.setMovementMethod(LinkMovementMethod.getInstance());
            tv_content.setText(RxTextUtils.getBuilder("").setBold().append(str).setUrl(str).create());//当内容为网址的时候，内容变为可点击
        } else {
            tv_content.setText(str);
        }

    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure, null);
        tv_sure = (TextView) dialog_view.findViewById(R.id.tv_sure);
        tv_title = (TextView) dialog_view.findViewById(R.id.tv_title);
        tv_title.setTextIsSelectable(true);
        tv_content = (TextView) dialog_view.findViewById(R.id.tv_content);
        tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_content.setTextIsSelectable(true);
        iv_logo = (ImageView) dialog_view.findViewById(R.id.iv_logo);
        setContentView(dialog_view);
    }

    public RxDialogSure(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogSure(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogSure(Context context) {
        super(context);
        initView();
    }

    public RxDialogSure(Activity context) {
        super(context);
        initView();
    }

    public RxDialogSure(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

}
