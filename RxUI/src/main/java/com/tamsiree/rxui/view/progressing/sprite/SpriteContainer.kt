package com.tamsiree.rxui.view.progressing.sprite

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Rect

/**
 * @author tamsiree
 */
abstract class SpriteContainer : Sprite() {
    private val sprites: Array<Sprite?>?
    override var color = 0
        set(color) {
            field = color
            for (i in 0 until childCount) {
                getChildAt(i)!!.color = color
            }
        }

    private fun initCallBack() {
        if (sprites != null) {
            for (sprite in sprites) {
                sprite?.callback = this
            }
        }
    }

    open fun onChildCreated(vararg sprites: Sprite?) {}
    val childCount: Int
        get() = sprites?.size ?: 0

    fun getChildAt(index: Int): Sprite? {
        return sprites?.get(index)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        drawChild(canvas)
    }

    open fun drawChild(canvas: Canvas) {
        if (sprites != null) {
            for (sprite in sprites) {
                val count = canvas.save()
                sprite?.draw(canvas)
                canvas.restoreToCount(count)
            }
        }
    }

    override fun drawSelf(canvas: Canvas?) {}
    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        for (sprite in sprites!!) {
            sprite?.bounds = bounds
        }
    }

    override fun start() {
        super.start()
        start(*sprites!!)
    }

    override fun stop() {
        super.stop()
        stop(*sprites!!)
    }

    override fun isRunning(): Boolean {
        return isRunning(*sprites!!) || super.isRunning()
    }

    abstract fun onCreateChild(): Array<Sprite?>?
    override fun onCreateAnimation(): ValueAnimator? {
        return null
    }

    companion object {
        fun start(vararg sprites: Sprite?) {
            for (sprite in sprites) {
                sprite?.start()
            }
        }

        fun stop(vararg sprites: Sprite?) {
            for (sprite in sprites) {
                sprite?.stop()
            }
        }

        fun isRunning(vararg sprites: Sprite?): Boolean {
            for (sprite in sprites) {
                if (sprite?.isRunning!!) {
                    return true
                }
            }
            return false
        }
    }

    init {
        sprites = onCreateChild()
        initCallBack()
        onChildCreated(*sprites!!)
    }
}