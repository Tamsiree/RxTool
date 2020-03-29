package com.tamsiree.rxui.view.popupwindows.tools

import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tamsiree.rxui.view.popupwindows.tools.RxPopupViewTool.isRtl

/**
 * @author Tamsiree
 */
internal object RxPopupViewCoordinatesFinder {
    /**
     * return the top left coordinates for positioning the tip
     *
     * @param tipView - the newly created tip view
     * @param popupView - tool tip object
     * @return point
     */
    fun getCoordinates(tipView: TextView, popupView: RxPopupView): Point {
        var point = Point()
        val anchorViewRxCoordinates = RxCoordinates(popupView.anchorView)
        val rootRxCoordinates = RxCoordinates(popupView.rootView)
        tipView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        when (popupView.position) {
            RxPopupView.POSITION_ABOVE -> point = getPositionAbove(tipView, popupView,
                    anchorViewRxCoordinates, rootRxCoordinates)
            RxPopupView.POSITION_BELOW -> point = getPositionBelow(tipView, popupView,
                    anchorViewRxCoordinates, rootRxCoordinates)
            RxPopupView.POSITION_LEFT_TO -> point = getPositionLeftTo(tipView, popupView,
                    anchorViewRxCoordinates, rootRxCoordinates)
            RxPopupView.POSITION_RIGHT_TO -> point = getPositionRightTo(tipView, popupView,
                    anchorViewRxCoordinates, rootRxCoordinates)
            else -> {
            }
        }

        // add user defined offset values
        point.x += if (isRtl) -popupView.offsetX else popupView.offsetX
        point.y += popupView.offsetY

        // coordinates retrieved are relative to 0,0 of the root layout
        // added view to root is subject to root padding
        // we need to subtract the top and left padding of root from coordinates. to adjust
        // top left tip coordinates
        point.x -= popupView.rootView.paddingLeft
        point.y -= popupView.rootView.paddingTop
        return point
    }

    private fun getPositionRightTo(tipView: TextView, rxPopupView: RxPopupView, anchorViewRxCoordinates: RxCoordinates, rootLocation: RxCoordinates): Point {
        val point = Point()
        point.x = anchorViewRxCoordinates.right
        AdjustRightToOutOfBounds(tipView, rxPopupView.rootView, point, anchorViewRxCoordinates, rootLocation)
        point.y = anchorViewRxCoordinates.top + getYCenteringOffset(tipView, rxPopupView)
        return point
    }

    private fun getPositionLeftTo(tipView: TextView, rxPopupView: RxPopupView, anchorViewRxCoordinates: RxCoordinates, rootLocation: RxCoordinates): Point {
        val point = Point()
        point.x = anchorViewRxCoordinates.left - tipView.measuredWidth
        AdjustLeftToOutOfBounds(tipView, rxPopupView.rootView, point, anchorViewRxCoordinates, rootLocation)
        point.y = anchorViewRxCoordinates.top + getYCenteringOffset(tipView, rxPopupView)
        return point
    }

    private fun getPositionBelow(tipView: TextView, rxPopupView: RxPopupView, anchorViewRxCoordinates: RxCoordinates, rootLocation: RxCoordinates): Point {
        val point = Point()
        point.x = anchorViewRxCoordinates.left + getXOffset(tipView, rxPopupView)
        if (rxPopupView.alignedCenter()) {
            AdjustHorizontalCenteredOutOfBounds(tipView, rxPopupView.rootView, point, rootLocation)
        } else if (rxPopupView.alignedLeft()) {
            AdjustHorizontalLeftAlignmentOutOfBounds(tipView, rxPopupView.rootView, point, anchorViewRxCoordinates, rootLocation)
        } else if (rxPopupView.alignedRight()) {
            AdjustHorizotalRightAlignmentOutOfBounds(tipView, rxPopupView.rootView, point, anchorViewRxCoordinates, rootLocation)
        }
        point.y = anchorViewRxCoordinates.bottom
        return point
    }

    private fun getPositionAbove(tipView: TextView, rxPopupView: RxPopupView,
                                 anchorViewRxCoordinates: RxCoordinates, rootLocation: RxCoordinates): Point {
        val point = Point()
        point.x = anchorViewRxCoordinates.left + getXOffset(tipView, rxPopupView)
        if (rxPopupView.alignedCenter()) {
            AdjustHorizontalCenteredOutOfBounds(tipView, rxPopupView.rootView, point, rootLocation)
        } else if (rxPopupView.alignedLeft()) {
            AdjustHorizontalLeftAlignmentOutOfBounds(tipView, rxPopupView.rootView, point, anchorViewRxCoordinates, rootLocation)
        } else if (rxPopupView.alignedRight()) {
            AdjustHorizotalRightAlignmentOutOfBounds(tipView, rxPopupView.rootView, point, anchorViewRxCoordinates, rootLocation)
        }
        point.y = anchorViewRxCoordinates.top - tipView.measuredHeight
        return point
    }

    private fun AdjustRightToOutOfBounds(tipView: TextView, root: ViewGroup, point: Point, anchorViewRxCoordinates: RxCoordinates, rootLocation: RxCoordinates) {
        val params = tipView.layoutParams
        val availableSpace = rootLocation.right - root.paddingRight - anchorViewRxCoordinates.right
        if (point.x + tipView.measuredWidth > rootLocation.right - root.paddingRight) {
            params.width = availableSpace
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tipView.layoutParams = params
            measureViewWithFixedWidth(tipView, params.width)
        }
    }

    private fun AdjustLeftToOutOfBounds(tipView: TextView, root: ViewGroup, point: Point, anchorViewRxCoordinates: RxCoordinates, rootLocation: RxCoordinates) {
        val params = tipView.layoutParams
        val rootLeft = rootLocation.left + root.paddingLeft
        if (point.x < rootLeft) {
            val availableSpace = anchorViewRxCoordinates.left - rootLeft
            point.x = rootLeft
            params.width = availableSpace
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tipView.layoutParams = params
            measureViewWithFixedWidth(tipView, params.width)
        }
    }

    private fun AdjustHorizotalRightAlignmentOutOfBounds(tipView: TextView, root: ViewGroup,
                                                         point: Point, anchorViewRxCoordinates: RxCoordinates,
                                                         rootLocation: RxCoordinates) {
        val params = tipView.layoutParams
        val rootLeft = rootLocation.left + root.paddingLeft
        if (point.x < rootLeft) {
            val availableSpace = anchorViewRxCoordinates.right - rootLeft
            point.x = rootLeft
            params.width = availableSpace
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tipView.layoutParams = params
            measureViewWithFixedWidth(tipView, params.width)
        }
    }

    private fun AdjustHorizontalLeftAlignmentOutOfBounds(tipView: TextView, root: ViewGroup,
                                                         point: Point, anchorViewRxCoordinates: RxCoordinates,
                                                         rootLocation: RxCoordinates) {
        val params = tipView.layoutParams
        val rootRight = rootLocation.right - root.paddingRight
        if (point.x + tipView.measuredWidth > rootRight) {
            params.width = rootRight - anchorViewRxCoordinates.left
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tipView.layoutParams = params
            measureViewWithFixedWidth(tipView, params.width)
        }
    }

    private fun AdjustHorizontalCenteredOutOfBounds(tipView: TextView, root: ViewGroup,
                                                    point: Point, rootLocation: RxCoordinates) {
        val params = tipView.layoutParams
        val rootWidth = root.width - root.paddingLeft - root.paddingRight
        if (tipView.measuredWidth > rootWidth) {
            point.x = rootLocation.left + root.paddingLeft
            params.width = rootWidth
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tipView.layoutParams = params
            measureViewWithFixedWidth(tipView, rootWidth)
        }
    }

    private fun measureViewWithFixedWidth(tipView: TextView, width: Int) {
        tipView.measure(View.MeasureSpec.makeMeasureSpec(width,
                View.MeasureSpec.EXACTLY), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * calculate the amount of movement need to be taken inorder to align tip
     * on X axis according to "align" parameter
     * @return int
     */
    private fun getXOffset(tipView: View, rxPopupView: RxPopupView): Int {
        val offset: Int
        offset = when (rxPopupView.align) {
            RxPopupView.ALIGN_CENTER -> (rxPopupView.anchorView.width - tipView.measuredWidth) / 2
            RxPopupView.ALIGN_LEFT -> 0
            RxPopupView.ALIGN_RIGHT -> rxPopupView.anchorView.width - tipView.measuredWidth
            else -> 0
        }
        return offset
    }

    /**
     * calculate the amount of movement need to be taken inorder to center tip
     * on Y axis
     * @return int
     */
    private fun getYCenteringOffset(tipView: View, rxPopupView: RxPopupView): Int {
        return (rxPopupView.anchorView.height - tipView.measuredHeight) / 2
    }
}