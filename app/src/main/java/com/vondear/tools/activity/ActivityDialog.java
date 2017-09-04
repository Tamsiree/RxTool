package com.vondear.tools.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vondear.rxtools.RxBarUtils;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.vondear.rxtools.view.dialog.RxDialogAcfunVideoLoading;
import com.vondear.rxtools.view.dialog.RxDialogEditSureCancel;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogScaleView;
import com.vondear.rxtools.view.dialog.RxDialogShapeLoading;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.dialog.RxDialogWheelYearMonthDay;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDialog extends ActivityBase {


    @BindView(R.id.button_tran)
    Button mButtonTran;
    @BindView(R.id.button_DialogSure)
    Button mButtonDialogSure;
    @BindView(R.id.button_DialogSureCancle)
    Button mButtonDialogSureCancle;
    @BindView(R.id.button_DialogEditTextSureCancle)
    Button mButtonDialogEditTextSureCancle;
    @BindView(R.id.button_DialogWheelYearMonthDay)
    Button mButtonDialogWheelYearMonthDay;
    @BindView(R.id.button_DialogShapeLoading)
    Button mButtonDialogShapeLoading;
    @BindView(R.id.button_DialogLoadingProgressAcfunVideo)
    Button mButtonDialogLoadingProgressAcfunVideo;
    @BindView(R.id.activity_dialog)
    LinearLayout mActivityDialog;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.button_DialogLoadingspinkit)
    Button mButtonDialogLoadingspinkit;
    @BindView(R.id.button_DialogScaleView)
    Button mButtonDialogScaleView;
    private RxDialogWheelYearMonthDay mRxDialogWheelYearMonthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarUtils.noTitle(this);
        setContentView(R.layout.activity_dialog);
        RxBarUtils.setTransparentStatusBar(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mRxTitle.setLeftFinish(mContext);
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

    @OnClick({
            R.id.button_DialogScaleView,
            R.id.button_DialogLoadingspinkit,
            R.id.button_tran,
            R.id.button_DialogSure,
            R.id.button_DialogSureCancle,
            R.id.button_DialogEditTextSureCancle,
            R.id.button_DialogWheelYearMonthDay,
            R.id.button_DialogShapeLoading,
            R.id.button_DialogLoadingProgressAcfunVideo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_tran:
                RxDialog rxDialog = new RxDialog(mContext, R.style.tran_dialog);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.image, null);
                ImageView page_item = (ImageView) view1.findViewById(R.id.page_item);
                page_item.setImageResource(R.drawable.coin);
                rxDialog.setContentView(view1);
                rxDialog.show();
                break;
            case R.id.button_DialogSure:
                final RxDialogSure rxDialogSure = new RxDialogSure(mContext);//提示弹窗
                rxDialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSure.cancel();
                    }
                });
                rxDialogSure.show();
                break;
            case R.id.button_DialogSureCancle:
                final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(mContext);//提示弹窗
                rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.show();
                break;
            case R.id.button_DialogEditTextSureCancle:
                final RxDialogEditSureCancel rxDialogEditSureCancel = new RxDialogEditSureCancel(mContext);//提示弹窗
                rxDialogEditSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogEditSureCancel.cancel();
                    }
                });
                rxDialogEditSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogEditSureCancel.cancel();
                    }
                });
                rxDialogEditSureCancel.show();
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
                new RxDialogAcfunVideoLoading(this).show();
                break;
            case R.id.button_DialogLoadingspinkit:
                new RxDialogLoading(mContext).show();
                break;
            case R.id.button_DialogScaleView:
                RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(mContext);
                rxDialogScaleView.setImageAssets("squirrel.jpg");
                rxDialogScaleView.show();
                break;
        }
    }
}
