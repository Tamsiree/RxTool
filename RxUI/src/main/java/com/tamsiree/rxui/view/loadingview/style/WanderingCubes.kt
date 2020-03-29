package com.tamsiree.rxui.view.loadingview.style

import android.animation.ValueAnimator
import android.graphics.Rect
import com.tamsiree.rxui.view.loadingview.animation.SpriteAnimatorBuilder
import com.tamsiree.rxui.view.loadingview.sprite.RectSprite
import com.tamsiree.rxui.view.loadingview.sprite.Sprite
import com.tamsiree.rxui.view.loadingview.sprite.SpriteContainer

/**
 * @author tamsiree
 */
class WanderingCubes : SpriteContainer() {
    override fun onCreateChild(): Array<Sprite?>? {
        return arrayOf(
                Cube(),
                Cube()
        )
    }

    override fun onChildCreated(vararg sprites: Sprite?) {
        super.onChildCreated(*sprites)
        sprites[1]?.setAnimationDelay(-900)
    }

    override fun onBoundsChange(bounds: Rect) {
        var bounds = bounds
        bounds = clipSquare(bounds)
        super.onBoundsChange(bounds)
        for (i in 0 until childCount) {
            val sprite = getChildAt(i)
            sprite!!.setDrawBounds0(
                    bounds.left,
                    bounds.top,
                    bounds.left + bounds.width() / 4,
                    bounds.top + bounds.height() / 4
            )
        }
    }

    private inner class Cube : RectSprite() {
        override fun onCreateAnimation(): ValueAnimator? {
            val fractions = floatArrayOf(0f, 0.25f, 0.5f, 0.51f, 0.75f, 1f)
            return SpriteAnimatorBuilder(this).rotate(fractions, 0, -90, -179, -180, -270, -360).translateXPercentage(fractions, 0f, 0.75f, 0.75f, 0.75f, 0f, 0f).translateYPercentage(fractions, 0f, 0f, 0.75f, 0.75f, 0.75f, 0f).scale(fractions, 1f, 0.5f, 1f, 1f, 0.5f, 1f).duration(1800).easeInOut(*fractions)
                    .build()
        }
    }
}