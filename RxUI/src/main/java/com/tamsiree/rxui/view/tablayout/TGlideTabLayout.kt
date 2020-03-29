package com.tamsiree.rxui.view.tablayout

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
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxkit.RxImageTool.sp2px
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener
import com.tamsiree.rxui.view.tablayout.tool.TLayoutMsgTool.show

/**
 * 滑动TabLayout,对于ViewPager的依赖性强
 */
class TGlideTabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : HorizontalScrollView(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener {
    private val mContext: Context
    private var mViewPager: ViewPager? = null
    private var mTitles: ArrayList<String>? = null
    private val mTabsContainer: LinearLayout
    private var mCurrentTab = 0
    private var mCurrentPositionOffset = 0f
    var tabCount = 0
        private set

    /**
     * 用于绘制显示器
     */
    private val mIndicatorRect = Rect()

    /**
     * 用于实现滚动居中
     */
    private val mTabRect = Rect()
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
    private var mIndicatorGravity = 0
    private var mIndicatorWidthEqualTitle = false

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
    private var mLastScrollX = 0
    private var mHeight = 0
    private var mSnapOnTabClick = false
    private var margin = 0f

    // show MsgTipView
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    @SuppressLint("UseSparseArrays")
    private val mInitSetMap = SparseArray<Boolean?>()
    private var mListener: OnTabSelectListener? = null
    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TGlideTabLayout)
        mIndicatorStyle = ta.getInt(R.styleable.TGlideTabLayout_indicator_style, STYLE_NORMAL)
        mIndicatorColor = ta.getColor(R.styleable.TGlideTabLayout_indicator_color, Color.parseColor(if (mIndicatorStyle == STYLE_BLOCK) "#4B6A87" else "#ffffff"))
        mIndicatorHeight = ta.getDimension(R.styleable.TGlideTabLayout_indicator_height,
                dp2px(if (mIndicatorStyle == STYLE_TRIANGLE) 4f else (if (mIndicatorStyle == STYLE_BLOCK) -1 else 2).toFloat()).toFloat())
        mIndicatorWidth = ta.getDimension(R.styleable.TGlideTabLayout_indicator_width, dp2px(if (mIndicatorStyle == STYLE_TRIANGLE) 10f else -1.toFloat()).toFloat())
        mIndicatorCornerRadius = ta.getDimension(R.styleable.TGlideTabLayout_indicator_corner_radius, dp2px(if (mIndicatorStyle == STYLE_BLOCK) -1f else 0.toFloat()).toFloat())
        indicatorMarginLeft = ta.getDimension(R.styleable.TGlideTabLayout_indicator_margin_left, dp2px(0f).toFloat())
        indicatorMarginTop = ta.getDimension(R.styleable.TGlideTabLayout_indicator_margin_top, dp2px(if (mIndicatorStyle == STYLE_BLOCK) 7f else 0.toFloat()).toFloat())
        indicatorMarginRight = ta.getDimension(R.styleable.TGlideTabLayout_indicator_margin_right, dp2px(0f).toFloat())
        indicatorMarginBottom = ta.getDimension(R.styleable.TGlideTabLayout_indicator_margin_bottom, dp2px(if (mIndicatorStyle == STYLE_BLOCK) 7f else 0.toFloat()).toFloat())
        mIndicatorGravity = ta.getInt(R.styleable.TGlideTabLayout_indicator_gravity, Gravity.BOTTOM)
        mIndicatorWidthEqualTitle = ta.getBoolean(R.styleable.TGlideTabLayout_indicator_width_equal_title, false)
        mUnderlineColor = ta.getColor(R.styleable.TGlideTabLayout_underline_color, Color.parseColor("#ffffff"))
        mUnderlineHeight = ta.getDimension(R.styleable.TGlideTabLayout_underline_height, dp2px(0f).toFloat())
        mUnderlineGravity = ta.getInt(R.styleable.TGlideTabLayout_underline_gravity, Gravity.BOTTOM)
        mDividerColor = ta.getColor(R.styleable.TGlideTabLayout_divider_color, Color.parseColor("#ffffff"))
        mDividerWidth = ta.getDimension(R.styleable.TGlideTabLayout_divider_width, dp2px(0f).toFloat())
        mDividerPadding = ta.getDimension(R.styleable.TGlideTabLayout_divider_padding, dp2px(12f).toFloat())
        mTextsize = ta.getDimension(R.styleable.TGlideTabLayout_titlesize, sp2px(14f).toFloat())
        mTextSelectColor = ta.getColor(R.styleable.TGlideTabLayout_textSelectColor, Color.parseColor("#ffffff"))
        mTextUnselectColor = ta.getColor(R.styleable.TGlideTabLayout_textUnSelectColor, Color.parseColor("#AAffffff"))
        mTextBold = ta.getInt(R.styleable.TGlideTabLayout_textBold, TEXT_BOLD_NONE)
        mTextAllCaps = ta.getBoolean(R.styleable.TGlideTabLayout_textAllCaps, false)
        mTabSpaceEqual = ta.getBoolean(R.styleable.TGlideTabLayout_tab_space_equal, false)
        mTabWidth = ta.getDimension(R.styleable.TGlideTabLayout_tab_width, dp2px(-1f).toFloat())
        mTabPadding = ta.getDimension(R.styleable.TGlideTabLayout_tab_padding, if (mTabSpaceEqual || mTabWidth > 0) dp2px(0f).toFloat() else dp2px(20f).toFloat())
        ta.recycle()
    }

    /**
     * 关联ViewPager
     */
    fun setViewPager(vp: ViewPager?) {
        check(!(vp == null || vp.adapter == null)) { "ViewPager or ViewPager adapter can not be NULL !" }
        mViewPager = vp
        mViewPager!!.removeOnPageChangeListener(this)
        mViewPager!!.addOnPageChangeListener(this)
        notifyDataSetChanged()
    }

    /**
     * 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况
     */
    fun setViewPager(vp: ViewPager?, titles: Array<String>) {
        check(!(vp == null || vp.adapter == null)) { "ViewPager or ViewPager adapter can not be NULL !" }
        check(!(titles == null || titles.size == 0)) { "Titles can not be EMPTY !" }
        check(titles.size == vp.adapter!!.count) { "Titles length must be the same as the page count !" }
        mViewPager = vp
        mTitles = ArrayList()

        mTitles!!.addAll(titles)

        mViewPager!!.removeOnPageChangeListener(this)
        mViewPager!!.addOnPageChangeListener(this)
        notifyDataSetChanged()
    }

    /**
     * 关联ViewPager,用于连适配器都不想自己实例化的情况
     */
    fun setViewPager(vp: ViewPager?, titles: Array<String>, fa: FragmentActivity, fragments: ArrayList<Fragment>) {
        checkNotNull(vp) { "ViewPager can not be NULL !" }
        check(!(titles == null || titles.size == 0)) { "Titles can not be EMPTY !" }
        mViewPager = vp
        mViewPager!!.adapter = InnerPagerAdapter(fa.supportFragmentManager, fragments, titles)
        mViewPager!!.removeOnPageChangeListener(this)
        mViewPager!!.addOnPageChangeListener(this)
        notifyDataSetChanged()
    }

    /**
     * 更新数据
     */
    fun notifyDataSetChanged() {
        mTabsContainer.removeAllViews()
        tabCount = if (mTitles == null) mViewPager!!.adapter!!.count else mTitles!!.size
        var tabView: View
        for (i in 0 until tabCount) {
            tabView = View.inflate(mContext, R.layout.layout_tab, null)
            val pageTitle = if (mTitles == null) mViewPager!!.adapter!!.getPageTitle(i) else mTitles!![i]
            addTab(i, pageTitle.toString(), tabView)
        }
        updateTabStyles()
    }

    fun addNewTab(title: String) {
        val tabView = View.inflate(mContext, R.layout.layout_tab, null)
        if (mTitles != null) {
            mTitles!!.add(title)
        }
        val pageTitle = if (mTitles == null) mViewPager!!.adapter!!.getPageTitle(tabCount) else mTitles!![tabCount]
        addTab(tabCount, pageTitle.toString(), tabView)
        tabCount = if (mTitles == null) mViewPager!!.adapter!!.count else mTitles!!.size
        updateTabStyles()
    }

    /**
     * 创建并添加tab
     */
    private fun addTab(position: Int, title: String?, tabView: View) {
        val tv_tab_title = tabView.findViewById<TextView>(R.id.tv_tab_title)
        if (tv_tab_title != null) {
            if (title != null) tv_tab_title.text = title
        }
        tabView.setOnClickListener { v ->
            val position = mTabsContainer.indexOfChild(v)
            if (position != -1) {
                if (mViewPager!!.currentItem != position) {
                    if (mSnapOnTabClick) {
                        mViewPager!!.setCurrentItem(position, false)
                    } else {
                        mViewPager!!.currentItem = position
                    }
                    if (mListener != null) {
                        mListener!!.onTabSelect(position)
                    }
                } else {
                    if (mListener != null) {
                        mListener!!.onTabReselect(position)
                    }
                }
            }
        }
        /** 每一个Tab的布局参数  */
        var lp_tab = if (mTabSpaceEqual) LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) else LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        if (mTabWidth > 0) {
            lp_tab = LinearLayout.LayoutParams(mTabWidth.toInt(), LayoutParams.MATCH_PARENT)
        }
        mTabsContainer.addView(tabView, position, lp_tab)
    }

    private fun updateTabStyles() {
        for (i in 0 until tabCount) {
            val v = mTabsContainer.getChildAt(i)
            //            v.setPadding((int) mTabPadding, v.getPaddingTop(), (int) mTabPadding, v.getPaddingBottom());
            val tv_tab_title = v.findViewById<TextView>(R.id.tv_tab_title)
            if (tv_tab_title != null) {
                tv_tab_title.setTextColor(if (i == mCurrentTab) mTextSelectColor else mTextUnselectColor)
                tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextsize)
                tv_tab_title.setPadding(mTabPadding.toInt(), 0, mTabPadding.toInt(), 0)
                if (mTextAllCaps) {
                    tv_tab_title.text = tv_tab_title.text.toString().toUpperCase()
                }
                if (mTextBold == TEXT_BOLD_BOTH) {
                    tv_tab_title.paint.isFakeBoldText = true
                } else if (mTextBold == TEXT_BOLD_NONE) {
                    tv_tab_title.paint.isFakeBoldText = false
                }
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        /**
         * position:当前View的位置
         * mCurrentPositionOffset:当前View的偏移量比例.[0,1)
         */
        mCurrentTab = position
        mCurrentPositionOffset = positionOffset
        scrollToCurrentTab()
        invalidate()
    }

    override fun onPageSelected(position: Int) {
        updateTabSelection(position)
    }

    override fun onPageScrollStateChanged(state: Int) {}

    /**
     * HorizontalScrollView滚到当前tab,并且居中显示
     */
    private fun scrollToCurrentTab() {
        if (tabCount <= 0) {
            return
        }
        val offset = (mCurrentPositionOffset * mTabsContainer.getChildAt(mCurrentTab).width).toInt()

        /**当前Tab的left+当前Tab的Width乘以positionOffset */
        var newScrollX = mTabsContainer.getChildAt(mCurrentTab).left + offset
        if (mCurrentTab > 0 || offset > 0) {
            /**HorizontalScrollView移动到当前tab,并居中 */
            newScrollX -= width / 2 - paddingLeft
            calcIndicatorRect()
            newScrollX += (mTabRect.right - mTabRect.left) / 2
        }
        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX
            /** scrollTo（int x,int y）:x,y代表的不是坐标点,而是偏移量
             * x:表示离起始位置的x水平方向的偏移量
             * y:表示离起始位置的y垂直方向的偏移量
             */
            scrollTo(newScrollX, 0)
        }
    }

    private fun updateTabSelection(position: Int) {
        for (i in 0 until tabCount) {
            val tabView = mTabsContainer.getChildAt(i)
            val isSelect = i == position
            val tab_title = tabView.findViewById<TextView>(R.id.tv_tab_title)
            if (tab_title != null) {
                tab_title.setTextColor(if (isSelect) mTextSelectColor else mTextUnselectColor)
                if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                    tab_title.paint.isFakeBoldText = isSelect
                }
            }
        }
    }

    private fun calcIndicatorRect() {
        val currentTabView = mTabsContainer.getChildAt(mCurrentTab)
        var left = currentTabView.left.toFloat()
        var right = currentTabView.right.toFloat()

        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            val tab_title = currentTabView.findViewById<TextView>(R.id.tv_tab_title)
            mTextPaint.textSize = mTextsize
            val textWidth = mTextPaint.measureText(tab_title.text.toString())
            margin = (right - left - textWidth) / 2
        }
        if (mCurrentTab < tabCount - 1) {
            val nextTabView = mTabsContainer.getChildAt(mCurrentTab + 1)
            val nextTabLeft = nextTabView.left.toFloat()
            val nextTabRight = nextTabView.right.toFloat()
            left = left + mCurrentPositionOffset * (nextTabLeft - left)
            right = right + mCurrentPositionOffset * (nextTabRight - right)

            //for mIndicatorWidthEqualTitle
            if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
                val next_tab_title = nextTabView.findViewById<TextView>(R.id.tv_tab_title)
                mTextPaint.textSize = mTextsize
                val nextTextWidth = mTextPaint.measureText(next_tab_title.text.toString())
                val nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2
                margin = margin + mCurrentPositionOffset * (nextMargin - margin)
            }
        }
        mIndicatorRect.left = left.toInt()
        mIndicatorRect.right = right.toInt()
        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            mIndicatorRect.left = (left + margin - 1).toInt()
            mIndicatorRect.right = (right - margin - 1).toInt()
        }
        mTabRect.left = left.toInt()
        mTabRect.right = right.toInt()
        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip
        } else { //indicatorWidth大于0时,圆角矩形以及三角形
            var indicatorLeft = currentTabView.left + (currentTabView.width - mIndicatorWidth) / 2
            if (mCurrentTab < tabCount - 1) {
                val nextTab = mTabsContainer.getChildAt(mCurrentTab + 1)
                indicatorLeft = indicatorLeft + mCurrentPositionOffset * (currentTabView.width / 2 + nextTab.width / 2)
            }
            mIndicatorRect.left = indicatorLeft.toInt()
            mIndicatorRect.right = (mIndicatorRect.left + mIndicatorWidth).toInt()
        }
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
        calcIndicatorRect()
        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.color = mIndicatorColor
                mTrianglePath.reset()
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left.toFloat(), height.toFloat())
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2 + (mIndicatorRect.right / 2).toFloat(), height - mIndicatorHeight)
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

    fun setCurrentTab(currentTab: Int, smoothScroll: Boolean) {
        mCurrentTab = currentTab
        mViewPager!!.setCurrentItem(currentTab, smoothScroll)
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

    fun setIndicatorWidthEqualTitle(indicatorWidthEqualTitle: Boolean) {
        mIndicatorWidthEqualTitle = indicatorWidthEqualTitle
        invalidate()
    }

    fun setUnderlineGravity(underlineGravity: Int) {
        mUnderlineGravity = underlineGravity
        invalidate()
    }

    fun setSnapOnTabClick(snapOnTabClick: Boolean) {
        mSnapOnTabClick = snapOnTabClick
    }

    //setter and getter
    var currentTab: Int
        get() = mCurrentTab
        set(currentTab) {
            mCurrentTab = currentTab
            mViewPager!!.currentItem = currentTab
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

    //setter and getter
    var isTextAllCaps: Boolean
        get() = mTextAllCaps
        set(textAllCaps) {
            mTextAllCaps = textAllCaps
            updateTabStyles()
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
            setMsgMargin(position, 4f, 2f)
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

    /**
     * 隐藏未读消息
     */
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
     * 设置未读消息偏移,原点为文字的右上角.当控件高度固定,消息提示位置易控制,显示效果佳
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
            lp.leftMargin = if (mTabWidth >= 0) (mTabWidth / 2 + textWidth / 2 + dp2px(leftPadding)).toInt() else (mTabPadding + textWidth + dp2px(leftPadding)).toInt()
            lp.topMargin = if (mHeight > 0) (mHeight - textHeight).toInt() / 2 - dp2px(bottomPadding) else 0
            tipView.layoutParams = lp
        }
    }

    /**
     * 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取TMsgView对象从而各种设置
     */
    fun getTMsgView(position: Int): TLayoutMsg {
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
                scrollToCurrentTab()
            }
        }
        super.onRestoreInstanceState(state)
    }

    internal inner class InnerPagerAdapter(fm: FragmentManager?, fragments: ArrayList<Fragment>, titles: Array<String>) : FragmentPagerAdapter(fm!!) {
        private val fragments: ArrayList<Fragment> = fragments
        private val titles: Array<String> = titles
        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
            // super.destroyItem(container, position, object);
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
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
        isFillViewport = true //设置滚动视图是否可以伸缩其内容以填充视口
        setWillNotDraw(false) //重写onDraw方法,需要调用这个方法来清除flag
        clipChildren = false
        clipToPadding = false
        mContext = context
        mTabsContainer = LinearLayout(context)
        addView(mTabsContainer)
        obtainAttributes(context, attrs)

        //get layout_height
        val height = attrs!!.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height")
        if (height == ViewGroup.LayoutParams.MATCH_PARENT.toString() + "") {
        } else if (height == ViewGroup.LayoutParams.WRAP_CONTENT.toString() + "") {
        } else {
            val systemAttrs = intArrayOf(android.R.attr.layout_height)
            val a = context.obtainStyledAttributes(attrs, systemAttrs)
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT)
            a.recycle()
        }
    }
}