package com.tamsiree.rxui.view.timeline.video

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxkit.RxImageTool.getScreenResolution
import com.tamsiree.rxkit.TLog.d
import com.tamsiree.rxui.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TVideoTimeline : View {
    private val KEY_TICK_TEXT_SIZE_IN_SP = 12
    private val BIG_TICK_HEIGHT_IN_DP = 15
    private val SMALL_TICK_HEIGHT_IN_DP = 12
    private val BIG_TICK_HALF_WIDTH_IN_DP = 2
    private val SMALL_TICK_HALF_WIDTH_IN_DP = 1
    private val BIG_TICK_HALF_WIDTH = dp2px(context, BIG_TICK_HALF_WIDTH_IN_DP.toFloat())
    private val TRIANGLE_LENGTH = BIG_TICK_HALF_WIDTH * 4
    private val BIG_TICK_HEIGHT = dp2px(context, BIG_TICK_HEIGHT_IN_DP.toFloat())
    private val SMALL_TICK_HALF_WIDTH = dp2px(context, SMALL_TICK_HALF_WIDTH_IN_DP.toFloat())
    private val SMALL_TICK_HEIGHT = dp2px(context, SMALL_TICK_HEIGHT_IN_DP.toFloat())
    private val KEY_TICK_TEXT_SIZE = dp2px(context, KEY_TICK_TEXT_SIZE_IN_SP.toFloat())
    var lastMmiddlecursor = 0
    var firstTickToSeeInSecondUTC: Long = -1
    var zoneOffsetInSeconds = 0
    var lastX = 0f
    var lastY = 0f
    var lastcurrentTimeInMillisecond: Long = 0
    var lastMoveState = false
    var lastCheckState = false
    private var pixelsPerSecond = 0f
    private var mOnBarMoveListener: OnBarMoveListener? = null
    private var mOnBarScaledListener: OnBarScaledListener? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var linesColor = Color.BLACK
    private var recordBackgroundColor = Color.argb(200, 251, 180, 76)
    private var textColor = Color.BLACK
    private var middleCursorColor = Color.RED
    private var timebarPaint: Paint? = Paint()
    private val keyTickTextPaint = TextPaint()
    private val VIEW_HEIGHT_IN_DP = 150
    private var VIEW_HEIGHT = 0
    private var middleCursorVisible = true
    private val timebarTickCriterionMap: MutableMap<Int, TVideoTimelineTickCriterion> = HashMap()
    private var timebarTickCriterionCount = 5
    var currentTVideoTimelineTickCriterionIndex = 3
    private var recordDataExistTimeClipsList: MutableList<TRecordDataExistTimeSection>? = ArrayList()
    private var recordDataExistTimeClipsListMap: MutableMap<Long, List<TRecordDataExistTimeSection>>? = HashMap()
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var currentTimeInMillisecond: Long = 0
    var mostLeftTimeInMillisecond: Long = 0
    var mostRightTimeInMillisecond: Long = 0
    var screenLeftTimeInMillisecond: Long = 0
        get() {
            field = getCurrentTimeInMillisecond() - (screenWidth.toFloat() * 1000f / 2f / pixelsPerSecond).toLong()
            return field
        }
        private set
    var screenRightTimeInMillisecond: Long = 0
        get() {
            field = getCurrentTimeInMillisecond() + (screenWidth * 1000f / 2f / pixelsPerSecond).toLong()
            return field
        }
        private set
    private var justScaledByPressingButton = false
    private var WHOLE_TIMEBAR_TOTAL_SECONDS: Long = 0
    private var path: Path? = null
    private var calendar: Calendar? = null
    private val zeroTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    /*
     * 设置最大最小缩放级别
     *  0:精度为秒
     *  1:精度为一分钟
     *  2：精度为6分钟
     *  3:精度为30分钟
     *  4:精度为2小时
     */
    private val ZOOMMAX = 3
    private val ZOOMMIN = 1
    var idTag = 0
    private var mode = NONE

    // 设置进度条是否自动滚动
    var isMoveing = false
        private set

    // 进度条滚动状态
    private var moveIng = false

    // 是否检查录像标志位
    private var checkVideo = false
    private var moThread: MoveThread? = null
    var handler0 = Handler(Handler.Callback { msg ->
        when (msg.what) {
            MOVEING -> openMove()
            ACTION_UP -> if (checkVideo) {
                if (!checkHasVideo()) {
                    d("ACTION_UP", "NO VIDEO currentTimeInMillisecond:$currentTimeInMillisecond lastcurrentTimeInMillisecond:$lastcurrentTimeInMillisecond")
                    currentTimeInMillisecond = lastcurrentTimeInMillisecond
                    invalidate()
                    checkVideo = lastCheckState
                    if (mOnBarMoveListener != null) {
                        mOnBarMoveListener!!.onBarMove(screenLeftTimeInMillisecond, screenRightTimeInMillisecond, -1)
                    }
                } else {
                    if (mOnBarMoveListener != null) {
                        mOnBarMoveListener!!.OnBarMoveFinish(screenLeftTimeInMillisecond, screenRightTimeInMillisecond, currentTimeInMillisecond)
                    }
                }
            } else {
                if (mOnBarMoveListener != null) {
                    mOnBarMoveListener!!.OnBarMoveFinish(screenLeftTimeInMillisecond, screenRightTimeInMillisecond, currentTimeInMillisecond)
                }
            }
        }
        false
    })
    private var readyCheck = false
    private var mDrag = true

    constructor(context: Context?) : super(context) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    fun getRecordDataExistTimeClipsList(): List<TRecordDataExistTimeSection>? {
        return recordDataExistTimeClipsList
    }

    fun setRecordDataExistTimeClipsList(recordDataExistTimeClipsList: MutableList<TRecordDataExistTimeSection>?) {
        this.recordDataExistTimeClipsList = recordDataExistTimeClipsList
        arrangeRecordDataExistTimeClipsIntoMap(recordDataExistTimeClipsList)
    }

    private fun arrangeRecordDataExistTimeClipsIntoMap(clipsList: List<TRecordDataExistTimeSection>?) {
        recordDataExistTimeClipsListMap = HashMap()
        if (clipsList != null) {
            for (clipItem in clipsList) {
                for (dateZeroOClockItem in clipItem.getCoverDateZeroOClockList()) {
                    var list: MutableList<TRecordDataExistTimeSection>? = null
                    if (recordDataExistTimeClipsListMap?.get(dateZeroOClockItem).also { list = it as MutableList<TRecordDataExistTimeSection>? } == null) {
                        list = ArrayList()
                        recordDataExistTimeClipsListMap!![dateZeroOClockItem] = list as ArrayList<TRecordDataExistTimeSection>
                    }
                    list!!.add(clipItem)
                }
            }
        }
        postInvalidate()
    }

    fun initTimebarLengthAndPosition(mostLeftTime: Long, mostRightTime: Long, currentTime: Long) {
        mostLeftTimeInMillisecond = mostLeftTime
        mostRightTimeInMillisecond = mostRightTime
        currentTimeInMillisecond = currentTime
        WHOLE_TIMEBAR_TOTAL_SECONDS = (mostRightTime - mostLeftTime) / 1000
        initTVideoTimelineTickCriterionMap()
        resetToStandardWidth()
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        path = Path()
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.TVideoTimeline, defStyleAttr, 0)
        val n = a.indexCount
        for (i in 0 until n) {
            val attr = a.getIndex(i)
            if (attr == R.styleable.TVideoTimeline_middleCursorColor) {
                middleCursorColor = a.getColor(attr, Color.RED)
            } else if (attr == R.styleable.TVideoTimeline_recordBackgroundColor) { // 默认颜色设置为橘黄色
                recordBackgroundColor = a.getColor(attr, Color.argb(200, 251, 180, 76))
            } else if (attr == R.styleable.TVideoTimeline_recordTextColor) { // 默认颜色设置为黑色
                textColor = a.getColor(attr, Color.BLACK)
            } else if (attr == R.styleable.TVideoTimeline_timebarColor) { // 默认颜色设置为黑色
                linesColor = a.getColor(attr, Color.BLACK)
            }
        }
        a.recycle()
        screenWidth = getScreenResolution(context)[0]
        screenHeight = getScreenResolution(context)[1]
        currentTimeInMillisecond = System.currentTimeMillis()
        calendar = Calendar.getInstance()
        calendar?.set(Calendar.HOUR_OF_DAY, 0)
        calendar?.set(Calendar.MINUTE, 0)
        calendar?.set(Calendar.SECOND, 0)
        mostLeftTimeInMillisecond = calendar?.timeInMillis!!


        //mostLeftTimeInMillisecond = currentTimeInMillisecond - 3 * 3600 * 1000;
        calendar = Calendar.getInstance()
        calendar?.set(Calendar.HOUR_OF_DAY, 0)
        calendar?.set(Calendar.MINUTE, 0)
        calendar?.set(Calendar.SECOND, 0)
        calendar?.add(Calendar.DAY_OF_MONTH, 1)
        mostRightTimeInMillisecond = calendar?.timeInMillis!!
        //mostRightTimeInMillisecond = currentTimeInMillisecond + 3 * 3600 * 1000;
        WHOLE_TIMEBAR_TOTAL_SECONDS = (mostRightTimeInMillisecond - mostLeftTimeInMillisecond) / 1000
        pixelsPerSecond = (width - screenWidth).toFloat() / WHOLE_TIMEBAR_TOTAL_SECONDS.toFloat()
        initTVideoTimelineTickCriterionMap()
        currentTVideoTimelineTickCriterionIndex = 3

        //resetToStandardWidth();
        keyTickTextPaint.isAntiAlias = true
        keyTickTextPaint.textSize = KEY_TICK_TEXT_SIZE.toFloat()
        keyTickTextPaint.color = textColor
        val scaleGestureListener: OnScaleGestureListener = object : OnScaleGestureListener {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                if (lastMoveState) {
                    if (handler0.hasMessages(MOVEING)) handler0.removeMessages(MOVEING)
                    handler0.sendEmptyMessageDelayed(MOVEING, 1100)
                }
                scaleTimebarByFactor(detector.scaleFactor, false)
                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                justScaledByPressingButton = true
            }
        }
        scaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)
    }

    fun scaleTimebarByFactor(scaleFactor: Float, scaleByClickButton: Boolean) {
        var newWidth = ((width - screenWidth) * scaleFactor).toInt()
        if (newWidth > timebarTickCriterionMap[ZOOMMIN]!!.viewLength || newWidth < timebarTickCriterionMap[ZOOMMAX]!!.viewLength) return
        if (newWidth > timebarTickCriterionMap[0]!!.viewLength) {
            currentTVideoTimelineTickCriterionIndex = 0
            newWidth = timebarTickCriterionMap[0]!!.viewLength
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener!!.onOnBarScaledMode(0)
            }
        } else if (newWidth < timebarTickCriterionMap[0]!!.viewLength
                && newWidth >= getAverageWidthForTwoCriterion(0, 1)) {
            currentTVideoTimelineTickCriterionIndex = 0
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener!!.onOnBarScaledMode(0)
            }
        } else if (newWidth < getAverageWidthForTwoCriterion(0, 1)
                && newWidth >= getAverageWidthForTwoCriterion(1, 2)) {
            currentTVideoTimelineTickCriterionIndex = 1
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener!!.onOnBarScaledMode(1)
            }
        } else if (newWidth < getAverageWidthForTwoCriterion(1, 2)
                && newWidth >= getAverageWidthForTwoCriterion(2, 3)) {
            currentTVideoTimelineTickCriterionIndex = 2
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener!!.onOnBarScaledMode(2)
            }
        } else if (newWidth < getAverageWidthForTwoCriterion(2, 3)
                && newWidth >= getAverageWidthForTwoCriterion(3, 4)) {
            currentTVideoTimelineTickCriterionIndex = 3
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener!!.onOnBarScaledMode(3)
            }
        } else if (newWidth < getAverageWidthForTwoCriterion(3, 4)
                && newWidth >= timebarTickCriterionMap[4]!!.viewLength) {
            currentTVideoTimelineTickCriterionIndex = 4
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener!!.onOnBarScaledMode(4)
            }
        } else if (newWidth < timebarTickCriterionMap[4]!!.viewLength) {
            currentTVideoTimelineTickCriterionIndex = 4
            newWidth = timebarTickCriterionMap[4]!!.viewLength
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener!!.onOnBarScaledMode(4)
            }
        }
        if (scaleByClickButton) {
            justScaledByPressingButton = true
        }
        val params = layoutParams
        params.width = newWidth
        layoutParams = params
    }

    fun setMode(scalMode: Int) {
        if (scalMode < ZOOMMIN || scalMode > ZOOMMAX || scalMode == currentTVideoTimelineTickCriterionIndex) return
        when (scalMode) {
            0 -> {
                currentTVideoTimelineTickCriterionIndex = 0
                val newWidth = timebarTickCriterionMap[0]!!.viewLength
                justScaledByPressingButton = true
                val params = layoutParams
                params.width = newWidth
                layoutParams = params
            }
            1 -> {
                currentTVideoTimelineTickCriterionIndex = 1
                val newWidth1 = timebarTickCriterionMap[1]!!.viewLength
                justScaledByPressingButton = true
                val params1 = layoutParams
                params1.width = newWidth1
                layoutParams = params1
            }
            2 -> {
                currentTVideoTimelineTickCriterionIndex = 2
                val newWidth2 = timebarTickCriterionMap[2]!!.viewLength
                justScaledByPressingButton = true
                val params2 = layoutParams
                params2.width = newWidth2
                layoutParams = params2
            }
            3 -> {
                currentTVideoTimelineTickCriterionIndex = 3
                val newWidth3 = timebarTickCriterionMap[3]!!.viewLength
                justScaledByPressingButton = true
                val params3 = layoutParams
                params3.width = newWidth3
                layoutParams = params3
            }
            4 -> {
                currentTVideoTimelineTickCriterionIndex = 4
                val newWidth4 = timebarTickCriterionMap[4]!!.viewLength
                justScaledByPressingButton = true
                val params4 = layoutParams
                params4.width = newWidth4
                layoutParams = params4
            }
        }
    }

    private fun getAverageWidthForTwoCriterion(criterion1Index: Int, criterion2Index: Int): Float {
        val width1 = timebarTickCriterionMap[criterion1Index]!!.viewLength
        val width2 = timebarTickCriterionMap[criterion2Index]!!.viewLength
        return ((width1 + width2) / 2).toFloat()
    }

    private fun initTVideoTimelineTickCriterionMap() {
        val t0 = TVideoTimelineTickCriterion()
        t0.totalSecondsInOneScreen = 10 * 60
        t0.keyTickInSecond = 1 * 60
        t0.minTickInSecond = 6
        t0.dataPattern = "HH:mm"
        t0.viewLength = (screenWidth.toFloat() * WHOLE_TIMEBAR_TOTAL_SECONDS / t0.totalSecondsInOneScreen.toFloat()).toInt()
        timebarTickCriterionMap[0] = t0

        /*TVideoTimelineTickCriterion t1 = new TVideoTimelineTickCriterion();
        t1.setTotalSecondsInOneScreen(60 * 60);
        t1.setKeyTickInSecond(10 * 60);
        t1.setMinTickInSecond(60);
        t1.setDataPattern("HH:mm");
        t1.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t1.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(1, t1);*/
        val t1 = TVideoTimelineTickCriterion()
        t1.totalSecondsInOneScreen = 6 * 60
        t1.keyTickInSecond = 60
        t1.minTickInSecond = 6
        t1.dataPattern = "HH:mm"
        t1.viewLength = (screenWidth.toFloat() * WHOLE_TIMEBAR_TOTAL_SECONDS / t1.totalSecondsInOneScreen.toFloat()).toInt()
        timebarTickCriterionMap[1] = t1

        /*TVideoTimelineTickCriterion t2 = new TVideoTimelineTickCriterion();
        t2.setTotalSecondsInOneScreen(6 * 60 * 60);
        t2.setKeyTickInSecond(60 * 60);
        t2.setMinTickInSecond(5 * 60);
        t2.setDataPattern("HH:mm");
        t2.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t2.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(2, t2);*/
        val t2 = TVideoTimelineTickCriterion()
        t2.totalSecondsInOneScreen = 1 * 60 * 60
        t2.keyTickInSecond = 10 * 60
        t2.minTickInSecond = 1 * 60
        t2.dataPattern = "HH:mm"
        t2.viewLength = (screenWidth.toFloat() * WHOLE_TIMEBAR_TOTAL_SECONDS / t2.totalSecondsInOneScreen.toFloat()).toInt()
        timebarTickCriterionMap[2] = t2

        /*  TVideoTimelineTickCriterion t3 = new TVideoTimelineTickCriterion();
        t3.setTotalSecondsInOneScreen(36 * 60 * 60);
        t3.setKeyTickInSecond(6 * 60 * 60);
        t3.setMinTickInSecond(30 * 60);
        t3.setDataPattern("HH:mm");
        t3.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t3.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(3, t3);*/
        val t3 = TVideoTimelineTickCriterion()
        t3.totalSecondsInOneScreen = 30 * 60 * 60
        t3.keyTickInSecond = 6 * 60 * 60
        t3.minTickInSecond = 60 * 60
        t3.dataPattern = "HH:mm"
        t3.viewLength = (screenWidth.toFloat() * WHOLE_TIMEBAR_TOTAL_SECONDS / t3.totalSecondsInOneScreen.toFloat()).toInt()
        timebarTickCriterionMap[3] = t3
        val t4 = TVideoTimelineTickCriterion()
        t4.totalSecondsInOneScreen = 6 * 24 * 60 * 60
        t4.keyTickInSecond = 24 * 60 * 60
        t4.minTickInSecond = 2 * 60 * 60
        t4.dataPattern = "MM.dd"
        // t4.dataPattern = "MM.dd HH:mm:ss";
        t4.viewLength = (screenWidth.toFloat() * WHOLE_TIMEBAR_TOTAL_SECONDS / t4.totalSecondsInOneScreen.toFloat()).toInt()
        timebarTickCriterionMap[4] = t4
        timebarTickCriterionCount = timebarTickCriterionMap.size
    }

    private fun resetToStandardWidth() {
        currentTVideoTimelineTickCriterionIndex = 3
        val params = layoutParams
        params.width = timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.viewLength
        layoutParams = params
    }

    fun getCurrentTimeInMillisecond(): Long {
        return currentTimeInMillisecond
    }

    fun setCurrentTimeInMillisecond(currentTimeInMillisecond: Long) {
        this.currentTimeInMillisecond = currentTimeInMillisecond
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        VIEW_HEIGHT = if (heightMode == MeasureSpec.AT_MOST) {
            dp2px(context, VIEW_HEIGHT_IN_DP.toFloat())
        } else {
            heightSize
        }
        setMeasuredDimension(measureWidth(widthMeasureSpec), VIEW_HEIGHT)
        if (justScaledByPressingButton && mOnBarScaledListener != null) {
            justScaledByPressingButton = false
            mOnBarScaledListener!!.onBarScaleFinish(screenLeftTimeInMillisecond, screenRightTimeInMillisecond, currentTimeInMillisecond)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        d("onSizeChanged", " w:$w h:$h oldw:$oldh w:$oldh")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        d("onLayout", "changed:$changed left:$left top:$top right:$right bottom:$bottom")

        /* if (currentTimeInMillisecond != System.currentTimeMillis() && left == 0)
            layout((int) (0 - (currentTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 * pixelsPerSecond),
                    getTop(),
                    getWidth() - (int) ((currentTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 * pixelsPerSecond),
                    getTop() + getHeight());*/super.onLayout(changed, left, top, right, bottom)
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val measureMode = MeasureSpec.getMode(widthMeasureSpec)
        val measureSize = MeasureSpec.getSize(widthMeasureSpec)
        var result = suggestedMinimumWidth
        when (measureMode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> {
                result = measureSize + screenWidth
                pixelsPerSecond = measureSize / WHOLE_TIMEBAR_TOTAL_SECONDS.toFloat()
                if (mOnBarScaledListener != null) {
                    mOnBarScaledListener!!.onBarScaled(screenLeftTimeInMillisecond, screenRightTimeInMillisecond, currentTimeInMillisecond)
                }
            }
            else -> {
            }
        }
        d("measureWidth", "measureMode:" + measureMode + "measureSize:" + measureSize + " result" + result)
        return result
    }

    private fun getTimeStringFromLong(value: Long): String {
        val timeFormat = SimpleDateFormat(timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.dataPattern)
        return timeFormat.format(value)
    }

    fun setMiddleCursorVisible(middleCursorVisible: Boolean) {
        this.middleCursorVisible = middleCursorVisible
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        /*if (notInited) {
            notInited = false;
            resetToStandardWidth();
            return;
        }*/d("onDraw", "onDraw")
        pixelsPerSecond = (width - screenWidth).toFloat() / WHOLE_TIMEBAR_TOTAL_SECONDS.toFloat()
        val cal = Calendar.getInstance()
        zoneOffsetInSeconds = cal[Calendar.ZONE_OFFSET] / 1000
        val forStartUTC = (currentTimeInMillisecond / 1000 - screenWidth / pixelsPerSecond / 2 - timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.minTickInSecond).toLong()
        val forEndUTC = (currentTimeInMillisecond / 1000 + screenWidth / pixelsPerSecond / 2 + timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.minTickInSecond).toLong()
        val forStartLocalTimezone = forStartUTC + zoneOffsetInSeconds
        val forEndLocalTimezone = forEndUTC + zoneOffsetInSeconds
        for (i in forStartLocalTimezone..forEndLocalTimezone) {
            if (i % timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.minTickInSecond == 0L) {
                firstTickToSeeInSecondUTC = i - zoneOffsetInSeconds
                break
            }
        }


        // 画刻度及时间
        drawTick(canvas)

        // 画录像条
        drawRecord(canvas)


        // 画中间刻度
        drawmiddleCursor(canvas)
        layout((0 - (currentTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 * pixelsPerSecond).toInt(),
                top,
                width - ((currentTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 * pixelsPerSecond).toInt(),
                top + height)
    }

    private fun drawTick(canvas: Canvas) {
        val totalTickToDrawInOneScreen = (screenWidth / pixelsPerSecond / timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.minTickInSecond).toInt() + 2
        val keytextY = height / 2.toFloat()
        for (i in -20..totalTickToDrawInOneScreen + 10) {
            val drawTickTimeInSecondUTC = firstTickToSeeInSecondUTC + i * timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.minTickInSecond
            val drawTickTimeInSecondLocalTimezone = drawTickTimeInSecondUTC + zoneOffsetInSeconds
            if (drawTickTimeInSecondLocalTimezone % timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.keyTickInSecond == 0L) { //关键刻度
                //画大刻度
                timebarPaint!!.color = linesColor
                timebarPaint!!.isAntiAlias = true
                timebarPaint!!.style = Paint.Style.FILL
                val startX = pixelsPerSecond * (drawTickTimeInSecondUTC - mostLeftTimeInMillisecond / 1000) + screenWidth / 2f
                val largeTickRect = RectF(startX - BIG_TICK_HALF_WIDTH / 2, (height - BIG_TICK_HEIGHT).toFloat(), startX + BIG_TICK_HALF_WIDTH / 2, height.toFloat())
                canvas.drawRect(largeTickRect, timebarPaint!!)
                val largeTickRect1 = RectF(startX - BIG_TICK_HALF_WIDTH / 2, 0f, startX + BIG_TICK_HALF_WIDTH / 2, BIG_TICK_HEIGHT.toFloat())
                canvas.drawRect(largeTickRect1, timebarPaint!!)

                //画时间文字
                val keytext = getTimeStringFromLong(drawTickTimeInSecondUTC * 1000)
                val keyTextWidth = keyTickTextPaint.measureText(keytext)
                val keytextX = startX - keyTextWidth / 2
                canvas.drawText(keytext, keytextX, keytextY, keyTickTextPaint)
            } else if (drawTickTimeInSecondLocalTimezone % timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.minTickInSecond == 0L) {
                //小刻度
                timebarPaint!!.color = linesColor
                timebarPaint!!.isAntiAlias = true
                timebarPaint!!.style = Paint.Style.FILL
                val startX = pixelsPerSecond * (drawTickTimeInSecondUTC - mostLeftTimeInMillisecond / 1000) + screenWidth / 2f
                val smallTickRect = RectF(startX - SMALL_TICK_HALF_WIDTH / 2, (height - SMALL_TICK_HEIGHT).toFloat(), startX + SMALL_TICK_HALF_WIDTH / 2, height.toFloat())
                canvas.drawRect(smallTickRect, timebarPaint!!)
                val smallTickRect1 = RectF(startX - SMALL_TICK_HALF_WIDTH / 2, 0f, startX + SMALL_TICK_HALF_WIDTH / 2, SMALL_TICK_HEIGHT.toFloat())
                canvas.drawRect(smallTickRect1, timebarPaint!!)
            }
        }
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, timebarPaint!!)
        canvas.drawLine(0f, VIEW_HEIGHT.toFloat(), width.toFloat(), VIEW_HEIGHT.toFloat(), timebarPaint!!)
    }

    private fun drawRecord(canvas: Canvas) {
        //录像从哪个时间点开始，单位是毫秒
        val startDrawTimeInSeconds = firstTickToSeeInSecondUTC + -20 * timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.minTickInSecond
        if (recordDataExistTimeClipsList != null && recordDataExistTimeClipsList!!.size > 0) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val startDrawTimeDateString = dateFormat.format(startDrawTimeInSeconds * 1000)
            val zeroTimeString = "$startDrawTimeDateString 00:00:00"
            val screenLastSecondToSee = (startDrawTimeInSeconds + screenWidth / pixelsPerSecond + 30 * timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.minTickInSecond).toLong() * 1000L
            val startDate: Date
            try {
                startDate = zeroTimeFormat.parse(zeroTimeString)
                var startList = recordDataExistTimeClipsListMap!![startDate.time]
                if (startList == null) {
                    var afterFindDays = 1
                    val findTimeInMilliseconds = startDate.time
                    var newFindStartMilliseconds = findTimeInMilliseconds
                    while (startList == null && newFindStartMilliseconds < screenLastSecondToSee) {
                        newFindStartMilliseconds = findTimeInMilliseconds + SECONDS_PER_DAY.toLong() * 1000L * afterFindDays.toLong()
                        startList = recordDataExistTimeClipsListMap!![newFindStartMilliseconds]
                        afterFindDays++
                    }
                }
                if (startList != null && startList.size > 0) {
                    val thisDateFirstClipStartIndex = recordDataExistTimeClipsList!!.indexOf(startList[0])
                    val endDrawTimeInSeconds = (startDrawTimeInSeconds
                            + screenWidth / pixelsPerSecond + timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.minTickInSecond * 30).toLong()
                    timebarPaint!!.color = recordBackgroundColor
                    timebarPaint!!.style = Paint.Style.FILL
                    for (i in thisDateFirstClipStartIndex until recordDataExistTimeClipsList!!.size) {
                        val leftX = pixelsPerSecond * (recordDataExistTimeClipsList!![i].startTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 + screenWidth / 2f
                        val rightX = pixelsPerSecond * (recordDataExistTimeClipsList!![i].endTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 + screenWidth / 2f
                        val rectF = RectF(leftX, 0f, rightX, height.toFloat())
                        canvas.drawRect(rectF, timebarPaint!!)
                        if (recordDataExistTimeClipsList!![i].endTimeInMillisecond >= endDrawTimeInSeconds * 1000) {
                            break
                        }
                    }
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    private fun drawmiddleCursor(canvas: Canvas) {
        if (middleCursorVisible) {
            timebarPaint!!.style = Paint.Style.FILL
            timebarPaint!!.color = middleCursorColor
            val currentCursor = ((currentTimeInMillisecond / 1000L - mostLeftTimeInMillisecond / 1000L) * pixelsPerSecond + screenWidth / 2f - TRIANGLE_LENGTH / 2).toInt()
            lastMmiddlecursor = currentCursor
            // TLog.d("TIMEBARVIEW", "currentCursor" + currentCursor + " viewWidth:" + getWidth());
            //path.rMoveTo(currentCursor, 0);
            // 画三角形
            path = Path()
            path!!.moveTo(currentCursor.toFloat(), 0f)
            path!!.lineTo(currentCursor + TRIANGLE_LENGTH.toFloat(), 0f)
            // 求三角形高
            val length = Math.sqrt(3.0).toFloat() * TRIANGLE_LENGTH / 2
            path!!.lineTo(currentCursor + TRIANGLE_LENGTH / 2.toFloat(), length)
            path!!.lineTo(currentCursor.toFloat(), 0f)
            canvas.drawPath(path!!, timebarPaint!!)
            // 画三角形下面的线条
            canvas.drawLine(currentCursor + TRIANGLE_LENGTH / 2.toFloat(), 0f, currentCursor + TRIANGLE_LENGTH / 2.toFloat(), VIEW_HEIGHT.toFloat(), timebarPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector!!.onTouchEvent(event)
        if (scaleGestureDetector!!.isInProgress) {
            return true
        }
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                if (handler0.hasMessages(ACTION_UP)) handler0.removeMessages(ACTION_UP)

                // 先记录进度条移动状态 如果进度条正在移动 先停止
                lastMoveState = isMoveing
                lastCheckState = checkVideo
                checkVideo = readyCheck
                closeMove()
                lastcurrentTimeInMillisecond = currentTimeInMillisecond
                mode = DRAG
                lastX = event.rawX
                lastY = event.rawY
            }
            MotionEvent.ACTION_POINTER_DOWN -> mode = ZOOM
            MotionEvent.ACTION_MOVE -> if (mode == DRAG && mDrag) {
                val dx = (event.rawX - lastX).toInt()
                if (dx == 0) {
                    return false
                }
                val top = top
                d("*****onTouchEvent", "  dx$dx left$left right$left$width")
                var left = left + dx
                var right = left + width
                if (left >= 0) {
                    left = 0
                    right = width
                }
                if (right < screenWidth) {
                    right = screenWidth
                    left = right - width
                }
                layout(left, top, right, top + height)
                invalidate()
                lastX = event.rawX
                lastY = event.rawY
                val deltaX = 0 - left
                val timeBarLength = width - screenWidth
                currentTimeInMillisecond = mostLeftTimeInMillisecond + deltaX * WHOLE_TIMEBAR_TOTAL_SECONDS * 1000 / timeBarLength
                if (mOnBarMoveListener != null) {
                    mOnBarMoveListener!!.onBarMove(screenLeftTimeInMillisecond, screenRightTimeInMillisecond, currentTimeInMillisecond)
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                currentTimeInMillisecond = lastcurrentTimeInMillisecond
                checkVideo = lastCheckState
                if (mOnBarMoveListener != null) {
                    mOnBarMoveListener!!.onBarMove(screenLeftTimeInMillisecond, screenRightTimeInMillisecond, currentTimeInMillisecond)
                }
                invalidate()
                /* if (lastMoveState) {
                    if (handler.hasMessages(MOVEING))
                        handler.removeMessages(MOVEING);
                    handler.sendEmptyMessageDelayed(MOVEING, 1100);
                }*/mode = NONE
            }
            MotionEvent.ACTION_UP -> {
                if (mode == DRAG) {
                    val deltaX_up = 0 - left
                    val timeBarLength_up = width - screenWidth
                    currentTimeInMillisecond = mostLeftTimeInMillisecond + deltaX_up * WHOLE_TIMEBAR_TOTAL_SECONDS * 1000 / timeBarLength_up
                    //invalidate();
                    if (handler0.hasMessages(ACTION_UP)) handler0.removeMessages(ACTION_UP)
                    handler0.sendEmptyMessageDelayed(ACTION_UP, 1100)
                    /*if (lastMoveState) {
                        if (handler.hasMessages(MOVEING))
                            handler.removeMessages(MOVEING);
                        handler.sendEmptyMessageDelayed(MOVEING, 1100);
                    }*/
                }
                mode = NONE
            }
        }
        return true
    }

    fun scaleByPressingButton(zoomIn: Boolean) {

        //当前所在刻度标准的默认长度（不含两端空出的screenWidth）
        val currentCriterionViewLength = timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.viewLength
        val currentViewLength = width - screenWidth
        if (currentViewLength == currentCriterionViewLength) {
            if (zoomIn) {
                val newCriteriaIndex = currentTVideoTimelineTickCriterionIndex - 1
                if (newCriteriaIndex < ZOOMMIN || newCriteriaIndex > ZOOMMAX) {
                    return
                } else {
                    currentTVideoTimelineTickCriterionIndex = newCriteriaIndex
                    val newWidth = timebarTickCriterionMap[newCriteriaIndex]!!.viewLength
                    justScaledByPressingButton = true
                    val params = layoutParams
                    params.width = newWidth
                    layoutParams = params
                }
            } else {
                val newCriteriaIndex = currentTVideoTimelineTickCriterionIndex + 1
                // TLog.d("newCriteriaIndex", newCriteriaIndex + "");
                if (newCriteriaIndex > ZOOMMAX || newCriteriaIndex >= timebarTickCriterionCount) {
                    return
                } else {
                    currentTVideoTimelineTickCriterionIndex = newCriteriaIndex
                    val newWidth = timebarTickCriterionMap[newCriteriaIndex]!!.viewLength
                    justScaledByPressingButton = true
                    val params = layoutParams
                    params.width = newWidth
                    layoutParams = params
                }
            }
        } else {
            if (currentViewLength > currentCriterionViewLength) {
                if (zoomIn) {
                    val newCriteriaIndex = currentTVideoTimelineTickCriterionIndex - 1
                    if (newCriteriaIndex < 0) {
                        return
                    } else {
                        currentTVideoTimelineTickCriterionIndex = newCriteriaIndex
                        val newWidth = timebarTickCriterionMap[newCriteriaIndex]!!.viewLength
                        justScaledByPressingButton = true
                        val params = layoutParams
                        params.width = newWidth
                        layoutParams = params
                    }
                } else {
                    val newWidth = timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.viewLength
                    justScaledByPressingButton = true
                    val params = layoutParams
                    params.width = newWidth
                    layoutParams = params
                }
            } else {
                if (zoomIn) {
                    val newWidth = timebarTickCriterionMap[currentTVideoTimelineTickCriterionIndex]!!.viewLength
                    justScaledByPressingButton = true
                    val params = layoutParams
                    params.width = newWidth
                    layoutParams = params
                } else {
                    val newCriteriaIndex = currentTVideoTimelineTickCriterionIndex + 1
                    if (newCriteriaIndex >= timebarTickCriterionCount) {
                        return
                    } else {
                        currentTVideoTimelineTickCriterionIndex = newCriteriaIndex
                        val newWidth = timebarTickCriterionMap[newCriteriaIndex]!!.viewLength
                        justScaledByPressingButton = true
                        val params = layoutParams
                        params.width = newWidth
                        layoutParams = params
                    }
                }
            }
        }
    }

    fun setOnBarMoveListener(onBarMoveListener: OnBarMoveListener?) {
        mOnBarMoveListener = onBarMoveListener
    }

    fun setOnBarScaledListener(onBarScaledListener: OnBarScaledListener?) {
        mOnBarScaledListener = onBarScaledListener
    }

    fun openMove() {
        if (!moveIng) {
            isMoveing = true
            moThread = null
            moThread = MoveThread()
            moThread!!.start()
        }
    }

    fun closeMove() {
        isMoveing = false
        moThread = null
    }

    fun setMoveFlag(moveFlag: Boolean) {
        isMoveing = moveFlag
    }

    /*
     *
     * 设置是否检查有录像
     *
     * */
    fun checkVideo(check: Boolean) {
        readyCheck = check
    }

    /*
     * 返回下一个录像开始点
     * */
    private fun locationVideo(): Long {
        if (recordDataExistTimeClipsList == null) return -1
        val size = recordDataExistTimeClipsList!!.size
        for (i in 0 until size - 1) {
            val lastEndTime = recordDataExistTimeClipsList!![i].endTimeInMillisecond
            val nextStartTime = recordDataExistTimeClipsList!![i + 1].startTimeInMillisecond
            if (currentTimeInMillisecond > lastEndTime && currentTimeInMillisecond < nextStartTime) {
                return nextStartTime
            }
        }
        return -1
    }

    /*判断是否有录像*/
    private fun checkHasVideo(): Boolean {
        if (recordDataExistTimeClipsList != null && recordDataExistTimeClipsList!!.size > 0) {
            for (recordInfo in recordDataExistTimeClipsList!!) {
                if (recordInfo.startTimeInMillisecond <= currentTimeInMillisecond
                        && currentTimeInMillisecond <= recordInfo.endTimeInMillisecond) return true
            }
        }
        return false
    }

    fun recycle() {
        closeMove()
        if (recordDataExistTimeClipsList != null) {
            recordDataExistTimeClipsList!!.clear()
            recordDataExistTimeClipsList = null
        }
        if (recordDataExistTimeClipsListMap != null) {
            recordDataExistTimeClipsListMap!!.clear()
            recordDataExistTimeClipsListMap = null
        }
        mOnBarMoveListener = null
        mOnBarScaledListener = null
        timebarPaint = null
        scaleGestureDetector = null
    }

    // 设置是否允许拖动
    fun setDrag(mDrag: Boolean) {
        this.mDrag = mDrag
    }

    interface OnBarMoveListener {
        fun onBarMove(screenLeftTime: Long, screenRightTime: Long, currentTime: Long)
        fun OnBarMoveFinish(screenLeftTime: Long, screenRightTime: Long, currentTime: Long)
    }

    interface OnBarScaledListener {
        fun onOnBarScaledMode(mode: Int)
        fun onBarScaled(screenLeftTime: Long, screenRightTime: Long, currentTime: Long)
        fun onBarScaleFinish(screenLeftTime: Long, screenRightTime: Long, currentTime: Long)
    }

    private inner class MoveThread : Thread() {
        override fun run() {
            d("MOVETHREAD", "thread is start")
            moveIng = true
            while (isMoveing) {
                try {
                    sleep(1000)
                    d("MOVETHREAD", "thread is running")
                    currentTimeInMillisecond += 1000
                    if (checkVideo) {
                        if (!checkHasVideo()) {
                            val nextStartTime = locationVideo()
                            if (nextStartTime != -1L) {
                                currentTimeInMillisecond = nextStartTime
                            } else {
                                currentTimeInMillisecond -= 1000
                                isMoveing = false
                                moveIng = false
                                break
                            }
                        }
                    }
                    postInvalidate()
                    post {
                        if (mOnBarMoveListener != null) {
                            mOnBarMoveListener!!.onBarMove(screenLeftTimeInMillisecond, screenRightTimeInMillisecond, currentTimeInMillisecond)
                        }
                    }
                } catch (e: InterruptedException) {
                    moveIng = false
                    e.printStackTrace()
                }
            }
            moveIng = false
            d("MOVETHREAD", "thread is stop")
        }
    }

    companion object {
        const val SECONDS_PER_DAY = 24 * 60 * 60
        private const val MOVEING = 0x001
        private const val ACTION_UP = MOVEING + 1
        private const val NONE = 0
        private const val DRAG = 1
        private const val ZOOM = 2
    }
}