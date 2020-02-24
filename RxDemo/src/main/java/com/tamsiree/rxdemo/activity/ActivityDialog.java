package com.tamsiree.rxdemo.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxkit.RxBarTool;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.RxTitle;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogAcfunVideoLoading;
import com.tamsiree.rxui.view.dialog.RxDialogDate;
import com.tamsiree.rxui.view.dialog.RxDialogEditSureCancel;
import com.tamsiree.rxui.view.dialog.RxDialogLoading;
import com.tamsiree.rxui.view.dialog.RxDialogScaleView;
import com.tamsiree.rxui.view.dialog.RxDialogShapeLoading;
import com.tamsiree.rxui.view.dialog.RxDialogSure;
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author tamsiree
 */
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
    private RxDialogDate mRxDialogDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_dialog);
        RxBarTool.setTransparentStatusBar(this);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);
        initView();
    }

    private void initView() {
        mRxTitle.setLeftFinish(mContext);
    }

    private void initWheelYearMonthDayDialog() {
        // ------------------------------------------------------------------选择日期开始
        mRxDialogDate = new RxDialogDate(this, 1994, 2018);
        mRxDialogDate.getSureView().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (mRxDialogDate.getCheckBoxDay().isChecked()) {
                            mButtonDialogWheelYearMonthDay.setText(
                                    mRxDialogDate.getSelectorYear() + "年"
                                            + mRxDialogDate.getSelectorMonth() + "月"
                                            + mRxDialogDate.getSelectorDay() + "日");
                        } else {
                            mButtonDialogWheelYearMonthDay.setText(
                                    mRxDialogDate.getSelectorYear() + "年"
                                            + mRxDialogDate.getSelectorMonth() + "月");
                        }
                        mRxDialogDate.cancel();
                    }
                });
        mRxDialogDate.getCancleView().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mRxDialogDate.cancel();
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
                ImageView pageItem = view1.findViewById(R.id.page_item);
                pageItem.setImageResource(R.drawable.coin);
                rxDialog.setContentView(view1);
                rxDialog.show();
                break;
            case R.id.button_DialogSure:
                //提示弹窗
                final RxDialogSure rxDialogSure = new RxDialogSure(mContext);
                rxDialogSure.getLogoView().setImageResource(R.drawable.logo);
                rxDialogSure.getSureView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSure.cancel();
                    }
                });
                rxDialogSure.show();
                break;
            case R.id.button_DialogSureCancle:
                //提示弹窗
                final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(mContext);
                rxDialogSureCancel.getTitleView().setBackgroundResource(R.drawable.logo);
                rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.show();
                break;
            case R.id.button_DialogEditTextSureCancle:
                //提示弹窗
                final RxDialogEditSureCancel rxDialogEditSureCancel = new RxDialogEditSureCancel(mContext);
                rxDialogEditSureCancel.getTitleView().setBackgroundResource(R.drawable.logo);
                rxDialogEditSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogEditSureCancel.cancel();
                    }
                });
                rxDialogEditSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogEditSureCancel.cancel();
                    }
                });
                rxDialogEditSureCancel.show();
                break;
            case R.id.button_DialogWheelYearMonthDay:
                if (mRxDialogDate == null) {
                    initWheelYearMonthDayDialog();
                }
                mRxDialogDate.show();
                break;
            case R.id.button_DialogShapeLoading:
                RxDialogShapeLoading rxDialogShapeLoading = new RxDialogShapeLoading(this);
                rxDialogShapeLoading.show();
                break;
            case R.id.button_DialogLoadingProgressAcfunVideo:
                new RxDialogAcfunVideoLoading(this).show();
                break;
            case R.id.button_DialogLoadingspinkit:
                RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
                rxDialogLoading.show();
                //rxDialogLoading.cancel(RxDialogLoading.RxCancelType.error,"");
                break;
            case R.id.button_DialogScaleView:
                RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(mContext);
                rxDialogScaleView.setImage("squirrel.jpg",true);
                rxDialogScaleView.show();
                break;
            default:
                break;
        }
    }
}
