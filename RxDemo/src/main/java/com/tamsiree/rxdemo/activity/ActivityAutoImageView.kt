package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_auto_image_view.*

/**
 * @author tamsiree
 */
class ActivityAutoImageView : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setTransparentStatusBar(this)
        setContentView(R.layout.activity_auto_image_view)

    }

    override fun initView() {
        setPortrait(this)
        rx_title.setLeftFinish(mContext)
    }

    override fun initData() {

    }
}