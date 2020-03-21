package com.tamsiree.rxdemo.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxImageTool.changeColorAlpha
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxRotateBar
import com.tamsiree.rxui.view.RxRotateBarBasic
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityRxRotateBar : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.ll_change)
    var mLlChange: LinearLayout? = null

    @JvmField
    @BindView(R.id.rating_view)
    var mRatingView: RxRotateBar? = null

    @JvmField
    @BindView(R.id.btn_change)
    var mBtnChange: Button? = null
    var bar1: RxRotateBarBasic? = null
    var bar2: RxRotateBarBasic? = null
    var bar3: RxRotateBarBasic? = null
    var bar4: RxRotateBarBasic? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_rotate_bar)
        ButterKnife.bind(this)
        setPortrait(this)
        initView()
    }

    private fun initView() {
        mRxTitle!!.setLeftFinish(mContext)
        bar1 = RxRotateBarBasic(5, "魅力")
        bar2 = RxRotateBarBasic(8, "财力")
        bar3 = RxRotateBarBasic(3, "精力")
        bar4 = RxRotateBarBasic(4, "体力")
        mRatingView!!.animatorListener = object : RxRotateBar.AnimatorListener {
            override fun onRotateStart() {}
            override fun onRotateEnd() {
                mBtnChange!!.visibility = View.VISIBLE
            }

            override fun onRatingStart() {}
            override fun onRatingEnd() {}
        }
        mRatingView!!.addRatingBar(bar1)
        mRatingView!!.addRatingBar(bar2)
        mRatingView!!.addRatingBar(bar3)
        mRatingView!!.addRatingBar(bar4)
        mRatingView!!.show()
    }

    @OnClick(R.id.btn_change)
    fun onViewClicked() {
        mBtnChange!!.visibility = View.GONE
        mLlChange!!.setBackgroundColor(Color.WHITE)
        mRatingView!!.centerTextColor = R.color.navy
        mRatingView!!.clear()
        bar1!!.setRatedColor(resources.getColor(R.color.google_red))
        bar1!!.setOutlineColor(resources.getColor(R.color.google_red))
        bar1!!.setRatingBarColor(changeColorAlpha(resources.getColor(R.color.google_red), 130))
        bar1!!.rate = 9
        bar2!!.setRatedColor(resources.getColor(R.color.google_yellow))
        bar2!!.setOutlineColor(resources.getColor(R.color.google_yellow))
        bar2!!.setRatingBarColor(changeColorAlpha(resources.getColor(R.color.google_yellow), 130))
        bar2!!.rate = 9
        bar3!!.setRatedColor(resources.getColor(R.color.darkslateblue))
        bar3!!.setOutlineColor(resources.getColor(R.color.darkslateblue))
        bar3!!.setRatingBarColor(changeColorAlpha(resources.getColor(R.color.darkslateblue), 130))
        bar3!!.rate = 9
        bar4!!.setRatedColor(resources.getColor(R.color.google_green))
        bar4!!.setOutlineColor(resources.getColor(R.color.google_green))
        bar4!!.setRatingBarColor(changeColorAlpha(resources.getColor(R.color.google_green), 130))
        bar4!!.rate = 9
        mRatingView!!.show()
    }
}