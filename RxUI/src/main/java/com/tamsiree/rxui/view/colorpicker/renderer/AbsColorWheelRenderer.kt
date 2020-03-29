package com.tamsiree.rxui.view.colorpicker.renderer

import com.tamsiree.rxui.view.colorpicker.ColorCircle
import java.util.*

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
abstract class AbsColorWheelRenderer : ColorWheelRenderer {
    protected var colorWheelRenderOption: ColorWheelRenderOption? = null
    override var colorCircleList: MutableList<ColorCircle> = ArrayList()
    override fun initWith(colorWheelRenderOption: ColorWheelRenderOption) {
        this.colorWheelRenderOption = colorWheelRenderOption
        colorCircleList.clear()
    }

    override val renderOption: ColorWheelRenderOption
        get() {
            if (colorWheelRenderOption == null) {
                colorWheelRenderOption = ColorWheelRenderOption()
            }
            return colorWheelRenderOption!!
        }

    protected val alphaValueAsInt: Int
        protected get() = Math.round(colorWheelRenderOption!!.alpha * 255)

    protected fun calcTotalCount(radius: Float, size: Float): Int {
        return Math.max(1, ((1f - ColorWheelRenderer.GAP_PERCENTAGE) * Math.PI / Math.asin(size / radius.toDouble()) + 0.5f).toInt())
    }
}