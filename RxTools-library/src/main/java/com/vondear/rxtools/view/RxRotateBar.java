package com.vondear.rxtools.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.vondear.rxtools.R;
import com.vondear.rxtools.RxImageTool;

import java.util.ArrayList;

/**
 *
 * @author Vondear
 * @date 2017/10/9
 *
 */
public class RxRotateBar extends FrameLayout {

    private static final int STROKE_OFFSET = 10;
    private static final long ROTATING_ANIMATION_DURATION = 3000L;
    private static final long RATING_ANIMATION_DURATION = 3000L;
    private static final long TEXT_ANIMATION_DURATION = 1000L;
    /**
     * animator callback listener
     */
    private AnimatorListener mAnimatorListener;
    // style attr
    private int mRatedColor, mUnratedColor, mTitleColor, mOutlineColor, mDefaultColor;
    private boolean isShowTitle = true;
    private int mRatingMax;
    private boolean isShow = false;
    private int mCenterX, mCenterY;
    private float rotateAngle;
    private int ratingGap = -1, textAlpha = 0;
    private ArrayList<RxRotateBarBasic> mRatingBars;
    private ValueAnimator rotateAnimator, ratingAnimator, titleAnimator;
    private Paint mCenterTextPaint;
    private int mCenterTextColor; // 蛛网等级填充的颜色
    private int mCenterTextSize = 40;
    private String mCenterText = "";
    private boolean isShowCenterTitle = false;

    public RxRotateBar(Context context) {
        super(context);
    }

    public RxRotateBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        // call onDraw in ViewGroup
        setWillNotDraw(false);
        initAttrs(context, attrs);

        mRatingBars = new ArrayList<>();
        initRotatingAnimation();
        initRatingAnimation();
        initTextAnimation();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RxRotateBar, 0, 0);
        mCenterText = attributes.getString(R.styleable.RxRotateBar_ratingCenterTitle);//中心标题
        mCenterTextSize = attributes.getDimensionPixelSize(R.styleable.RxRotateBar_centerTitleSize, RxImageTool.dip2px(20));//标题字体大小
        mRatedColor = attributes.getColor(R.styleable.RxRotateBar_ratingRatedColor, 0);
        mUnratedColor = attributes.getColor(R.styleable.RxRotateBar_ratingUnratedColor, 0);
        mTitleColor = attributes.getColor(R.styleable.RxRotateBar_ratingTitleColor, 0);
        mOutlineColor = attributes.getColor(R.styleable.RxRotateBar_ratingOutlineColor, 0);
        mCenterTextColor = attributes.getColor(R.styleable.RxRotateBar_ratingCenterColor, Color.WHITE);
        mDefaultColor = attributes.getColor(R.styleable.RxRotateBar_ratingDefaultColor, 0);
        isShowTitle = attributes.getBoolean(R.styleable.RxRotateBar_ratingTitleVisible, true);
        mRatingMax = attributes.getInt(R.styleable.RxRotateBar_ratingMax, 0);
        attributes.recycle();
    }

    private void initTextAnimation() {
        titleAnimator = ValueAnimator.ofInt(0, 255);
        titleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textAlpha = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        titleAnimator.setDuration(TEXT_ANIMATION_DURATION);
        titleAnimator.setInterpolator(new AccelerateInterpolator());
    }

    public void initRotatingAnimation() {
        rotateAnimator = ValueAnimator.ofFloat(0.0f, 360f * 3);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        rotateAnimator.setDuration(ROTATING_ANIMATION_DURATION);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        rotateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mAnimatorListener != null) {
                    mAnimatorListener.onRotateStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowCenterTitle = true;
                if (mAnimatorListener != null) {
                    mAnimatorListener.onRotateEnd();
                }
                titleAnimator.start();
                ratingAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void initRatingAnimation() {
        ratingAnimator = ValueAnimator.ofInt(0, 9);
        ratingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ratingGap = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        ratingAnimator.setDuration(RATING_ANIMATION_DURATION);
        ratingAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mAnimatorListener != null) {
                    mAnimatorListener.onRatingStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowCenterTitle = true;
                if (mAnimatorListener != null) {
                    mAnimatorListener.onRatingEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;
        initRatingBar();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //初始化字体画笔
        mCenterTextPaint = new Paint();
        mCenterTextPaint.setAntiAlias(true);
        mCenterTextPaint.setColor(mCenterTextColor);
        mCenterTextPaint.setTextSize(mCenterTextSize);

        if (isShow) {
            canvas.save();
            canvas.rotate(rotateAngle, mCenterX, mCenterY);
            for (RxRotateBarBasic ratingBar : mRatingBars) {
                ratingBar.drawOutLine(canvas);
            }
            canvas.restore();

            canvas.save();
            canvas.rotate(-rotateAngle, mCenterX, mCenterY);
            for (RxRotateBarBasic ratingBar : mRatingBars) {
                ratingBar.drawUnRate(canvas);
                ratingBar.drawShadow(canvas);
            }
            canvas.restore();

            if (ratingGap != -1) {
                for (RxRotateBarBasic ratingBar : mRatingBars) {
                    for (int rate = 0; rate < ratingBar.getRate(); rate++) {
                        if (rate <= ratingGap) {
                            ratingBar.drawRate(canvas, rate);
                        }
                    }
                }
            }

            for (RxRotateBarBasic ratingBar : mRatingBars) {
                ratingBar.drawTitle(canvas, textAlpha);
            }

            float textWidth = mCenterTextPaint.measureText(mCenterText);
            Paint.FontMetrics fontMetrics = mCenterTextPaint.getFontMetrics();
            float textHeight = fontMetrics.descent - fontMetrics.ascent;

            if (isShowCenterTitle) {
                canvas.drawText(mCenterText, mCenterX - textWidth / 2, mCenterY + textHeight / 4, mCenterTextPaint);
            }
        }
    }

    public void addRatingBar(RxRotateBarBasic ratingBar) {
        mRatingBars.add(ratingBar);
    }

    public void removeRatingBar(RxRotateBarBasic ratingBar) {
        mRatingBars.remove(ratingBar);
    }

    public void removeAll() {
        mRatingBars.removeAll(mRatingBars);
        clear();
    }

    public void clear() {
        isShow = false;
        ratingGap = -1;
        textAlpha = 0;
    }

    public void show() {
        isShowCenterTitle = false;

        titleAnimator.cancel();
        ratingAnimator.cancel();

        if (mRatingBars.size() == 0) {
            return;
        }
        initRatingBar();
        rotateAnimator.start();
        isShow = true;
    }

    private void initRatingBar() {

        int dividePart = mRatingBars.size();

        int sweepAngle = dividePart == 1 ? 360 : (360 - dividePart * STROKE_OFFSET) / dividePart;

        int rotateOffset = dividePart == 1 ? 90 : 90 + sweepAngle / 2;

        for (int i = 0; i < dividePart; i++) {
            float startAngle = i * (sweepAngle + STROKE_OFFSET) - rotateOffset;
            RxRotateBarBasic ratingBar = mRatingBars.get(i);

            if (dividePart == 1) {
                // only show one rating bar
                ratingBar.setIsSingle(true);
            }
            ratingBar.setCenterX(mCenterX);
            ratingBar.setCenterY(mCenterY);
            ratingBar.setStartAngle(startAngle);
            ratingBar.setSweepAngle(sweepAngle);

            // style attr
            ratingBar.isShowTitle(isShowTitle);
            if (mDefaultColor != 0) {
                ratingBar.setRatingBarColor(mDefaultColor);
            }
            if (mTitleColor != 0) {
                ratingBar.setTitleColor(mTitleColor);
            }
            if (mRatedColor != 0) {
                ratingBar.setRatedColor(mRatedColor);
            }
            if (mUnratedColor != 0) {
                ratingBar.setUnRatedColor(mUnratedColor);
            }
            if (mOutlineColor != 0) {
                ratingBar.setOutlineColor(mOutlineColor);
            }
            if (mRatingMax != 0) {
                ratingBar.setMaxRate(mRatingMax);
            }

            ratingBar.init();
        }

    }

    public AnimatorListener getAnimatorListener() {
        return mAnimatorListener;
    }

    public void setAnimatorListener(AnimatorListener mAnimatorListener) {
        this.mAnimatorListener = mAnimatorListener;
    }

    public void isShowTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    public void setDefaultColor(int color) {
        this.mDefaultColor = color;
    }

    public int getCenterTextColor() {
        return mCenterTextColor;
    }

    public void setCenterTextColor(int centerTextColor) {
        mCenterTextColor = centerTextColor;
        invalidate();
    }

    public int getCenterTextSize() {
        return mCenterTextSize;
    }

    public void setCenterTextSize(int centerTextSize) {
        mCenterTextSize = centerTextSize;
        invalidate();
    }

    public String getCenterText() {
        return mCenterText;
    }

    public void setCenterText(String centerText) {
        mCenterText = centerText;
        invalidate();
    }

    public interface AnimatorListener {
        void onRotateStart();

        void onRotateEnd();

        void onRatingStart();

        void onRatingEnd();
    }
}
