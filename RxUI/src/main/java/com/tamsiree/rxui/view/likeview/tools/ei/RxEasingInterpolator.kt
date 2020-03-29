package com.tamsiree.rxui.view.likeview.tools.ei

import android.animation.TimeInterpolator

/**
 * @author tamsiree
 * The Easing class provides a collection of ease functions. It does not use the standard 4 param
 * ease signature. Instead it uses a single param which indicates the current linear ratio (0 to 1) of the tween.
 */
class RxEasingInterpolator(val ease: RxEase) : TimeInterpolator {
    override fun getInterpolation(input: Float): Float {
        return RxEasingProvider.get(ease, input)
    }

}