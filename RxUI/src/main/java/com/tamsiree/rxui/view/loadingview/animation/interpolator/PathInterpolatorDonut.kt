package com.tamsiree.rxui.view.loadingview.animation.interpolator

import android.graphics.Path
import android.graphics.PathMeasure
import android.view.animation.Interpolator

/**
 * @author tamsiree
 * A path interpolator implementation compatible with API 4+.
 */
internal class PathInterpolatorDonut(path: Path?) : Interpolator {
    private val mX: FloatArray
    private val mY: FloatArray

    constructor(controlX: Float, controlY: Float) : this(createQuad(controlX, controlY))
    constructor(controlX1: Float, controlY1: Float,
                controlX2: Float, controlY2: Float) : this(createCubic(controlX1, controlY1, controlX2, controlY2))

    override fun getInterpolation(t: Float): Float {
        if (t <= 0.0f) {
            return 0.0f
        } else if (t >= 1.0f) {
            return 1.0f
        }

        // Do a binary search for the correct x to interpolate between.
        var startIndex = 0
        var endIndex = mX.size - 1
        while (endIndex - startIndex > 1) {
            val midIndex = (startIndex + endIndex) / 2
            if (t < mX[midIndex]) {
                endIndex = midIndex
            } else {
                startIndex = midIndex
            }
        }
        val xRange = mX[endIndex] - mX[startIndex]
        if (xRange == 0f) {
            return mY[startIndex]
        }
        val tInRange = t - mX[startIndex]
        val fraction = tInRange / xRange
        val startY = mY[startIndex]
        val endY = mY[endIndex]
        return startY + fraction * (endY - startY)
    }

    companion object {
        /**
         * Governs the accuracy of the approximation of the [Path].
         */
        private const val PRECISION = 0.002f
        private fun createQuad(controlX: Float, controlY: Float): Path {
            val path = Path()
            path.moveTo(0.0f, 0.0f)
            path.quadTo(controlX, controlY, 1.0f, 1.0f)
            return path
        }

        private fun createCubic(controlX1: Float, controlY1: Float,
                                controlX2: Float, controlY2: Float): Path {
            val path = Path()
            path.moveTo(0.0f, 0.0f)
            path.cubicTo(controlX1, controlY1, controlX2, controlY2, 1.0f, 1.0f)
            return path
        }
    }

    init {
        val pathMeasure = PathMeasure(path, false /* forceClosed */)
        val pathLength = pathMeasure.length
        val numPoints = (pathLength / PRECISION).toInt() + 1
        mX = FloatArray(numPoints)
        mY = FloatArray(numPoints)
        val position = FloatArray(2)
        for (i in 0 until numPoints) {
            val distance = i * pathLength / (numPoints - 1)
            pathMeasure.getPosTan(distance, position, null /* tangent */)
            mX[i] = position[0]
            mY[i] = position[1]
        }
    }
}