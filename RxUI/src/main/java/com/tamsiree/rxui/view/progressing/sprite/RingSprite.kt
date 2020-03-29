package com.tamsiree.rxui.view.progressing.sprite

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint

/**
 * @author tamsiree
 */
open class RingSprite : ShapeSprite() {
    override fun drawShape(canvas: Canvas?, paint: Paint?) {
        if (drawBounds != null) {
            paint!!.style = Paint.Style.STROKE
            val radius = Math.min(drawBounds.width(), drawBounds.height()) / 2
            paint.strokeWidth = radius / 12.toFloat()
            canvas!!.drawCircle(drawBounds.centerX().toFloat(),
                    drawBounds.centerY().toFloat(),
                    radius.toFloat(), paint)
        }
    }

    override fun onCreateAnimation(): ValueAnimator? {
        return null
    }
}