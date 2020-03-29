package com.tamsiree.rxui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import com.tamsiree.rxkit.RxImageTool.changeColorAlpha
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxkit.model.ModelSpider
import com.tamsiree.rxui.R
import java.util.*

/**
 * @author tamsiree
 * @date 16/9/25
 */
class RxCobwebView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(mContext, attrs, defStyleAttr) {
    //中心点
    private var center = 0

    //外层菱形圆半径
    private var one_radius = 0f

    //多边形之间的间距
    private val distance = 0f

    //字体矩形
    private var str_rect: Rect? = null

    //层级颜色 集合
    private var levelPaintList: MutableList<Paint>? = null

    //默认大小
    private var defalutSize = 300
    private lateinit var mSpiderNames: Array<String>

    //等级列表
    private lateinit var mSpiderLevels: FloatArray

    //各等级进度画笔
    private var rank_Paint: Paint? = null

    //字体画笔
    private var mSpiderNamePaint: Paint? = null

    //中心线画笔
    private var center_paint: Paint? = null

    //最大等级
    private var mSpiderMaxLevel = 0

    //蜘蛛数量
    private var mSpiderNumber = 0
    private var mSpiderList: MutableList<ModelSpider> = ArrayList()

    //蛛网内部填充颜色
    private var mSpiderColor = 0

    //蛛网半径颜色
    private var mSpiderRadiusColor = 0

    //蛛网等级填充的颜色
    private var mSpiderLevelColor = 0

    //蛛网等级描边的颜色
    private var mSpiderLevelStrokeColor = 0

    //是否使用蛛网等级的描边
    private var mSpiderLevelStroke = false

    //蛛网等级描边的宽度
    private var mSpiderLevelStrokeWidth = 0f

    //是否支持手势旋转
    private var mSpiderRotate = false
    private var mSpiderNameSize = 0
    private var mDetector: GestureDetector? = null
    private var mScroller: Scroller? = null
    private var mFlingPoint = 0f
    private var mRotateOrientation = 0.0
    private var mPerimeter = 0.0
    private var mRotateAngle = 0.0
    private var mPointCenter: PointF? = null
    private fun initEvent() {
        defalutSize = dp2px(mContext, defalutSize.toFloat())
        mSpiderNames = arrayOf("金钱", "能力", "美貌", "智慧", "交际", "口才")
        mSpiderLevels = floatArrayOf(1f, 1f, 1f, 1f, 1f, 1f)
        mSpiderList.clear()
        for (position in mSpiderNames.indices) {
            mSpiderList.add(ModelSpider(mSpiderNames[position], mSpiderLevels[position]))
        }
        mSpiderNumber = mSpiderList.size

        //初始化字体画笔
        mSpiderNamePaint = Paint()
        mSpiderNamePaint!!.isAntiAlias = true
        mSpiderNamePaint!!.color = Color.BLACK
        mSpiderNamePaint!!.textSize = mSpiderNameSize.toFloat()
        str_rect = Rect()
        mSpiderNamePaint!!.getTextBounds(mSpiderList[0].spiderName, 0, mSpiderList[0].spiderName.length, str_rect)

        //初始化各等级进度画笔
        rank_Paint = Paint()
        rank_Paint!!.isAntiAlias = true
        rank_Paint!!.color = Color.RED
        rank_Paint!!.strokeWidth = 8f
        //设置空心
        rank_Paint!!.style = Paint.Style.STROKE
        initLevelPoints()

        //初始化 蛛网半径画笔
        center_paint = Paint()
        center_paint!!.isAntiAlias = true
        center_paint!!.color = mSpiderRadiusColor
        mScroller = Scroller(mContext)
        mDetector = GestureDetector(mContext, GestureListener())
        mDetector!!.setIsLongpressEnabled(false)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (!mSpiderRotate) {
            super.onTouchEvent(event)
        } else mDetector!!.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            val x = mScroller!!.currX
            val y = mScroller!!.currY
            val max = Math.max(Math.abs(x), Math.abs(y))
            val rotateDis = RxRotateTool.CIRCLE_ANGLE * (Math.abs(max - mFlingPoint) / mPerimeter)
            var rotate = mRotateAngle
            if (mRotateOrientation > 0) {
                rotate += rotateDis
            } else if (mRotateOrientation < 0) {
                rotate -= rotateDis
            }
            handleRotate(rotate)
            mFlingPoint = max.toFloat()
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mPointCenter = PointF((w / 2).toFloat(), (h / 2).toFloat())
    }

    private fun handleRotate(rotate: Double) {
        var rotate = rotate
        rotate = RxRotateTool.getNormalizedAngle(rotate)
        mRotateAngle = rotate
        invalidate()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        //获得这个控件对应的属性。
        val a = context.obtainStyledAttributes(attrs, R.styleable.RxCobwebView)
        //蛛网内部颜色
        mSpiderColor = a.getColor(R.styleable.RxCobwebView_spiderColor, resources.getColor(R.color.teal))
        //蛛网半径颜色
        mSpiderRadiusColor = a.getColor(R.styleable.RxCobwebView_spiderRadiusColor, Color.WHITE)
        //蛛网等级描边颜色
        mSpiderLevelStrokeColor = a.getColor(R.styleable.RxCobwebView_spiderLevelColor, resources.getColor(R.color.custom_progress_orange_progress))
        //蛛网等级颜色
        mSpiderLevelColor = changeColorAlpha(mSpiderLevelStrokeColor, 255 / 2)
        //是否需要 蛛网等级描边
        mSpiderLevelStroke = a.getBoolean(R.styleable.RxCobwebView_spiderLevelStroke, true)
        //是否需要 蛛网等级描边
        mSpiderRotate = a.getBoolean(R.styleable.RxCobwebView_spiderRotate, true)
        //蛛网等级描边 宽度
        mSpiderLevelStrokeWidth = a.getFloat(R.styleable.RxCobwebView_spiderLevelStrokeWidth, 3f)
        //蛛网最大层级数
        mSpiderMaxLevel = a.getInteger(R.styleable.RxCobwebView_spiderMaxLevel, 4)
        //标题字体大小
        mSpiderNameSize = a.getDimensionPixelSize(R.styleable.RxCobwebView_spiderNameSize, dp2px(mContext, 16f))
        a.recycle()
    }

    private fun initLevelPoints() {
        levelPaintList = ArrayList()

        //初始化 N 层多边形画笔
        for (i in mSpiderMaxLevel downTo 1) {
            val paint = Paint()
            paint.isAntiAlias = true
            var scale = mSpiderMaxLevel * 10 / 11
            if (scale < 1) {
                scale = 1
            }
            paint.color = changeColorAlpha(mSpiderColor, (255 / (mSpiderMaxLevel + 1) * (mSpiderMaxLevel - i - 1) + 255 / scale) % 255)
            //设置实心
            paint.style = Paint.Style.FILL
            levelPaintList?.add(paint)
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawSpiderName(canvas)
        for (position in 0 until mSpiderMaxLevel) {
            drawCobweb(canvas, position)
        }
        drawSpiderRadiusLine(canvas)
        drawSpiderLevel(canvas)
    }

    /**
     * 绘制等级进度
     */
    private fun drawSpiderLevel(canvas: Canvas) {
        val path = Path()
        var nextAngle: Float
        var nextRadians: Float
        var nextPointX: Float
        var nextPointY: Float
        var currentRadius: Float
        val averageAngle = 360 / mSpiderNumber.toFloat()
        val offsetAngle: Float = if (averageAngle > 0 && mSpiderNumber % 2 == 0) averageAngle / 2 else 0f
        for (position in 0 until mSpiderNumber) {
            var scale = mSpiderList[position].spiderLevel / mSpiderMaxLevel
            if (scale >= 1) {
                scale = 1f
            }
            currentRadius = scale * one_radius
            nextAngle = offsetAngle + position * averageAngle
            nextRadians = Math.toRadians(nextAngle.toDouble()).toFloat()
            nextPointX = (center + Math.sin(nextRadians - mRotateAngle) * currentRadius).toFloat()
            nextPointY = (center - Math.cos(nextRadians - mRotateAngle) * currentRadius).toFloat()
            if (position == 0) {
                path.moveTo(nextPointX, nextPointY)
            } else {
                path.lineTo(nextPointX, nextPointY)
            }
        }
        val scorePaint = Paint()
        scorePaint.color = mSpiderLevelColor
        scorePaint.style = Paint.Style.FILL_AND_STROKE
        scorePaint.isAntiAlias = true
        path.close()
        canvas.drawPath(path, scorePaint)
        var scoreStrokePaint: Paint? = null

        // 绘制描边
        if (mSpiderLevelStroke) {
            if (scoreStrokePaint == null) {
                scoreStrokePaint = Paint()
                scoreStrokePaint.color = mSpiderLevelStrokeColor
                scoreStrokePaint.style = Paint.Style.STROKE
                scoreStrokePaint.isAntiAlias = true
                if (mSpiderLevelStrokeWidth > 0) {
                    scoreStrokePaint.strokeWidth = mSpiderLevelStrokeWidth
                }
            }
            canvas.drawPath(path, scoreStrokePaint)
        }
    }

    /**
     * 绘制字体
     *
     * @param canvas 画笔
     */
    private fun drawSpiderName(canvas: Canvas) {
        var nextAngle: Float
        var nextRadians: Float
        var nextPointX: Float
        var nextPointY: Float
        var currentRadius: Float
        val averageAngle = 360 / mSpiderNumber.toFloat()
        val offsetAngle: Float = if (averageAngle > 0 && mSpiderNumber % 2 == 0) averageAngle / 2 else 0f
        for (position in 0 until mSpiderNumber) {
            currentRadius = (paddingTop + str_rect!!.height()).toFloat() + one_radius
            nextAngle = offsetAngle + position * averageAngle
            nextRadians = Math.toRadians(nextAngle.toDouble()).toFloat()
            val text = mSpiderList[position].spiderName
            val textWidth = mSpiderNamePaint!!.measureText(text)
            val fontMetrics = mSpiderNamePaint!!.fontMetrics
            val textHeight = fontMetrics.descent - fontMetrics.ascent
            nextPointX = (center + Math.sin(nextRadians - mRotateAngle) * currentRadius - textWidth / 2).toFloat()
            nextPointY = (center - Math.cos(nextRadians - mRotateAngle) * currentRadius + textHeight / 4).toFloat()
            canvas.drawText(text,
                    nextPointX,
                    nextPointY,
                    mSpiderNamePaint!!)
        }
        mPerimeter = 2 * Math.PI * one_radius
    }

    /**
     * //绘制层级蛛网
     *
     * @param canvas 画笔
     */
    private fun drawCobweb(canvas: Canvas, index: Int) {
        val path = Path()
        var nextAngle: Float
        var nextRadians: Float
        var nextPointX: Float
        var nextPointY: Float
        var currentRadius: Float
        val averageAngle = 360 / mSpiderNumber.toFloat()
        val offsetAngle: Float = if (averageAngle > 0 && mSpiderNumber % 2 == 0) averageAngle / 2 else 0f
        for (position in 0 until mSpiderNumber) {
            currentRadius = (index + 1) * one_radius / mSpiderMaxLevel
            nextAngle = offsetAngle + position * averageAngle
            nextRadians = Math.toRadians(nextAngle.toDouble()).toFloat()
            nextPointX = (center + Math.sin(nextRadians - mRotateAngle) * currentRadius).toFloat()
            nextPointY = (center - Math.cos(nextRadians - mRotateAngle) * currentRadius).toFloat()
            if (position == 0) {
                path.moveTo(nextPointX, nextPointY)
            } else {
                path.lineTo(nextPointX, nextPointY)
            }
        }
        path.close()
        canvas.drawPath(path, levelPaintList!![mSpiderMaxLevel - index - 1])
        var scoreStrokePaint: Paint? = null

        // 绘制描边
        if (mSpiderLevelStroke) {
            if (scoreStrokePaint == null) {
                scoreStrokePaint = Paint()
                scoreStrokePaint.color = changeColorAlpha(levelPaintList!![mSpiderMaxLevel - 1].color, 50)
                scoreStrokePaint.style = Paint.Style.STROKE
                scoreStrokePaint.isAntiAlias = true
                if (mSpiderLevelStrokeWidth > 0) {
                    scoreStrokePaint.strokeWidth = mSpiderLevelStrokeWidth
                }
            }
            canvas.drawPath(path, scoreStrokePaint)
        }
    }

    /**
     * 绘制连接中心的线
     *
     * @param canvas Canvas
     */
    private fun drawSpiderRadiusLine(canvas: Canvas) {
        var nextAngle: Float
        var nextRadians: Float
        var nextPointX: Float
        var nextPointY: Float
        val averageAngle = 360 / mSpiderNumber.toFloat()
        val offsetAngle: Float = if (averageAngle > 0 && mSpiderNumber % 2 == 0) averageAngle / 2 else 0f
        for (position in 0 until mSpiderNumber) {
            nextAngle = offsetAngle + position * averageAngle
            nextRadians = Math.toRadians(nextAngle.toDouble()).toFloat()
            nextPointX = (center + Math.sin(nextRadians - mRotateAngle) * one_radius).toFloat()
            nextPointY = (center - Math.cos(nextRadians - mRotateAngle) * one_radius).toFloat()
            canvas.drawLine(center.toFloat(), center.toFloat(), nextPointX, nextPointY, center_paint!!)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val wSize = MeasureSpec.getSize(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        val hSize = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int
        width = if (wMode == MeasureSpec.EXACTLY) {
            wSize
        } else {
            Math.min(wSize, defalutSize)
        }
        height = if (hMode == MeasureSpec.EXACTLY) {
            hSize
        } else {
            Math.min(hSize, defalutSize)
        }
        //中心点
        center = width / 2
        one_radius = center - paddingTop - (2 * str_rect!!.height()).toFloat()
        setMeasuredDimension(width, height)
    }

    var spiderMaxLevel: Int
        get() = mSpiderMaxLevel
        set(spiderMaxLevel) {
            mSpiderMaxLevel = spiderMaxLevel
            initLevelPoints()
            invalidate()
        }

    val spiderList: List<ModelSpider>
        get() = mSpiderList

    fun setSpiderList(spiderList: MutableList<ModelSpider>) {
        mSpiderList = spiderList
        mSpiderNumber = mSpiderList.size
        invalidate()
    }

    var spiderColor: Int
        get() = mSpiderColor
        set(spiderColor) {
            mSpiderColor = spiderColor
            initLevelPoints()
            invalidate()
        }

    var spiderRadiusColor: Int
        get() = mSpiderRadiusColor
        set(spiderRadiusColor) {
            mSpiderRadiusColor = spiderRadiusColor
            invalidate()
        }

    var spiderLevelColor: Int
        get() = mSpiderLevelStrokeColor
        set(spiderLevelColor) {
            mSpiderLevelStrokeColor = spiderLevelColor
            mSpiderLevelColor = changeColorAlpha(mSpiderLevelStrokeColor, 255 / 2)
            invalidate()
        }

    var isSpiderLevelStroke: Boolean
        get() = mSpiderLevelStroke
        set(spiderLevelStroke) {
            mSpiderLevelStroke = spiderLevelStroke
            invalidate()
        }

    var spiderLevelStrokeWidth: Float
        get() = mSpiderLevelStrokeWidth
        set(spiderLevelStrokeWidth) {
            mSpiderLevelStrokeWidth = spiderLevelStrokeWidth
            invalidate()
        }

    var spiderNameSize: Int
        get() = mSpiderNameSize
        set(spiderNameSize) {
            mSpiderNameSize = spiderNameSize
            invalidate()
        }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            if (!mScroller!!.isFinished) {
                mScroller!!.forceFinished(true)
            }
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                mFlingPoint = e2.x
                mScroller!!.fling(e2.x.toInt(),
                        0,
                        velocityX.toInt(),
                        0,
                        (-mPerimeter + e2.x).toInt(),
                        (mPerimeter + e2.x).toInt(),
                        0,
                        0)
            } else if (Math.abs(velocityY) > Math.abs(velocityX)) {
                mFlingPoint = e2.y
                mScroller!!.fling(0,
                        e2.y.toInt(),
                        0,
                        velocityY.toInt(),
                        0,
                        0,
                        (-mPerimeter + e2.y).toInt(),
                        (mPerimeter + e2.y).toInt())
            }
            invalidate()
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            var rotate = mRotateAngle
            val dis = RxRotateTool.getRotateAngle(PointF(e2.x - distanceX, e2.y - distanceY)
                    , PointF(e2.x, e2.y), mPointCenter!!)
            rotate += dis
            handleRotate(rotate)
            mRotateOrientation = dis
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }

    init {
        initAttrs(attrs)
        initEvent()
    }
}