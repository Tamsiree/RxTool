package com.tamsiree.rxui.view.colorpicker.slider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import com.tamsiree.rxkit.RxImageTool.colorAtLightness
import com.tamsiree.rxkit.RxImageTool.lightnessOfColor
import com.tamsiree.rxui.view.colorpicker.ColorPickerView
import com.tamsiree.rxui.view.colorpicker.builder.PaintBuilder

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class LightnessSlider : AbsCustomSlider {
    private var color = 0
    private val barPaint = PaintBuilder.newPaint().build()
    private val solid = PaintBuilder.newPaint().build()
    private val clearingStroke = PaintBuilder.newPaint().color(-0x1).xPerMode(PorterDuff.Mode.CLEAR).build()
    private var colorPicker: ColorPickerView? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun drawBar(barCanvas: Canvas) {
        val width = barCanvas.width
        val height = barCanvas.height
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        val l = Math.max(2, width / 256)
        var x = 0
        while (x <= width) {
            hsv[2] = x.toFloat() / (width - 1)
            barPaint.color = Color.HSVToColor(hsv)
            barCanvas.drawRect(x.toFloat(), 0f, x + l.toFloat(), height.toFloat(), barPaint)
            x += l
        }
    }

    override fun onValueChanged(value: Float) {
        if (colorPicker != null) {
            colorPicker!!.setLightness(value)
        }
    }

    override fun drawHandle(canvas: Canvas, x: Float, y: Float) {
        solid.color = colorAtLightness(color, value)
        canvas.drawCircle(x, y, handleRadius.toFloat(), clearingStroke)
        canvas.drawCircle(x, y, handleRadius * 0.75f, solid)
    }

    fun setColorPicker(colorPicker: ColorPickerView?) {
        this.colorPicker = colorPicker
    }

    fun setColor(color: Int) {
        this.color = color
        value = lightnessOfColor(color)
        if (bar != null) {
            updateBar()
            invalidate()
        }
    }
}