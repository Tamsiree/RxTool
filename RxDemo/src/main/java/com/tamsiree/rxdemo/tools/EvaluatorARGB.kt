/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tamsiree.rxdemo.tools

import android.animation.TypeEvaluator

/**
 * This evaluator can be used to perform type interpolation between integer
 * values that represent ARGB colors.
 * @author tamsiree
 */
class EvaluatorARGB : TypeEvaluator<Any?> {
    /**
     * This function returns the calculated in-between value for a color
     * given integers that represent the start and end values in the four
     * bytes of the 32-bit int. Each channel is separately linearly interpolated
     * and the resulting calculated values are recombined into the return value.
     *
     * @param fraction   The fraction from the starting to the ending values
     * @param startValue A 32-bit int value representing colors in the
     * separate bytes of the parameter
     * @param endValue   A 32-bit int value representing colors in the
     * separate bytes of the parameter
     * @return A value that is calculated to be the linearly interpolated
     * result, derived by separating the start and end values into separate
     * color channels and interpolating each one separately, recombining the
     * resulting values in the same way.
     */
    override fun evaluate(fraction: Float, startValue: Any?, endValue: Any?): Any? {
        val startInt = startValue as Int
        val startA = startInt shr 24 and 0xff
        val startR = startInt shr 16 and 0xff
        val startG = startInt shr 8 and 0xff
        val startB = startInt and 0xff
        val endInt = endValue as Int
        val endA = endInt shr 24 and 0xff
        val endR = endInt shr 16 and 0xff
        val endG = endInt shr 8 and 0xff
        val endB = endInt and 0xff
        return startA + (fraction * (endA - startA)).toInt() shl 24 or (
                startR + (fraction * (endR - startR)).toInt() shl 16) or (
                startG + (fraction * (endG - startG)).toInt() shl 8) or
                startB + (fraction * (endB - startB)).toInt()
    }

    companion object {
        /**
         * Returns an instance of `EvaluatorARGB` that may be used in
         * [android.animation.ValueAnimator.setEvaluator]. The same instance may
         * be used in multiple `Animator`s because it holds no state.
         *
         * @return An instance of `ArgbEvalutor`.
         */
        val instance = EvaluatorARGB()

    }
}