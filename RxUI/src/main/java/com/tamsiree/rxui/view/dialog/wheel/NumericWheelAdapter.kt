package com.tamsiree.rxui.view.dialog.wheel

import android.content.Context

/**
 * @author tamsiree
 * Numeric Wheel adapter.
 */
open class NumericWheelAdapter

/**
 * Constructor
 * @param context the current context
 */
@JvmOverloads
constructor(context: Context, // Values
            private val minValue: Int = DEFAULT_MIN_VALUE, private val maxValue: Int = DEFAULT_MAX_VALUE, // format
            private val format: String? = null) : AbstractWheelTextAdapter(context) {

    public override fun getItemText(index: Int): CharSequence? {
        if (index in 0 until itemsCount) {
            val value = minValue + index
            return if (format != null) String.format(format, value) else value.toString()
        }
        return null
    }

    override val itemsCount: Int
        get() = maxValue - minValue + 1

    companion object {
        /** The default min value  */
        const val DEFAULT_MAX_VALUE = 9

        /** The default max value  */
        private const val DEFAULT_MIN_VALUE = 0
    }

}