package com.tamsiree.rxui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import com.tamsiree.rxui.R

/**
 * @author tamsiree
 * @date 2016/7/20
 *
 *
 * 仿淘宝首页的 淘宝头条滚动的自定义View
 */
class RxTextViewVerticalMore(context: Context, attrs: AttributeSet) : ViewFlipper(context, attrs) {
    private var mContext: Context? = null
    private val isSetAnimDuration = false
    private val interval = 5000

    /**
     * 动画时间
     */
    private val animDuration = 500
    private fun init(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        mContext = context
        flipInterval = interval
        val animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in)
        if (isSetAnimDuration) {
            animIn.duration = animDuration.toLong()
        }
        inAnimation = animIn
        val animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out)
        if (isSetAnimDuration) {
            animOut.duration = animDuration.toLong()
        }
        outAnimation = animOut
    }

    /**
     * 设置循环滚动的View数组
     *
     * @param views
     */
    fun setViews(views: List<View>?) {
        if (views == null || views.size == 0) {
            return
        }
        removeAllViews()
        for (i in views.indices) {
            //设置监听回调
            views[i].setOnClickListener {
                if (onItemClickListener != null) {
                    onItemClickListener!!.onItemClick(i, views[i])
                }
            }
            addView(views[i])
        }
        startFlipping()
    }

    /**
     * 点击
     */
    private var onItemClickListener: OnItemClickListener? = null

    /**
     * 设置监听接口
     *
     * @param onItemClickListener
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    /**
     * item_view的接口
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View?)
    }

    init {
        init(context, attrs, 0)
    }
}