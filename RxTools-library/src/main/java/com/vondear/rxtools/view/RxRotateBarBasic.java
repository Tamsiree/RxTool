package com.vondear.rxtools.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;

import java.util.ArrayList;

/**
 *
 * @author Vondear
 * @date 12/24/15
 */
public class RxRotateBarBasic {

    private static final int ITEM_OFFSET = 1;

    private static final int SHADOW_ALPHA = (int) (255 * 0.2); // 20%

    private static final int UN_RATING_ALPHA = (int) (255 * 0.3); // 30%

    private static final int RATING_ALPHA = (int) (255 * 1.0); // 100%

    private static final int OUTLINE_ALPHA = (int) (255 * 0.4); // 40%

    private boolean isSingle = false;

    private boolean isShowTitle = true;

    private int outlineWidth, ratingBarWidth, shadowWidth, textWidth;

    private float mStartAngle;

    private float mSweepAngle;

    private int maxRate = 10;

    private int mCurRate;

    private ArrayList<Rate> rates;

    private Paint ratedPaint, unRatedPaint, shadowPaint, outlinePaint;
    private TextPaint titlePaint;

    private int mRadius;

    private String mTitle;

    private int mCenterX, mCenterY;

    private RectF outlineOval, ratingOval, shadowOval;

    public RxRotateBarBasic(int curRate, String title) {
        this.mCurRate = curRate;
        this.mTitle = title;

        outlinePaint = new Paint();
        ratedPaint = new Paint();
        unRatedPaint = new Paint();
        shadowPaint = new Paint();
        titlePaint = new TextPaint();
        // set default color
        setRatingBarColor(Color.WHITE);
    }

    protected void init() {
        // in this order to init
        initRatingBar();
        initOval();
        initPaint();
    }

    private void initOval() {
        mRadius = mCenterX > mCenterY ? mCenterY : mCenterX;

        // text bar : 1/10 of radius
        textWidth = mRadius / 10;
        // rating bar : 1/10 of radius
        ratingBarWidth = mRadius / 10;
        // shadow : 1/3 of rating bar
        shadowWidth = ratingBarWidth / 3;
        // outline : 1/3 of shadow
        outlineWidth = shadowWidth / 3;

        int outlineRadius = mRadius - (textWidth / 2); // outline include text and outline, radius is base on textWidth

        int paddingRadius = outlineRadius - (textWidth / 2); // padding space draw nothing, radius is base on textWidth

        int ratingBarRadius = paddingRadius - (textWidth / 2) - (ratingBarWidth / 2);

        int shadowRadius = ratingBarRadius - (ratingBarWidth / 2) - (shadowWidth / 2);

        outlineOval = new RectF();
        ratingOval = new RectF();
        shadowOval = new RectF();

        outlineOval.left = mCenterX - outlineRadius;
        outlineOval.top = mCenterY - outlineRadius;
        outlineOval.right = mCenterX + outlineRadius;
        outlineOval.bottom = mCenterY + outlineRadius;

        ratingOval.left = mCenterX - ratingBarRadius;
        ratingOval.top = mCenterY - ratingBarRadius;
        ratingOval.right = mCenterX + ratingBarRadius;
        ratingOval.bottom = mCenterY + ratingBarRadius;

        shadowOval.left = mCenterX - shadowRadius;
        shadowOval.top = mCenterY - shadowRadius;
        shadowOval.right = mCenterX + shadowRadius;
        shadowOval.bottom = mCenterY + shadowRadius;
    }

    private void initPaint() {

        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(outlineWidth);
        outlinePaint.setAlpha(OUTLINE_ALPHA);

        ratedPaint.setAntiAlias(true);
        ratedPaint.setStyle(Paint.Style.STROKE);
        ratedPaint.setStrokeWidth(ratingBarWidth);
        ratedPaint.setAlpha(RATING_ALPHA);

        unRatedPaint.setAntiAlias(true);
        unRatedPaint.setStyle(Paint.Style.STROKE);
        unRatedPaint.setStrokeWidth(ratingBarWidth);
        unRatedPaint.setAlpha(UN_RATING_ALPHA);

        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setStrokeWidth(shadowWidth);
        shadowPaint.setAlpha(SHADOW_ALPHA);

        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(textWidth);
        titlePaint.setAlpha(RATING_ALPHA);
    }

    private void initRatingBar() {
        rates = new ArrayList<>();

        float itemSweepAngle;
        if (isSingle) {
            itemSweepAngle = (mSweepAngle - (ITEM_OFFSET * (maxRate))) / maxRate;
        } else {
            itemSweepAngle = (mSweepAngle - (ITEM_OFFSET * (maxRate - 1))) / maxRate;
        }

        for (int i = 0; i < maxRate; i++) {
            float itemStartAngle = mStartAngle + i * (itemSweepAngle + ITEM_OFFSET);
            rates.add(new Rate(itemStartAngle, itemSweepAngle));
        }
    }

    protected void drawUnRate(Canvas canvas) {
        for (Rate arc : rates) {
            arc.drawArc(canvas, ratingOval, unRatedPaint);
        }
    }

    protected void drawRate(Canvas canvas, int index) {
        if (index >= maxRate) {
            return;
        }
        Rate arc = rates.get(index);
        arc.drawArc(canvas, ratingOval, ratedPaint);
    }

    protected void drawShadow(Canvas canvas) {
        for (Rate arc : rates) {
            arc.drawArc(canvas, shadowOval, shadowPaint);
        }
    }

    protected void drawTitle(Canvas canvas, int alpha) {
        if (alpha > 0 && isShowTitle) {
            Path path = new Path();
            float circumference = (float) (Math.PI * (outlineOval.right - outlineOval.left));
            float textAngle = (360 / circumference) * titlePaint.measureText(getTitle());

            float startAngle = mStartAngle + mSweepAngle / 2 - textAngle / 2;

            if (isSingle) {
                // when single, draw 360 the path will be a circle
                path.addArc(outlineOval, startAngle - mSweepAngle / 2, mSweepAngle / 2);
            } else {
                path.addArc(outlineOval, startAngle, mSweepAngle);
            }

            titlePaint.setAlpha(alpha);
            canvas.drawTextOnPath(mTitle, path, 0, textWidth / 3, titlePaint);
        }
    }

    protected void drawOutLine(Canvas canvas) {

        float circumference = (float) (Math.PI * (outlineOval.right - outlineOval.left));
        float textAngle = (360 / circumference) * titlePaint.measureText(getTitle());

        float sweepAngle = (mSweepAngle - textAngle - 1 - 1) / 2;

        if (isShowTitle) {
            // text left
            float leftStartAngle = mStartAngle;
            canvas.drawArc(outlineOval, leftStartAngle, sweepAngle, false, outlinePaint);
            // text right
            float rightStartAngle = mStartAngle + mSweepAngle - sweepAngle;
            canvas.drawArc(outlineOval, rightStartAngle, sweepAngle, false, outlinePaint);
        } else {
            canvas.drawArc(outlineOval, mStartAngle, mSweepAngle, false, outlinePaint);
        }


    }

    public void setMaxRate(int maxRate) {
        this.maxRate = maxRate;
    }

    public int getRate() {
        return mCurRate;
    }

    public void setRate(int curRate) {
        this.mCurRate = curRate;
    }

    protected void setStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
    }

    protected void setSweepAngle(float mSweepAngle) {
        this.mSweepAngle = mSweepAngle;
    }

    protected void setCenterX(int mCenterX) {
        this.mCenterX = mCenterX;
    }

    protected void setCenterY(int mCenterY) {
        this.mCenterY = mCenterY;
    }

    protected void setIsSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setRatedColor(int color) {
        ratedPaint.setColor(color);
    }

    public void setUnRatedColor(int color) {
        unRatedPaint.setColor(color);
    }

    public void setTitleColor(int color) {
        titlePaint.setColor(color);
    }

    public void setOutlineColor(int color) {
        outlinePaint.setColor(color);
    }

    public void setShadowColor(int color) {
        shadowPaint.setColor(color);
    }

    public void isShowTitle(boolean isShow) {
        isShowTitle = isShow;
    }

    public void setRatingBarColor(int color) {
        ratedPaint.setColor(color);
        unRatedPaint.setColor(color);
        titlePaint.setColor(color);
        outlinePaint.setColor(color);
        shadowPaint.setColor(color);
    }

    /**
     * Rate class
     */
    public class Rate {
        private float startAngle, sweepAngle;

        public Rate(float startAngle, float sweepAngle) {
            this.startAngle = startAngle;
            this.sweepAngle = sweepAngle;
        }

        public void drawArc(Canvas canvas, RectF oval, Paint paint) {
            canvas.drawArc(oval, startAngle, sweepAngle, false, paint);
        }
    }

}
