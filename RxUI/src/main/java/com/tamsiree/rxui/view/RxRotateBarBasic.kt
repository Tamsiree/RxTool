package com.tamsiree.rxui.view

import android.graphics.*
import android.text.TextPaint
import java.util.*

/**
 * @author Tamsiree
 * @date 12/24/15
 */
class RxRotateBarBasic(var rate: Int, var title: String) {
    private var isSingle = false
    private var isShowTitle = true
    private var outlineWidth = 0
    private var ratingBarWidth = 0
    private var shadowWidth = 0
    private var textWidth = 0
    private var mStartAngle = 0f
    private var mSweepAngle = 0f
    private var maxRate = 10
    private var rates: ArrayList<Rate>? = null
    private val ratedPaint: Paint = Paint()
    private val unRatedPaint: Paint = Paint()
    private val shadowPaint: Paint = Paint()
    private val outlinePaint: Paint = Paint()
    private val titlePaint: TextPaint = TextPaint()
    private var mRadius = 0
    private var mCenterX = 0
    private var mCenterY = 0
    private var outlineOval: RectF? = null
    private var ratingOval: RectF? = null
    private var shadowOval: RectF? = null
    fun init() {
        // in this order to init
        initRatingBar()
        initOval()
        initPaint()
    }

    private fun initOval() {
        mRadius = if (mCenterX > mCenterY) mCenterY else mCenterX

        // text bar : 1/10 of radius
        textWidth = mRadius / 10
        // rating bar : 1/10 of radius
        ratingBarWidth = mRadius / 10
        // shadow : 1/3 of rating bar
        shadowWidth = ratingBarWidth / 3
        // outline : 1/3 of shadow
        outlineWidth = shadowWidth / 3

        // outline include text and outline, radius is base on textWidth
        val outlineRadius = mRadius - textWidth / 2

        // padding space draw nothing, radius is base on textWidth
        val paddingRadius = outlineRadius - textWidth / 2
        val ratingBarRadius = paddingRadius - textWidth / 2 - ratingBarWidth / 2
        val shadowRadius = ratingBarRadius - ratingBarWidth / 2 - shadowWidth / 2
        outlineOval = RectF()
        ratingOval = RectF()
        shadowOval = RectF()
        outlineOval!!.left = mCenterX - outlineRadius.toFloat()
        outlineOval!!.top = mCenterY - outlineRadius.toFloat()
        outlineOval!!.right = mCenterX + outlineRadius.toFloat()
        outlineOval!!.bottom = mCenterY + outlineRadius.toFloat()
        ratingOval!!.left = mCenterX - ratingBarRadius.toFloat()
        ratingOval!!.top = mCenterY - ratingBarRadius.toFloat()
        ratingOval!!.right = mCenterX + ratingBarRadius.toFloat()
        ratingOval!!.bottom = mCenterY + ratingBarRadius.toFloat()
        shadowOval!!.left = mCenterX - shadowRadius.toFloat()
        shadowOval!!.top = mCenterY - shadowRadius.toFloat()
        shadowOval!!.right = mCenterX + shadowRadius.toFloat()
        shadowOval!!.bottom = mCenterY + shadowRadius.toFloat()
    }

    private fun initPaint() {
        outlinePaint.isAntiAlias = true
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.strokeWidth = outlineWidth.toFloat()
        outlinePaint.alpha = OUTLINE_ALPHA
        ratedPaint.isAntiAlias = true
        ratedPaint.style = Paint.Style.STROKE
        ratedPaint.strokeWidth = ratingBarWidth.toFloat()
        ratedPaint.alpha = RATING_ALPHA
        unRatedPaint.isAntiAlias = true
        unRatedPaint.style = Paint.Style.STROKE
        unRatedPaint.strokeWidth = ratingBarWidth.toFloat()
        unRatedPaint.alpha = UN_RATING_ALPHA
        shadowPaint.isAntiAlias = true
        shadowPaint.style = Paint.Style.STROKE
        shadowPaint.strokeWidth = shadowWidth.toFloat()
        shadowPaint.alpha = SHADOW_ALPHA
        titlePaint.isAntiAlias = true
        titlePaint.textSize = textWidth.toFloat()
        titlePaint.alpha = RATING_ALPHA
    }

    private fun initRatingBar() {
        rates = ArrayList()
        val itemSweepAngle: Float
        itemSweepAngle = if (isSingle) {
            (mSweepAngle - ITEM_OFFSET * maxRate) / maxRate
        } else {
            (mSweepAngle - ITEM_OFFSET * (maxRate - 1)) / maxRate
        }
        for (i in 0 until maxRate) {
            val itemStartAngle = mStartAngle + i * (itemSweepAngle + ITEM_OFFSET)
            rates!!.add(Rate(itemStartAngle, itemSweepAngle))
        }
    }

    fun drawUnRate(canvas: Canvas) {
        for (arc in rates!!) {
            arc.drawArc(canvas, ratingOval, unRatedPaint)
        }
    }

    fun drawRate(canvas: Canvas, index: Int) {
        if (index >= maxRate) {
            return
        }
        val arc = rates!![index]
        arc.drawArc(canvas, ratingOval, ratedPaint)
    }

    fun drawShadow(canvas: Canvas) {
        for (arc in rates!!) {
            arc.drawArc(canvas, shadowOval, shadowPaint)
        }
    }

    fun drawTitle(canvas: Canvas, alpha: Int) {
        if (alpha > 0 && isShowTitle) {
            val path = Path()
            val circumference = (Math.PI * (outlineOval!!.right - outlineOval!!.left)).toFloat()
            val textAngle = 360 / circumference * titlePaint.measureText(title)
            val startAngle = mStartAngle + mSweepAngle / 2 - textAngle / 2
            if (isSingle) {
                // when single, draw 360 the path will be a circle
                path.addArc(outlineOval, startAngle - mSweepAngle / 2, mSweepAngle / 2)
            } else {
                path.addArc(outlineOval, startAngle, mSweepAngle)
            }
            titlePaint.alpha = alpha
            canvas.drawTextOnPath(title, path, 0f, textWidth / 3.toFloat(), titlePaint)
        }
    }

    fun drawOutLine(canvas: Canvas) {
        val circumference = (Math.PI * (outlineOval!!.right - outlineOval!!.left)).toFloat()
        val textAngle = 360 / circumference * titlePaint.measureText(title)
        val sweepAngle = (mSweepAngle - textAngle - 1 - 1) / 2
        if (isShowTitle) {
            // text left
            val leftStartAngle = mStartAngle
            canvas.drawArc(outlineOval!!, leftStartAngle, sweepAngle, false, outlinePaint)
            // text right
            val rightStartAngle = mStartAngle + mSweepAngle - sweepAngle
            canvas.drawArc(outlineOval!!, rightStartAngle, sweepAngle, false, outlinePaint)
        } else {
            canvas.drawArc(outlineOval!!, mStartAngle, mSweepAngle, false, outlinePaint)
        }
    }

    fun setMaxRate(maxRate: Int) {
        this.maxRate = maxRate
    }

    fun setStartAngle(mStartAngle: Float) {
        this.mStartAngle = mStartAngle
    }

    fun setSweepAngle(mSweepAngle: Float) {
        this.mSweepAngle = mSweepAngle
    }

    fun setCenterX(mCenterX: Int) {
        this.mCenterX = mCenterX
    }

    fun setCenterY(mCenterY: Int) {
        this.mCenterY = mCenterY
    }

    fun setIsSingle(isSingle: Boolean) {
        this.isSingle = isSingle
    }

    fun setRatedColor(color: Int) {
        ratedPaint.color = color
    }

    fun setUnRatedColor(color: Int) {
        unRatedPaint.color = color
    }

    fun setTitleColor(color: Int) {
        titlePaint.color = color
    }

    fun setOutlineColor(color: Int) {
        outlinePaint.color = color
    }

    fun setShadowColor(color: Int) {
        shadowPaint.color = color
    }

    fun isShowTitle(isShow: Boolean) {
        isShowTitle = isShow
    }

    fun setRatingBarColor(color: Int) {
        ratedPaint.color = color
        unRatedPaint.color = color
        titlePaint.color = color
        outlinePaint.color = color
        shadowPaint.color = color
    }

    /**
     * Rate class
     */
    inner class Rate(private val startAngle: Float, private val sweepAngle: Float) {
        fun drawArc(canvas: Canvas, oval: RectF?, paint: Paint?) {
            canvas.drawArc(oval!!, startAngle, sweepAngle, false, paint!!)
        }

    }

    companion object {
        private const val ITEM_OFFSET = 1
        private const val SHADOW_ALPHA = (255 * 0.2).toInt() // 20%
        private const val UN_RATING_ALPHA = (255 * 0.3).toInt() // 30%
        private const val RATING_ALPHA = (255 * 1.0).toInt() // 100%
        private const val OUTLINE_ALPHA = (255 * 0.4).toInt() // 40%
    }

    init {
        // set default color
        setRatingBarColor(Color.WHITE)
    }
}