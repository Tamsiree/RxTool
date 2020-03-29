package com.tamsiree.rxui.view.loadingview.style

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Rect
import android.view.animation.LinearInterpolator
import com.tamsiree.rxui.view.loadingview.animation.SpriteAnimatorBuilder
import com.tamsiree.rxui.view.loadingview.sprite.RectSprite
import com.tamsiree.rxui.view.loadingview.sprite.Sprite
import com.tamsiree.rxui.view.loadingview.sprite.SpriteContainer

/**
 * @author tamsiree
 */
class FoldingCube : SpriteContainer() {
    private val wrapContent = false
    override fun onCreateChild(): Array<Sprite?>? {
        val cubes = arrayOfNulls<Sprite>(4)

        for (i in cubes.indices) {
            cubes[i] = Cube()
            cubes[i]?.setAnimationDelay(300 * i - 1200)
        }
        return cubes
    }

    override fun onBoundsChange(bounds: Rect) {
        var bounds = bounds
        super.onBoundsChange(bounds)
        bounds = clipSquare(bounds)
        var size = Math.min(bounds.width(), bounds.height())
        if (wrapContent) {
            size = Math.sqrt(
                    (size
                            * size) / 2.toDouble()).toInt()
            val oW = (bounds.width() - size) / 2
            val oH = (bounds.height() - size) / 2
            bounds = Rect(
                    bounds.left + oW,
                    bounds.top + oH,
                    bounds.right - oW,
                    bounds.bottom - oH
            )
        }
        val px = bounds.left + size / 2 + 1
        val py = bounds.top + size / 2 + 1
        for (i in 0 until childCount) {
            val sprite = getChildAt(i)
            sprite!!.setDrawBounds0(
                    bounds.left,
                    bounds.top,
                    px,
                    py
            )
            sprite.pivotX = sprite.drawBounds.right.toFloat()
            sprite.pivotY = sprite.drawBounds.bottom.toFloat()
        }
    }

    override fun drawChild(canvas: Canvas) {
        val bounds = clipSquare(bounds)
        for (i in 0 until childCount) {
            val count = canvas.save()
            canvas.rotate(45 + i * 90.toFloat(), bounds.centerX().toFloat(), bounds.centerY().toFloat())
            val sprite = getChildAt(i)
            sprite!!.draw(canvas)
            canvas.restoreToCount(count)
        }
    }

    class Cube : RectSprite() {
        override fun onCreateAnimation(): ValueAnimator? {
            val fractions = floatArrayOf(0f, 0.1f, 0.25f, 0.75f, 0.9f, 1f)
            return SpriteAnimatorBuilder(this).alpha(fractions, 0, 0, 255, 255, 0, 0).rotateX(fractions, -180, -180, 0, 0, 0, 0).rotateY(fractions, 0, 0, 0, 0, 180, 180).duration(2400).interpolator(LinearInterpolator())
                    .build()
        }

        init {
            alpha = 0
            rotateX = -180
        }
    }
}