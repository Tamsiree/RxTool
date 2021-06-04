package com.tamsiree.rxui.view.loading

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tamsiree.rxui.R

class TImageView : AppCompatImageView, TLoadingView {
    private var loaderController: TLoadingManager? = null
    private var defaultColorResource = 0

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        loaderController = TLoadingManager(this)
        @SuppressLint("CustomViewStyleable") val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TLoadingView, 0, 0)
        loaderController!!.setUseGradient(typedArray.getBoolean(R.styleable.TLoadingView_gradient, TLoadingProfile.USE_GRADIENT_DEFAULT))
        loaderController!!.setCorners(typedArray.getInt(R.styleable.TLoadingView_corners, TLoadingProfile.CORNER_DEFAULT))
        defaultColorResource = typedArray.getColor(R.styleable.TLoadingView_TLoadingColor, ContextCompat.getColor(context, R.color.D7))
        typedArray.recycle()
    }

    fun resetLoader() {
        if (drawable != null) {
            super.setImageDrawable(null)
            loaderController!!.startLoading()
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        loaderController!!.onSizeChanged()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        loaderController!!.onDraw(canvas)
    }

    override fun setRectColor(rectPaint: Paint) {
        rectPaint.color = defaultColorResource
    }

    override fun valueSet(): Boolean {
        return drawable != null
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        loaderController!!.stopLoading()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        loaderController!!.stopLoading()
    }

    override fun setImageIcon(icon: Icon?) {
        super.setImageIcon(icon)
        loaderController!!.stopLoading()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        loaderController!!.stopLoading()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loaderController!!.removeAnimatorUpdateListener()
    }
}