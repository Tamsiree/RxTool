package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.widget.Toast
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_rx_toast.*

/**
 * @author tamsiree
 */
class ActivityRxToast : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_toast)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setOnClickListener { finish() }
        button_error_toast.setOnClickListener { RxToast.error(mContext, "这是一个提示错误的Toast！", Toast.LENGTH_SHORT, true).show() }
        button_success_toast.setOnClickListener { RxToast.success(mContext, "这是一个提示成功的Toast!", Toast.LENGTH_SHORT, true).show() }
        button_info_toast.setOnClickListener { RxToast.info(mContext, "这是一个提示信息的Toast.", Toast.LENGTH_SHORT, true).show() }
        button_warning_toast.setOnClickListener { RxToast.warning(mContext, "这是一个提示警告的Toast.", Toast.LENGTH_SHORT, true).show() }
        button_normal_toast_wo_icon.setOnClickListener { RxToast.normal(mContext, "这是一个普通的没有ICON的Toast").show() }
        button_normal_toast_w_icon.setOnClickListener {
            val icon = resources.getDrawable(R.drawable.clover)
            RxToast.normal(mContext, "这是一个普通的包含ICON的Toast", icon).show()
        }
    }

    override fun initData() {

    }

}