package com.tamsiree.rxui.view.colorpicker.renderer

import android.graphics.Color
import com.tamsiree.rxui.view.colorpicker.ColorCircle
import com.tamsiree.rxui.view.colorpicker.builder.PaintBuilder
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class FlowerColorWheelRenderer : AbsColorWheelRenderer() {
    private val selectorFill = PaintBuilder.newPaint().build()
    private val hsv = FloatArray(3)
    private val sizeJitter = 1.2f
    override fun draw() {
        val setSize = colorCircleList.size
        var currentCount = 0
        val half = colorWheelRenderOption?.targetCanvas?.width!! / 2f
        val density = colorWheelRenderOption?.density
        val strokeWidth = colorWheelRenderOption?.strokeWidth
        val maxRadius = colorWheelRenderOption?.maxRadius
        val cSize = colorWheelRenderOption?.cSize
        for (i in 0 until density!!) {
            // 0~1
            val p = i.toFloat() / (density - 1)
            // -0.5 ~ 0.5
            val jitter = (i - density / 2f) / density
            val radius = maxRadius!! * p
            val size = max(1.5f + strokeWidth!!, cSize!! + (if (i == 0) 0f else cSize * sizeJitter * jitter))
            val total = min(calcTotalCount(radius, size), density * 2)
            for (j in 0 until total) {
                val angle = Math.PI * 2 * j / total + Math.PI / total * ((i + 1) % 2)
                val x = half + (radius * cos(angle)).toFloat()
                val y = half + (radius * sin(angle)).toFloat()
                hsv[0] = (angle * 180 / Math.PI).toFloat()
                hsv[1] = radius / maxRadius
                hsv[2] = colorWheelRenderOption?.lightness!!
                selectorFill.color = Color.HSVToColor(hsv)
                selectorFill.alpha = alphaValueAsInt
                colorWheelRenderOption?.targetCanvas?.drawCircle(x, y, size - strokeWidth, selectorFill)
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