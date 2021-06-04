package com.tamsiree.rxui.view.loading

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.tamsiree.rxui.R

class TTextView : AppCompatTextView, TLoadingView {
    private var loaderController: TLoadingManager? = null
    private var defaultColorResource = 0
    private var darkerColorResource = 0

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
        loaderController?.setWidthWeight(typedArray.getFloat(R.styleable.TLoadingView_width_weight, TLoadingProfile.MAX_WEIGHT))
        loaderController?.setHeightWeight(typedArray.getFloat(R.styleable.TLoadingView_height_weight, TLoadingProfile.MAX_WEIGHT))
        loaderController?.setUseGradient(typedArray.getBoolean(R.styleable.TLoadingView_gradient, TLoadingProfile.USE_GRADIENT_DEFAULT))
        loaderController?.setCorners(typedArray.getInt(R.styleable.TLoadingView_corners, TLoadingProfile.CORNER_DEFAULT))
        defaultColorResource = typedArray.getColor(R.styleable.TLoadingView_TLoadingColor, ContextCompat.getColor(context, R.color.D7))
        darkerColorResource = typedArray.getColor(R.styleable.TLoadingView_TLoadingColor, ContextCompat.getColor(context, R.color.B4))
        typedArray.recycle()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        loaderController?.onSizeChanged()
    }

    fun resetLoader() {
        if (!TextUtils.isEmpty(text)) {
            super.setText("")
            loaderController?.startLoading()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        loaderController?.onDraw(canvas, compoundPaddingLeft.toFloat(),
                compoundPaddingTop.toFloat(),
                compoundPaddingRight.toFloat(),
                compoundPaddingBottom.toFloat())
    }

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, type)
        if (loaderController != null) {
            loaderController!!.stopLoading()
        }
    }

    override fun setRectColor(rectPaint: Paint) {
        val typeface = typeface
        if (typeface != null && typeface.style == Typeface.BOLD) {
            rectPaint.color = darkerColorResource
        } else {
            rectPaint.color = defaultColorResource
        }
    }

    override fun valueSet(): Boolean {
        return !TextUtils.isEmpty(text)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loaderController?.removeAnimatorUpdateListener()
    }
}