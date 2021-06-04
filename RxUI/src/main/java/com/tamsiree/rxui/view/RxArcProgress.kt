package com.tamsiree.rxui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.tamsiree.rxkit.RxDataTool.Companion.format2Decimals
import com.tamsiree.rxui.R
import kotlin.math.sqrt

/**
 * 弧形进度条
 *
 * @author Tamsiree
 * @date 2015/12/03
 */
class RxArcProgress @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {
    /**
     * 画笔对象的引用
     */
    private val paint: Paint = Paint()
    private val textPaint: Paint = Paint()
    private val moneyPaint: Paint = Paint()
    private val moneyDPaint: Paint = Paint()

    /**
     * 圆环的颜色
     */
    var cricleColor: Int

    /**
     * 圆环进度的颜色
     */
    var cricleProgressColor: Int

    /**
     * 中间进度百分比的字符串的颜色
     */
    var textColor: Int

    /**
     * 中间进度百分比的字符串的字体
     */
    var textSize: Float

    /**
     * 圆环的宽度
     */
    var roundWidth: Float

    /**
     * 最大进度
     */
    private var max: Double

    /**
     * 当前进度
     */
    private var progress = 0.0

    /**
     * 是否显示中间的进度
     */
    private val textIsDisplayable: Boolean

    /**
     * 进度的风格，实心或者空心
     */
    private val style: Int
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /*
         * 画最外层的大圆环
         */
        //获取圆心的x坐标
        val centre = width / 2 - 90
        //圆环的半径
        val radius = (centre - roundWidth / 2).toInt()
        //用于定义的圆弧的形状和大小的界限
        @SuppressLint("DrawAllocation") val oval = RectF((centre - radius + 90).toFloat(), (centre - radius + 90).toFloat(), (centre + radius + 90).toFloat(), (centre + radius + 90).toFloat())
        //设置圆环的颜色
        paint.color = cricleColor
        //设置空心
        paint.style = Paint.Style.STROKE
        //设置圆环的宽度
        paint.strokeWidth = roundWidth
        //消除锯齿
        paint.isAntiAlias = true
        //设置边缘为圆角
        paint.strokeCap = Paint.Cap.ROUND
        //		canvas.drawRect(0, 0, getWidth(), getWidth(), paint);// 正方形
        textPaint.color = cricleColor
        textPaint.isAntiAlias = true
        textPaint.textSize = 36f
        moneyPaint.color = cricleColor
        moneyPaint.isAntiAlias = true
        moneyPaint.textSize = 65f
        moneyDPaint.color = cricleColor
        moneyDPaint.isAntiAlias = true
        moneyDPaint.textSize = 48f
        //左边最小值
        val v = 2 * radius - Math.sqrt(2.0) * (radius / 4f) + 130
        canvas.drawText("0元", (radius - sqrt(2.0) * (radius / 2) + 10).toFloat(), v.toFloat(), textPaint)
        //右边最大值
        canvas.drawText(getMax().toString() + "元", (radius + Math.sqrt(2.0) * (radius / 2) + 138).toFloat(), v.toFloat(), textPaint)
        /*if(progress<50){
			double money = progress*1+(Math.floor(Math.random()*getMax()));
			canvas.drawText(money+"", (centre+90) - moneyPaint.measureText(money+"")/2-15, centre+165, moneyPaint);//右边最大值
		}else{*/
        //右边最大值
        canvas.drawText(format2Decimals(getProgress().toString() + ""), centre + 90 - moneyPaint.measureText(format2Decimals(getProgress().toString() + "")) / 2 - 15, centre + 105.toFloat(), moneyPaint)
        //}
        //右边最大值
        canvas.drawText("元", centre + 90 + moneyPaint.measureText(format2Decimals(getProgress().toString() + "")) / 2 - 10, centre + 105.toFloat(), moneyDPaint)
        //根据进度画圆弧
        canvas.drawArc(oval, 135f, 270f, false, paint)
        /**
         * 画进度百分比
         */
        paint.strokeWidth = 0f
        paint.color = textColor
        paint.textSize = textSize
        //设置字体
        paint.typeface = Typeface.DEFAULT_BOLD
        //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        val percent = (progress.toFloat() / max.toFloat() * 100).toInt()
        //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        val textWidth = paint.measureText("$percent%")
        if (textIsDisplayable && percent != 0 && style == STROKE) {
            //canvas.drawText(percent + "%", centre+90 - textWidth / 2, centre + 90 + textSize/2, paint); //画出进度百分比
        }
        /**
         * 画圆弧 ，画圆环的进度
         */
        //设置进度是实心还是空心
        paint.strokeWidth = roundWidth //设置圆环的宽度
        paint.color = cricleProgressColor //设置进度的颜色
        when (style) {
            STROKE -> {
                paint.style = Paint.Style.STROKE
                if (progress >= 0) {
                    canvas.drawArc(oval, 135f, 270 * (progress.toFloat() / max.toFloat()), false, paint) //根据进度画圆弧
                }
            }
            else -> {
            }
        }
    }

    @Synchronized
    fun getMax(): Double {
        return max
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    @Synchronized
    fun setMax(max: Double) {
        require(max >= 0) { "max not less than 0" }
        this.max = max
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    @Synchronized
    fun getProgress(): Double {
        return progress
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    @Synchronized
    fun setProgress(progress: Double) {
        var progress = progress
        if (progress < 0) {
            this.progress = progress
            //throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max
        }
        if (progress <= max) {
            this.progress = progress
            postInvalidate()
        }
    }

    companion object {
        const val STROKE = 0
    }

    init {

        //进度条画笔
        //文字画笔
        //文字画笔
        //文字画笔
        val mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RxArcProgress)

        //获取自定义属性和默认值
        cricleColor = mTypedArray.getColor(R.styleable.RxArcProgress_roundColor, Color.WHITE)
        cricleProgressColor = mTypedArray.getColor(R.styleable.RxArcProgress_roundProgressColor, Color.parseColor("#F6B141"))
        textColor = mTypedArray.getColor(R.styleable.RxArcProgress_textColor, Color.GREEN)
        textSize = mTypedArray.getDimension(R.styleable.RxArcProgress_textSize1, 15f)
        roundWidth = mTypedArray.getDimension(R.styleable.RxArcProgress_roundWidth, 20f)
        max = mTypedArray.getInteger(R.styleable.RxArcProgress_max, 100).toDouble()
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RxArcProgress_textIsDisplayable, true)
        style = mTypedArray.getInt(R.styleable.RxArcProgress_style_ui, 0)
        mTypedArray.recycle()
    }
}