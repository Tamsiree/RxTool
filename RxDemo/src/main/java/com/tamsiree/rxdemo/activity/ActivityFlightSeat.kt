package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxSeatAirplane
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityFlightSeat : ActivityBase() {
    @JvmField
    @BindView(R.id.fsv)
    var mFlightSeatView: RxSeatAirplane? = null

    @JvmField
    @BindView(R.id.btn_clear)
    var mBtnClear: Button? = null

    @JvmField
    @BindView(R.id.btn_zoom)
    var mBtnZoom: Button? = null

    @JvmField
    @BindView(R.id.btn_goto)
    var mBtnGoto: Button? = null

    @JvmField
    @BindView(R.id.activity_flight_seat)
    var mActivityFlightSeat: LinearLayout? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setTransparentStatusBar(this)
        setContentView(R.layout.activity_flight_seat)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
        initView()
        setTestData()
    }

    private fun initView() {
        mFlightSeatView!!.setMaxSelectStates(10)
    }

    fun zoom(v: View?) {
        mFlightSeatView!!.startAnim(true)
    }

    fun gotoposition(v: View?) {
        mFlightSeatView!!.goCabinPosition(RxSeatAirplane.CabinPosition.Middle)
    }

    fun clear(v: View?) {
        mFlightSeatView!!.setEmptySelecting()
    }

    private fun setTestData() {
        for (i in 0..5) {
            var j = 0
            while (j < 9) {
                mFlightSeatView!!.setSeatSelected(j, i)
                j = j + 2
            }
        }
        for (i in 0..9) {
            var j = 0
            while (j < 8) {
                mFlightSeatView!!.setSeatSelected(i + 20, j)
                j = j + 2
            }
        }
        for (i in 0..9) {
            var j = 0
            while (j < 8) {
                mFlightSeatView!!.setSeatSelected(i + 35, j)
                j = j + 3
            }
        }
        for (i in 11..19) {
            var j = 0
            while (j < 8) {
                mFlightSeatView!!.setSeatSelected(i + 35, j)
                j = j + 4
            }
        }
        mFlightSeatView!!.invalidate()
    }

    @OnClick(R.id.btn_clear, R.id.btn_zoom, R.id.btn_goto)
    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_clear -> clear(view)
            R.id.btn_zoom -> zoom(view)
            R.id.btn_goto -> gotoposition(view)
            else -> {
            }
        }
    }
}