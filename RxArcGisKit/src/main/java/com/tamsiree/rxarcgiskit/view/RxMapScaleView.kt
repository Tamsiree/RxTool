package com.tamsiree.rxarcgiskit.view

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.esri.arcgisruntime.mapping.view.MapView
import com.tamsiree.rxarcgiskit.R

/**
 * Created by Tamsiree on 2017/11/5.
 */
class RxMapScaleView : View {
    private var mContext: Context
    private var scaleWidth = 0
    private var scaleHeight = 0
    private var textColor = 0
    private var mapView: MapView? = null
    private var text: String? = null
    private var textSize = 0
    private var scaleSpaceText = 0
    private var mPaint: Paint? = null
    fun setScaleWidth(scaleWidth: Int) {
        this.scaleWidth = scaleWidth
    }

    fun setText(text: String?) {
        this.text = text
    }

    fun setMapView(mapView: MapView?) {
        this.mapView = mapView
    }

    constructor(context: Context) : super(context) {
        this.mContext = context
        initVariables()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.mContext = context
        initVariables()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.mContext = context
        initVariables()
    }

    private fun initVariables() {
        scaleWidth = 104 //
        scaleHeight = 8 //比比例尺宽度例尺高度
        textColor = Color.BLACK //比例尺字体颜色
        text = "20公里" //比例尺文本
        textSize = 18 //比例尺宽度
        scaleSpaceText = 8 //比例尺文本与图形的间隔高度
        mPaint = Paint() //画笔
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = getWidthSize(widthMeasureSpec)
        val heightSize = getHeightSize(heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
    }

    /**
     * 测量ScaleView的高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private fun getHeightSize(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        var height = 0
        when (mode) {
            MeasureSpec.AT_MOST -> height = textSize + scaleSpaceText + scaleHeight
            MeasureSpec.EXACTLY -> {
                height = MeasureSpec.getSize(heightMeasureSpec)
            }
            MeasureSpec.UNSPECIFIED -> {
                height = Math.max(textSize + scaleSpaceText + scaleHeight, MeasureSpec.getSize(heightMeasureSpec))
            }
            else -> {
            }
        }
        return height
    }

    /**
     * 测量ScaleView的宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private fun getWidthSize(widthMeasureSpec: Int): Int {
        return MeasureSpec.getSize(widthMeasureSpec)
    }

    /**
     * 绘制上面的文字和下面的比例尺，因为比例尺是.9.png，
     * 我们需要利用drawNinepath方法绘制比例尺
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = scaleWidth
        mPaint!!.color = textColor
        mPaint!!.isAntiAlias = true
        mPaint!!.textSize = textSize.toFloat()
        mPaint!!.typeface = Typeface.DEFAULT_BOLD
        val textWidth = mPaint!!.measureText(text)
        canvas.drawText(text!!, (width - textWidth) / 2, textSize.toFloat(), mPaint!!)
        val scaleRect = Rect(0, textSize + scaleSpaceText, width, textSize + scaleSpaceText + scaleHeight)
        drawNinepath(canvas, R.drawable.plotting_scale_new, scaleRect)
    }

    // 绘制.9.PNG图片：
    private fun drawNinepath(canvas: Canvas, resId: Int, rect: Rect) {
        val bmp = BitmapFactory.decodeResource(resources, resId)
        val patch = NinePatch(bmp, bmp.ninePatchChunk, null)
        patch.draw(canvas, rect)
    }

    /**
     * 根据缩放级别更新ScaleView的文字以及比例尺的长度
     */
    fun refreshScaleView() {
        if (mapView == null) {
            throw NullPointerException("you can call setMapView(MapView mapView) at first")
        }
        //结果单位米，表示图上1厘米代表*米
        val scale = mapView!!.mapScale / 100
        // ppi为每英尺像素数，
        // ppi/2.54为1厘米的像素数
        val ppi = pPIOfDevice
        if (scale > 0 && scale <= 5) { //换算5米
            val unit = "5米"
            val scaleWidth = (5 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 5 && scale <= 10) { //换算10米
            val unit = "10米"
            val scaleWidth = (10 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 10 && scale <= 20) { //换算20米
            val unit = "20米"
            val scaleWidth = (20 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 20 && scale <= 50) { //换算50米
            val unit = "50米"
            val scaleWidth = (50 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 50 && scale <= 100) { //换算100米
            val unit = "100米"
            val scaleWidth = (100 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 100 && scale <= 200) { //换算200米
            val unit = "200米"
            val scaleWidth = (200 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 200 && scale <= 500) { //换算500米
            val unit = "500米"
            val scaleWidth = (500 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 500 && scale <= 1000) { //换算1公里
            val unit = "1公里"
            val scaleWidth = (1000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 1000 && scale <= 2000) { //换算2公里
            val unit = "2公里"
            val scaleWidth = (2000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 2000 && scale <= 5000) { //换算5公里
            val unit = "5公里"
            val scaleWidth = (5000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 5000 && scale <= 10000) { //换算10公里
            val unit = "10公里"
            val scaleWidth = (10000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 10000 && scale <= 20000) { //换算20公里
            val unit = "20公里"
            val scaleWidth = (20000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 20000 && scale <= 25000) { //换算25公里
            val unit = "25公里"
            val scaleWidth = (25000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 25000 && scale <= 50000) { //换算50公里
            val unit = "50公里"
            val scaleWidth = (50000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 50000 && scale <= 100000) { //换算100公里
            val unit = "100公里"
            val scaleWidth = (100000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 100000 && scale <= 200000) { //换算200公里
            val unit = "200公里"
            val scaleWidth = (200000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 200000 && scale <= 250000) { //换算250公里
            val unit = "250公里"
            val scaleWidth = (250000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 250000 && scale <= 500000) { //换算500公里
            val unit = "500公里"
            val scaleWidth = (500000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        } else if (scale > 500000 && scale <= 1000000) { //换算1000公里
            val unit = "1000公里"
            val scaleWidth = (1000000 * ppi / 2.54 / scale).toInt()
            reInitSaleView(unit, scaleWidth)
        }
        invalidate()
    }

    private fun reInitSaleView(unit: String, scaleWidth: Int) {
        //更新文字
        setText(unit)
        //更新比例尺长度
        setScaleWidth(scaleWidth)
    }//

    //获取屏幕的真实分辨率
    private val pPIOfDevice: Double
        private get() {
            val point = Point()
            val activity = mContext as Activity
            activity.windowManager.defaultDisplay.getRealSize(point) //获取屏幕的真实分辨率
            val dm = resources.displayMetrics
            val x = Math.pow(point.x / dm.xdpi.toDouble(), 2.0) //
            val y = Math.pow(point.y / dm.ydpi.toDouble(), 2.0)
            val screenInches = Math.sqrt(x + y)
            return Math.sqrt(Math.pow(point.x.toDouble(), 2.0) + Math.pow(point.y.toDouble(), 2.0)) / screenInches
        }
}