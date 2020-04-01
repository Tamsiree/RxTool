package com.tamsiree.rxui.view.mark.canvas

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.tamsiree.rxui.view.mark.model.Coordinate
import com.tamsiree.rxui.view.mark.model.ParentMetrics


object CanvasKit {
    /**
     * Draws a backdrop rectangle with a given paint on the receiving canvas
     */
    internal fun Canvas.drawBackdropRectangle(parentMetrics: ParentMetrics,
                                              canvasY: Float, paint: Paint) {
        val (parentWidth, parentHeight) = parentMetrics

        save()
        translate(0F, canvasY)
        clipRect(0F, 0F, parentWidth, parentHeight)
        drawRect(0F, 0F, parentWidth, parentHeight, paint)
        restore()
    }

    /**
     * Draws a backdrop circle with a given animated value on the receiving canvas
     */
    internal fun Canvas.drawBackdropCircle(
            parentMetrics: ParentMetrics,
            canvasY: Float,
            paint: Paint,
            centerCoordinate: Coordinate,
            radius: Float) {
        val (parentWidth, parentHeight) = parentMetrics
        val (cX, cY) = centerCoordinate

        save()
        translate(0F, canvasY)
        clipRect(0F, 0F, parentWidth, parentHeight)
        drawCircle(cX, cY, radius, paint)
        restore()
    }

    /**
     * Draws a drawable on the canvas at the input coordinates
     */
    internal fun Canvas.drawDrawable(
            canvasY: Float,
            centerCoordinate: Coordinate,
            drawable: Drawable,
            @ColorInt colorInt: Int,
            @Px iconSizePx: Int,
            circularClipRadius: Float?) {
        if (circularClipRadius != null && circularClipRadius <= 0f) {
            return
        }

        val (centerX, centerY) = centerCoordinate

        val left = centerX - (0.5 * iconSizePx)
        val top = centerY - (0.5 * iconSizePx)
        val right = left + iconSizePx
        val bottom = top + iconSizePx
        val bounds = Rect(
                left.toInt(),
                top.toInt(),
                right.toInt(),
                bottom.toInt())

        drawable.bounds = bounds

        save()
        translate(0F, canvasY)
        if (circularClipRadius != null) {
            clipPath(Path().apply { addCircle(centerX, centerY, circularClipRadius, Path.Direction.CCW) })
        }
        drawable.setColorFilter(colorInt, PorterDuff.Mode.SRC_IN)
        drawable.draw(this)
        restore()
    }
}