package com.tamsiree.rxui.view.loadingview.sprite

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint

/**
 * @author tamsiree
 */
open class RectSprite : ShapeSprite() {
    override fun onCreateAnimation(): ValueAnimator? {
        return null
    }

    override fun drawShape(canvas: Canvas?, paint: Paint?) {
        if (drawBounds != null) {
            canvas?.drawRect(drawBounds, paint!!)
        }
    }
}