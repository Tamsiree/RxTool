package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_on_crash.*

class ActivityOnCrash : ActivityBase() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_crash)
    }

    @SuppressLint("StaticFieldLeak")
    override fun initView() {
        rx_title.setLeftFinish(mContext)
        button_crash_main_thread.setOnClickListener { throw RuntimeException("I'm a cool exception and I crashed the main thread!") }
        button_crash_bg_thread.setOnClickListener {
            object : AsyncTask<Void?, Void?, Void>() {
                override fun doInBackground(vararg voids: Void?): Void {
                    throw RuntimeException("I'm also cool, and I crashed the background thread!")
                }
            }.execute()
        }
        button_crash_with_delay.setOnClickListener {
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

    override fun initData() {

    }
}