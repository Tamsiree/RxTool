package com.tamsiree.rxui.view.mark.canvas

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.tamsiree.rxui.view.mark.canvas.CanvasUtils.drawBackdropRectangle
import com.tamsiree.rxui.view.mark.model.AnimationData
import com.tamsiree.rxui.view.mark.model.CanvasAnimation
import com.tamsiree.rxui.view.mark.model.ParentMetrics


/**
 * Lightweight canvas drawable for fading a background based on input constraints
 */
internal class FadingBackgroundCanvasDrawable(
        private val fadeAnimationData: AnimationData,
        private val backdropPaint: Paint
) : CanvasDrawable {

    override var invalidateCallback: () -> Unit = {}

    private var fadeAnimation: CanvasAnimation? = null

    private var currentAlpha: Int = 255

    override fun onDraw(canvas: Canvas, parentMetrics: ParentMetrics, canvasY: Float, proportion: Float) {
        if (proportion >= fadeAnimationData.startProportion && fadeAnimation == null) {
            val animator = createFadeAnimator()
            fadeAnimation = CanvasAnimation(animator)
            animator.start()
        }

        canvas.drawBackdropRectangle(parentMetrics, canvasY, backdropPaint.apply {
            alpha = currentAlpha
        })
    }

    override fun reset() {
        fadeAnimation?.animator?.cancel()
        fadeAnimation = null
        currentAlpha = 255
    }

    private fun createFadeAnimator() = ValueAnimator().apply {
        setIntValues(255, 0)
        interpolator = LinearInterpolator()
        duration = fadeAnimationData.duration
        addUpdateListener {
            invalidateCallback()
            currentAlpha = it.animatedValue as Int
        }
        doOnEnd {
            fadeAnimation = fadeAnimation?.copy(hasEnded = true)
        }
    }
}