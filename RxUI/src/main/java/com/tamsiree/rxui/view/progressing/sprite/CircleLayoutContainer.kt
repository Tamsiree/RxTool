package com.tamsiree.rxui.view.progressing.sprite

import android.graphics.Canvas
import android.graphics.Rect

/**
 * @author tamsiree
 */
abstract class CircleLayoutContainer : SpriteContainer() {
    override fun drawChild(canvas: Canvas) {
        for (i in 0 until childCount) {
            val sprite = getChildAt(i)
            val count = canvas.save()
            canvas.rotate(i * 360 / childCount.toFloat(),
                    bounds.centerX().toFloat(),
                    bounds.centerY().toFloat())
            sprite!!.draw(canvas)
            canvas.restoreToCount(count)
        }
    }

    override fun onBoundsChange(bounds: Rect) {
        var bounds = bounds
        super.onBoundsChange(bounds)
        bounds = clipSquare(bounds)
        val radius = (bounds.width() * Math.PI / 3.6f / childCount).toInt()
        val left = bounds.centerX() - radius
        val right = bounds.centerX() + radius
        for (i in 0 until childCount) {
            val sprite = getChildAt(i)
            sprite!!.setDrawBounds0(left, bounds.top, right, bounds.top + radius * 2)
        }
    }
}