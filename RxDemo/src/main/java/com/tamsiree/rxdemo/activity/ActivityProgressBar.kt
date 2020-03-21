package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxDataTool.Companion.stringToInt
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxArcProgress
import com.tamsiree.rxui.view.RxProgressBar
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.roundprogressbar.RxIconRoundProgress
import com.tamsiree.rxui.view.roundprogressbar.RxRoundProgress
import com.tamsiree.rxui.view.roundprogressbar.RxTextRoundProgress

/**
 * @author tamsiree
 */
@SuppressLint("HandlerLeak")
class ActivityProgressBar : ActivityBase() {
    var money = 1000.0
    var downLoadThread: Thread? = null
    var downLoadThread1: Thread? = null
    var downLoadThread2: Thread? = null
    var downLoadRxRoundPdThread: Thread? = null
    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            mFlikerbar!!.progress = msg.arg1.toFloat()
            mRoundFlikerbar!!.progress = msg.arg1.toFloat()
            if (msg.arg1 == 100) {
                mFlikerbar!!.finishLoad()
                mRoundFlikerbar!!.finishLoad()
            }
        }
    }

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.flikerbar)
    var mFlikerbar: RxProgressBar? = null

    @JvmField
    @BindView(R.id.round_flikerbar)
    var mRoundFlikerbar: RxProgressBar? = null

    @JvmField
    @BindView(R.id.rx_round_pd1)
    var mRxRoundPd1: RxRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd2)
    var mRxRoundPd2: RxRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd3)
    var mRxRoundPd3: RxRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd4)
    var mRxRoundPd4: RxRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd5)
    var mRxRoundPd5: RxRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd6)
    var mRxRoundPd6: RxRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd7)
    var mRxRoundPd7: RxRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd8)
    var mRxRoundPd8: RxIconRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd9)
    var mRxRoundPd9: RxIconRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd10)
    var mRxRoundPd10: RxIconRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd11)
    var mRxRoundPd11: RxIconRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd12)
    var mRxRoundPd12: RxIconRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd13)
    var mRxRoundPd13: RxIconRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd14)
    var mRxRoundPd14: RxIconRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd15)
    var mRxRoundPd15: RxTextRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd16)
    var mRxRoundPd16: RxTextRoundProgress? = null

    @JvmField
    @BindView(R.id.rx_round_pd17)
    var mRxRoundPd17: RxTextRoundProgress? = null

    @JvmField
    @BindView(R.id.progress_one)
    var mProgressOne: RxIconRoundProgress? = null

    @JvmField
    @BindView(R.id.progress_two)
    var mProgressTwo: RxRoundProgress? = null

    @JvmField
    @BindView(R.id.progress_three)
    var mProgressThree: RxTextRoundProgress? = null

    @JvmField
    @BindView(R.id.textView)
    var mTextView: TextView? = null

    @JvmField
    @BindView(R.id.pb_line_of_credit)
    var mPbLineOfCredit: ProgressBar? = null

    @JvmField
    @BindView(R.id.textView5)
    var mTextView5: TextView? = null

    @JvmField
    @BindView(R.id.roundProgressBar1)
    var mRoundProgressBar1: RxArcProgress? = null

    @JvmField
    @BindView(R.id.activity_round_progress_bar)
    var mActivityRoundProgressBar: LinearLayout? = null
    private var progress = 0.0
    private var progress1 = 0
    private val money1 = 10000
    private var mRxRoundProgress = 0
    var mRxRoundPdHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            mRxRoundPd1!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd2!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd3!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd4!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd5!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd6!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd7!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd8!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd9!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd10!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd11!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd12!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd13!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd14!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd15!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd16!!.progress = mRxRoundProgress.toFloat()
            mRxRoundPd17!!.progress = mRxRoundProgress.toFloat()
            mProgressOne!!.progress = mRxRoundProgress.toFloat()
            mProgressTwo!!.progress = mRxRoundProgress.toFloat()
            mProgressThree!!.progress = mRxRoundProgress.toFloat()
        }
    }
    private val mRxRoundPdMax = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setContentView(R.layout.activity_progress_bar)
        ButterKnife.bind(this)
        setPortrait(this)
        initView()
        initRoundProgress()
        initLineProgress()
        initRxRoundPd()
        downLoad()
    }

    private fun initView() {
        mRxTitle!!.setLeftFinish(mContext)
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
        mRoundProgressBar1!!.progress = progress
        mRoundProgressBar1!!.max = getMax(money).toDouble()
        downLoadThread2 = Thread(Runnable {
            try {
                while (!downLoadThread2!!.isInterrupted) {
                    while (progress < mRoundProgressBar1!!.max) {
                        progress += mRoundProgressBar1!!.max * 0.01f
                        if (progress < mRoundProgressBar1!!.max) {
                            mRoundProgressBar1!!.progress = progress
                        }
                        Thread.sleep(8)
                    }
                    while (progress > 0) {
                        progress -= mRoundProgressBar1!!.max * 0.01f
                        if (progress > 0) {
                            mRoundProgressBar1!!.progress = progress
                        }
                        Thread.sleep(8)
                    }
                    if (money != 0.0) {
                        while (progress < money) {
                            progress += money * 0.01f
                            mRoundProgressBar1!!.progress = progress
                            Thread.sleep(10)
                        }
                    }
                    mRoundProgressBar1!!.progress = money
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })
        downLoadThread2!!.start()
    }

    private fun initLineProgress() {
        progress1 = 0 // 进度初始化
        mPbLineOfCredit!!.progress = progress1
        mPbLineOfCredit!!.max = getMax(money1.toDouble())
        downLoadThread1 = Thread(Runnable {
            try {
                while (!downLoadThread1!!.isInterrupted) {
                    while (progress1 < mPbLineOfCredit!!.max) {
                        progress1 += (mPbLineOfCredit!!.max * 0.01f).toInt()
                        if (progress1 < mPbLineOfCredit!!.max) {
                            mPbLineOfCredit!!.progress = progress1
                            //tv_current.setText(progress1+"");
                        }
                        Thread.sleep(8)
                    }
                    while (progress1 > 0) {
                        progress1 -= (mPbLineOfCredit!!.max * 0.01f).toInt()
                        if (progress1 > 0) {
                            mPbLineOfCredit!!.progress = progress1
                            //tv_current.setText(progress1+"");
                        }
                        Thread.sleep(8)
                    }
                    while (progress1 < money1) {
                        progress1 += (money1 * 0.01f).toInt()
                        mPbLineOfCredit!!.progress = progress1
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
        mRxRoundPd1!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd2!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd3!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd4!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd5!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd6!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd7!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd8!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd9!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd10!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd11!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd12!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd13!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd14!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd15!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd16!!.max = mRxRoundPdMax.toFloat()
        mRxRoundPd17!!.max = mRxRoundPdMax.toFloat()
        mProgressOne!!.max = mRxRoundPdMax.toFloat()
        mProgressTwo!!.max = mRxRoundPdMax.toFloat()
        mProgressThree!!.max = mRxRoundPdMax.toFloat()
        downLoadRxRoundPdThread = Thread(Runnable {
            try {
                while (!downLoadRxRoundPdThread?.isInterrupted!!) {
                    while (mRxRoundProgress < mRxRoundPd1!!.max) {
                        mRxRoundProgress += (mRxRoundPd1?.max!! * 0.01f).toInt()
                        if (mRxRoundProgress < mRxRoundPd1!!.max) {
                            val message = Message()
                            message.what = 101
                            mRxRoundPdHandler.sendMessage(message)
                        }
                        Thread.sleep(8)
                    }
                    while (mRxRoundProgress > 0) {
                        mRxRoundProgress -= (mRxRoundPd1?.max!! * 0.01f).toInt()
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
        if (!mFlikerbar!!.isFinish) {
            mFlikerbar!!.toggle()
            mRoundFlikerbar!!.toggle()
            if (mFlikerbar!!.isStop) {
                downLoadThread!!.interrupt()
            } else {
                downLoad()
            }
        }
    }

    fun reLoad() {
        downLoadThread!!.interrupt()
        // 重新加载
        mFlikerbar!!.reset()
        mRoundFlikerbar!!.reset()
        downLoad()
    }

    //==============================================================================================Flikerbar 加载事件处理 end
    private fun downLoad() {
        downLoadThread = Thread(Runnable {
            try {
                while (!downLoadThread!!.isInterrupted) {
                    var progress = mFlikerbar!!.progress
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

    @OnClick(R.id.flikerbar, R.id.round_flikerbar)
    fun onClick(view: View) {
        when (view.id) {
            R.id.flikerbar -> initFlikerProgressBar()
            R.id.round_flikerbar -> initFlikerProgressBar()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        downLoadThread!!.interrupt()
        downLoadThread1!!.interrupt()
        downLoadThread2!!.interrupt()
        downLoadRxRoundPdThread!!.interrupt()
    }
}