package com.tamsiree.rxui.view.cardstack

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.database.Observable
import android.os.Build
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxkit.TLog.e
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.cardstack.tools.*
import java.util.*

/**
 * @author Tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class RxCardStackView : ViewGroup, RxScrollDelegate {
    private val mObserver = ViewDataObserver()
    private val mScrollOffset = IntArray(2)
    var totalLength = 0
        private set
    var overlapGaps = 0
    var overlapGapsCollapse = 0
    var numBottomShow = 0
    private var mRxAdapterStack: RxAdapterStack<*>? = null
    private var mSelectPosition = DEFAULT_SELECT_POSITION
    var showHeight = 0
        private set
    private var mViewHolders: MutableList<ViewHolder>? = null
    private var mRxAdapterAnimator: RxAdapterAnimator? = null
    private var mDuration = 0
    private var mScroller: OverScroller? = null
    private var mLastMotionY = 0
    private var mIsBeingDragged = false
    private var mVelocityTracker: VelocityTracker? = null
    private var mTouchSlop = 0
    private var mMinimumVelocity = 0
    private var mMaximumVelocity = 0
    private var mActivePointerId = INVALID_POINTER
    private var mNestedYOffset = 0
    private var mScrollEnable = true
    var rxScrollDelegate: RxScrollDelegate? = null
        private set
    var itemExpendListener: ItemExpendListener? = null

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, 0)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.RxCardStackView, defStyleAttr, defStyleRes)
        overlapGaps = array.getDimensionPixelSize(R.styleable.RxCardStackView_stackOverlapGaps, dp2px(context, 20f))
        overlapGapsCollapse = array.getDimensionPixelSize(R.styleable.RxCardStackView_stackOverlapGapsCollapse, dp2px(context, 20f))
        duration = array.getInt(R.styleable.RxCardStackView_stackDuration, RxAdapterAnimator.ANIMATION_DURATION)
        setAnimationType(array.getInt(R.styleable.RxCardStackView_stackAnimationType, UP_DOWN_STACK))
        numBottomShow = array.getInt(R.styleable.RxCardStackView_stackNumBottomShow, 3)
        array.recycle()
        mViewHolders = ArrayList()
        initScroller()
    }

    private fun initScroller() {
        mScroller = OverScroller(context)
        isFocusable = true
        descendantFocusability = FOCUS_AFTER_DESCENDANTS
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        checkContentHeightByParent()
        measureChild(widthMeasureSpec, heightMeasureSpec)
    }

    private fun checkContentHeightByParent() {
        val parentView = parent as View
        showHeight = parentView.measuredHeight - parentView.paddingTop - parentView.paddingBottom
    }

    private fun measureChild(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxWidth = 0
        totalLength = 0
        totalLength += paddingTop + paddingBottom
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val totalLength = totalLength
            val lp = child.layoutParams as LayoutParams
            if (lp.mHeaderHeight == -1) {
                lp.mHeaderHeight = child.measuredHeight
            }
            val childHeight = lp.mHeaderHeight
            this.totalLength = Math.max(totalLength, totalLength + childHeight + lp.topMargin +
                    lp.bottomMargin)
            this.totalLength -= overlapGaps * 2
            val margin = lp.leftMargin + lp.rightMargin
            val measuredWidth = child.measuredWidth + margin
            maxWidth = Math.max(maxWidth, measuredWidth)
        }
        totalLength += overlapGaps * 2
        var heightSize = totalLength
        heightSize = Math.max(heightSize, showHeight)
        val heightSizeAndState = View.resolveSizeAndState(heightSize, heightMeasureSpec, 0)
        setMeasuredDimension(View.resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                heightSizeAndState)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutChild()
    }

    private fun layoutChild() {
        var childTop = paddingTop
        val childLeft = paddingLeft
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            val lp = child.layoutParams as LayoutParams
            childTop += lp.topMargin
            if (i != 0) {
                childTop -= overlapGaps * 2
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
            } else {
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
            }
            childTop += lp.mHeaderHeight
        }
    }

    fun updateSelectPosition(selectPosition: Int) {
        post { doCardClickAnimation(mViewHolders!![selectPosition], selectPosition) }
    }

    fun clearSelectPosition() {
        updateSelectPosition(mSelectPosition)
    }

    fun clearScrollYAndTranslation() {
        if (mSelectPosition != DEFAULT_SELECT_POSITION) {
            clearSelectPosition()
        }
        if (rxScrollDelegate != null) {
            rxScrollDelegate!!.viewScrollY = 0
        }
        requestLayout()
    }

    fun setAdapter(rxAdapterStack: RxAdapterStack<*>?) {
        mRxAdapterStack = rxAdapterStack
        mRxAdapterStack!!.registerObserver(mObserver)
        refreshView()
    }

    fun setAnimationType(type: Int) {
        val rxAdapterAnimator: RxAdapterAnimator = when (type) {
            ALL_DOWN -> RxAdapterAllMoveDownAnimator(this)
            UP_DOWN -> RxAdapterUpDownAnimator(this)
            else -> RxAdapterUpDownStackAnimator(this)
        }
        setRxAdapterAnimator(rxAdapterAnimator)
    }

    fun setRxAdapterAnimator(rxAdapterAnimator: RxAdapterAnimator?) {
        clearScrollYAndTranslation()
        mRxAdapterAnimator = rxAdapterAnimator
        if (mRxAdapterAnimator is RxAdapterUpDownStackAnimator) {
            rxScrollDelegate = RxStackScrollDelegateImpl(this)
        } else {
            rxScrollDelegate = this
        }
    }

    private fun refreshView() {
        removeAllViews()
        mViewHolders!!.clear()
        for (i in 0 until mRxAdapterStack!!.itemCount) {
            val holder = getViewHolder(i)
            holder!!.position = i
            holder.onItemExpand(i == mSelectPosition)
            addView(holder.itemView)
            setClickAnimator(holder, i)
            mRxAdapterStack!!.bindViewHolder(holder, i)
        }
        requestLayout()
    }

    fun getViewHolder(i: Int): ViewHolder? {
        if (i == DEFAULT_SELECT_POSITION) {
            return null
        }
        val viewHolder: ViewHolder
        if (mViewHolders!!.size <= i || mViewHolders!![i].mItemViewType != mRxAdapterStack!!.getItemViewType(i)) {
            viewHolder = mRxAdapterStack!!.createView(this, mRxAdapterStack!!.getItemViewType(i))
            mViewHolders!!.add(viewHolder)
        } else {
            viewHolder = mViewHolders!![i]
        }
        return viewHolder
    }

    private fun setClickAnimator(holder: ViewHolder?, position: Int) {
        setOnClickListener { v: View? ->
            if (mSelectPosition == DEFAULT_SELECT_POSITION) {
                return@setOnClickListener
            }
            performItemClick(mViewHolders!![mSelectPosition])
        }
        holder!!.itemView.setOnClickListener { v: View? -> performItemClick(holder) }
    }

    operator fun next() {
        if (mSelectPosition == DEFAULT_SELECT_POSITION || mSelectPosition == mViewHolders!!.size - 1) {
            return
        }
        performItemClick(mViewHolders!![mSelectPosition + 1])
    }

    fun pre() {
        if (mSelectPosition == DEFAULT_SELECT_POSITION || mSelectPosition == 0) {
            return
        }
        performItemClick(mViewHolders!![mSelectPosition - 1])
    }

    val isExpending: Boolean
        get() = mSelectPosition != DEFAULT_SELECT_POSITION

    fun performItemClick(viewHolder: ViewHolder?) {
        doCardClickAnimation(viewHolder, viewHolder!!.position)
    }

    private fun doCardClickAnimation(viewHolder: ViewHolder?, position: Int) {
        checkContentHeightByParent()
        mRxAdapterAnimator!!.itemClick(viewHolder!!, position)
    }

    private fun initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        } else {
            mVelocityTracker!!.clear()
        }
    }

    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        if (action == MotionEvent.ACTION_MOVE && mIsBeingDragged) {
            return true
        }
        if (viewScrollY == 0 && !canScrollVertically(1)) {
            return false
        }
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_MOVE -> {
                val activePointerId = mActivePointerId
                if (activePointerId != INVALID_POINTER) {
                    val pointerIndex = ev.findPointerIndex(activePointerId)
                    if (pointerIndex == -1) {
                        e(TAG, "Invalid pointerId=" + activePointerId
                                + " in onInterceptTouchEvent")
                    } else {
                        val y = ev.getY(pointerIndex).toInt()
                        val yDiff = Math.abs(y - mLastMotionY)
                        if (yDiff > mTouchSlop) {
                            mIsBeingDragged = true
                            mLastMotionY = y
                            initVelocityTrackerIfNotExists()
                            mVelocityTracker!!.addMovement(ev)
                            mNestedYOffset = 0
                            val parent = parent
                            parent?.requestDisallowInterceptTouchEvent(true)
                        }
                    }
                }
            }
            MotionEvent.ACTION_DOWN -> {
                mLastMotionY = ev.y.toInt()
                mActivePointerId = ev.getPointerId(0)
                initOrResetVelocityTracker()
                mVelocityTracker!!.addMovement(ev)
                mIsBeingDragged = !mScroller!!.isFinished
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
                recycleVelocityTracker()
                if (mScroller!!.springBack(viewScrollX, viewScrollY, 0, 0, 0, scrollRange)) {
                    postInvalidate()
                }
            }
            MotionEvent.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)
            else -> {
            }
        }
        if (!mScrollEnable) {
            mIsBeingDragged = false
        }
        return mIsBeingDragged
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (!mIsBeingDragged) {
            super.onTouchEvent(ev)
        }
        if (!mScrollEnable) {
            return true
        }
        initVelocityTrackerIfNotExists()
        val vtev = MotionEvent.obtain(ev)
        val actionMasked = ev.actionMasked
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0
        }
        vtev.offsetLocation(0f, mNestedYOffset.toFloat())
        when (actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (childCount == 0) {
                    return false
                }
                if (!mScroller!!.isFinished.also { mIsBeingDragged = it }) {
                    val parent = parent
                    parent?.requestDisallowInterceptTouchEvent(true)
                }
                if (!mScroller!!.isFinished) {
                    mScroller!!.abortAnimation()
                }
                mLastMotionY = ev.y.toInt()
                mActivePointerId = ev.getPointerId(0)
            }
            MotionEvent.ACTION_MOVE -> {
                val activePointerIndex = ev.findPointerIndex(mActivePointerId)
                if (activePointerIndex == -1) {
                    e(TAG, "Invalid pointerId=$mActivePointerId in onTouchEvent")
                } else {
                    val y = ev.getY(activePointerIndex).toInt()
                    var deltaY = mLastMotionY - y
                    if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                        val parent = parent
                        parent?.requestDisallowInterceptTouchEvent(true)
                        mIsBeingDragged = true
                        if (deltaY > 0) {
                            deltaY -= mTouchSlop
                        } else {
                            deltaY += mTouchSlop
                        }
                    }
                    if (mIsBeingDragged) {
                        mLastMotionY = y - mScrollOffset[1]
                        val range = scrollRange
                        if (rxScrollDelegate is RxStackScrollDelegateImpl) {
                            (rxScrollDelegate as RxStackScrollDelegateImpl).scrollViewTo(0, deltaY + (rxScrollDelegate as RxStackScrollDelegateImpl).viewScrollY)
                        } else {
                            if (overScrollBy(0, deltaY, 0, viewScrollY,
                                            0, range, 0, 0, true)) {
                                mVelocityTracker!!.clear()
                            }
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (mIsBeingDragged) {
                    val velocityTracker = mVelocityTracker
                    velocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                    val initialVelocity = velocityTracker.getYVelocity(mActivePointerId).toInt()
                    if (childCount > 0) {
                        if (Math.abs(initialVelocity) > mMinimumVelocity) {
                            fling(-initialVelocity)
                        } else {
                            if (mScroller!!.springBack(viewScrollX, rxScrollDelegate!!.viewScrollY, 0, 0, 0,
                                            scrollRange)) {
                                postInvalidate()
                            }
                        }
                        mActivePointerId = INVALID_POINTER
                    }
                }
                endDrag()
            }
            MotionEvent.ACTION_CANCEL -> {
                if (mIsBeingDragged && childCount > 0) {
                    if (mScroller!!.springBack(viewScrollX, rxScrollDelegate!!.viewScrollY, 0, 0, 0, scrollRange)) {
                        postInvalidate()
                    }
                    mActivePointerId = INVALID_POINTER
                }
                endDrag()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = ev.actionIndex
                mLastMotionY = ev.getY(index).toInt()
                mActivePointerId = ev.getPointerId(index)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                onSecondaryPointerUp(ev)
                mLastMotionY = ev.getY(ev.findPointerIndex(mActivePointerId)).toInt()
            }
            else -> {
            }
        }
        if (mVelocityTracker != null) {
            mVelocityTracker!!.addMovement(vtev)
        }
        vtev.recycle()
        return true
    }

    private fun onSecondaryPointerUp(ev: MotionEvent) {
        val pointerIndex = ev.action and MotionEvent.ACTION_POINTER_INDEX_MASK shr
                MotionEvent.ACTION_POINTER_INDEX_SHIFT
        val pointerId = ev.getPointerId(pointerIndex)
        if (pointerId == mActivePointerId) {
            val newPointerIndex = if (pointerIndex == 0) 1 else 0
            mLastMotionY = ev.getY(newPointerIndex).toInt()
            mActivePointerId = ev.getPointerId(newPointerIndex)
            if (mVelocityTracker != null) {
                mVelocityTracker!!.clear()
            }
        }
    }

    private val scrollRange: Int
        private get() {
            var scrollRange = 0
            if (childCount > 0) {
                scrollRange = Math.max(0,
                        totalLength - showHeight)
            }
            return scrollRange
        }

    override fun computeVerticalScrollRange(): Int {
        val count = childCount
        val contentHeight = showHeight
        if (count == 0) {
            return contentHeight
        }
        var scrollRange = totalLength
        val scrollY = rxScrollDelegate!!.viewScrollY
        val overscrollBottom = Math.max(0, scrollRange - contentHeight)
        if (scrollY < 0) {
            scrollRange -= scrollY
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom
        }
        return scrollRange
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int,
                                clampedX: Boolean, clampedY: Boolean) {
        if (!mScroller!!.isFinished) {
            val oldX = rxScrollDelegate!!.viewScrollX
            val oldY = rxScrollDelegate!!.viewScrollY
            rxScrollDelegate!!.viewScrollX = scrollX
            rxScrollDelegate!!.viewScrollY = scrollY
            onScrollChanged(rxScrollDelegate!!.viewScrollX, rxScrollDelegate!!.viewScrollY, oldX, oldY)
            if (clampedY) {
                mScroller!!.springBack(rxScrollDelegate!!.viewScrollX, rxScrollDelegate!!.viewScrollY, 0, 0, 0, scrollRange)
            }
        } else {
            super.scrollTo(scrollX, scrollY)
        }
    }

    override fun computeVerticalScrollOffset(): Int {
        return Math.max(0, super.computeVerticalScrollOffset())
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            rxScrollDelegate!!.scrollViewTo(0, mScroller!!.currY)
            postInvalidate()
        }
    }

    fun fling(velocityY: Int) {
        if (childCount > 0) {
            val height = showHeight
            val bottom = totalLength
            mScroller!!.fling(rxScrollDelegate!!.viewScrollX, rxScrollDelegate!!.viewScrollY, 0, velocityY, 0, 0, 0,
                    Math.max(0, bottom - height), 0, 0)
            postInvalidate()
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        var x = x
        var y = y
        if (childCount > 0) {
            x = clamp(x, width - paddingRight - paddingLeft, width)
            y = clamp(y, showHeight, totalLength)
            if (x != rxScrollDelegate!!.viewScrollX || y != rxScrollDelegate!!.viewScrollY) {
                super.scrollTo(x, y)
            }
        }
    }

    override var viewScrollX: Int
        get() = scrollX
        set(x) {
            scrollX = x
        }

    override fun scrollViewTo(x: Int, y: Int) {
        scrollTo(x, y)
    }

    override var viewScrollY: Int
        get() = scrollY
        set(y) {
            scrollY = y
        }

    private fun endDrag() {
        mIsBeingDragged = false
        recycleVelocityTracker()
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    var selectPosition: Int
        get() = mSelectPosition
        set(selectPosition) {
            mSelectPosition = selectPosition
            itemExpendListener!!.onItemExpend(mSelectPosition != DEFAULT_SELECT_POSITION)
        }

    fun setScrollEnable(scrollEnable: Boolean) {
        mScrollEnable = scrollEnable
    }

    var duration: Int
        get() = if (mRxAdapterAnimator != null) {
            mDuration
        } else 0
        set(duration) {
            mDuration = duration
        }

    interface ItemExpendListener {
        fun onItemExpend(expend: Boolean)
    }

    class LayoutParams : MarginLayoutParams {
        var mHeaderHeight = 0

        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
            @SuppressLint("CustomViewStyleable") val array = c.obtainStyledAttributes(attrs, R.styleable.RxCardStackView)
            mHeaderHeight = array.getDimensionPixelSize(R.styleable.RxCardStackView_stackHeaderHeight, -1)
            array.recycle()
        }

        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
    }

    abstract class Adapter<VH : ViewHolder> {
        private val mObservable = AdapterDataObservable()
        fun createView(parent: ViewGroup, viewType: Int): VH {
            val holder = onCreateView(parent, viewType)
            holder.mItemViewType = viewType
            return holder
        }

        protected abstract fun onCreateView(parent: ViewGroup, viewType: Int): VH
        fun bindViewHolder(holder: VH, position: Int) {
            onBindViewHolder(holder, position)
        }

        protected abstract fun onBindViewHolder(holder: VH, position: Int)

        abstract val itemCount: Int

        open fun getItemViewType(position: Int): Int {
            return 0
        }

        fun notifyDataSetChanged() {
            mObservable.notifyChanged()
        }

        fun registerObserver(observer: AdapterDataObserver) {
            mObservable.registerObserver(observer)
        }
    }

    abstract class ViewHolder(var itemView: View) {
        var mItemViewType = INVALID_TYPE
        var position = 0
        val context: Context
            get() = itemView.context

        abstract fun onItemExpand(b: Boolean)
        open fun onAnimationStateChange(state: Int, willBeSelect: Boolean) {}

    }

    class AdapterDataObservable : Observable<AdapterDataObserver>() {
        fun hasObservers(): Boolean {
            return !mObservers.isEmpty()
        }

        fun notifyChanged() {
            for (i in mObservers.indices.reversed()) {
                mObservers[i]!!.onChanged()
            }
        }
    }

    abstract class AdapterDataObserver {
        open fun onChanged() {}
    }

    private inner class ViewDataObserver : AdapterDataObserver() {
        override fun onChanged() {
            refreshView()
        }
    }

    companion object {
        const val INVALID_TYPE = -1
        const val ANIMATION_STATE_START = 0
        const val ANIMATION_STATE_END = 1
        const val ANIMATION_STATE_CANCEL = 2
        const val ALL_DOWN = 0
        const val UP_DOWN = 1
        const val UP_DOWN_STACK = 2
        const val DEFAULT_SELECT_POSITION = -1
        private const val INVALID_POINTER = -1
        private const val TAG = "RxCardStackView"
        private fun clamp(n: Int, my: Int, child: Int): Int {
            if (my >= child || n < 0) {
                return 0
            }
            return if (my + n > child) {
                child - my
            } else n
        }
    }
}