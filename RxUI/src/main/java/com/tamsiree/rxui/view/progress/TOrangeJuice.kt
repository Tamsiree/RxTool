package com.tamsiree.rxui.view.progress

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxui.R
import java.util.*

class TOrangeJuice(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val mLeftMargin: Int
    private val mRightMargin: Int

    /**
     * 获取中等振幅
     */
    /**
     * 设置中等振幅
     *
     * @param amplitude
     */
    // 中等振幅大小
    var middleAmplitude = MIDDLE_AMPLITUDE

    /**
     * 获取振幅差
     */
    /**
     * 设置振幅差
     *
     * @param disparity
     */
    // 振幅差
    var mplitudeDisparity = AMPLITUDE_DISPARITY

    // 叶子飘动一个周期所花的时间
    private var mLeafFloatTime = LEAF_FLOAT_TIME

    // 叶子旋转一周需要的时间
    private var mLeafRotateTime = LEAF_ROTATE_TIME
    private val mResources: Resources
    private var mLeafBitmap: Bitmap? = null
    private var mLeafWidth = 0
    private var mLeafHeight = 0
    private var mOuterBitmap: Bitmap? = null
    private var mOuterSrcRect: Rect? = null
    private var mOuterDestRect: Rect? = null
    private var mOuterWidth = 0
    private var mOuterHeight = 0
    private var mTotalWidth = 0
    private var mTotalHeight = 0
    private var mBitmapPaint: Paint? = null
    private var mWhitePaint: Paint? = null
    private var mOrangePaint: Paint? = null
    private var mWhiteRectF: RectF? = null
    private var mOrangeRectF: RectF? = null
    private var mArcRectF: RectF? = null

    // 当前进度
    private var mProgress = 0

    // 所绘制的进度条部分的宽度
    private var mProgressWidth = 0

    // 当前所在的绘制的进度条的位置
    private var mCurrentProgressPosition = 0

    // 弧形的半径
    private var mArcRadius = 0

    // arc的右上角的x坐标，也是矩形x坐标的起始点
    private var mArcRightLocation = 0

    // 用于产生叶子信息
    private val mLeafFactory: LeafFactory

    // 产生出的叶子信息
    private val mLeafInfos: List<Leaf>

    // 用于控制随机增加的时间不抱团
    private var mAddTime = 0
    private fun initPaint() {
        mBitmapPaint = Paint()
        mBitmapPaint!!.isAntiAlias = true
        mBitmapPaint!!.isDither = true
        mBitmapPaint!!.isFilterBitmap = true
        mWhitePaint = Paint()
        mWhitePaint!!.isAntiAlias = true
        mWhitePaint!!.color = WHITE_COLOR
        mOrangePaint = Paint()
        mOrangePaint!!.isAntiAlias = true
        mOrangePaint!!.color = ORANGE_COLOR
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制进度条和叶子
        // 之所以把叶子放在进度条里绘制，主要是层级原因
        drawProgressAndLeafs(canvas)
        // drawLeafs(canvas);
        canvas.drawBitmap(mOuterBitmap!!, mOuterSrcRect, mOuterDestRect!!, mBitmapPaint)
        postInvalidate()
    }

    private fun drawProgressAndLeafs(canvas: Canvas) {
        if (mProgress >= TOTAL_PROGRESS) {
            mProgress = 0
        }
        // mProgressWidth为进度条的宽度，根据当前进度算出进度条的位置
        mCurrentProgressPosition = mProgressWidth * mProgress / TOTAL_PROGRESS
        // 即当前位置在图中所示1范围内
        if (mCurrentProgressPosition < mArcRadius) {
//            RxLogTool.i(TAG, "mProgress = " + mProgress + "---mCurrentProgressPosition = " + mCurrentProgressPosition + "--mArcProgressWidth" + mArcRadius);
            // 1.绘制白色ARC，绘制orange ARC
            // 2.绘制白色矩形

            // 1.绘制白色ARC
            canvas.drawArc(mArcRectF!!, 90f, 180f, false, mWhitePaint!!)

            // 2.绘制白色矩形
            mWhiteRectF!!.left = mArcRightLocation.toFloat()
            canvas.drawRect(mWhiteRectF!!, mWhitePaint!!)

            // 绘制叶子
            drawLeafs(canvas)

            // 3.绘制棕色 ARC
            // 单边角度
            val angle = Math.toDegrees(Math.acos(((mArcRadius - mCurrentProgressPosition)
                    / mArcRadius.toFloat()).toDouble())).toInt()
            // 起始的位置
            val startAngle = 180 - angle
            // 扫过的角度
            val sweepAngle = 2 * angle
            //            RxLogTool.i(TAG, "startAngle = " + startAngle);
            canvas.drawArc(mArcRectF!!, startAngle.toFloat(), sweepAngle.toFloat(), false, mOrangePaint!!)
        } else {
//            RxLogTool.i(TAG, "mProgress = " + mProgress + "---transfer-----mCurrentProgressPosition = " + mCurrentProgressPosition + "--mArcProgressWidth" + mArcRadius);
            // 1.绘制white RECT
            // 2.绘制Orange ARC
            // 3.绘制orange RECT
            // 这个层级进行绘制能让叶子感觉是融入棕色进度条中

            // 1.绘制white RECT
            mWhiteRectF!!.left = mCurrentProgressPosition.toFloat()
            canvas.drawRect(mWhiteRectF!!, mWhitePaint!!)
            // 绘制叶子
            drawLeafs(canvas)
            // 2.绘制Orange ARC
            canvas.drawArc(mArcRectF!!, 90f, 180f, false, mOrangePaint!!)
            // 3.绘制orange RECT
            mOrangeRectF!!.left = mArcRightLocation.toFloat()
            mOrangeRectF!!.right = mCurrentProgressPosition.toFloat()
            canvas.drawRect(mOrangeRectF!!, mOrangePaint!!)
        }
    }

    /**
     * 绘制叶子
     *
     * @param canvas
     */
    private fun drawLeafs(canvas: Canvas) {
        mLeafRotateTime = if (mLeafRotateTime <= 0) LEAF_ROTATE_TIME else mLeafRotateTime
        val currentTime = System.currentTimeMillis()
        for (i in mLeafInfos.indices) {
            val orange_pulp = mLeafInfos[i]
            if (currentTime > orange_pulp.startTime && orange_pulp.startTime != 0L) {
                // 绘制叶子－－根据叶子的类型和当前时间得出叶子的（x，y）
                getLeafLocation(orange_pulp, currentTime)
                // 根据时间计算旋转角度
                canvas.save()
                // 通过Matrix控制叶子旋转
                val matrix = Matrix()
                val transX = mLeftMargin + orange_pulp.x
                val transY = mLeftMargin + orange_pulp.y
                //                RxLogTool.i(TAG, "left.x = " + orange_pulp.x + "--orange_pulp.y=" + orange_pulp.y);
                matrix.postTranslate(transX, transY)
                // 通过时间关联旋转角度，则可以直接通过修改LEAF_ROTATE_TIME调节叶子旋转快慢
                val rotateFraction = ((currentTime - orange_pulp.startTime) % mLeafRotateTime
                        / mLeafRotateTime.toFloat())
                val angle = (rotateFraction * 360).toInt()
                // 根据叶子旋转方向确定叶子旋转角度
                val rotate = if (orange_pulp.rotateDirection == 0) angle + orange_pulp.rotateAngle else -angle
                +orange_pulp.rotateAngle
                matrix.postRotate(rotate.toFloat(), transX
                        + mLeafWidth / 2, transY + mLeafHeight / 2)
                canvas.drawBitmap(mLeafBitmap!!, matrix, mBitmapPaint)
                canvas.restore()
            } else {
                continue
            }
        }
    }

    private fun getLeafLocation(orange_pulp: Leaf, currentTime: Long) {
        val intervalTime = currentTime - orange_pulp.startTime
        mLeafFloatTime = if (mLeafFloatTime <= 0) LEAF_FLOAT_TIME else mLeafFloatTime
        if (intervalTime < 0) {
            return
        } else if (intervalTime > mLeafFloatTime) {
            orange_pulp.startTime = (System.currentTimeMillis()
                    + Random().nextInt(mLeafFloatTime.toInt()))
        }
        val fraction = intervalTime.toFloat() / mLeafFloatTime
        orange_pulp.x = (mProgressWidth - mProgressWidth * fraction)
        orange_pulp.y = getLocationY(orange_pulp).toFloat()
    }

    // 通过叶子信息获取当前叶子的Y值
    private fun getLocationY(orange_pulp: Leaf): Int {
        // y = A(wx+Q)+h
        val w = (2.toFloat() * Math.PI / mProgressWidth).toFloat()
        var a = middleAmplitude.toFloat()
        when (orange_pulp.type) {
            StartType.LITTLE ->                 // 小振幅 ＝ 中等振幅 － 振幅差
                a = middleAmplitude - mplitudeDisparity.toFloat()
            StartType.MIDDLE -> a = middleAmplitude.toFloat()
            StartType.BIG ->                 // 小振幅 ＝ 中等振幅 + 振幅差
                a = middleAmplitude + mplitudeDisparity.toFloat()
            else -> {
            }
        }
        //        RxLogTool.i(TAG, "---a = " + a + "---w = " + w + "--orange_pulp.x = " + orange_pulp.x);
        return (a * Math.sin(w * orange_pulp.x.toDouble())).toInt() + mArcRadius * 2 / 3
    }

    private fun initBitmap() {
        mLeafBitmap = (mResources.getDrawable(R.drawable.orange_pulp) as BitmapDrawable).bitmap
        mLeafWidth = mLeafBitmap?.width!!
        mLeafHeight = mLeafBitmap?.height!!
        mOuterBitmap = (mResources.getDrawable(R.drawable.orange_pulp_frame) as BitmapDrawable).bitmap
        mOuterWidth = mOuterBitmap?.width!!
        mOuterHeight = mOuterBitmap?.height!!
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mTotalWidth = w
        mTotalHeight = h
        mProgressWidth = mTotalWidth - mLeftMargin - mRightMargin
        mArcRadius = (mTotalHeight - 2 * mLeftMargin) / 2
        mOuterSrcRect = Rect(0, 0, mOuterWidth, mOuterHeight)
        mOuterDestRect = Rect(0, 0, mTotalWidth, mTotalHeight)
        mWhiteRectF = RectF((mLeftMargin + mCurrentProgressPosition).toFloat(), mLeftMargin.toFloat(), (mTotalWidth
                - mRightMargin).toFloat(),
                (mTotalHeight - mLeftMargin).toFloat())
        mOrangeRectF = RectF((mLeftMargin + mArcRadius).toFloat(), mLeftMargin.toFloat(),
                mCurrentProgressPosition.toFloat()
                , (mTotalHeight - mLeftMargin).toFloat())
        mArcRectF = RectF(mLeftMargin.toFloat(), mLeftMargin.toFloat(), (mLeftMargin + 2 * mArcRadius).toFloat(),
                (mTotalHeight - mLeftMargin).toFloat())
        mArcRightLocation = mLeftMargin + mArcRadius
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    fun setProgress(progress: Int) {
        mProgress = progress
        postInvalidate()
    }

    /**
     * 获取叶子飘完一个周期所花的时间
     */
    /**
     * 设置叶子飘完一个周期所花的时间
     *
     * @param time
     */
    var leafFloatTime: Long
        get() {
            mLeafFloatTime = if (mLeafFloatTime == 0L) LEAF_FLOAT_TIME else mLeafFloatTime
            return mLeafFloatTime
        }
        set(time) {
            mLeafFloatTime = time
        }

    /**
     * 获取叶子旋转一周所花的时间
     */
    /**
     * 设置叶子旋转一周所花的时间
     *
     * @param time
     */
    var leafRotateTime: Long
        get() {
            mLeafRotateTime = if (mLeafRotateTime == 0L) LEAF_ROTATE_TIME else mLeafRotateTime
            return mLeafRotateTime
        }
        set(time) {
            mLeafRotateTime = time
        }

    private enum class StartType {
        LITTLE, MIDDLE, BIG
    }

    /**
     * 叶子对象，用来记录叶子主要数据
     *
     * @author Ajian_Studio
     */
    private inner class Leaf {
        // 在绘制部分的位置
        var x = 0f
        var y = 0f

        // 控制叶子飘动的幅度
        var type: StartType? = null

        // 旋转角度
        var rotateAngle = 0

        // 旋转方向--0代表顺时针，1代表逆时针
        var rotateDirection = 0

        // 起始时间(ms)
        var startTime: Long = 0
    }

    private inner class LeafFactory {
        var random = Random()

        private val MAX_LEAFS = 8

        // 生成一个叶子信息
        fun generateLeaf(): Leaf {
            val orange_pulp = Leaf()
            val randomType = random.nextInt(3)
            // 随时类型－ 随机振幅
            var type = StartType.MIDDLE
            when (randomType) {
                0 -> {
                }
                1 -> type = StartType.LITTLE
                2 -> type = StartType.BIG
                else -> {
                }
            }
            orange_pulp.type = type
            // 随机起始的旋转角度
            orange_pulp.rotateAngle = random.nextInt(360)
            // 随机旋转方向（顺时针或逆时针）
            orange_pulp.rotateDirection = random.nextInt(2)
            // 为了产生交错的感觉，让开始的时间有一定的随机性
            mLeafFloatTime = if (mLeafFloatTime <= 0) LEAF_FLOAT_TIME else mLeafFloatTime
            mAddTime += random.nextInt((mLeafFloatTime * 2).toInt())
            orange_pulp.startTime = System.currentTimeMillis() + mAddTime
            return orange_pulp
        }

        // 根据传入的叶子数量产生叶子信息
        // 根据最大叶子数产生叶子信息
        @JvmOverloads
        fun generateLeafs(orange_pulpSize: Int = MAX_LEAFS): List<Leaf> {
            val orange_pulps: MutableList<Leaf> = LinkedList()
            for (i in 0 until orange_pulpSize) {
                orange_pulps.add(generateLeaf())
            }
            return orange_pulps
        }
    }

    companion object {
        private const val TAG = "TOrangeJuice"

        // 淡白色
        private const val WHITE_COLOR = -0x21c67

        // 橙色
        private const val ORANGE_COLOR = -0x5800

        // 中等振幅大小
        private const val MIDDLE_AMPLITUDE = 13

        // 不同类型之间的振幅差距
        private const val AMPLITUDE_DISPARITY = 5

        // 总进度
        private const val TOTAL_PROGRESS = 100

        // 叶子飘动一个周期所花的时间
        private const val LEAF_FLOAT_TIME: Long = 3000

        // 叶子旋转一周需要的时间
        private const val LEAF_ROTATE_TIME: Long = 2000

        // 用于控制绘制的进度条距离左／上／下的距离
        private const val LEFT_MARGIN = 9

        // 用于控制绘制的进度条距离右的距离
        private const val RIGHT_MARGIN = 25
    }

    init {
        mResources = resources
        mLeftMargin = dp2px(context!!, LEFT_MARGIN.toFloat())
        mRightMargin = dp2px(context, RIGHT_MARGIN.toFloat())
        mLeafFloatTime = LEAF_FLOAT_TIME
        mLeafRotateTime = LEAF_ROTATE_TIME
        initBitmap()
        initPaint()
        mLeafFactory = LeafFactory()
        mLeafInfos = mLeafFactory.generateLeafs()
    }
}