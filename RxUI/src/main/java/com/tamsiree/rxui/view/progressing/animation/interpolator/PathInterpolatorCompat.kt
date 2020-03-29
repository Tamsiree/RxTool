package com.tamsiree.rxui.view.progressing.animation.interpolator

import android.graphics.Path
import android.os.Build
import android.view.animation.Interpolator

/**
 * @author tamsiree
 * Helper for creating path-based [Interpolator] instances. On API 21 or newer, the
 * platform implementation will be used and on older platforms a compatible alternative
 * implementation will be used.
 */
object PathInterpolatorCompat {
    /**
     * Create an [Interpolator] for an arbitrary [Path]. The [Path]
     * must begin at `(0, 0)` and end at `(1, 1)`. The x-coordinate along the
     * [Path] is the input value and the output is the y coordinate of the line at that
     * point. This means that the Path must conform to a function `y = f(x)`.
     *
     *
     * The [Path] must not have gaps in the x direction and must not
     * loop back on itself such that there can be two points sharing the same x coordinate.
     *
     * @param path the [Path] to use to make the line representing the [Interpolator]
     * @return the [Interpolator] representing the [Path]
     */
    fun create(path: Path?): Interpolator {
        return if (Build.VERSION.SDK_INT >= 21) {
            PathInterpolatorCompatApi21.create(path)
        } else PathInterpolatorCompatBase.create(path)
    }

    /**
     * Create an [Interpolator] for a quadratic Bezier curve. The end points
     * `(0, 0)` and `(1, 1)` are assumed.
     *
     * @param controlX the x coordinate of the quadratic Bezier control point
     * @param controlY the y coordinate of the quadratic Bezier control point
     * @return the [Interpolator] representing the quadratic Bezier curve
     */
    fun create(controlX: Float, controlY: Float): Interpolator {
        return if (Build.VERSION.SDK_INT >= 21) {
            PathInterpolatorCompatApi21.create(controlX, controlY)
        } else PathInterpolatorCompatBase.create(controlX, controlY)
    }

    /**
     * Create an [Interpolator] for a cubic Bezier curve.  The end points
     * `(0, 0)` and `(1, 1)` are assumed.
     *
     * @param controlX1 the x coordinate of the first control point of the cubic Bezier
     * @param controlY1 the y coordinate of the first control point of the cubic Bezier
     * @param controlX2 the x coordinate of the second control point of the cubic Bezier
     * @param controlY2 the y coordinate of the second control point of the cubic Bezier
     * @return the [Interpolator] representing the cubic Bezier curve
     */
    fun create(controlX1: Float, controlY1: Float,
               controlX2: Float, controlY2: Float): Interpolator {
        return if (Build.VERSION.SDK_INT >= 21) {
            PathInterpolatorCompatApi21.create(controlX1, controlY1, controlX2, controlY2)
        } else PathInterpolatorCompatBase.create(controlX1, controlY1, controlX2, controlY2)
    }
}