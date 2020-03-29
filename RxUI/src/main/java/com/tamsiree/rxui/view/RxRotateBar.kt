package com.tamsiree.rxui.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.tamsiree.rxkit.RxImageTool.dip2px
import com.tamsiree.rxui.R
import java.util.*

/**
 * @author Tamsiree
 * @date 2017/10/9
 */
class RxRotateBar : FrameLayout {
    /**
     * animator callback listener
     */
    var animatorListener: AnimatorListener? = null

    // style attr
    private var mRatedColor = 0
    private var mUnratedColor = 0
    private var mTitleColor = 0
    private var mOutlineColor = 0
    private var mDefaultColor = 0
    private var isShowTitle = true
    private var mRatingMax = 0
    private var isShow = false
    private var mCenterX = 0
    private var mCenterY = 0
    private var rotateAngle = 0f
    private var ratingGap = -1
    private var textAlpha = 0
    private var mRatingBars: ArrayList<RxRotateBarBasic>? = null
    private var rotateAnimator: ValueAnimator? = null
    private var ratingAnimator: ValueAnimator? = null
    private var titleAnimator: ValueAnimator? = null
    private var mCenterTextPaint: Paint? = null

    // 蛛网等级填充的颜色
    private var mCenterTextColor = 0
    private var mCenterTextSize = 40
    private var mCenterText: String? = ""
    private var isShowCenterTitle = false

    constructor(context: Context?) : super(context!!)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        // call onDraw in ViewGroup
        setWillNotDraw(false)
        initAttrs(context, attrs)
        mRatingBars = ArrayList()
        initRotatingAnimation()
        initRatingAnimation()
        initTextAnimation()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        //load styled attributes.
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.RxRotateBar, 0, 0)
        //中心标题
        mCenterText = attributes.getString(R.styleable.RxRotateBar_ratingCenterTitle)
        //标题字体大小
        mCenterTextSize = attributes.getDimensionPixelSize(R.styleable.RxRotateBar_centerTitleSize, dip2px(20f))
        mRatedColor = attributes.getColor(R.styleable.RxRotateBar_ratingRatedColor, 0)
        mUnratedColor = attributes.getColor(R.styleable.RxRotateBar_ratingUnratedColor, 0)
        mTitleColor = attributes.getColor(R.styleable.RxRotateBar_ratingTitleColor, 0)
        mOutlineColor = attributes.getColor(R.styleable.RxRotateBar_ratingOutlineColor, 0)
        mCenterTextColor = attributes.getColor(R.styleable.RxRotateBar_ratingCenterColor, Color.WHITE)
        mDefaultColor = attributes.getColor(R.styleable.RxRotateBar_ratingDefaultColor, 0)
        isShowTitle = attributes.getBoolean(R.styleable.RxRotateBar_ratingTitleVisible, true)
        mRatingMax = attributes.getInt(R.styleable.RxRotateBar_ratingMax, 0)
        attributes.recycle()
    }

    private fun initTextAnimation() {
        titleAnimator = ValueAnimator.ofInt(0, 255)
        titleAnimator?.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            textAlpha = animation.animatedValue as Int
            invalidate()
        })
        titleAnimator?.duration = TEXT_ANIMATION_DURATION
        titleAnimator?.interpolator = AccelerateInterpolator()
    }

    fun initRotatingAnimation() {
        rotateAnimator = ValueAnimator.ofFloat(0.0f, 360f * 3)
        rotateAnimator?.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            rotateAngle = animation.animatedValue as Float
            invalidate()
        })
        rotateAnimator?.duration = ROTATING_ANIMATION_DURATION
        rotateAnimator?.interpolator = AccelerateDecelerateInterpolator()
        rotateAnimator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                if (animatorListener != null) {
                    animatorListener!!.onRotateStart()
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                isShowCenterTitle = true
                if (animatorListener != null) {
                    animatorListener!!.onRotateEnd()
                }
                titleAnimator!!.start()
                ratingAnimator!!.start()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    fun initRatingAnimation() {
        ratingAnimator = ValueAnimator.ofInt(0, 9)
        ratingAnimator?.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            ratingGap = animation.animatedValue as Int
            invalidate()
        })
        ratingAnimator?.duration = RATING_ANIMATION_DURATION
        ratingAnimator?.interpolator = LinearInterpolator()
        rotateAnimator!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                if (animatorListener != null) {
                    animatorListener!!.onRatingStart()
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                isShowCenterTitle = true
                if (animatorListener != null) {
                    animatorListener!!.onRatingEnd()
                }
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w / 2
        mCenterY = h / 2
        initRatingBar()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //初始化字体画笔
        mCenterTextPaint = Paint()
        mCenterTextPaint!!.isAntiAlias = true
        mCenterTextPaint!!.color = mCenterTextColor
        mCenterTextPaint!!.textSize = mCenterTextSize.toFloat()
        if (isShow) {
            canvas.save()
            canvas.rotate(rotateAngle, mCenterX.toFloat(), mCenterY.toFloat())
            for (ratingBar in mRatingBars!!) {
                ratingBar.drawOutLine(canvas)
            }
            canvas.restore()
            canvas.save()
            canvas.rotate(-rotateAngle, mCenterX.toFloat(), mCenterY.toFloat())
            for (ratingBar in mRatingBars!!) {
                ratingBar.drawUnRate(canvas)
                ratingBar.drawShadow(canvas)
            }
            canvas.restore()
            if (ratingGap != -1) {
                for (ratingBar in mRatingBars!!) {
                    for (rate in 0 until ratingBar.rate) {
                        if (rate <= ratingGap) {
                            ratingBar.drawRate(canvas, rate)
                        }
                    }
                }
            }
            for (ratingBar in mRatingBars!!) {
                ratingBar.drawTitle(canvas, textAlpha)
            }
            val textWidth = mCenterTextPaint!!.measureText(mCenterText)
            val fontMetrics = mCenterTextPaint!!.fontMetrics
            val textHeight = fontMetrics.descent - fontMetrics.ascent
            if (isShowCenterTitle) {
                canvas.drawText(mCenterText!!, mCenterX - textWidth / 2, mCenterY + textHeight / 4, mCenterTextPaint!!)
            }
        }
    }

    fun addRatingBar(ratingBar: RxRotateBarBasic) {
        mRatingBars?.add(ratingBar)
    }

    fun removeRatingBar(ratingBar: RxRotateBarBasic) {
        mRatingBars?.remove(ratingBar)
    }

    fun removeAll() {
        mRatingBars?.removeAll(mRatingBars!!)
        clear()
    }

    fun clear() {
        isShow = false
        ratingGap = -1
        textAlpha = 0
    }

    fun show() {
        isShowCenterTitle = false
        titleAnimator!!.cancel()
        ratingAnimator!!.cancel()
        if (mRatingBars!!.size == 0) {
            return
        }
        initRatingBar()
        rotateAnimator!!.start()
        isShow = true
    }

    private fun initRatingBar() {
        val dividePart = mRatingBars!!.size
        val sweepAngle = if (dividePart == 1) 360 else (360 - dividePart * STROKE_OFFSET) / dividePart
        val rotateOffset = if (dividePart == 1) 90 else 90 + sweepAngle / 2
        for (i in 0 until dividePart) {
            val startAngle = i * (sweepAngle + STROKE_OFFSET) - rotateOffset.toFloat()
            val ratingBar = mRatingBars!![i]
            if (dividePart == 1) {
                // only show one rating bar
                ratingBar.setIsSingle(true)
            }
            ratingBar.setCenterX(mCenterX)
            ratingBar.setCenterY(mCenterY)
            ratingBar.setStartAngle(startAngle)
            ratingBar.setSweepAngle(sweepAngle.toFloat())

            // style attr
            ratingBar.isShowTitle(isShowTitle)
            if (mDefaultColor != 0) {
                ratingBar.setRatingBarColor(mDefaultColor)
            }
            if (mTitleColor != 0) {
                ratingBar.setTitleColor(mTitleColor)
            }
            if (mRatedColor != 0) {
                ratingBar.setRatedColor(mRatedColor)
            }
            if (mUnratedColor != 0) {
                ratingBar.setUnRatedColor(mUnratedColor)
            }
            if (mOutlineColor != 0) {
                ratingBar.setOutlineColor(mOutlineColor)
            }
            if (mRatingMax != 0) {
                ratingBar.setMaxRate(mRatingMax)
            }
            ratingBar.init()
        }
    }

    fun isShowTitle(isShowTitle: Boolean) {
        this.isShowTitle = isShowTitle
    }

    fun setDefaultColor(color: Int) {
        mDefaultColor = color
    }

    var centerTextColor: Int
        get() = mCenterTextColor
        set(centerTextColor) {
            mCenterTextColor = centerTextColor
            invalidate()
        }

    var centerTextSize: Int
        get() = mCenterTextSize
        set(centerTextSize) {
            mCenterTextSize = centerTextSize
            invalidate()
        }

    var centerText: String?
        get() = mCenterText
        set(centerText) {
            mCenterText = centerText
            invalidate()
        }

    interface AnimatorListener {
        fun onRotateStart()
        fun onRotateEnd()
        fun onRatingStart()
        fun onRatingEnd()
    }

    companion object {
        private const val STROKE_OFFSET = 10
        private const val ROTATING_ANIMATION_DURATION = 3000L
        private const val RATING_ANIMATION_DURATION = 3000L
        private const val TEXT_ANIMATION_DURATION = 1000L
    }
}