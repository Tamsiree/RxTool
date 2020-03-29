package com.tamsiree.rxui.view.likeview.tools

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import com.tamsiree.rxui.view.likeview.tools.RxPorterImageView

/**
 * @author tamsiree
 */
abstract class RxPorterImageView : AppCompatImageView {
    var paintColor = Color.GRAY
    private var maskCanvas: Canvas? = null
    private var maskBitmap: Bitmap? = null
    private var maskPaint: Paint? = null
    private var drawableCanvas: Canvas? = null
    private var drawableBitmap: Bitmap? = null
    private var drawablePaint: Paint? = null
    private var invalidated = true

    constructor(context: Context) : super(context) {
        setup(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        setup(context, attrs, defStyle)
    }

    private fun setup(context: Context, attrs: AttributeSet?, defStyle: Int) {
        if (scaleType == ScaleType.FIT_CENTER) {
            scaleType = ScaleType.CENTER_CROP
        }
        maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        maskPaint!!.color = Color.BLACK
    }

    fun setSrcColor(color: Int) {
        paintColor = color
        setImageDrawable(ColorDrawable(color))
        if (drawablePaint != null) {
            drawablePaint!!.color = color
            invalidate()
        }
    }

    override fun invalidate() {
        invalidated = true
        super.invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        createMaskCanvas(w, h, oldw, oldh)
    }

    private fun createMaskCanvas(width: Int, height: Int, oldw: Int, oldh: Int) {
        val sizeChanged = width != oldw || height != oldh
        val isValid = width > 0 && height > 0
        if (isValid && (maskCanvas == null || sizeChanged)) {
            maskCanvas = Canvas()
            maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            maskCanvas!!.setBitmap(maskBitmap)
            maskPaint!!.reset()
            paintMaskCanvas(maskCanvas, maskPaint, width, height)
            drawableCanvas = Canvas()
            drawableBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            drawableCanvas!!.setBitmap(drawableBitmap)
            drawablePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            drawablePaint!!.color = paintColor
            invalidated = true
        }
    }

    protected abstract fun paintMaskCanvas(maskCanvas: Canvas?, maskPaint: Paint?, width: Int, height: Int)
    override fun onDraw(canvas: Canvas) {
        if (!isInEditMode) {
            val saveCount = canvas.saveLayer(0.0f, 0.0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
            try {
                if (invalidated) {
                    val drawable = drawable
                    if (drawable != null) {
                        invalidated = false
                        val imageMatrix = imageMatrix
                        if (imageMatrix == null) { // && mPaddingTop == 0 && mPaddingLeft == 0) {
                            drawable.draw(drawableCanvas!!)
                        } else {
                            val drawableSaveCount = drawableCanvas!!.saveCount
                            drawableCanvas!!.save()
                            drawableCanvas!!.concat(imageMatrix)
                            drawable.draw(drawableCanvas!!)
                            drawableCanvas!!.restoreToCount(drawableSaveCount)
                        }
                        drawablePaint!!.reset()
                        drawablePaint!!.isFilterBitmap = false
                        drawablePaint!!.xfermode = PORTER_DUFF_XFERMODE
                        drawableCanvas!!.drawBitmap(maskBitmap!!, 0.0f, 0.0f, drawablePaint)
                    }
                }
                if (!invalidated) {
                    drawablePaint!!.xfermode = null
                    canvas.drawBitmap(drawableBitmap!!, 0.0f, 0.0f, drawablePaint)
                }
            } catch (e: Exception) {
                val log = "Exception occured while drawing $id"
                Log.e(TAG, log, e)
            } finally {
                canvas.restoreToCount(saveCount)
            }
        } else {
            super.onDraw(canvas)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        if (widthMeasureSpec == 0) {
            widthMeasureSpec = 50
        }
        if (heightMeasureSpec == 0) {
            heightMeasureSpec = 50
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    companion object {
        private val TAG = RxPorterImageView::class.java.simpleName
        private val PORTER_DUFF_XFERMODE = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }
}