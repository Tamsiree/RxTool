package com.tamsiree.rxdemo.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.heart.RxHeartLayout
import com.tamsiree.rxui.view.likeview.RxShineButton
import java.util.*

/**
 * @author tamsiree
 */
class ActivityLike : ActivityBase() {
    @JvmField
    @BindView(R.id.po_image0)
    var mRxShineButton: RxShineButton? = null

    @JvmField
    @BindView(R.id.po_image1)
    var porterShapeImageView1: RxShineButton? = null

    @JvmField
    @BindView(R.id.po_image2)
    var porterShapeImageView2: RxShineButton? = null

    @JvmField
    @BindView(R.id.po_image3)
    var porterShapeImageView3: RxShineButton? = null

    @JvmField
    @BindView(R.id.ll_top)
    var mLlTop: LinearLayout? = null

    @JvmField
    @BindView(R.id.wrapper)
    var mWrapper: LinearLayout? = null

    @JvmField
    @BindView(R.id.po_image8)
    var mPoImage8: RxShineButton? = null

    @JvmField
    @BindView(R.id.love)
    var mLove: ImageView? = null

    @JvmField
    @BindView(R.id.ll_control)
    var mLlControl: LinearLayout? = null

    @JvmField
    @BindView(R.id.ll_bottom)
    var mLlBottom: LinearLayout? = null

    @JvmField
    @BindView(R.id.heart_layout)
    var mRxHeartLayout: RxHeartLayout? = null

    @JvmField
    @BindView(R.id.tv_hv)
    var mTvHv: TextView? = null

    @JvmField
    @BindView(R.id.activity_like)
    var mActivityLike: RelativeLayout? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
        mRxShineButton!!.init(this)
        porterShapeImageView1!!.init(this)
        porterShapeImageView2!!.init(this)
        porterShapeImageView3!!.init(this)
        val rxShineButtonJava = RxShineButton(this)
        rxShineButtonJava.setBtnColor(Color.GRAY)
        rxShineButtonJava.setBtnFillColor(Color.RED)
        rxShineButtonJava.setShapeResource(R.raw.heart)
        rxShineButtonJava.setAllowRandomColor(true)
        rxShineButtonJava.setShineSize(100)
        val layoutParams = LinearLayout.LayoutParams(100, 100)
        rxShineButtonJava.layoutParams = layoutParams
        if (mWrapper != null) {
            mWrapper!!.addView(rxShineButtonJava)
        }
        mRxShineButton!!.setOnClickListener { }
        mRxShineButton!!.setOnCheckStateChangeListener { view, checked -> }
        porterShapeImageView2!!.setOnClickListener { }
        porterShapeImageView3!!.setOnClickListener { }
    }

    private val random = Random()

    @OnClick(R.id.love)
    fun onClick() {
        mRxHeartLayout!!.post {
            val rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255))
            mRxHeartLayout!!.addHeart(rgb)
        }
    }
}