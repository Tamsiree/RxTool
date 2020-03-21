package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxActivityTool
import com.tamsiree.rxkit.RxDeviceTool
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivitySeat : ActivityBase() {
    @JvmField
    @BindView(R.id.btn_movie)
    var mBtnMovie: Button? = null

    @JvmField
    @BindView(R.id.btn_flight)
    var mBtnFlight: Button? = null

    @JvmField
    @BindView(R.id.activity_seat)
    var mActivitySeat: LinearLayout? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat)
        ButterKnife.bind(this)
        RxDeviceTool.setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
    }

    @OnClick(R.id.btn_movie, R.id.btn_flight)
    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_movie -> RxActivityTool.skipActivity(this@ActivitySeat, ActivityMovieSeat::class.java)
            R.id.btn_flight -> RxActivityTool.skipActivity(this@ActivitySeat, ActivityFlightSeat::class.java)
        }
    }
}