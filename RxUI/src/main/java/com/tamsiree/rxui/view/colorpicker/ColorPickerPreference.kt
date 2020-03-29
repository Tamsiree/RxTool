package com.tamsiree.rxui.view.colorpicker

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.preference.Preference
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.colorpicker.ColorPickerView.WHEEL_TYPE
import com.tamsiree.rxui.view.colorpicker.ColorPickerView.WHEEL_TYPE.Companion.indexOf
import com.tamsiree.rxui.view.colorpicker.builder.ColorPickerClickListener
import com.tamsiree.rxui.view.colorpicker.builder.ColorPickerDialogBuilder

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class ColorPickerPreference : Preference {
    protected var alphaSlider = false
    protected var lightSlider = false
    protected var selectedColor = 0
    protected var wheelType: WHEEL_TYPE? = null
    protected var density = 0
    protected var colorIndicator: ImageView? = null
    private var pickerTitle: String? = null
    private var pickerButtonCancel: String? = null
    private var pickerButtonOk: String? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initWith(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initWith(context, attrs)
    }

    private fun initWith(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference)
        try {
            alphaSlider = typedArray.getBoolean(R.styleable.ColorPickerPreference_alphaSlider, false)
            lightSlider = typedArray.getBoolean(R.styleable.ColorPickerPreference_lightnessSlider, false)
            density = typedArray.getInt(R.styleable.ColorPickerPreference_density, 10)
            wheelType = indexOf(typedArray.getInt(R.styleable.ColorPickerPreference_wheelType, 0))
            selectedColor = typedArray.getInt(R.styleable.ColorPickerPreference_initialColor, -0x1)
            pickerTitle = typedArray.getString(R.styleable.ColorPickerPreference_pickerTitle)
            if (pickerTitle == null) {
                pickerTitle = "Choose color"
            }
            pickerButtonCancel = typedArray.getString(R.styleable.ColorPickerPreference_pickerButtonCancel)
            if (pickerButtonCancel == null) {
                pickerButtonCancel = "cancel"
            }
            pickerButtonOk = typedArray.getString(R.styleable.ColorPickerPreference_pickerButtonOk)
            if (pickerButtonOk == null) {
                pickerButtonOk = "ok"
            }
        } finally {
            typedArray.recycle()
        }
        widgetLayoutResource = R.layout.color_widget
    }

    override fun onBindView(view: View) {
        super.onBindView(view)
        val res = view.context.resources
        var colorChoiceDrawable: GradientDrawable? = null
        colorIndicator = view.findViewById<View>(R.id.color_indicator) as ImageView
        val currentDrawable = colorIndicator!!.drawable
        if (currentDrawable != null && currentDrawable is GradientDrawable) {
            colorChoiceDrawable = currentDrawable
        }
        if (colorChoiceDrawable == null) {
            colorChoiceDrawable = GradientDrawable()
            colorChoiceDrawable.shape = GradientDrawable.OVAL
        }
        val tmpColor = if (isEnabled) selectedColor else darken(selectedColor, .5f)
        colorChoiceDrawable.setColor(tmpColor)
        colorChoiceDrawable.setStroke(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1f,
                res.displayMetrics
        ).toInt(), darken(tmpColor, .8f))
        colorIndicator!!.setImageDrawable(colorChoiceDrawable)
    }

    fun setValue(value: Int) {
        if (callChangeListener(value)) {
            selectedColor = value
            persistInt(value)
            notifyChanged()
        }
    }

    override fun onSetInitialValue(restoreValue: Boolean, defaultValue: Any) {
        setValue((if (restoreValue) getPersistedInt(0) else defaultValue as Int))
    }

    override fun onClick() {
        val builder = ColorPickerDialogBuilder
                .with(context)
                .setTitle(pickerTitle)
                .initialColor(selectedColor)
                .wheelType(wheelType)
                .density(density)
                .setPositiveButton(pickerButtonOk, object : ColorPickerClickListener {
                    override fun onClick(d: DialogInterface?, lastSelectedColor: Int, allColors: Array<Int?>?) {
                        setValue(lastSelectedColor)
                    }
                })
                .setNegativeButton(pickerButtonCancel, null)
        if (!alphaSlider && !lightSlider) {
            builder.noSliders()
        } else if (!alphaSlider) {
            builder.lightnessSliderOnly()
        } else if (!lightSlider) {
            builder.alphaSliderOnly()
        }
        builder
                .build()
                .show()
    }

    companion object {
        fun darken(color: Int, factor: Float): Int {
            val a = Color.alpha(color)
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            return Color.argb(a,
                    Math.max((r * factor).toInt(), 0),
                    Math.max((g * factor).toInt(), 0),
                    Math.max((b * factor).toInt(), 0))
        }
    }
}