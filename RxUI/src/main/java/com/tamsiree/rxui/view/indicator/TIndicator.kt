package com.tamsiree.rxui.view.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.tamsiree.rxkit.RxImageTool
import com.tamsiree.rxui.R

class TIndicator @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : View(context, attrs, defStyle), ViewPager.OnPageChangeListener {

    companion object {

        private const val DEFAULT_DOT_COUNT = 5
        private const val DEFAULT_FADING_DOT_COUNT = 1
        private const val DEFAULT_DOT_RADIUS_DP = 4
        private const val DEFAULT_SELECTED_DOT_RADIUS_DP = 5.5f

        // Measured outside of first dot to outside of next dot: O<->O
        private const val DEFAULT_DOT_SEPARATION_DISTANCE_DP = 10

    }

    private var recyclerView: RecyclerView? = null
    private var viewPager: ViewPager? = null
    private var internalRecyclerScrollListener: InternalRecyclerScrollListener? = null
    private val interpolator = DecelerateInterpolator()

    private var dotCount = DEFAULT_DOT_COUNT
    private var fadingDotCount = DEFAULT_FADING_DOT_COUNT
    private var selectedDotRadiusPx = RxImageTool.dp2px(context, DEFAULT_SELECTED_DOT_RADIUS_DP)
    private var dotRadiusPx = RxImageTool.dp2px(context, DEFAULT_DOT_RADIUS_DP.toFloat())
    private var dotSeparationDistancePx =
            RxImageTool.dp2px(context, DEFAULT_DOT_SEPARATION_DISTANCE_DP.toFloat())
    private var supportRtl = false
    private var verticalSupport = false

    @ColorInt
    private var dotColor: Int = ContextCompat.getColor(this.context, R.color.E8)

    @ColorInt
    private var selectedDotColor: Int = ContextCompat.getColor(
            this.context,
            R.color.White
    )
    private val selectedDotPaint = Paint()
    private val dotPaint = Paint()

    /**
     * The current pager position. Used to draw the selected dot if different size/color.
     */
    private var selectedItemPosition: Int = 0

    /**
     * A temporary value used to reflect changes/transition from one selected item to the next.
     */
    private var intermediateSelectedItemPosition: Int = 0

    /**
     * The scroll percentage of the viewpager or recyclerview.
     * Used for moving the dots/scaling the fading dots.
     */
    private var offsetPercent: Float = 0f

    init {
        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.TIndicator,
                    0,
                    0
            )
            dotCount = typedArray.getInteger(
                    R.styleable.TIndicator_dotCount,
                    DEFAULT_DOT_COUNT
            )
            fadingDotCount = typedArray.getInt(
                    R.styleable.TIndicator_fadingDotCount,
                    DEFAULT_FADING_DOT_COUNT
            )
            dotRadiusPx = typedArray.getDimensionPixelSize(
                    R.styleable.TIndicator_dotRadius,
                    dotRadiusPx
            )
            selectedDotRadiusPx = typedArray.getDimensionPixelSize(
                    R.styleable.TIndicator_selectedDotRadius,
                    selectedDotRadiusPx
            )
            dotColor = typedArray.getColor(R.styleable.TIndicator_dotColor, dotColor)
            selectedDotColor = typedArray.getColor(
                    R.styleable.TIndicator_selectedDotColor,
                    selectedDotColor
            )
            dotSeparationDistancePx = typedArray.getDimensionPixelSize(
                    R.styleable.TIndicator_dotSeparation,
                    dotSeparationDistancePx
            )
            supportRtl =
                    typedArray.getBoolean(R.styleable.TIndicator_supportRTL, false)
            verticalSupport = typedArray.getBoolean(
                    R.styleable.TIndicator_verticalSupport,
                    false
            )
        }

        selectedDotPaint.apply {
            style = Paint.Style.FILL
            color = selectedDotColor
            isAntiAlias = true
        }
        dotPaint.apply {
            style = Paint.Style.FILL
            color = dotColor
            isAntiAlias = true
        }
    }

    /**
     * Iterate over the total pager item count and draw every dot based on position.
     *
     * Helper methods - getDotCoordinate(Int) & getRadius(Int)
     * will return values outside the calculated width or with an invalid radius
     * if the dot is not to be drawn.
     *
     * TODO: "Instagram style" drawing where all dots are drawn at once.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        (0 until getPagerItemCount())
                .map { getDotCoordinate(it) }
                .forEach {
                    val xPosition: Float
                    val yPosition: Float
                    if (verticalSupport) {
                        xPosition = getDotYCoordinate().toFloat()
                        yPosition = height / 2 + it
                    } else {
                        xPosition = width / 2 + it
                        yPosition = getDotYCoordinate().toFloat()
                    }
                    canvas.drawCircle(xPosition, yPosition, getRadius(it), getPaint(it))
                }
    }

    /**
     * Set the dimensions of the TIndicator.
     * Width/Height is calculated below with getCalculatedWidth().
     * Width/Height is simply the diameter of the largest circle.
     *
     * TODO: Add support for padding.
     * TODO: Add support for MATCH_PARENT
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minimumViewSize = 2 * selectedDotRadiusPx
        if (verticalSupport) {
            setMeasuredDimension(minimumViewSize, getCalculatedWidth())
        } else {
            setMeasuredDimension(getCalculatedWidth(), minimumViewSize)
        }
    }


    /**
     * Gets the coordinate for a dot based on the position in the pager.
     */
    private fun getDotCoordinate(pagerPosition: Int): Float =
            (pagerPosition - intermediateSelectedItemPosition) * getDistanceBetweenTheCenterOfTwoDots() +
                    (getDistanceBetweenTheCenterOfTwoDots() * offsetPercent)

    /**
     * Get the y coordinate for a dot.
     *
     * The bottom of the view is y = 0 and a dot is drawn from the center, so therefore
     * the y coordinate is simply the radius.
     */
    private fun getDotYCoordinate(): Int = selectedDotRadiusPx

    /**
     * Calculates the distance between 2 dots center.
     */
    private fun getDistanceBetweenTheCenterOfTwoDots() = 2 * dotRadiusPx + dotSeparationDistancePx

    /**
     * Calculates a dot's radius based on it's position.
     *
     * If the position is within 1 dot's length, it's the currently selected dot.
     *
     * If the position is within a threshold (half the width of the number of non fading dots),
     * it is a normal sized dot.
     *
     * If the position is outside of the above threshold, it is a fading dot or not visible. The
     * radius is calculated based on a interpolator percentage of how far the
     * viewpager/recyclerview has scrolled.
     */
    private fun getRadius(coordinate: Float): Float {
        val coordinateAbs = Math.abs(coordinate)
        // Get the coordinate where dots begin showing as fading dots (x coordinates > half of width of all large dots)
        val largeDotThreshold = dotCount.toFloat() / 2 * getDistanceBetweenTheCenterOfTwoDots()
        return when {
            coordinateAbs < getDistanceBetweenTheCenterOfTwoDots() / 2 -> selectedDotRadiusPx.toFloat()
            coordinateAbs <= largeDotThreshold -> dotRadiusPx.toFloat()
            else -> {
                // Determine how close the dot is to the edge of the view for scaling the size of the dot
                val percentTowardsEdge = (coordinateAbs - largeDotThreshold) /
                        (getCalculatedWidth() / 2.01f - largeDotThreshold)
                interpolator.getInterpolation(1 - percentTowardsEdge) * dotRadiusPx
            }
        }
    }

    /**
     * Returns the dot's color based on coordinate, similar to {@link #getRadius(Float)}.
     *
     * If the position is within 1 dot's length of x or y = 0, it is the currently selected dot.
     *
     * All other dots will be the normal specified dot color.
     */
    private fun getPaint(coordinate: Float): Paint = when {
        Math.abs(coordinate) < getDistanceBetweenTheCenterOfTwoDots() / 2 -> selectedDotPaint
        else -> dotPaint
    }

    /**
     * Get the calculated width of the view.
     *
     * Calculated by the total number of visible dots (normal & fading).
     *
     * TODO: Add support for padding.
     */
    private fun getCalculatedWidth(): Int {
        val maxNumVisibleDots = dotCount + 2 * fadingDotCount
        return (maxNumVisibleDots - 1) * getDistanceBetweenTheCenterOfTwoDots() + 2 * dotRadiusPx
    }

    /**
     * Attach a RecyclerView to the Pager Indicator.
     *
     * If ViewPager previously attached, remove this class (page change listener) and set to null.
     * If other RecyclerView previously attached, remove internal scroll listener.
     */
    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        viewPager?.removeOnPageChangeListener(this)
        viewPager = null

        internalRecyclerScrollListener?.let { this.recyclerView?.removeOnScrollListener(it) }

        this.recyclerView = recyclerView
        internalRecyclerScrollListener = InternalRecyclerScrollListener()
        this.recyclerView?.addOnScrollListener(internalRecyclerScrollListener!!)
    }

    /**
     * Attach a ViewPager to the Pager Indicator.
     *
     * If RecyclerView previously attached, scroll listener will be removed and RV set to null.
     * If other ViewPager previously attached, remove reference to this class (page change listener).
     */
    fun attachToViewPager(viewPager: ViewPager?) {
        internalRecyclerScrollListener?.let { recyclerView?.removeOnScrollListener(it) }
        recyclerView = null

        this.viewPager?.removeOnPageChangeListener(this)

        this.viewPager = viewPager
        this.viewPager?.addOnPageChangeListener(this)

        selectedItemPosition = viewPager?.currentItem!!
    }

    private fun getPagerItemCount(): Int = when {
        recyclerView != null -> recyclerView?.adapter?.itemCount!!
        viewPager != null -> viewPager?.adapter?.count!!
        else -> 0
    }

    /**
     * Checks if the View is in RTL direction
     */
    private fun isRtl() = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL

    /**
     * Gets the RTL position of the position in any adapter
     */
    private fun getRTLPosition(position: Int) = getPagerItemCount() - position - 1

    /**
     * ViewPager.OnPageChangeListener implementation.
     *
     * Used to update the intermediateSelectedPosition & offsetPercent when the page is scrolled.
     * OffsetPercent multiplied by -1 to account for natural swipe movement.
     */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (supportRtl && isRtl()) {
            intermediateSelectedItemPosition = getRTLPosition(position)
            offsetPercent = positionOffset * 1
        } else {
            intermediateSelectedItemPosition = position
            offsetPercent = positionOffset * -1
        }
        invalidate()
    }

    override fun onPageSelected(position: Int) {
        intermediateSelectedItemPosition = selectedItemPosition
        selectedItemPosition = if (supportRtl && isRtl()) {
            getRTLPosition(position)
        } else {
            position
        }
        invalidate()
    }

    override fun onPageScrollStateChanged(state: Int) {
        // Not implemented
    }

    /**
     * Internal scroll listener to handle the scaling/fading/selected dot states for a RecyclerView.
     */
    internal inner class InternalRecyclerScrollListener : RecyclerView.OnScrollListener() {

        /**
         * The previous most visible child page in the RecyclerView.
         *
         * Used to differentiate between the current most visible child page to correctly determine
         * the currently selected item and percentage scrolled.
         */
        private var previousMostVisibleChild: View? = null

        /**
         * Determine based on the percentage a child viewholder's view is visible what position
         * is the currently selected.
         *
         * Use this percentage to also calculate the offsetPercentage
         * used to scale dots.
         */
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            val view = getMostVisibleChild()

            if (view != null) {
                setIntermediateSelectedItemPosition(view)
                offsetPercent = view.left.toFloat() / view.measuredWidth
            }

            with(recyclerView.layoutManager as LinearLayoutManager) {
                val visibleItemPosition =
                        if (dx >= 0) findLastVisibleItemPosition() else findFirstVisibleItemPosition()

                if (previousMostVisibleChild !== findViewByPosition(visibleItemPosition)) {
                    selectedItemPosition = intermediateSelectedItemPosition
                }
            }

            previousMostVisibleChild = view
            invalidate()
        }

        /**
         * Returns the currently most visible viewholder view in the Recyclerview.
         *
         * The most visible view is determined based on percentage of the view visible. This is
         * calculated below in calculatePercentVisible().
         */
        private fun getMostVisibleChild(): View? {
            var mostVisibleChild: View? = null
            var mostVisibleChildPercent = 0f
            for (i in recyclerView?.layoutManager?.childCount!! - 1 downTo 0) {
                val child = recyclerView?.layoutManager?.getChildAt(i)
                if (child != null) {
                    val percentVisible = calculatePercentVisible(child)
                    if (percentVisible >= mostVisibleChildPercent) {
                        mostVisibleChildPercent = percentVisible
                        mostVisibleChild = child
                    }
                }
            }

            return mostVisibleChild
        }

        private fun calculatePercentVisible(child: View): Float {
            val left = child.left
            val right = child.right
            val width = child.width

            return when {
                left < 0 -> right / width.toFloat()
                right > getWidth() -> (getWidth() - left) / width.toFloat()
                else -> 1f
            }
        }

        private fun setIntermediateSelectedItemPosition(mostVisibleChild: View?) {
            with(recyclerView?.findContainingViewHolder(mostVisibleChild!!)?.adapterPosition!!) {
                intermediateSelectedItemPosition = if (isRtl() && !verticalSupport) {
                    getRTLPosition(this)
                } else {
                    this
                }
            }
        }
    }
}
