package com.tamsiree.rxui.view.progressing.style

import android.animation.ValueAnimator
import com.tamsiree.rxui.view.progressing.animation.SpriteAnimatorBuilder
import com.tamsiree.rxui.view.progressing.animation.interpolator.KeyFrameInterpolator.Companion.pathInterpolator
import com.tamsiree.rxui.view.progressing.sprite.RingSprite

/**
 * @author tamsiree
 */
class PulseRing : RingSprite() {
    override fun onCreateAnimation(): ValueAnimator? {
        val fractions = floatArrayOf(0f, 0.7f, 1f)
        return SpriteAnimatorBuilder(this).scale(fractions, 0f, 1f, 1f).alpha(fractions, 255, (255 * 0.7).toInt(), 0).duration(1000).interpolator(pathInterpolator(0.21f, 0.53f, 0.56f, 0.8f, *fractions)).build()
    }

    init {
        setScale(0f)
    }
}