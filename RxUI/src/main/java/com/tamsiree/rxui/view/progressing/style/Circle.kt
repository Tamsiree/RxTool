package com.tamsiree.rxui.view.progressing.style

import android.animation.ValueAnimator
import com.tamsiree.rxui.view.progressing.animation.SpriteAnimatorBuilder
import com.tamsiree.rxui.view.progressing.sprite.CircleLayoutContainer
import com.tamsiree.rxui.view.progressing.sprite.CircleSprite
import com.tamsiree.rxui.view.progressing.sprite.Sprite

/**
 * @author tamsiree
 */
class Circle : CircleLayoutContainer() {
    override fun onCreateChild(): Array<Sprite?>? {
        val dots = arrayOfNulls<Sprite>(12)
        for (i in dots.indices) {
            dots[i] = Dot()
            dots[i]!!.setAnimationDelay(1200 / 12 * i + -1200)
        }
        return dots
    }

    private inner class Dot internal constructor() : CircleSprite() {
        override fun onCreateAnimation(): ValueAnimator? {
            val fractions = floatArrayOf(0f, 0.5f, 1f)
            return SpriteAnimatorBuilder(this).scale(fractions, 0f, 1f, 0f).duration(1200).easeInOut(*fractions)
                    .build()
        }

        init {
            setScale(0f)
        }
    }
}