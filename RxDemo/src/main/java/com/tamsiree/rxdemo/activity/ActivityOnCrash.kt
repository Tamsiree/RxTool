package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle

class ActivityOnCrash : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.button_crash_main_thread)
    var mButtonCrashMainThread: Button? = null

    @JvmField
    @BindView(R.id.button_crash_bg_thread)
    var mButtonCrashBgThread: Button? = null

    @JvmField
    @BindView(R.id.button_crash_with_delay)
    var mButtonCrashWithDelay: Button? = null

    @SuppressLint("StaticFieldLeak")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_crash)
        ButterKnife.bind(this)
        mRxTitle!!.setLeftFinish(mContext)
        mButtonCrashMainThread!!.setOnClickListener { throw RuntimeException("I'm a cool exception and I crashed the main thread!") }
        mButtonCrashBgThread!!.setOnClickListener {

            object : AsyncTask<Void?, Void?, Void>() {
                override fun doInBackground(vararg voids: Void?): Void {
                    throw RuntimeException("I'm also cool, and I crashed the background thread!")
                }
            }.execute()
        }
        mButtonCrashWithDelay!!.setOnClickListener {
            object : AsyncTask<Void?, Void?, Void>() {
                override fun doInBackground(vararg voids: Void?): Void {
                    try {
                        Thread.sleep(5000)
                    } catch (e: InterruptedException) {
                        //meh
                    }
                    throw RuntimeException("I am a not so cool exception, and I am delayed, so you can check if the app crashes when in background!")
                }
            }.execute()
        }
    }
}