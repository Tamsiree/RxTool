package com.tamsiree.rxui.view.popupwindows.tools

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.popupwindows.tools.RxPopupViewTool.isRtl

/**
 * @author Tamsiree
 */
internal object RxPopupViewBackgroundConstructor {
    /**
     * Select which background will be assign to the tip view
     */
    fun setBackground(tipView: View, rxPopupView: RxPopupView) {

        // show tool tip without arrow. no need to continue
        if (rxPopupView.hideArrow()) {
            setToolTipNoArrowBackground(tipView, rxPopupView.backgroundColor)
            return
        }
        when (rxPopupView.position) {
            RxPopupView.POSITION_ABOVE -> setToolTipAboveBackground(tipView, rxPopupView)
            RxPopupView.POSITION_BELOW -> setToolTipBelowBackground(tipView, rxPopupView)
            RxPopupView.POSITION_LEFT_TO -> setToolTipLeftToBackground(tipView, rxPopupView.backgroundColor)
            RxPopupView.POSITION_RIGHT_TO -> setToolTipRightToBackground(tipView, rxPopupView.backgroundColor)
            else -> {
            }
        }
    }

    private fun setToolTipAboveBackground(tipView: View, rxPopupView: RxPopupView) {
        when (rxPopupView.align) {
            RxPopupView.ALIGN_CENTER -> setTipBackground(tipView, R.drawable.tooltip_arrow_down, rxPopupView.backgroundColor)
            RxPopupView.ALIGN_LEFT -> setTipBackground(tipView,
                    if (!isRtl) R.drawable.tooltip_arrow_down_left else R.drawable.tooltip_arrow_down_right
                    , rxPopupView.backgroundColor)
            RxPopupView.ALIGN_RIGHT -> setTipBackground(tipView,
                    if (!isRtl) R.drawable.tooltip_arrow_down_right else R.drawable.tooltip_arrow_down_left
                    , rxPopupView.backgroundColor)
            else -> {
            }
        }
    }

    private fun setToolTipBelowBackground(tipView: View, rxPopupView: RxPopupView) {
        when (rxPopupView.align) {
            RxPopupView.ALIGN_CENTER -> setTipBackground(tipView, R.drawable.tooltip_arrow_up, rxPopupView.backgroundColor)
            RxPopupView.ALIGN_LEFT -> setTipBackground(tipView,
                    if (!isRtl) R.drawable.tooltip_arrow_up_left else R.drawable.tooltip_arrow_up_right
                    , rxPopupView.backgroundColor)
            RxPopupView.ALIGN_RIGHT -> setTipBackground(tipView,
                    if (!isRtl) R.drawable.tooltip_arrow_up_right else R.drawable.tooltip_arrow_up_left
                    , rxPopupView.backgroundColor)
            else -> {
            }
        }
    }

    private fun setToolTipLeftToBackground(tipView: View, color: Int) {
        setTipBackground(tipView, if (!isRtl) R.drawable.tooltip_arrow_right else R.drawable.tooltip_arrow_left,
                color)
    }

    private fun setToolTipRightToBackground(tipView: View, color: Int) {
        setTipBackground(tipView, if (!isRtl) R.drawable.tooltip_arrow_left else R.drawable.tooltip_arrow_right,
                color)
    }

    private fun setToolTipNoArrowBackground(tipView: View, color: Int) {
        setTipBackground(tipView, R.drawable.tooltip_no_arrow, color)
    }

    private fun setTipBackground(tipView: View, drawableRes: Int, color: Int) {
        val paintedDrawable = getTintedDrawable(tipView.context,
                drawableRes, color)
        setViewBackground(tipView, paintedDrawable)
    }

    private fun setViewBackground(view: View, drawable: Drawable?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.background = drawable
        } else {
            view.setBackgroundDrawable(drawable)
        }
    }

    private fun getTintedDrawable(context: Context, drawableRes: Int, color: Int): Drawable? {
        val drawable: Drawable?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.resources.getDrawable(drawableRes, null)
            drawable?.setTint(color)
        } else {
            drawable = context.resources.getDrawable(drawableRes)
            drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
        return drawable
    }
}