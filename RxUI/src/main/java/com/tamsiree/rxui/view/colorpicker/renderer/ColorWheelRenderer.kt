package com.tamsiree.rxui.view.colorpicker.renderer

import com.tamsiree.rxui.view.colorpicker.ColorCircle

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
interface ColorWheelRenderer {
    fun draw()
    val renderOption: ColorWheelRenderOption
    fun initWith(colorWheelRenderOption: ColorWheelRenderOption)
    val colorCircleList: List<ColorCircle>?

    companion object {
        const val GAP_PERCENTAGE = 0.025f
    }
}