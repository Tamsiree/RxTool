package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityRxToast : ActivityBase() {
    @JvmField
    @BindView(R.id.button_error_toast)
    var mButtonErrorToast: Button? = null

    @JvmField
    @BindView(R.id.button_success_toast)
    var mButtonSuccessToast: Button? = null

    @JvmField
    @BindView(R.id.button_info_toast)
    var mButtonInfoToast: Button? = null

    @JvmField
    @BindView(R.id.button_warning_toast)
    var mButtonWarningToast: Button? = null

    @JvmField
    @BindView(R.id.button_normal_toast_wo_icon)
    var mButtonNormalToastWoIcon: Button? = null

    @JvmField
    @BindView(R.id.button_normal_toast_w_icon)
    var mButtonNormalToastWIcon: Button? = null

    @JvmField
    @BindView(R.id.activity_main)
    var mActivityMain: RelativeLayout? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_toast)
        ButterKnife.bind(this)
        setPortrait(this)
        initView()
    }

    protected fun initView() {
        mRxTitle!!.setOnClickListener { finish() }
    }

    @OnClick(R.id.button_error_toast, R.id.button_success_toast, R.id.button_info_toast, R.id.button_warning_toast, R.id.button_normal_toast_wo_icon, R.id.button_normal_toast_w_icon)
    fun onClick(view: View) {
        when (view.id) {
            R.id.button_error_toast -> RxToast.error(mContext!!, "这是一个提示错误的Toast！", Toast.LENGTH_SHORT, true).show()
            R.id.button_success_toast -> RxToast.success(mContext!!, "这是一个提示成功的Toast!", Toast.LENGTH_SHORT, true).show()
            R.id.button_info_toast -> RxToast.info(mContext!!, "这是一个提示信息的Toast.", Toast.LENGTH_SHORT, true).show()
            R.id.button_warning_toast -> RxToast.warning(mContext!!, "这是一个提示警告的Toast.", Toast.LENGTH_SHORT, true).show()
            R.id.button_normal_toast_wo_icon -> RxToast.normal(mContext!!, "这是一个普通的没有ICON的Toast").show()
            R.id.button_normal_toast_w_icon -> {
                val icon = resources.getDrawable(R.drawable.clover)
                RxToast.normal(mContext!!, "这是一个普通的包含ICON的Toast", icon).show()
            }
            else -> {
            }
        }
    }
}