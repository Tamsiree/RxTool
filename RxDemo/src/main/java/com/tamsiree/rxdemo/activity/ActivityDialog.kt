package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.dialog.*
import kotlinx.android.synthetic.main.activity_dialog.*

/**
 * @author tamsiree
 */
class ActivityDialog : ActivityBase() {

    private var mRxDialogDate: RxDialogDate? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setContentView(R.layout.activity_dialog)
        setTransparentStatusBar(this)
        setPortrait(this)
    }


    override fun initView() {
        rx_title.setLeftFinish(mContext)

        button_tran.setOnClickListener {
            val rxDialog = RxDialog(mContext, R.style.tran_dialog)
            val view1 = LayoutInflater.from(mContext).inflate(R.layout.image, null)
            val pageItem = view1.findViewById<ImageView>(R.id.page_item)
            pageItem.setImageResource(R.drawable.coin)
            rxDialog.setContentView(view1)
            rxDialog.show()
        }
        button_DialogSure.setOnClickListener {
            //提示弹窗
            val rxDialogSure = RxDialogSure(mContext)
            rxDialogSure.logoView.setImageResource(R.drawable.logo)
            rxDialogSure.sureView.setOnClickListener { rxDialogSure.cancel() }
            rxDialogSure.show()
        }
        button_DialogSureCancle.setOnClickListener {
            //提示弹窗
            val rxDialogSureCancel = RxDialogSureCancel(mContext)
            rxDialogSureCancel.titleView.setBackgroundResource(R.drawable.logo)
            rxDialogSureCancel.sureView.setOnClickListener { rxDialogSureCancel.cancel() }
            rxDialogSureCancel.cancelView.setOnClickListener { rxDialogSureCancel.cancel() }
            rxDialogSureCancel.show()
        }
        button_DialogEditTextSureCancle.setOnClickListener {
            //提示弹窗
            val rxDialogEditSureCancel = RxDialogEditSureCancel(mContext)
            rxDialogEditSureCancel.titleView.setBackgroundResource(R.drawable.logo)
            rxDialogEditSureCancel.sureView.setOnClickListener { rxDialogEditSureCancel.cancel() }
            rxDialogEditSureCancel.cancelView.setOnClickListener { rxDialogEditSureCancel.cancel() }
            rxDialogEditSureCancel.show()
        }
        button_DialogWheelYearMonthDay.setOnClickListener {
            if (mRxDialogDate == null) {
                initWheelYearMonthDayDialog()
            }
            mRxDialogDate!!.show()
        }
        button_DialogShapeLoading.setOnClickListener {
            val rxDialogShapeLoading = RxDialogShapeLoading(this)
            rxDialogShapeLoading.show()
        }
        button_DialogLoadingProgressAcfunVideo.setOnClickListener { RxDialogAcfunVideoLoading(this).show() }
        button_DialogLoadingspinkit.setOnClickListener {
            val rxDialogLoading = RxDialogLoading(mContext)
            rxDialogLoading.show()
        }
        button_DialogScaleView.setOnClickListener {
            val rxDialogScaleView = RxDialogScaleView(mContext)
            rxDialogScaleView.setImage("squirrel.jpg", true)
            rxDialogScaleView.show()
        }
    }

    override fun initData() {

    }

    private fun initWheelYearMonthDayDialog() {
        // ------------------------------------------------------------------选择日期开始
        mRxDialogDate = RxDialogDate(this, 1994)
        mRxDialogDate?.sureView?.setOnClickListener {
            if (mRxDialogDate?.checkBoxDay?.isChecked!!) {
                button_DialogWheelYearMonthDay.text = (mRxDialogDate?.selectorYear.toString() + "年"
                        + mRxDialogDate?.selectorMonth + "月"
                        + mRxDialogDate?.selectorDay + "日")
            } else {
                button_DialogWheelYearMonthDay.text = (mRxDialogDate?.selectorYear.toString() + "年"
                        + mRxDialogDate?.selectorMonth + "月")
            }
            mRxDialogDate!!.cancel()
        }
        mRxDialogDate?.cancleView?.setOnClickListener { mRxDialogDate?.cancel() }
        // ------------------------------------------------------------------选择日期结束
    }

}