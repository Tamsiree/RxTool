package com.tamsiree.rxui.view.progressing.style

import android.animation.ValueAnimator
import android.graphics.Rect
import com.tamsiree.rxui.view.progressing.animation.SpriteAnimatorBuilder
import com.tamsiree.rxui.view.progressing.sprite.RectSprite
import com.tamsiree.rxui.view.progressing.sprite.Sprite
import com.tamsiree.rxui.view.progressing.sprite.SpriteContainer

/**
 * @author tamsiree
 */
class Wave : SpriteContainer() {
    override fun onCreateChild(): Array<Sprite?>? {
        val waveItems = arrayOfNulls<Sprite>(5)
        for (i in waveItems.indices) {
            waveItems[i] = WaveItem()
            waveItems[i]!!.setAnimationDelay(-1200 + i * 100)
        }
        return waveItems
    }

    override fun onBoundsChange(bounds: Rect) {
        var bounds = bounds
        super.onBoundsChange(bounds)
        bounds = clipSquare(bounds)
        val rw = bounds.width() / childCount
        val width = bounds.width() / 5 * 3 / 5
        for (i in 0 until childCount) {
            val sprite = getChildAt(i)
            val l = bounds.left + i * rw + rw / 5
            val r = l + width
            sprite!!.setDrawBounds0(l, bounds.top, r, bounds.bottom)
        }
    }

    private inner class WaveItem internal constructor() : RectSprite() {
        override fun onCreateAnimation(): ValueAnimator? {
            val fractions = floatArrayOf(0f, 0.2f, 0.4f, 1f)
            return SpriteAnimatorBuilder(this).scaleY(fractions, 0.4f, 1f, 0.4f, 0.4f).duration(1200).easeInOut(*fractions)
                    .build()
        }

        init {
            scaleY = 0.4f
        }
    }
}