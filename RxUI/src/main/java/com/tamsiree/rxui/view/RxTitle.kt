package com.tamsiree.rxui.view

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tamsiree.rxkit.RxActivityTool
import com.tamsiree.rxkit.RxDataTool.Companion.isNullString
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxkit.RxKeyboardTool.hideSoftInput
import com.tamsiree.rxui.R

/**
 * @ClassName RxTitle
 * @Description TODO
 * @Author tamsiree
 * @Date 2017/1/2 下午 5:58
 * @Version 1.0
 */
class RxTitle : FrameLayout {

    //*******************************************控件start******************************************
    private var mRootView: View? = null

    //根布局
    private var mRootLayout: LinearLayout? = null

    //Title的TextView控件
    private var mTvTitle: RxTextAutoZoom? = null

    //左边布局
    private var mLlLeft: LinearLayout? = null

    //左边ImageView控件的背景
    private var mllIconLeftBg: LinearLayout? = null

    //左边ImageView控件
    private var mIvLeft: ImageView? = null

    //左边TextView控件
    private var mTvLeft: TextView? = null

    //右边布局
    private var mLlRight: LinearLayout? = null

    //左边ImageView控件的背景
    private var mllIconRightBg: LinearLayout? = null

    //右边ImageView控件
    private var mIvRight: ImageView? = null

    //右边TextView控件
    private var mTvRight: TextView? = null

    //===========================================控件end=============================================

    //*******************************************属性start*******************************************
    //Title文字
    private var mTitle: String? = null

    //Title字体颜色
    private var mTitleColor = 0

    //Title字体大小
    private var mTitleSize = 0

    //Title是否显示
    private var mTitleVisibility = false

    //------------------------------------------左侧布局---------------------------------------------
    //左边 ICON 引用的资源ID
    private var mLeftIcon = 0

    //左边 ICON 是否显示
    private var mLeftIconVisibility = false

    //左边文字
    private var mLeftText: String? = null

    //左边字体颜色
    private var mLeftTextColor = 0

    //左边字体大小
    private var mLeftTextSize = 0

    //左边文字是否显示
    private var mLeftTextVisibility = false

    //--------------------------------------------右侧布局-------------------------------------------

    //右边 ICON 引用的资源ID
    private var mRightIcon = 0

    //右边文字
    private var mRightText: String? = null

    //右边 ICON 是否显示
    private var mRightIconVisibility = false

    //右边字体颜色
    private var mRightTextColor = 0

    //右边字体大小
    private var mRightTextSize = 0

    //右边文字是否显示
    private var mRightTextVisibility = false


    //===========================================属性end=============================================
    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        //导入布局
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.include_rx_title, this)
        mRootLayout = findViewById(R.id.root_layout)
        mTvTitle = findViewById(R.id.tv_rx_title)
        mLlLeft = findViewById(R.id.ll_left)
        mllIconLeftBg = findViewById(R.id.ll_icon_left_bg)
        mllIconRightBg = findViewById(R.id.ll_icon_right_bg)
        mIvLeft = findViewById(R.id.iv_left)
        mIvRight = findViewById(R.id.iv_right)
        mLlRight = findViewById(R.id.ll_right)
        mTvLeft = findViewById(R.id.tv_left)
        mTvRight = findViewById(R.id.tv_right)

        //获得这个控件对应的属性。
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.RxTitle)
        try {
            //获得属性值
            //getColor(R.styleable.RxTitle_RxBackground, getResources().getColor(R.color.transparent))
            //标题
            mTitle = a.getString(R.styleable.RxTitle_title)
            //标题颜色
            mTitleColor = a.getColor(R.styleable.RxTitle_titleColor, resources.getColor(R.color.white))

            //标题字体大小
            mTitleSize = a.getDimensionPixelSize(R.styleable.RxTitle_titleSize, dp2px(context, 20f))
            //TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())
            mTitleVisibility = a.getBoolean(R.styleable.RxTitle_titleVisibility, true)

            //左边图标
            mLeftIcon = a.getResourceId(R.styleable.RxTitle_leftIcon, R.drawable.ic_back)
            //右边图标
            mRightIcon = a.getResourceId(R.styleable.RxTitle_rightIcon, R.drawable.set)
            //左边图标是否显示
            mLeftIconVisibility = a.getBoolean(R.styleable.RxTitle_leftIconVisibility, true)
            //右边图标是否显示
            mRightIconVisibility = a.getBoolean(R.styleable.RxTitle_rightIconVisibility, false)
            mLeftText = a.getString(R.styleable.RxTitle_leftText)
            //左边字体颜色
            mLeftTextColor = a.getColor(R.styleable.RxTitle_leftTextColor, resources.getColor(R.color.white))
            //左侧标题字体大小
            mLeftTextSize = a.getDimensionPixelSize(R.styleable.RxTitle_leftTextSize, dp2px(context, 8f))
            mLeftTextVisibility = a.getBoolean(R.styleable.RxTitle_leftTextVisibility, false)
            mRightText = a.getString(R.styleable.RxTitle_rightText)
            //右边字体颜色
            mRightTextColor = a.getColor(R.styleable.RxTitle_rightTextColor, resources.getColor(R.color.white))
            //标题字体大小
            mRightTextSize = a.getDimensionPixelSize(R.styleable.RxTitle_rightTextSize, dp2px(context, 8f))
            mRightTextVisibility = a.getBoolean(R.styleable.RxTitle_rightTextVisibility, false)
        } finally {
            //回收这个对象
            a.recycle()
        }

        //******************************************************************************************以下属性初始化
        if (!isNullString(mTitle)) {
            setTitle(mTitle)
        }
        if (mTitleColor != 0) {
            setTitleColor(mTitleColor)
        }
        if (mTitleSize != 0) {
            setTitleSize(mTitleSize)
        }
        if (mLeftIcon != 0) {
            setLeftIcon(mLeftIcon)
        }
        if (mRightIcon != 0) {
            setRightIcon(mRightIcon)
        }
        setTitleVisibility(mTitleVisibility)
        setLeftText(mLeftText)
        setLeftTextColor(mLeftTextColor)
        setLeftTextSize(mLeftTextSize)
        setLeftTextVisibility(mLeftTextVisibility)
        setRightText(mRightText)
        setRightTextColor(mRightTextColor)
        setRightTextSize(mRightTextSize)
        setRightTextVisibility(mRightTextVisibility)
        setLeftIconVisibility(mLeftIconVisibility)
        setRightIconVisibility(mRightIconVisibility)
        initAutoFitEditText(context)
        //==========================================================================================以上为属性初始化
    }

    private fun initAutoFitEditText(context: Context) {
        mTvTitle?.clearFocus()
        mTvTitle?.isEnabled = false
        mTvTitle?.isFocusableInTouchMode = false
        mTvTitle?.isFocusable = false
        mTvTitle?.setEnableSizeCache(false)
        //might cause crash on some devices
        mTvTitle?.movementMethod = null
        // can be added after layout inflation;
        mTvTitle?.maxHeight = dp2px(context, 55f)
        //don't forget to add min text size programmatically
        mTvTitle?.minTextSize = 37f
        try {
            RxTextAutoZoom.setNormalization(getContext() as Activity, mRootLayout, mTvTitle)
            hideSoftInput((getContext() as Activity))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //**********************************************************************************************以下为get方法
    fun getRootLayout(): LinearLayout? {
        return mRootLayout
    }

    fun getTitleTextView(): RxTextAutoZoom? {
        return mTvTitle
    }

    fun getLeftLayout(): LinearLayout? {
        return mLlLeft
    }

    fun getLeftImageView(): ImageView? {
        return mIvLeft
    }

    fun getLeftTextView(): TextView? {
        return mTvLeft
    }

    fun getRightLayout(): LinearLayout? {
        return mLlRight
    }

    fun getRightImageView(): ImageView? {
        return mIvRight
    }

    fun getRightTextView(): TextView? {
        return mTvRight
    }

    fun isTitleVisibility(): Boolean {
        return mTitleVisibility
    }

    fun getLeftIconBg(): LinearLayout? {
        return mllIconLeftBg
    }

    fun getRightIconBg(): LinearLayout? {
        return mllIconRightBg
    }

    fun setTitleVisibility(titleVisibility: Boolean) {
        mTitleVisibility = titleVisibility
        if (mTitleVisibility) {
            mTvTitle?.visibility = View.VISIBLE
        } else {
            mTvTitle?.visibility = View.GONE
        }
    }

    fun getLeftText(): String? {
        return mLeftText
    }

    //**********************************************************************************************以下为  左边文字  相关方法
    fun setLeftText(leftText: String?): Unit {
        mLeftText = leftText
        mTvLeft?.text = mLeftText
    }

    fun getLeftTextColor(): Int {
        return mLeftTextColor
    }

    fun setLeftTextColor(leftTextColor: Int) {
        mLeftTextColor = leftTextColor
        mTvLeft?.setTextColor(mLeftTextColor)
    }

    fun getLeftTextSize(): Int {
        return mLeftTextSize
    }

    fun setLeftTextSize(leftTextSize: Int) {
        mLeftTextSize = leftTextSize
        mTvLeft?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize.toFloat())
    }

    fun isLeftTextVisibility(): Boolean {
        return mLeftTextVisibility
    }

    fun setLeftTextVisibility(leftTextVisibility: Boolean) {
        mLeftTextVisibility = leftTextVisibility
        if (mLeftTextVisibility) {
            mTvLeft?.visibility = View.VISIBLE
        } else {
            mTvLeft?.visibility = View.GONE
        }
    }

    fun getRightText(): String? {
        return mRightText
    }

    //**********************************************************************************************以下为  右边文字  相关方法
    fun setRightText(rightText: String?) {
        mRightText = rightText
        mTvRight?.text = mRightText
    }

    fun getRightTextColor(): Int {
        return mRightTextColor
    }

    fun setRightTextColor(rightTextColor: Int) {
        mRightTextColor = rightTextColor
        mTvRight?.setTextColor(mRightTextColor)
    }

    fun getRightTextSize(): Int {
        return mRightTextSize
    }

    fun setRightTextSize(rightTextSize: Int) {
        mRightTextSize = rightTextSize
        mTvRight?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize.toFloat())
    }

    //==============================================================================================以上为get方法


    //**********************************************************************************************以下为set方法
    fun isRightTextVisibility(): Boolean {
        return mRightTextVisibility
    }

    fun setRightTextVisibility(rightTextVisibility: Boolean) {
        mRightTextVisibility = rightTextVisibility
        if (mRightTextVisibility) {
            mTvRight?.visibility = View.VISIBLE
            if (isRightIconVisibility()) {
                mTvRight?.setPadding(0, 0, 0, 0)
            }
        } else {
            mTvRight?.visibility = View.GONE
        }
    }

    fun getTitle(): String? {
        return mTitle
    }

    //**********************************************************************************************以下为Title相关方法
    fun setTitle(title: String?) {
        mTitle = title
        mTvTitle?.setText(mTitle)
    }

    fun getTitleColor(): Int {
        return mTitleColor
    }

    fun setTitleColor(titleColor: Int) {
        mTitleColor = titleColor
        mTvTitle?.setTextColor(mTitleColor)
    }

    fun getTitleSize(): Int {
        return mTitleSize
    }

    fun setTitleSize(titleSize: Int) {
        mTitleSize = titleSize
        mTvTitle?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize.toFloat())
    }

    fun getLeftIcon(): Int {
        return mLeftIcon
    }

    fun setLeftIcon(leftIcon: Int) {
        mLeftIcon = leftIcon
        mIvLeft?.setImageResource(mLeftIcon)
    }

    fun getRightIcon(): Int {
        return mRightIcon
    }

    //==============================================================================================以上为  Title  相关方法
    fun setRightIcon(rightIcon: Int) {
        mRightIcon = rightIcon
        mIvRight?.setImageResource(mRightIcon)
    }

    fun isLeftIconVisibility(): Boolean {
        return mLeftIconVisibility
    }

    fun setLeftIconVisibility(leftIconVisibility: Boolean) {
        mLeftIconVisibility = leftIconVisibility
        if (mLeftIconVisibility) {
            mIvLeft?.visibility = View.VISIBLE
        } else {
            mIvLeft?.visibility = View.GONE
        }
    }

    fun isRightIconVisibility(): Boolean {
        return mRightIconVisibility
    }

    //==============================================================================================以上为  左边文字  相关方法
    fun setRightIconVisibility(rightIconVisibility: Boolean) {
        mRightIconVisibility = rightIconVisibility
        if (mRightIconVisibility) {
            mIvRight?.visibility = View.VISIBLE
        } else {
            mIvRight?.visibility = View.GONE
        }
    }

    fun setLeftFinish(activity: Activity) {
        mLlLeft?.setOnClickListener { v: View? -> activity.finish() }
    }

    fun setLeftFinish(activity: Activity, isFade: Boolean, isTransition: Boolean) {
        mLlLeft?.setOnClickListener { v: View? ->
            RxActivityTool.finishActivity(activity, isTransition)
            if (isFade) {
                RxActivityTool.fadeTransition(activity)
            }
        }
    }

    fun setLeftOnClickListener(onClickListener: OnClickListener?) {
        mLlLeft?.setOnClickListener(onClickListener)
    }

    fun setRightOnClickListener(onClickListener: OnClickListener?) {
        mLlRight?.setOnClickListener(onClickListener)
    }

    //==============================================================================================以上为  右边文字  相关方法
    fun setLeftTextOnClickListener(onClickListener: OnClickListener?) {
        mTvLeft?.setOnClickListener(onClickListener)
    }

    fun setRightTextOnClickListener(onClickListener: OnClickListener?) {
        mTvRight?.setOnClickListener(onClickListener)
    }

    fun setLeftIconOnClickListener(onClickListener: OnClickListener?) {
        mIvLeft?.setOnClickListener(onClickListener)
    }

    fun setRightIconOnClickListener(onClickListener: OnClickListener?) {
        mIvRight?.setOnClickListener(onClickListener)
    }

}