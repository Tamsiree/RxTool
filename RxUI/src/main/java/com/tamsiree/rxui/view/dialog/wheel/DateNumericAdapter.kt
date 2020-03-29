package com.tamsiree.rxui.view.dialog.wheel

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * @author tamsiree
 */
class DateNumericAdapter(context: Context?, minValue: Int, maxValue: Int,
        // Index of item to be highlighted
                         var currentValue: Int) : NumericWheelAdapter(context!!, minValue, maxValue) {
    // Index of current item
    var currentItem = 0

    override fun configureTextView(view: TextView) {
        super.configureTextView(view)
        /*if (currentItem == currentValue) {
			view.setTextColor(0xFF0000F0);
		}*/view.typeface = Typeface.SANS_SERIF
    }

    override fun getItem(index: Int, cachedView: View?, parent: ViewGroup?): View? {
        currentItem = index
        return super.getItem(index, cachedView, parent)
    }

    /**
     * Constructor
     */
    init {
        textSize = 16
    }
}