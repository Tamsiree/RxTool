package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxVibrateTool.vibrateComplicated
import com.tamsiree.rxkit.RxVibrateTool.vibrateOnce
import com.tamsiree.rxkit.RxVibrateTool.vibrateStop
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityVibrate : ActivityBase() {
    @JvmField
    @BindView(R.id.btn_vibrate_once)
    var mBtnVibrateOnce: Button? = null

    @JvmField
    @BindView(R.id.btn_vibrate_Complicated)
    var mBtnVibrateComplicated: Button? = null

    @JvmField
    @BindView(R.id.btn_vibrate_stop)
    var mBtnVibrateStop: Button? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    private val temp = longArrayOf(100, 10, 100, 1000)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vibrate)
        setPortrait(this)
    }

    override fun initView() {
        ButterKnife.bind(this)
        mRxTitle?.setLeftFinish(mContext)
    }

    override fun initData() {

    }

    @OnClick(R.id.btn_vibrate_once, R.id.btn_vibrate_Complicated, R.id.btn_vibrate_stop)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_vibrate_once -> vibrateOnce(this, 2000)
            R.id.btn_vibrate_Complicated -> vibrateComplicated(this, temp, 0)
            R.id.btn_vibrate_stop -> vibrateStop()
        }
    }
}