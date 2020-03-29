package com.tamsiree.rxui.view.progressing.animation.interpolator

import android.annotation.TargetApi
import android.graphics.Path
import android.os.Build
import android.view.animation.Interpolator
import android.view.animation.PathInterpolator

/**
 * @author tamsiree
 * API 21+ implementation for path interpolator compatibility.
 */
internal object PathInterpolatorCompatApi21 {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun create(path: Path?): Interpolator {
        return PathInterpolator(path)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun create(controlX: Float, controlY: Float): Interpolator {
        return PathInterpolator(controlX, controlY)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun create(controlX1: Float, controlY1: Float,
               controlX2: Float, controlY2: Float): Interpolator {
        return PathInterpolator(controlX1, controlY1, controlX2, controlY2)
    }
}