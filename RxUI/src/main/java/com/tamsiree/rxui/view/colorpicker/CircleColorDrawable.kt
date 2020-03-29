package com.tamsiree.rxui.view.colorpicker

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import com.tamsiree.rxui.view.colorpicker.builder.PaintBuilder

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class CircleColorDrawable : ColorDrawable {
    private var strokeWidth = 0f
    private val strokePaint = PaintBuilder.newPaint().style(Paint.Style.STROKE).stroke(strokeWidth).color(-0x1).build()
    private val fillPaint = PaintBuilder.newPaint().style(Paint.Style.FILL).color(0).build()
    private val fillBackPaint = PaintBuilder.newPaint().shader(PaintBuilder.createAlphaPatternShader(16)).build()

    constructor() : super()
    constructor(color: Int) : super(color)

    override fun draw(canvas: Canvas) {
        canvas.drawColor(0)
        val width = canvas.width
        val radius = width / 2f
        strokeWidth = radius / 12f
        strokePaint.strokeWidth = strokeWidth
        fillPaint.color = color
        canvas.drawCircle(radius, radius, radius - strokeWidth * 1.5f, fillBackPaint)
        canvas.drawCircle(radius, radius, radius - strokeWidth * 1.5f, fillPaint)
        canvas.drawCircle(radius, radius, radius - strokeWidth, strokePaint)
    }

    override fun setColor(color: Int) {
        super.setColor(color)
        invalidateSelf()
    }
}