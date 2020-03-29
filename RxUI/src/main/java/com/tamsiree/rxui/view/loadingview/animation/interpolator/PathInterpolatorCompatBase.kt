package com.tamsiree.rxui.view.loadingview.animation.interpolator

import android.graphics.Path
import android.view.animation.Interpolator

/**
 * @author tamsiree
 * Base implementation for path interpolator compatibility.
 */
internal object PathInterpolatorCompatBase {
    fun create(path: Path?): Interpolator {
        return PathInterpolatorDonut(path)
    }

    fun create(controlX: Float, controlY: Float): Interpolator {
        return PathInterpolatorDonut(controlX, controlY)
    }

    fun create(controlX1: Float, controlY1: Float,
               controlX2: Float, controlY2: Float): Interpolator {
        return PathInterpolatorDonut(controlX1, controlY1, controlX2, controlY2)
    }
}