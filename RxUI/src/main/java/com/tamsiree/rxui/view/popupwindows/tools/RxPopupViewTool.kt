package com.tamsiree.rxui.view.popupwindows.tools

import java.util.*

/**
 * @author Tamsiree
 */
internal object RxPopupViewTool {
    val isRtl: Boolean
        get() {
            val defLocal = Locale.getDefault()
            return (Character.getDirectionality(defLocal.getDisplayName(defLocal)[0])
                    == Character.DIRECTIONALITY_RIGHT_TO_LEFT)
        }
}