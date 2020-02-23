package com.tamsiree.rxui.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tamsiree.rxkit.RxDataTool;
import com.tamsiree.rxui.R;


/**
 * @author tamsiree
 * @date 2016/7/19
 * 确认 取消 Dialog
 */
public class RxDialogSureCancel extends RxDialog {

    private ImageView mIvLogo;
    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvCancel;
    private TextView mTvTitle;

    private String title;
    private int logoIcon = -1;

    public RxDialogSureCancel(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogSureCancel(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogSureCancel(Context context) {
        super(context);
        initView();
    }

    public RxDialogSureCancel(Activity context) {
        super(context);
        initView();
    }

    public RxDialogSureCancel(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public ImageView getLogoView() {
        return mIvLogo;
    }

    public TextView getTitleView() {
        return mTvTitle;
    }

    public void setContent(String content) {
        this.mTvContent.setText(content);
    }

    public TextView getContentView() {
        return mTvContent;
    }

    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }

    public TextView getSureView() {
        return mTvSure;
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    public TextView getCancelView() {
        return mTvCancel;
    }

    public void setSureListener(View.OnClickListener sureListener) {
        mTvSure.setOnClickListener(sureListener);
    }

    public void setCancelListener(View.OnClickListener cancelListener) {
        mTvCancel.setOnClickListener(cancelListener);
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure_false, null);
        mIvLogo = dialogView.findViewById(R.id.iv_logo);
        mTvSure = dialogView.findViewById(R.id.tv_sure);
        mTvCancel = dialogView.findViewById(R.id.tv_cancel);
        mTvContent = dialogView.findViewById(R.id.tv_content);
        mTvContent.setTextIsSelectable(true);
        mTvTitle = dialogView.findViewById(R.id.tv_title);


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
