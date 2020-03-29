package com.tamsiree.rxui.view.loadingview.sprite

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.FloatProperty
import android.util.IntProperty
import android.util.Property
import com.tamsiree.rxkit.RxAnimationTool.isRunning
import com.tamsiree.rxkit.RxAnimationTool.isStarted
import com.tamsiree.rxkit.RxAnimationTool.start

/**
 * @author tamsiree.
 */
@SuppressLint("NewApi")
abstract class Sprite : Drawable(), ValueAnimator.AnimatorUpdateListener, Animatable, Drawable.Callback {
    var drawBounds = ZERO_BOUNDS_RECT
    private var scale = 1f
    var scaleX = 1f
    var scaleY = 1f
    var pivotX = 0f
    var pivotY = 0f
    var animationDelay = 0
        private set
    var rotateX = 0
    var rotateY = 0
    var translateX = 0
    var translateY = 0
    var rotate = 0
    var translateXPercentage = 0f
    var translateYPercentage = 0f
    private var animator: ValueAnimator? = null
    private var alpha = 255
    private val mCamera: Camera
    private val mMatrix: Matrix
    abstract var color: Int
    override fun getAlpha(): Int {
        return alpha
    }

    override fun setAlpha(alpha: Int) {
        this.alpha = alpha
    }

    @SuppressLint("WrongConstant")
    override fun getOpacity(): Int {
        return PixelFormat.RGBA_8888
    }

    fun getScale(): Float {
        return scale
    }

    fun setScale(scale: Float) {
        this.scale = scale
        scaleX = scale
        scaleY = scale
    }

    fun setAnimationDelay(animationDelay: Int): Sprite {
        this.animationDelay = animationDelay
        return this
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {}
    abstract fun onCreateAnimation(): ValueAnimator?
    override fun start() {
        if (isStarted(animator)) {
            return
        }
        animator = obtainAnimation()
        if (animator == null) {
            return
        }
        start(animator)
        invalidateSelf()
    }

    fun obtainAnimation(): ValueAnimator? {
        if (animator == null) {
            animator = onCreateAnimation()
        }
        if (animator != null) {
            animator!!.addUpdateListener(this)
            animator!!.startDelay = animationDelay.toLong()
        }
        return animator
    }

    override fun stop() {
        if (isStarted(animator)) {
            animator!!.removeAllUpdateListeners()
            animator!!.end()
            reset()
        }
    }

    protected abstract fun drawSelf(canvas: Canvas?)
    fun reset() {
        scale = 1f
        rotateX = 0
        rotateY = 0
        translateX = 0
        translateY = 0
        rotate = 0
        translateXPercentage = 0f
        translateYPercentage = 0f
    }

    override fun isRunning(): Boolean {
        return isRunning(animator)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        setDrawBounds0(bounds)
    }

    fun setDrawBounds0(left: Int, top: Int, right: Int, bottom: Int) {
        drawBounds = Rect(left, top, right, bottom)
        pivotX = drawBounds.centerX().toFloat()
        pivotY = drawBounds.centerY().toFloat()
    }

    override fun invalidateDrawable(who: Drawable) {
        invalidateSelf()
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {}
    override fun unscheduleDrawable(who: Drawable, what: Runnable) {}
    override fun onAnimationUpdate(animation: ValueAnimator) {
        val callback = callback
        callback?.invalidateDrawable(this)
    }


    fun setDrawBounds0(drawBounds: Rect) {
        setDrawBounds0(drawBounds.left, drawBounds.top, drawBounds.right, drawBounds.bottom)
    }

    override fun draw(canvas: Canvas) {
        var tx = translateX
        tx = if (tx == 0) (bounds.width() * translateXPercentage).toInt() else tx
        var ty = translateY
        ty = if (ty == 0) (bounds.height() * translateYPercentage).toInt() else ty
        canvas.translate(tx.toFloat(), ty.toFloat())
        canvas.scale(scaleX, scaleY, pivotX, pivotY)
        canvas.rotate(rotate.toFloat(), pivotX, pivotY)
        if (rotateX != 0 || rotateY != 0) {
            mCamera.save()
            mCamera.rotateX(rotateX.toFloat())
            mCamera.rotateY(rotateY.toFloat())
            mCamera.getMatrix(mMatrix)
            mMatrix.preTranslate(-pivotX, -pivotY)
            mMatrix.postTranslate(pivotX, pivotY)
            mCamera.restore()
            canvas.concat(mMatrix)
        }
        drawSelf(canvas)
    }

    fun clipSquare(rect: Rect): Rect {
        val w = rect.width()
        val h = rect.height()
        val min = Math.min(w, h)
        val cx = rect.centerX()
        val cy = rect.centerY()
        val r = min / 2
        return Rect(
                cx - r,
                cy - r,
                cx + r,
                cy + r
        )
    }

    companion object {
        @JvmField
        val ROTATE_X: Property<Sprite, Int> = object : IntProperty<Sprite>("rotateX") {
            override fun setValue(`object`: Sprite, value: Int) {
                `object`.rotateX = value
            }

            override fun get(`object`: Sprite): Int {
                return `object`.rotateX
            }
        }

        @JvmField
        val ROTATE: Property<Sprite, Int> = object : IntProperty<Sprite>("rotate") {
            override fun setValue(`object`: Sprite, value: Int) {
                `object`.rotate = value
            }

            override fun get(`object`: Sprite): Int {
                return `object`.rotate
            }
        }

        @JvmField
        val ROTATE_Y: Property<Sprite, Int> = object : IntProperty<Sprite>("rotateY") {
            override fun setValue(`object`: Sprite, value: Int) {
                `object`.rotateY = value
            }

            override fun get(`object`: Sprite): Int {
                return `object`.rotateY
            }
        }

        @JvmField
        val TRANSLATE_X: Property<Sprite, Int> = object : IntProperty<Sprite>("translateX") {
            override fun setValue(`object`: Sprite, value: Int) {
                `object`.translateX = value
            }

            override fun get(`object`: Sprite): Int {
                return `object`.translateX
            }
        }

        @JvmField
        val TRANSLATE_Y: Property<Sprite, Int> = object : IntProperty<Sprite>("translateY") {
            override fun setValue(`object`: Sprite, value: Int) {
                `object`.translateY = value
            }

            override fun get(`object`: Sprite): Int {
                return `object`.translateY
            }
        }

        @JvmField
        val TRANSLATE_X_PERCENTAGE: Property<Sprite, Float> = object : FloatProperty<Sprite>("translateXPercentage") {
            override fun setValue(`object`: Sprite, value: Float) {
                `object`.translateXPercentage = value
            }

            override fun get(`object`: Sprite): Float {
                return `object`.translateXPercentage
            }
        }

        @JvmField
        val TRANSLATE_Y_PERCENTAGE: Property<Sprite, Float> = object : FloatProperty<Sprite>("translateYPercentage") {
            override fun setValue(`object`: Sprite, value: Float) {
                `object`.translateYPercentage = value
            }

            override fun get(`object`: Sprite): Float {
                return `object`.translateYPercentage
            }
        }
        val SCALE_X: Property<Sprite, Float> = object : FloatProperty<Sprite>("scaleX") {
            override fun setValue(`object`: Sprite, value: Float) {
                `object`.scaleX = value
            }

            override fun get(`object`: Sprite): Float {
                return `object`.scaleX
            }
        }

        @JvmField
        val SCALE_Y: Property<Sprite, Float> = object : FloatProperty<Sprite>("scaleY") {
            override fun setValue(`object`: Sprite, value: Float) {
                `object`.scaleY = value
            }

            override fun get(`object`: Sprite): Float {
                return `object`.scaleY
            }
        }

        @JvmField
        val SCALE: Property<Sprite, Float> = object : FloatProperty<Sprite>("scale") {
            override fun setValue(`object`: Sprite, value: Float) {
                `object`.setScale(value)
            }

            override fun get(`object`: Sprite): Float {
                return `object`.getScale()
            }
        }

        @JvmField
        val ALPHA: Property<Sprite, Int> = object : IntProperty<Sprite>("alpha") {
            override fun setValue(`object`: Sprite, value: Int) {
                `object`.setAlpha(value)
            }

            override fun get(`object`: Sprite): Int {
                return `object`.getAlpha()
            }
        }
        private val ZERO_BOUNDS_RECT = Rect()
    }

    init {
        mCamera = Camera()
        mMatrix = Matrix()
    }
}