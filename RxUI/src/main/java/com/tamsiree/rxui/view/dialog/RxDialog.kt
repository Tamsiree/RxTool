package com.tamsiree.rxui.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.tamsiree.rxui.R
import java.util.*

/**
 * @author tamsiree
 */
open class RxDialog : Dialog {

    protected lateinit var mContext: Context
    var layoutParams: WindowManager.LayoutParams? = null

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        initView(context)
    }

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {
        initView(context)
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    private fun initView(context: Context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        Objects.requireNonNull(this.window).setBackgroundDrawableResource(R.drawable.transparent_bg)
        mContext = context
        val window = this.window
        layoutParams = window!!.attributes
        layoutParams?.alpha = 1f
        window.attributes = layoutParams
        if (layoutParams != null) {
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams?.gravity = Gravity.CENTER
        }
    }

    /**
     * @param context 实体
     * @param alpha   透明度 0.0f--1f(不透明)
     * @param gravity 方向(Gravity.BOTTOM,Gravity.TOP,Gravity.LEFT,Gravity.RIGHT)
     */
    constructor(context: Context?, alpha: Float, gravity: Int) : super(context!!) {
        // TODO Auto-generated constructor stub
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        Objects.requireNonNull(this.window).setBackgroundDrawableResource(R.drawable.transparent_bg)
        mContext = context
        val window = this.window
        layoutParams = window!!.attributes
        layoutParams?.alpha = 1f
        window.attributes = layoutParams
        if (layoutParams != null) {
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams?.gravity = gravity
        }
    }

    /**
     * 隐藏头部导航栏状态栏
     */
    fun skipTools() {
        Objects.requireNonNull(window).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 设置全屏显示
     */
    fun setFullScreen() {
        val window = window!!
        window.decorView.setPadding(0, 0, 0, 0)
        val lp = window.attributes
        lp.width = WindowManager.LayoutParams.FILL_PARENT
        lp.height = WindowManager.LayoutParams.FILL_PARENT
        window.attributes = lp
    }

    /**
     * 设置宽度match_parent
     */
    fun setFullScreenWidth() {
        val window = window!!
        window.decorView.setPadding(0, 0, 0, 0)
        val lp = window.attributes
        lp.width = WindowManager.LayoutParams.FILL_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
    }

    /**
     * 设置高度为match_parent
     */
    fun setFullScreenHeight() {
        val window = window!!
        window.decorView.setPadding(0, 0, 0, 0)
        val lp = window.attributes
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.FILL_PARENT
        window.attributes = lp
    }

    fun setOnWhole() {
        Objects.requireNonNull(window).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
    }
}