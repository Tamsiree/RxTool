package com.tamsiree.rxui.view.colorpicker.renderer

import android.graphics.Color
import com.tamsiree.rxui.view.colorpicker.ColorCircle
import com.tamsiree.rxui.view.colorpicker.builder.PaintBuilder

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class SimpleColorWheelRenderer : AbsColorWheelRenderer() {
    private val selectorFill = PaintBuilder.newPaint().build()
    private val hsv = FloatArray(3)
    override fun draw() {
        val setSize = colorCircleList.size
        var currentCount = 0
        val half = colorWheelRenderOption?.targetCanvas?.width!! / 2f
        val density = colorWheelRenderOption?.density
        val maxRadius = colorWheelRenderOption?.maxRadius
        for (i in 0 until density!!) {
            // 0~1
            val p = i.toFloat() / (density - 1)
            val radius = maxRadius!! * p
            val size = colorWheelRenderOption?.cSize
            val total = calcTotalCount(radius, size!!)
            for (j in 0 until total) {
                val angle = Math.PI * 2 * j / total + Math.PI / total * ((i + 1) % 2)
                val x = half + (radius * Math.cos(angle)).toFloat()
                val y = half + (radius * Math.sin(angle)).toFloat()
                hsv[0] = (angle * 180 / Math.PI).toFloat()
                hsv[1] = radius / maxRadius
                hsv[2] = colorWheelRenderOption?.lightness!!
                selectorFill.color = Color.HSVToColor(hsv)
                selectorFill.alpha = alphaValueAsInt
                colorWheelRenderOption?.targetCanvas?.drawCircle(x, y, size - colorWheelRenderOption?.strokeWidth!!, selectorFill)
                if (currentCount >= setSize) {
                    colorCircleList.add(ColorCircle(x, y, hsv))
                } else {
                    colorCircleList[currentCount][x, y] = hsv
                }
                currentCount++
            }
        }
    }
}