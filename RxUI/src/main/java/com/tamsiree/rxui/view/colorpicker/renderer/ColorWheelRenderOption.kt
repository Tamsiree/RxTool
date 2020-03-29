package com.tamsiree.rxui.view.colorpicker.renderer

import android.graphics.Canvas

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class ColorWheelRenderOption {
    var density = 0
    var maxRadius = 0f
    var cSize = 0f
    var strokeWidth = 0f

    @JvmField
    var alpha = 0f
    var lightness = 0f
    var targetCanvas: Canvas? = null
}