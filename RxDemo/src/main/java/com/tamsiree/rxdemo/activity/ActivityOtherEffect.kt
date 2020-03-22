package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_other_effect.*

class ActivityOtherEffect : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_effect)
    }

    override fun initView() {
        rx_title.setLeftFinish(this)
        initTUnReadView()
        initTCrossView()
    }

    override fun initData() {

    }

    private fun initTUnReadView() {
        tunreadview.setText("14") //设置未读信息数
        //        mTunreadview.setNewText();     //设置有新的信息状态
    }

    private fun initTCrossView() {
        tcross_view.setOnClickListener { tcross_view.toggle() }
        tcross_view.setColor(resources.getColor(R.color.green_xiaomi))
    }
}