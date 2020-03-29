package com.tamsiree.rxui.view.loadingview.sprite

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint

/**
 * @author tamsiree
 */
open class CircleSprite : ShapeSprite() {
    override fun onCreateAnimation(): ValueAnimator? {
        return null
    }

    override fun drawShape(canvas: Canvas?, paint: Paint?) {
        if (drawBounds != null) {
            val radius = Math.min(drawBounds.width(), drawBounds.height()) / 2
            canvas!!.drawCircle(drawBounds.centerX().toFloat(),
                    drawBounds.centerY().toFloat(),
                    radius.toFloat(), paint!!)
        }
    }
}