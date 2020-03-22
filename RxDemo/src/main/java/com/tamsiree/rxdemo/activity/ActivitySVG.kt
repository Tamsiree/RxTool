package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.activity_svg.*

/**
 * @author tamsiree
 */
class ActivitySVG : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBarTool.hideStatusBar(this)
        setContentView(R.layout.activity_svg)
    }

    override fun initView() {
        ButterKnife.bind(this)
        RxDeviceTool.setPortrait(this)
    }

    override fun initData() {
        setSvg(ModelSVG.values()[0])
        tv_version.text = String.format("VERSION %s", RxDeviceTool.getAppVersionName(mContext))
    }

    private fun setSvg(modelSvg: ModelSVG) {
        animated_svg_view.setGlyphStrings(*modelSvg.glyphs)
        animated_svg_view.setFillColors(modelSvg.colors)
        animated_svg_view.setViewportSize(modelSvg.width, modelSvg.height)
        animated_svg_view.setTraceResidueColor(0x32000000)
        animated_svg_view.setTraceColors(modelSvg.colors)
        animated_svg_view.rebuildGlyphData()
        animated_svg_view.setOnStateChangeListener { state: Int ->
            when (state) {
                AnimatedSvgView.STATE_FINISHED -> {
                    tv_app_name.visibility = View.VISIBLE
                    tv_version.visibility = View.VISIBLE
                    RxTool.delayToDo(2000, object : OnSimpleListener {
                        override fun doSomething() {
                            mContext.let { RxActivityTool.skipActivityAndFinish(it, ActivityMain::class.java, true) }
                        }
                    })
                }
                else -> {
                }
            }
        }
        animated_svg_view.start()
    }
}