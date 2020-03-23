package com.tamsiree.rxui.animation

import android.graphics.Path
import android.graphics.PathMeasure
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * @author Tamsiree
 * @date 2018/6/19
 */
class FloatAnimation(path: Path?, rotation: Float, parent: View, child: View) : Animation() {
    private val mPm: PathMeasure = PathMeasure(path, false)
    private val mView: View
    private val mDistance: Float
    private val mRotation: Float
    override fun applyTransformation(factor: Float, transformation: Transformation) {
        val matrix = transformation.matrix
        mPm.getMatrix(mDistance * factor, matrix, PathMeasure.POSITION_MATRIX_FLAG)
        mView.rotation = mRotation * factor
        var scale = 1f
        if (3000.0f * factor < 200.0f) {
            scale = scale(factor.toDouble(), 0.0, 0.06666667014360428, 0.20000000298023224, 1.100000023841858)
        } else if (3000.0f * factor < 300.0f) {
            scale = scale(factor.toDouble(), 0.06666667014360428, 0.10000000149011612, 1.100000023841858, 1.0)
        }
        mView.scaleX = scale
        mView.scaleY = scale
        transformation.alpha = 1.0f - factor
    }

    companion object {
        private fun scale(a: Double, b: Double, c: Double, d: Double, e: Double): Float {
            return ((a - b) / (c - b) * (e - d) + d).toFloat()
        }
    }

    init {
        mDistance = mPm.length
        mView = child
        mRotation = rotation
        parent.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }
}