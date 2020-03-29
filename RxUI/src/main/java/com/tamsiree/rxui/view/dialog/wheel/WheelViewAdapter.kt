package com.tamsiree.rxui.view.dialog.wheel

import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup

/**
 * @author tamsiree
 * Wheel items adapter interface
 */
interface WheelViewAdapter {
    /**
     * Gets items count
     * @return the count of wheel items
     */
    val itemsCount: Int

    /**
     * Get a View that displays the data at the specified position in the data set
     *
     * @param index the item index
     * @param convertView the old view to reuse if possible
     * @param parent the parent that this view will eventually be attached to
     * @return the wheel item View
     */
    fun getItem(index: Int, convertView: View?, parent: ViewGroup?): View?

    /**
     * Get a View that displays an empty wheel item placed before the first or after
     * the last wheel item.
     *
     * @param convertView the old view to reuse if possible
     * @param parent the parent that this view will eventually be attached to
     * @return the empty item View
     */
    fun getEmptyItem(convertView: View?, parent: ViewGroup?): View?

    /**
     * Register an observer that is called when changes happen to the data used by this adapter.
     * @param observer the observer to be registered
     */
    fun registerDataSetObserver(observer: DataSetObserver?)

    /**
     * Unregister an observer that has previously been registered
     * @param observer the observer to be unregistered
     */
    fun unregisterDataSetObserver(observer: DataSetObserver?)
}