package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.widget.RelativeLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityAutoImageView : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.activity_auto_image_view)
    var mActivityAutoImageView: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setTransparentStatusBar(this)
        setContentView(R.layout.activity_auto_image_view)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
    }
}