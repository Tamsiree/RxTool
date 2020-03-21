package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.jaredrummler.android.widget.AnimatedSvgView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.model.ModelSVG
import com.tamsiree.rxkit.RxActivityTool
import com.tamsiree.rxkit.RxBarTool
import com.tamsiree.rxkit.RxDeviceTool
import com.tamsiree.rxkit.RxTool
import com.tamsiree.rxkit.interfaces.OnSimpleListener
import com.tamsiree.rxui.activity.ActivityBase

/**
 * @author tamsiree
 */
class ActivitySVG : ActivityBase() {
    @JvmField
    @BindView(R.id.animated_svg_view)
    var mSvgView: AnimatedSvgView? = null

    @JvmField
    @BindView(R.id.app_name)
    var mAppName: ImageView? = null

    @JvmField
    @BindView(R.id.tv_app_name)
    var mTvAppName: TextView? = null

    @JvmField
    @BindView(R.id.tv_version)
    var mTvVersion: TextView? = null

    @JvmField
    @BindView(R.id.activity_svg)
    var mActivitySvg: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBarTool.hideStatusBar(this)
        setContentView(R.layout.activity_svg)
        ButterKnife.bind(this)
        RxDeviceTool.setPortrait(this)
        setSvg(ModelSVG.values()[0])
        mTvVersion!!.text = String.format("VERSION %s", RxDeviceTool.getAppVersionName(mContext))
    }

    private fun setSvg(modelSvg: ModelSVG) {
        mSvgView!!.setGlyphStrings(*modelSvg.glyphs)
        mSvgView!!.setFillColors(modelSvg.colors)
        mSvgView!!.setViewportSize(modelSvg.width, modelSvg.height)
        mSvgView!!.setTraceResidueColor(0x32000000)
        mSvgView!!.setTraceColors(modelSvg.colors)
        mSvgView!!.rebuildGlyphData()
        mSvgView!!.setOnStateChangeListener { state: Int ->
            when (state) {
                AnimatedSvgView.STATE_FINISHED -> {
                    mTvAppName!!.visibility = View.VISIBLE
                    mTvVersion!!.visibility = View.VISIBLE
                    RxTool.delayToDo(2000, object : OnSimpleListener {
                        override fun doSomething() {
                            mContext?.let { RxActivityTool.skipActivityAndFinish(it, ActivityMain::class.java, true) }
                        }
                    })
                }
                else -> {
                }
            }
        }
        mSvgView!!.start()
    }
}