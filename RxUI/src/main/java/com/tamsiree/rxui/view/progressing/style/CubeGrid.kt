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
class CubeGrid : SpriteContainer() {
    override fun onCreateChild(): Array<Sprite?>? {
        val delays = intArrayOf(
                200, 300, 400, 100, 200, 300, 0, 100, 200
        )
        val gridItems = arrayOfNulls<Sprite>(9)
        for (i in gridItems.indices) {
            gridItems[i] = GridItem()
            gridItems[i]!!.setAnimationDelay(delays[i])
        }
        return gridItems
    }

    override fun onBoundsChange(bounds: Rect) {
        var bounds = bounds
        super.onBoundsChange(bounds)
        bounds = clipSquare(bounds)
        val width = (bounds.width() * 0.33f).toInt()
        val height = (bounds.height() * 0.33f).toInt()
        for (i in 0 until childCount) {
            val x = i % 3
            val y = i / 3
            val l = bounds.left + x * width
            val t = bounds.top + y * height
            val sprite = getChildAt(i)
            sprite!!.setDrawBounds0(l, t, l + width, t + height)
        }
    }

    private inner class GridItem : RectSprite() {
        override fun onCreateAnimation(): ValueAnimator? {
            val fractions = floatArrayOf(0f, 0.35f, 0.7f, 1f)
            return SpriteAnimatorBuilder(this).scale(fractions, 1f, 0f, 1f, 1f).duration(1300).easeInOut(*fractions)
                    .build()
        }
    }
}