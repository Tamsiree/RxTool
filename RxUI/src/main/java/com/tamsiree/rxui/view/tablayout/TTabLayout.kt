package com.tamsiree.rxui.view.tablayout

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxkit.RxImageTool.sp2px
import com.tamsiree.rxui.R
import com.tamsiree.rxui.fragment.TFragmentManager
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener
import com.tamsiree.rxui.view.tablayout.listener.TabLayoutModel
import com.tamsiree.rxui.view.tablayout.tool.TLayoutMsgTool.show
import java.util.*

/**
 * 没有继承HorizontalScrollView不能滑动,对于ViewPager无依赖
 */
class TTabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {
    private val mContext: Context
    private val mTabEntitys = ArrayList<TabLayoutModel>()
    private val mTabsContainer: LinearLayout
    private var mCurrentTab = 0
    private var mLastTab = 0
    var tabCount = 0
        private set

    /**
     * 用于绘制显示器
     */
    private val mIndicatorRect = Rect()
    private val mIndicatorDrawable = GradientDrawable()
    private val mRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mDividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrianglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrianglePath = Path()
    private var mIndicatorStyle = STYLE_NORMAL
    private var mTabPadding = 0f
    private var mTabSpaceEqual = false
    private var mTabWidth = 0f

    /**
     * indicator
     */
    private var mIndicatorColor = 0
    private var mIndicatorHeight = 0f
    private var mIndicatorWidth = 0f
    private var mIndicatorCornerRadius = 0f
    var indicatorMarginLeft = 0f
        private set
    var indicatorMarginTop = 0f
        private set
    var indicatorMarginRight = 0f
        private set
    var indicatorMarginBottom = 0f
        private set
    var indicatorAnimDuration: Long = 0
    var isIndicatorAnimEnable = false
    var isIndicatorBounceEnable = false
    private var mIndicatorGravity = 0

    /**
     * underline
     */
    private var mUnderlineColor = 0
    private var mUnderlineHeight = 0f
    private var mUnderlineGravity = 0

    /**
     * divider
     */
    private var mDividerColor = 0
    private var mDividerWidth = 0f
    private var mDividerPadding = 0f
    private var mTextsize = 0f
    private var mTextSelectColor = 0
    private var mTextUnselectColor = 0
    private var mTextBold = 0
    private var mTextAllCaps = false

    /**
     * icon
     */
    private var mIconVisible = false
    private var mIconGravity = 0
    private var mIconWidth = 0f
    private var mIconHeight = 0f
    private var mIconMargin = 0f
    private var mHeight = 0

    /**
     * anim
     */
    private val mValueAnimator: ValueAnimator
    private val mInterpolator = OvershootInterpolator(1.5f)
    private var mTFragmentManager: TFragmentManager? = null
    private var mIsFirstDraw = true

    // show MsgTipView
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    @SuppressLint("UseSparseArrays")
    private val mInitSetMap = SparseArray<Boolean?>()
    private var mListener: OnTabSelectListener? = null
    private val mCurrentP = IndicatorPoint()
    private val mLastP = IndicatorPoint()
    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TTabLayout)
        mIndicatorStyle = ta.getInt(R.styleable.TTabLayout_indicator_style, 0)
        mIndicatorColor = ta.getColor(R.styleable.TTabLayout_indicator_color, Color.parseColor(if (mIndicatorStyle == STYLE_BLOCK) "#4B6A87" else "#ffffff"))
        mIndicatorHeight = ta.getDimension(R.styleable.TTabLayout_indicator_height, dp2px(if (mIndicatorStyle == STYLE_TRIANGLE) 4f else (if (mIndicatorStyle == STYLE_BLOCK) -1 else 2).toFloat()).toFloat())
        mIndicatorWidth = ta.getDimension(R.styleable.TTabLayout_indicator_width, dp2px(if (mIndicatorStyle == STYLE_TRIANGLE) 10f else -1.toFloat()).toFloat())
        mIndicatorCornerRadius = ta.getDimension(R.styleable.TTabLayout_indicator_corner_radius, dp2px(if (mIndicatorStyle == STYLE_BLOCK) -1f else 0.toFloat()).toFloat())
        indicatorMarginLeft = ta.getDimension(R.styleable.TTabLayout_indicator_margin_left, dp2px(0f).toFloat())
        indicatorMarginTop = ta.getDimension(R.styleable.TTabLayout_indicator_margin_top, dp2px(if (mIndicatorStyle == STYLE_BLOCK) 7f else 0.toFloat()).toFloat())
        indicatorMarginRight = ta.getDimension(R.styleable.TTabLayout_indicator_margin_right, dp2px(0f).toFloat())
        indicatorMarginBottom = ta.getDimension(R.styleable.TTabLayout_indicator_margin_bottom, dp2px(if (mIndicatorStyle == STYLE_BLOCK) 7f else 0.toFloat()).toFloat())
        isIndicatorAnimEnable = ta.getBoolean(R.styleable.TTabLayout_indicator_anim_enable, true)
        isIndicatorBounceEnable = ta.getBoolean(R.styleable.TTabLayout_indicator_bounce_enable, true)
        indicatorAnimDuration = ta.getInt(R.styleable.TTabLayout_indicator_anim_duration, -1).toLong()
        mIndicatorGravity = ta.getInt(R.styleable.TTabLayout_indicator_gravity, Gravity.BOTTOM)
        mUnderlineColor = ta.getColor(R.styleable.TTabLayout_underline_color, Color.parseColor("#ffffff"))
        mUnderlineHeight = ta.getDimension(R.styleable.TTabLayout_underline_height, dp2px(0f).toFloat())
        mUnderlineGravity = ta.getInt(R.styleable.TTabLayout_underline_gravity, Gravity.BOTTOM)
        mDividerColor = ta.getColor(R.styleable.TTabLayout_divider_color, Color.parseColor("#ffffff"))
        mDividerWidth = ta.getDimension(R.styleable.TTabLayout_divider_width, dp2px(0f).toFloat())
        mDividerPadding = ta.getDimension(R.styleable.TTabLayout_divider_padding, dp2px(12f).toFloat())
        mTextsize = ta.getDimension(R.styleable.TTabLayout_titlesize, sp2px(13f).toFloat())
        mTextSelectColor = ta.getColor(R.styleable.TTabLayout_textSelectColor, Color.parseColor("#ffffff"))
        mTextUnselectColor = ta.getColor(R.styleable.TTabLayout_textUnSelectColor, Color.parseColor("#AAffffff"))
        mTextBold = ta.getInt(R.styleable.TTabLayout_textBold, TEXT_BOLD_NONE)
        mTextAllCaps = ta.getBoolean(R.styleable.TTabLayout_textAllCaps, false)
        mIconVisible = ta.getBoolean(R.styleable.TTabLayout_iconVisible, true)
        mIconGravity = ta.getInt(R.styleable.TTabLayout_icon_gravity, Gravity.TOP)
        mIconWidth = ta.getDimension(R.styleable.TTabLayout_iconWidth, dp2px(0f).toFloat())
        mIconHeight = ta.getDimension(R.styleable.TTabLayout_iconHeight, dp2px(0f).toFloat())
        mIconMargin = ta.getDimension(R.styleable.TTabLayout_iconMargin, dp2px(2.5f).toFloat())
        mTabSpaceEqual = ta.getBoolean(R.styleable.TTabLayout_tab_space_equal, true)
        mTabWidth = ta.getDimension(R.styleable.TTabLayout_tab_width, dp2px(-1f).toFloat())
        mTabPadding = ta.getDimension(R.styleable.TTabLayout_tab_padding, if (mTabSpaceEqual || mTabWidth > 0) dp2px(0f).toFloat() else dp2px(10f).toFloat())
        ta.recycle()
    }

    fun setTabData(tabEntitys: ArrayList<TabLayoutModel>?) {
        check(!(tabEntitys == null || tabEntitys.size == 0)) { "TabEntitys can not be NULL or EMPTY !" }
        mTabEntitys.clear()
        mTabEntitys.addAll(tabEntitys)
        notifyDataSetChanged()
    }

    /**
     * 关联数据支持同时切换fragments
     */
    fun setTabData(tabModels: ArrayList<TabLayoutModel>?, fa: FragmentActivity, containerViewId: Int, fragments: ArrayList<Fragment>) {
        mTFragmentManager = TFragmentManager(fa.supportFragmentManager, containerViewId, fragments)
        setTabData(tabModels)
    }

    /**
     * 更新数据
     */
    fun notifyDataSetChanged() {
        mTabsContainer.removeAllViews()
        tabCount = mTabEntitys.size
        var tabView: View
        for (i in 0 until tabCount) {
            tabView = when (mIconGravity) {
                Gravity.LEFT -> {
                    View.inflate(mContext, R.layout.layout_tab_left, null)
                }
                Gravity.RIGHT -> {
                    View.inflate(mContext, R.layout.layout_tab_right, null)
                }
                Gravity.BOTTOM -> {
                    View.inflate(mContext, R.layout.layout_tab_bottom, null)
                }
                else -> {
                    View.inflate(mContext, R.layout.layout_tab_top, null)
                }
            }
            tabView.tag = i
            addTab(i, tabView)
        }
        updateTabStyles()
    }

    /**
     * 创建并添加tab
     */
    private fun addTab(position: Int, tabView: View) {
        val tv_tab_title = tabView.findViewById<TextView>(R.id.tv_tab_title)
        tv_tab_title.text = mTabEntitys[position].tabTitle
        val iv_tab_icon = tabView.findViewById<ImageView>(R.id.iv_tab_icon)
        iv_tab_icon.setImageResource(mTabEntitys[position].tabUnselectedIcon)
        tabView.setOnClickListener { v: View ->
            val position1 = v.tag as Int
            if (mCurrentTab != position1) {
                currentTab = position1
                if (mListener != null) {
                    mListener!!.onTabSelect(position1)
                }
            } else {
                if (mListener != null) {
                    mListener!!.onTabReselect(position1)
                }
            }
        }

        /* 每一个Tab的布局参数 */
        var lp_tab = if (mTabSpaceEqual) LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) else LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        if (mTabWidth > 0) {
            lp_tab = LinearLayout.LayoutParams(mTabWidth.toInt(), LayoutParams.MATCH_PARENT)
        }
        mTabsContainer.addView(tabView, position, lp_tab)
    }

    private fun updateTabStyles() {
        for (i in 0 until tabCount) {
            val tabView = mTabsContainer.getChildAt(i)
            tabView.setPadding(mTabPadding.toInt(), 0, mTabPadding.toInt(), 0)
            val tv_tab_title = tabView.findViewById<TextView>(R.id.tv_tab_title)
            tv_tab_title.setTextColor(if (i == mCurrentTab) mTextSelectColor else mTextUnselectColor)
            tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextsize)
            //            tv_tab_title.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
            if (mTextAllCaps) {
                tv_tab_title.text = tv_tab_title.text.toString().toUpperCase()
            }
            if (mTextBold == TEXT_BOLD_BOTH) {
                tv_tab_title.paint.isFakeBoldText = true
            } else if (mTextBold == TEXT_BOLD_NONE) {
                tv_tab_title.paint.isFakeBoldText = false
            }
            val iv_tab_icon = tabView.findViewById<ImageView>(R.id.iv_tab_icon)
            if (mIconVisible) {
                iv_tab_icon.visibility = View.VISIBLE
                val tabEntity = mTabEntitys[i]
                iv_tab_icon.setImageResource(if (i == mCurrentTab) tabEntity.tabSelectedIcon else tabEntity.tabUnselectedIcon)
                val lp = LinearLayout.LayoutParams(
                        if (mIconWidth <= 0) LinearLayout.LayoutParams.WRAP_CONTENT else mIconWidth.toInt(),
                        if (mIconHeight <= 0) LinearLayout.LayoutParams.WRAP_CONTENT else mIconHeight.toInt())
                if (mIconGravity == Gravity.LEFT) {
                    lp.rightMargin = mIconMargin.toInt()
                } else if (mIconGravity == Gravity.RIGHT) {
                    lp.leftMargin = mIconMargin.toInt()
                } else if (mIconGravity == Gravity.BOTTOM) {
                    lp.topMargin = mIconMargin.toInt()
                } else {
                    lp.bottomMargin = mIconMargin.toInt()
                }
                iv_tab_icon.layoutParams = lp
            } else {
                iv_tab_icon.visibility = View.GONE
            }
        }
    }

    private fun updateTabSelection(position: Int) {
        for (i in 0 until tabCount) {
            val tabView = mTabsContainer.getChildAt(i)
            val isSelect = i == position
            val tab_title = tabView.findViewById<TextView>(R.id.tv_tab_title)
            tab_title.setTextColor(if (isSelect) mTextSelectColor else mTextUnselectColor)
            val iv_tab_icon = tabView.findViewById<ImageView>(R.id.iv_tab_icon)
            val tabEntity = mTabEntitys[i]
            iv_tab_icon.setImageResource(if (isSelect) tabEntity.tabSelectedIcon else tabEntity.tabUnselectedIcon)
            if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                tab_title.paint.isFakeBoldText = isSelect
            }
        }
    }

    private fun calcOffset() {
        val currentTabView = mTabsContainer.getChildAt(mCurrentTab)
        mCurrentP.left = currentTabView.left.toFloat()
        mCurrentP.right = currentTabView.right.toFloat()
        val lastTabView = mTabsContainer.getChildAt(mLastTab)
        mLastP.left = lastTabView.left.toFloat()
        mLastP.right = lastTabView.right.toFloat()

//        RxLogTool.d("AAA", "mLastP--->" + mLastP.left + "&" + mLastP.right);
//        RxLogTool.d("AAA", "mCurrentP--->" + mCurrentP.left + "&" + mCurrentP.right);
        if (mLastP.left == mCurrentP.left && mLastP.right == mCurrentP.right) {
            invalidate()
        } else {
            mValueAnimator.setObjectValues(mLastP, mCurrentP)
            if (isIndicatorBounceEnable) {
                mValueAnimator.interpolator = mInterpolator
            }
            if (indicatorAnimDuration < 0) {
                indicatorAnimDuration = if (isIndicatorBounceEnable) 500 else 250.toLong()
            }
            mValueAnimator.duration = indicatorAnimDuration
            mValueAnimator.start()
        }
    }

    private fun calcIndicatorRect() {
        val currentTabView = mTabsContainer.getChildAt(mCurrentTab)
        val left = currentTabView.left.toFloat()
        val right = currentTabView.right.toFloat()
        mIndicatorRect.left = left.toInt()
        mIndicatorRect.right = right.toInt()
        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip
        } else { //indicatorWidth大于0时,圆角矩形以及三角形
            val indicatorLeft = currentTabView.left + (currentTabView.width - mIndicatorWidth) / 2
            mIndicatorRect.left = indicatorLeft.toInt()
            mIndicatorRect.right = (mIndicatorRect.left + mIndicatorWidth).toInt()
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val currentTabView = mTabsContainer.getChildAt(mCurrentTab)
        val p = animation.animatedValue as IndicatorPoint
        mIndicatorRect.left = p.left.toInt()
        mIndicatorRect.right = p.right.toInt()
        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip
        } else { //indicatorWidth大于0时,圆角矩形以及三角形
            val indicatorLeft = p.left + (currentTabView.width - mIndicatorWidth) / 2
            mIndicatorRect.left = indicatorLeft.toInt()
            mIndicatorRect.right = (mIndicatorRect.left + mIndicatorWidth).toInt()
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isInEditMode || tabCount <= 0) {
            return
        }
        val height = height
        val paddingLeft = paddingLeft
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.strokeWidth = mDividerWidth
            mDividerPaint.color = mDividerColor
            for (i in 0 until tabCount - 1) {
                val tab = mTabsContainer.getChildAt(i)
                canvas.drawLine(paddingLeft + tab.right.toFloat(), mDividerPadding, paddingLeft + tab.right.toFloat(), height - mDividerPadding, mDividerPaint)
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.color = mUnderlineColor
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft.toFloat(), height - mUnderlineHeight, mTabsContainer.width + paddingLeft.toFloat(), height.toFloat(), mRectPaint)
            } else {
                canvas.drawRect(paddingLeft.toFloat(), 0f, mTabsContainer.width + paddingLeft.toFloat(), mUnderlineHeight, mRectPaint)
            }
        }

        //draw indicator line
        if (isIndicatorAnimEnable) {
            if (mIsFirstDraw) {
                mIsFirstDraw = false
                calcIndicatorRect()
            }
        } else {
            calcIndicatorRect()
        }
        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.color = mIndicatorColor
                mTrianglePath.reset()
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left.toFloat(), height.toFloat())
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2f + mIndicatorRect.right / 2f, height - mIndicatorHeight)
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right.toFloat(), height.toFloat())
                mTrianglePath.close()
                canvas.drawPath(mTrianglePath, mTrianglePaint)
            }
        } else if (mIndicatorStyle == STYLE_BLOCK) {
            if (mIndicatorHeight < 0) {
                mIndicatorHeight = height - indicatorMarginTop - indicatorMarginBottom
            } else {
            }
            if (mIndicatorHeight > 0) {
                if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                    mIndicatorCornerRadius = mIndicatorHeight / 2
                }
                mIndicatorDrawable.setColor(mIndicatorColor)
                mIndicatorDrawable.setBounds(paddingLeft + indicatorMarginLeft.toInt() + mIndicatorRect.left,
                        indicatorMarginTop.toInt(), (paddingLeft + mIndicatorRect.right - indicatorMarginRight).toInt(),
                        (indicatorMarginTop + mIndicatorHeight).toInt())
                mIndicatorDrawable.cornerRadius = mIndicatorCornerRadius
                mIndicatorDrawable.draw(canvas)
            }
        } else {
            /* mRectPaint.setColor(mIndicatorColor);
                calcIndicatorRect();
                canvas.drawRect(getPaddingLeft() + mIndicatorRect.left, getHeight() - mIndicatorHeight,
                        mIndicatorRect.right + getPaddingLeft(), getHeight(), mRectPaint);*/
            if (mIndicatorHeight > 0) {
                mIndicatorDrawable.setColor(mIndicatorColor)
                if (mIndicatorGravity == Gravity.BOTTOM) {
                    mIndicatorDrawable.setBounds(paddingLeft + indicatorMarginLeft.toInt() + mIndicatorRect.left,
                            height - mIndicatorHeight.toInt() - indicatorMarginBottom.toInt(),
                            paddingLeft + mIndicatorRect.right - indicatorMarginRight.toInt(),
                            height - indicatorMarginBottom.toInt())
                } else {
                    mIndicatorDrawable.setBounds(paddingLeft + indicatorMarginLeft.toInt() + mIndicatorRect.left,
                            indicatorMarginTop.toInt(),
                            paddingLeft + mIndicatorRect.right - indicatorMarginRight.toInt(),
                            mIndicatorHeight.toInt() + indicatorMarginTop.toInt())
                }
                mIndicatorDrawable.cornerRadius = mIndicatorCornerRadius
                mIndicatorDrawable.draw(canvas)
            }
        }
    }

    fun setIndicatorGravity(indicatorGravity: Int) {
        mIndicatorGravity = indicatorGravity
        invalidate()
    }

    fun setIndicatorMargin(indicatorMarginLeft: Float, indicatorMarginTop: Float,
                           indicatorMarginRight: Float, indicatorMarginBottom: Float) {
        this.indicatorMarginLeft = dp2px(indicatorMarginLeft).toFloat()
        this.indicatorMarginTop = dp2px(indicatorMarginTop).toFloat()
        this.indicatorMarginRight = dp2px(indicatorMarginRight).toFloat()
        this.indicatorMarginBottom = dp2px(indicatorMarginBottom).toFloat()
        invalidate()
    }

    fun setUnderlineGravity(underlineGravity: Int) {
        mUnderlineGravity = underlineGravity
        invalidate()
    }

    //setter and getter
    var currentTab: Int
        get() = mCurrentTab
        set(currentTab) {
            mLastTab = mCurrentTab
            mCurrentTab = currentTab
            updateTabSelection(currentTab)
            if (mTFragmentManager != null) {
                mTFragmentManager!!.setFragments(currentTab)
            }
            if (isIndicatorAnimEnable) {
                calcOffset()
            } else {
                invalidate()
            }
        }

    var indicatorStyle: Int
        get() = mIndicatorStyle
        set(indicatorStyle) {
            mIndicatorStyle = indicatorStyle
            invalidate()
        }

    var tabPadding: Float
        get() = mTabPadding
        set(tabPadding) {
            mTabPadding = dp2px(tabPadding).toFloat()
            updateTabStyles()
        }

    var isTabSpaceEqual: Boolean
        get() = mTabSpaceEqual
        set(tabSpaceEqual) {
            mTabSpaceEqual = tabSpaceEqual
            updateTabStyles()
        }

    var tabWidth: Float
        get() = mTabWidth
        set(tabWidth) {
            mTabWidth = dp2px(tabWidth).toFloat()
            updateTabStyles()
        }

    var indicatorColor: Int
        get() = mIndicatorColor
        set(indicatorColor) {
            mIndicatorColor = indicatorColor
            invalidate()
        }

    var indicatorHeight: Float
        get() = mIndicatorHeight
        set(indicatorHeight) {
            mIndicatorHeight = dp2px(indicatorHeight).toFloat()
            invalidate()
        }

    var indicatorWidth: Float
        get() = mIndicatorWidth
        set(indicatorWidth) {
            mIndicatorWidth = dp2px(indicatorWidth).toFloat()
            invalidate()
        }

    var indicatorCornerRadius: Float
        get() = mIndicatorCornerRadius
        set(indicatorCornerRadius) {
            mIndicatorCornerRadius = dp2px(indicatorCornerRadius).toFloat()
            invalidate()
        }

    var underlineColor: Int
        get() = mUnderlineColor
        set(underlineColor) {
            mUnderlineColor = underlineColor
            invalidate()
        }

    var underlineHeight: Float
        get() = mUnderlineHeight
        set(underlineHeight) {
            mUnderlineHeight = dp2px(underlineHeight).toFloat()
            invalidate()
        }

    var dividerColor: Int
        get() = mDividerColor
        set(dividerColor) {
            mDividerColor = dividerColor
            invalidate()
        }

    var dividerWidth: Float
        get() = mDividerWidth
        set(dividerWidth) {
            mDividerWidth = dp2px(dividerWidth).toFloat()
            invalidate()
        }

    var dividerPadding: Float
        get() = mDividerPadding
        set(dividerPadding) {
            mDividerPadding = dp2px(dividerPadding).toFloat()
            invalidate()
        }

    var textsize: Float
        get() = mTextsize
        set(textsize) {
            mTextsize = sp2px(textsize).toFloat()
            updateTabStyles()
        }

    var textSelectColor: Int
        get() = mTextSelectColor
        set(textSelectColor) {
            mTextSelectColor = textSelectColor
            updateTabStyles()
        }

    var textUnselectColor: Int
        get() = mTextUnselectColor
        set(textUnselectColor) {
            mTextUnselectColor = textUnselectColor
            updateTabStyles()
        }

    var textBold: Int
        get() = mTextBold
        set(textBold) {
            mTextBold = textBold
            updateTabStyles()
        }

    var isTextAllCaps: Boolean
        get() = mTextAllCaps
        set(textAllCaps) {
            mTextAllCaps = textAllCaps
            updateTabStyles()
        }

    var iconGravity: Int
        get() = mIconGravity
        set(iconGravity) {
            mIconGravity = iconGravity
            notifyDataSetChanged()
        }

    var iconWidth: Float
        get() = mIconWidth
        set(iconWidth) {
            mIconWidth = dp2px(iconWidth).toFloat()
            updateTabStyles()
        }

    var iconHeight: Float
        get() = mIconHeight
        set(iconHeight) {
            mIconHeight = dp2px(iconHeight).toFloat()
            updateTabStyles()
        }

    //setter and getter
    var iconMargin: Float
        get() = mIconMargin
        set(iconMargin) {
            mIconMargin = dp2px(iconMargin).toFloat()
            updateTabStyles()
        }

    var isIconVisible: Boolean
        get() = mIconVisible
        set(iconVisible) {
            mIconVisible = iconVisible
            updateTabStyles()
        }

    fun getIconView(tab: Int): ImageView {
        val tabView = mTabsContainer.getChildAt(tab)
        return tabView.findViewById(R.id.iv_tab_icon)
    }

    fun getTitleView(tab: Int): TextView {
        val tabView = mTabsContainer.getChildAt(tab)
        return tabView.findViewById(R.id.tv_tab_title)
    }

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    fun showMsg(position: Int, num: Int) {
        var position = position
        if (position >= tabCount) {
            position = tabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(position)
        val tipView: TLayoutMsg = tabView.findViewById(R.id.rtv_msg_tip)
        if (tipView != null) {
            show(tipView, num)
            if (mInitSetMap[position] != null && mInitSetMap[position]!!) {
                return
            }
            if (!mIconVisible) {
                setMsgMargin(position, 2f, 2f)
            } else {
                setMsgMargin(position, 0f,
                        if (mIconGravity == Gravity.LEFT || mIconGravity == Gravity.RIGHT) 4f else 0.toFloat())
            }
            mInitSetMap.put(position, true)
        }
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    fun showDot(position: Int) {
        var position = position
        if (position >= tabCount) {
            position = tabCount - 1
        }
        showMsg(position, 0)
    }

    fun hideMsg(position: Int) {
        var position = position
        if (position >= tabCount) {
            position = tabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(position)
        val tipView: TLayoutMsg = tabView.findViewById(R.id.rtv_msg_tip)
        if (tipView != null) {
            tipView.visibility = View.GONE
        }
    }

    /**
     * 设置提示红点偏移,注意
     * 1.控件为固定高度:参照点为tab内容的右上角
     * 2.控件高度不固定(WRAP_CONTENT):参照点为tab内容的右上角,此时高度已是红点的最高显示范围,所以这时bottomPadding其实就是topPadding
     */
    fun setMsgMargin(position: Int, leftPadding: Float, bottomPadding: Float) {
        var position = position
        if (position >= tabCount) {
            position = tabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(position)
        val tipView: TLayoutMsg = tabView.findViewById(R.id.rtv_msg_tip)
        if (tipView != null) {
            val tv_tab_title = tabView.findViewById<TextView>(R.id.tv_tab_title)
            mTextPaint.textSize = mTextsize
            val textWidth = mTextPaint.measureText(tv_tab_title.text.toString())
            val textHeight = mTextPaint.descent() - mTextPaint.ascent()
            val lp = tipView.layoutParams as MarginLayoutParams
            var iconH = mIconHeight
            var margin = 0f
            if (mIconVisible) {
                if (iconH <= 0) {
                    iconH = mContext.resources.getDrawable(mTabEntitys[position].tabSelectedIcon).intrinsicHeight.toFloat()
                }
                margin = mIconMargin
            }
            if (mIconGravity == Gravity.TOP || mIconGravity == Gravity.BOTTOM) {
                lp.leftMargin = dp2px(leftPadding)
                lp.topMargin = if (mHeight > 0) (mHeight - textHeight - iconH - margin).toInt() / 2 - dp2px(bottomPadding) else dp2px(bottomPadding)
            } else {
                lp.leftMargin = dp2px(leftPadding)
                lp.topMargin = if (mHeight > 0) (mHeight - Math.max(textHeight, iconH)).toInt() / 2 - dp2px(bottomPadding) else dp2px(bottomPadding)
            }
            tipView.layoutParams = lp
        }
    }

    /**
     * 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取MsgView对象从而各种设置
     */
    fun getMsgView(position: Int): TLayoutMsg {
        var position = position
        if (position >= tabCount) {
            position = tabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(position)
        return tabView.findViewById(R.id.rtv_msg_tip)
    }

    fun setOnTabSelectListener(listener: OnTabSelectListener?) {
        mListener = listener
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("instanceState", super.onSaveInstanceState())
        bundle.putInt("mCurrentTab", mCurrentTab)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        var state: Parcelable? = state
        if (state is Bundle) {
            val bundle = state
            mCurrentTab = bundle.getInt("mCurrentTab")
            state = bundle.getParcelable("instanceState")
            if (mCurrentTab != 0 && mTabsContainer.childCount > 0) {
                updateTabSelection(mCurrentTab)
            }
        }
        super.onRestoreInstanceState(state)
    }

    internal inner class IndicatorPoint {
        var left = 0f
        var right = 0f
    }

    internal inner class PointEvaluator : TypeEvaluator<IndicatorPoint> {
        override fun evaluate(fraction: Float, startValue: IndicatorPoint, endValue: IndicatorPoint): IndicatorPoint {
            val left = startValue.left + fraction * (endValue.left - startValue.left)
            val right = startValue.right + fraction * (endValue.right - startValue.right)
            val point = IndicatorPoint()
            point.left = left
            point.right = right
            return point
        }
    }

    companion object {
        private const val STYLE_NORMAL = 0
        private const val STYLE_TRIANGLE = 1
        private const val STYLE_BLOCK = 2

        /**
         * title
         */
        private const val TEXT_BOLD_NONE = 0
        private const val TEXT_BOLD_WHEN_SELECT = 1
        private const val TEXT_BOLD_BOTH = 2
    }

    init {
        setWillNotDraw(false) //重写onDraw方法,需要调用这个方法来清除flag
        clipChildren = false
        clipToPadding = false
        mContext = context
        mTabsContainer = LinearLayout(context)
        addView(mTabsContainer)
        obtainAttributes(context, attrs)

        //get layout_height
        val height = attrs!!.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height")

        //create ViewPager
        if (height == ViewGroup.LayoutParams.MATCH_PARENT.toString() + "") {
        } else if (height == ViewGroup.LayoutParams.WRAP_CONTENT.toString() + "") {
        } else {
            val systemAttrs = intArrayOf(android.R.attr.layout_height)
            val a = context.obtainStyledAttributes(attrs, systemAttrs)
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT)
            a.recycle()
        }
        mValueAnimator = ValueAnimator.ofObject(PointEvaluator(), mLastP, mCurrentP)
        mValueAnimator.addUpdateListener(this)
    }
}