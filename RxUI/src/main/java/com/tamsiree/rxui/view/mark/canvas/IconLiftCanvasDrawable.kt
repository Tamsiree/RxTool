package com.tamsiree.rxui.view.mark.canvas

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.animation.OvershootInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.Px
import androidx.core.animation.doOnEnd
import com.tamsiree.rxui.view.mark.canvas.CanvasKit.drawDrawable
import com.tamsiree.rxui.view.mark.model.AnimationData
import com.tamsiree.rxui.view.mark.model.CanvasAnimation
import com.tamsiree.rxui.view.mark.model.Coordinate
import com.tamsiree.rxui.view.mark.model.ParentMetrics
import kotlin.math.max

/**
 * Lightweight canvas drawable for handling interactive Icon lift and overshoot animations
 */
internal class IconLiftCanvasDrawable(
        @FloatRange(from = 0.0, to = 1.0)
        private val liftStartProportion: Float,
        private val bounceAnimationData: AnimationData,
        @FloatRange(from = 1.0)
        private val maxIconScaleProportion: Float,
        private val iconInsetPx: Int,
        @Px
        private val iconSizePx: Int,
        @ColorInt
        private val iconColor: Int,
        private val icon: Drawable,
        private val clipStartProportion: Float?
) : CanvasDrawable {

    override var invalidateCallback: () -> Unit = {}

    private var bounceAnimation: CanvasAnimation? = null

    private var currentLiftProportion: Float = 1.0F
    private var circularClipRadius: Float? = null

    override fun onDraw(canvas: Canvas, parentMetrics: ParentMetrics, canvasY: Float, proportion: Float) {
        // Adjust lift
        when {
            proportion >= liftStartProportion && proportion < bounceAnimationData.startProportion && bounceAnimation == null -> {
                val liftRange = bounceAnimationData.startProportion - liftStartProportion
                val currentRangeProportion = (liftRange - (bounceAnimationData.startProportion - proportion)) / liftRange

                currentLiftProportion = 1.0F + ((maxIconScaleProportion - 1.0F) * currentRangeProportion)
            }
            proportion >= bounceAnimationData.startProportion && bounceAnimation == null -> {
                val bounceAnimator = createIconBounceAnimator()
                bounceAnimation = CanvasAnimation(
                        animator = bounceAnimator
                )

                bounceAnimator.start()
            }
            bounceAnimation?.hasEnded == false -> {
            } // Skip here such that we don't reset the lift proportion
            else -> {
                currentLiftProportion = 1.0F
            }
        }

        if (clipStartProportion != null) {
            circularClipRadius = if (bounceAnimation == null) max(0f, (proportion - clipStartProportion) * parentMetrics.width) else parentMetrics.width
        }

        drawIcon(canvas, parentMetrics, canvasY)
    }

    override fun reset() {
        bounceAnimation?.animator?.cancel()
        bounceAnimation = null
        currentLiftProportion = 1.0F
        circularClipRadius = null
    }

    private fun drawIcon(canvas: Canvas, parentMetrics: ParentMetrics, canvasY: Float) {
        canvas.drawDrawable(
                canvasY = canvasY,
                centerCoordinate = Coordinate(
                        x = parentMetrics.width - iconInsetPx.toFloat(),
                        y = parentMetrics.height / 2F
                ),
                drawable = icon,
                colorInt = iconColor,
                iconSizePx = (iconSizePx * currentLiftProportion).toInt(),
                circularClipRadius = circularClipRadius
        )
    }

    private fun createIconBounceAnimator() = ValueAnimator().apply {
        setFloatValues(currentLiftProportion, 1F)
        interpolator = OvershootInterpolator(3.0F)
        duration = bounceAnimationData.duration
        addUpdateListener { animator ->
            currentLiftProportion = animator.animatedValue as Float
            invalidateCallback()
        }
        doOnEnd {
            bounceAnimation = bounceAnimation?.copy(hasEnded = true)
        }
    }
}