package com.tamsiree.rxui.view.roundprogressbar.common

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxui.R

/**
 * @author tamsiree
 */
abstract class RxBaseRoundProgress : LinearLayout {
    private var layoutBackground: LinearLayout? = null
    private var layoutProgress: LinearLayout? = null
    private var layoutSecondaryProgress: LinearLayout? = null
    private var radius = 0
    private var padding = 0
    private var totalWidth = 0
    private var max = 0f
    private var progress = 0f
    private var secondaryProgress = 0f
    private var colorBackground = 0
    private var colorProgress = 0
    private var colorSecondaryProgress = 0
    private var isReverse = false
    private var progressChangedListener: OnProgressChangedListener? = null

    constructor(context: Context) : super(context) {
        setup(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context, attrs)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        setup(context, attrs)
    }

    // Setup a progress bar layout by sub class
    protected abstract fun initLayout(): Int

    // Setup an attribute parameter on sub class
    protected abstract fun initStyleable(context: Context, attrs: AttributeSet?)

    // Initial any view on sub class
    protected abstract fun initView()

    // Draw a progress by sub class
    protected abstract fun drawProgress(layoutProgress: LinearLayout?, max: Float, progress: Float, totalWidth: Float,
                                        radius: Int, padding: Int, colorProgress: Int, isReverse: Boolean)

    // Draw all view on sub class
    protected abstract fun onViewDraw()
    fun setup(context: Context, attrs: AttributeSet?) {
        setupStyleable(context, attrs)
        removeAllViews()
        // Setup layout for sub class
        LayoutInflater.from(context).inflate(initLayout(), this)
        // Initial default view
        layoutBackground = findViewById(R.id.layout_background)
        layoutProgress = findViewById(R.id.layout_progress)
        layoutSecondaryProgress = findViewById(R.id.layout_secondary_progress)
        initView()
    }

    // Retrieve initial parameter from view attribute
    fun setupStyleable(context: Context, attrs: AttributeSet?) {

        //获得这个控件对应的属性。
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxBaseRoundProgress)
        radius = typedArray.getDimension(R.styleable.RxBaseRoundProgress_rcRadius, dp2px(context, DEFAULT_PROGRESS_RADIUS.toFloat()).toFloat()).toInt()
        padding = typedArray.getDimension(R.styleable.RxBaseRoundProgress_rcBackgroundPadding, dp2px(context, DEFAULT_BACKGROUND_PADDING.toFloat()).toFloat()).toInt()
        isReverse = typedArray.getBoolean(R.styleable.RxBaseRoundProgress_rcReverse, false)
        max = typedArray.getFloat(R.styleable.RxBaseRoundProgress_rcMax, DEFAULT_MAX_PROGRESS.toFloat())
        progress = typedArray.getFloat(R.styleable.RxBaseRoundProgress_rcProgress, DEFAULT_PROGRESS.toFloat())
        secondaryProgress = typedArray.getFloat(R.styleable.RxBaseRoundProgress_rcSecondaryProgress, DEFAULT_SECONDARY_PROGRESS.toFloat())
        val colorBackgroundDefault = context.resources.getColor(R.color.Gray5F)
        colorBackground = typedArray.getColor(R.styleable.RxBaseRoundProgress_rcBackgroundColor, colorBackgroundDefault)
        val colorProgressDefault = context.resources.getColor(R.color.Gray7F)
        colorProgress = typedArray.getColor(R.styleable.RxBaseRoundProgress_rcProgressColor, colorProgressDefault)
        val colorSecondaryProgressDefault = context.resources.getColor(R.color.TransColor)
        colorSecondaryProgress = typedArray.getColor(R.styleable.RxBaseRoundProgress_rcSecondaryProgressColor, colorSecondaryProgressDefault)
        typedArray.recycle()
        initStyleable(context, attrs)
    }

    // Progress bar always refresh when view size has changed
    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        totalWidth = newWidth
        drawAll()
        postDelayed({
            drawPrimaryProgress()
            drawSecondaryProgress()
        }, 5)
    }

    // Redraw all view
    protected fun drawAll() {
        drawBackgroundProgress()
        drawPadding()
        drawProgressReverse()
        drawPrimaryProgress()
        drawSecondaryProgress()
        onViewDraw()
    }

    // Draw progress background
    private fun drawBackgroundProgress() {
        val backgroundDrawable = createGradientDrawable(colorBackground)
        val newRadius = radius - padding / 2
        backgroundDrawable.cornerRadii = floatArrayOf(newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat())
        layoutBackground!!.background = backgroundDrawable
    }

    // Create an empty color rectangle gradient drawable
    protected fun createGradientDrawable(color: Int): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(color)
        return gradientDrawable
    }

    private fun drawPrimaryProgress() {
        drawProgress(layoutProgress, max, progress, totalWidth.toFloat(), radius, padding, colorProgress, isReverse)
    }

    private fun drawSecondaryProgress() {
        drawProgress(layoutSecondaryProgress, max, secondaryProgress, totalWidth.toFloat(), radius, padding, colorSecondaryProgress, isReverse)
    }

    private fun drawProgressReverse() {
        setupReverse(layoutProgress)
        setupReverse(layoutSecondaryProgress)
    }

    // Change progress position by depending on isReverse flag
    private fun setupReverse(layoutProgress: LinearLayout?) {
        val progressParams = layoutProgress!!.layoutParams as RelativeLayout.LayoutParams
        removeLayoutParamsRule(progressParams)
        if (isReverse) {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            // For support with RTL on API 17 or more
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        } else {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            // For support with RTL on API 17 or more
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_START)
        }
        layoutProgress.layoutParams = progressParams
    }

    private fun drawPadding() {
        layoutBackground!!.setPadding(padding, padding, padding, padding)
    }

    // Remove all of relative align rule
    private fun removeLayoutParamsRule(layoutParams: RelativeLayout.LayoutParams) {
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
    }

    /*    @SuppressLint("NewApi")
    protected float dp2px(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / (DisplayMetrics.DENSITY_DEFAULT * 1.0f)));
    }*/

    fun getReverse(): Boolean {
        return isReverse
    }


    fun setReverse(isReverse: Boolean) {
        this.isReverse = isReverse
        drawProgressReverse()
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    fun getRadius(): Int {
        return radius
    }

    fun setRadius(radius: Int) {
        if (radius >= 0) {
            this.radius = radius
        }
        drawBackgroundProgress()
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    fun getPadding(): Int {
        return padding
    }

    fun setPadding(padding: Int) {
        if (padding >= 0) {
            this.padding = padding
        }
        drawPadding()
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    fun getMax(): Float {
        return max
    }

    fun setMax(max: Float) {
        if (max >= 0) {
            this.max = max
        }
        if (progress > max) {
            progress = max
        }
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    val layoutWidth: Float
        get() = totalWidth.toFloat()


    fun getProgress(): Float {
        return progress
    }


    open fun setProgress(progress: Float) {
        if (progress < 0) {
            this.progress = 0f
        } else this.progress = Math.min(progress, max)
        drawPrimaryProgress()
        if (progressChangedListener != null) {
            progressChangedListener!!.onProgressChanged(id, this.progress, true, false)
        }
    }

    val secondaryProgressWidth: Float
        get() = if (layoutSecondaryProgress != null) {
            layoutSecondaryProgress!!.width.toFloat()
        } else 0f

    fun getSecondaryProgress(): Float {
        return secondaryProgress
    }

    fun setSecondaryProgress(secondaryProgress: Float) {
        if (secondaryProgress < 0) {
            this.secondaryProgress = 0f
        } else if (secondaryProgress > max) {
            this.secondaryProgress = max
        } else {
            this.secondaryProgress = secondaryProgress
        }
        drawSecondaryProgress()
        if (progressChangedListener != null) {
            progressChangedListener!!.onProgressChanged(id, this.secondaryProgress, false, true)
        }
    }

    var progressBackgroundColor: Int
        get() = colorBackground
        set(colorBackground) {
            this.colorBackground = colorBackground
            drawBackgroundProgress()
        }

    var progressColor: Int
        get() = colorProgress
        set(colorProgress) {
            this.colorProgress = colorProgress
            drawPrimaryProgress()
        }

    var secondaryProgressColor: Int
        get() = colorSecondaryProgress
        set(colorSecondaryProgress) {
            this.colorSecondaryProgress = colorSecondaryProgress
            drawSecondaryProgress()
        }

    fun setOnProgressChangedListener(listener: OnProgressChangedListener?) {
        progressChangedListener = listener
    }

    override fun invalidate() {
        super.invalidate()
        drawAll()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.radius = radius
        ss.padding = padding
        ss.colorBackground = colorBackground
        ss.colorProgress = colorProgress
        ss.colorSecondaryProgress = colorSecondaryProgress
        ss.max = max
        ss.progress = progress
        ss.secondaryProgress = secondaryProgress
        ss.isReverse = isReverse
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        val ss = state
        super.onRestoreInstanceState(ss.superState)
        radius = ss.radius
        padding = ss.padding
        colorBackground = ss.colorBackground
        colorProgress = ss.colorProgress
        colorSecondaryProgress = ss.colorSecondaryProgress
        max = ss.max
        progress = ss.progress
        secondaryProgress = ss.secondaryProgress
        isReverse = ss.isReverse
    }

    private class SavedState : BaseSavedState {
        var max = 0f
        var progress = 0f
        var secondaryProgress = 0f
        var radius = 0
        var padding = 0
        var colorBackground = 0
        var colorProgress = 0
        var colorSecondaryProgress = 0
        var isReverse = false

        internal constructor(superState: Parcelable?) : super(superState)
        private constructor(`in`: Parcel) : super(`in`) {
            max = `in`.readFloat()
            progress = `in`.readFloat()
            secondaryProgress = `in`.readFloat()
            radius = `in`.readInt()
            padding = `in`.readInt()
            colorBackground = `in`.readInt()
            colorProgress = `in`.readInt()
            colorSecondaryProgress = `in`.readInt()
            isReverse = `in`.readByte().toInt() != 0
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(max)
            out.writeFloat(progress)
            out.writeFloat(secondaryProgress)
            out.writeInt(radius)
            out.writeInt(padding)
            out.writeInt(colorBackground)
            out.writeInt(colorProgress)
            out.writeInt(colorSecondaryProgress)
            out.writeByte((if (isReverse) 1 else 0).toByte())
        }

        companion object {
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState? {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    interface OnProgressChangedListener {
        fun onProgressChanged(viewId: Int, progress: Float, isPrimaryProgress: Boolean, isSecondaryProgress: Boolean)
    }

    companion object {
        protected const val DEFAULT_MAX_PROGRESS = 100
        protected const val DEFAULT_PROGRESS = 0
        protected const val DEFAULT_SECONDARY_PROGRESS = 0
        protected const val DEFAULT_PROGRESS_RADIUS = 30
        protected const val DEFAULT_BACKGROUND_PADDING = 0
    }
}