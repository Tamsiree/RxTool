package com.tamsiree.rxui.view.indicator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.tamsiree.rxkit.TLog.d
import com.tamsiree.rxui.R
import java.util.*

/**
 * TStep indicator that can be used with (or without) a [ViewPager] to display current progress through an
 * Onboarding or any process in multiple steps.
 * The default main primary color if not specified in the XML attributes will use the theme primary color defined
 * via `colorPrimary` attribute.
 * If this view is used on a device below API 11, animations will not be used.
 * Usage of stepper custom attributes:
 */
class TStepperIndicator : View, ViewPager.OnPageChangeListener {
    /**
     * Paint used to draw circle
     */
    private var circlePaint: Paint? = null

    /**
     * List of [Paint] objects used to draw the circle for each step.
     */
    private var stepsCirclePaintList: MutableList<Paint>? = null

    /**
     * The radius for the circle which describes an step.
     *
     *
     * This is either declared via XML or default is used.
     */
    private var circleRadius = 0f

    /**
     * Flag indicating if the steps should be displayed with an number instead of empty circles and current animated
     * with bullet.
     */
    private var showStepTextNumber = false

    /**
     * Paint used to draw the number indicator for all steps.
     */
    private var stepTextNumberPaint: Paint? = null

    /**
     * List of [Paint] objects used to draw the number indicator for each step.
     */
    private var stepsTextNumberPaintList: MutableList<Paint>? = null

    /**
     * Paint used to draw the indicator circle for the current and cleared steps
     */
    private var indicatorPaint: Paint? = null

    /**
     * List of [Paint] objects used by each step indicating the current and cleared steps.
     * If this is set, it will override the default.
     */
    private var stepsIndicatorPaintList: MutableList<Paint>? = null

    /**
     * Paint used to draw the line between steps - as default.
     */
    private var linePaint: Paint? = null

    /**
     * Paint used to draw the line between steps when done.
     */
    private var lineDonePaint: Paint? = null

    /**
     * Paint used to draw the line between steps when animated.
     */
    private var lineDoneAnimatedPaint: Paint? = null

    /**
     * List of [Path] for each line between steps
     */
    private val linePathList: MutableList<Path> = ArrayList()

    /**
     * The progress of the animation.
     * DO NOT DELETE OR RENAME: Will be used by animations logic.
     */
    private var animProgress = 0f

    /**
     * The radius for the animated indicator.
     * DO NOT DELETE OR RENAME: Will be used by animations logic.
     */
    private var animIndicatorRadius = 0f

    /**
     * The radius for the animated check mark.
     * DO NOT DELETE OR RENAME: Will be used by animations logic.
     */
    private var animCheckRadius = 0f

    /**
     * Flag indicating if the indicator for the current step should be displayed at the bottom.
     *
     *
     * This is useful if you want to use text number indicator for the steps as the bullet indicator will be
     * disabled for that flow.
     */
    private var useBottomIndicator = false

    /**
     * The top margin of the bottom indicator.
     */
    private var bottomIndicatorMarginTop = 0f

    /**
     * The width of the bottom indicator.
     */
    private var bottomIndicatorWidth = 0f

    /**
     * The height of the bottom indicator.
     */
    private var bottomIndicatorHeight = 0f

    /**
     * Flag indicating if the bottom indicator should use the same colors as the steps.
     */
    private var useBottomIndicatorWithStepColors = false

    /**
     * "Constant" size of the lines between steps
     */
    private var lineLength = 0f

    // Values retrieved from xml (or default values)
    private var checkRadius = 0f
    private var indicatorRadius = 0f
    private var lineMargin = 0f
    private var animDuration = 0

    /**
     * Custom step click listener which will notify any component which sets an listener of any events (touch events)
     * that happen regarding the steps widget.
     */
    private val onStepClickListeners: MutableList<OnStepClickListener>? = ArrayList(0)

    /**
     * Click areas for each of the steps supported by the StepperIndicator widget.
     */
    private var stepsClickAreas: MutableList<RectF>? = null

    /**
     * The gesture detector at which all the touch events will be propagated to.
     */
    private var gestureDetector: GestureDetector? = null
    private var stepCount = 0
    private var currentStep = 0
    private var previousStep = 0

    // X position of each step indicator's center
    private var indicators: FloatArray? = null

    // Utils to avoid object instantiation during onDraw
    private val stepAreaRect = Rect()
    private val stepAreaRectF = RectF()
    private var pager: ViewPager? = null
    private var doneIcon: Drawable? = null
    private var showDoneIcon = false

    // If viewpager is attached, viewpager's page titles are used when {@code showLabels} equals true
    private var labelPaint: TextPaint? = null
    private var labels: Array<CharSequence?>? = null
    private var showLabels = false
    private var labelMarginTop = 0f
    private var labelSize = 0f
    private var labelLayouts: Array<StaticLayout?>? = null
    private var maxLabelHeight = 0f

    // Running animations
    private var animatorSet: AnimatorSet? = null
    private var lineAnimator: ObjectAnimator? = null
    private var indicatorAnimator: ObjectAnimator? = null
    private var checkAnimator: ObjectAnimator? = null

    /**
     * Custom gesture listener though which all the touch events are propagated.
     *
     *
     * The whole purpose of this listener is to correctly detect which step was touched by the user and notify
     * the component which registered to receive event updates through
     * [.addOnStepClickListener]
     */
    private val gestureListener: GestureDetector.OnGestureListener = object : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            var clickedStep = STEP_INVALID
            if (isOnStepClickListenerAvailable) {
                for (i in stepsClickAreas!!.indices) {
                    if (stepsClickAreas!![i].contains(e.x, e.y)) {
                        clickedStep = i
                        // Stop as we found the step which was clicked
                        break
                    }
                }
            }

            // If the clicked step is valid and an listener was setup - send the event
            if (clickedStep != STEP_INVALID) {
                for (listener in onStepClickListeners!!) {
                    listener.onStepClicked(clickedStep)
                }
            }
            return super.onSingleTapConfirmed(e)
        }
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val resources = resources

        // Default values
        val defaultPrimaryColor = getPrimaryColor(context)
        val defaultCircleColor = ContextCompat.getColor(context, R.color.stpi_default_circle_color)
        val defaultCircleRadius = resources.getDimension(R.dimen.stpi_default_circle_radius)
        val defaultCircleStrokeWidth = resources.getDimension(R.dimen.stpi_default_circle_stroke_width)
        val defaultIndicatorRadius = resources.getDimension(R.dimen.stpi_default_indicator_radius)
        val defaultLineStrokeWidth = resources.getDimension(R.dimen.stpi_default_line_stroke_width)
        val defaultLineMargin = resources.getDimension(R.dimen.stpi_default_line_margin)
        val defaultLineColor = ContextCompat.getColor(context, R.color.stpi_default_line_color)

        /* Customize the widget based on the properties set on XML, or use default if not provided */
//        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TStepperIndicator, defStyleAttr, 0);
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.TStepperIndicator)
        circlePaint = Paint()
        circlePaint!!.strokeWidth = a.getDimension(R.styleable.TStepperIndicator_stpi_circleStrokeWidth, defaultCircleStrokeWidth)
        circlePaint!!.style = Paint.Style.STROKE
        circlePaint!!.color = a.getColor(R.styleable.TStepperIndicator_stpi_circleColor, defaultCircleColor)
        circlePaint!!.isAntiAlias = true

        // Call this as early as possible as other properties are configured based on the number of steps
        setStepCount(a.getInteger(R.styleable.TStepperIndicator_stpi_stepCount, 2))
        val stepsCircleColorsResId = a.getResourceId(R.styleable.TStepperIndicator_stpi_stepsCircleColors, 0)
        if (stepsCircleColorsResId != 0) {
            stepsCirclePaintList = ArrayList(stepCount)
            for (i in 0 until stepCount) {
                // Based on the main indicator paint object, we create the customized one
                val circlePaint = Paint(circlePaint)
                if (isInEditMode) {
                    // Fallback for edit mode - to show something in the preview
                    circlePaint.color = randomColor
                } else {
                    // Get the array of attributes for the colors
                    val colorResValues = context.resources.obtainTypedArray(stepsCircleColorsResId)
                    require(stepCount <= colorResValues.length()) {
                        "Invalid number of colors for the circles. Please provide a list " +
                                "of colors with as many items as the number of steps required!"
                    }
                    circlePaint.color = colorResValues.getColor(i, 0) // specific color
                    // No need for the array anymore, recycle it
                    colorResValues.recycle()
                }
                stepsCirclePaintList?.add(circlePaint)
            }
        }
        indicatorPaint = Paint(circlePaint)
        indicatorPaint!!.style = Paint.Style.FILL
        indicatorPaint!!.color = a.getColor(R.styleable.TStepperIndicator_stpi_indicatorColor, defaultPrimaryColor)
        indicatorPaint!!.isAntiAlias = true
        stepTextNumberPaint = Paint(indicatorPaint)
        stepTextNumberPaint!!.textSize = getResources().getDimension(R.dimen.stpi_default_text_size)
        showStepTextNumber = a.getBoolean(R.styleable.TStepperIndicator_stpi_showStepNumberInstead, false)

        // Get the resource from the context style properties
        val stepsIndicatorColorsResId = a
                .getResourceId(R.styleable.TStepperIndicator_stpi_stepsIndicatorColors, 0)
        if (stepsIndicatorColorsResId != 0) {
            // init the list of colors with the same size as the number of steps
            stepsIndicatorPaintList = ArrayList(stepCount)
            if (showStepTextNumber) {
                stepsTextNumberPaintList = ArrayList(stepCount)
            }
            for (i in 0 until stepCount) {
                val indicatorPaint = Paint(indicatorPaint)
                val textNumberPaint = if (showStepTextNumber) Paint(stepTextNumberPaint) else null
                if (isInEditMode) {
                    // Fallback for edit mode - to show something in the preview
                    indicatorPaint.color = randomColor // random color
                    if (null != textNumberPaint) {
                        textNumberPaint.color = indicatorPaint.color
                    }
                } else {
                    // Get the array of attributes for the colors
                    val colorResValues = context.resources.obtainTypedArray(stepsIndicatorColorsResId)
                    require(stepCount <= colorResValues.length()) {
                        "Invalid number of colors for the indicators. Please provide a list " +
                                "of colors with as many items as the number of steps required!"
                    }
                    indicatorPaint.color = colorResValues.getColor(i, 0) // specific color
                    if (null != textNumberPaint) {
                        textNumberPaint.color = indicatorPaint.color
                    }
                    // No need for the array anymore, recycle it
                    colorResValues.recycle()
                }
                stepsIndicatorPaintList?.add(indicatorPaint)
                if (showStepTextNumber && null != textNumberPaint) {
                    stepsTextNumberPaintList!!.add(textNumberPaint)
                }
            }
        }
        linePaint = Paint()
        linePaint!!.strokeWidth = a.getDimension(R.styleable.TStepperIndicator_stpi_lineStrokeWidth, defaultLineStrokeWidth)
        linePaint!!.strokeCap = Paint.Cap.ROUND
        linePaint!!.style = Paint.Style.STROKE
        linePaint!!.color = a.getColor(R.styleable.TStepperIndicator_stpi_lineColor, defaultLineColor)
        linePaint!!.isAntiAlias = true
        lineDonePaint = Paint(linePaint)
        lineDonePaint!!.color = a.getColor(R.styleable.TStepperIndicator_stpi_lineDoneColor, defaultPrimaryColor)
        lineDoneAnimatedPaint = Paint(lineDonePaint)

        // Check if we should use the bottom indicator instead of the bullet one
        useBottomIndicator = a.getBoolean(R.styleable.TStepperIndicator_stpi_useBottomIndicator, false)
        if (useBottomIndicator) {
            // Get the default height(stroke width) for the bottom indicator
            val defaultHeight = resources.getDimension(R.dimen.stpi_default_bottom_indicator_height)
            bottomIndicatorHeight = a
                    .getDimension(R.styleable.TStepperIndicator_stpi_bottomIndicatorHeight, defaultHeight)
            if (bottomIndicatorHeight <= 0) {
                d(TAG, "init: Invalid indicator height, disabling bottom indicator feature! Please provide " +
                        "a value greater than 0.")
                useBottomIndicator = false
            }

            // Get the default width for the bottom indicator
            val defaultWidth = resources.getDimension(R.dimen.stpi_default_bottom_indicator_width)
            bottomIndicatorWidth = a.getDimension(R.styleable.TStepperIndicator_stpi_bottomIndicatorWidth, defaultWidth)

            // Get the default top margin for the bottom indicator
            val defaultTopMargin = resources.getDimension(R.dimen.stpi_default_bottom_indicator_margin_top)
            bottomIndicatorMarginTop = a
                    .getDimension(R.styleable.TStepperIndicator_stpi_bottomIndicatorMarginTop, defaultTopMargin)
            useBottomIndicatorWithStepColors = a
                    .getBoolean(R.styleable.TStepperIndicator_stpi_useBottomIndicatorWithStepColors, false)
        }
        circleRadius = a.getDimension(R.styleable.TStepperIndicator_stpi_circleRadius, defaultCircleRadius)
        checkRadius = circleRadius + circlePaint!!.strokeWidth / 2f
        indicatorRadius = a.getDimension(R.styleable.TStepperIndicator_stpi_indicatorRadius, defaultIndicatorRadius)
        animIndicatorRadius = indicatorRadius
        animCheckRadius = checkRadius
        lineMargin = a.getDimension(R.styleable.TStepperIndicator_stpi_lineMargin, defaultLineMargin)
        animDuration = a.getInteger(R.styleable.TStepperIndicator_stpi_animDuration, DEFAULT_ANIMATION_DURATION)
        showDoneIcon = a.getBoolean(R.styleable.TStepperIndicator_stpi_showDoneIcon, true)
        doneIcon = a.getDrawable(R.styleable.TStepperIndicator_stpi_doneIconDrawable)

        // Labels Configuration
        labelPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        labelPaint!!.textAlign = Paint.Align.CENTER
        val defaultLabelSize = resources.getDimension(R.dimen.stpi_default_label_size)
        labelSize = a.getDimension(R.styleable.TStepperIndicator_stpi_labelSize, defaultLabelSize)
        labelPaint!!.textSize = labelSize
        val defaultLabelMarginTop = resources.getDimension(R.dimen.stpi_default_label_margin_top)
        labelMarginTop = a.getDimension(R.styleable.TStepperIndicator_stpi_labelMarginTop, defaultLabelMarginTop)
        showLabels(a.getBoolean(R.styleable.TStepperIndicator_stpi_showLabels, false))
        setLabels(a.getTextArray(R.styleable.TStepperIndicator_stpi_labels))
        if (a.hasValue(R.styleable.TStepperIndicator_stpi_labelColor)) {
            setLabelColor(a.getColor(R.styleable.TStepperIndicator_stpi_labelColor, 0))
        } else {
            setLabelColor(getTextColorSecondary(getContext()))
        }
        if (isInEditMode && showLabels && labels == null) {
            labels = arrayOf("First", "Second", "Third", "Fourth", "Fifth")
        }
        if (!a.hasValue(R.styleable.TStepperIndicator_stpi_stepCount) && labels != null) {
            setStepCount(labels!!.size)
        }
        a.recycle()
        if (showDoneIcon && doneIcon == null) {
            doneIcon = ContextCompat.getDrawable(context, R.drawable.ic_done_white_18dp)
        }
        if (doneIcon != null) {
            val size = getContext().resources.getDimensionPixelSize(R.dimen.stpi_done_icon_size)
            doneIcon!!.setBounds(0, 0, size, size)
        }

        // Display at least 1 cleared step for preview in XML editor
        if (isInEditMode) {
            currentStep = Math.max(Math.ceil(stepCount / 2f.toDouble()).toInt(), 1)
        }

        // Initialize the gesture detector, setup with our custom gesture listener
        gestureDetector = GestureDetector(getContext(), gestureListener)
    }

    /**
     * Get an random color [Paint] object.
     *
     * @return [Paint] object with the same attributes as [.circlePaint] and with an random color.
     * @see .circlePaint
     *
     * @see .getRandomColor
     */
    private val randomPaint: Paint
        private get() {
            val paint = Paint(indicatorPaint)
            paint.color = randomColor
            return paint
        }

    /**
     * Get an random color value.
     *
     * @return The color value as AARRGGBB
     */
    private val randomColor: Int
        private get() {
            val rnd = Random()
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Dispatch the touch events to our custom gesture detector.
        gestureDetector!!.onTouchEvent(event)
        return true // we handle the event in the gesture detector
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        compute() // for setting up the indicator based on the new position
    }

    /**
     * Make calculations for establishing the exact positions of each step component, for the line dividers, for
     * bottom indicators, etc.
     *
     *
     * Call this whenever there is an layout change for the widget.
     */
    private fun compute() {
        requireNotNull(circlePaint) {
            "circlePaint is invalid! Make sure you setup the field circlePaint " +
                    "before calling compute() method!"
        }
        indicators = FloatArray(stepCount)
        linePathList.clear()
        var startX = circleRadius * EXPAND_MARK + circlePaint!!.strokeWidth / 2f
        if (useBottomIndicator) {
            startX = bottomIndicatorWidth / 2f
        }
        if (showLabels) {
            // gridWidth is the width of the grid assigned for the step indicator
            val gridWidth = measuredWidth / stepCount
            startX = gridWidth / 2f
        }

        // Compute position of indicators and line length
        val divider = (measuredWidth - startX * 2f) / (stepCount - 1)
        lineLength = divider - (circleRadius * 2f + circlePaint!!.strokeWidth) - lineMargin * 2

        // Compute position of circles and lines once
        for (i in indicators!!.indices) {
            indicators!![i] = startX + divider * i
        }
        for (i in 0 until indicators!!.size - 1) {
            val position = (indicators!![i] + indicators!![i + 1]) / 2 - lineLength / 2
            val linePath = Path()
            val lineY = stepCenterY
            linePath.moveTo(position, lineY)
            linePath.lineTo(position + lineLength, lineY)
            linePathList.add(linePath)
        }
        computeStepsClickAreas() // update the position of the steps click area also
    }

    /**
     * Calculate the area for each step. This ensure the correct step is detected when an click event is detected.
     *
     *
     *
     *
     * Whenever [.compute] method is called, make sure to call this method also so that the steps click
     * area is updated.
     */
    fun computeStepsClickAreas() {
        require(stepCount != STEP_INVALID) {
            "stepCount wasn't setup yet. Make sure you call setStepCount() " +
                    "before computing the steps click area!"
        }
        requireNotNull(indicators) {
            "indicators wasn't setup yet. Make sure the indicators are " +
                    "initialized and setup correctly before trying to compute the click " +
                    "area for each step!"
        }

        // Initialize the list for the steps click area
        stepsClickAreas = ArrayList(stepCount)

        // Compute the clicked area for each step
        for (indicator in indicators!!) {
            // Get the indicator position
            // Calculate the bounds for the step
            val left = indicator - circleRadius * 2
            val right = indicator + circleRadius * 2
            val top = stepCenterY - circleRadius * 2
            val bottom = stepCenterY + circleRadius + getBottomIndicatorHeight()

            // Store the click area for the step
            val area = RectF(left, top, right, bottom)
            stepsClickAreas?.add(area)
        }
    }

    /**
     * Get the height of the bottom indicator.
     *
     *
     * The height will include the height necessary for correctly drawing the bottom indicator plus the margin
     * set in XML (or the default one).
     *
     *
     *
     *
     * If the widget isn't set to display the bottom indicator this will method will always return `0`
     *
     * @return The height of the bottom indicator in pixels or `0`.
     */
    private fun getBottomIndicatorHeight(): Int {
        return if (useBottomIndicator) {
            (bottomIndicatorHeight + bottomIndicatorMarginTop).toInt()
        } else {
            0
        }
    }

    private fun getMaxLabelHeight(): Float {
        return if (showLabels) maxLabelHeight + labelMarginTop else 0f
    }

    private fun calculateMaxLabelHeight(measuredWidth: Int) {
        if (!showLabels) return

        // gridWidth is the width of the grid assigned for the step indicator
        val twoDp = context.resources.getDimensionPixelSize(R.dimen.stpi_two_dp)
        val gridWidth = measuredWidth / stepCount - twoDp
        if (gridWidth <= 0) return

        // Compute StaticLayout for the labels
        labelLayouts = arrayOfNulls(labels!!.size)
        maxLabelHeight = 0f
        val labelSingleLineHeight = labelPaint!!.descent() - labelPaint!!.ascent()
        for (i in labels!!.indices) {
            if (labels!![i] == null) continue
            labelLayouts!![i] = StaticLayout(labels!![i], labelPaint, gridWidth,
                    Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
            maxLabelHeight = Math.max(maxLabelHeight, labelLayouts!![i]!!.lineCount * labelSingleLineHeight)
        }
    }

    private val stepCenterY: Float
        private get() = (measuredHeight - getBottomIndicatorHeight() - getMaxLabelHeight()) / 2f

    override fun onDraw(canvas: Canvas) {
        val centerY = stepCenterY

        // Currently Drawing animation from step n-1 to n, or back from n+1 to n
        val inAnimation = animatorSet != null && animatorSet!!.isRunning
        val inLineAnimation = lineAnimator != null && lineAnimator!!.isRunning
        val inIndicatorAnimation = indicatorAnimator != null && indicatorAnimator!!.isRunning
        val inCheckAnimation = checkAnimator != null && checkAnimator!!.isRunning
        val drawToNext = previousStep == currentStep - 1
        val drawFromNext = previousStep == currentStep + 1
        for (i in indicators!!.indices) {
            val indicator = indicators!![i]

            // We draw the "done" check if previous step, or if we are going back (if going back, animated value will reduce radius to 0)
            val drawCheck = i < currentStep || drawFromNext && i == currentStep

            // Draw back circle
            canvas.drawCircle(indicator, centerY, circleRadius, getStepCirclePaint(i)!!)

            // Draw the step number inside the back circle if the flag for this is set to true
            if (showStepTextNumber) {
                val stepLabel = (i + 1).toString()
                stepAreaRect[(indicator - circleRadius).toInt(), (centerY - circleRadius).toInt(), (indicator + circleRadius).toInt()] = (centerY + circleRadius).toInt()
                stepAreaRectF.set(stepAreaRect)
                val stepTextNumberPaint = getStepTextNumberPaint(i)

                // measure text width
                stepAreaRectF.right = stepTextNumberPaint!!.measureText(stepLabel, 0, stepLabel.length)
                // measure text height
                stepAreaRectF.bottom = stepTextNumberPaint.descent() - stepTextNumberPaint.ascent()
                stepAreaRectF.left += (stepAreaRect.width() - stepAreaRectF.right) / 2.0f
                stepAreaRectF.top += (stepAreaRect.height() - stepAreaRectF.bottom) / 2.0f
                canvas.drawText(stepLabel, stepAreaRectF.left, stepAreaRectF.top - stepTextNumberPaint.ascent(),
                        stepTextNumberPaint)
            }
            if (showLabels && labelLayouts != null && i < labelLayouts!!.size && labelLayouts!![i] != null) {
                drawLayout(labelLayouts!![i],
                        indicator, height - getBottomIndicatorHeight() - maxLabelHeight,
                        canvas, labelPaint)
            }
            if (useBottomIndicator) {
                // Show the current step indicator as bottom line
                if (i == currentStep) {
                    // Draw custom indicator for current step only
                    canvas.drawRect(indicator - bottomIndicatorWidth / 2, height - bottomIndicatorHeight,
                            indicator + bottomIndicatorWidth / 2, height.toFloat(),
                            (if (useBottomIndicatorWithStepColors) getStepIndicatorPaint(i) else indicatorPaint)!!)
                }
            } else {
                // Show the current step indicator as bullet
                // If current step, or coming back from next step and still animating
                if (i == currentStep && !drawFromNext || i == previousStep && drawFromNext && inAnimation) {
                    // Draw animated indicator
                    canvas.drawCircle(indicator, centerY, animIndicatorRadius, getStepIndicatorPaint(i)!!)
                }
            }

            // Draw check mark
            if (drawCheck) {
                var radius = checkRadius
                // Use animated radius value?
                if (i == previousStep && drawToNext || i == currentStep && drawFromNext) radius = animCheckRadius
                canvas.drawCircle(indicator, centerY, radius, getStepIndicatorPaint(i)!!)

                // Draw check bitmap
                if (!isInEditMode && showDoneIcon) {
                    if (i != previousStep && i != currentStep ||
                            !inCheckAnimation && !(i == currentStep && !inAnimation)) {
                        canvas.save()
                        canvas.translate(indicator - doneIcon!!.intrinsicWidth / 2,
                                centerY - doneIcon!!.intrinsicHeight / 2)
                        doneIcon!!.draw(canvas)
                        canvas.restore()
                    }
                }
            }

            // Draw lines
            if (i < linePathList.size) {
                if (i >= currentStep) {
                    canvas.drawPath(linePathList[i], linePaint!!)
                    if (i == currentStep && drawFromNext && (inLineAnimation || inIndicatorAnimation)) {
                        // Coming back from n+1
                        canvas.drawPath(linePathList[i], lineDoneAnimatedPaint!!)
                    }
                } else {
                    if (i == currentStep - 1 && drawToNext && inLineAnimation) {
                        // Going to n+1
                        canvas.drawPath(linePathList[i], linePaint!!)
                        canvas.drawPath(linePathList[i], lineDoneAnimatedPaint!!)
                    } else {
                        canvas.drawPath(linePathList[i], lineDonePaint!!)
                    }
                }
            }
        }
    }

    /**
     * Get the [Paint] object which should be used for displaying the current step indicator.
     *
     * @param stepPosition The step position for which to retrieve the [Paint] object
     * @return The [Paint] object for the specified step position
     */
    private fun getStepIndicatorPaint(stepPosition: Int): Paint? {
        return getPaint(stepPosition, stepsIndicatorPaintList, indicatorPaint)
    }

    /**
     * Get the [Paint] object which should be used for drawing the text number the current step.
     *
     * @param stepPosition The step position for which to retrieve the [Paint] object
     * @return The [Paint] object for the specified step position
     */
    private fun getStepTextNumberPaint(stepPosition: Int): Paint? {
        return getPaint(stepPosition, stepsTextNumberPaintList, stepTextNumberPaint)
    }

    /**
     * Get the [Paint] object which should be used for drawing the circle for the step.
     * ic_done_white_18dp
     *
     * @param stepPosition The step position for which to retrieve the [Paint] object
     * @return The [Paint] object for the specified step position
     */
    private fun getStepCirclePaint(stepPosition: Int): Paint? {
        return getPaint(stepPosition, stepsCirclePaintList, circlePaint)
    }

    /**
     * Get the [Paint] object based on the step position and the source list of [Paint] objects.
     *
     *
     * If none found, will try to use the provided default. If not valid also, an random [Paint] object
     * will be returned instead.
     *
     * @param stepPosition The step position for which the [Paint] object is needed
     * @param sourceList   The source list of [Paint] object.
     * @param defaultPaint The default [Paint] object which will be returned if the source list does not
     * contain the specified step.
     * @return [Paint] object for the specified step position.
     */
    private fun getPaint(stepPosition: Int, sourceList: List<Paint>?, defaultPaint: Paint?): Paint? {
        isStepValid(stepPosition) // it will throw an error if not valid
        var paint: Paint? = null
        if (null != sourceList && !sourceList.isEmpty()) {
            try {
                paint = sourceList[stepPosition]
            } catch (e: IndexOutOfBoundsException) {
                // We use an random color as this usually should not happen, maybe in edit mode
                d(TAG, "getPaint: could not find the specific step paint to use! Try to use default instead!")
            }
        }
        if (null == paint && null != defaultPaint) {
            // Try to use the default
            paint = defaultPaint
        }
        if (null == paint) {
            d(TAG, "getPaint: could not use default paint for the specific step! Using random Paint instead!")
            // If we reached this point, not even the default is setup, rely on some random color
            paint = randomPaint
        }
        return paint
    }

    /**
     * Check if the step position provided is an valid and supported step.
     *
     *
     * This method ensured the widget doesn't try to use invalid steps. It will throw an exception whenever an
     * invalid step is detected. Catch the exception if it is expected or it doesn't affect the flow.
     *
     * @param stepPos The step position to verify
     * @return `true` if the step is valid, otherwise it will throw an exception.
     */
    private fun isStepValid(stepPos: Int): Boolean {
        require(!(stepPos < 0 || stepPos > stepCount - 1)) {
            "Invalid step position. " + stepPos + " is not a valid position! it " +
                    "should be between 0 and stepCount(" + stepCount + ")"
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val width = if (widthMode == MeasureSpec.EXACTLY) widthSize else suggestedMinimumWidth
        calculateMaxLabelHeight(width)

        // Compute the necessary height for the widget
        val desiredHeight = Math.ceil(
                (circleRadius * EXPAND_MARK * 2 +
                        circlePaint!!.strokeWidth +
                        getBottomIndicatorHeight() +
                        getMaxLabelHeight())
                        .toDouble()).toInt()
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val height = if (heightMode == MeasureSpec.EXACTLY) heightSize else desiredHeight
        setMeasuredDimension(width, height)
    }

    fun getStepCount(): Int {
        return stepCount
    }

    fun setStepCount(stepCount: Int) {
        require(stepCount >= 2) { "stepCount must be >= 2" }
        this.stepCount = stepCount
        currentStep = 0
        compute()
        invalidate()
    }

    fun getCurrentStep(): Int {
        return currentStep
    }

    /**
     * Sets the current step
     *
     * @param currentStep a value between 0 (inclusive) and stepCount (inclusive)
     */
    @UiThread
    fun setCurrentStep(currentStep: Int) {
        require(!(currentStep < 0 || currentStep > stepCount)) { "Invalid step value $currentStep" }
        previousStep = this.currentStep
        this.currentStep = currentStep

        // Cancel any running animations
        if (animatorSet != null) {
            animatorSet!!.cancel()
        }
        animatorSet = null
        lineAnimator = null
        indicatorAnimator = null

        // TODO: 05/08/16 handle cases where steps are skipped - need to animate all of them
        if (currentStep == previousStep + 1) {
            // Going to next step
            animatorSet = AnimatorSet()

            // First, draw line to new
            lineAnimator = ObjectAnimator.ofFloat(this@TStepperIndicator, "animProgress", 1.0f, 0.0f)

            // Same time, pop check mark
            checkAnimator = ObjectAnimator.ofFloat(this@TStepperIndicator, "animCheckRadius", indicatorRadius,
                    checkRadius * EXPAND_MARK, checkRadius)

            // Finally, pop current step indicator
            animIndicatorRadius = 0f
            indicatorAnimator = ObjectAnimator.ofFloat(this@TStepperIndicator, "animIndicatorRadius", 0f,
                    indicatorRadius * 1.4f, indicatorRadius)
            animatorSet!!.play(lineAnimator).with(checkAnimator).before(indicatorAnimator)
        } else if (currentStep == previousStep - 1) {
            // Going back to previous step
            animatorSet = AnimatorSet()

            // First, pop out current step indicator
            indicatorAnimator = ObjectAnimator
                    .ofFloat(this@TStepperIndicator, "animIndicatorRadius", indicatorRadius, 0f)

            // Then delete line
            animProgress = 1.0f
            lineDoneAnimatedPaint!!.pathEffect = null
            lineAnimator = ObjectAnimator.ofFloat(this@TStepperIndicator, "animProgress", 0.0f, 1.0f)

            // Finally, pop out check mark to display step indicator
            animCheckRadius = checkRadius
            checkAnimator = ObjectAnimator
                    .ofFloat(this@TStepperIndicator, "animCheckRadius", checkRadius, indicatorRadius)
            animatorSet!!.playSequentially(indicatorAnimator, lineAnimator, checkAnimator)
        }
        if (animatorSet != null) {
            // Max 500 ms for the animation
            lineAnimator!!.duration = Math.min(500, animDuration).toLong()
            lineAnimator!!.interpolator = DecelerateInterpolator()
            // Other animations will run 2 times faster that line animation
            indicatorAnimator!!.duration = lineAnimator!!.duration / 2
            checkAnimator!!.duration = lineAnimator!!.duration / 2
            animatorSet!!.start()
        }
        invalidate()
    }

    /**
     * Setter method for the animation progress.
     *
     * <font color="red">DO NOT CALL, DELETE OR RENAME</font>: Will be used by animation.
     */
    fun setAnimProgress(animProgress: Float) {
        this.animProgress = animProgress
        lineDoneAnimatedPaint!!.pathEffect = createPathEffect(lineLength, animProgress, 0.0f)
        invalidate()
    }

    /**
     * Setter method for the indicator radius animation.
     *
     * <font color="red">DO NOT CALL, DELETE OR RENAME</font>: Will be used by animation.
     */
    fun setAnimIndicatorRadius(animIndicatorRadius: Float) {
        this.animIndicatorRadius = animIndicatorRadius
        invalidate()
    }

    /**
     * Setter method for the checkmark radius animation.
     *
     * <font color="red">DO NOT CALL, DELETE OR RENAME</font>: Will be used by animation.
     */
    fun setAnimCheckRadius(animCheckRadius: Float) {
        this.animCheckRadius = animCheckRadius
        invalidate()
    }

    /**
     * Set the [ViewPager] associated with this widget indicator.
     *
     * @param pager [ViewPager] to attach
     */
    fun setViewPager(pager: ViewPager) {
        checkNotNull(pager.adapter) { "ViewPager does not have adapter instance." }
        setViewPager(pager, pager.adapter!!.count)
    }

    /**
     * Set the [ViewPager] associated with this widget indicator.
     *
     * @param pager        [ViewPager] to attach
     * @param keepLastPage `true` if the widget should not create an indicator for the last page, to use it as
     * the
     * final page
     */
    fun setViewPager(pager: ViewPager, keepLastPage: Boolean) {
        checkNotNull(pager.adapter) { "ViewPager does not have adapter instance." }
        setViewPager(pager, pager.adapter!!.count - if (keepLastPage) 1 else 0)
    }

    /**
     * Set the [ViewPager] associated with this widget indicator.
     *
     * @param pager     [ViewPager] to attach
     * @param stepCount The real page count to display (use this if you are using looped viewpager to indicate the real
     * number
     * of pages)
     */
    fun setViewPager(pager: ViewPager, stepCount: Int) {
        if (this.pager === pager) {
            return
        }
        if (this.pager != null) {
            pager.removeOnPageChangeListener(this)
        }
        checkNotNull(pager.adapter) { "ViewPager does not have adapter instance." }
        this.pager = pager
        this.stepCount = stepCount
        currentStep = 0
        pager.addOnPageChangeListener(this)
        if (showLabels && labels == null) {
            setLabelsUsingPageTitles()
        }
        requestLayout()
        invalidate()
    }

    private fun setLabelsUsingPageTitles() {
        val pagerAdapter = pager!!.adapter
        val pagerCount = pagerAdapter!!.count
        labels = arrayOfNulls(pagerCount)
        for (i in 0 until pagerCount) {
            labels!![i] = pagerAdapter.getPageTitle(i)
        }
    }

    /**
     * Pass a labels array of Charsequence that is greater than or equal to the `stepCount`.
     * Never pass `null` to this manually. Call `showLabels(false)` to hide labels.
     *
     * @param labelsArray Non-null array of CharSequence
     */
    fun setLabels(labelsArray: Array<CharSequence?>?) {
        if (labelsArray == null) {
            labels = null
            return
        }
        require(stepCount <= labelsArray.size) {
            "Invalid number of labels for the indicators. Please provide a list " +
                    "of labels with at least as many items as the number of steps required!"
        }
        labels = labelsArray
        showLabels(true)
    }

    fun setLabelColor(color: Int) {
        labelPaint!!.color = color
        requestLayout()
        invalidate()
    }

    /**
     * Shows the labels if true is passed. Else hides them.
     *
     * @param show Boolean to show or hide the labels
     */
    fun showLabels(show: Boolean) {
        showLabels = show
        requestLayout()
        invalidate()
    }

    /**
     * Add the [OnStepClickListener] to the list of listeners which will receive events when an step is clicked.
     *
     * @param listener The [OnStepClickListener] which will be added
     */
    fun addOnStepClickListener(listener: OnStepClickListener) {
        onStepClickListeners!!.add(listener)
    }

    /**
     * Remove the specified [OnStepClickListener] from the list of listeners which will receive events when an
     * step is clicked.
     *
     * @param listener The [OnStepClickListener] which will be removed
     */
    fun removeOnStepClickListener(listener: OnStepClickListener) {
        onStepClickListeners!!.remove(listener)
    }

    /**
     * Remove all [OnStepClickListener] listeners from the StepperIndicator widget.
     * No more events will be propagated.
     */
    fun clearOnStepClickListeners() {
        onStepClickListeners!!.clear()
    }

    /**
     * Check if the widget has any valid [OnStepClickListener] listener set for receiving events from the steps.
     *
     * @return `true` if there are listeners registered, `false` otherwise.
     */
    val isOnStepClickListenerAvailable: Boolean
        get() = null != onStepClickListeners && !onStepClickListeners.isEmpty()

    fun setDoneIcon(doneIcon: Drawable?) {
        this.doneIcon = doneIcon
        if (doneIcon != null) {
            showDoneIcon = true
            val size = context.resources.getDimensionPixelSize(R.dimen.stpi_done_icon_size)
            doneIcon.setBounds(0, 0, size, size)
        }
        invalidate()
    }

    fun setShowDoneIcon(showDoneIcon: Boolean) {
        this.showDoneIcon = showDoneIcon
        invalidate()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        /* no-op */
    }

    override fun onPageSelected(position: Int) {
        setCurrentStep(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
        /* no-op */
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        // Try to restore the current step
        currentStep = savedState.mCurrentStep
        requestLayout()
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        // Store current stop so that it can be resumed when restored
        savedState.mCurrentStep = currentStep
        return savedState
    }

    /**
     * Contract used by the StepperIndicator widget to notify any listener of steps interaction events.
     */
    interface OnStepClickListener {
        /**
         * Step was clicked
         *
         * @param step The step position which was clicked. (starts from 0, as the ViewPager bound to the widget)
         */
        fun onStepClicked(step: Int)
    }

    /**
     * Saved state in which information about the state of the widget is stored.
     *
     *
     * Use this whenever you want to store or restore some information about the state of the widget.
     */
    private class SavedState : BaseSavedState {
        var mCurrentStep = 0

        constructor(superState: Parcelable?) : super(superState)
        private constructor(`in`: Parcel) : super(`in`) {
            mCurrentStep = `in`.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(mCurrentStep)
        }

        companion object {
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState? {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        private const val TAG = "StepperIndicator"

        /**
         * Duration of the line drawing animation (ms)
         */
        private const val DEFAULT_ANIMATION_DURATION = 200

        /**
         * Max multiplier of the radius when a step is being animated to the "done" state before going to it's normal radius
         */
        private const val EXPAND_MARK = 1.3f
        private const val STEP_INVALID = -1
        fun getPrimaryColor(context: Context): Int {
            var color = context.resources.getIdentifier("colorPrimary", "attr", context.packageName)
            if (color != 0) {
                // If using support library v7 primaryColor
                val t = TypedValue()
                context.theme.resolveAttribute(color, t, true)
                color = t.data
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // If using native primaryColor (SDK >21)
                val t = context.obtainStyledAttributes(intArrayOf(android.R.attr.colorPrimary))
                color = t.getColor(0, ContextCompat.getColor(context, R.color.stpi_default_primary_color))
                t.recycle()
            } else {
                val t = context.obtainStyledAttributes(intArrayOf(R.attr.colorPrimary))
                color = t.getColor(0, ContextCompat.getColor(context, R.color.stpi_default_primary_color))
                t.recycle()
            }
            return color
        }

        fun getTextColorSecondary(context: Context): Int {
            val t = context.obtainStyledAttributes(intArrayOf(android.R.attr.textColorSecondary))
            val color = t.getColor(0, ContextCompat.getColor(context, R.color.stpi_default_text_color))
            t.recycle()
            return color
        }

        private fun createPathEffect(pathLength: Float, phase: Float, offset: Float): PathEffect {
            // Create a PathEffect to set on a Paint to only draw some part of the line
            return DashPathEffect(floatArrayOf(pathLength, pathLength), Math.max(phase * pathLength, offset))
        }

        /**
         * x and y anchored to top-middle point of StaticLayout
         */
        fun drawLayout(layout: Layout?, x: Float, y: Float,
                       canvas: Canvas, paint: TextPaint?) {
            canvas.save()
            canvas.translate(x, y)
            layout!!.draw(canvas)
            canvas.restore()
        }
    }
}