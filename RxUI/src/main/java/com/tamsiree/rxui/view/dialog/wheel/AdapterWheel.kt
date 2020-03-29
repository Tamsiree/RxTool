package com.tamsiree.rxui.view.dialog.wheel

import android.content.Context

/**
 * @author tamsiree
 */

class AdapterWheel
/**
 * Constructor
 * @param context the current context
 * @param adapter the source adapter
 */(context: Context?,
    /**
     * Gets original adapter
     * @return the original adapter
     */
// Source adapter
    val adapter: WheelAdapter) : AbstractWheelTextAdapter(context!!) {

    override val itemsCount: Int
        get() = adapter.itemsCount

    override fun getItemText(index: Int): CharSequence? {
        return adapter.getItem(index)
    }

}