package com.tamsiree.rxui.view.progressing.animation.interpolator

import android.animation.TimeInterpolator
import android.view.animation.Interpolator
import com.tamsiree.rxui.view.progressing.animation.interpolator.Ease.inOut

/**
 * @author tamsiree
 */
class KeyFrameInterpolator(private val interpolator: TimeInterpolator, private vararg var fractions: Float) : Interpolator {
    fun setFractions(vararg fractions: Float) {
        this.fractions = fractions
    }

    @Synchronized
    override fun getInterpolation(input: Float): Float {
        var input = input
        if (fractions.size > 1) {
            for (i in 0 until fractions.size - 1) {
                val start = fractions[i]
                val end = fractions[i + 1]
                val duration = end - start
                if (input >= start && input <= end) {
                    input = (input - start) / duration
                    return start + (interpolator.getInterpolation(input)
                            * duration)
                }
            }
        }
        return interpolator.getInterpolation(input)
    }

    companion object {
        @JvmStatic
        fun easeInOut(vararg fractions: Float): KeyFrameInterpolator {
            val interpolator = KeyFrameInterpolator(inOut())
            interpolator.setFractions(*fractions)
            return interpolator
        }

        @JvmStatic
        fun pathInterpolator(controlX1: Float, controlY1: Float,
                             controlX2: Float, controlY2: Float,
                             vararg fractions: Float): KeyFrameInterpolator {
            val interpolator = KeyFrameInterpolator(PathInterpolatorCompat.create(controlX1, controlY1, controlX2, controlY2))
            interpolator.setFractions(*fractions)
            return interpolator
        }
    }

}