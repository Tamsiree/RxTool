package com.tamsiree.rxdemo.activity

import android.os.Bundle
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.TUnReadView
import com.tamsiree.rxui.view.other.TCrossView

class ActivityOtherEffect : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.tunreadview)
    var mTunreadview: TUnReadView? = null

    @JvmField
    @BindView(R.id.tcross_view)
    var mTcrossView: TCrossView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_effect)
        ButterKnife.bind(this)
        initView()
    }

    private fun initView() {
        mRxTitle!!.setLeftFinish(this)
        initTUnReadView()
        initTCrossView()
    }

    private fun initTUnReadView() {
        mTunreadview!!.setText("14") //设置未读信息数
        //        mTunreadview.setNewText();     //设置有新的信息状态
    }

    private fun initTCrossView() {
        mTcrossView!!.setOnClickListener { mTcrossView!!.toggle() }
        mTcrossView!!.setColor(resources.getColor(R.color.green_xiaomi))
    }
}