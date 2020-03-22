package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.TextView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxfeature.tool.RxBarCode
import com.tamsiree.rxfeature.tool.RxQRCode
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_create_qrcode.*

/**
 * @author tamsiree
 */
class ActivityCreateQRCode : ActivityBase(), View.OnClickListener {

    @SuppressLint("HandlerLeak")
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                60000 -> initData()
            }
        }
    }
    private val time_second = 0
    private val second = 60
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(mContext)
        setContentView(R.layout.activity_create_qrcode)
        setTransparentStatusBar(mContext)
        setPortrait(this)
    }

    private fun AuthCode(view: TextView?, timeSecond: Int) {
        mRunnable = object : Runnable {
            var mSumNum = timeSecond
            override fun run() {
                Handler.postDelayed(mRunnable, 1000)
                view!!.text = mSumNum.toString() + ""
                view.isEnabled = false
                mSumNum--
                if (mSumNum < 0) {
                    view.text = 0.toString() + ""
                    view.isEnabled = true
                    val message = Message()
                    message.what = 60000
                    mHandler.sendMessage(message)
                    // 干掉这个定时器，下次减不会累加
                    Handler.removeCallbacks(mRunnable)
                    AuthCode(tv_time_second, second)
                }
            }
        }
        Handler.postDelayed(mRunnable, 1000)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        rx_title.title = "动态生成码"
        ll_refresh.setOnClickListener(this)
    }

    override fun initData() {
        RxQRCode.createQRCode("时间戳:" + System.currentTimeMillis(), 800, 800, iv_code)
        iv_linecode.setImageBitmap(RxBarCode.createBarCode("" + System.currentTimeMillis(), 1000, 300))
        AuthCode(tv_time_second, second)
    }

    override fun onClick(arg0: View) {
        when (arg0.id) {
            R.id.ll_refresh -> {
                Handler.removeCallbacks(mRunnable)
                initData()
                tv_time_second.text = second.toString() + ""
                AuthCode(tv_time_second, second)
            }
            else -> {
            }
        }
    }

    companion object {
        private val Handler = Handler()
        private var mRunnable: Runnable? = null
    }
}