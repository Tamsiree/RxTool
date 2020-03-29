package com.tamsiree.rxui.view.loading

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.*
import android.view.animation.LinearInterpolator

internal class TLoadingManager(private val loaderView: TLoadingView) : ValueAnimator.AnimatorUpdateListener {
    private var rectPaint: Paint? = null
    private var linearGradient: LinearGradient? = null
    private var progress = 0f
    private var valueAnimator: ValueAnimator? = null
    private var widthWeight = TLoadingProfile.MAX_WEIGHT
    private var heightWeight = TLoadingProfile.MAX_WEIGHT
    private var useGradient = TLoadingProfile.USE_GRADIENT_DEFAULT
    private var corners = TLoadingProfile.CORNER_DEFAULT
    private fun init() {
        rectPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        loaderView.setRectColor(rectPaint!!)
        setValueAnimator(0.5f, 1f, ObjectAnimator.INFINITE)
    }

    @SuppressLint("DrawAllocation")
    @JvmOverloads
    fun onDraw(canvas: Canvas, left_pad: Float = 0f, top_pad: Float = 0f, right_pad: Float = 0f, bottom_pad: Float = 0f) {
        val margin_height = canvas.height * (1 - heightWeight) / 2
        rectPaint!!.alpha = (progress * MAX_COLOR_CONSTANT_VALUE).toInt()
        if (useGradient) {
            prepareGradient(canvas.width * widthWeight)
        }
        canvas.drawRoundRect(RectF(0 + left_pad,
                margin_height + top_pad,
                canvas.width * widthWeight - right_pad,
                canvas.height - margin_height - bottom_pad),
                corners.toFloat(), corners.toFloat(),
                rectPaint!!)
    }

    fun onSizeChanged() {
        linearGradient = null
        startLoading()
    }

    private fun prepareGradient(width: Float) {
        if (linearGradient == null) {
            linearGradient = LinearGradient(0f, 0f, width, 0f, rectPaint!!.color,
                    TLoadingProfile.COLOR_DEFAULT_GRADIENT, Shader.TileMode.MIRROR)
        }
        rectPaint!!.shader = linearGradient
    }

    fun startLoading() {
        if (valueAnimator != null && !loaderView.valueSet()) {
            valueAnimator!!.cancel()
            init()
            valueAnimator!!.start()
        }
    }

    fun setHeightWeight(heightWeight: Float) {
        this.heightWeight = validateWeight(heightWeight)
    }

    fun setWidthWeight(widthWeight: Float) {
        this.widthWeight = validateWeight(widthWeight)
    }

    fun setUseGradient(useGradient: Boolean) {
        this.useGradient = useGradient
    }

    fun setCorners(corners: Int) {
        this.corners = corners
    }

    private fun validateWeight(weight: Float): Float {
        if (weight > TLoadingProfile.MAX_WEIGHT) return TLoadingProfile.MAX_WEIGHT
        return if (weight < TLoadingProfile.MIN_WEIGHT) TLoadingProfile.MIN_WEIGHT else weight
    }

    fun stopLoading() {
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
            setValueAnimator(progress, 0f, 0)
            valueAnimator!!.start()
        }
    }

    private fun setValueAnimator(begin: Float, end: Float, repeatCount: Int) {
        valueAnimator = ValueAnimator.ofFloat(begin, end)
        valueAnimator?.repeatCount = repeatCount
        valueAnimator?.duration = ANIMATION_CYCLE_DURATION.toLong()
        valueAnimator?.repeatMode = ValueAnimator.REVERSE
        valueAnimator?.interpolator = LinearInterpolator()
        valueAnimator?.addUpdateListener(this)
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        progress = valueAnimator.animatedValue as Float
        loaderView.invalidate()
    }

    fun removeAnimatorUpdateListener() {
        if (valueAnimator != null) {
            valueAnimator!!.removeUpdateListener(this)
            valueAnimator!!.cancel()
        }
        progress = 0f
    }

    companion object {
        private const val MAX_COLOR_CONSTANT_VALUE = 255
        private const val ANIMATION_CYCLE_DURATION = 750 //milis
    }

    init {
        init()
    }
}