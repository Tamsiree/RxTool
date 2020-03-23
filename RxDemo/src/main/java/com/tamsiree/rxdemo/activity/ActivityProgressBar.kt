package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxDataTool.Companion.stringToInt
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_progress_bar.*

/**
 * @author tamsiree
 */
@SuppressLint("HandlerLeak")
class ActivityProgressBar : ActivityBase() {

    private var money = 1000.0
    private var downLoadThread: Thread? = null
    private var downLoadThread1: Thread? = null
    private var downLoadThread2: Thread? = null
    private var downLoadRxRoundPdThread: Thread? = null


    private var progress = 0.0
    private var progress1 = 0
    private val money1 = 10000
    private var mRxRoundProgress = 0

    private val mRxRoundPdMax = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setContentView(R.layout.activity_progress_bar)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)

        flikerbar.setOnClickListener { initFlikerProgressBar() }
        round_flikerbar.setOnClickListener { initFlikerProgressBar() }
        initRoundProgress()
        initLineProgress()
        initRxRoundPd()
        button_reload.setOnClickListener { reLoad() }
    }

    override fun initData() {
        downLoad()
    }

    private fun getMax(currentProgress: Double): Int {
        return if (currentProgress < 100 && currentProgress > 0) {
            100
        } else if (currentProgress >= 100 && currentProgress < 1000) {
            1000
        } else if (currentProgress >= 1000 && currentProgress < 5000) {
            5000
        } else if (currentProgress >= 5000 && currentProgress < 20000) {
            20000
        } else if (currentProgress >= 20000 && currentProgress < 100000) {
            100000
        } else if (currentProgress >= 100000) {
            stringToInt((currentProgress * 1.1f).toString() + "")
        } else {
            stringToInt(currentProgress.toString() + "")
        }
    }

    private fun initRoundProgress() {
        progress = 0.0 // 进度初始化
        roundProgressBar1.progress = progress
        roundProgressBar1.max = getMax(money).toDouble()
        downLoadThread2 = Thread(Runnable {
            try {
                while (!downLoadThread2!!.isInterrupted) {
                    while (progress < roundProgressBar1.max) {
                        progress += roundProgressBar1.max * 0.01f
                        if (progress < roundProgressBar1.max) {
                            roundProgressBar1.progress = progress
                        }
                        Thread.sleep(8)
                    }
                    while (progress > 0) {
                        progress -= roundProgressBar1.max * 0.01f
                        if (progress > 0) {
                            roundProgressBar1.progress = progress
                        }
                        Thread.sleep(8)
                    }
                    if (money != 0.0) {
                        while (progress < money) {
                            progress += money * 0.01f
                            roundProgressBar1.progress = progress
                            Thread.sleep(10)
                        }
                    }
                    roundProgressBar1.progress = money
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })
        downLoadThread2!!.start()
    }

    private fun initLineProgress() {
        progress1 = 0 // 进度初始化
        pb_line_of_credit.progress = progress1
        pb_line_of_credit.max = getMax(money1.toDouble())
        downLoadThread1 = Thread(Runnable {
            try {
                while (!downLoadThread1!!.isInterrupted) {
                    while (progress1 < pb_line_of_credit.max) {
                        progress1 += (pb_line_of_credit.max * 0.01f).toInt()
                        if (progress1 < pb_line_of_credit.max) {
                            pb_line_of_credit.progress = progress1
                            //tv_current.setText(progress1+"");
                        }
                        Thread.sleep(8)
                    }
                    while (progress1 > 0) {
                        progress1 -= (pb_line_of_credit.max * 0.01f).toInt()
                        if (progress1 > 0) {
                            pb_line_of_credit.progress = progress1
                            //tv_current.setText(progress1+"");
                        }
                        Thread.sleep(8)
                    }
                    while (progress1 < money1) {
                        progress1 += (money1 * 0.01f).toInt()
                        pb_line_of_credit.progress = progress1
                        //tv_current.setText(progress1+"");
                        Thread.sleep(10)
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })
        downLoadThread1!!.start()
    }

    private fun initRxRoundPd() {
        mRxRoundProgress = 0 // 进度初始化
        rx_round_pd1.max = mRxRoundPdMax.toFloat()
        rx_round_pd2.max = mRxRoundPdMax.toFloat()
        rx_round_pd3.max = mRxRoundPdMax.toFloat()
        rx_round_pd4.max = mRxRoundPdMax.toFloat()
        rx_round_pd5.max = mRxRoundPdMax.toFloat()
        rx_round_pd6.max = mRxRoundPdMax.toFloat()
        rx_round_pd7.max = mRxRoundPdMax.toFloat()
        rx_round_pd8.max = mRxRoundPdMax.toFloat()
        rx_round_pd9.max = mRxRoundPdMax.toFloat()
        rx_round_pd10.max = mRxRoundPdMax.toFloat()
        rx_round_pd11.max = mRxRoundPdMax.toFloat()
        rx_round_pd12.max = mRxRoundPdMax.toFloat()
        rx_round_pd13.max = mRxRoundPdMax.toFloat()
        rx_round_pd14.max = mRxRoundPdMax.toFloat()
        rx_round_pd15.max = mRxRoundPdMax.toFloat()
        rx_round_pd16.max = mRxRoundPdMax.toFloat()
        rx_round_pd17.max = mRxRoundPdMax.toFloat()
        progress_one.max = mRxRoundPdMax.toFloat()
        progress_two.max = mRxRoundPdMax.toFloat()
        progress_three.max = mRxRoundPdMax.toFloat()
        downLoadRxRoundPdThread = Thread(Runnable {
            try {
                while (!downLoadRxRoundPdThread?.isInterrupted!!) {
                    while (mRxRoundProgress < rx_round_pd1.max) {
                        mRxRoundProgress += (rx_round_pd1.max * 0.01f).toInt()
                        if (mRxRoundProgress < rx_round_pd1.max) {
                            val message = Message()
                            message.what = 101
                            mRxRoundPdHandler.sendMessage(message)
                        }
                        Thread.sleep(8)
                    }
                    while (mRxRoundProgress > 0) {
                        mRxRoundProgress -= (rx_round_pd1.max * 0.01f).toInt()
                        if (mRxRoundProgress > 0) {
                            val message = Message()
                            message.what = 101
                            mRxRoundPdHandler.sendMessage(message)
                        }
                        Thread.sleep(8)
                    }
                    while (mRxRoundProgress < mRxRoundPdMax) {
                        mRxRoundProgress += (mRxRoundPdMax * 0.01f).toInt()
                        val message = Message()
                        message.what = 101
                        mRxRoundPdHandler.sendMessage(message)
                        Thread.sleep(10)
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })
        downLoadRxRoundPdThread?.start()
    }

    //----------------------------------------------------------------------------------------------Flikerbar 加载事件处理 start
    private fun initFlikerProgressBar() {
        if (!flikerbar.isFinish) {
            flikerbar.toggle()
            round_flikerbar.toggle()
            if (flikerbar.isStop) {
                downLoadThread!!.interrupt()
            } else {
                downLoad()
            }
        }
    }

    fun reLoad() {
        downLoadThread!!.interrupt()
        // 重新加载
        flikerbar.reset()
        round_flikerbar.reset()
        downLoad()
    }

    //==============================================================================================Flikerbar 加载事件处理 end

    private var mRxRoundPdHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            rx_round_pd1.progress = mRxRoundProgress.toFloat()
            rx_round_pd2.progress = mRxRoundProgress.toFloat()
            rx_round_pd3.progress = mRxRoundProgress.toFloat()
            rx_round_pd4.progress = mRxRoundProgress.toFloat()
            rx_round_pd5.progress = mRxRoundProgress.toFloat()
            rx_round_pd6.progress = mRxRoundProgress.toFloat()
            rx_round_pd7.progress = mRxRoundProgress.toFloat()
            rx_round_pd8.progress = mRxRoundProgress.toFloat()
            rx_round_pd9.progress = mRxRoundProgress.toFloat()
            rx_round_pd10.progress = mRxRoundProgress.toFloat()
            rx_round_pd11.progress = mRxRoundProgress.toFloat()
            rx_round_pd12.progress = mRxRoundProgress.toFloat()
            rx_round_pd13.progress = mRxRoundProgress.toFloat()
            rx_round_pd14.progress = mRxRoundProgress.toFloat()
            rx_round_pd15.progress = mRxRoundProgress.toFloat()
            rx_round_pd16.progress = mRxRoundProgress.toFloat()
            rx_round_pd17.progress = mRxRoundProgress.toFloat()
            progress_one.progress = mRxRoundProgress.toFloat()
            progress_two.progress = mRxRoundProgress.toFloat()
            progress_three.progress = mRxRoundProgress.toFloat()
        }
    }

    private var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            flikerbar.progress = msg.arg1.toFloat()
            round_flikerbar.progress = msg.arg1.toFloat()
            if (msg.arg1 == 100) {
                flikerbar.finishLoad()
                round_flikerbar.finishLoad()
            }
        }
    }

    private fun downLoad() {
        downLoadThread = Thread(Runnable {
            try {
                while (!downLoadThread!!.isInterrupted) {
                    var progress = flikerbar.progress
                    progress += 2f
                    Thread.sleep(200)
                    val message = handler.obtainMessage()
                    message.arg1 = progress.toInt()
                    handler.sendMessage(message)
                    if (progress == 100f) {
                        break
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })
        downLoadThread!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        downLoadThread!!.interrupt()
        downLoadThread1!!.interrupt()
        downLoadThread2!!.interrupt()
        downLoadRxRoundPdThread!!.interrupt()
    }
}