package com.tamsiree.rxui.view.progressing.sprite

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint

/**
 * @author tamsiree
 */
abstract class ShapeSprite : Sprite() {
    private val mPaint: Paint
    var useColor = 0
        private set
    private var mBaseColor = 0
    override var color: Int
        get() = mBaseColor
        set(color) {
            mBaseColor = color
            updateUseColor()
        }

    override fun setAlpha(alpha: Int) {
        super.setAlpha(alpha)
        updateUseColor()
    }

    private fun updateUseColor() {
        var alpha = alpha
        alpha += alpha shr 7
        val baseAlpha = mBaseColor ushr 24
        val useAlpha = baseAlpha * alpha shr 8
        useColor = mBaseColor shl 8 ushr 8 or (useAlpha shl 24)
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun drawSelf(canvas: Canvas?) {
        mPaint.color = useColor
        drawShape(canvas, mPaint)
    }

    abstract fun drawShape(canvas: Canvas?, paint: Paint?)

    init {
        color = Color.WHITE
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = useColor
    }
}