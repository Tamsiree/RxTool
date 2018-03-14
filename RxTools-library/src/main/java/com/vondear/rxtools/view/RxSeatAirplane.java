package com.vondear.rxtools.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.vondear.rxtools.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author vondear
 * @date 16/7/22
 */


public class RxSeatAirplane extends View {


    private Paint mPaint;
    private Paint mPaintOther;
    private Paint mPaintMap;


    private RectF rectFCabin;
    RectF rectFWall;
    RectF rectFWC;

    private HashMap<String, RectF> mSeats
            = new HashMap<>();

    private HashMap<String, SeatState> mSeatSelecting
            = new HashMap<>();

    private HashMap<String, SeatState> mSeatSelected
            = new HashMap<>();

    private HashMap<String, RectF> mSeatSelectingRectF = new HashMap<>();


    private Path pathFuselage;
    private Path pathArrow;
    private Path pathTail;

    Bitmap mBitmapCabin = null;

    Bitmap mBitmapFuselage = null;
    Bitmap mBitmapArrow = null;
    Bitmap mBitmapTail = null;
    Bitmap mBitmapSeat_normal = null;
    Bitmap mBitmapSeat_selected = null;
    Bitmap mBitmapSeat_selecting = null;

    float scaleValue = 2.0f;
    float scaleMaxValue = 3f;
    float scaleMap = 10f;
    private int maxSelectStates = 10;
    float moveY = 0;


    private String getSeatKeyName(int row, int column) {
        return String.valueOf(row + "#" + column);
    }


    public void setSeatSelected(int row, int column) {

        mSeatSelected.put(getSeatKeyName(row, column), SeatState.Selected);
    }


    public void goCabinPosition(CabinPosition mCabinPosition) {
        if (mAnimatedValue > 0) {

            if (mCabinPosition == CabinPosition.Top) {
                moveY = 0;

            } else if (mCabinPosition == CabinPosition.Last) {
                moveY = rectFCabin.height() - rectFCabin.width() * 2.5f;
            } else if (mCabinPosition == CabinPosition.Middle) {
                moveY = (rectFCabin.height() - rectFCabin.width() * 2.5f) / 2;
            }
            invalidate();


        }
    }


    public void setEmptySelecting() {
        mSeatSelecting.clear();
        mSeatSelectingRectF.clear();
        invalidate();
    }

    public void setMaxSelectStates(int count) {
        maxSelectStates = count;
    }


    public RxSeatAirplane(Context context) {
        this(context, null);
    }

    public RxSeatAirplane(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxSeatAirplane(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST
                && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(dip2px(150), dip2px(200));
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) (heightSpecSize * 0.75f), heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) (widthSpecSize / 0.75f));
        }
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);


        mPaintOther = new Paint();
        mPaintOther.setAntiAlias(true);
        mPaintOther.setColor(Color.rgb(138, 138, 138));
        mPaintOther.setStyle(Paint.Style.FILL);
        mPaintMap = new Paint();
        mPaintMap.setAntiAlias(true);
        mPaintMap.setColor(Color.rgb(138, 138, 138));
        mPaintMap.setStyle(Paint.Style.FILL);

        rectFCabin = new RectF();
        rectFWall = new RectF();
        rectFWC = new RectF();

        pathFuselage = new Path();
        pathFuselage.reset();
        pathArrow = new Path();
        pathArrow.reset();
        pathTail = new Path();
        pathTail.reset();

        setOnTouchListener(new MoveListerner((Activity) getContext()) {
            @Override
            public void moveDirection(View v, int direction, float distanceX, float distanceY) {


            }

            @Override
            public void moveUpAndDownDistance(MotionEvent event, int distance, int distanceY) {

//                System.out.println("-----moveUpAndDownDistance:" + distance + "----:" + distanceY);

                if (mAnimatedValue > 0) {

                    if (moveY >= 0 && moveY <= rectFCabin.height() - rectFCabin.width() * 2.5f) {
                        moveY = moveY + distanceY;
                        invalidate();
                    }
                    if (moveY < 0) {
                        moveY = 0;
                    }
                    if (moveY > rectFCabin.height() - rectFCabin.width() * 2.5f) {
                        moveY = rectFCabin.height() - rectFCabin.width() * 2.5f;
                    }
                }


            }

            @Override
            public void moveOver() {
//                System.out.println("-----moveOver:");

            }

            @Override
            public void Touch(float x, float y) {
                if (mAnimatedValue == 0) {
                    startAnim(false);
                } else {


                    RectF selecting = new RectF();


                    Iterator iter = mSeats.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = (String) entry.getKey();
                        RectF val = (RectF) entry.getValue();
//                        x=x-moveY;


                        if (val.contains(x, y)) {
//                            System.out.println("----key" + key);
                            if (mSeatSelecting.containsKey(key)) {

                                if (mSeatSelecting.get(key) != SeatState.Selected) {
                                    mSeatSelecting.remove(key);
                                    mSeatSelectingRectF.remove(key);
                                    invalidate();
                                }


                            } else {
                                if (mSeatSelecting.size()
                                        >= maxSelectStates) {
                                    Toast.makeText(getContext(),
                                            "Choose a maximum of " + maxSelectStates,
                                            Toast.LENGTH_SHORT).show();
                                    return;

                                } else {
                                    if (!mSeatSelected.containsKey(key)) {
                                        mSeatSelecting.put(key,
                                                SeatState.Selecting);

                                        selecting.top = val.top / scaleMap
                                                + rectFCabin.top + rectFCabin.width() * 0.8f
                                                + moveY / scaleMap;
                                        selecting.bottom = val.bottom / scaleMap
                                                + rectFCabin.top + rectFCabin.width() * 0.8f
                                                + moveY / scaleMap;
                                        selecting.left = val.centerX() / scaleMap - val.width() / scaleMap / 2f
                                                - val.width() / scaleMap / 2f
                                                + rectFCabin.left;
                                        selecting.right = val.centerX() / scaleMap + val.width() / scaleMap / 2f
                                                - val.width() / scaleMap / 2f
                                                + rectFCabin.left;


                                        mSeatSelectingRectF.put(key, selecting);

                                        invalidate();

                                    }
//                                    else
//                                    {
//                                        Toast.makeText(getContext(),
//                                               "The selected",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
                                }
                            }

                        }


                    }


                }

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        scaleValue = scaleMaxValue * mAnimatedValue;


        Matrix matrix = new Matrix();
        matrix.postScale(1 + scaleValue * 2f, 1 + scaleValue * 2f);
        matrix.postTranslate(getMeasuredWidth() * -1 * scaleValue,
                scaleValue * 2f * (getMeasuredHeight() / 3f / 3f + getMeasuredWidth() / 8f) * -1 - moveY);
        float rectFCabinWidth = getMeasuredWidth() / 8f;
        rectFCabin.top = getMeasuredHeight() / 3f / 3f;
        rectFCabin.left = getMeasuredWidth() / 2f - rectFCabinWidth / 2f;
        rectFCabin.right = getMeasuredWidth() / 2f + rectFCabinWidth / 2f;
        rectFCabin.bottom = getMeasuredHeight() / 3f / 3f + getMeasuredHeight() / 3f * 2f;
        canvas.drawBitmap(getBitmapFuselage(rectFCabinWidth), matrix, mPaint);
        canvas.drawBitmap(getBitmapArrow(), matrix, mPaint);
        canvas.drawBitmap(getBitmapTail(), matrix, mPaint);
        canvas.drawBitmap(getBitmapCabin(), matrix, mPaint);
        rectFCabinWidth = getMeasuredWidth() / 8f * (1 + scaleValue * 2f);
        rectFCabin.top = getMeasuredHeight() / 3f / 3f;
        rectFCabin.left = getMeasuredWidth() / 2f - rectFCabinWidth / 2f;
        rectFCabin.right = getMeasuredWidth() / 2f + rectFCabinWidth / 2f;
        rectFCabin.bottom = getMeasuredHeight() / 3f / 3f + getMeasuredHeight() / 3f * 2f * (1 + scaleValue * 2f);
        canvas.translate(0,
                scaleValue * (1 - 0.1f * (scaleMaxValue - 2))
                        * (getMeasuredHeight() / 3f / 3f + getMeasuredWidth() / 8f) * -1 - moveY);
        drawSeatFirst(canvas);
        drawSeatTourist(canvas);
        drawSeatLast(canvas);
        drawSeatMap(canvas);
        canvas.restore();


    }


    public enum CabinPosition {
        Top, Middle, Last
    }

    enum SeatType {
        Left, Middle, Right
    }

    enum SeatState {
        Normal, Selected, Selecting
    }

    enum CabinType {
        Frist, Second, Tourist, Last
    }


    private void setSeat(int i, int j, Canvas canvas, float seatWH, SeatType type
            , CabinType cabinType) {
        float top = 0f;
        float left = 0f;
        if (cabinType == CabinType.Frist) {
            if (type == SeatType.Left) {
                top = rectFCabin.top + rectFCabin.width() / 2 +
                        seatWH + i * (seatWH) + seatWH / 2;

                left = rectFCabin.left + j * (seatWH) + seatWH / 3;
            } else if (type == SeatType.Middle) {
                top = rectFCabin.top + rectFCabin.width() / 2 +
                        seatWH + i * (seatWH) + seatWH / 2 + seatWH / 2;
                left = rectFCabin.left + j * (seatWH) + seatWH * 1f;
            } else if (type == SeatType.Right) {
                top = rectFCabin.top + rectFCabin.width() / 2 +
                        seatWH + i * (seatWH) + seatWH / 2;
                left = rectFCabin.left + j * (seatWH) + seatWH * 2f
                        - seatWH / 3f;
            }
        } else if (cabinType == CabinType.Second) {
            if (type == SeatType.Left) {

                left = rectFCabin.left + j * (seatWH) + seatWH / 3;
                top = rectFCabin.top + seatWH * 14f
                        + rectFCabin.width() / 2 +
                        seatWH + i * (seatWH) + seatWH / 2;


            } else if (type == SeatType.Middle) {


                left = rectFCabin.left + j * (seatWH) + seatWH / 1f;
                top = rectFCabin.top + rectFCabin.width() / 2
                        + seatWH * 14f +
                        seatWH + i * (seatWH) + seatWH / 2 + seatWH / 2;
            } else if (type == SeatType.Right) {


                left = rectFCabin.left + j * (seatWH) + seatWH * 2f
                        - seatWH / 3f;
                top = rectFCabin.top
                        + seatWH * 14f
                        + rectFCabin.width() / 2 +
                        seatWH + i * (seatWH) + seatWH / 2;


            }
        } else if (cabinType == CabinType.Tourist) {
            if (type == SeatType.Left) {
                left = rectFCabin.left + j * (seatWH)
                        + seatWH / 3;
                top = rectFWall.bottom
                        + seatWH * 1.5f
                        +
                        i * (seatWH);


            } else if (type == SeatType.Middle) {
                left = rectFCabin.left + j * (seatWH)
                        + seatWH * 1f
                ;
                top = rectFWall.bottom
                        + seatWH * 1.5f
                        +
                        i * (seatWH);


            } else if (type == SeatType.Right) {
                left = rectFCabin.left + j * (seatWH)
                        + seatWH * 2.0f -
                        seatWH / 3f
                ;
                top = rectFWall.bottom
                        + seatWH * 1.5f
                        +
                        i * (seatWH);


            }
        } else if (cabinType == CabinType.Last) {
            if (type == SeatType.Left) {


                left = rectFCabin.left + j * (seatWH)
                        + seatWH / 3
                ;
                top = rectFWC.bottom
                        + seatWH * 1.5f
                        +
                        i * (seatWH);


            } else if (type == SeatType.Middle) {

                left = rectFCabin.left + j * (seatWH)
                        + seatWH * 1f
                ;
                top = rectFWC.bottom
                        + seatWH * 1.5f
                        +
                        i * (seatWH);


            } else if (type == SeatType.Right) {

                left = rectFCabin.left + j * (seatWH)
                        + seatWH * 2.0f -
                        seatWH / 3f
                ;
                top = rectFWC.bottom
                        + seatWH * 1.5f
                        +
                        i * (seatWH);


            }
        }


        RectF sRectF = new RectF();

        sRectF = new RectF(left, top,
                left + seatWH, top + seatWH);
        PointSeat point = null;
        if (cabinType == CabinType.Frist) {
            point = new PointSeat(i, j);
        } else if (cabinType == CabinType.Second) {
            point = new PointSeat(i + 7, j);

        } else if (cabinType == CabinType.Tourist) {
            point = new PointSeat(i + 10, j);

        } else if (cabinType == CabinType.Last) {
            point = new PointSeat(i + 35, j);

        }


        if (mAnimatedValue == 1) {
            if (cabinType == CabinType.Frist) {

                if (type == SeatType.Left || type == SeatType.Right) {

                    sRectF.top = sRectF.top - rectFCabin.top - rectFCabin.width() / 2
                            - seatWH * (scaleMaxValue - 1.51f) - moveY;
                    sRectF.bottom = sRectF.bottom - rectFCabin.top - rectFCabin.width() / 2
                            - seatWH * (scaleMaxValue - 1.51f) - moveY;

                }
                if (type == SeatType.Middle) {

                    sRectF.top = sRectF.top - rectFCabin.top - rectFCabin.width() / 2
                            - seatWH * (scaleMaxValue - 1.8f) - seatWH / 2f - moveY;
                    sRectF.bottom = sRectF.bottom - rectFCabin.top - rectFCabin.width() / 2
                            - seatWH * (scaleMaxValue - 1.8f) - seatWH / 2f - moveY;

                }


            } else if (cabinType == CabinType.Second) {


                if (type == SeatType.Left || type == SeatType.Right) {

                    sRectF.top = sRectF.top - rectFCabin.top - rectFCabin.width() / 2
                            - seatWH * (scaleMaxValue - 1.25f) - moveY;
                    sRectF.bottom = sRectF.bottom - rectFCabin.top - rectFCabin.width() / 2
                            - seatWH * (scaleMaxValue - 1.25f) - moveY;

                }
                if (type == SeatType.Middle) {

                    sRectF.top = sRectF.top - rectFCabin.top - rectFCabin.width() / 2
                            - seatWH * (scaleMaxValue - 1.75f) - seatWH / 2f - moveY;
                    sRectF.bottom = sRectF.bottom - rectFCabin.top - rectFCabin.width() / 2
                            - seatWH * (scaleMaxValue - 1.75f) - seatWH / 2f - moveY;

                }


//                sRectF.top = sRectF.top - rectFCabin.top - rectFCabin.width() / 2 - seatWH * (scaleMaxValue - 1) - moveY;
//                sRectF.bottom = sRectF.bottom - rectFCabin.top - rectFCabin.width() / 2 - seatWH * (scaleMaxValue - 1) - moveY;
            } else if (cabinType == CabinType.Tourist) {
                sRectF.top = sRectF.top - rectFCabin.top - rectFCabin.width() / 2 - seatWH * (scaleMaxValue - 1) - moveY;
                sRectF.bottom = sRectF.bottom - rectFCabin.top - rectFCabin.width() / 2 - seatWH * (scaleMaxValue - 1) - moveY;

            } else if (cabinType == CabinType.Last) {
                sRectF.top = sRectF.top - rectFCabin.top - rectFCabin.width() / 2 - seatWH * (scaleMaxValue - 1) - moveY;
                sRectF.bottom = sRectF.bottom - rectFCabin.top - rectFCabin.width() / 2 - seatWH * (scaleMaxValue - 1) - moveY;

            }

            if (sRectF.top > 0 && sRectF.bottom < getMeasuredHeight()) {
                mSeats.put(getSeatKeyName(point.row, point.column), sRectF);
            }


        }


        if (mSeatSelected.containsKey(getSeatKeyName(point.row, point.column))) {


            canvas.drawBitmap(getSeat(seatWH, mSeatSelected.get(getSeatKeyName(point.row, point.column)))
                    , left
                    ,
                    top
                    ,
                    mPaint
            );
        } else if (mSeatSelecting.containsKey(getSeatKeyName(point.row, point.column))) {
//            System.out.println("-----" + point.row + "--" + point.column);

            canvas.drawBitmap(getSeat(seatWH, mSeatSelecting.get(getSeatKeyName(point.row, point.column)))
                    , left
                    ,
                    top
                    ,
                    mPaint


            );

            if (mAnimatedValue == 1) {
                if (mSeatSelecting.get(getSeatKeyName(point.row, point.column)) == SeatState.Selecting) {
                    String text = (point.row + 1) + "," + (point.column + 1);
                    mPaintOther.setColor(Color.WHITE);
                    mPaintOther.setTextSize(seatWH / 4f);
                    canvas.drawText(
                            text, left + seatWH / 2f - getFontlength(mPaintOther, text) / 2,
                            top + seatWH / 2f + getFontHeight(mPaintOther, text) / 3,
                            mPaintOther
                    );
                    mPaintOther.setColor(Color.rgb(138, 138, 138));

                }
            }
        } else {
            canvas.drawBitmap(getSeat(seatWH, SeatState.Normal)
                    , left
                    ,
                    top
                    ,
                    mPaint

            );


        }
    }


    private void drawSeatMap(Canvas canvas) {
        if (mAnimatedValue == 1) {


            float mapW = rectFCabin.width() / scaleMap;
            float mapH = (rectFCabin.height() - rectFCabin.width() * 2.5f) / scaleMap
                    + getMeasuredHeight() / scaleMap;
            RectF rectFMap = new RectF(rectFCabin.left, rectFCabin.top + rectFCabin.width() * 0.8f + moveY
                    , rectFCabin.left + mapW, rectFCabin.top + rectFCabin.width() * 0.8f + mapH + moveY);
            mPaintMap.setColor(Color.rgb(138, 138, 138));
            mPaintMap.setAlpha(80);

            mPaintMap.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectFMap, mPaintMap);


            mapH = getHeight() / scaleMap;

            RectF rectFMapSee = new RectF(rectFCabin.left,
                    rectFCabin.top + rectFCabin.width() * 0.8f + moveY + moveY / scaleMap
                    , rectFCabin.left + mapW,
                    rectFCabin.top + rectFCabin.width() * 0.8f + mapH + moveY + moveY / scaleMap);
            mPaintMap.setStyle(Paint.Style.STROKE);
            mPaintMap.setStrokeWidth(dip2px(0.75f));
            mPaintMap.setColor(Color.RED);

            canvas.drawRect(rectFMapSee, mPaintMap);

            mPaintMap.setStrokeWidth(0);

            if (mSeatSelectingRectF.size() > 0) {
                mPaintMap.setStyle(Paint.Style.FILL);
                mPaintMap.setColor(Color.RED);
                mPaintMap.setAlpha(80);
                RectF r = new RectF();

                Iterator iter = mSeatSelectingRectF.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    RectF val = (RectF) entry.getValue();


                    r.top = val.top + moveY;
                    r.bottom = val.bottom + moveY;
                    r.left = val.left - dip2px(0.5f);
                    r.right = val.right - dip2px(0.5f);


                    canvas.drawRect(r, mPaintMap);


                }


            }


        }

    }


    private void drawSeatFirst(Canvas canvas) {

        int row = 7;
        int column = 7;
        mSeats.clear();

        float seatWH = (float) (rectFCabin.width() / 9.0f);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i >= 0 && j < 2) {

                    setSeat(i, j, canvas, seatWH, SeatType.Left, CabinType.Frist);
//                    setBitmap

                } else if (j >= 2 && j < 5 && i < row - 1) {
                    setSeat(i, j, canvas, seatWH, SeatType.Middle, CabinType.Frist);


                } else if (j >= 5) {

                    setSeat(i, j, canvas, seatWH, SeatType.Right, CabinType.Frist);

                }


            }

        }

//        RectF rectFWC = new RectF();

        rectFWC.top = rectFCabin.top + rectFCabin.width() / 2 +
                seatWH + (row + 2.5f) * (seatWH) + seatWH / 2;

        rectFWC.bottom = rectFCabin.top + rectFCabin.width() / 2 +
                seatWH + (row + 4.5f) * (seatWH) + seatWH / 2;


        rectFWC.left = rectFCabin.left + seatWH / 3;
        rectFWC.right = rectFCabin.left + seatWH / 3 + seatWH * 2


        ;
        mPaintOther.setStyle(Paint.Style.STROKE);

        canvas.drawRect(rectFWC, mPaintOther);


        drawWcText(rectFWC, canvas);


        RectF rectFWifi = new RectF();


        rectFWifi.top = rectFCabin.top + rectFCabin.width() / 2 +
                seatWH + (row + 1f) * (seatWH) + seatWH / 2;
        rectFWifi.bottom = rectFCabin.top + rectFCabin.width() / 2 +
                seatWH + (row + 4.5f) * (seatWH) + seatWH / 2;

        rectFWifi.left = rectFWC.right + seatWH / 2f;

        rectFWifi.right = rectFCabin.left + column * (seatWH) + seatWH * 2f
                - seatWH / 3f - seatWH * 2 - seatWH / 2f;

        canvas.drawRect(rectFWifi, mPaintOther);


        drawWifiLogo(rectFWifi, canvas);


        rectFWC.top = rectFCabin.top + rectFCabin.width() / 2 +
                seatWH + (row + 2.5f) * (seatWH) + seatWH / 2;

        rectFWC.bottom = rectFCabin.top + rectFCabin.width() / 2 +
                seatWH + (row + 4.5f) * (seatWH) + seatWH / 2;


        rectFWC.right = rectFCabin.left + column * (seatWH) + seatWH * 2f
                - seatWH / 3f;
        rectFWC.left = rectFCabin.left + column * (seatWH) + seatWH * 2f
                - seatWH / 3f - seatWH * 2

        ;
        mPaintOther.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectFWC, mPaintOther);
        drawWcText(rectFWC, canvas);
        drawSeatSecond(canvas, seatWH);

    }


    private void drawSeatSecond(Canvas canvas, float seatWH) {
        int row = 3;
        int column = 8;

        float seatWH2 = (float) (rectFCabin.width() / 10.0f);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i >= 0 && j < 2) {


                    setSeat(i, j, canvas, seatWH2, SeatType.Left, CabinType.Second);


                } else if (j >= 2 && j < 6) {


                    setSeat(i, j, canvas, seatWH2, SeatType.Middle, CabinType.Second);


                } else if (j >= 6) {

                    setSeat(i, j, canvas, seatWH2, SeatType.Right, CabinType.Second);

                }


            }

        }

        mPaintOther.setStyle(Paint.Style.FILL);

        rectFWall.top = rectFCabin.top
                + seatWH * 13
                + rectFCabin.width() / 2 +
                seatWH + (row + 1) * (seatWH2) + seatWH2 / 2;
        rectFWall.left = rectFCabin.left + seatWH2 / 3;
        rectFWall.right = rectFCabin.left + seatWH2 / 3
                + 2.5f * seatWH2;
        rectFWall.bottom = rectFCabin.top
                + seatWH * 13
                + rectFCabin.width() / 2 +
                seatWH + (row + 1) * (seatWH2) + seatWH2 / 2
                + ((int) (dip2px(2) * mAnimatedValue < 1 ? 1 : (int) (dip2px(2) * mAnimatedValue)));
        canvas.drawRoundRect(rectFWall, dip2px(1), dip2px(1), mPaintOther);
        rectFWall.top = rectFCabin.top
                + seatWH * 13
                + rectFCabin.width() / 2 +
                seatWH + (row + 1) * (seatWH2) + seatWH2 / 2;
        rectFWall.left = rectFCabin.left + column * (seatWH2) + seatWH2 * 2f
                - seatWH / 3f - 2.5f * seatWH2;
        rectFWall.right = rectFCabin.left + column * (seatWH2) + seatWH2 * 2f
                - seatWH / 3f;
        rectFWall.bottom = rectFCabin.top
                + seatWH * 13
                + rectFCabin.width() / 2 +
                seatWH + (row + 1) * (seatWH2) + seatWH2 / 2
                + ((int) (dip2px(2) * mAnimatedValue < 1 ? 1 : (int) (dip2px(2) * mAnimatedValue)));
        canvas.drawRoundRect(rectFWall, dip2px(1), dip2px(1), mPaintOther);

    }


    private void drawSeatTourist(Canvas canvas) {
        int row = 25;
        int column = 10;
        int seatWH = (int) (rectFCabin.width() / 12);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {


                if (j >= 0 && j < 3) {


                    setSeat(i, j, canvas, seatWH, SeatType.Left, CabinType.Tourist);


                } else if (j >= 3 && j < 7) {

                    setSeat(i, j, canvas, seatWH, SeatType.Middle, CabinType.Tourist);


                } else if (j >= 7) {
                    setSeat(i, j, canvas, seatWH, SeatType.Right, CabinType.Tourist);

                }


            }

        }


        rectFWC.top = rectFWall.bottom
                + seatWH * 1.5f
                +
                26 * (seatWH);
        rectFWC.bottom = rectFWC.top + seatWH * 3;


        rectFWC.left = rectFCabin.left + seatWH / 3;
        rectFWC.right = rectFCabin.left + seatWH / 3 + seatWH * 3;
        mPaintOther.setStyle(Paint.Style.STROKE);

        canvas.drawRect(rectFWC, mPaintOther);

        drawWcText(rectFWC, canvas);


        RectF rectFWifi = new RectF();

        rectFWifi.top = rectFWall.bottom
                + seatWH * 1.5f
                +
                26 * (seatWH);
        rectFWifi.bottom = rectFWifi.top + seatWH * 3;


        rectFWifi.left = rectFWC.right + seatWH / 2f;

        rectFWifi.right = rectFCabin.left + column * (seatWH) + seatWH * 2f
                - seatWH / 3f - seatWH * 3 - seatWH / 2f
        ;

        canvas.drawRect(rectFWifi, mPaintOther);


        drawWifiLogo(rectFWifi, canvas);


        rectFWC.top = rectFWall.bottom
                + seatWH * 1.5f
                +
                26 * (seatWH);
        rectFWC.bottom = rectFWC.top + seatWH * 3;


        rectFWC.left = rectFCabin.left + column * (seatWH) + seatWH * 2f
                - seatWH / 3f - seatWH * 3;
        rectFWC.right = rectFCabin.left + column * (seatWH) + seatWH * 2f
                - seatWH / 3f;
        mPaintOther.setStyle(Paint.Style.STROKE);

        canvas.drawRect(rectFWC, mPaintOther);


        drawWcText(rectFWC, canvas);


    }

    private void drawSeatLast(Canvas canvas) {
        int row = 19;
        int column = 10;
        int seatWH = (int) (rectFCabin.width() / 12);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {


                if (j >= 0 && j < 3) {

                    setSeat(i, j, canvas, seatWH, SeatType.Left, CabinType.Last);

                } else if (j >= 3 && j < 7) {

                    setSeat(i, j, canvas, seatWH, SeatType.Middle, CabinType.Last);

                } else if (j >= 7) {

                    setSeat(i, j, canvas, seatWH, SeatType.Right, CabinType.Last);

                }


            }

        }

    }


    private Bitmap setBitmapSize(int iconId, float w) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), iconId);
        float s = w * 1.0f / bitmap.getWidth();
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * s),
                (int) (bitmap.getHeight() * s), true);
        return bitmap;
    }


    private Bitmap getSeat(float width, SeatState type) {
        if (type == SeatState.Normal) {
            if (mBitmapSeat_normal == null) {
                mBitmapSeat_normal = setBitmapSize(R.drawable.seat_gray, width);

            } else if (Math.abs(mBitmapSeat_normal.getWidth() - width) > 1) {
                mBitmapSeat_normal = setBitmapSize(R.drawable.seat_gray, width);

            }

            return mBitmapSeat_normal;
        }
        if (type == SeatState.Selected) {
            if (mBitmapSeat_selected == null) {
                mBitmapSeat_selected = setBitmapSize(R.drawable.seat_sold, width);
            } else if (Math.abs(mBitmapSeat_selected.getWidth() - width) > 1) {
                mBitmapSeat_selected = setBitmapSize(R.drawable.seat_sold, width);

            }


            return mBitmapSeat_selected;

        }
        if (type == SeatState.Selecting) {
            if (mBitmapSeat_selecting == null) {
                mBitmapSeat_selecting = setBitmapSize(R.drawable.seat_green, width);
            } else if (Math.abs(mBitmapSeat_selecting.getWidth() - width) > 1) {
                mBitmapSeat_selecting = setBitmapSize(R.drawable.seat_green, width);

            }
            return mBitmapSeat_selecting;

        }
        return null;
    }


    public Bitmap getBitmapFuselage(float rectFCabinWidth) {
        Canvas canvas = null;

        int w = getMeasuredWidth();
        int h = getMeasuredHeight();


        if (mBitmapFuselage == null) {
            mBitmapFuselage = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(mBitmapFuselage);
            pathFuselage.moveTo(w / 2f - rectFCabinWidth / 2f - dip2px(2),
                    rectFCabin.top + rectFCabinWidth / 2f);
            pathFuselage.cubicTo(
                    w / 2f - rectFCabinWidth / 4f,

                    rectFCabin.top - rectFCabinWidth * 1.2f,
                    w / 2f + rectFCabinWidth / 4f,
                    rectFCabin.top - rectFCabinWidth * 1.2f,
                    w / 2f + rectFCabinWidth / 2f + dip2px(2),
                    rectFCabin.top + rectFCabinWidth / 2f
            );


            rectFCabin.top = rectFCabin.top + dip2px(10);//机翼向下平移距离
            pathFuselage.lineTo(w / 2f + rectFCabinWidth / 2f + dip2px(2)
                    , rectFCabin.top + rectFCabin.height() / 3f);

            pathFuselage.lineTo(w
                    , rectFCabin.top + rectFCabin.height() * 0.55f);

            pathFuselage.lineTo(w
                    , rectFCabin.top + rectFCabin.height() * 0.55f + rectFCabin.width() * 0.8f);

            pathFuselage.lineTo(rectFCabin.right + rectFCabin.width() / 2 * 1.5f
                    , rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 6f / 2f);

            pathFuselage.lineTo(w / 2f + rectFCabinWidth / 2f + dip2px(2)
                    , rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 6f / 2f);
//
            pathFuselage.lineTo(w / 2f + rectFCabinWidth / 2f + dip2px(2)
                    , rectFCabin.bottom - rectFCabinWidth / 2f);


            pathFuselage.cubicTo(
                    w / 2f + rectFCabinWidth / 4f,

                    rectFCabin.bottom + rectFCabinWidth * 2.5f,
                    w / 2f - rectFCabinWidth / 4f,
                    rectFCabin.bottom + rectFCabinWidth * 2.5f,
                    w / 2f - rectFCabinWidth / 2f - dip2px(2)
                    , rectFCabin.bottom - rectFCabinWidth / 2f
            );

            pathFuselage.lineTo(w / 2f - rectFCabinWidth / 2f - dip2px(2)
                    , rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 6f / 2f);
            pathFuselage.lineTo(rectFCabin.left - rectFCabin.width() / 2 * 1.5f
                    , rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 6f / 2f);
            pathFuselage.lineTo(0
                    , rectFCabin.top + rectFCabin.height() * 0.55f + rectFCabin.width() * 0.8f);

            pathFuselage.lineTo(0
                    , rectFCabin.top + rectFCabin.height() * 0.55f);

            pathFuselage.lineTo(w / 2f - rectFCabinWidth / 2f - dip2px(2)
                    , rectFCabin.top + rectFCabin.height() / 3f);
            pathFuselage.close();
            mPaint.setColor(Color.WHITE);
            mPaint.setAlpha(150);
            canvas.drawPath(pathFuselage, mPaint);
        }

        return mBitmapFuselage;
    }

    private Bitmap getBitmapCabin() {
        Canvas canvas = null;

        if (mBitmapCabin == null) {
            mBitmapCabin = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(mBitmapCabin);
            mPaint.setColor(Color.WHITE);
            rectFCabin.top = rectFCabin.top - dip2px(10);
            rectFCabin.bottom = rectFCabin.bottom + dip2px(5);
            canvas.drawRoundRect(rectFCabin, getMeasuredWidth() / 8f / 2f, getMeasuredWidth() / 8f / 2f, mPaint);

        }

        return mBitmapCabin;

    }


    public Bitmap getBitmapArrow() {
        Canvas canvas = null;

        if (mBitmapArrow == null) {
            mBitmapArrow = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(mBitmapArrow);


            pathArrow.reset();

            pathArrow.moveTo(rectFCabin.right + rectFCabin.width() / 2 * 1.2f
                    , rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 6f / 2f);


            pathArrow.quadTo(
                    rectFCabin.right + rectFCabin.width() / 2 * 1.3f,
                    rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 5f,

                    rectFCabin.right + rectFCabin.width() / 2 * 1.4f
                    , rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 6f / 2f);

            pathArrow.close();
            canvas.drawPath(pathArrow, mPaint);
            pathArrow.reset();
            pathArrow.moveTo(rectFCabin.left - rectFCabin.width() / 2 * 1.2f
                    , rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 6f / 2f);
            pathArrow.quadTo(
                    rectFCabin.left - rectFCabin.width() / 2 * 1.3f,
                    rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 5f,

                    rectFCabin.left - rectFCabin.width() / 2 * 1.4f
                    , rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 6f / 2f);

            pathArrow.close();
            canvas.drawPath(pathArrow, mPaint);


            pathArrow.reset();


            float right1x = getMeasuredWidth();
            float right1y = rectFCabin.top + rectFCabin.height() * 0.55f + rectFCabin.width() * 0.8f;
            float right2x = rectFCabin.right + rectFCabin.width() / 2 * 1.5f;
            float right2y = rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 6f / 2f;
            pathArrow.moveTo(
                    (right1x + right2x) / 2f,
                    (right1y + right2y) / 2f);
            pathArrow.quadTo(
                    (right1x + right2x) / 2f + rectFCabin.width() / 2 * 0.1f,
                    (right1y + right2y) / 2f + rectFCabin.height() / 60f * 7f,
                    (right1x + right2x) / 2f + rectFCabin.width() / 2 * 0.2f,
                    (right1y + right2y) / 2f
            );
            canvas.drawPath(pathArrow, mPaint);


            pathArrow.reset();

            float left1x = 0;
            float left1y = rectFCabin.top + rectFCabin.height() * 0.55f + rectFCabin.width() * 0.8f;
            float left2x = rectFCabin.left - rectFCabin.width() / 2 * 1.5f;
            float left2y = rectFCabin.top + rectFCabin.height() / 2f + rectFCabin.height() / 6f / 2f;
            pathArrow.moveTo(
                    (left1x + left2x) / 2f,
                    (left1y + left2y) / 2f);
            pathArrow.quadTo(
                    (left1x + left2x) / 2f - rectFCabin.width() / 2 * 0.1f,
                    (left1y + left2y) / 2f + rectFCabin.height() / 60f * 7f,
                    (left1x + left2x) / 2f - rectFCabin.width() / 2 * 0.2f,
                    (left1y + left2y) / 2f
            );
            mPaint.setColor(Color.WHITE);
            mPaint.setAlpha(150);
            canvas.drawPath(pathArrow, mPaint);


        }

        return mBitmapArrow;
    }


    private Bitmap getBitmapTail() {

        Canvas canvas = null;

        if (mBitmapTail == null) {
            mBitmapTail = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(mBitmapTail);
            pathTail.reset();
            rectFCabin.bottom = rectFCabin.bottom - dip2px(5);
            pathTail.moveTo(rectFCabin.centerX(),
                    rectFCabin.bottom + rectFCabin.width() / 2);

            pathTail.lineTo(rectFCabin.centerX() + rectFCabin.width() * 1.5f,
                    rectFCabin.bottom + rectFCabin.width() * 1.5f);
            pathTail.lineTo(rectFCabin.centerX() + rectFCabin.width() * 1.5f,
                    rectFCabin.bottom + rectFCabin.width() * 2f);
            pathTail.lineTo(rectFCabin.centerX(),
                    rectFCabin.bottom + rectFCabin.width() * 1.5f);
            pathTail.lineTo(rectFCabin.centerX() - rectFCabin.width() * 1.5f,
                    rectFCabin.bottom + rectFCabin.width() * 2f);
            pathTail.lineTo(rectFCabin.centerX() - rectFCabin.width() * 1.5f,
                    rectFCabin.bottom + rectFCabin.width() * 1.5f);
            pathTail.close();

            canvas.drawPath(pathTail, mPaint);

            pathTail.reset();
            pathTail.moveTo(rectFCabin.centerX() - rectFCabin.width() / 2 * 0.1f,
                    rectFCabin.bottom + rectFCabin.width() * 1.5f);
            pathTail.quadTo(
                    rectFCabin.centerX(),
                    rectFCabin.bottom + rectFCabin.width() * 3f,
                    rectFCabin.centerX() + rectFCabin.width() / 2 * 0.1f,
                    rectFCabin.bottom + rectFCabin.width() * 1.5f);

            pathTail.close();
            mPaint.setColor(Color.WHITE);
            mPaint.setAlpha(150);
            canvas.drawPath(pathTail, mPaint);

        }
        return mBitmapTail;


    }

    public void startAnim(boolean zoomOut) {// false zoom in,true zoom out
        stopAnim();
        startViewAnim(0f, 1f, 280, zoomOut);
    }

    private ValueAnimator valueAnimator;
    private float mAnimatedValue = 0f;

    public void stopAnim() {
        if (valueAnimator != null) {
            clearAnimation();
            valueAnimator.setRepeatCount(0);
            valueAnimator.cancel();
            valueAnimator.end();
            mAnimatedValue = 0f;
            postInvalidate();
        }
    }


    private ValueAnimator startViewAnim(float startF, final float endF, long time, final boolean zoomOut) {

        if (zoomOut && moveY > 0) {
            moveY = 0;
            invalidate();
        }


        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);//无限循环
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                mAnimatedValue = (float) valueAnimator.getAnimatedValue();

                if (zoomOut) {
                    mAnimatedValue = 1 - mAnimatedValue;
                }

                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();

        }

        return valueAnimator;
    }

    public float getFontlength(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.width();
    }

    public float getFontHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.height();

    }


    public class PointSeat {
        public int row;
        public int column;

        public PointSeat(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }


    private void drawWcText(RectF rectFWC, Canvas canvas) {
        mPaintOther.setTextSize(rectFWC.width() / 4);
        mPaintOther.setAlpha(150);
        canvas.drawText("WC",
                rectFWC.centerX()
                        - getFontlength(mPaintOther, "WC") / 2f
                ,
                rectFWC.centerY() + getFontHeight(mPaintOther, "WC") / 3f,
                mPaintOther
        );
        mPaintOther.setAlpha(255);

    }


    private void drawWifiLogo(RectF rectFWifi, Canvas canvas) {
        float signalRadius = rectFWifi.height() / 2 / 4;
        RectF rect = null;
        mPaintOther.setStrokeWidth(signalRadius / 4);
        mPaintOther.setAlpha(150);
        float marginTop = signalRadius * (3 + 0.5f) / 2f;

        for (int i = 0; i < 4; i++) {

            float radius = signalRadius * (i + 0.5f);
            if (i == 0) {
                radius = signalRadius / 2f;
            }
            rect = new RectF(
                    rectFWifi.centerX() - radius,
                    rectFWifi.centerY() - radius + marginTop,
                    rectFWifi.centerX() + radius,
                    rectFWifi.centerY() + radius + marginTop);
            if (i != 0) {
                mPaintOther.setStyle(Paint.Style.STROKE);
                canvas.drawArc(rect, -135, 90
                        , false, mPaintOther);
            } else {
                mPaintOther.setStyle(Paint.Style.FILL);
                canvas.drawArc(rect, -135, 90
                        , true, mPaintOther);
            }

        }
        mPaintOther.setStrokeWidth(0);
        mPaintOther.setAlpha(255);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();

    }

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);


    private void setBitmap(final Canvas canvas, final float seatWH, final SeatState type, final float left, final float top) {
        fixedThreadPool.execute(new Runnable() {

            @Override
            public void run() {
//                canvas.drawBitmap(getSeat(seatWH, mSeatSelected.get(getSeatKeyName(point.row, point.column)))
//                        , left
//                        ,
//                        top
//                        ,
//                        mPaint
//                );

                Bitmap b = getSeat(seatWH, type);
                if (b != null) {

//                    canvas.drawBitmap(b,left,top,mPaint);

                    Message msg = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putFloat("left", left);
                    bundle.putFloat("top", top);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bitmapByte = baos.toByteArray();
                    bundle.putByteArray("bitmap", bitmapByte);
                    msg.setData(bundle);
                    msg.obj = canvas;
                    mHandler.sendMessage(msg);


                }
//                b.recycle();

            }
        });
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Canvas c = (Canvas) msg.obj;

            Bundle bundle = msg.getData();

            if (bundle != null && c != null) {

                float left = bundle.getFloat("left");
                float top = bundle.getFloat("top");
                byte[] bis = bundle.getByteArray("bitmap");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                if (bitmap != null) {
                    c.drawBitmap(bitmap, left, top, mPaint);
                }


            }


        }
    };


    public abstract class MoveListerner implements OnTouchListener,
            GestureDetector.OnGestureListener {
        public static final int MOVE_TO_LEFT = 0;
        public static final int MOVE_TO_RIGHT = MOVE_TO_LEFT + 1;
        public static final int MOVE_TO_UP = MOVE_TO_RIGHT + 1;
        public static final int MOVE_TO_DOWN = MOVE_TO_UP + 1;

        private static final int FLING_MIN_DISTANCE = 150;
        private static final int FLING_MIN_VELOCITY = 50;
        private boolean isScorllStart = false;
        private boolean isUpAndDown = false;
        GestureDetector mGestureDetector;
        float x1 = 0;
        float x2 = 0;
        float y1 = 0;
        float y2 = 0;

        public MoveListerner(Activity context) {
            super();
            mGestureDetector = new GestureDetector(context, this);
        }

        float startX = 0;
        float startY = 0;


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mGestureDetector.onTouchEvent(event)) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
//                Touch(event.getX(), event.getY());
                    startX = event.getX();
                    startY = event.getY();
                    return true;
                } else if (MotionEvent.ACTION_UP == event.getAction()) {
                    if (Math.abs(event.getX() - startX) < 5 &&
                            Math.abs(event.getY() - startY) < 5) {
                        Touch(event.getX(), event.getY());
                        return true;

                    }

                }
            }
            // 处理手势结束
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    endGesture();
                    break;
            }
            return false;
        }

        private void endGesture() {
            isScorllStart = false;
            isUpAndDown = false;
//        Log.e("a", "AA:over");
            moveOver();
        }

        public abstract void moveDirection(View v, int direction, float distanceX, float distanceY);

        public abstract void moveUpAndDownDistance(MotionEvent event, int distance, int distanceY);

        public abstract void moveOver();

        public abstract void Touch(float x, float y);


        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {
            float mOldY = e1.getY();
            int y = (int) e2.getRawY();
            if (!isScorllStart) {
                if (Math.abs(distanceX) / Math.abs(distanceY) > 2) {
                    // 左右滑动
                    isUpAndDown = false;
                    isScorllStart = true;
                } else if (Math.abs(distanceY) / Math.abs(distanceX) > 3) {
                    // 上下滑动
                    isUpAndDown = true;
                    isScorllStart = true;
                } else {
                    isScorllStart = false;
                }
            } else {
                // 算滑动速度的问题了
                if (isUpAndDown) {
                    // 是上下滑动，关闭左右检测
                    if (mOldY + 5 < y) {
                        moveUpAndDownDistance(e2, -3, (int) distanceY);
                    } else if (mOldY + 5 > y) {
                        moveUpAndDownDistance(e2, 3, (int) distanceY);
                    }
                }
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
//        Log.e("a", "AA:A" + velocityX + ":" + velocityY);
            if (isUpAndDown)
                return false;
            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // Fling left
                moveDirection(null, MOVE_TO_LEFT, e1.getX() - e2.getX(), e1.getY() - e2.getY());
            } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // Fling right
                moveDirection(null, MOVE_TO_RIGHT, e2.getX() - e1.getX(), e2.getY() - e1.getY());
            } else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                    && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                // Fling up
                moveDirection(null, MOVE_TO_UP, 0, e1.getY() - e2.getY());
            } else {
                // Fling down
                moveDirection(null, MOVE_TO_DOWN, 0, e2.getY() - e1.getY());
            }
            return false;
        }
    }
}
