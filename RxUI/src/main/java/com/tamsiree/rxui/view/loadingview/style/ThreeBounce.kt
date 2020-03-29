package com.tamsiree.rxui.view.loadingview.style

import android.animation.ValueAnimator
import android.graphics.Rect
import com.tamsiree.rxui.view.loadingview.animation.SpriteAnimatorBuilder
import com.tamsiree.rxui.view.loadingview.sprite.CircleSprite
import com.tamsiree.rxui.view.loadingview.sprite.Sprite
import com.tamsiree.rxui.view.loadingview.sprite.SpriteContainer

/**
 * @author tamsiree
 */
class ThreeBounce : SpriteContainer() {
    override fun onCreateChild(): Array<Sprite?>? {
        return arrayOf(
                Bounce(),
                Bounce(),
                Bounce()
        )
    }

    override fun onChildCreated(vararg sprites: Sprite?) {
        super.onChildCreated(*sprites)
        sprites[1]?.setAnimationDelay(160)
        sprites[2]?.setAnimationDelay(320)
    }

    override fun onBoundsChange(bounds: Rect) {
        var bounds = bounds
        super.onBoundsChange(bounds)
        bounds = clipSquare(bounds)
        val radius = bounds.width() / 8
        val top = bounds.centerY() - radius
        val bottom = bounds.centerY() + radius
        for (i in 0 until childCount) {
            val left = (bounds.width() * i / 3
                    + bounds.left)
            getChildAt(i)!!.setDrawBounds0(
                    left, top, left + radius * 2, bottom
            )
        }
    }

    private inner class Bounce internal constructor() : CircleSprite() {
        override fun onCreateAnimation(): ValueAnimator? {
            val fractions = floatArrayOf(0f, 0.4f, 0.8f, 1f)
            return SpriteAnimatorBuilder(this).scale(fractions, 0f, 1f, 0f, 0f).duration(1400).easeInOut(*fractions)
                    .build()
        }

        init {
            setScale(0f)
        }
    }
}