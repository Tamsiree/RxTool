package com.tamsiree.rxui.view.popupwindows.tools

import android.view.View

/**
 * @author tamsiree
 */
class RxCoordinates(view: View) {
    var left: Int
    var top: Int
    var right: Int
    var bottom: Int

    init {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        left = location[0]
        right = left + view.width
        top = location[1]
        bottom = top + view.height
    }
}