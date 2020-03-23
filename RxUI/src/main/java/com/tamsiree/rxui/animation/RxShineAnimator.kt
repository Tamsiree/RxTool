package com.tamsiree.rxui.animation

import android.animation.ValueAnimator
import android.graphics.Canvas
import com.tamsiree.rxui.view.likeview.tools.RxShineView
import com.tamsiree.rxui.view.likeview.tools.ei.RxEase
import com.tamsiree.rxui.view.likeview.tools.ei.RxEasingInterpolator

/**
 * @author tamsiree
 * @date 2016/7/5 下午5:09
 */
class RxShineAnimator : ValueAnimator {
    var MAX_VALUE = 1.5f
    var ANIM_DURATION: Long = 1500
    var canvas: Canvas? = null

    constructor() {
        setFloatValues(1f, MAX_VALUE)
        duration = ANIM_DURATION
        startDelay = 200
        interpolator = RxEasingInterpolator(RxEase.QUART_OUT)
    }

    constructor(duration: Long, maxValue: Float, delay: Long) {
        setFloatValues(1f, maxValue)
        setDuration(duration)
        startDelay = delay
        interpolator = RxEasingInterpolator(RxEase.QUART_OUT)
    }

    fun startAnim(rxShineView: RxShineView?, centerAnimX: Int, centerAnimY: Int) {
        start()
    }

}