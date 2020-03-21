package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxNetSpeedView
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityNetSpeed : ActivityBase() {
    @JvmField
    @BindView(R.id.button2)
    var mButton2: Button? = null

    @JvmField
    @BindView(R.id.button3)
    var mButton3: Button? = null

    @JvmField
    @BindView(R.id.activity_net_speed)
    var mActivityNetSpeed: RelativeLayout? = null

    @JvmField
    @BindView(R.id.rx_net_speed_view)
    var mRxNetSpeedView: RxNetSpeedView? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_speed)
        ButterKnife.bind(this)
        mRxTitle!!.setLeftFinish(mContext)
        setPortrait(this)
    }

    @OnClick(R.id.button2, R.id.button3)
    fun onClick(view: View) {
        when (view.id) {
            R.id.button2 -> mRxNetSpeedView!!.isMulti = false
            R.id.button3 -> mRxNetSpeedView!!.isMulti = true
        }
    }
}