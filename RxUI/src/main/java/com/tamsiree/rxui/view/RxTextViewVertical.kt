package com.tamsiree.rxui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.ViewSwitcher
import java.util.*

/**
 * @author tamsiree
 */
class RxTextViewVertical(private var mContext: Context?, attrs: AttributeSet?) : TextSwitcher(mContext, attrs), ViewSwitcher.ViewFactory {
    private var mTextSize = 16f
    private var mPadding = 5
    private var textColor = Color.BLACK
    private var itemClickListener: OnItemClickListener? = null
    private var currentId = -1
    private val textList: ArrayList<String>
    private var mHandler: Handler? = null

    constructor(context: Context?) : this(context, null) {
        mContext = context
    }

    /**
     * @param textSize  字号
     * @param padding   内边距
     * @param textColor 字体颜色
     */
    fun setText(textSize: Float, padding: Int, textColor: Int) {
        mTextSize = textSize
        mPadding = padding
        this.textColor = textColor
    }

    fun setAnimTime(animDuration: Long) {
        setFactory(this)
        val `in`: Animation = TranslateAnimation(0f, 0f, animDuration.toFloat(), 0f)
        `in`.duration = animDuration
        `in`.interpolator = AccelerateInterpolator()
        val out: Animation = TranslateAnimation(0f, 0f, 0f, (-animDuration).toFloat())
        out.duration = animDuration
        out.interpolator = AccelerateInterpolator()
        inAnimation = `in`
        outAnimation = out
    }

    /**
     * 间隔时间
     *
     * @param time
     */
    @SuppressLint("HandlerLeak")
    fun setTextStillTime(time: Long) {
        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    FLAG_START_AUTO_SCROLL -> {
                        if (textList.size > 0) {
                            currentId++
                            setText(textList[currentId % textList.size])
                        }
                        mHandler!!.sendEmptyMessageDelayed(FLAG_START_AUTO_SCROLL, time)
                    }
                    FLAG_STOP_AUTO_SCROLL -> mHandler!!.removeMessages(FLAG_START_AUTO_SCROLL)
                    else -> {
                    }
                }
            }
        }
    }

    /**
     * 设置数据源
     *
     * @param titles
     */
    fun setTextList(titles: ArrayList<String>?) {
        textList.clear()
        textList.addAll(titles!!)
        currentId = -1
    }

    /**
     * 开始滚动
     */
    fun startAutoScroll() {
        mHandler!!.sendEmptyMessage(FLAG_START_AUTO_SCROLL)
    }

    /**
     * 停止滚动
     */
    fun stopAutoScroll() {
        mHandler!!.sendEmptyMessage(FLAG_STOP_AUTO_SCROLL)
    }

    override fun makeView(): View {
        val t = TextView(mContext)
        t.gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
        t.maxLines = 1
        t.setPadding(mPadding, mPadding, mPadding, mPadding)
        t.setTextColor(textColor)
        t.textSize = mTextSize
        t.isClickable = true
        t.setOnClickListener {
            if (itemClickListener != null && textList.size > 0 && currentId != -1) {
                itemClickListener!!.onItemClick(currentId % textList.size)
            }
        }
        return t
    }

    /**
     * 设置点击事件监听
     *
     * @param itemClickListener
     */
    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    /**
     * 轮播文本点击监听器
     */
    interface OnItemClickListener {
        /**
         * 点击回调
         *
         * @param position 当前点击ID
         */
        fun onItemClick(position: Int)
    }

    companion object {
        private const val FLAG_START_AUTO_SCROLL = 0
        private const val FLAG_STOP_AUTO_SCROLL = 1
    }

    init {
        textList = ArrayList()
    }
}