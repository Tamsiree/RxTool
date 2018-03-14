package com.vondear.rxtools.view.wheelhorizontal;


/**
 * @author vondear
 * Wheel clicked listener interface.
 * <p>The onItemClicked() method is called whenever a spinnerwheel item is clicked
 * <li> New Wheel position is set
 * <li> Wheel view is scrolled
 */
public interface OnWheelClickedListener {
    /**
     * Callback method to be invoked when current item clicked
     * @param wheel the spinnerwheel view
     * @param itemIndex the index of clicked item
     */
    void onItemClicked(AbstractWheel wheel, int itemIndex);
}
