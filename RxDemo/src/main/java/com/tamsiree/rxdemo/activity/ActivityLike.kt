package com.tamsiree.rxdemo.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.likeview.RxShineButton
import kotlinx.android.synthetic.main.activity_like.*
import java.util.*

/**
 * @author tamsiree
 */
class ActivityLike : ActivityBase() {

    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        po_image0.init(this)
        po_image1.init(this)
        po_image2.init(this)
        po_image3.init(this)
        val rxShineButtonJava = RxShineButton(this)
        rxShineButtonJava.setBtnColor(Color.GRAY)
        rxShineButtonJava.setBtnFillColor(Color.RED)
        rxShineButtonJava.setShapeResource(R.raw.heart)
        rxShineButtonJava.setAllowRandomColor(true)
        rxShineButtonJava.setShineSize(100)
        val layoutParams = LinearLayout.LayoutParams(100, 100)
        rxShineButtonJava.layoutParams = layoutParams
        if (wrapper != null) {
            wrapper.addView(rxShineButtonJava)
        }
        po_image0.setOnClickListener { }
        po_image0.setOnCheckStateChangeListener(object : RxShineButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: View?, checked: Boolean) {

            }
        })

        po_image2.setOnClickListener { }
        po_image3.setOnClickListener { }

        love.setOnClickListener {
            heart_layout.post {
                val rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255))
                heart_layout.addHeart(rgb)
            }
        }
    }

    override fun initData() {

    }


}