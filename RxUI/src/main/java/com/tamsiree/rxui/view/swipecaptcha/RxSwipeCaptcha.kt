package com.tamsiree.rxui.view.swipecaptcha

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatImageView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.tamsiree.rxkit.TLog.d
import com.tamsiree.rxkit.TLog.e
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.swipecaptcha.RxSwipeCaptcha
import java.util.*

/**
 * @author Tamsiree.
 * 介绍：仿斗鱼滑动验证码View
 * @date 更新时间： 2017/08/24.
 */
class RxSwipeCaptcha @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {
    private val TAG = RxSwipeCaptcha::class.java.name

    //控件的宽高
    protected var mWidth = 0
    protected var mHeight = 0

    //验证码滑块的宽高
    private var mCaptchaWidth = 0
    private var mCaptchaHeight = 0

    //验证码的左上角(起点)的x y
    private var mCaptchaX = 0
    private var mCaptchaY = 0
    private var mRandom: Random? = null
    private var mPaint: Paint? = null

    //验证码 阴影、抠图的Path
    private var mCaptchaPath: Path? = null
    private var mPorterDuffXfermode: PorterDuffXfermode? = null

    //是否绘制滑块（验证失败闪烁动画用）
    private var isDrawMask = false

    //滑块Bitmap
    private var mMaskBitmap: Bitmap? = null
    private var mMaskPaint: Paint? = null

    //用于绘制阴影的Paint
    private var mMaskShadowPaint: Paint? = null
    private var mMaskShadowBitmap: Bitmap? = null

    //滑块的位移
    private var mDragerOffset = 0

    //是否处于验证模式，在验证成功后 为false，其余情况为true
    private var isMatchMode = false

    //验证的误差允许值
    private var mMatchDeviation = 0f

    //验证失败的闪烁动画
    private var mFailAnim: ValueAnimator? = null

    //验证成功的白光一闪动画
    private var isShowSuccessAnim = false
    private var mSuccessAnim: ValueAnimator? = null

    //画笔
    private var mSuccessPaint: Paint? = null

    //动画的offset
    private var mSuccessAnimOffset = 0

    //成功动画 平行四边形Path
    private var mSuccessPath: Path? = null
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val defaultSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics).toInt()
        mCaptchaHeight = defaultSize
        mCaptchaWidth = defaultSize
        mMatchDeviation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.RxSwipeCaptcha, defStyleAttr, 0)
        val n = ta.indexCount
        for (i in 0 until n) {
            val attr = ta.getIndex(i)
            if (attr == R.styleable.RxSwipeCaptcha_captchaHeight) {
                mCaptchaHeight = ta.getDimension(attr, defaultSize.toFloat()).toInt()
            } else if (attr == R.styleable.RxSwipeCaptcha_captchaWidth) {
                mCaptchaWidth = ta.getDimension(attr, defaultSize.toFloat()).toInt()
            } else if (attr == R.styleable.RxSwipeCaptcha_matchDeviation) {
                mMatchDeviation = ta.getDimension(attr, mMatchDeviation)
            }
        }
        ta.recycle()
        mRandom = Random(System.nanoTime())
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mPaint!!.color = 0x77000000
        //mPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔遮罩滤镜
        mPaint!!.maskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.SOLID)

        //滑块区域
        mPorterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        mMaskPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

        // 实例化阴影画笔
        mMaskShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mMaskShadowPaint!!.color = Color.BLACK
        /*mMaskShadowPaint.setStrokeWidth(50);
        mMaskShadowPaint.setTextSize(50);
        mMaskShadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);*/mMaskShadowPaint!!.maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID)
        mCaptchaPath = Path()
        mWidth = mCaptchaWidth
        mHeight = mCaptchaHeight
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        //动画区域 会用到宽高
        createMatchAnim()
        post { createCaptcha() }
    }

    //验证动画初始化区域
    private fun createMatchAnim() {
        mFailAnim = ValueAnimator.ofFloat(0f, 1f)
        mFailAnim?.setDuration(100)?.repeatCount = 4
        mFailAnim?.repeatMode = ValueAnimator.REVERSE
        //失败的时候先闪一闪动画 斗鱼是 隐藏-显示 -隐藏 -显示
        mFailAnim?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onCaptchaMatchCallback!!.matchFailed(this@RxSwipeCaptcha)
            }
        })
        mFailAnim?.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            d(TAG, "onAnimationUpdate: $animatedValue")
            isDrawMask = animatedValue >= 0.5f
            invalidate()
        })
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics).toInt()
        mSuccessAnim = ValueAnimator.ofInt(mWidth + width, 0)
        mSuccessAnim?.duration = 500
        mSuccessAnim?.interpolator = FastOutLinearInInterpolator()
        mSuccessAnim?.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            mSuccessAnimOffset = animation.animatedValue as Int
            invalidate()
        })
        mSuccessAnim?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                isShowSuccessAnim = true
            }

            override fun onAnimationEnd(animation: Animator) {
                onCaptchaMatchCallback!!.matchSuccess(this@RxSwipeCaptcha)
                isShowSuccessAnim = false
                isMatchMode = false
            }
        })
        mSuccessPaint = Paint()
        mSuccessPaint!!.shader = LinearGradient(0f, 0f, width / 2f * 3f, mHeight.toFloat(), intArrayOf(
                0x00ffffff, -0x77000001), floatArrayOf(0f, 0.5f),
                Shader.TileMode.MIRROR)
        //模仿斗鱼 是一个平行四边形滚动过去
        mSuccessPath = Path()
        mSuccessPath!!.moveTo(0f, 0f)
        mSuccessPath!!.rLineTo(width.toFloat(), 0f)
        mSuccessPath!!.rLineTo(width / 2.toFloat(), mHeight.toFloat())
        mSuccessPath!!.rLineTo(-width.toFloat(), 0f)
        mSuccessPath!!.close()
    }

    /**
     * 生成验证码区域
     */
    fun createCaptcha() {
        if (drawable != null) {
            resetFlags()
            createCaptchaPath()
            craeteMask()
            invalidate()
        }
    }

    /**
     * 重置一些flasg， 开启验证模式
     */
    private fun resetFlags() {
        isMatchMode = true
    }

    /**
     * 生成验证码Path
     */
    private fun createCaptchaPath() {
        //原本打算随机生成gap，后来发现 宽度/3 效果比较好，
        var gap = mRandom!!.nextInt(mCaptchaWidth / 2)
        gap = mCaptchaWidth / 3

        //随机生成验证码阴影左上角 x y 点，
        mCaptchaX = mRandom!!.nextInt(Math.abs(mWidth - mCaptchaWidth - gap))
        mCaptchaY = mRandom!!.nextInt(Math.abs(mHeight - mCaptchaHeight - gap))
        d(TAG, "createCaptchaPath() called mWidth:$mWidth, mHeight:$mHeight, mCaptchaX:$mCaptchaX, mCaptchaY:$mCaptchaY")
        mCaptchaPath!!.reset()
        mCaptchaPath!!.lineTo(0f, 0f)


        //从左上角开始 绘制一个不规则的阴影
        mCaptchaPath!!.moveTo(mCaptchaX.toFloat(), mCaptchaY.toFloat()) //左上角


        /*mCaptchaPath.lineTo(mCaptchaX + gap, mCaptchaY);
        //画出凹凸 由于是多段Path 无法闭合，简直阿西吧
        int r = mCaptchaWidth / 2 - gap;
        RectF oval = new RectF(mCaptchaX + gap, mCaptchaY - (r), mCaptchaX + gap + r * 2, mCaptchaY + (r));
        mCaptchaPath.arcTo(oval, 180, 180);*/mCaptchaPath!!.lineTo(mCaptchaX + gap.toFloat(), mCaptchaY.toFloat())
        //draw一个随机凹凸的圆
        drawPartCircle(PointF((mCaptchaX + gap).toFloat(), mCaptchaY.toFloat()),
                PointF((mCaptchaX + gap * 2).toFloat(), mCaptchaY.toFloat()),
                mCaptchaPath, mRandom!!.nextBoolean())
        mCaptchaPath!!.lineTo(mCaptchaX + mCaptchaWidth.toFloat(), mCaptchaY.toFloat()) //右上角
        mCaptchaPath!!.lineTo(mCaptchaX + mCaptchaWidth.toFloat(), mCaptchaY + gap.toFloat())
        //draw一个随机凹凸的圆
        drawPartCircle(PointF((mCaptchaX + mCaptchaWidth).toFloat(), (mCaptchaY + gap).toFloat()),
                PointF((mCaptchaX + mCaptchaWidth).toFloat(), (mCaptchaY + gap * 2).toFloat()),
                mCaptchaPath, mRandom!!.nextBoolean())
        mCaptchaPath!!.lineTo(mCaptchaX + mCaptchaWidth.toFloat(), mCaptchaY + mCaptchaHeight.toFloat()) //右下角
        mCaptchaPath!!.lineTo(mCaptchaX + mCaptchaWidth - gap.toFloat(), mCaptchaY + mCaptchaHeight.toFloat())
        //draw一个随机凹凸的圆
        drawPartCircle(PointF((mCaptchaX + mCaptchaWidth - gap).toFloat(), (mCaptchaY + mCaptchaHeight).toFloat()),
                PointF((mCaptchaX + mCaptchaWidth - gap * 2).toFloat(), (mCaptchaY + mCaptchaHeight).toFloat()),
                mCaptchaPath, mRandom!!.nextBoolean())
        mCaptchaPath!!.lineTo(mCaptchaX.toFloat(), mCaptchaY + mCaptchaHeight.toFloat()) //左下角
        mCaptchaPath!!.lineTo(mCaptchaX.toFloat(), mCaptchaY + mCaptchaHeight - gap.toFloat())
        //draw一个随机凹凸的圆
        drawPartCircle(PointF(mCaptchaX.toFloat(), (mCaptchaY + mCaptchaHeight - gap).toFloat()),
                PointF(mCaptchaX.toFloat(), (mCaptchaY + mCaptchaHeight - gap * 2).toFloat()),
                mCaptchaPath, mRandom!!.nextBoolean())
        mCaptchaPath!!.close()

        /*RectF oval = new RectF(mCaptchaX + gap, mCaptchaY - (r), mCaptchaX + gap + r * 2, mCaptchaY + (r));
        mCaptchaPath.addArc(oval, 180,180);
        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY);
        //凹的话，麻烦一点，要利用多次move
        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY + gap);
        oval = new RectF(mCaptchaX + mCaptchaWidth - r, mCaptchaY + gap, mCaptchaX + mCaptchaWidth + r, mCaptchaY + gap + r * 2);
        mCaptchaPath.addArc(oval, 90, 180);
        mCaptchaPath.moveTo(mCaptchaX + mCaptchaWidth, mCaptchaY + gap + r * 2);*/
        /*
        mCaptchaPath.lineTo(mCaptchaX + mCaptchaWidth, mCaptchaY + mCaptchaHeight);
        mCaptchaPath.lineTo(mCaptchaX, mCaptchaY + mCaptchaHeight);
        mCaptchaPath.close();*/
    }

    //生成滑块
    private fun craeteMask() {
        mMaskBitmap = getMaskBitmap((drawable as BitmapDrawable).bitmap, mCaptchaPath)
        //滑块阴影
        mMaskShadowBitmap = mMaskBitmap!!.extractAlpha()
        //拖动的位移重置
        mDragerOffset = 0
        //isDrawMask  绘制失败闪烁动画用
        isDrawMask = true
    }

    //抠图
    private fun getMaskBitmap(mBitmap: Bitmap, mask: Path?): Bitmap {
        //以控件宽高 create一块bitmap
        val tempBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        e(TAG, " getMaskBitmap: width:" + mBitmap.width + ",  height:" + mBitmap.height)
        e(TAG, " View: width:$mWidth,  height:$mHeight")
        //把创建的bitmap作为画板
        val mCanvas = Canvas(tempBitmap)
        //有锯齿 且无法解决,所以换成XFermode的方法做
        //mCanvas.clipPath(mask);
        // 抗锯齿
        mCanvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        //绘制用于遮罩的圆形
        mCanvas.drawPath(mask!!, mMaskPaint!!)
        //设置遮罩模式(图像混合模式)
        mMaskPaint!!.xfermode = mPorterDuffXfermode
        //mMaskPaint.setShadowLayer(5, 3, 3, 0xFF0000FF);

        // 设置光源的方向
        val direction = floatArrayOf(1f, 1f, 1f)
        //设置环境光亮度
        val light = 1f
        // 选择要应用的反射等级
        val specular = 6f
        // 向mask应用一定级别的模糊
        val blur = 3.5f
        //EmbossMaskFilter emboss=new EmbossMaskFilter(direction,light,specular,blur);
        val maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID)
        // 应用mask
        mMaskPaint!!.maskFilter = maskFilter

        //★考虑到scaleType等因素，要用Matrix对Bitmap进行缩放
        mCanvas.drawBitmap(mBitmap, imageMatrix, mMaskPaint)
        mMaskPaint!!.xfermode = null
        return tempBitmap
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //继承自ImageView，所以Bitmap，ImageView已经帮我们draw好了。
        //我只在上面绘制和验证码相关的部分，

        //是否处于验证模式，在验证成功后 为false，其余情况为true
        if (isMatchMode) {
            //首先绘制验证码阴影
            if (mCaptchaPath != null) {
                canvas.drawPath(mCaptchaPath!!, mPaint!!)
            }
            //绘制滑块
            // isDrawMask  绘制失败闪烁动画用
            if (null != mMaskBitmap && null != mMaskShadowBitmap && isDrawMask) {
                // 先绘制阴影
                canvas.drawBitmap(mMaskShadowBitmap!!, -mCaptchaX + mDragerOffset.toFloat(), 0f, mMaskShadowPaint)
                canvas.drawBitmap(mMaskBitmap!!, -mCaptchaX + mDragerOffset.toFloat(), 0f, null)
            }
            //验证成功，白光扫过的动画，这一块动画感觉不完美，有提高空间
            if (isShowSuccessAnim) {
                canvas.translate(mSuccessAnimOffset.toFloat(), 0f)
                canvas.drawPath(mSuccessPath!!, mSuccessPaint!!)
            }
        }
    }

    /**
     * 校验
     */
    fun matchCaptcha() {
        if (null != onCaptchaMatchCallback && isMatchMode) {
            //这里验证逻辑，是通过比较，拖拽的距离 和 验证码起点x坐标。 默认3dp以内算是验证成功。
            if (Math.abs(mDragerOffset - mCaptchaX) < mMatchDeviation) {
                d(TAG, "matchCaptcha() true: mDragerOffset:$mDragerOffset, mCaptchaX:$mCaptchaX")
                //matchSuccess();
                //成功的动画
                mSuccessAnim!!.start()
            } else {
                e(TAG, "matchCaptcha() false: mDragerOffset:$mDragerOffset, mCaptchaX:$mCaptchaX")
                mFailAnim!!.start()
                //matchFailed();
            }
        }
    }

    /**
     * 重置验证码滑动距离,(一般用于验证失败)
     */
    fun resetCaptcha() {
        mDragerOffset = 0
        invalidate()
    }//return ((BitmapDrawable) getDrawable()).getBitmap().getWidth() - mCaptchaWidth;
    //返回控件宽度

    /**
     * 最大可滑动值
     *
     * @return
     */
    val maxSwipeValue: Int
        get() =//return ((BitmapDrawable) getDrawable()).getBitmap().getWidth() - mCaptchaWidth;
                //返回控件宽度
            mWidth - mCaptchaWidth

    /**
     * 设置当前滑动值
     *
     * @param value
     */
    fun setCurrentSwipeValue(value: Int) {
        mDragerOffset = value
        invalidate()
    }

    interface OnCaptchaMatchCallback {
        fun matchSuccess(rxSwipeCaptcha: RxSwipeCaptcha)
        fun matchFailed(rxSwipeCaptcha: RxSwipeCaptcha)
    }

    /**
     * 验证码验证的回调
     */
    var onCaptchaMatchCallback: OnCaptchaMatchCallback? = null

    /**
     * 设置验证码验证回调
     *
     * @param onCaptchaMatchCallback
     * @return
     */
    fun setOnCaptchaMatchCallback(onCaptchaMatchCallback: OnCaptchaMatchCallback?): RxSwipeCaptcha {
        this.onCaptchaMatchCallback = onCaptchaMatchCallback
        return this
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
    private fun drawPartCircle(start: PointF, end: PointF, path: Path?, outer: Boolean) {
        val c = 0.551915024494f
        //中点
        val middle = PointF(start.x + (end.x - start.x) / 2, start.y + (end.y - start.y) / 2)
        //半径
        val r1 = Math.sqrt(Math.pow((middle.x - start.x).toDouble(), 2.0) + Math.pow((middle.y - start.y).toDouble(), 2.0)).toFloat()
        //gap值
        val gap1 = r1 * c
        if (start.x == end.x) {
            //绘制竖直方向的

            //是否是从上到下
            val topToBottom = end.y - start.y > 0
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            val flag: Int //旋转系数
            flag = if (topToBottom) {
                1
            } else {
                -1
            }
            if (outer) {
                //凸的 两个半圆
                path!!.cubicTo(start.x + gap1 * flag, start.y,
                        middle.x + r1 * flag, middle.y - gap1 * flag,
                        middle.x + r1 * flag, middle.y)
                path.cubicTo(middle.x + r1 * flag, middle.y + gap1 * flag,
                        end.x + gap1 * flag, end.y,
                        end.x, end.y)
            } else {
                //凹的 两个半圆
                path!!.cubicTo(start.x - gap1 * flag, start.y,
                        middle.x - r1 * flag, middle.y - gap1 * flag,
                        middle.x - r1 * flag, middle.y)
                path.cubicTo(middle.x - r1 * flag, middle.y + gap1 * flag,
                        end.x - gap1 * flag, end.y,
                        end.x, end.y)
            }
        } else {
            //绘制水平方向的

            //是否是从左到右
            val leftToRight = end.x - start.x > 0
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            val flag: Int //旋转系数
            flag = if (leftToRight) {
                1
            } else {
                -1
            }
            if (outer) {
                //凸 两个半圆
                path!!.cubicTo(start.x, start.y - gap1 * flag,
                        middle.x - gap1 * flag, middle.y - r1 * flag,
                        middle.x, middle.y - r1 * flag)
                path.cubicTo(middle.x + gap1 * flag, middle.y - r1 * flag,
                        end.x, end.y - gap1 * flag,
                        end.x, end.y)
            } else {
                //凹 两个半圆
                path!!.cubicTo(start.x, start.y + gap1 * flag,
                        middle.x - gap1 * flag, middle.y + r1 * flag,
                        middle.x, middle.y + r1 * flag)
                path.cubicTo(middle.x + gap1 * flag, middle.y + r1 * flag,
                        end.x, end.y + gap1 * flag,
                        end.x, end.y)
            }
        }
    }

    init {
        init(context, attrs, defStyleAttr)
    }
}