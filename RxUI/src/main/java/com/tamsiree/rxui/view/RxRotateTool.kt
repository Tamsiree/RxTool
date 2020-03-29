package com.tamsiree.rxui.view

import android.graphics.PointF

/**
 * @author tamsiree
 */
object RxRotateTool {
    const val CIRCLE_ANGLE = 2 * Math.PI
    fun getRotateAngle(p1: PointF, p2: PointF, mPointCenter: PointF): Double {
        val q1 = getQuadrant(p1, mPointCenter)
        val q2 = getQuadrant(p2, mPointCenter)
        val angle1 = getAngle(p1, mPointCenter)
        val angle2 = getAngle(p2, mPointCenter)
        return if (q1 == q2) {
            angle1 - angle2
        } else {
            0.00
        }
    }

    fun getAngle(p: PointF, mPointCenter: PointF): Double {
        val x = p.x - mPointCenter.x
        val y = mPointCenter.y - p.y
        val angle = Math.atan(y / x.toDouble())
        return getNormalizedAngle(angle)
    }

    fun getQuadrant(p: PointF, mPointCenter: PointF): Int {
        val x = p.x
        val y = p.y
        if (x > mPointCenter.x) {
            if (y > mPointCenter.y) {
                return 4
            } else if (y < mPointCenter.y) {
                return 1
            }
        } else if (x < mPointCenter.x) {
            if (y > mPointCenter.y) {
                return 3
            } else if (y < mPointCenter.y) {
                return 2
            }
        }
        return -1
    }

    fun getNormalizedAngle(angle: Double): Double {
        var angle = angle
        while (angle < 0) {
            angle += CIRCLE_ANGLE
        }
        return angle % CIRCLE_ANGLE
    }
}