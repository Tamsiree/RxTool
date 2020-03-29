package com.tamsiree.rxui.view.colorpicker.slider

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DimenRes
import com.tamsiree.rxui.R

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
abstract class AbsCustomSlider : View {
    protected var bitmap: Bitmap? = null
    protected var bitmapCanvas: Canvas? = null
    protected var bar: Bitmap? = null
    protected var barCanvas: Canvas? = null
    protected var onValueChangedListener: OnValueChangedListener? = null
    protected var barOffsetX = 0
    protected var handleRadius = 20
    protected var barHeight = 5
    protected var value = 1f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    protected fun updateBar() {
        handleRadius = getDimension(R.dimen.default_slider_handler_radius)
        barHeight = getDimension(R.dimen.default_slider_bar_height)
        barOffsetX = handleRadius
        if (bar == null) {
            createBitmaps()
        }
        drawBar(barCanvas!!)
        invalidate()
    }

    protected open fun createBitmaps() {
        val width = width
        val height = height
        var barTemp = Bitmap.createBitmap(width - barOffsetX * 2, barHeight, Bitmap.Config.ARGB_8888)
        bar = barTemp
        barCanvas = Canvas(barTemp)
        if (bitmap == null || bitmap!!.width != width || bitmap!!.height != height) {
            if (bitmap != null) {
                bitmap!!.recycle()
            }
            var bitmapTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap = bitmapTemp
            bitmapCanvas = Canvas(bitmapTemp)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bar != null && bitmapCanvas != null) {
            bitmapCanvas!!.drawColor(0, PorterDuff.Mode.CLEAR)
            bitmapCanvas!!.drawBitmap(bar!!, barOffsetX.toFloat(), (height - bar!!.height) / 2.toFloat(), null)
            val x = handleRadius + value * (width - handleRadius * 2)
            val y = height / 2f
            drawHandle(bitmapCanvas!!, x, y)
            canvas.drawBitmap(bitmap!!, 0f, 0f, null)
        }
    }

    protected abstract fun drawBar(barCanvas: Canvas)
    protected abstract fun onValueChanged(value: Float)
    protected abstract fun drawHandle(canvas: Canvas, x: Float, y: Float)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateBar()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = 0
        when (widthMode) {
            MeasureSpec.UNSPECIFIED -> width = widthMeasureSpec
            MeasureSpec.AT_MOST -> width = MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.EXACTLY -> width = MeasureSpec.getSize(widthMeasureSpec)
            else -> {
            }
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = 0
        when (heightMode) {
            MeasureSpec.UNSPECIFIED -> height = heightMeasureSpec
            MeasureSpec.AT_MOST -> height = MeasureSpec.getSize(heightMeasureSpec)
            MeasureSpec.EXACTLY -> height = MeasureSpec.getSize(heightMeasureSpec)
            else -> {
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if (bar != null) {
                    value = (event.x - barOffsetX) / bar!!.width
                    value = Math.max(0f, Math.min(value, 1f))
                    onValueChanged(value)
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                onValueChanged(value)
                if (onValueChangedListener != null) {
                    onValueChangedListener!!.onValueChanged(value)
                }
                invalidate()
            }
            else -> {
            }
        }
        return true
    }

    protected fun getDimension(@DimenRes id: Int): Int {
        return resources.getDimensionPixelSize(id)
    }


}