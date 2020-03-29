package com.tamsiree.rxui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.tamsiree.rxkit.RxImageTool.dip2px
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxui.R

/**
 * @author tamsiree
 * @date 2016/8/26
 * 描述：添加圆角支持 on 2016/11/11
 *
 */
class RxProgressBar @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), Runnable {
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    private val DEFAULT_HEIGHT_DP = 35
    private var borderWidth = 0
    private val maxProgress = 100f
    private var textPaint: Paint? = null
    private var bgPaint: Paint? = null
    private var pgPaint: Paint? = null
    private var progressText: String? = null
    private var textRect: Rect? = null
    private var bgRectf: RectF? = null

    /**
     * 左右来回移动的滑块
     */
    private var flikerBitmap: Bitmap? = null

    /**
     * 滑块移动最左边位置，作用是控制移动
     */
    private var flickerLeft = 0f

    /**
     * 进度条 bitmap ，包含滑块
     */
    private var pgBitmap: Bitmap? = null
    private var pgCanvas: Canvas? = null

    /**
     * 当前进度
     */
    private var progress = 0f
    private var isFinish = false
    private var isStop = false

    /**
     * 下载中颜色
     */
    private var loadingColor = 0

    /**
     * 暂停时颜色
     */
    private var pauseColor = 0

    /**
     * 完成时的眼神
     */
    private var finishColor = 0

    /**
     * 进度停下来的颜色
     */
    private val stopColor = 0

    /**
     * 进度文本、边框、进度条颜色
     */
    private var progressColor = 0
    private var textSize = 0
    private var radius = 0
    private var thread: Thread? = null
    var bitmapShader: BitmapShader? = null
    private fun initAttrs(attrs: AttributeSet?) {
        @SuppressLint("CustomViewStyleable") val ta = context.obtainStyledAttributes(attrs, R.styleable.FlikerProgressBar)
        try {
            textSize = ta.getDimension(R.styleable.FlikerProgressBar_textSize, 12f).toInt()
            loadingColor = ta.getColor(R.styleable.FlikerProgressBar_loadingColor, Color.parseColor("#40c4ff"))
            pauseColor = ta.getColor(R.styleable.FlikerProgressBar_stopColor, Color.parseColor("#ff9800"))
            finishColor = ta.getColor(R.styleable.FlikerProgressBar_finishColor, Color.parseColor("#3CB371"))
            radius = ta.getDimension(R.styleable.FlikerProgressBar_radius, 0f).toInt()
            borderWidth = ta.getDimension(R.styleable.FlikerProgressBar_borderWidth, 1f).toInt()
        } finally {
            ta.recycle()
        }
    }

    private fun init() {
        bgPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        bgPaint!!.style = Paint.Style.STROKE
        bgPaint!!.strokeWidth = borderWidth.toFloat()
        pgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pgPaint!!.style = Paint.Style.FILL
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint!!.textSize = textSize.toFloat()
        textRect = Rect()
        bgRectf = RectF(borderWidth.toFloat(), borderWidth.toFloat(), (measuredWidth - borderWidth).toFloat(), (measuredHeight - borderWidth).toFloat())
        progressColor = if (isStop) {
            pauseColor
        } else {
            loadingColor
        }
        flikerBitmap = BitmapFactory.decodeResource(resources, R.drawable.flicker)
        flickerLeft = -flikerBitmap!!.width.toFloat()
        initPgBimap()
    }

    private fun initPgBimap() {
        pgBitmap = Bitmap.createBitmap(measuredWidth - borderWidth, measuredHeight - borderWidth, Bitmap.Config.ARGB_8888)
        pgCanvas = Canvas(pgBitmap!!)
        thread = Thread(this)
        thread!!.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        var height = 0
        when (heightSpecMode) {
            MeasureSpec.AT_MOST -> height = dp2px(DEFAULT_HEIGHT_DP.toFloat())
            MeasureSpec.EXACTLY, MeasureSpec.UNSPECIFIED -> height = heightSpecSize
            else -> {
            }
        }
        setMeasuredDimension(widthSpecSize, height)
        if (pgBitmap == null) {
            init()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //背景
        drawBackGround(canvas)

        //进度
        drawProgress(canvas)

        //进度text
        drawProgressText(canvas)

        //变色处理
        drawColorProgressText(canvas)
    }

    /**
     * 边框
     *
     * @param canvas
     */
    private fun drawBackGround(canvas: Canvas) {
        bgPaint!!.color = progressColor
        //left、top、right、bottom不要贴着控件边，否则border只有一半绘制在控件内,导致圆角处线条显粗
        canvas.drawRoundRect(bgRectf!!, radius.toFloat(), radius.toFloat(), bgPaint!!)
    }

    /**
     * 进度
     */
    @SuppressLint("WrongConstant")
    private fun drawProgress(canvas: Canvas) {
        pgPaint!!.color = progressColor
        val right = progress / maxProgress * measuredWidth
        pgCanvas!!.save() //Canvas.CLIP_SAVE_FLAG
        pgCanvas!!.clipRect(0f, 0f, right, measuredHeight.toFloat())
        pgCanvas!!.drawColor(progressColor)
        pgCanvas!!.restore()
        if (!isStop) {
            pgPaint!!.xfermode = xfermode
            pgCanvas!!.drawBitmap(flikerBitmap!!, flickerLeft, 0f, pgPaint)
            pgPaint!!.xfermode = null
        }

        //控制显示区域
        bitmapShader = BitmapShader(pgBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        pgPaint!!.shader = bitmapShader
        canvas.drawRoundRect(bgRectf!!, radius.toFloat(), radius.toFloat(), pgPaint!!)
    }

    /**
     * 进度提示文本
     *
     * @param canvas
     */
    private fun drawProgressText(canvas: Canvas) {
        textPaint!!.color = progressColor
        progressText = getProgressText()
        textPaint!!.getTextBounds(progressText, 0, progressText!!.length, textRect)
        val tWidth = textRect!!.width()
        val tHeight = textRect!!.height()
        val xCoordinate = (measuredWidth - tWidth) / 2.toFloat()
        val yCoordinate = (measuredHeight + tHeight) / 2.toFloat()
        canvas.drawText(progressText!!, xCoordinate, yCoordinate, textPaint!!)
    }

    /**
     * 变色处理
     *
     * @param canvas
     */
    @SuppressLint("WrongConstant")
    private fun drawColorProgressText(canvas: Canvas) {
        textPaint!!.color = Color.WHITE
        val tWidth = textRect!!.width()
        val tHeight = textRect!!.height()
        val xCoordinate = (measuredWidth - tWidth) / 2.toFloat()
        val yCoordinate = (measuredHeight + tHeight) / 2.toFloat()
        val progressWidth = progress / maxProgress * measuredWidth
        if (progressWidth > xCoordinate) {
            canvas.save() //Canvas.CLIP_SAVE_FLAG
            val right = Math.min(progressWidth, xCoordinate + tWidth * 1.1f)
            canvas.clipRect(xCoordinate, 0f, right, measuredHeight.toFloat())
            canvas.drawText(progressText!!, xCoordinate, yCoordinate, textPaint!!)
            canvas.restore()
        }
    }

    fun setProgress(progress: Float) {
        if (!isStop) {
            if (progress < maxProgress) {
                this.progress = progress
            } else {
                this.progress = maxProgress
                finishLoad()
            }
            invalidate()
        }
    }

    private fun setFinish(stop: Boolean) {
        isStop = stop
        if (isStop) {
            progressColor = finishColor
            thread!!.interrupt()
        } else {
            progressColor = loadingColor
            thread = Thread(this)
            thread!!.start()
        }
        invalidate()
    }

    fun setStop(stop: Boolean) {
        isStop = stop
        if (isStop) {
            progressColor = pauseColor
            thread!!.interrupt()
        } else {
            progressColor = loadingColor
            thread = Thread(this)
            thread!!.start()
        }
        invalidate()
    }

    fun finishLoad() {
        isFinish = true
        setFinish(true)
    }

    fun toggle() {
        if (!isFinish) {
            if (isStop) {
                setStop(false)
            } else {
                setStop(true)
            }
        }
    }

    override fun run() {
        val width = flikerBitmap!!.width
        try {
            while (!isStop && !thread!!.isInterrupted) {
                flickerLeft += dip2px(5f).toFloat()
                val progressWidth = progress / maxProgress * measuredWidth
                if (flickerLeft >= progressWidth) {
                    flickerLeft = -width.toFloat()
                }
                postInvalidate()
                Thread.sleep(20)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * 重置
     */
    fun reset() {
        setStop(true)
        progress = 0f
        isFinish = false
        isStop = false
        progressColor = loadingColor
        progressText = ""
        flickerLeft = -flikerBitmap!!.width.toFloat()
        initPgBimap()
    }

    fun getProgress(): Float {
        return progress
    }

    fun isStop(): Boolean {
        return isStop
    }

    fun isFinish(): Boolean {
        return isFinish
    }

    private fun getProgressText(): String {
        val text: String
        text = if (!isFinish) {
            if (!isStop) {
                "下载中$progress%"
            } else {
                "继续"
            }
        } else {
            "下载完成"
        }
        return text
    }

    init {
        initAttrs(attrs)
    }
}