package com.vondear.tools.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.rxtools.RxBarUtils;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.vondear.rxtools.view.dialog.RxDialogEditTextSureCancle;
import com.vondear.rxtools.view.dialog.RxDialogLoadingProgressAcfunVideo;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancle;
import com.vondear.rxtools.view.dialog.RxDialogShapeLoading;
import com.vondear.rxtools.view.dialog.RxDialogWheelYearMonthDay;
import com.vondear.tools.R;

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
    @BindView(R.id.button_tran)
    Button mButtonTran;


    private Context context;

    private RxDialogWheelYearMonthDay mRxDialogWheelYearMonthDay;

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
        mRxDialogWheelYearMonthDay = new RxDialogWheelYearMonthDay(this, 1994, 2017);
        mRxDialogWheelYearMonthDay.getTv_sure().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (mRxDialogWheelYearMonthDay.getCheckBox_day().isChecked()) {
                            mButtonDialogWheelYearMonthDay.setText(
                                    mRxDialogWheelYearMonthDay.getSelectorYear() + "年"
                                            + mRxDialogWheelYearMonthDay.getSelectorMonth() + "月"
                                            + mRxDialogWheelYearMonthDay.getSelectorDay() + "日");
                        } else {
                            mButtonDialogWheelYearMonthDay.setText(
                                    mRxDialogWheelYearMonthDay.getSelectorYear() + "年"
                                            + mRxDialogWheelYearMonthDay.getSelectorMonth() + "月");
                        }
                        mRxDialogWheelYearMonthDay.cancel();
                    }
                });
        mRxDialogWheelYearMonthDay.getTv_cancle().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mRxDialogWheelYearMonthDay.cancel();
                    }
                });
        // ------------------------------------------------------------------选择日期结束
    }

    @OnClick({R.id.button_tran,R.id.button_DialogSure, R.id.button_DialogSureCancle, R.id.button_DialogEditTextSureCancle, R.id.button_DialogWheelYearMonthDay, R.id.button_DialogShapeLoading, R.id.button_DialogLoadingProgressAcfunVideo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_tran:
                RxDialog rxDialog = new RxDialog(context,R.style.tran_dialog);
                View view1 = LayoutInflater.from(context).inflate(R.layout.image,null);
                ImageView page_item = (ImageView) view1.findViewById(R.id.page_item);
                page_item.setImageResource(R.drawable.coin);
                rxDialog.setContentView(view1);
                rxDialog.show();
                break;
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
                if (mRxDialogWheelYearMonthDay == null) {
                    initWheelYearMonthDayDialog();
                }
                mRxDialogWheelYearMonthDay.show();
                break;
            case R.id.button_DialogShapeLoading:
                RxDialogShapeLoading rxDialogShapeLoading = new RxDialogShapeLoading(this);
                rxDialogShapeLoading.show();
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
