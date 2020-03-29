package com.tamsiree.rxui.view.loadingview.style

import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.animation.LinearInterpolator
import com.tamsiree.rxui.view.loadingview.animation.SpriteAnimatorBuilder
import com.tamsiree.rxui.view.loadingview.sprite.CircleSprite
import com.tamsiree.rxui.view.loadingview.sprite.Sprite
import com.tamsiree.rxui.view.loadingview.sprite.SpriteContainer

/**
 * @author tamsiree
 */
class ChasingDots : SpriteContainer() {
    override fun onCreateChild(): Array<Sprite?>? {
        return arrayOf(
                Dot(),
                Dot()
        )
    }

    override fun onChildCreated(vararg sprites: Sprite?) {
        super.onChildCreated(*sprites)
        sprites[1]?.setAnimationDelay(-1000)
    }

    override fun onCreateAnimation(): ValueAnimator? {
        val fractions = floatArrayOf(0f, 1f)
        return SpriteAnimatorBuilder(this).rotate(fractions, 0, 360).duration(2000).interpolator(LinearInterpolator()).build()
    }

    override fun onBoundsChange(bounds: Rect) {
        var bounds = bounds
        super.onBoundsChange(bounds)
        bounds = clipSquare(bounds)
        val drawW = (bounds.width() * 0.6f).toInt()
        getChildAt(0)!!.setDrawBounds0(
                bounds.right - drawW,
                bounds.top,
                bounds.right
                , bounds.top + drawW
        )
        getChildAt(1)!!.setDrawBounds0(
                bounds.right - drawW,
                bounds.bottom - drawW,
                bounds.right,
                bounds.bottom
        )
    }

    private inner class Dot internal constructor() : CircleSprite() {
        override fun onCreateAnimation(): ValueAnimator? {
            val fractions = floatArrayOf(0f, 0.5f, 1f)
            return SpriteAnimatorBuilder(this).scale(fractions, 0f, 1f, 0f).duration(2000).easeInOut(*fractions)
                    .build()
        }

        init {
            setScale(0f)
        }
    }
}