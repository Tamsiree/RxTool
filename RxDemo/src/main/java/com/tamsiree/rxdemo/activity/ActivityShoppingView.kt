package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxActivityTool
import com.tamsiree.rxkit.RxBarTool
import com.tamsiree.rxkit.RxDeviceTool
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_shopping_view.*

/**
 * @author tamsiree
 */
class ActivityShoppingView : ActivityBase() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBarTool.noTitle(this)
        setContentView(R.layout.activity_shopping_view)
        RxDeviceTool.setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        btn_take_out.setOnClickListener { RxActivityTool.skipActivity(mContext, ActivityELMe::class.java) }
    }

    override fun initData() {

    }
}