package com.tamsiree.rxui.view.mark.canvas


import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.animation.doOnEnd
import com.tamsiree.rxui.view.mark.canvas.CanvasUtils.drawDrawable
import com.tamsiree.rxui.view.mark.model.*


/**
 * Lightweight canvas animation for drawing a color change animation at a given trigger proportion
 */
internal class IconChangeColorCanvasDrawable(
        private val colorChangeAnimationData: AnimationData,
        @Px
        private val iconInsetPx: Int,
        @Px
        private val iconSizePx: Int,
        private val iconColor: ColorChange,
        private val icon: Drawable
) : CanvasDrawable {

    override var invalidateCallback: () -> Unit = {}

    private var colorAnimation: CanvasAnimation? = null

    @ColorInt
    private var currentIconColor: Int = iconColor.start

    override fun onDraw(canvas: Canvas, parentMetrics: ParentMetrics, canvasY: Float, proportion: Float) {
        if (proportion >= colorChangeAnimationData.startProportion && colorAnimation == null) {
            val animator = createColorInterpolationAnimator()
            colorAnimation = CanvasAnimation(animator)
            animator.start()
        }

        drawIcon(canvas, parentMetrics, canvasY)
    }

    override fun reset() {
        colorAnimation?.animator?.cancel()
        colorAnimation = null
        currentIconColor = iconColor.start
    }

    private fun drawIcon(canvas: Canvas, parentMetrics: ParentMetrics, canvasY: Float) {
        canvas.drawDrawable(
                canvasY,
                centerCoordinate = Coordinate(
                        x = parentMetrics.width - iconInsetPx.toFloat(),
                        y = parentMetrics.height / 2F
                ),
                drawable = icon,
                colorInt = currentIconColor,
                iconSizePx = iconSizePx,
                circularClipRadius = null)
    }

    private fun createColorInterpolationAnimator() = ValueAnimator().apply {
        val (start, end) = iconColor
        setIntValues(start, end)
        setEvaluator(ArgbEvaluator())
        duration = colorChangeAnimationData.duration
        interpolator = LinearInterpolator()
        addUpdateListener { animator ->
            currentIconColor = animator.animatedValue as Int
            invalidateCallback()
        }
        doOnEnd {
            colorAnimation = colorAnimation?.copy(hasEnded = true)
        }
    }
}