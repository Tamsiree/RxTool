package com.tamsiree.rxui.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tamsiree.rxkit.RxDataTool;
import com.tamsiree.rxui.R;


/**
 * @author tamsiree
 * @date 2016/7/19
 * Mainly used for confirmation and cancel.
 */
public class RxDialogEditSureCancel extends RxDialog {

    private ImageView mIvLogo;
    private TextView mTvSure;
    private TextView mTvCancel;
    private EditText editText;
    private TextView mTvTitle;

    private String title;
    private int logoIcon = -1;

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

    public ImageView getLogoView() {
        return mIvLogo;
    }

    public TextView getTitleView() {
        return mTvTitle;
    }

    public EditText getEditText() {
        return editText;
    }

    public TextView getSureView() {
        return mTvSure;
    }

    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }

    public TextView getCancelView() {
        return mTvCancel;
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edittext_sure_false, null);
        mIvLogo = dialogView.findViewById(R.id.iv_logo);
        mTvTitle = dialogView.findViewById(R.id.tv_title);
        mTvSure = dialogView.findViewById(R.id.tv_sure);
        mTvCancel = dialogView.findViewById(R.id.tv_cancle);
        editText = dialogView.findViewById(R.id.editText);

        if (RxDataTool.isNullString(title)) {
            mTvTitle.setVisibility(View.GONE);
        }

        if (logoIcon == -1) {
            mIvLogo.setVisibility(View.GONE);
        }

        setContentView(dialogView);
    }

    public void setLogo(int LOGOIcon) {
        logoIcon = LOGOIcon;
        if (logoIcon == -1) {
            mIvLogo.setVisibility(View.GONE);
            return;
        }
        mIvLogo.setVisibility(View.VISIBLE);
        mIvLogo.setImageResource(logoIcon);
    }

    public void setTitle(String titleStr) {
        this.title = titleStr;
        if (RxDataTool.isNullString(title)) {
            mTvTitle.setVisibility(View.GONE);
            return;
        }
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText(title);
    }

}
