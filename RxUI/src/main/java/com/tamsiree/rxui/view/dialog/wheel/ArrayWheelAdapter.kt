package com.tamsiree.rxui.view.dialog.wheel

import android.content.Context

/**
 * @param <T> the element type
 * The simple Array wheel adapter
 * @author tamsiree
</T> */
open class ArrayWheelAdapter<T>
//setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
(context: Context, // items
 private val items: Array<T>) : AbstractWheelTextAdapter(context, 0, 0) {
    public override fun getItemText(index: Int): CharSequence? {
        if (index >= 0 && index < items.size) {
            val item = items[index]
            return if (item is CharSequence) {
                item
            } else item.toString()
        }
        return null
    }

    override val itemsCount: Int
        get() = items.size

}