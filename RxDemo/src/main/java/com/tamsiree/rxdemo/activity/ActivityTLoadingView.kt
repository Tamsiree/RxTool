package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_tloading_view.*

class ActivityTLoadingView : ActivityBase() {

    private val WAIT_DURATION = 5000
    private var dummyWait: DummyWait? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tloading_view)
    }

    override fun initView() {
        rxTitle.setLeftFinish(this)
        btn_reset.setOnClickListener { resetLoader() }
    }

    override fun initData() {
        loadData()
    }

    private fun loadData() {
        if (dummyWait != null) {
            dummyWait!!.cancel(true)
        }
        dummyWait = DummyWait()
        dummyWait!!.execute()
    }

    private fun postLoadData() {
        txt_name.text = "神秘商人"
        txt_title.text = "只有有缘人能够相遇"
        txt_phone.text = "时限 24 小时"
        txt_email.text = "说着一口苦涩难懂的语言 ddjhklfsalkjhghjkl"
        image_icon.setImageResource(R.drawable.circle_dialog)
    }

    private fun resetLoader() {
        txt_name.resetLoader()
        txt_title.resetLoader()
        txt_phone.resetLoader()
        txt_email.resetLoader()
        image_icon.resetLoader()
        loadData()
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class DummyWait : AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            try {
                Thread.sleep(WAIT_DURATION.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            postLoadData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dummyWait != null) {
            dummyWait!!.cancel(true)
        }
    }
}