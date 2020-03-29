package com.tamsiree.rxui.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import com.tamsiree.rxui.R

/**
 * @author tamsiree
 * 自动左右平滑移动的ImageView
 */
class RxAutoImageView : FrameLayout {
    private var mImageView: ImageView? = null
    var resId = 0

    constructor(context: Context?) : super(context!!)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        //导入布局
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.layout_auto_imageview, this)
        mImageView = findViewById(R.id.img_backgroud)

        //获得这个控件对应的属性。
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.RxAutoImageView)
        resId = try {
            //获得属性值
            a.getResourceId(R.styleable.RxAutoImageView_ImageSrc, 0)
        } finally {
            //回收这个对象
            a.recycle()
        }
        if (resId != 0) {
            mImageView?.setImageResource(resId)
        }
        Handler().postDelayed({
            val animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim)
            mImageView?.startAnimation(animation)
        }, 200)
    }
}