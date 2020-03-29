package com.tamsiree.rxui.view.other

import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.tamsiree.rxui.R

class TCrossView : View {
    // Arcs that define the set of all points between which the two lines are drawn
    // Names (top, bottom, etc) are from the reference point of the "plus" configuration.
    private var mArcTop: Path? = null
    private var mArcBottom: Path? = null
    private var mArcLeft: Path? = null
    private var mArcRight: Path? = null

    // Pre-compute arc lengths when layout changes
    private var mArcLengthTop = 0f
    private var mArcLengthBottom = 0f
    private var mArcLengthLeft = 0f
    private var mArcLengthRight = 0f
    private var mPaint: Paint? = null
    private var mColor = DEFAULT_COLOR
    private var mRect: RectF? = null
    private var mPathMeasure: PathMeasure? = null
    private var mFromXY: FloatArray? = null
    private var mToXY: FloatArray? = null

    /**
     * Internal state flag for the drawn appearance, plus or cross.
     * The default starting position is "plus". This represents the real configuration, whereas
     * `mPercent` holds the frame-by-frame position when animating between
     * the states.
     */
    private var mState = FLAG_STATE_PLUS

    /**
     * The percent value upon the arcs that line endpoints should be found
     * when drawing.
     */
    private var mPercent = 1f

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        readXmlAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        readXmlAttributes(context, attrs)
    }

    private fun readXmlAttributes(context: Context, attrs: AttributeSet?) {
        // Size will be used for width and height of the icon, plus the space in between
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.TCrossView, 0, 0)
        mColor = try {
            a.getColor(R.styleable.TCrossView_lineColor, DEFAULT_COLOR)
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setPointFromPercent(mArcTop, mArcLengthTop, mPercent, mFromXY!!)
        setPointFromPercent(mArcBottom, mArcLengthBottom, mPercent, mToXY!!)
        canvas.drawLine(mFromXY!![0], mFromXY!![1], mToXY!![0], mToXY!![1], mPaint!!)
        setPointFromPercent(mArcLeft, mArcLengthLeft, mPercent, mFromXY!!)
        setPointFromPercent(mArcRight, mArcLengthRight, mPercent, mToXY!!)
        canvas.drawLine(mFromXY!![0], mFromXY!![1], mToXY!![0], mToXY!![1], mPaint!!)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (changed) {
            init()
            invalidate()
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        var parcelable = super.onSaveInstanceState()
        if (parcelable == null) {
            parcelable = Bundle()
        }
        val savedState = CrossViewState(parcelable)
        savedState.flagState = mState
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is CrossViewState) {
            super.onRestoreInstanceState(state)
            return
        }
        val ss = state
        mState = ss.flagState
        if (mState != FLAG_STATE_PLUS && mState != FLAG_STATE_CROSS) {
            mState = FLAG_STATE_PLUS
        }
        super.onRestoreInstanceState(ss.superState)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        init()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        init()
    }

    fun setColor(argb: Int) {
        mColor = argb
        if (mPaint == null) {
            mPaint = Paint()
        }
        mPaint!!.color = argb
        invalidate()
    }
    /**
     * Tell this view to switch states from cross to plus, or back.
     *
     * @param animationDurationMS duration in milliseconds for the toggle animation
     * @return an integer flag that represents the new state after toggling.
     * This will be either [.FLAG_STATE_PLUS] or [.FLAG_STATE_CROSS]
     */
    /**
     * Tell this view to switch states from cross to plus, or back, using the default animation duration.
     *
     * @return an integer flag that represents the new state after toggling.
     * This will be either [.FLAG_STATE_PLUS] or [.FLAG_STATE_CROSS]
     */
    @JvmOverloads
    fun toggle(animationDurationMS: Long = ANIMATION_DURATION_MS): Int {
        mState = if (mState == FLAG_STATE_PLUS) FLAG_STATE_CROSS else FLAG_STATE_PLUS
        // invert percent, because state was just flipped
        mPercent = 1 - mPercent
        val animator = ValueAnimator.ofFloat(mPercent, 1f)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = animationDurationMS
        animator.addUpdateListener { animation -> setPercent(animation.animatedFraction) }
        animator.start()
        return mState
    }
    /**
     * Transition to "X" over the given animation duration
     *
     * @param animationDurationMS
     */
    /**
     * Transition to "X"
     */
    @JvmOverloads
    fun cross(animationDurationMS: Long = ANIMATION_DURATION_MS) {
        if (mState == FLAG_STATE_CROSS) {
            return
        }
        toggle(animationDurationMS)
    }
    /**
     * Transition to "+" over the given animation duration
     */
    /**
     * Transition to "+"
     */
    @JvmOverloads
    fun plus(animationDurationMS: Long = ANIMATION_DURATION_MS) {
        if (mState == FLAG_STATE_PLUS) {
            return
        }
        toggle(animationDurationMS)
    }

    private fun setPercent(percent: Float) {
        mPercent = percent
        invalidate()
    }

    /**
     * Perform measurements and pre-calculations.  This should be called any time
     * the view measurements or visuals are changed, such as with a call to [.setPadding]
     * or an operating system callback like [.onLayout].
     */
    private fun init() {
        mPaint = Paint()
        mRect = RectF()
        mRect!!.left = paddingLeft.toFloat()
        mRect!!.right = width - paddingRight.toFloat()
        mRect!!.top = paddingTop.toFloat()
        mRect!!.bottom = height - paddingBottom.toFloat()
        mPathMeasure = PathMeasure()
        mArcTop = Path()
        mArcTop!!.addArc(mRect, ARC_TOP_START, ARC_TOP_ANGLE)
        mPathMeasure!!.setPath(mArcTop, false)
        mArcLengthTop = mPathMeasure!!.length
        mArcBottom = Path()
        mArcBottom!!.addArc(mRect, ARC_BOTTOM_START, ARC_BOTTOM_ANGLE)
        mPathMeasure!!.setPath(mArcBottom, false)
        mArcLengthBottom = mPathMeasure!!.length
        mArcLeft = Path()
        mArcLeft!!.addArc(mRect, ARC_LEFT_START, ARC_LEFT_ANGLE)
        mPathMeasure!!.setPath(mArcLeft, false)
        mArcLengthLeft = mPathMeasure!!.length
        mArcRight = Path()
        mArcRight!!.addArc(mRect, ARC_RIGHT_START, ARC_RIGHT_ANGLE)
        mPathMeasure!!.setPath(mArcRight, false)
        mArcLengthRight = mPathMeasure!!.length
        mPaint!!.isAntiAlias = true
        mPaint!!.color = mColor
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeCap = Paint.Cap.SQUARE
        mPaint!!.strokeWidth = DEFAULT_STROKE_WIDTH
        mFromXY = floatArrayOf(0f, 0f)
        mToXY = floatArrayOf(0f, 0f)
    }

    /**
     * Given some path and its length, find the point ([x,y]) on that path at
     * the given percentage of length.  Store the result in `points`.
     *
     * @param path    any path
     * @param length  the length of `path`
     * @param percent the percentage along the path's length to find a point
     * @param points  a float array of length 2, where the coordinates will be stored
     */
    private fun setPointFromPercent(path: Path?, length: Float, percent: Float, points: FloatArray) {
        val percentFromState = if (mState == FLAG_STATE_PLUS) percent else 1 - percent
        mPathMeasure!!.setPath(path, false)
        mPathMeasure!!.getPosTan(length * percentFromState, points, null)
    }

    /**
     * Internal saved state
     */
    internal class CrossViewState : BaseSavedState {
        var flagState = 0

        constructor(superState: Parcelable?) : super(superState)
        private constructor(`in`: Parcel) : super(`in`) {
            flagState = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(flagState)
        }

        companion object {
            val CREATOR: Parcelable.Creator<CrossViewState> = object : Parcelable.Creator<CrossViewState> {
                override fun createFromParcel(`in`: Parcel): CrossViewState? {
                    return CrossViewState(`in`)
                }

                override fun newArray(size: Int): Array<CrossViewState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        /**
         * Flag to denote the "plus" configuration
         */
        const val FLAG_STATE_PLUS = 0

        /**
         * Flag to denote the "cross" configuration
         */
        const val FLAG_STATE_CROSS = 1
        private const val ARC_TOP_START = 225f
        private const val ARC_TOP_ANGLE = 45f
        private const val ARC_BOTTOM_START = 45f
        private const val ARC_BOTTOM_ANGLE = 45f
        private const val ARC_LEFT_START = 315f
        private const val ARC_LEFT_ANGLE = -135f // sweep backwards
        private const val ARC_RIGHT_START = 135f
        private const val ARC_RIGHT_ANGLE = -135f // sweep backwards
        private const val ANIMATION_DURATION_MS = 300L
        private const val DEFAULT_COLOR = Color.BLACK
        private const val DEFAULT_STROKE_WIDTH = 4f
    }
}