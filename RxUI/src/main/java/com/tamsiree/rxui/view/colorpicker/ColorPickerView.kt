package com.tamsiree.rxui.view.colorpicker

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.tamsiree.rxkit.RxImageTool.adjustAlpha
import com.tamsiree.rxkit.RxImageTool.alphaValueAsInt
import com.tamsiree.rxkit.RxImageTool.colorAtLightness
import com.tamsiree.rxkit.RxImageTool.getAlphaPercent
import com.tamsiree.rxkit.RxImageTool.getHexString
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.colorpicker.builder.ColorWheelRendererBuilder
import com.tamsiree.rxui.view.colorpicker.builder.PaintBuilder
import com.tamsiree.rxui.view.colorpicker.renderer.ColorWheelRenderer
import com.tamsiree.rxui.view.colorpicker.slider.AlphaSlider
import com.tamsiree.rxui.view.colorpicker.slider.LightnessSlider
import java.util.*
import kotlin.math.max

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class ColorPickerView : View {
    private var colorWheel: Bitmap? = null
    private var colorWheelCanvas: Canvas? = null
    private var density = 10
    private var lightness = 1f

    private var alphaS = 1f
    private val backgroundColor = 0x00000000
    var allColors: Array<Int?>? = arrayOf(null, null, null, null, null)
        private set
    private var colorSelection = 0
    private var initialColor: Int? = null
    private var pickerTextColor: Int? = null
    private val colorWheelFill = PaintBuilder.newPaint().color(0).build()
    private val selectorStroke1 = PaintBuilder.newPaint().color(-0x1).build()
    private val selectorStroke2 = PaintBuilder.newPaint().color(-0x1000000).build()
    private val alphaPatternPaint = PaintBuilder.newPaint().build()
    private var currentColorCircle: ColorCircle? = null
    private val colorChangedListeners: ArrayList<OnColorChangedListener>? = ArrayList()
    private val listeners: ArrayList<OnColorSelectedListener>? = ArrayList()
    private var lightnessSlider: LightnessSlider? = null
    private var alphaSlider: AlphaSlider? = null
    private var colorEdit: EditText? = null
    private var colorPreview: LinearLayout? = null
    private var renderer: ColorWheelRenderer? = null
    private val colorTextChange: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            try {
                val color = Color.parseColor(s.toString())

                // set the color without changing the edit text preventing stack overflow
                setColor(color, false)
            } catch (e: Exception) {
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
    private var alphaSliderViewId = 0
    private var lightnessSliderViewId = 0

    constructor(context: Context) : super(context) {
        initWith(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initWith(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initWith(context, attrs)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initWith(context, attrs)
    }

    private fun initWith(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference)
        density = typedArray.getInt(R.styleable.ColorPickerPreference_density, 10)
        initialColor = typedArray.getInt(R.styleable.ColorPickerPreference_initialColor, -0x1)
        pickerTextColor = typedArray.getInt(R.styleable.ColorPickerPreference_pickerColorEditTextColor, -0x1)
        val wheelType = WHEEL_TYPE.indexOf(typedArray.getInt(R.styleable.ColorPickerPreference_wheelType, 0))
        val renderer = ColorWheelRendererBuilder.getRenderer(wheelType)
        alphaSliderViewId = typedArray.getResourceId(R.styleable.ColorPickerPreference_alphaSliderView, 0)
        lightnessSliderViewId = typedArray.getResourceId(R.styleable.ColorPickerPreference_lightnessSliderView, 0)
        setRenderer(renderer)
        setDensity(density)
        setInitialColor(initialColor!!, true)
        typedArray.recycle()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        updateColorWheel()
        currentColorCircle = findNearestByColor(initialColor!!)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (alphaSliderViewId != 0) {
            setAlphaSlider(rootView.findViewById(alphaSliderViewId))
        }
        if (lightnessSliderViewId != 0) {
            setLightnessSlider(rootView.findViewById(lightnessSliderViewId))
        }
        updateColorWheel()
        currentColorCircle = findNearestByColor(initialColor!!)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateColorWheel()
    }

    private fun updateColorWheel() {
        var width = measuredWidth
        val height = measuredHeight
        if (height < width) {
            width = height
        }
        if (width <= 0) {
            return
        }
        if (colorWheel == null) {
            val temp = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
            colorWheel = temp
            colorWheelCanvas = Canvas(temp)
            alphaPatternPaint.shader = PaintBuilder.createAlphaPatternShader(8)
        }
        drawColorWheel()
        invalidate()
    }

    private fun drawColorWheel() {
        colorWheelCanvas!!.drawColor(0, PorterDuff.Mode.CLEAR)
        if (renderer == null) {
            return
        }
        val half = colorWheelCanvas!!.width / 2f
        val strokeWidth = STROKE_RATIO * (1f + ColorWheelRenderer.GAP_PERCENTAGE)
        val maxRadius = half - strokeWidth - half / density
        val cSize = maxRadius / (density - 1) / 2
        val colorWheelRenderOption = renderer!!.renderOption
        colorWheelRenderOption.density = density
        colorWheelRenderOption.maxRadius = maxRadius
        colorWheelRenderOption.cSize = cSize
        colorWheelRenderOption.strokeWidth = strokeWidth
        colorWheelRenderOption.alpha = alphaS
        colorWheelRenderOption.lightness = lightness
        colorWheelRenderOption.targetCanvas = colorWheelCanvas
        renderer!!.initWith(colorWheelRenderOption)
        renderer!!.draw()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = 0
        when (widthMode) {
            MeasureSpec.UNSPECIFIED -> width = widthMeasureSpec
            MeasureSpec.AT_MOST -> width = MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.EXACTLY -> width = MeasureSpec.getSize(widthMeasureSpec)
            else -> {
            }
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = 0
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            height = widthMeasureSpec
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = MeasureSpec.getSize(heightMeasureSpec)
        } else if (widthMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec)
        }
        var squareDimen = width
        if (height < width) {
            squareDimen = height
        }
        setMeasuredDimension(squareDimen, squareDimen)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val lastSelectedColor = selectedColor
                currentColorCircle = findNearestByPosition(event.x, event.y)
                val selectedColor = selectedColor
                callOnColorChangedListeners(lastSelectedColor, selectedColor)
                initialColor = selectedColor
                setColorToSliders(selectedColor)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val selectedColor = selectedColor
                if (listeners != null) {
                    for (listener in listeners) {
                        try {
                            listener.onColorSelected(selectedColor)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                setColorToSliders(selectedColor)
                setColorText(selectedColor)
                setColorPreviewColor(selectedColor)
                invalidate()
            }
            else -> {
            }
        }
        return true
    }

    protected fun callOnColorChangedListeners(oldColor: Int, newColor: Int) {
        if (colorChangedListeners != null && oldColor != newColor) {
            for (listener in colorChangedListeners) {
                try {
                    listener.onColorChanged(newColor)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(backgroundColor)
        if (colorWheel != null) {
            canvas.drawBitmap(colorWheel!!, 0f, 0f, null)
        }
        if (currentColorCircle != null) {
            val maxRadius = canvas.width / 2f - STROKE_RATIO * (1f + ColorWheelRenderer.GAP_PERCENTAGE)
            val size = maxRadius / density / 2
            colorWheelFill.color = Color.HSVToColor(currentColorCircle!!.getHsvWithLightness(lightness))
            colorWheelFill.alpha = (alphaS * 0xff).toInt()
            canvas.drawCircle(currentColorCircle!!.x, currentColorCircle!!.y, size * STROKE_RATIO, selectorStroke1)
            canvas.drawCircle(currentColorCircle!!.x, currentColorCircle!!.y, size * (1 + (STROKE_RATIO - 1) / 2), selectorStroke2)
            canvas.drawCircle(currentColorCircle!!.x, currentColorCircle!!.y, size, alphaPatternPaint)
            canvas.drawCircle(currentColorCircle!!.x, currentColorCircle!!.y, size, colorWheelFill)
        }
    }

    private fun findNearestByPosition(x: Float, y: Float): ColorCircle? {
        var near: ColorCircle? = null
        var minDist = Double.MAX_VALUE
        for (colorCircle in renderer?.colorCircleList!!) {
            val dist = colorCircle.sqDist(x, y)
            if (minDist > dist) {
                minDist = dist
                near = colorCircle
            }
        }
        return near
    }

    private fun findNearestByColor(color: Int): ColorCircle? {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        var near: ColorCircle? = null
        var minDiff = Double.MAX_VALUE
        val x = hsv[1] * Math.cos(hsv[0] * Math.PI / 180)
        val y = hsv[1] * Math.sin(hsv[0] * Math.PI / 180)
        for (colorCircle in renderer?.colorCircleList!!) {
            val hsv1 = colorCircle.hsv
            val x1 = hsv1[1] * Math.cos(hsv1[0] * Math.PI / 180)
            val y1 = hsv1[1] * Math.sin(hsv1[0] * Math.PI / 180)
            val dx = x - x1
            val dy = y - y1
            val dist = dx * dx + dy * dy
            if (dist < minDiff) {
                minDiff = dist
                near = colorCircle
            }
        }
        return near
    }

    var selectedColor: Int
        get() {
            var color = 0
            if (currentColorCircle != null) {
                color = colorAtLightness(currentColorCircle!!.color, lightness)
            }
            return adjustAlpha(alphaS, color)
        }
        set(previewNumber) {
            if (allColors == null || allColors!!.size < previewNumber) {
                return
            }
            colorSelection = previewNumber
            setHighlightedColor(previewNumber)
            val color = allColors!![previewNumber] ?: return
            setColor(color, true)
        }

    fun setInitialColors(colors: Array<Int?>?, selectedColor: Int) {
        allColors = colors
        colorSelection = selectedColor
        var initialColor = allColors!![colorSelection]
        if (initialColor == null) {
            initialColor = -0x1
        }
        setInitialColor(initialColor, true)
    }

    fun setInitialColor(color: Int, updateText: Boolean) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        alphaS = getAlphaPercent(color)
        lightness = hsv[2]
        allColors!![colorSelection] = color
        initialColor = color
        setColorPreviewColor(color)
        setColorToSliders(color)
        if (colorEdit != null && updateText) {
            setColorText(color)
        }
        currentColorCircle = findNearestByColor(color)
    }

    fun setLightness(lightness: Float) {
        val lastSelectedColor = selectedColor
        this.lightness = lightness
        initialColor = Color.HSVToColor(alphaValueAsInt(alphaS), currentColorCircle?.getHsvWithLightness(lightness))
        if (colorEdit != null) {
            colorEdit!!.setText(getHexString(initialColor!!, alphaSlider != null))
        }
        if (alphaSlider != null && initialColor != null) {
            alphaSlider?.setColor(initialColor!!)
        }
        callOnColorChangedListeners(lastSelectedColor, initialColor!!)
        updateColorWheel()
        invalidate()
    }

    fun setColor(color: Int, updateText: Boolean) {
        setInitialColor(color, updateText)
        updateColorWheel()
        invalidate()
    }

    fun setAlphaValue(alpha: Float) {
        val lastSelectedColor = selectedColor
        this.alphaS = alpha
        initialColor = Color.HSVToColor(alphaValueAsInt(this.alphaS), currentColorCircle!!.getHsvWithLightness(lightness))
        if (colorEdit != null) {
            colorEdit?.setText(getHexString(initialColor!!, alphaSlider != null))
        }
        if (lightnessSlider != null && initialColor != null) {
            lightnessSlider?.setColor(initialColor!!)
        }
        callOnColorChangedListeners(lastSelectedColor, initialColor!!)
        updateColorWheel()
        invalidate()
    }

    fun addOnColorChangedListener(listener: OnColorChangedListener) {
        colorChangedListeners?.add(listener)
    }

    fun addOnColorSelectedListener(listener: OnColorSelectedListener) {
        listeners?.add(listener)
    }

    fun setLightnessSlider(lightnessSlider: LightnessSlider?) {
        this.lightnessSlider = lightnessSlider
        if (lightnessSlider != null) {
            this.lightnessSlider?.setColorPicker(this)
            this.lightnessSlider?.setColor(selectedColor)
        }
    }

    fun setAlphaSlider(alphaSlider: AlphaSlider?) {
        this.alphaSlider = alphaSlider
        if (alphaSlider != null) {
            this.alphaSlider?.setColorPicker(this)
            this.alphaSlider?.setColor(selectedColor)
        }
    }

    fun setColorEdit(colorEdit: EditText?) {
        this.colorEdit = colorEdit
        if (this.colorEdit != null) {
            this.colorEdit?.visibility = VISIBLE
            this.colorEdit?.addTextChangedListener(colorTextChange)
            setColorEditTextColor(pickerTextColor!!)
        }
    }

    fun setColorEditTextColor(argb: Int) {
        pickerTextColor = argb
        if (colorEdit != null) {
            colorEdit?.setTextColor(argb)
        }
    }

    fun setDensity(density: Int) {
        this.density = max(2, density)
        invalidate()
    }

    fun setRenderer(renderer: ColorWheelRenderer?) {
        this.renderer = renderer
        invalidate()
    }

    fun setColorPreview(colorPreview: LinearLayout?, selectedColor: Int?) {
        var selectedColor = selectedColor
        if (colorPreview == null) {
            return
        }
        this.colorPreview = colorPreview
        if (selectedColor == null) {
            selectedColor = 0
        }
        val children = colorPreview.childCount
        if (children == 0 || colorPreview.visibility != VISIBLE) {
            return
        }
        for (i in 0 until children) {
            val childView = colorPreview.getChildAt(i) as? LinearLayout ?: continue
            val childLayout = childView
            if (i == selectedColor) {
                childLayout.setBackgroundColor(Color.WHITE)
            }
            val childImage = childLayout.findViewById<ImageView>(R.id.image_preview)
            childImage.isClickable = true
            childImage.tag = i
            childImage.setOnClickListener(OnClickListener { v ->
                if (v == null) {
                    return@OnClickListener
                }
                val tag = v.tag
                if (tag == null || tag !is Int) {
                    return@OnClickListener
                }
                selectedColor = tag
            })
        }
    }

    private fun setHighlightedColor(previewNumber: Int) {
        val children = colorPreview!!.childCount
        if (children == 0 || colorPreview!!.visibility != VISIBLE) {
            return
        }
        for (i in 0 until children) {
            val childView = colorPreview!!.getChildAt(i) as? LinearLayout ?: continue
            val childLayout = childView
            if (i == previewNumber) {
                childLayout.setBackgroundColor(Color.WHITE)
            } else {
                childLayout.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    private fun setColorPreviewColor(newColor: Int) {
        if (colorPreview == null || allColors == null || colorSelection > allColors!!.size || allColors!![colorSelection] == null) {
            return
        }
        val children = colorPreview!!.childCount
        if (children == 0 || colorPreview!!.visibility != VISIBLE) {
            return
        }
        val childView = colorPreview!!.getChildAt(colorSelection) as? LinearLayout ?: return
        val childImage = childView.findViewById<ImageView>(R.id.image_preview)
        childImage.setImageDrawable(CircleColorDrawable(newColor))
    }

    private fun setColorText(argb: Int) {
        if (colorEdit == null) {
            return
        }
        colorEdit!!.setText(getHexString(argb, alphaSlider != null))
    }

    private fun setColorToSliders(selectedColor: Int) {
        if (lightnessSlider != null) {
            lightnessSlider!!.setColor(selectedColor)
        }
        if (alphaSlider != null) {
            alphaSlider!!.setColor(selectedColor)
        }
    }

    enum class WHEEL_TYPE {
        //花心
        FLOWER,

        //圆形
        CIRCLE;

        companion object {
            @JvmStatic
            fun indexOf(index: Int): WHEEL_TYPE {
                return when (index) {
                    0 -> FLOWER
                    1 -> CIRCLE
                    else -> FLOWER
                }
            }
        }
    }

    companion object {
        private const val STROKE_RATIO = 2f
    }
}