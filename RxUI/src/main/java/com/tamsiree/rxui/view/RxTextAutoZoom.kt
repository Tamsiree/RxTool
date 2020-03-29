package com.tamsiree.rxui.view

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.util.*

/**
 * @author tamsiree
 */
@SuppressLint("AppCompatCustomView")
class RxTextAutoZoom : EditText {
    private val availableSpaceRect = RectF()
    private val textCachedSizes = SparseIntArray()
    private var sizeTester: SizeTester? = null
    private var maxTextSize = 0f
    private var spacingMult = 1.0f
    private var spacingAdd = 0.0f
    private var minTextSize: Float? = null
    private var widthLimit = 0
    private var maxLines = 0
    private var enableSizeCache = true
    private var initiallized = false
    private var textPaint: TextPaint? = null

    constructor(context: Context?) : this(context, null, 0) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?,
                defStyle: Int) : super(context, attrs, defStyle) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    override fun setTypeface(tf: Typeface?) {
        if (textPaint == null) {
            textPaint = TextPaint(paint)
        }
        textPaint!!.typeface = tf
        super.setTypeface(tf)
    }

    override fun setTextSize(size: Float) {
        maxTextSize = size
        textCachedSizes.clear()
        adjustTextSize()
    }

    override fun setMaxLines(maxlines: Int) {
        super.setMaxLines(maxlines)
        maxLines = maxlines
        reAdjust()
    }

    override fun getMaxLines(): Int {
        return maxLines
    }

    override fun setSingleLine() {
        super.setSingleLine()
        maxLines = 1
        reAdjust()
    }

    override fun setSingleLine(singleLine: Boolean) {
        super.setSingleLine(singleLine)
        maxLines = if (singleLine) {
            1
        } else {
            NO_LINE_LIMIT
        }
        reAdjust()
    }

    override fun setLines(lines: Int) {
        super.setLines(lines)
        maxLines = lines
        reAdjust()
    }

    override fun setTextSize(unit: Int, size: Float) {
        val c = context
        val r: Resources
        r = if (c == null) {
            Resources.getSystem()
        } else {
            c.resources
        }
        maxTextSize = TypedValue.applyDimension(unit, size,
                r.displayMetrics)
        textCachedSizes.clear()
        adjustTextSize()
    }

    override fun setLineSpacing(add: Float, mult: Float) {
        super.setLineSpacing(add, mult)
        spacingMult = mult
        spacingAdd = add
    }

    private fun initView() {

        //获得这个控件对应的属性。

        // using the minimal recommended font size
        minTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)
        maxTextSize = textSize
        if (maxLines == 0) {
            // no value was assigned during construction
            maxLines = NO_LINE_LIMIT
        }
        // prepare size tester:
        sizeTester = object : SizeTester {
            val textRect = RectF()

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            override fun onTestSize(suggestedSize: Int,
                                    availableSPace: RectF): Int {
                textPaint!!.textSize = suggestedSize.toFloat()
                val text = text.toString()
                val singleline = getMaxLines() == 1
                if (singleline) {
                    textRect.bottom = textPaint!!.fontSpacing
                    textRect.right = textPaint!!.measureText(text)
                } else {
                    val layout = StaticLayout(text, textPaint,
                            widthLimit, Layout.Alignment.ALIGN_NORMAL, spacingMult,
                            spacingAdd, true)
                    if (getMaxLines() != NO_LINE_LIMIT
                            && layout.lineCount > getMaxLines()) {
                        return 1
                    }
                    textRect.bottom = layout.height.toFloat()
                    var maxWidth = -1
                    for (i in 0 until layout.lineCount) {
                        if (maxWidth < layout.getLineWidth(i)) {
                            maxWidth = layout.getLineWidth(i).toInt()
                        }
                    }
                    textRect.right = maxWidth.toFloat()
                }
                textRect.offsetTo(0f, 0f)
                return if (availableSPace.contains(textRect)) {
                    // may be too small, don't worry we will find the best match
                    -1
                } else 1
                // else, too big
            }
        }
        initiallized = true
    }

    fun getMinTextSize(): Float? {
        return minTextSize
    }

    private fun reAdjust() {
        adjustTextSize()
    }

    private fun adjustTextSize() {
        if (!initiallized) {
            return
        }
        val startSize = Math.round(minTextSize!!)
        val heightLimit = (measuredHeight
                - compoundPaddingBottom - compoundPaddingTop)
        widthLimit = (measuredWidth - compoundPaddingLeft
                - compoundPaddingRight)
        if (widthLimit <= 0) {
            return
        }
        availableSpaceRect.right = widthLimit.toFloat()
        availableSpaceRect.bottom = heightLimit.toFloat()
        super.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                efficientTextSizeSearch(startSize, maxTextSize.toInt(),
                        sizeTester, availableSpaceRect).toFloat())
    }

    /**
     * Enables or disables size caching, enabling it will improve performance
     * where you are animating a value inside TextView. This stores the font
     * size against getText().length() Be careful though while enabling it as 0
     * takes more space than 1 on some fonts and so on.
     *
     * @param enable enable font size caching
     */
    fun setEnableSizeCache(enable: Boolean) {
        enableSizeCache = enable
        textCachedSizes.clear()
        adjustTextSize()
    }

    /**
     * Set the lower text size limit and invalidate the view
     *
     * @param minTextSize 最小的文字大小
     */
    fun setMinTextSize(minTextSize: Float?) {
        this.minTextSize = minTextSize
        reAdjust()
    }

    private fun efficientTextSizeSearch(start: Int, end: Int,
                                        sizeTester: SizeTester?, availableSpace: RectF): Int {
        if (!enableSizeCache) {
            return binarySearch(start, end, sizeTester, availableSpace)
        }
        val text = text.toString()
        val key = text.length
        var size = textCachedSizes[key]
        if (size != 0) {
            return size
        }
        size = binarySearch(start, end, sizeTester, availableSpace)
        textCachedSizes.put(key, size)
        return size
    }

    override fun onTextChanged(text: CharSequence, start: Int,
                               before: Int, after: Int) {
        super.onTextChanged(text, start, before, after)
        reAdjust()
    }

    override fun onSizeChanged(width: Int, height: Int,
                               oldwidth: Int, oldheight: Int) {
        textCachedSizes.clear()
        super.onSizeChanged(width, height, oldwidth, oldheight)
        if (width != oldwidth || height != oldheight) {
            reAdjust()
        }
    }

    private fun binarySearch(start: Int, end: Int,
                             sizeTester: SizeTester?, availableSpace: RectF): Int {
        var lastBest = start
        var lo = start
        var hi = end - 1
        var mid: Int
        while (lo <= hi) {
            mid = lo + hi ushr 1
            val midValCmp = sizeTester!!.onTestSize(mid, availableSpace)
            if (midValCmp < 0) {
                lastBest = lo
                lo = mid + 1
            } else if (midValCmp > 0) {
                hi = mid - 1
                lastBest = hi
            } else {
                return mid
            }
        }
        // make sure to return last best
        // this is what should always be returned
        return lastBest
    }

    interface SizeTester {
        /**
         * AutoFitEditText
         *
         * @param suggestedSize  Size of text to be tested
         * @param availableSpace available space in which text must fit
         * @return an integer < 0 if after applying `suggestedSize` to
         * text, it takes less space than `availableSpace`, > 0
         * otherwise
         */
        fun onTestSize(suggestedSize: Int, availableSpace: RectF): Int
    }

    companion object {
        private const val NO_LINE_LIMIT = -1

        @SuppressLint("ClickableViewAccessibility")
        fun setNormalization(a: Activity, rootView: View, aText: RxTextAutoZoom) {

            // if the view is not instance of AutoFitEditText
            // i.e. if the user taps outside of the box
            if (rootView !is RxTextAutoZoom) {
                rootView.setOnTouchListener { v: View?, event: MotionEvent? ->
                    hideSoftKeyboard(a)
                    if (aText.getMinTextSize() != null && aText.textSize < aText.getMinTextSize()!!) {
                        // you can define your minSize, in this case is 50f
                        // trim all the new lines and set the text as it was
                        // before
                        aText.setText(aText.text.toString().replace("\n", ""))
                    }
                    false
                }
            }

            // If a layout container, iterate over children and seed recursion.
            if (rootView is ViewGroup) {
                for (i in 0 until rootView.childCount) {
                    val innerView = rootView.getChildAt(i)
                    setNormalization(a, innerView, aText)
                }
            }
        }

        fun hideSoftKeyboard(a: Activity) {
            val inputMethodManager = a
                    .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (a.currentFocus != null
                    && a.currentFocus!!.windowToken != null) {
                Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(a.currentFocus!!.windowToken, 0)
            }
        }
    }
}