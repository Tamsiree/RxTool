package com.tamsiree.rxui.view.progressing.style

import android.animation.ValueAnimator
import android.graphics.Rect
import com.tamsiree.rxui.view.progressing.animation.SpriteAnimatorBuilder
import com.tamsiree.rxui.view.progressing.sprite.RectSprite

/**
 * @author tamsiree
 */
class RotatingPlane : RectSprite() {
    override fun onBoundsChange(bounds: Rect) {
        drawBounds = clipSquare(bounds)
    }

    override fun onCreateAnimation(): ValueAnimator? {
        val fractions = floatArrayOf(0f, 0.5f, 1f)
        return SpriteAnimatorBuilder(this).rotateX(fractions, 0, -180, -180).rotateY(fractions, 0, 0, -180).duration(1200).easeInOut(*fractions)
                .build()
    }
}