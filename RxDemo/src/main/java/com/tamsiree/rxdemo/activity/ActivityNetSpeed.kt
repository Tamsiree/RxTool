package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_net_speed.*

/**
 * @author tamsiree
 */
class ActivityNetSpeed : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_speed)
        rx_title.setLeftFinish(mContext)
        setPortrait(this)
    }

    override fun initView() {
        button2.setOnClickListener { rx_net_speed_view.isMulti = false }
        button3.setOnClickListener { rx_net_speed_view.isMulti = true }
    }

    override fun initData() {

    }

}