package com.tamsiree.rxui.view.dialog.wheel

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.widget.LinearLayout
import com.tamsiree.rxui.R
import java.util.*
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.max

/**
 * @author Tamsiree
 *
 * Numeric wheel view.
 */
class WheelView : View {
    /** Top and bottom shadows colors  */
    private var SHADOWS_COLORS = intArrayOf(-0xeeeeef,
            0x00AAAAAA, 0x00AAAAAA)

    // Wheel Values
    var currentItem = 0

    /**
     * Gets count of visible items
     *
     * @return the count of visible items
     */
    /**
     * Sets the desired count of visible items.
     * Actual amount of visible items depends on wheel layout parameters.
     * To apply changes and rebuild view call measure().
     *
     * @param count the desired count for visible items
     */
    // Count of visible items
    var visibleItems = DEF_VISIBLE_ITEMS

    // Item height
    private var itemHeight = 0

    // Center Line
    private var centerDrawable: Drawable? = null

    // Wheel drawables
    private var wheelBackground = R.drawable.wheel_bg
    private var wheelForeground = R.drawable.wheel_val_holo

    // Shadows drawables
    private var topShadow: GradientDrawable? = null
    private var bottomShadow: GradientDrawable? = null

    // Draw Shadows
    private var drawShadows = true

    // Scrolling
    private var scroller: WheelScroller? = null
    private var isScrollingPerformed = false
    private var scrollingOffset = 0

    // Cyclic
    var isCyclic = false

    // Items layout
    private var itemsLayout: LinearLayout? = null

    // The number of first item in layout
    private var firstItem = 0

    // View adapter
    var viewAdapter: WheelViewAdapter? = null

    // Recycle
    private val recycle = WheelRecycle(this)

    // Listeners
    private val changingListeners: MutableList<OnWheelChangedListener> = LinkedList()
    private val scrollingListeners: MutableList<OnWheelScrollListener> = LinkedList()
    private val clickingListeners: MutableList<OnWheelClickedListener> = LinkedList()

    /**
     * Constructor
     */
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initData(context)
    }

    /**
     * Constructor
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initData(context)
    }

    /**
     * Constructor
     */
    constructor(context: Context) : super(context) {
        initData(context)
    }

    /**
     * Initializes class data
     * @param context the context
     */
    private fun initData(context: Context) {
        scroller = WheelScroller(context, scrollingListener)
    }

    // Scrolling listener
    var scrollingListener: WheelScroller.ScrollingListener = object : WheelScroller.ScrollingListener {
        override fun onStarted() {
            isScrollingPerformed = true
            notifyScrollingListenersAboutStart()
        }

        override fun onScroll(distance: Int) {
            doScroll(distance)
            val height = height
            if (scrollingOffset > height) {
                scrollingOffset = height
                scroller!!.stopScrolling()
            } else if (scrollingOffset < -height) {
                scrollingOffset = -height
                scroller!!.stopScrolling()
            }
        }

        override fun onFinished() {
            if (isScrollingPerformed) {
                notifyScrollingListenersAboutEnd()
                isScrollingPerformed = false
            }
            scrollingOffset = 0
            invalidate()
        }

        override fun onJustify() {
            if (Math.abs(scrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
                scroller!!.scroll(scrollingOffset, 0)
            }
        }
    }

    /**
     * Set the the specified scrolling interpolator
     * @param interpolator the interpolator
     */
    fun setInterpolator(interpolator: Interpolator?) {
        scroller!!.setInterpolator(interpolator)
    }

    // Adapter listener
    private val dataObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            invalidateWheel(false)
        }

        override fun onInvalidated() {
            invalidateWheel(true)
        }
    }

    /**
     * Sets view adapter. Usually new adapters contain different views, so
     * it needs to rebuild view by calling measure().
     *
     * @param wheelViewAdapter the view adapter
     */
    fun setViewAdapter0(wheelViewAdapter: WheelViewAdapter) {
        if (viewAdapter != null) {
            viewAdapter?.unregisterDataSetObserver(dataObserver)
        }
        viewAdapter = wheelViewAdapter
        if (viewAdapter != null) {
            viewAdapter?.registerDataSetObserver(dataObserver)
        }
        invalidateWheel(true)
    }

    /**
     * Adds wheel changing listener
     * @param listener the listener
     */
    fun addChangingListener(listener: OnWheelChangedListener) {
        changingListeners.add(listener)
    }

    /**
     * Removes wheel changing listener
     * @param listener the listener
     */
    fun removeChangingListener(listener: OnWheelChangedListener) {
        changingListeners.remove(listener)
    }

    /**
     * Notifies changing listeners
     * @param oldValue the old wheel value
     * @param newValue the new wheel value
     */
    protected fun notifyChangingListeners(oldValue: Int, newValue: Int) {
        for (listener in changingListeners) {
            listener.onChanged(this, oldValue, newValue)
        }
    }

    /**
     * Adds wheel scrolling listener
     * @param listener the listener
     */
    fun addScrollingListener(listener: OnWheelScrollListener) {
        scrollingListeners.add(listener)
    }

    /**
     * Removes wheel scrolling listener
     * @param listener the listener
     */
    fun removeScrollingListener(listener: OnWheelScrollListener) {
        scrollingListeners.remove(listener)
    }

    /**
     * Notifies listeners about starting scrolling
     */
    protected fun notifyScrollingListenersAboutStart() {
        for (listener in scrollingListeners) {
            listener.onScrollingStarted(this)
        }
    }

    /**
     * Notifies listeners about ending scrolling
     */
    protected fun notifyScrollingListenersAboutEnd() {
        for (listener in scrollingListeners) {
            listener.onScrollingFinished(this)
        }
    }

    /**
     * Adds wheel clicking listener
     * @param listener the listener
     */
    fun addClickingListener(listener: OnWheelClickedListener) {
        clickingListeners.add(listener)
    }

    /**
     * Removes wheel clicking listener
     * @param listener the listener
     */
    fun removeClickingListener(listener: OnWheelClickedListener) {
        clickingListeners.remove(listener)
    }

    /**
     * Notifies listeners about clicking
     */
    protected fun notifyClickListenersAboutClick(item: Int) {
        for (listener in clickingListeners) {
            listener.onItemClicked(this, item)
        }
    }

    /**
     * Sets the current item. Does nothing when index is wrong.
     *
     * @param index the item index
     * @param animated the animation flag
     */
    fun setCurrentItem(index: Int, animated: Boolean) {
        var index = index
        if (viewAdapter == null || viewAdapter!!.itemsCount == 0) {
            return  // throw?
        }
        val itemCount = viewAdapter!!.itemsCount
        if (index < 0 || index >= itemCount) {
            if (isCyclic) {
                while (index < 0) {
                    index += itemCount
                }
                index %= itemCount
            } else {
                return  // throw?
            }
        }
        if (index != currentItem) {
            if (animated) {
                var itemsToScroll = index - currentItem
                if (isCyclic) {
                    val scroll = itemCount + Math.min(index, currentItem) - Math.max(index, currentItem)
                    if (scroll < Math.abs(itemsToScroll)) {
                        itemsToScroll = if (itemsToScroll < 0) scroll else -scroll
                    }
                }
                scroll(itemsToScroll, 0)
            } else {
                scrollingOffset = 0
                val old = currentItem
                currentItem = index
                notifyChangingListeners(old, currentItem)
                invalidate()
            }
        }
    }

    /**
     * Sets the current item w/o animation. Does nothing when index is wrong.
     *
     * @param index the item index
     */
    fun setCurrentItem0(index: Int) {
        setCurrentItem(index, false)
    }

    /**
     * Tests if wheel is cyclic. That means before the 1st item there is shown the last one
     * @return true if wheel is cyclic
     */
    fun isCyclic0(): Boolean {
        return isCyclic
    }

    /**
     * Set wheel cyclic flag
     * @param isCyclic the flag to set
     */
    fun setCyclic0(isCyclic: Boolean) {
        this.isCyclic = isCyclic
        invalidateWheel(false)
    }

    /**
     * Determine whether shadows are drawn
     * @return true is shadows are drawn
     */
    fun drawShadows(): Boolean {
        return drawShadows
    }

    /**
     * Set whether shadows should be drawn
     * @param drawShadows flag as true or false
     */
    fun setDrawShadows(drawShadows: Boolean) {
        this.drawShadows = drawShadows
    }

    /**
     * Set the shadow gradient color
     * @param start
     * @param middle
     * @param end
     */
    fun setShadowColor(start: Int, middle: Int, end: Int) {
        SHADOWS_COLORS = intArrayOf(start, middle, end)
    }

    /**
     * Sets the drawable for the wheel background
     * @param resource
     */
    fun setWheelBackground(resource: Int) {
        wheelBackground = resource
        setBackgroundResource(wheelBackground)
    }

    /**
     * Sets the drawable for the wheel foreground
     * @param resource
     */
    fun setWheelForeground(resource: Int) {
        wheelForeground = resource
        centerDrawable = context.resources.getDrawable(wheelForeground)
    }

    /**
     * Invalidates wheel
     * @param clearCaches if true then cached views will be clear
     */
    fun invalidateWheel(clearCaches: Boolean) {
        if (clearCaches) {
            recycle.clearAll()
            if (itemsLayout != null) {
                itemsLayout?.removeAllViews()
            }
            scrollingOffset = 0
        } else if (itemsLayout != null) {
            // cache all items
            recycle.recycleItems(itemsLayout!!, firstItem, ItemsRange())
        }
        invalidate()
    }

    /**
     * Initializes resources
     */
    private fun initResourcesIfNecessary() {
        if (centerDrawable == null) {
            centerDrawable = context.resources.getDrawable(wheelForeground)
        }
        if (topShadow == null) {
            topShadow = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, SHADOWS_COLORS)
        }
        if (bottomShadow == null) {
            bottomShadow = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, SHADOWS_COLORS)
        }
        setBackgroundResource(wheelBackground)
    }

    /**
     * Calculates desired height for layout
     *
     * @param layout
     * the source layout
     * @return the desired layout height
     */
    private fun getDesiredHeight(layout: LinearLayout?): Int {
        if (layout != null && layout.getChildAt(0) != null) {
            itemHeight = layout.getChildAt(0).measuredHeight
        }
        val desired = itemHeight * visibleItems - itemHeight * ITEM_OFFSET_PERCENT / 50
        return Math.max(desired, suggestedMinimumHeight)
    }

    /**
     * Returns height of wheel item
     * @return the item height
     */
    private fun getItemHeight(): Int {
        if (itemHeight != 0) {
            return itemHeight
        }
        if (itemsLayout != null && itemsLayout?.getChildAt(0) != null) {
            itemHeight = itemsLayout?.getChildAt(0)!!.height
            return itemHeight
        }
        return height / visibleItems
    }

    /**
     * Calculates control width and creates text layouts
     * @param widthSize the input layout width
     * @param mode the layout mode
     * @return the calculated control width
     */
    private fun calculateLayoutWidth(widthSize: Int, mode: Int): Int {
        initResourcesIfNecessary()

        // TODO: make it static
        itemsLayout?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        itemsLayout?.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        var width = itemsLayout?.measuredWidth!!
        if (mode == MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            width += 2 * PADDING

            // Check against our minimum width
            width = max(width, suggestedMinimumWidth)
            if (mode == MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize
            }
        }
        itemsLayout?.measure(MeasureSpec.makeMeasureSpec(width - 2 * PADDING, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        return width
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        buildViewForMeasuring()
        val width = calculateLayoutWidth(widthSize, widthMode)
        var height: Int
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            height = getDesiredHeight(itemsLayout)
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize)
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layout(r - l, b - t)
    }

    /**
     * Sets layouts width and height
     * @param width the layout width
     * @param height the layout height
     */
    private fun layout(width: Int, height: Int) {
        val itemsWidth = width - 2 * PADDING
        itemsLayout?.layout(0, 0, itemsWidth, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (viewAdapter != null && viewAdapter!!.itemsCount > 0) {
            updateView()
            drawItems(canvas)
            drawCenterRect(canvas)
        }
        if (drawShadows) {
            drawShadows(canvas)
        }
    }

    /**
     * Draws shadows on top and bottom of control
     * @param canvas the canvas for drawing
     */
    private fun drawShadows(canvas: Canvas) {
        val height = (1.5 * getItemHeight()).toInt()
        topShadow?.setBounds(0, 0, width, height)
        topShadow?.draw(canvas)
        bottomShadow?.setBounds(0, getHeight() - height, width, getHeight())
        bottomShadow?.draw(canvas)
    }

    /**
     * Draws items
     * @param canvas the canvas for drawing
     */
    private fun drawItems(canvas: Canvas) {
        canvas.save()
        val top = (currentItem - firstItem) * getItemHeight() + (getItemHeight() - height) / 2
        canvas.translate(PADDING.toFloat(), -top + scrollingOffset.toFloat())
        itemsLayout?.draw(canvas)
        canvas.restore()
    }

    /**
     * Draws rect for current value
     * @param canvas the canvas for drawing
     */
    private fun drawCenterRect(canvas: Canvas) {
        val center = height / 2
        val offset = (getItemHeight() / 2 * 1.2).toInt()
        centerDrawable?.setBounds(0, center - offset, width, center + offset)
        centerDrawable?.draw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled || viewAdapter == null) {
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_MOVE -> if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_UP -> if (!isScrollingPerformed) {
                var distance = event.y.toInt() - height / 2
                if (distance > 0) {
                    distance += getItemHeight() / 2
                } else {
                    distance -= getItemHeight() / 2
                }
                val items = distance / getItemHeight()
                if (items != 0 && isValidItemIndex(currentItem + items)) {
                    notifyClickListenersAboutClick(currentItem + items)
                }
            }
        }
        return scroller?.onTouchEvent(event)!!
    }

    /**
     * Scrolls the wheel
     * @param delta the scrolling value
     */
    private fun doScroll(delta: Int) {
        scrollingOffset += delta
        val itemHeight = getItemHeight()
        var count = scrollingOffset / itemHeight
        var pos = currentItem - count
        val itemCount = viewAdapter?.itemsCount!!
        var fixPos = scrollingOffset % itemHeight
        if (abs(fixPos) <= itemHeight / 2) {
            fixPos = 0
        }
        if (isCyclic && itemCount > 0) {
            if (fixPos > 0) {
                pos--
                count++
            } else if (fixPos < 0) {
                pos++
                count--
            }
            // fix position by rotating
            while (pos < 0) {
                pos += itemCount
            }
            pos %= itemCount
        } else {
            //
            if (pos < 0) {
                count = currentItem
                pos = 0
            } else if (pos >= itemCount) {
                count = currentItem - itemCount + 1
                pos = itemCount - 1
            } else if (pos > 0 && fixPos > 0) {
                pos--
                count++
            } else if (pos < itemCount - 1 && fixPos < 0) {
                pos++
                count--
            }
        }
        val offset = scrollingOffset
        if (pos != currentItem) {
            setCurrentItem(pos, false)
        } else {
            invalidate()
        }

        // update offset
        scrollingOffset = offset - count * itemHeight
        if (scrollingOffset > height) {
            scrollingOffset = if (height <= 0) {
                0
            } else {
                scrollingOffset % height + height
            }
        }
    }

    /**
     * Scroll the wheel
     * @param itemsToScroll items to scroll
     * @param time scrolling duration
     */
    fun scroll(itemsToScroll: Int, time: Int) {
        val distance = itemsToScroll * getItemHeight() - scrollingOffset
        scroller?.scroll(distance, time)
    }// process empty items above the first or below the second// top + bottom items

    /**
     * Calculates range for wheel items
     * @return the items range
     */
    private val itemsRange: ItemsRange?
        get() {
            if (getItemHeight() == 0) {
                return null
            }
            var first = currentItem
            var count = 1
            while (count * getItemHeight() < height) {
                first--
                count += 2 // top + bottom items
            }
            if (scrollingOffset != 0) {
                if (scrollingOffset > 0) {
                    first--
                }
                count++

                // process empty items above the first or below the second
                val emptyItems = scrollingOffset / getItemHeight()
                first -= emptyItems
                count += asin(emptyItems.toDouble()).toInt()
            }
            return ItemsRange(first, count)
        }

    /**
     * Rebuilds wheel items if necessary. Caches all unused items.
     *
     * @return true if items are rebuilt
     */
    private fun rebuildItems(): Boolean {
        var updated = false
        val range = itemsRange
        if (itemsLayout != null) {
            val first = recycle.recycleItems(itemsLayout!!, firstItem, range!!)
            updated = firstItem != first
            firstItem = first
        } else {
            createItemsLayout()
            updated = true
        }
        if (!updated) {
            updated = firstItem != range?.first || itemsLayout?.childCount != range.count
        }
        if (firstItem > range?.first!! && firstItem <= range.last) {
            for (i in firstItem - 1 downTo range.first) {
                if (!addViewItem(i, true)) {
                    break
                }
                firstItem = i
            }
        } else {
            firstItem = range.first
        }
        var first = firstItem
        for (i in itemsLayout!!.childCount until range.count) {
            if (!addViewItem(firstItem + i, false) && itemsLayout?.childCount == 0) {
                first++
            }
        }
        firstItem = first
        return updated
    }

    /**
     * Updates view. Rebuilds items and label if necessary, recalculate items sizes.
     */
    private fun updateView() {
        if (rebuildItems()) {
            calculateLayoutWidth(width, MeasureSpec.EXACTLY)
            layout(width, height)
        }
    }

    /**
     * Creates item layouts if necessary
     */
    private fun createItemsLayout() {
        if (itemsLayout == null) {
            itemsLayout = LinearLayout(context)
            itemsLayout?.orientation = LinearLayout.VERTICAL
        }
    }

    /**
     * Builds view for measuring
     */
    private fun buildViewForMeasuring() {
        // clear all items
        if (itemsLayout != null) {
            recycle.recycleItems(itemsLayout!!, firstItem, ItemsRange())
        } else {
            createItemsLayout()
        }

        // add views
        // all items must be included to measure width correctly
        for (i in viewAdapter?.itemsCount!! - 1 downTo 0) {
            if (addViewItem(i, true)) {
                firstItem = i
            }
        }
    }

    /**
     * Adds view for item to items layout
     * @param index the item index
     * @param first the flag indicates if view should be first
     * @return true if corresponding item exists and is added
     */
    private fun addViewItem(index: Int, first: Boolean): Boolean {
        val view = getItemView(index)
        if (view != null) {
            if (first) {
                itemsLayout?.addView(view, 0)
            } else {
                itemsLayout?.addView(view)
            }
            return true
        }
        return false
    }

    /**
     * Checks whether intem index is valid
     * @param index the item index
     * @return true if item index is not out of bounds or the wheel is cyclic
     */
    private fun isValidItemIndex(index: Int?): Boolean {
        return viewAdapter != null && viewAdapter?.itemsCount!! > 0 &&
                (isCyclic || index!! >= 0 && index < viewAdapter?.itemsCount!!)
    }

    /**
     * Returns view for specified item
     * @param index the item index
     * @return item view or empty view if index is out of bounds
     */
    private fun getItemView(index: Int): View? {
        var index1 = index
        if (viewAdapter == null || viewAdapter?.itemsCount == 0) {
            return null
        }
        val count = viewAdapter?.itemsCount
        if (!isValidItemIndex(index1)) {
            return viewAdapter?.getEmptyItem(recycle.emptyItem, itemsLayout)
        } else {
            while (index1 < 0) {
                index1 += count!!
            }
        }
        index1 %= count!!

        val view = recycle.item
        val viewGroup = itemsLayout
        return viewAdapter?.getItem(index1, view, viewGroup)
    }

    /**
     * Stops scrolling
     */
    fun stopScrolling() {
        scroller!!.stopScrolling()
    }

    companion object {
        /** Top and bottom items offset (to hide that)  */
        private const val ITEM_OFFSET_PERCENT = 0

        /** Left and right padding value  */
        private const val PADDING = 10

        /** Default count of visible items  */
        private const val DEF_VISIBLE_ITEMS = 5
    }
}