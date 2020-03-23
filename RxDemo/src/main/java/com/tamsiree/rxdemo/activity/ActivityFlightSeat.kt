package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxSeatAirplane
import kotlinx.android.synthetic.main.activity_flight_seat.*

/**
 * @author tamsiree
 */
class ActivityFlightSeat : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setTransparentStatusBar(this)
        setContentView(R.layout.activity_flight_seat)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        fsv.setMaxSelectStates(10)

        btn_clear.setOnClickListener { clear() }
        btn_zoom.setOnClickListener { zoom() }
        btn_goto.setOnClickListener { gotoposition() }

    }

    override fun initData() {
        setTestData()
    }

    fun zoom() {
        fsv.startAnim(true)
    }

    fun gotoposition() {
        fsv.goCabinPosition(RxSeatAirplane.CabinPosition.Middle)
    }

    fun clear() {
        fsv.setEmptySelecting()
    }

    private fun setTestData() {
        for (i in 0..5) {
            var j = 0
            while (j < 9) {
                fsv.setSeatSelected(j, i)
                j += 2
            }
        }
        for (i in 0..9) {
            var j = 0
            while (j < 8) {
                fsv.setSeatSelected(i + 20, j)
                j += 2
            }
        }
        for (i in 0..9) {
            var j = 0
            while (j < 8) {
                fsv.setSeatSelected(i + 35, j)
                j += 3
            }
        }
        for (i in 11..19) {
            var j = 0
            while (j < 8) {
                fsv.setSeatSelected(i + 35, j)
                j += 4
            }
        }
        fsv.invalidate()
    }

}