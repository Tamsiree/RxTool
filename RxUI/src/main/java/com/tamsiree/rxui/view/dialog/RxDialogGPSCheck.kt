package com.tamsiree.rxui.view.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import com.tamsiree.rxkit.RxLocationTool.openGpsSettings

/**
 * @author tamsiree
 * @date 2016/7/19
 * 检测Gps状态
 */
class RxDialogGPSCheck : RxDialogSureCancel {
    constructor(context: Context?, themeResId: Int) : super(context, themeResId) {
        initView()
    }

    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {
        initView()
    }

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Activity?) : super(context) {
        initView()
    }

    constructor(context: Context?, alpha: Float, gravity: Int) : super(context, alpha, gravity) {
        initView()
    }

    private fun initView() {
        titleView.setBackgroundDrawable(null)
        setTitle("GPS未打开")
        titleView.textSize = 16f
        titleView.setTextColor(Color.BLACK)
        setContent("您需要在系统设置中打开GPS方可采集数据")
        sureView.text = "去设置"
        cancelView.text = "知道了"
        sureView.setOnClickListener {
            openGpsSettings(mContext)
            cancel()
        }
        cancelView.setOnClickListener { cancel() }
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }
}