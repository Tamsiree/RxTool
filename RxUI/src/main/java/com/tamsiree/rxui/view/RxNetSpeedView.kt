package com.tamsiree.rxui.view

import android.content.Context
import android.net.TrafficStats
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.tamsiree.rxui.R
import java.text.DecimalFormat

/**
 * @author tamsiree
 * @date 2017/2/15
 */
class RxNetSpeedView : FrameLayout {
    private var rlLayoutBig: RelativeLayout? = null
    private var tvMobileTx: TextView? = null
    private var tvMobileRx: TextView? = null
    private var tvWlanTx: TextView? = null
    private var tvWlanRx: TextView? = null
    private var tvSum: TextView? = null
    private var mobileTx: TextView? = null
    private var mobileRx: TextView? = null
    private var wlanTx: TextView? = null
    private var wlanRx: TextView? = null
    var timeSpan = 2000.0
    private var rxtxTotal: Long = 0
    private var mobileRecvSum: Long = 0
    private var mobileSendSum: Long = 0
    private var wlanRecvSum: Long = 0
    private var wlanSendSum: Long = 0
    private val exitTime: Long = 0
    private var mTextColor = 0
    private var mTextSize = 0
    private var isMulti = false
    private val showFloatFormat = DecimalFormat("0.00")
    var timeInterval: Long = 500

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.rx_netspeed_view, this)
        rlLayoutBig = findViewById(R.id.rlLayoutBig)
        tvMobileTx = findViewById(R.id.tvMobileTx)
        tvMobileRx = findViewById(R.id.tvMobileRx)
        tvWlanTx = findViewById(R.id.tvWlanTx)
        tvWlanRx = findViewById(R.id.tvWlanRx)
        tvSum = findViewById(R.id.tvSum)
        mobileTx = findViewById(R.id.MobileTx)
        mobileRx = findViewById(R.id.MobileRx)
        wlanTx = findViewById(R.id.WlanTx)
        wlanRx = findViewById(R.id.WlanRx)

        //获得这个控件对应的属性。
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.RxNetSpeedView)
        try {
            //获得属性值
            mTextColor = a.getColor(R.styleable.RxNetSpeedView_RxTextColor, resources.getColor(R.color.white))
            //TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())
            mTextSize = a.getDimensionPixelSize(R.styleable.RxNetSpeedView_RxTextSize, 12)
            isMulti = a.getBoolean(R.styleable.RxNetSpeedView_isMulti, false)
        } finally {
            //回收这个对象
            a.recycle()
        }
        setTextColor(mTextColor)
        setTextSize(mTextSize)
        setMulti(isMulti)

        //延迟调用
        mHandler.postDelayed(task, timeInterval)
    }

    fun setTextSize(textSize: Int) {
        if (textSize != 0) {
            tvMobileTx!!.textSize = textSize.toFloat()
            tvMobileRx!!.textSize = textSize.toFloat()
            tvWlanTx!!.textSize = textSize.toFloat()
            tvWlanRx!!.textSize = textSize.toFloat()
            tvSum!!.textSize = textSize.toFloat()
            mobileTx!!.textSize = textSize.toFloat()
            mobileRx!!.textSize = textSize.toFloat()
            wlanTx!!.textSize = textSize.toFloat()
            wlanRx!!.textSize = textSize.toFloat()
        }
    }

    fun setTextColor(textColor: Int) {
        if (textColor != 0) {
            tvMobileTx!!.setTextColor(textColor)
            tvMobileRx!!.setTextColor(textColor)
            tvWlanTx!!.setTextColor(textColor)
            tvWlanRx!!.setTextColor(textColor)
            tvSum!!.setTextColor(textColor)
            mobileTx!!.setTextColor(textColor)
            mobileRx!!.setTextColor(textColor)
            wlanTx!!.setTextColor(textColor)
            wlanRx!!.setTextColor(textColor)
        }
    }

    constructor(context: Context?) : super(context!!)

    fun updateViewData() {
        val tempSum = (TrafficStats.getTotalRxBytes()
                + TrafficStats.getTotalTxBytes())
        val rxtxLast = tempSum - rxtxTotal
        val totalSpeed = rxtxLast * 1000 / timeSpan
        rxtxTotal = tempSum
        val tempMobileRx = TrafficStats.getMobileRxBytes()
        val tempMobileTx = TrafficStats.getMobileTxBytes()
        val tempWlanRx = TrafficStats.getTotalRxBytes() - tempMobileRx
        val tempWlanTx = TrafficStats.getTotalTxBytes() - tempMobileTx
        val mobileLastRecv = tempMobileRx - mobileRecvSum
        val mobileLastSend = tempMobileTx - mobileSendSum
        val wlanLastRecv = tempWlanRx - wlanRecvSum
        val wlanLastSend = tempWlanTx - wlanSendSum
        val mobileRecvSpeed = mobileLastRecv * 1000 / timeSpan
        val mobileSendSpeed = mobileLastSend * 1000 / timeSpan
        val wlanRecvSpeed = wlanLastRecv * 1000 / timeSpan
        val wlanSendSpeed = wlanLastSend * 1000 / timeSpan
        mobileRecvSum = tempMobileRx
        mobileSendSum = tempMobileTx
        wlanRecvSum = tempWlanRx
        wlanSendSum = tempWlanTx
        //==========================================================
        if (isMulti) {
            if (mobileRecvSpeed >= 0.0) {
                tvMobileRx!!.text = showSpeed(mobileRecvSpeed)
            }
            if (mobileSendSpeed >= 0.0) {
                tvMobileTx!!.text = showSpeed(mobileSendSpeed)
            }
            if (wlanRecvSpeed >= 0.0) {
                tvWlanRx!!.text = showSpeed(wlanRecvSpeed)
            }
            if (wlanSendSpeed >= 0.0) {
                tvWlanTx!!.text = showSpeed(wlanSendSpeed)
            }
        } else {
            //==============================================================
            if (totalSpeed >= 0.0) {
                tvSum!!.text = showSpeed(totalSpeed)
            }
        }
        //==============================================================
    }

    private fun showSpeed(speed: Double): String {
        val speedString: String
        speedString = if (speed >= 1048576.0) {
            showFloatFormat.format(speed / 1048576.0) + "MB/s"
        } else {
            showFloatFormat.format(speed / 1024.0) + "KB/s"
        }
        return speedString
    }

    private val mHandler = Handler()
    private val task: Runnable = object : Runnable {
        override fun run() {
            //设置延迟时间，此处是5秒
            mHandler.postDelayed(this, timeInterval)
            updateViewData()
            //需要执行的代码
        }
    }

    fun isMulti(): Boolean {
        return isMulti
    }

    fun setMulti(multi: Boolean) {
        isMulti = multi
        if (isMulti) {
            tvSum!!.visibility = View.GONE
            rlLayoutBig!!.visibility = View.VISIBLE
        } else {
            tvSum!!.visibility = View.VISIBLE
            rlLayoutBig!!.visibility = View.GONE
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeCallbacks(task)
    }
}