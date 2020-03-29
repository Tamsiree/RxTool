package com.tamsiree.rxdemo.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxImageTool.changeColorAlpha
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxRotateBar
import com.tamsiree.rxui.view.RxRotateBarBasic
import kotlinx.android.synthetic.main.activity_rx_rotate_bar.*

/**
 * @author tamsiree
 */
class ActivityRxRotateBar : ActivityBase() {

    private var bar1: RxRotateBarBasic? = null
    private var bar2: RxRotateBarBasic? = null
    private var bar3: RxRotateBarBasic? = null
    private var bar4: RxRotateBarBasic? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_rotate_bar)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        bar1 = RxRotateBarBasic(5, "魅力")
        bar2 = RxRotateBarBasic(8, "财力")
        bar3 = RxRotateBarBasic(3, "精力")
        bar4 = RxRotateBarBasic(4, "体力")
        rating_view.animatorListener = object : RxRotateBar.AnimatorListener {
            override fun onRotateStart() {}
            override fun onRotateEnd() {
                btn_change.visibility = View.VISIBLE
            }

            override fun onRatingStart() {}
            override fun onRatingEnd() {}
        }
        rating_view.addRatingBar(bar1!!)
        rating_view.addRatingBar(bar2!!)
        rating_view.addRatingBar(bar3!!)
        rating_view.addRatingBar(bar4!!)
        rating_view.show()

        btn_change.setOnClickListener {
            btn_change.visibility = View.GONE
            ll_change.setBackgroundColor(Color.WHITE)
            rating_view.centerTextColor = R.color.navy
            rating_view.clear()
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
            rating_view.show()
        }
    }

    override fun initData() {

    }

}