package com.vondear.rxtools.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vondear.rxtools.R;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxDialogEditSureCancel extends RxDialog {

    private TextView mTvSure;
    private TextView mTvCancel;
    private TextView mTvContent;
    private EditText editText;
    private TextView mTvTitle;

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public EditText getEditText() {
        return editText;
    }

    public TextView getTvContent() {
        return mTvContent;
    }

    public void setContent(String content) {
        this.mTvContent.setText(content);
    }

    public TextView getTvSure() {
        return mTvSure;
    }

    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }


    public TextView getTvCancel() {
        return mTvCancel;
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edittext_sure_false, null);
        mTvTitle = (TextView) dialog_view.findViewById(R.id.tv_logo);
        mTvSure = (TextView) dialog_view.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialog_view.findViewById(R.id.tv_cancle);
        editText = (EditText) dialog_view.findViewById(R.id.editText);
        mTvContent = (TextView) dialog_view.findViewById(R.id.tv_title);
        setContentView(dialog_view);
    }

    public RxDialogEditSureCancel(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogEditSureCancel(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogEditSureCancel(Context context) {
        super(context);
        initView();
    }

    public RxDialogEditSureCancel(Activity context) {
        super(context);
        initView();
    }

    public RxDialogEditSureCancel(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }
}
