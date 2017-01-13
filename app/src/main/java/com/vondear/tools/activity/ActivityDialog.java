package com.vondear.tools.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.tools.R;
import com.vondear.rxtools.RxBarUtils;
import com.vondear.rxtools.view.dialog.RxDialogEditTextSureCancle;
import com.vondear.rxtools.view.dialog.RxDialogLoadingProgressAcfunVideo;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancle;
import com.vondear.rxtools.view.dialog.dialogShapeLoadingView.ShapeLoadingDialog;
import com.vondear.rxtools.view.dialog.dialogWheel.DialogWheelYearMonthDay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDialog extends Activity {

    @BindView(R.id.button_DialogSure)
    Button buttonDialogSure;
    @BindView(R.id.button_DialogSureCancle)
    Button buttonDialogSureCancle;
    @BindView(R.id.button_DialogEditTextSureCancle)
    Button buttonDialogEditTextSureCancle;
    @BindView(R.id.button_DialogWheelYearMonthDay)
    Button mButtonDialogWheelYearMonthDay;
    @BindView(R.id.button_DialogShapeLoading)
    Button mButtonDialogShapeLoading;
    @BindView(R.id.button_DialogLoadingProgressAcfunVideo)
    Button mButtonDialogLoadingProgressAcfunVideo;
    @BindView(R.id.iv_finish)
    ImageView mIvFinish;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.activity_dialog)
    LinearLayout mActivityDialog;


    private Context context;

    private DialogWheelYearMonthDay mDialogWheelYearMonthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarUtils.noTitle(this);
        setContentView(R.layout.activity_dialog);
        RxBarUtils.setTransparentStatusBar(this);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        mTvTitle.setText("常用Dialog展示");
    }

    private void initWheelYearMonthDayDialog() {
        // ------------------------------------------------------------------选择日期开始
        mDialogWheelYearMonthDay = new DialogWheelYearMonthDay(this, 1994,2017);
        mDialogWheelYearMonthDay.getTv_sure().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (mDialogWheelYearMonthDay.getCheckBox_day().isChecked()) {
                            mButtonDialogWheelYearMonthDay.setText(
                                    mDialogWheelYearMonthDay.getSelectorYear() + "年"
                                            + mDialogWheelYearMonthDay.getSelectorMonth() + "月"
                                            + mDialogWheelYearMonthDay.getSelectorDay() + "日");
                        } else {
                            mButtonDialogWheelYearMonthDay.setText(
                                    mDialogWheelYearMonthDay.getSelectorYear() + "年"
                                            + mDialogWheelYearMonthDay.getSelectorMonth() + "月");
                        }
                        mDialogWheelYearMonthDay.cancel();
                    }
                });
        mDialogWheelYearMonthDay.getTv_cancle().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mDialogWheelYearMonthDay.cancel();
                    }
                });
        // ------------------------------------------------------------------选择日期结束
    }

    @OnClick({R.id.button_DialogSure, R.id.button_DialogSureCancle, R.id.button_DialogEditTextSureCancle, R.id.button_DialogWheelYearMonthDay, R.id.button_DialogShapeLoading, R.id.button_DialogLoadingProgressAcfunVideo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_DialogSure:
                final RxDialogSure rxDialogSure = new RxDialogSure(context);//提示弹窗
                rxDialogSure.getTv_content().setMovementMethod(ScrollingMovementMethod.getInstance());
                rxDialogSure.getTv_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSure.cancel();
                    }
                });
                rxDialogSure.show();
                break;
            case R.id.button_DialogSureCancle:
                final RxDialogSureCancle rxDialogSureCancle = new RxDialogSureCancle(context);//提示弹窗
                rxDialogSureCancle.getTv_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancle.cancel();
                    }
                });
                rxDialogSureCancle.getTv_cancle().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancle.cancel();
                    }
                });
                rxDialogSureCancle.show();
                break;
            case R.id.button_DialogEditTextSureCancle:
                final RxDialogEditTextSureCancle rxDialogEditTextSureCancle = new RxDialogEditTextSureCancle(context);//提示弹窗
                rxDialogEditTextSureCancle.getTv_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogEditTextSureCancle.cancel();
                    }
                });
                rxDialogEditTextSureCancle.getTv_cancle().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogEditTextSureCancle.cancel();
                    }
                });
                rxDialogEditTextSureCancle.show();
                break;
            case R.id.button_DialogWheelYearMonthDay:
                if (mDialogWheelYearMonthDay == null) {
                    initWheelYearMonthDayDialog();
                }
                mDialogWheelYearMonthDay.show();
                break;
            case R.id.button_DialogShapeLoading:
                ShapeLoadingDialog shapeLoadingDialog = new ShapeLoadingDialog(this);
                shapeLoadingDialog.show();
                break;
            case R.id.button_DialogLoadingProgressAcfunVideo:
                new RxDialogLoadingProgressAcfunVideo(this).show();
                break;
        }
    }

    @OnClick(R.id.iv_finish)
    public void onClick() {
        finish();
    }
}
