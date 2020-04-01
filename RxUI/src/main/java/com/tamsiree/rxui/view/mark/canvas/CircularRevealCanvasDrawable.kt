package com.tamsiree.rxui.view.mark.canvas

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.tamsiree.rxui.view.mark.canvas.CanvasUtils.drawBackdropCircle
import com.tamsiree.rxui.view.mark.canvas.CanvasUtils.drawBackdropRectangle
import com.tamsiree.rxui.view.mark.model.AnimationData
import com.tamsiree.rxui.view.mark.model.Coordinate
import com.tamsiree.rxui.view.mark.model.ParentMetrics


/**
 * Lightweight canvas drawable for handling circular reveals
 */
internal class CircularRevealCanvasDrawable(
        private val interactionStartProportion: Float,
        private val revealData: AnimationData,
        private val centerXInset: Float,
        private val backdropPaint: Paint
) : CanvasDrawable {

    override var invalidateCallback: () -> Unit = {}

    private var hasAnimated: Boolean = false

    private var animator: Animator? = null
    private var animatedValue: Float? = null

    override fun onDraw(canvas: Canvas, parentMetrics: ParentMetrics, canvasY: Float, proportion: Float) {
        when {
            proportion >= revealData.startProportion -> {
                when {
                    animator == null -> {
                        animator = createRevealAnimator(proportion).also { it.start() }
                        drawCircle(canvas, proportion, parentMetrics, canvasY)
                    }
                    animator?.isStarted == true && !hasAnimated -> {
                        drawCircle(canvas, animatedValue!!, parentMetrics, canvasY)
                    }
                    hasAnimated -> {
                        drawRectangle(canvas, parentMetrics, canvasY)
                    }
                }
            }
            proportion >= interactionStartProportion && proportion < revealData.startProportion -> {
                if (hasAnimated || animator?.isStarted == true) {
                    drawRectangle(canvas, parentMetrics, canvasY)
                } else {
                    drawCircle(canvas, proportion, parentMetrics, canvasY)
                }
            }
            hasAnimated || (proportion < interactionStartProportion && animator != null) -> {
                drawRectangle(canvas, parentMetrics, canvasY)
            }
        }
    }

    override fun reset() {
        hasAnimated = false
        animator?.cancel()
        animator = null
        animatedValue = null
    }

    private fun drawCircle(canvas: Canvas, proportion: Float, parentMetrics: ParentMetrics, canvasY: Float) {
        val (parentWidth, parentHeight) = parentMetrics

        // Calculate radius
        val radius = parentWidth * (proportion - interactionStartProportion)

        val circleCenterCoordinate = Coordinate(
                x = parentWidth - centerXInset,
                y = parentHeight / 2F
        )

        // Draw
        canvas.drawBackdropCircle(
                parentMetrics = parentMetrics,
                canvasY = canvasY,
                paint = backdropPaint,
                centerCoordinate = circleCenterCoordinate,
                radius = radius)
    }

    private fun drawRectangle(canvas: Canvas, parentMetrics: ParentMetrics, canvasY: Float) {
        canvas.drawBackdropRectangle(parentMetrics, canvasY, backdropPaint)
    }

    private fun createRevealAnimator(currentProportion: Float): Animator = ValueAnimator().apply {
        setFloatValues(currentProportion, 1F)
        interpolator = LinearInterpolator()
        duration = revealData.duration
        addUpdateListener {
            invalidateCallback()
            this@CircularRevealCanvasDrawable.animatedValue = it.animatedValue as Float
        }
        doOnEnd {
            hasAnimated = true
        }
    }
}