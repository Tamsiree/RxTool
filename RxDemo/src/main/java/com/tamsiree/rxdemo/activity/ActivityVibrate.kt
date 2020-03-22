package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxVibrateTool.vibrateComplicated
import com.tamsiree.rxkit.RxVibrateTool.vibrateOnce
import com.tamsiree.rxkit.RxVibrateTool.vibrateStop
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_vibrate.*

/**
 * @author tamsiree
 */
class ActivityVibrate : ActivityBase() {

    private val temp = longArrayOf(100, 10, 100, 1000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vibrate)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)

        btn_vibrate_once.setOnClickListener { vibrateOnce(this, 2000) }
        btn_vibrate_Complicated.setOnClickListener { vibrateComplicated(this, temp, 0) }
        btn_vibrate_stop.setOnClickListener { vibrateStop() }
    }

    override fun initData() {

    }

}