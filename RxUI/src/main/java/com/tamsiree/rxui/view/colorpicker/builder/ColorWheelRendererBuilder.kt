package com.tamsiree.rxui.view.colorpicker.builder

import com.tamsiree.rxui.view.colorpicker.ColorPickerView.WHEEL_TYPE
import com.tamsiree.rxui.view.colorpicker.renderer.ColorWheelRenderer
import com.tamsiree.rxui.view.colorpicker.renderer.FlowerColorWheelRenderer
import com.tamsiree.rxui.view.colorpicker.renderer.SimpleColorWheelRenderer

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
object ColorWheelRendererBuilder {
    @JvmStatic
    fun getRenderer(wheelType: WHEEL_TYPE?): ColorWheelRenderer {
        when (wheelType) {
            WHEEL_TYPE.CIRCLE -> return SimpleColorWheelRenderer()
            WHEEL_TYPE.FLOWER -> return FlowerColorWheelRenderer()
            else -> {
            }
        }
        throw IllegalArgumentException("wrong WHEEL_TYPE")
    }
}