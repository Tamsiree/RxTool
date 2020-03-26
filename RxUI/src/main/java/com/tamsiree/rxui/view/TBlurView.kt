package com.tamsiree.rxui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.IntRange
import androidx.appcompat.widget.AppCompatImageView
import com.tamsiree.rxkit.RxAnimationTool.startSwitchBackgroundAnim
import com.tamsiree.rxkit.TBlurTool
import com.tamsiree.rxkit.TBlurTool.getBlurBitmap
import com.tamsiree.rxui.R

/**
 * @ClassName TBlurImageView
 * @Description TODO
 * @Author tamsiree
 * @Date 20-3-26 下午2:32
 * @Version 1.0
 */
class TBlurView : AppCompatImageView {
    private lateinit var mImageView: ImageView
    private var mBlurRunnable: Runnable? = null

    /**
     * 模糊度 (0...25f)
     */
    @SuppressLint("SupportAnnotationUsage")
    @IntRange(from = 0, to = 25)
    var blurRadius = 15f

    //模糊View引用的资源ID
    var blurSrc = 0

    /**
     * 单位是 ms 毫秒
     */
    var delayTime: Long = 200
    private lateinit var mContext: Context

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        mContext = context
        mImageView = this
        mImageView.scaleType = ScaleType.CENTER_CROP
        //获得这个控件对应的属性。
        val a = context.obtainStyledAttributes(attrs, R.styleable.TBlurView)
        try {
            //模糊View
            blurSrc = a.getResourceId(R.styleable.TBlurView_blurSrc, R.drawable.icon_placeholder)
            blurRadius = a.getInteger(R.styleable.TBlurView_blurRadius, 15).toFloat()
            delayTime = a.getInteger(R.styleable.TBlurView_blurDelayTime, 200).toLong()
        } finally {
            //回收这个对象
            a.recycle()
        }
        if (isInEditMode) {
            val bitmap = BitmapFactory.decodeResource(mContext.resources, blurSrc)
            mImageView.setImageBitmap(TBlurTool.stackBlur(bitmap, blurRadius.toInt(), true))
        } else {
            notifyChange(blurSrc)
        }
    }

    fun notifyChange(resId: Int) {
        blurSrc = resId
        removeCallbacks(mBlurRunnable)
        mBlurRunnable = Runnable { setBlurImage() }
        postDelayed(mBlurRunnable, delayTime)
    }

    private fun generateBlurImage(): Bitmap {
        val bitmap = BitmapFactory.decodeResource(mContext.resources, blurSrc)
        return getBlurBitmap(mContext, bitmap, blurRadius)
    }

    private fun setBlurImage() {
        startSwitchBackgroundAnim(mImageView, generateBlurImage())
    }

}