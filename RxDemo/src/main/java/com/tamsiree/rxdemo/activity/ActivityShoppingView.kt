package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxActivityTool
import com.tamsiree.rxkit.RxBarTool
import com.tamsiree.rxkit.RxDeviceTool
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxShoppingView
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityShoppingView : ActivityBase() {
    @JvmField
    @BindView(R.id.sv_1)
    var mSv1: RxShoppingView? = null

    @JvmField
    @BindView(R.id.sv_2)
    var mSv2: RxShoppingView? = null

    @JvmField
    @BindView(R.id.btn_take_out)
    var mBtnTakeOut: Button? = null

    @JvmField
    @BindView(R.id.activity_shopping_view)
    var mActivityShoppingView: LinearLayout? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBarTool.noTitle(this)
        setContentView(R.layout.activity_shopping_view)
        ButterKnife.bind(this)
        RxDeviceTool.setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
    }

    @OnClick(R.id.btn_take_out)
    fun onClick() {
        RxActivityTool.skipActivity(mContext!!, ActivityELMe::class.java)
    }
}