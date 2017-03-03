package com.vondear.rxtools.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxtools.R;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxDialogEditTextSureCancle extends RxDialog {

    private TextView tv_sure;
    private TextView tv_cancle;
    private TextView tv_title;
    private EditText editText;
    private ImageView iv_logo;

    public void setIv_logo(ImageView iv_logo) {
        this.iv_logo = iv_logo;
    }

    public ImageView getIv_logo() {
        return iv_logo;
    }

    public EditText getEditText() {
        return editText;
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public TextView getTv_sure() {
        return tv_sure;
    }

    public TextView getTv_cancle() {
        return tv_cancle;
    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edittext_sure_false, null);
        iv_logo = (ImageView) dialog_view.findViewById(R.id.iv_logo);
        tv_sure = (TextView) dialog_view.findViewById(R.id.tv_sure);
        tv_cancle = (TextView) dialog_view.findViewById(R.id.tv_cancle);
        editText = (EditText) dialog_view.findViewById(R.id.editText);
        tv_title = (TextView) dialog_view.findViewById(R.id.tv_title);
        setContentView(dialog_view);
    }

    public RxDialogEditTextSureCancle(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogEditTextSureCancle(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogEditTextSureCancle(Context context) {
        super(context);
        initView();
    }

    public RxDialogEditTextSureCancle(Activity context) {
        super(context);
        initView();
    }

    public RxDialogEditTextSureCancle(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }
}
