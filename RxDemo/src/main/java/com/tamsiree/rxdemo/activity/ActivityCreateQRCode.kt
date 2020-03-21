package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxfeature.tool.RxBarCode
import com.tamsiree.rxfeature.tool.RxQRCode
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityCreateQRCode : ActivityBase(), View.OnClickListener {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.iv_linecode)
    var mIvLinecode: ImageView? = null

    @JvmField
    @BindView(R.id.iv_code)
    var mIvCode: ImageView? = null

    @JvmField
    @BindView(R.id.imageView1)
    var mImageView1: ImageView? = null

    @JvmField
    @BindView(R.id.textView2)
    var mTextView2: TextView? = null

    @JvmField
    @BindView(R.id.tv_time_second)
    var mTvTimeSecond: TextView? = null

    @JvmField
    @BindView(R.id.ll_refresh)
    var mLlRefresh: LinearLayout? = null

    @SuppressLint("HandlerLeak")
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                60000 -> initData()
                else -> {
                }
            }
        }
    }
    private val time_second = 0
    private val second = 60
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(mContext!!)
        setContentView(R.layout.activity_create_qrcode)
        setTransparentStatusBar(mContext!!)
        ButterKnife.bind(this)
        setPortrait(this)
        initView()
        initData()
        AuthCode(mTvTimeSecond, second)
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
                    AuthCode(mTvTimeSecond, second)
                }
            }
        }
        Handler.postDelayed(mRunnable, 1000)
    }

    private fun initView() {
        mRxTitle!!.setLeftFinish(mContext)
        mRxTitle!!.title = "动态生成码"
        mLlRefresh!!.setOnClickListener(this)
    }

    private fun initData() {
        // TODO Auto-generated method stub
        RxQRCode.createQRCode("时间戳:" + System.currentTimeMillis(), 800, 800, mIvCode)
        mIvLinecode!!.setImageBitmap(RxBarCode.createBarCode("" + System.currentTimeMillis(), 1000, 300))
    }

    override fun onClick(arg0: View) {
        when (arg0.id) {
            R.id.ll_refresh -> {
                Handler.removeCallbacks(mRunnable)
                initData()
                mTvTimeSecond!!.text = second.toString() + ""
                AuthCode(mTvTimeSecond, second)
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