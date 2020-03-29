package com.tamsiree.rxui.view.colorpicker.slider

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.util.AttributeSet
import com.tamsiree.rxkit.RxImageTool.getAlphaPercent
import com.tamsiree.rxui.view.colorpicker.ColorPickerView
import com.tamsiree.rxui.view.colorpicker.builder.PaintBuilder

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class AlphaSlider : AbsCustomSlider {
    var colorS = 0
    private val alphaPatternPaint = PaintBuilder.newPaint().build()
    private val barPaint = PaintBuilder.newPaint().build()
    private val solid = PaintBuilder.newPaint().build()
    private val clearingStroke = PaintBuilder.newPaint().color(-0x1).xPerMode(PorterDuff.Mode.CLEAR).build()
    private var colorPicker: ColorPickerView? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun createBitmaps() {
        super.createBitmaps()
        alphaPatternPaint.shader = PaintBuilder.createAlphaPatternShader(barHeight / 2)
    }

    override fun drawBar(barCanvas: Canvas) {
        val width = barCanvas.width
        val height = barCanvas.height
        barCanvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), alphaPatternPaint)
        val l = Math.max(2, width / 256)
        var x = 0
        while (x <= width) {
            val alpha = x.toFloat() / (width - 1)
            barPaint.color = colorS
            barPaint.alpha = Math.round(alpha * 255)
            barCanvas.drawRect(x.toFloat(), 0f, x + l.toFloat(), height.toFloat(), barPaint)
            x += l
        }
    }

    override fun onValueChanged(value: Float) {
        if (colorPicker != null) {
            colorPicker!!.setAlphaValue(value)
        }
    }

    override fun drawHandle(canvas: Canvas, x: Float, y: Float) {
        solid.color = colorS
        solid.alpha = Math.round(value * 255)
        canvas.drawCircle(x, y, handleRadius.toFloat(), clearingStroke)
        if (value < 1) {
            canvas.drawCircle(x, y, handleRadius * 0.75f, alphaPatternPaint)
        }
        canvas.drawCircle(x, y, handleRadius * 0.75f, solid)
    }

    fun setColorPicker(colorPicker: ColorPickerView?) {
        this.colorPicker = colorPicker
    }

    fun setColor(color: Int) {
        this.colorS = color
        value = getAlphaPercent(color)
        if (bar != null) {
            updateBar()
            invalidate()
        }
    }
}