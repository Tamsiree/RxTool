package com.tamsiree.rxui.view.dialog.shapeloadingview

import android.animation.ArgbEvaluator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import com.tamsiree.rxui.R

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class RxShapeView : View {
    var shape = Shape.SHAPE_CIRCLE
        private set
    private val mInterpolator: Interpolator = DecelerateInterpolator()
    private val mArgbEvaluator = ArgbEvaluator()
    private var mTriangleColor = 0
    private var mCircleColor = 0
    private var mRectColor = 0

    /**
     * 用赛贝尔曲线画圆
     */
    private val mMagicNumber = 0.55228475f

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint!!.color = resources.getColor(R.color.triangle)
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        setBackgroundColor(resources.getColor(R.color.transparent))
        mTriangleColor = resources.getColor(R.color.triangle)
        mCircleColor = resources.getColor(R.color.circle)
        mRectColor = resources.getColor(R.color.triangle)
    }

    var mIsLoading = false
    private var mPaint: Paint? = null
    private var mControlX = 0f
    private var mControlY = 0f
    private var mAnimPercent = 0f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (visibility == GONE) {
            return
        }
        when (shape) {
            Shape.SHAPE_TRIANGLE -> if (mIsLoading) {
                mAnimPercent += 0.1611113f
                val color = mArgbEvaluator.evaluate(mAnimPercent, mTriangleColor, mCircleColor) as Int
                mPaint!!.color = color
                // triangle to circle
                val path = Path()
                path.moveTo(relativeXFromView(0.5f), relativeYFromView(0f))
                if (mAnimPercent >= 1) {
                    shape = Shape.SHAPE_CIRCLE
                    mIsLoading = false
                    mAnimPercent = 1f
                }
                val controlX = mControlX - relativeXFromView(mAnimPercent * mTriangle2Circle) * genhao3
                val controlY = mControlY - relativeYFromView(mAnimPercent * mTriangle2Circle)
                path.quadTo(relativeXFromView(1f) - controlX, controlY, relativeXFromView(0.5f + genhao3 / 4), relativeYFromView(0.75f))
                path.quadTo(relativeXFromView(0.5f), relativeYFromView(0.75f + 2 * mAnimPercent * mTriangle2Circle), relativeXFromView(0.5f - genhao3 / 4), relativeYFromView(0.75f))
                path.quadTo(controlX, controlY, relativeXFromView(0.5f), relativeYFromView(0f))
                path.close()
                canvas.drawPath(path, mPaint!!)
                invalidate()
            } else {
                val path = Path()
                mPaint!!.color = resources.getColor(R.color.triangle)
                path.moveTo(relativeXFromView(0.5f), relativeYFromView(0f))
                path.lineTo(relativeXFromView(1f), relativeYFromView(genhao3 / 2f))
                path.lineTo(relativeXFromView(0f), relativeYFromView(genhao3 / 2f))
                mControlX = relativeXFromView(0.5f - genhao3 / 8.0f)
                mControlY = relativeYFromView(3 / 8.0f)
                mAnimPercent = 0f
                path.close()
                canvas.drawPath(path, mPaint!!)
            }
            Shape.SHAPE_CIRCLE -> if (mIsLoading) {
                val magicNumber = mMagicNumber + mAnimPercent
                mAnimPercent += 0.12f
                if (magicNumber + mAnimPercent >= 1.9f) {
                    shape = Shape.SHAPE_RECT
                    mIsLoading = false
                }
                val color = mArgbEvaluator.evaluate(mAnimPercent, mCircleColor, mRectColor) as Int
                mPaint!!.color = color
                val path = Path()
                path.moveTo(relativeXFromView(0.5f), relativeYFromView(0f))
                path.cubicTo(relativeXFromView(0.5f + magicNumber / 2), relativeYFromView(0f),
                        relativeXFromView(1f), relativeYFromView(0.5f - magicNumber / 2),
                        relativeXFromView(1f), relativeYFromView(0.5f))
                path.cubicTo(
                        relativeXFromView(1f), relativeXFromView(0.5f + magicNumber / 2),
                        relativeXFromView(0.5f + magicNumber / 2), relativeYFromView(1f),
                        relativeXFromView(0.5f), relativeYFromView(1f))
                path.cubicTo(relativeXFromView(0.5f - magicNumber / 2), relativeXFromView(1f),
                        relativeXFromView(0f), relativeYFromView(0.5f + magicNumber / 2),
                        relativeXFromView(0f), relativeYFromView(0.5f))
                path.cubicTo(relativeXFromView(0f), relativeXFromView(0.5f - magicNumber / 2),
                        relativeXFromView(0.5f - magicNumber / 2), relativeYFromView(0f),
                        relativeXFromView(0.5f), relativeYFromView(0f))
                path.close()
                canvas.drawPath(path, mPaint!!)
                invalidate()
            } else {
                mPaint!!.color = resources.getColor(R.color.circle)
                val path = Path()
                val magicNumber = mMagicNumber
                path.moveTo(relativeXFromView(0.5f), relativeYFromView(0f))
                path.cubicTo(relativeXFromView(0.5f + magicNumber / 2), 0f,
                        relativeXFromView(1f), relativeYFromView(magicNumber / 2),
                        relativeXFromView(1f), relativeYFromView(0.5f))
                path.cubicTo(
                        relativeXFromView(1f), relativeXFromView(0.5f + magicNumber / 2),
                        relativeXFromView(0.5f + magicNumber / 2), relativeYFromView(1f),
                        relativeXFromView(0.5f), relativeYFromView(1f))
                path.cubicTo(relativeXFromView(0.5f - magicNumber / 2), relativeXFromView(1f),
                        relativeXFromView(0f), relativeYFromView(0.5f + magicNumber / 2),
                        relativeXFromView(0f), relativeYFromView(0.5f))
                path.cubicTo(relativeXFromView(0f), relativeXFromView(0.5f - magicNumber / 2),
                        relativeXFromView(0.5f - magicNumber / 2), relativeYFromView(0f),
                        relativeXFromView(0.5f), relativeYFromView(0f))
                mAnimPercent = 0f
                path.close()
                canvas.drawPath(path, mPaint!!)
            }
            Shape.SHAPE_RECT -> if (mIsLoading) {
                mAnimPercent += 0.15f
                if (mAnimPercent >= 1) {
                    shape = Shape.SHAPE_TRIANGLE
                    mIsLoading = false
                    mAnimPercent = 1f
                }
                val color = mArgbEvaluator.evaluate(mAnimPercent, mRectColor, mTriangleColor) as Int
                mPaint!!.color = color
                val path = Path()
                path.moveTo(relativeXFromView(0.5f * mAnimPercent), 0f)
                path.lineTo(relativeYFromView(1 - 0.5f * mAnimPercent), 0f)
                val distanceX = mControlX * mAnimPercent
                val distanceY = (relativeYFromView(1f) - mControlY) * mAnimPercent
                path.lineTo(relativeXFromView(1f) - distanceX, relativeYFromView(1f) - distanceY)
                path.lineTo(relativeXFromView(0f) + distanceX, relativeYFromView(1f) - distanceY)
                path.close()
                canvas.drawPath(path, mPaint!!)
                invalidate()
            } else {
                mPaint!!.color = resources.getColor(R.color.rect)
                mControlX = relativeXFromView(0.5f - genhao3 / 4)
                mControlY = relativeYFromView(0.75f)
                val path = Path()
                path.moveTo(relativeXFromView(0f), relativeYFromView(0f))
                path.lineTo(relativeXFromView(1f), relativeYFromView(0f))
                path.lineTo(relativeXFromView(1f), relativeYFromView(1f))
                path.lineTo(relativeXFromView(0f), relativeYFromView(1f))
                path.close()
                mAnimPercent = 0f
                canvas.drawPath(path, mPaint!!)
            }
            else -> {
            }
        }
    }

    private fun relativeXFromView(percent: Float): Float {
        return width * percent
    }

    private fun relativeYFromView(percent: Float): Float {
        return height * percent
    }

    fun changeShape() {
        mIsLoading = true
        invalidate()
    }

    enum class Shape {
        SHAPE_TRIANGLE, SHAPE_RECT, SHAPE_CIRCLE
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == VISIBLE) {
            invalidate()
        }
    }

    companion object {
        private const val genhao3 = 1.7320508075689f
        private const val mTriangle2Circle = 0.25555555f
    }
}