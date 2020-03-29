package com.tamsiree.rxui.view.colorpicker.builder

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.text.InputFilter.LengthFilter
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.tamsiree.rxkit.RxImageTool.getHexString
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.colorpicker.ColorPickerView
import com.tamsiree.rxui.view.colorpicker.ColorPickerView.WHEEL_TYPE
import com.tamsiree.rxui.view.colorpicker.OnColorChangedListener
import com.tamsiree.rxui.view.colorpicker.OnColorSelectedListener
import com.tamsiree.rxui.view.colorpicker.builder.ColorWheelRendererBuilder.getRenderer
import com.tamsiree.rxui.view.colorpicker.slider.AlphaSlider
import com.tamsiree.rxui.view.colorpicker.slider.LightnessSlider

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class ColorPickerDialogBuilder private constructor(context: Context, theme: Int = 0) {
    private val builder: AlertDialog.Builder
    private val pickerContainer: LinearLayout
    private val colorPickerView: ColorPickerView
    private var lightnessSlider: LightnessSlider? = null
    private var alphaSlider: AlphaSlider? = null
    private var colorEdit: EditText? = null
    private var colorPreview: LinearLayout? = null
    private var isLightnessSliderEnabled = true
    private var isAlphaSliderEnabled = true
    private var isColorEditEnabled = false
    private var isPreviewEnabled = false
    private var pickerCount = 1
    private var defaultMargin = 0
    private val initialColor = arrayOf<Int?>(null, null, null, null, null)
    fun setTitle(title: String?): ColorPickerDialogBuilder {
        builder.setTitle(title)
        return this
    }

    fun setTitle(titleId: Int): ColorPickerDialogBuilder {
        builder.setTitle(titleId)
        return this
    }

    fun initialColor(initialColor: Int): ColorPickerDialogBuilder {
        this.initialColor[0] = initialColor
        return this
    }

    fun initialColors(initialColor: IntArray): ColorPickerDialogBuilder {
        var i = 0
        while (i < initialColor.size && i < this.initialColor.size) {
            this.initialColor[i] = initialColor[i]
            i++
        }
        return this
    }

    fun wheelType(wheelType: WHEEL_TYPE?): ColorPickerDialogBuilder {
        val renderer = getRenderer(wheelType)
        colorPickerView.setRenderer(renderer)
        return this
    }

    fun density(density: Int): ColorPickerDialogBuilder {
        colorPickerView.setDensity(density)
        return this
    }

    fun setOnColorChangedListener(onColorChangedListener: OnColorChangedListener?): ColorPickerDialogBuilder {
        colorPickerView.addOnColorChangedListener(onColorChangedListener!!)
        return this
    }

    fun setOnColorSelectedListener(onColorSelectedListener: OnColorSelectedListener?): ColorPickerDialogBuilder {
        colorPickerView.addOnColorSelectedListener(onColorSelectedListener!!)
        return this
    }

    fun setPositiveButton(text: CharSequence?, onClickListener: ColorPickerClickListener): ColorPickerDialogBuilder {
        builder.setPositiveButton(text) { dialog, which -> positiveButtonOnClick(dialog, onClickListener) }
        return this
    }

    fun setPositiveButton(textId: Int, onClickListener: ColorPickerClickListener): ColorPickerDialogBuilder {
        builder.setPositiveButton(textId) { dialog, which -> positiveButtonOnClick(dialog, onClickListener) }
        return this
    }

    fun setNegativeButton(text: CharSequence?, onClickListener: DialogInterface.OnClickListener?): ColorPickerDialogBuilder {
        builder.setNegativeButton(text, onClickListener)
        return this
    }

    fun setNegativeButton(textId: Int, onClickListener: DialogInterface.OnClickListener?): ColorPickerDialogBuilder {
        builder.setNegativeButton(textId, onClickListener)
        return this
    }

    fun noSliders(): ColorPickerDialogBuilder {
        isLightnessSliderEnabled = false
        isAlphaSliderEnabled = false
        return this
    }

    fun alphaSliderOnly(): ColorPickerDialogBuilder {
        isLightnessSliderEnabled = false
        isAlphaSliderEnabled = true
        return this
    }

    fun lightnessSliderOnly(): ColorPickerDialogBuilder {
        isLightnessSliderEnabled = true
        isAlphaSliderEnabled = false
        return this
    }

    fun showAlphaSlider(showAlpha: Boolean): ColorPickerDialogBuilder {
        isAlphaSliderEnabled = showAlpha
        return this
    }

    fun showLightnessSlider(showLightness: Boolean): ColorPickerDialogBuilder {
        isLightnessSliderEnabled = showLightness
        return this
    }

    fun showColorEdit(showEdit: Boolean): ColorPickerDialogBuilder {
        isColorEditEnabled = showEdit
        return this
    }

    fun setColorEditTextColor(argb: Int): ColorPickerDialogBuilder {
        colorPickerView.setColorEditTextColor(argb)
        return this
    }

    fun showColorPreview(showPreview: Boolean): ColorPickerDialogBuilder {
        isPreviewEnabled = showPreview
        if (!showPreview) {
            pickerCount = 1
        }
        return this
    }

    @Throws(IndexOutOfBoundsException::class)
    fun setPickerCount(pickerCount: Int): ColorPickerDialogBuilder {
        if (pickerCount < 1 || pickerCount > 5) {
            throw IndexOutOfBoundsException("Picker Can Only Support 1-5 Colors")
        }
        this.pickerCount = pickerCount
        if (this.pickerCount > 1) {
            isPreviewEnabled = true
        }
        return this
    }

    fun build(): AlertDialog {
        val context = builder.context
        colorPickerView.setInitialColors(initialColor, getStartOffset(initialColor))
        if (isLightnessSliderEnabled) {
            val layoutParamsForLightnessBar = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDimensionAsPx(context, R.dimen.default_slider_height))
            lightnessSlider = LightnessSlider(context)
            lightnessSlider!!.layoutParams = layoutParamsForLightnessBar
            pickerContainer.addView(lightnessSlider)
            colorPickerView.setLightnessSlider(lightnessSlider)
            lightnessSlider!!.setColor(getStartColor(initialColor))
        }
        if (isAlphaSliderEnabled) {
            val layoutParamsForAlphaBar = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDimensionAsPx(context, R.dimen.default_slider_height))
            alphaSlider = AlphaSlider(context)
            alphaSlider!!.layoutParams = layoutParamsForAlphaBar
            pickerContainer.addView(alphaSlider)
            colorPickerView.setAlphaSlider(alphaSlider)
            alphaSlider!!.setColor(getStartColor(initialColor))
        }
        if (isColorEditEnabled) {
            val layoutParamsForColorEdit = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            colorEdit = View.inflate(context, R.layout.picker_edit, null) as EditText
            colorEdit!!.filters = arrayOf<InputFilter>(AllCaps())
            colorEdit!!.setSingleLine()
            colorEdit!!.visibility = View.GONE

            // limit number of characters to hexColors
            val maxLength = if (isAlphaSliderEnabled) 9 else 7
            colorEdit!!.filters = arrayOf<InputFilter>(LengthFilter(maxLength))
            pickerContainer.addView(colorEdit, layoutParamsForColorEdit)
            colorEdit!!.setText(getHexString(getStartColor(initialColor), isAlphaSliderEnabled))
            colorPickerView.setColorEdit(colorEdit)
        }
        if (isPreviewEnabled) {
            colorPreview = View.inflate(context, R.layout.color_preview, null) as LinearLayout
            colorPreview!!.visibility = View.GONE
            pickerContainer.addView(colorPreview)
            if (initialColor.size == 0) {
                val colorImage = View.inflate(context, R.layout.color_selector, null) as ImageView
                colorImage.setImageDrawable(ColorDrawable(Color.WHITE))
            } else {
                var i = 0
                while (i < initialColor.size && i < pickerCount) {
                    if (initialColor[i] == null) {
                        break
                    }
                    val colorLayout = View.inflate(context, R.layout.color_selector, null) as LinearLayout
                    val colorImage = colorLayout.findViewById<ImageView>(R.id.image_preview)
                    colorImage.setImageDrawable(ColorDrawable(initialColor[i]!!))
                    colorPreview!!.addView(colorLayout)
                    i++
                }
            }
            colorPreview!!.visibility = View.VISIBLE
            colorPickerView.setColorPreview(colorPreview, getStartOffset(initialColor))
        }
        return builder.create()
    }

    private fun getStartOffset(colors: Array<Int?>): Int {
        var start = 0
        for (i in colors.indices) {
            if (colors[i] == null) {
                return start
            }
            start = (i + 1) / 2
        }
        return start
    }

    private fun getStartColor(colors: Array<Int?>): Int {
        val startColor = getStartOffset(colors)
        return (if (startColor == null) Color.WHITE else colors[startColor])!!
    }

    private fun positiveButtonOnClick(dialog: DialogInterface, onClickListener: ColorPickerClickListener) {
        val selectedColor = colorPickerView.selectedColor
        val allColors = colorPickerView.allColors
        onClickListener.onClick(dialog, selectedColor, allColors)
    }

    companion object {
        fun with(context: Context): ColorPickerDialogBuilder {
            return ColorPickerDialogBuilder(context)
        }

        fun with(context: Context, theme: Int): ColorPickerDialogBuilder {
            return ColorPickerDialogBuilder(context, theme)
        }

        private fun getDimensionAsPx(context: Context, rid: Int): Int {
            return (context.resources.getDimension(rid) + .5f).toInt()
        }
    }

    init {
        defaultMargin = getDimensionAsPx(context, R.dimen.default_slider_margin)
        val dialogMarginBetweenTitle = getDimensionAsPx(context, R.dimen.default_slider_margin_btw_title)
        builder = AlertDialog.Builder(context, theme)
        pickerContainer = LinearLayout(context)
        pickerContainer.orientation = LinearLayout.VERTICAL
        pickerContainer.gravity = Gravity.CENTER_HORIZONTAL
        pickerContainer.setPadding(defaultMargin, dialogMarginBetweenTitle, defaultMargin, defaultMargin)
        val layoutParamsForColorPickerView = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        layoutParamsForColorPickerView.weight = 1f
        colorPickerView = ColorPickerView(context)
        pickerContainer.addView(colorPickerView, layoutParamsForColorPickerView)
        builder.setView(pickerContainer)
    }
}