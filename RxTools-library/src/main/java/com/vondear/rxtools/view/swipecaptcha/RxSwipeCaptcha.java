package com.vondear.rxtools.view.swipecaptcha;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import com.vondear.rxtools.R;

import java.util.Random;

/**
 * Create By Vondear.
 * 介绍：仿斗鱼滑动验证码View
 * 更新时间： 2017/08/24.
 */

public class RxSwipeCaptcha extends android.support.v7.widget.AppCompatImageView {
    private final String TAG = RxSwipeCaptcha.class.getName();
    //控件的宽高
    protected int mWidth;
    protected int mHeight;

    //验证码滑块的宽高
    private int mCaptchaWidth;
    private int mCaptchaHeight;
    //验证码的左上角(起点)的x y
    private int mCaptchaX;
    private int mCaptchaY;
    private Random mRandom;
    private Paint mPaint;
    //验证码 阴影、抠图的Path
    private Path mCaptchaPath;
    private PorterDuffXfermode mPorterDuffXfermode;


    //是否绘制滑块（验证失败闪烁动画用）
    private boolean isDrawMask;
    //滑块Bitmap
    private Bitmap mMaskBitmap;
    private Paint mMaskPaint;
    //用于绘制阴影的Paint
    private Paint mMaskShadowPaint;
    private Bitmap mMaskShadowBitmap;
    //滑块的位移
    private int mDragerOffset;

    //是否处于验证模式，在验证成功后 为false，其余情况为true
    private boolean isMatchMode;
    //验证的误差允许值
    private float mMatchDeviation;
    //验证失败的闪烁动画
    private ValueAnimator mFailAnim;
    //验证成功的白光一闪动画
    private boolean isShowSuccessAnim;
    private ValueAnimator mSuccessAnim;
    private Paint mSuccessPaint;//画笔
    private int mSuccessAnimOffset;//动画的offset
    private Path mSuccessPath;//成功动画 平行四边形Path


    public RxSwipeCaptcha(Context context) {
        this(context, null);
    }

    public RxSwipeCaptcha(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxSwipeCaptcha(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        int defaultSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
        mCaptchaHeight = defaultSize;
        mCaptchaWidth = defaultSize;
        mMatchDeviation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RxSwipeCaptcha, defStyleAttr, 0);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.RxSwipeCaptcha_captchaHeight) {
                mCaptchaHeight = (int) ta.getDimension(attr, defaultSize);
            } else if (attr == R.styleable.RxSwipeCaptcha_captchaWidth) {
                mCaptchaWidth = (int) ta.getDimension(attr, defaultSize);
            } else if (attr == R.styleable.RxSwipeCaptcha_matchDeviation) {
                mMatchDeviation = ta.getDimension(attr, mMatchDeviation);
            }
        }
        ta.recycle();


        mRandom = new Random(System.nanoTime());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(0x77000000);
        //mPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔遮罩滤镜
        mPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));

        //滑块区域
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        // 实例化阴影画笔
        mMaskShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mMaskShadowPaint.setColor(Color.BLACK);
/*        mMaskShadowPaint.setStrokeWidth(50);
        mMaskShadowPaint.setTextSize(50);
        mMaskShadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);*/
        mMaskShadowPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));

        mCaptchaPath = new Path();

        mWidth = mCaptchaWidth;
        mHeight = mCaptchaHeight;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        //动画区域 会用到宽高
        createMatchAnim();

        post(new Runnable() {
            @Override
            public void run() {
                createCaptcha();
            }
        });
    }

    //验证动画初始化区域
    private void createMatchAnim() {
        mFailAnim = ValueAnimator.ofFloat(0, 1);
        mFailAnim.setDuration(100)
                .setRepeatCount(4);
        mFailAnim.setRepeatMode(ValueAnimator.REVERSE);
        //失败的时候先闪一闪动画 斗鱼是 隐藏-显示 -隐藏 -显示
        mFailAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onCaptchaMatchCallback.matchFailed(RxSwipeCaptcha.this);
            }
        });
        mFailAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: " + animatedValue);
                if (animatedValue < 0.5f) {
                    isDrawMask = false;
                } else {
                    isDrawMask = true;
                }
                invalidate();
            }
        });

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        mSuccessAnim = ValueAnimator.ofInt(mWidth + width, 0);
        mSuccessAnim.setDuration(500);
        mSuccessAnim.setInterpolator(new FastOutLinearInInterpolator());
        mSuccessAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSuccessAnimOffset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mSuccessAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isShowSuccessAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onCaptchaMatchCallback.matchSuccess(RxSwipeCaptcha.this);
                isShowSuccessAnim = false;
                isMatchMode = false;
            }
        });
        mSuccessPaint = new Paint();
        mSuccessPaint.setShader(new LinearGradient(0, 0, width / 2 * 3, mHeight, new int[]{
                0x00ffffff, 0x88ffffff}, new float[]{0, 0.5f},
                Shader.TileMode.MIRROR));
        //模仿斗鱼 是一个平行四边形滚动过去
        mSuccessPath = new Path();
        mSuccessPath.moveTo(0, 0);
        mSuccessPath.rLineTo(width, 0);
        mSuccessPath.rLineTo(width / 2, mHeight);
        mSuccessPath.rLineTo(-width, 0);
        mSuccessPath.close();
    }

    //生成验证码区域
    public void createCaptcha() {
        if (getDrawable() != null) {
            resetFlags();
            createCaptchaPath();
            craeteMask();
            invalidate();
        }
    }

    //重置一些flasg， 开启验证模式
    private void resetFlags() {
        isMatchMode = true;
    }

    //生成验证码Path
    private void createCaptchaPath() {
        //原本打算随机生成gap，后来发现 宽度/3 效果比较好，
        int gap = mRandom.nextInt(mCaptchaWidth / 2);
        gap = mCaptchaWidth / 3;

        //随机生成验证码阴影左上角 x y 点，
        mCaptchaX = mRandom.nextInt(Math.abs(mWidth - mCaptchaWidth - gap));
        mCaptchaY = mRandom.nextInt(Math.abs(mHeight - mCaptchaHeight - gap));
        Log.d(TAG, "createCaptchaPath() called mWidth:" + mWidth + ", mHeight:" + mHeight + ", mCaptchaX:" + mCaptchaX + ", mCaptchaY:" + mCaptchaY);

        mCaptchaPath.reset();
        mCaptchaPath.lineTo(0, 0);


        //从左上角开始 绘制一个不规则的阴影
        mCaptchaPath.moveTo(mCaptchaX, mCaptchaY);//左上角


/*        mCaptchaPath.lineTo(mCaptchaX + gap, mCaptchaY);
        //画出凹凸 由于是多段Path 无法闭合，简直阿西吧
        int r = mCaptchaWidth / 2 - gap;
        RectF oval = new RectF(mCaptchaX + gap, mCaptchaY - (r), mCaptchaX + gap + r * 2, mCaptchaY + (r));
        mCaptchaPath.arcTo(oval, 180, 180);*/

        mCaptchaPath.lineTo(mCaptchaX + gap, mCaptchaY);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX + gap, mCaptchaY),
                new PointF(mCaptchaX + gap * 2, mCaptchaY),
                mCaptchaPath, mRandom.nextBoolean());


        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY);//右上角
        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY + gap);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX + mCaptchaWidth, mCaptchaY + gap),
                new PointF(mCaptchaX + mCaptchaWidth, mCaptchaY + gap * 2),
                mCaptchaPath, mRandom.nextBoolean());


        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY + mCaptchaHeight);//右下角
        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth - gap, mCaptchaY + mCaptchaHeight);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX + mCaptchaWidth - gap, mCaptchaY + mCaptchaHeight),
                new PointF(mCaptchaX + mCaptchaWidth - gap * 2, mCaptchaY + mCaptchaHeight),
                mCaptchaPath, mRandom.nextBoolean());


        mCaptchaPath.lineTo(mCaptchaX, mCaptchaY + mCaptchaHeight);//左下角
        mCaptchaPath.lineTo(mCaptchaX, mCaptchaY + mCaptchaHeight - gap);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX, mCaptchaY + mCaptchaHeight - gap),
                new PointF(mCaptchaX, mCaptchaY + mCaptchaHeight - gap * 2),
                mCaptchaPath, mRandom.nextBoolean());


        mCaptchaPath.close();

/*        RectF oval = new RectF(mCaptchaX + gap, mCaptchaY - (r), mCaptchaX + gap + r * 2, mCaptchaY + (r));
        mCaptchaPath.addArc(oval, 180,180);
        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY);
        //凹的话，麻烦一点，要利用多次move
        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY + gap);
        oval = new RectF(mCaptchaX + mCaptchaWidth - r, mCaptchaY + gap, mCaptchaX + mCaptchaWidth + r, mCaptchaY + gap + r * 2);
        mCaptchaPath.addArc(oval, 90, 180);
        mCaptchaPath.moveTo(mCaptchaX + mCaptchaWidth, mCaptchaY + gap + r * 2);*//*
        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY + mCaptchaHeight);
        mCaptchaPath.lineTo(mCaptchaX, mCaptchaY + mCaptchaHeight);
        mCaptchaPath.close();*/
    }

    //生成滑块
    private void craeteMask() {
        mMaskBitmap = getMaskBitmap(((BitmapDrawable) getDrawable()).getBitmap(), mCaptchaPath);
        //滑块阴影
        mMaskShadowBitmap = mMaskBitmap.extractAlpha();
        //拖动的位移重置
        mDragerOffset = 0;
        //isDrawMask  绘制失败闪烁动画用
        isDrawMask = true;
    }

    //抠图
    private Bitmap getMaskBitmap(Bitmap mBitmap, Path mask) {
        //以控件宽高 create一块bitmap
        Bitmap tempBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Log.e(TAG, " getMaskBitmap: width:" + mBitmap.getWidth() + ",  height:" + mBitmap.getHeight());
        Log.e(TAG, " View: width:" + mWidth + ",  height:" + mHeight);
        //把创建的bitmap作为画板
        Canvas mCanvas = new Canvas(tempBitmap);
        //有锯齿 且无法解决,所以换成XFermode的方法做
        //mCanvas.clipPath(mask);
        // 抗锯齿
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        //绘制用于遮罩的圆形
        mCanvas.drawPath(mask, mMaskPaint);
        //设置遮罩模式(图像混合模式)
        mMaskPaint.setXfermode(mPorterDuffXfermode);
//        mMaskPaint.setShadowLayer(5, 3, 3, 0xFF0000FF);

        // 设置光源的方向
        float[] direction = new float[]{ 1, 1, 1 };
//设置环境光亮度
        float light = 1f;
// 选择要应用的反射等级
        float specular = 6;
// 向mask应用一定级别的模糊
        float blur = 3.5f;
//        EmbossMaskFilter emboss=new EmbossMaskFilter(direction,light,specular,blur);
        BlurMaskFilter maskFilter = new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID);
// 应用mask
        mMaskPaint.setMaskFilter(maskFilter);

        //★考虑到scaleType等因素，要用Matrix对Bitmap进行缩放
        mCanvas.drawBitmap(mBitmap, getImageMatrix(), mMaskPaint);
        mMaskPaint.setXfermode(null);
        return tempBitmap;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //继承自ImageView，所以Bitmap，ImageView已经帮我们draw好了。
        //我只在上面绘制和验证码相关的部分，

        //是否处于验证模式，在验证成功后 为false，其余情况为true
        if (isMatchMode) {
            //首先绘制验证码阴影
            if (mCaptchaPath != null) {
                canvas.drawPath(mCaptchaPath, mPaint);
            }
            //绘制滑块
            // isDrawMask  绘制失败闪烁动画用
            if (null != mMaskBitmap && null != mMaskShadowBitmap && isDrawMask) {
                // 先绘制阴影
                canvas.drawBitmap(mMaskShadowBitmap, -mCaptchaX + mDragerOffset, 0, mMaskShadowPaint);
                canvas.drawBitmap(mMaskBitmap, -mCaptchaX + mDragerOffset, 0, null);
            }
            //验证成功，白光扫过的动画，这一块动画感觉不完美，有提高空间
            if (isShowSuccessAnim) {
                canvas.translate(mSuccessAnimOffset, 0);
                canvas.drawPath(mSuccessPath, mSuccessPaint);
            }
        }
    }


    /**
     * 校验
     */
    public void matchCaptcha() {
        if (null != onCaptchaMatchCallback && isMatchMode) {
            //这里验证逻辑，是通过比较，拖拽的距离 和 验证码起点x坐标。 默认3dp以内算是验证成功。
            if (Math.abs(mDragerOffset - mCaptchaX) < mMatchDeviation) {
                Log.d(TAG, "matchCaptcha() true: mDragerOffset:" + mDragerOffset + ", mCaptchaX:" + mCaptchaX);
                //matchSuccess();
                //成功的动画
                mSuccessAnim.start();


            } else {
                Log.e(TAG, "matchCaptcha() false: mDragerOffset:" + mDragerOffset + ", mCaptchaX:" + mCaptchaX);

                mFailAnim.start();
                //matchFailed();
            }
        }

    }

    /**
     * 重置验证码滑动距离,(一般用于验证失败)
     */
    public void resetCaptcha() {
        mDragerOffset = 0;
        invalidate();
    }

    /**
     * 最大可滑动值
     *
     * @return
     */
    public int getMaxSwipeValue() {
        //return ((BitmapDrawable) getDrawable()).getBitmap().getWidth() - mCaptchaWidth;
        //返回控件宽度
        return mWidth - mCaptchaWidth;
    }

    /**
     * 设置当前滑动值
     *
     * @param value
     */
    public void setCurrentSwipeValue(int value) {
        mDragerOffset = value;
        invalidate();
    }

    public interface OnCaptchaMatchCallback {
        void matchSuccess(RxSwipeCaptcha rxSwipeCaptcha);

        void matchFailed(RxSwipeCaptcha rxSwipeCaptcha);
    }

    /**
     * 验证码验证的回调
     */
    private OnCaptchaMatchCallback onCaptchaMatchCallback;

    public OnCaptchaMatchCallback getOnCaptchaMatchCallback() {
        return onCaptchaMatchCallback;
    }

    /**
     * 设置验证码验证回调
     *
     * @param onCaptchaMatchCallback
     * @return
     */
    public RxSwipeCaptcha setOnCaptchaMatchCallback(OnCaptchaMatchCallback onCaptchaMatchCallback) {
        this.onCaptchaMatchCallback = onCaptchaMatchCallback;
        return this;
    }

    /**
     * 传入起点、终点 坐标、凹凸和Path。
     * 会自动绘制凹凸的半圆弧
     *
     * @param start 起点坐标
     * @param end   终点坐标
     * @param path  半圆会绘制在这个path上
     * @param outer 是否凸半圆
     */
    private void drawPartCircle(PointF start, PointF end, Path path, boolean outer) {
        float c = 0.551915024494f;
        //中点
        PointF middle = new PointF(start.x + (end.x - start.x) / 2, start.y + (end.y - start.y) / 2);
        //半径
        float r1 = (float) Math.sqrt(Math.pow((middle.x - start.x), 2) + Math.pow((middle.y - start.y), 2));
        //gap值
        float gap1 = r1 * c;

        if (start.x == end.x) {
            //绘制竖直方向的

            //是否是从上到下
            boolean topToBottom = end.y - start.y > 0 ? true : false;
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            int flag;//旋转系数
            if (topToBottom) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸的 两个半圆
                path.cubicTo(start.x + gap1 * flag, start.y,
                        middle.x + r1 * flag, middle.y - gap1 * flag,
                        middle.x + r1 * flag, middle.y);
                path.cubicTo(middle.x + r1 * flag, middle.y + gap1 * flag,
                        end.x + gap1 * flag, end.y,
                        end.x, end.y);
            } else {
                //凹的 两个半圆
                path.cubicTo(start.x - gap1 * flag, start.y,
                        middle.x - r1 * flag, middle.y - gap1 * flag,
                        middle.x - r1 * flag, middle.y);
                path.cubicTo(middle.x - r1 * flag, middle.y + gap1 * flag,
                        end.x - gap1 * flag, end.y,
                        end.x, end.y);
            }
        } else {
            //绘制水平方向的

            //是否是从左到右
            boolean leftToRight = end.x - start.x > 0 ? true : false;
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            int flag;//旋转系数
            if (leftToRight) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸 两个半圆
                path.cubicTo(start.x, start.y - gap1 * flag,
                        middle.x - gap1 * flag, middle.y - r1 * flag,
                        middle.x, middle.y - r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y - r1 * flag,
                        end.x, end.y - gap1 * flag,
                        end.x, end.y);
            } else {
                //凹 两个半圆
                path.cubicTo(start.x, start.y + gap1 * flag,
                        middle.x - gap1 * flag, middle.y + r1 * flag,
                        middle.x, middle.y + r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y + r1 * flag,
                        end.x, end.y + gap1 * flag,
                        end.x, end.y);
            }
        }
    }
}
