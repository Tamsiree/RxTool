
package com.tamsiree.rxui.view.timeline.video;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.tamsiree.rxkit.RxImageTool;
import com.tamsiree.rxkit.TLog;
import com.tamsiree.rxui.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TVideoTimeline extends View {

    public final static int SECONDS_PER_DAY = 24 * 60 * 60;
    private static final int MOVEING = 0x001;
    private static final int ACTION_UP = MOVEING + 1;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private final int KEY_TICK_TEXT_SIZE_IN_SP = 12;
    private final int BIG_TICK_HEIGHT_IN_DP = 15;
    private final int SMALL_TICK_HEIGHT_IN_DP = 12;
    private final int BIG_TICK_HALF_WIDTH_IN_DP = 2;
    private final int SMALL_TICK_HALF_WIDTH_IN_DP = 1;
    private final int BIG_TICK_HALF_WIDTH = RxImageTool.dp2px(getContext(), BIG_TICK_HALF_WIDTH_IN_DP);
    private final int TRIANGLE_LENGTH = BIG_TICK_HALF_WIDTH * 4;
    private final int BIG_TICK_HEIGHT = RxImageTool.dp2px(getContext(), BIG_TICK_HEIGHT_IN_DP);
    private final int SMALL_TICK_HALF_WIDTH = RxImageTool.dp2px(getContext(), SMALL_TICK_HALF_WIDTH_IN_DP);
    private final int SMALL_TICK_HEIGHT = RxImageTool.dp2px(getContext(), SMALL_TICK_HEIGHT_IN_DP);
    private final int KEY_TICK_TEXT_SIZE = RxImageTool.dp2px(getContext(), KEY_TICK_TEXT_SIZE_IN_SP);
    int lastMmiddlecursor = 0;
    long firstTickToSeeInSecondUTC = -1;
    int zoneOffsetInSeconds;
    float lastX, lastY;
    long lastcurrentTimeInMillisecond = 0;
    boolean lastMoveState;
    boolean lastCheckState;
    private float pixelsPerSecond = 0;
    private OnBarMoveListener mOnBarMoveListener;
    private OnBarScaledListener mOnBarScaledListener;
    private int screenWidth, screenHeight;
    private int linesColor = Color.BLACK;
    private int recordBackgroundColor = Color.argb(200, 251, 180, 76);
    private int textColor = Color.BLACK;
    private int middleCursorColor = Color.RED;
    private Paint timebarPaint = new Paint();
    private TextPaint keyTickTextPaint = new TextPaint();
    private int VIEW_HEIGHT_IN_DP = 150;
    private int VIEW_HEIGHT;
    private boolean middleCursorVisible = true;
    private Map<Integer, TVideoTimelineTickCriterion> timebarTickCriterionMap = new HashMap<>();
    private int timebarTickCriterionCount = 5;
    private int currentTVideoTimelineTickCriterionIndex = 3;
    private List<TRecordDataExistTimeSection> recordDataExistTimeClipsList = new ArrayList<>();
    private Map<Long, List<TRecordDataExistTimeSection>> recordDataExistTimeClipsListMap = new HashMap<>();
    private ScaleGestureDetector scaleGestureDetector;
    private long currentTimeInMillisecond;
    private long mostLeftTimeInMillisecond;
    private long mostRightTimeInMillisecond;
    private long screenLeftTimeInMillisecond;
    private long screenRightTimeInMillisecond;
    private boolean justScaledByPressingButton = false;
    private long WHOLE_TIMEBAR_TOTAL_SECONDS;
    private Path path;
    private Calendar calendar;
    private SimpleDateFormat zeroTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /*
     * 设置最大最小缩放级别
     *  0:精度为秒
     *  1:精度为一分钟
     *  2：精度为6分钟
     *  3:精度为30分钟
     *  4:精度为2小时
     */
    private int ZOOMMAX = 3;
    private int ZOOMMIN = 1;
    private int idTag;
    private int mode = NONE;
    // 设置进度条是否自动滚动
    private boolean moveFlag = false;
    // 进度条滚动状态
    private boolean moveIng = false;
    // 是否检查录像标志位
    private boolean checkVideo = false;
    private MoveThread moThread;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case MOVEING:
                    openMove();
                    break;
                case ACTION_UP:
                    if (checkVideo) {
                        if (!checkHasVideo()) {
                            TLog.d("ACTION_UP", "NO VIDEO currentTimeInMillisecond:" + currentTimeInMillisecond + " lastcurrentTimeInMillisecond:" + lastcurrentTimeInMillisecond);
                            currentTimeInMillisecond = lastcurrentTimeInMillisecond;
                            invalidate();
                            checkVideo = lastCheckState;
                            if (mOnBarMoveListener != null) {
                                mOnBarMoveListener.onBarMove(getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), -1);
                            }
                        } else {
                            if (mOnBarMoveListener != null) {
                                mOnBarMoveListener.OnBarMoveFinish(getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), currentTimeInMillisecond);
                            }
                        }
                    } else {
                        if (mOnBarMoveListener != null) {
                            mOnBarMoveListener.OnBarMoveFinish(getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), currentTimeInMillisecond);
                        }
                    }
                    break;

            }

            return false;
        }
    });
    private boolean readyCheck = false;
    private boolean mDrag = true;

    public TVideoTimeline(Context context) {
        super(context);
        init(null, 0);

    }

    public TVideoTimeline(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }


    public TVideoTimeline(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public List<TRecordDataExistTimeSection> getRecordDataExistTimeClipsList() {
        return recordDataExistTimeClipsList;
    }

    public void setRecordDataExistTimeClipsList(List<TRecordDataExistTimeSection> recordDataExistTimeClipsList) {
        this.recordDataExistTimeClipsList = recordDataExistTimeClipsList;
        arrangeRecordDataExistTimeClipsIntoMap(recordDataExistTimeClipsList);
    }

    public long getMostLeftTimeInMillisecond() {
        return mostLeftTimeInMillisecond;
    }

    public void setMostLeftTimeInMillisecond(long mostLeftTimeInMillisecond) {
        this.mostLeftTimeInMillisecond = mostLeftTimeInMillisecond;
    }

    public long getMostRightTimeInMillisecond() {
        return mostRightTimeInMillisecond;
    }

    public void setMostRightTimeInMillisecond(long mostRightTimeInMillisecond) {
        this.mostRightTimeInMillisecond = mostRightTimeInMillisecond;
    }

    public long getScreenLeftTimeInMillisecond() {
        screenLeftTimeInMillisecond = getCurrentTimeInMillisecond() - (long) ((float) screenWidth * 1000f / 2f / pixelsPerSecond);

        return screenLeftTimeInMillisecond;
    }

    public long getScreenRightTimeInMillisecond() {
        screenRightTimeInMillisecond = getCurrentTimeInMillisecond() + (long) (screenWidth * 1000f / 2f / pixelsPerSecond);
        return screenRightTimeInMillisecond;
    }

    private void arrangeRecordDataExistTimeClipsIntoMap(List<TRecordDataExistTimeSection> clipsList) {
        recordDataExistTimeClipsListMap = new HashMap<>();

        if (clipsList != null) {
            for (TRecordDataExistTimeSection clipItem : clipsList) {
                for (Long dateZeroOClockItem : clipItem.getCoverDateZeroOClockList()) {
                    List<TRecordDataExistTimeSection> list = null;
                    if ((list = recordDataExistTimeClipsListMap.get(dateZeroOClockItem)) == null) {
                        list = new ArrayList<>();
                        recordDataExistTimeClipsListMap.put(dateZeroOClockItem, list);
                    }
                    list.add(clipItem);
                }

            }
        }
        postInvalidate();
    }

    public void initTimebarLengthAndPosition(long mostLeftTime, long mostRightTime, long currentTime) {
        this.mostLeftTimeInMillisecond = mostLeftTime;
        this.mostRightTimeInMillisecond = mostRightTime;
        this.currentTimeInMillisecond = currentTime;
        WHOLE_TIMEBAR_TOTAL_SECONDS = (mostRightTime - mostLeftTime) / 1000;
        initTVideoTimelineTickCriterionMap();
        resetToStandardWidth();
    }

    public int getCurrentTVideoTimelineTickCriterionIndex() {
        return currentTVideoTimelineTickCriterionIndex;
    }

    public void setCurrentTVideoTimelineTickCriterionIndex(int currentTVideoTimelineTickCriterionIndex) {
        this.currentTVideoTimelineTickCriterionIndex = currentTVideoTimelineTickCriterionIndex;
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        path = new Path();
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TVideoTimeline, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.TVideoTimeline_middleCursorColor) {
                middleCursorColor = a.getColor(attr, Color.RED);

            } else if (attr == R.styleable.TVideoTimeline_recordBackgroundColor) {// 默认颜色设置为橘黄色
                recordBackgroundColor = a.getColor(attr, Color.argb(200, 251, 180, 76));

            } else if (attr == R.styleable.TVideoTimeline_recordTextColor) {// 默认颜色设置为黑色
                textColor = a.getColor(attr, Color.BLACK);

            } else if (attr == R.styleable.TVideoTimeline_timebarColor) {// 默认颜色设置为黑色
                linesColor = a.getColor(attr, Color.BLACK);

            }

        }
        a.recycle();
        screenWidth = RxImageTool.getScreenResolution(getContext())[0];
        screenHeight = RxImageTool.getScreenResolution(getContext())[1];


        currentTimeInMillisecond = System.currentTimeMillis();

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        mostLeftTimeInMillisecond = calendar.getTimeInMillis();


        //mostLeftTimeInMillisecond = currentTimeInMillisecond - 3 * 3600 * 1000;

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        mostRightTimeInMillisecond = calendar.getTimeInMillis();
        //mostRightTimeInMillisecond = currentTimeInMillisecond + 3 * 3600 * 1000;


        WHOLE_TIMEBAR_TOTAL_SECONDS = (mostRightTimeInMillisecond - mostLeftTimeInMillisecond) / 1000;

        pixelsPerSecond = (float) (getWidth() - screenWidth) / (float) WHOLE_TIMEBAR_TOTAL_SECONDS;

        initTVideoTimelineTickCriterionMap();
        setCurrentTVideoTimelineTickCriterionIndex(3);

        //resetToStandardWidth();

        keyTickTextPaint.setAntiAlias(true);
        keyTickTextPaint.setTextSize(KEY_TICK_TEXT_SIZE);
        keyTickTextPaint.setColor(textColor);

        ScaleGestureDetector.OnScaleGestureListener scaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (lastMoveState) {
                    if (handler.hasMessages(MOVEING))
                        handler.removeMessages(MOVEING);
                    handler.sendEmptyMessageDelayed(MOVEING, 1100);
                }
                scaleTimebarByFactor(detector.getScaleFactor(), false);
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                justScaledByPressingButton = true;
            }


        };
        scaleGestureDetector = new ScaleGestureDetector(getContext(), scaleGestureListener);

    }

    public void scaleTimebarByFactor(float scaleFactor, boolean scaleByClickButton) {

        int newWidth = (int) ((getWidth() - screenWidth) * scaleFactor);

        if (newWidth > timebarTickCriterionMap.get(ZOOMMIN).getViewLength() || newWidth < timebarTickCriterionMap.get(ZOOMMAX).getViewLength())
            return;

        if (newWidth > timebarTickCriterionMap.get(0).getViewLength()) {
            setCurrentTVideoTimelineTickCriterionIndex(0);
            newWidth = timebarTickCriterionMap.get(0).getViewLength();
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener.onOnBarScaledMode(0);
            }

        } else if (newWidth < timebarTickCriterionMap.get(0).getViewLength()
                && newWidth >= getAverageWidthForTwoCriterion(0, 1)) {
            setCurrentTVideoTimelineTickCriterionIndex(0);
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener.onOnBarScaledMode(0);
            }

        } else if (newWidth < getAverageWidthForTwoCriterion(0, 1)
                && newWidth >= getAverageWidthForTwoCriterion(1, 2)) {
            setCurrentTVideoTimelineTickCriterionIndex(1);
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener.onOnBarScaledMode(1);
            }

        } else if (newWidth < getAverageWidthForTwoCriterion(1, 2)
                && newWidth >= getAverageWidthForTwoCriterion(2, 3)) {
            setCurrentTVideoTimelineTickCriterionIndex(2);
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener.onOnBarScaledMode(2);
            }

        } else if (newWidth < getAverageWidthForTwoCriterion(2, 3)
                && newWidth >= getAverageWidthForTwoCriterion(3, 4)) {
            setCurrentTVideoTimelineTickCriterionIndex(3);
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener.onOnBarScaledMode(3);
            }

        } else if (newWidth < getAverageWidthForTwoCriterion(3, 4)
                && newWidth >= timebarTickCriterionMap.get(4).getViewLength()) {
            setCurrentTVideoTimelineTickCriterionIndex(4);
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener.onOnBarScaledMode(4);
            }

        } else if (newWidth < timebarTickCriterionMap.get(4).getViewLength()) {
            setCurrentTVideoTimelineTickCriterionIndex(4);
            newWidth = timebarTickCriterionMap.get(4).getViewLength();
            if (mOnBarScaledListener != null) {
                mOnBarScaledListener.onOnBarScaledMode(4);
            }

        }

        if (scaleByClickButton) {
            justScaledByPressingButton = true;
        }


        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = newWidth;
        setLayoutParams(params);

    }

    public void setMode(int scalMode) {
        if (scalMode < ZOOMMIN || scalMode > ZOOMMAX || scalMode == currentTVideoTimelineTickCriterionIndex)
            return;

        switch (scalMode) {
            case 0:
                setCurrentTVideoTimelineTickCriterionIndex(0);
                int newWidth = timebarTickCriterionMap.get(0).getViewLength();
                justScaledByPressingButton = true;
                ViewGroup.LayoutParams params = getLayoutParams();
                params.width = newWidth;
                setLayoutParams(params);
                break;
            case 1:
                setCurrentTVideoTimelineTickCriterionIndex(1);
                int newWidth1 = timebarTickCriterionMap.get(1).getViewLength();
                justScaledByPressingButton = true;
                ViewGroup.LayoutParams params1 = getLayoutParams();
                params1.width = newWidth1;
                setLayoutParams(params1);
                break;
            case 2:
                setCurrentTVideoTimelineTickCriterionIndex(2);
                int newWidth2 = timebarTickCriterionMap.get(2).getViewLength();
                justScaledByPressingButton = true;
                ViewGroup.LayoutParams params2 = getLayoutParams();
                params2.width = newWidth2;
                setLayoutParams(params2);
                break;
            case 3:
                setCurrentTVideoTimelineTickCriterionIndex(3);
                int newWidth3 = timebarTickCriterionMap.get(3).getViewLength();
                justScaledByPressingButton = true;
                ViewGroup.LayoutParams params3 = getLayoutParams();
                params3.width = newWidth3;
                setLayoutParams(params3);
                break;
            case 4:
                setCurrentTVideoTimelineTickCriterionIndex(4);
                int newWidth4 = timebarTickCriterionMap.get(4).getViewLength();
                justScaledByPressingButton = true;
                ViewGroup.LayoutParams params4 = getLayoutParams();
                params4.width = newWidth4;
                setLayoutParams(params4);
                break;

        }
    }

    private float getAverageWidthForTwoCriterion(int criterion1Index, int criterion2Index) {
        int width1 = timebarTickCriterionMap.get(criterion1Index).getViewLength();
        int width2 = timebarTickCriterionMap.get(criterion2Index).getViewLength();
        return (width1 + width2) / 2;
    }

    private void initTVideoTimelineTickCriterionMap() {
        TVideoTimelineTickCriterion t0 = new TVideoTimelineTickCriterion();
        t0.setTotalSecondsInOneScreen(10 * 60);
        t0.setKeyTickInSecond(1 * 60);
        t0.setMinTickInSecond(6);
        t0.setDataPattern("HH:mm");
        t0.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t0.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(0, t0);

        /*TVideoTimelineTickCriterion t1 = new TVideoTimelineTickCriterion();
        t1.setTotalSecondsInOneScreen(60 * 60);
        t1.setKeyTickInSecond(10 * 60);
        t1.setMinTickInSecond(60);
        t1.setDataPattern("HH:mm");
        t1.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t1.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(1, t1);*/

        TVideoTimelineTickCriterion t1 = new TVideoTimelineTickCriterion();
        t1.setTotalSecondsInOneScreen(6 * 60);
        t1.setKeyTickInSecond(60);
        t1.setMinTickInSecond(6);
        t1.setDataPattern("HH:mm");
        t1.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t1.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(1, t1);

        /*TVideoTimelineTickCriterion t2 = new TVideoTimelineTickCriterion();
        t2.setTotalSecondsInOneScreen(6 * 60 * 60);
        t2.setKeyTickInSecond(60 * 60);
        t2.setMinTickInSecond(5 * 60);
        t2.setDataPattern("HH:mm");
        t2.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t2.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(2, t2);*/
        TVideoTimelineTickCriterion t2 = new TVideoTimelineTickCriterion();
        t2.setTotalSecondsInOneScreen(1 * 60 * 60);
        t2.setKeyTickInSecond(10 * 60);
        t2.setMinTickInSecond(1 * 60);
        t2.setDataPattern("HH:mm");
        t2.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t2.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(2, t2);

      /*  TVideoTimelineTickCriterion t3 = new TVideoTimelineTickCriterion();
        t3.setTotalSecondsInOneScreen(36 * 60 * 60);
        t3.setKeyTickInSecond(6 * 60 * 60);
        t3.setMinTickInSecond(30 * 60);
        t3.setDataPattern("HH:mm");
        t3.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t3.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(3, t3);*/

        TVideoTimelineTickCriterion t3 = new TVideoTimelineTickCriterion();
        t3.setTotalSecondsInOneScreen(30 * 60 * 60);
        t3.setKeyTickInSecond(6 * 60 * 60);
        t3.setMinTickInSecond(60 * 60);
        t3.setDataPattern("HH:mm");
        t3.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t3.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(3, t3);

        TVideoTimelineTickCriterion t4 = new TVideoTimelineTickCriterion();
        t4.setTotalSecondsInOneScreen(6 * 24 * 60 * 60);
        t4.setKeyTickInSecond(24 * 60 * 60);
        t4.setMinTickInSecond(2 * 60 * 60);
        t4.setDataPattern("MM.dd");
        // t4.dataPattern = "MM.dd HH:mm:ss";
        t4.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t4.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(4, t4);

        timebarTickCriterionCount = timebarTickCriterionMap.size();
    }

    private void resetToStandardWidth() {
        setCurrentTVideoTimelineTickCriterionIndex(3);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getViewLength();
        setLayoutParams(params);
    }

    public long getCurrentTimeInMillisecond() {
        return currentTimeInMillisecond;
    }

    public void setCurrentTimeInMillisecond(long currentTimeInMillisecond) {
        this.currentTimeInMillisecond = currentTimeInMillisecond;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            VIEW_HEIGHT = RxImageTool.dp2px(getContext(), VIEW_HEIGHT_IN_DP);
        } else {
            VIEW_HEIGHT = heightSize;
        }

        setMeasuredDimension(measureWidth(widthMeasureSpec), VIEW_HEIGHT);

        if (justScaledByPressingButton && mOnBarScaledListener != null) {
            justScaledByPressingButton = false;
            mOnBarScaledListener.onBarScaleFinish(getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), currentTimeInMillisecond);
        }


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        TLog.d("onSizeChanged", " w:" + w + " h:" + h + " oldw:" + oldh + " w:" + oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        TLog.d("onLayout", "changed:" + changed + " left:" + left + " top:" + top + " right:" + right + " bottom:" + bottom);

       /* if (currentTimeInMillisecond != System.currentTimeMillis() && left == 0)
            layout((int) (0 - (currentTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 * pixelsPerSecond),
                    getTop(),
                    getWidth() - (int) ((currentTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 * pixelsPerSecond),
                    getTop() + getHeight());*/
        super.onLayout(changed, left, top, right, bottom);

    }

    private int measureWidth(int widthMeasureSpec) {
        int measureMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureSize = MeasureSpec.getSize(widthMeasureSpec);
        int result = getSuggestedMinimumWidth();
        switch (measureMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = measureSize + screenWidth;
                pixelsPerSecond = measureSize / (float) WHOLE_TIMEBAR_TOTAL_SECONDS;
                if (mOnBarScaledListener != null) {
                    mOnBarScaledListener.onBarScaled(getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), currentTimeInMillisecond);
                }
                break;
            default:
                break;
        }
        TLog.d("measureWidth", "measureMode:" + measureMode + "measureSize:" + measureSize + " result" + result);
        return result;
    }

    private String getTimeStringFromLong(long value) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getDataPattern());
        return timeFormat.format(value);
    }

    public void setMiddleCursorVisible(boolean middleCursorVisible) {
        this.middleCursorVisible = middleCursorVisible;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        /*if (notInited) {
            notInited = false;
            resetToStandardWidth();
            return;
        }*/

        TLog.d("onDraw", "onDraw");

        pixelsPerSecond = (float) (getWidth() - screenWidth) / (float) WHOLE_TIMEBAR_TOTAL_SECONDS;


        Calendar cal = Calendar.getInstance();
        zoneOffsetInSeconds = cal.get(Calendar.ZONE_OFFSET) / 1000;
        long forStartUTC = (long) (currentTimeInMillisecond / 1000 - screenWidth / pixelsPerSecond / 2 - timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getMinTickInSecond());
        long forEndUTC = (long) (currentTimeInMillisecond / 1000 + screenWidth / pixelsPerSecond / 2 + timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getMinTickInSecond());

        long forStartLocalTimezone = forStartUTC + zoneOffsetInSeconds;
        long forEndLocalTimezone = forEndUTC + zoneOffsetInSeconds;


        for (long i = forStartLocalTimezone; i <= forEndLocalTimezone; i++) {
            if (i % timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getMinTickInSecond() == 0) {
                firstTickToSeeInSecondUTC = i - zoneOffsetInSeconds;
                break;

            }
        }


        // 画刻度及时间
        drawTick(canvas);

        // 画录像条
        drawRecord(canvas);


        // 画中间刻度
        drawmiddleCursor(canvas);

        layout((int) (0 - (currentTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 * pixelsPerSecond),
                getTop(),
                getWidth() - (int) ((currentTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 * pixelsPerSecond),
                getTop() + getHeight());


    }

    private void drawTick(Canvas canvas) {
        int totalTickToDrawInOneScreen = (int) (screenWidth / pixelsPerSecond / timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getMinTickInSecond()) + 2;
        float keytextY = getHeight() / 2;
        for (int i = -20; i <= totalTickToDrawInOneScreen + 10; i++) {
            long drawTickTimeInSecondUTC = firstTickToSeeInSecondUTC + i * timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getMinTickInSecond();
            long drawTickTimeInSecondLocalTimezone = drawTickTimeInSecondUTC + zoneOffsetInSeconds;

            if (drawTickTimeInSecondLocalTimezone % timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getKeyTickInSecond() == 0) {//关键刻度
                //画大刻度
                timebarPaint.setColor(linesColor);
                timebarPaint.setAntiAlias(true);
                timebarPaint.setStyle(Paint.Style.FILL);
                float startX = pixelsPerSecond * (drawTickTimeInSecondUTC - mostLeftTimeInMillisecond / 1000) + screenWidth / 2f;
                RectF largeTickRect = new RectF(startX - BIG_TICK_HALF_WIDTH / 2, getHeight() - BIG_TICK_HEIGHT, (startX + BIG_TICK_HALF_WIDTH / 2), getHeight());
                canvas.drawRect(largeTickRect, timebarPaint);
                RectF largeTickRect1 = new RectF(startX - BIG_TICK_HALF_WIDTH / 2, 0, (startX + BIG_TICK_HALF_WIDTH / 2), BIG_TICK_HEIGHT);
                canvas.drawRect(largeTickRect1, timebarPaint);

                //画时间文字
                String keytext = getTimeStringFromLong(drawTickTimeInSecondUTC * 1000);
                float keyTextWidth = keyTickTextPaint.measureText(keytext);
                float keytextX = startX - keyTextWidth / 2;
                canvas.drawText(keytext, keytextX, keytextY, keyTickTextPaint);
            } else if (drawTickTimeInSecondLocalTimezone % timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getMinTickInSecond() == 0) {
                //小刻度
                timebarPaint.setColor(linesColor);
                timebarPaint.setAntiAlias(true);
                timebarPaint.setStyle(Paint.Style.FILL);
                float startX = pixelsPerSecond * (drawTickTimeInSecondUTC - mostLeftTimeInMillisecond / 1000) + screenWidth / 2f;
                RectF smallTickRect = new RectF(startX - SMALL_TICK_HALF_WIDTH / 2, getHeight() - SMALL_TICK_HEIGHT, (startX + SMALL_TICK_HALF_WIDTH / 2), getHeight());
                canvas.drawRect(smallTickRect, timebarPaint);

                RectF smallTickRect1 = new RectF(startX - SMALL_TICK_HALF_WIDTH / 2, 0, (startX + SMALL_TICK_HALF_WIDTH / 2), SMALL_TICK_HEIGHT);
                canvas.drawRect(smallTickRect1, timebarPaint);
            }

        }

        canvas.drawLine(0, 0, getWidth(), 0, timebarPaint);
        canvas.drawLine(0, VIEW_HEIGHT, getWidth(), VIEW_HEIGHT, timebarPaint);
    }

    private void drawRecord(Canvas canvas) {
        //录像从哪个时间点开始，单位是毫秒
        long startDrawTimeInSeconds = firstTickToSeeInSecondUTC + (-20) * timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getMinTickInSecond();

        if (recordDataExistTimeClipsList != null && recordDataExistTimeClipsList.size() > 0) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startDrawTimeDateString = dateFormat.format(startDrawTimeInSeconds * 1000);
            String zeroTimeString = startDrawTimeDateString + " 00:00:00";

            long screenLastSecondToSee = (long) (startDrawTimeInSeconds + screenWidth / pixelsPerSecond + 30 * timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getMinTickInSecond()) * 1000L;

            Date startDate;
            try {

                startDate = zeroTimeFormat.parse(zeroTimeString);
                List<TRecordDataExistTimeSection> startList = recordDataExistTimeClipsListMap.get(startDate.getTime());
                if (startList == null) {
                    int afterFindDays = 1;
                    long findTimeInMilliseconds = startDate.getTime();
                    long newFindStartMilliseconds = findTimeInMilliseconds;
                    while (startList == null && newFindStartMilliseconds < screenLastSecondToSee) {
                        newFindStartMilliseconds = findTimeInMilliseconds + (long) SECONDS_PER_DAY * 1000L * (long) afterFindDays;
                        startList = recordDataExistTimeClipsListMap.get(newFindStartMilliseconds);
                        afterFindDays++;
                    }
                }

                if (startList != null && startList.size() > 0) {
                    int thisDateFirstClipStartIndex = recordDataExistTimeClipsList.indexOf(startList.get(0));

                    long endDrawTimeInSeconds = (long) (startDrawTimeInSeconds
                            + screenWidth / pixelsPerSecond
                            + timebarTickCriterionMap.get(currentTVideoTimelineTickCriterionIndex).getMinTickInSecond() * 30);

                    timebarPaint.setColor(recordBackgroundColor);
                    timebarPaint.setStyle(Paint.Style.FILL);

                    for (int i = thisDateFirstClipStartIndex; i < recordDataExistTimeClipsList.size(); i++) {
                        float leftX = pixelsPerSecond * (recordDataExistTimeClipsList.get(i).getStartTimeInMillisecond() - mostLeftTimeInMillisecond) / 1000 + screenWidth / 2f;
                        float rightX = pixelsPerSecond * (recordDataExistTimeClipsList.get(i).getEndTimeInMillisecond() - mostLeftTimeInMillisecond) / 1000 + screenWidth / 2f;
                        RectF rectF = new RectF(leftX, 0, rightX, getHeight());
                        canvas.drawRect(rectF, timebarPaint);
                        if (recordDataExistTimeClipsList.get(i).getEndTimeInMillisecond() >= endDrawTimeInSeconds * 1000) {
                            break;
                        }
                    }
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawmiddleCursor(Canvas canvas) {
        if (middleCursorVisible) {
            timebarPaint.setStyle(Paint.Style.FILL);
            timebarPaint.setColor(middleCursorColor);
            int currentCursor = (int) ((currentTimeInMillisecond / 1000L - mostLeftTimeInMillisecond / 1000L) * pixelsPerSecond + screenWidth / 2f - TRIANGLE_LENGTH / 2);
            lastMmiddlecursor = currentCursor;
            // TLog.d("TIMEBARVIEW", "currentCursor" + currentCursor + " viewWidth:" + getWidth());
            //path.rMoveTo(currentCursor, 0);
            // 画三角形
            path = new Path();
            path.moveTo(currentCursor, 0);
            path.lineTo(currentCursor + TRIANGLE_LENGTH, 0);
            // 求三角形高
            float length = (float) Math.sqrt(3d) * TRIANGLE_LENGTH / 2;
            path.lineTo(currentCursor + TRIANGLE_LENGTH / 2, length);
            path.lineTo(currentCursor, 0);
            canvas.drawPath(path, timebarPaint);
            // 画三角形下面的线条
            canvas.drawLine(currentCursor + TRIANGLE_LENGTH / 2, 0, currentCursor + TRIANGLE_LENGTH / 2, VIEW_HEIGHT, timebarPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        scaleGestureDetector.onTouchEvent(event);

        if (scaleGestureDetector.isInProgress()) {
            return true;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                if (handler.hasMessages(ACTION_UP))
                    handler.removeMessages(ACTION_UP);

                // 先记录进度条移动状态 如果进度条正在移动 先停止
                lastMoveState = moveFlag;
                lastCheckState = checkVideo;
                checkVideo = readyCheck;
                closeMove();
                lastcurrentTimeInMillisecond = currentTimeInMillisecond;
                mode = DRAG;
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG && mDrag) {
                    int dx = (int) (event.getRawX() - lastX);
                    if (dx == 0) {
                        return false;
                    }
                    int top = getTop();
                    TLog.d("*****onTouchEvent", "  dx" + dx + " left" + getLeft() + " right" + getLeft() + getWidth());
                    int left = getLeft() + dx;
                    int right = left + getWidth();

                    if (left >= 0) {
                        left = 0;
                        right = getWidth();
                    }

                    if (right < screenWidth) {
                        right = screenWidth;
                        left = right - getWidth();
                    }
                    layout(left, top, right, top + getHeight());
                    invalidate();

                    lastX = event.getRawX();
                    lastY = event.getRawY();

                    int deltaX = (0 - left);
                    int timeBarLength = getWidth() - screenWidth;
                    currentTimeInMillisecond = mostLeftTimeInMillisecond + deltaX * WHOLE_TIMEBAR_TOTAL_SECONDS * 1000 / timeBarLength;

                    if (mOnBarMoveListener != null) {
                        mOnBarMoveListener.onBarMove(getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), currentTimeInMillisecond);
                    }

                }
                break;
            case MotionEvent.ACTION_CANCEL:
                currentTimeInMillisecond = lastcurrentTimeInMillisecond;
                checkVideo = lastCheckState;
                if (mOnBarMoveListener != null) {
                    mOnBarMoveListener.onBarMove(getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), currentTimeInMillisecond);
                }
                invalidate();
               /* if (lastMoveState) {
                    if (handler.hasMessages(MOVEING))
                        handler.removeMessages(MOVEING);
                    handler.sendEmptyMessageDelayed(MOVEING, 1100);
                }*/
                mode = NONE;
                break;
            case MotionEvent.ACTION_UP:
                if (mode == DRAG) {
                    int deltaX_up = (0 - getLeft());
                    int timeBarLength_up = getWidth() - screenWidth;
                    currentTimeInMillisecond = mostLeftTimeInMillisecond + deltaX_up * WHOLE_TIMEBAR_TOTAL_SECONDS * 1000 / timeBarLength_up;
                    //invalidate();
                    if (handler.hasMessages(ACTION_UP))
                        handler.removeMessages(ACTION_UP);
                    handler.sendEmptyMessageDelayed(ACTION_UP, 1100);
                    /*if (lastMoveState) {
                        if (handler.hasMessages(MOVEING))
                            handler.removeMessages(MOVEING);
                        handler.sendEmptyMessageDelayed(MOVEING, 1100);
                    }*/

                }
                mode = NONE;
                break;
        }


        return true;
    }

    public void scaleByPressingButton(boolean zoomIn) {

        //当前所在刻度标准的默认长度（不含两端空出的screenWidth）
        int currentCriterionViewLength = timebarTickCriterionMap.get(getCurrentTVideoTimelineTickCriterionIndex()).getViewLength();

        int currentViewLength = getWidth() - screenWidth;

        if (currentViewLength == currentCriterionViewLength) {
            if (zoomIn) {
                int newCriteriaIndex = getCurrentTVideoTimelineTickCriterionIndex() - 1;
                if (newCriteriaIndex < ZOOMMIN || newCriteriaIndex > ZOOMMAX) {
                    return;
                } else {
                    setCurrentTVideoTimelineTickCriterionIndex(newCriteriaIndex);
                    int newWidth = timebarTickCriterionMap.get(newCriteriaIndex).getViewLength();
                    justScaledByPressingButton = true;

                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.width = newWidth;
                    setLayoutParams(params);
                }
            } else {
                int newCriteriaIndex = getCurrentTVideoTimelineTickCriterionIndex() + 1;
                // TLog.d("newCriteriaIndex", newCriteriaIndex + "");
                if (newCriteriaIndex > ZOOMMAX || newCriteriaIndex >= timebarTickCriterionCount) {
                    return;
                } else {
                    setCurrentTVideoTimelineTickCriterionIndex(newCriteriaIndex);
                    int newWidth = timebarTickCriterionMap.get(newCriteriaIndex).getViewLength();
                    justScaledByPressingButton = true;

                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.width = newWidth;
                    setLayoutParams(params);
                }
            }
        } else {
            if (currentViewLength > currentCriterionViewLength) {

                if (zoomIn) {
                    int newCriteriaIndex = getCurrentTVideoTimelineTickCriterionIndex() - 1;
                    if (newCriteriaIndex < 0) {
                        return;
                    } else {
                        setCurrentTVideoTimelineTickCriterionIndex(newCriteriaIndex);
                        int newWidth = timebarTickCriterionMap.get(newCriteriaIndex).getViewLength();
                        justScaledByPressingButton = true;

                        ViewGroup.LayoutParams params = getLayoutParams();
                        params.width = newWidth;
                        setLayoutParams(params);
                    }
                } else {
                    int newWidth = timebarTickCriterionMap.get(getCurrentTVideoTimelineTickCriterionIndex()).getViewLength();
                    justScaledByPressingButton = true;

                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.width = newWidth;
                    setLayoutParams(params);
                }

            } else {

                if (zoomIn) {
                    int newWidth = timebarTickCriterionMap.get(getCurrentTVideoTimelineTickCriterionIndex()).getViewLength();
                    justScaledByPressingButton = true;

                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.width = newWidth;
                    setLayoutParams(params);


                } else {
                    int newCriteriaIndex = getCurrentTVideoTimelineTickCriterionIndex() + 1;
                    if (newCriteriaIndex >= timebarTickCriterionCount) {
                        return;
                    } else {
                        setCurrentTVideoTimelineTickCriterionIndex(newCriteriaIndex);

                        int newWidth = timebarTickCriterionMap.get(newCriteriaIndex).getViewLength();
                        justScaledByPressingButton = true;

                        ViewGroup.LayoutParams params = getLayoutParams();
                        params.width = newWidth;
                        setLayoutParams(params);
                    }
                }

            }
        }


    }

    public void setOnBarMoveListener(OnBarMoveListener onBarMoveListener) {
        mOnBarMoveListener = onBarMoveListener;
    }

    public void setOnBarScaledListener(OnBarScaledListener onBarScaledListener) {
        mOnBarScaledListener = onBarScaledListener;
    }

    public void openMove() {
        if (!moveIng) {
            moveFlag = true;
            moThread = null;
            moThread = new MoveThread();
            moThread.start();
        }
    }

    public void closeMove() {
        moveFlag = false;
        moThread = null;
    }

    public boolean isMoveing() {
        return moveFlag;
    }

    public void setMoveFlag(boolean moveFlag) {
        this.moveFlag = moveFlag;
    }

    /*
     *
     * 设置是否检查有录像
     *
     * */
    public void checkVideo(boolean check) {
        readyCheck = check;
    }

    /*
     * 返回下一个录像开始点
     * */
    private long locationVideo() {
        if (recordDataExistTimeClipsList == null)
            return -1;
        int size = recordDataExistTimeClipsList.size();
        for (int i = 0; i < size - 1; i++) {
            long lastEndTime = recordDataExistTimeClipsList.get(i).getEndTimeInMillisecond();
            long nextStartTime = recordDataExistTimeClipsList.get(i + 1).getStartTimeInMillisecond();
            if (currentTimeInMillisecond > lastEndTime && currentTimeInMillisecond < nextStartTime) {
                return nextStartTime;
            }
        }
        return -1;
    }

    /*判断是否有录像*/
    private boolean checkHasVideo() {
        if (recordDataExistTimeClipsList != null && recordDataExistTimeClipsList.size() > 0) {
            for (TRecordDataExistTimeSection recordInfo : recordDataExistTimeClipsList) {
                if (recordInfo.getStartTimeInMillisecond() <= currentTimeInMillisecond
                        && currentTimeInMillisecond <= recordInfo.getEndTimeInMillisecond())
                    return true;
            }
        }
        return false;
    }

    public void recycle() {
        closeMove();
        if (recordDataExistTimeClipsList != null) {
            recordDataExistTimeClipsList.clear();
            recordDataExistTimeClipsList = null;
        }
        if (recordDataExistTimeClipsListMap != null) {
            recordDataExistTimeClipsListMap.clear();
            recordDataExistTimeClipsListMap = null;
        }
        mOnBarMoveListener = null;
        mOnBarScaledListener = null;
        timebarPaint = null;
        scaleGestureDetector = null;
    }

    public int getIdTag() {
        return idTag;
    }

    public void setIdTag(int idTag) {
        this.idTag = idTag;
    }

    // 设置是否允许拖动
    public void setDrag(boolean mDrag) {
        this.mDrag = mDrag;
    }

    public interface OnBarMoveListener {

        void onBarMove(long screenLeftTime, long screenRightTime, long currentTime);

        void OnBarMoveFinish(long screenLeftTime, long screenRightTime, long currentTime);
    }

    public interface OnBarScaledListener {

        void onOnBarScaledMode(int mode);

        void onBarScaled(long screenLeftTime, long screenRightTime, long currentTime);


        void onBarScaleFinish(long screenLeftTime, long screenRightTime, long currentTime);
    }

    private class MoveThread extends Thread {
        @Override
        public void run() {
            TLog.d("MOVETHREAD", "thread is start");
            moveIng = true;
            while (moveFlag) {
                try {
                    Thread.sleep(1000);
                    TLog.d("MOVETHREAD", "thread is running");
                    currentTimeInMillisecond += 1000;
                    if (checkVideo) {
                        if (!checkHasVideo()) {
                            long nextStartTime = locationVideo();
                            if (nextStartTime != -1) {
                                currentTimeInMillisecond = nextStartTime;
                            } else {
                                currentTimeInMillisecond -= 1000;
                                moveFlag = false;
                                moveIng = false;
                                break;
                            }
                        }
                    }
                    postInvalidate();
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnBarMoveListener != null) {
                                mOnBarMoveListener.onBarMove(getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), currentTimeInMillisecond);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    moveIng = false;
                    e.printStackTrace();
                }
            }
            moveIng = false;
            TLog.d("MOVETHREAD", "thread is stop");
        }
    }
}

