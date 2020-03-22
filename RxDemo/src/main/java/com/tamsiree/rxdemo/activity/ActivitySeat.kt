package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxActivityTool
import com.tamsiree.rxkit.RxDeviceTool
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_seat.*

/**
 * @author tamsiree
 */
class ActivitySeat : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat)
        RxDeviceTool.setPortrait(this)

    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        btn_movie.setOnClickListener { RxActivityTool.skipActivity(this@ActivitySeat, ActivityMovieSeat::class.java) }
        btn_flight.setOnClickListener { RxActivityTool.skipActivity(this@ActivitySeat, ActivityFlightSeat::class.java) }
    }

    override fun initData() {

    }

}