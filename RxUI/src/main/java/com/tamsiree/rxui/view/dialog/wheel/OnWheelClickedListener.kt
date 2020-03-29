package com.tamsiree.rxui.view.dialog.wheel

/**
 * @author tamsiree
 * Wheel clicked listener interface.
 *
 * The onItemClicked() method is called whenever a wheel item is clicked
 *  *  New Wheel position is set
 *  *  Wheel view is scrolled
 */
interface OnWheelClickedListener {
    /**
     * Callback method to be invoked when current item clicked
     * @param wheel the wheel view
     * @param itemIndex the index of clicked item
     */
    fun onItemClicked(wheel: WheelView?, itemIndex: Int)
}