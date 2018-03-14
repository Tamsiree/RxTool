package com.vondear.rxtools.view.popupwindows.tools;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Vondear
 */
class RxPopupViewCoordinatesFinder {

    /**
     * return the top left coordinates for positioning the tip
     * 
     * @param tipView - the newly created tip view
     * @param popupView - tool tip object
     * @return point
     */
    static Point getCoordinates(final TextView tipView, RxPopupView popupView) {
        Point point = new Point();
        final RxCoordinates anchorViewRxCoordinates = new RxCoordinates(popupView.getAnchorView());
        final RxCoordinates rootRxCoordinates = new RxCoordinates(popupView.getRootView());

        tipView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (popupView.getPosition()) {
            case RxPopupView.POSITION_ABOVE:
                point = getPositionAbove(tipView, popupView,
                        anchorViewRxCoordinates, rootRxCoordinates);
                break;
            case RxPopupView.POSITION_BELOW:
                point = getPositionBelow(tipView, popupView,
                        anchorViewRxCoordinates, rootRxCoordinates);
                break;
            case RxPopupView.POSITION_LEFT_TO:
                point = getPositionLeftTo(tipView, popupView,
                        anchorViewRxCoordinates, rootRxCoordinates);
                break;
            case RxPopupView.POSITION_RIGHT_TO:
                point = getPositionRightTo(tipView, popupView,
                        anchorViewRxCoordinates, rootRxCoordinates);
                break;
            default:
                break;
        }

        // add user defined offset values
        point.x += RxPopupViewTool.isRtl() ? -popupView.getOffsetX() : popupView.getOffsetX();
        point.y += popupView.getOffsetY();

        // coordinates retrieved are relative to 0,0 of the root layout
        // added view to root is subject to root padding
        // we need to subtract the top and left padding of root from coordinates. to adjust
        // top left tip coordinates
        point.x -= popupView.getRootView().getPaddingLeft();
        point.y -= popupView.getRootView().getPaddingTop();

        return point;

    }

    private static Point getPositionRightTo(TextView tipView, RxPopupView rxPopupView, RxCoordinates anchorViewRxCoordinates, RxCoordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewRxCoordinates.right;
        AdjustRightToOutOfBounds(tipView, rxPopupView.getRootView(), point, anchorViewRxCoordinates, rootLocation);
        point.y = anchorViewRxCoordinates.top + getYCenteringOffset(tipView, rxPopupView);
        return point;
    }

    private static Point getPositionLeftTo(TextView tipView, RxPopupView rxPopupView, RxCoordinates anchorViewRxCoordinates, RxCoordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewRxCoordinates.left - tipView.getMeasuredWidth();
        AdjustLeftToOutOfBounds(tipView, rxPopupView.getRootView(), point, anchorViewRxCoordinates, rootLocation);
        point.y = anchorViewRxCoordinates.top + getYCenteringOffset(tipView, rxPopupView);
        return point;
    }

    private static Point getPositionBelow(TextView tipView, RxPopupView rxPopupView, RxCoordinates anchorViewRxCoordinates, RxCoordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewRxCoordinates.left + getXOffset(tipView, rxPopupView);
        if (rxPopupView.alignedCenter()) {
            AdjustHorizontalCenteredOutOfBounds(tipView, rxPopupView.getRootView(), point, rootLocation);
        } else if (rxPopupView.alignedLeft()){
            AdjustHorizontalLeftAlignmentOutOfBounds(tipView, rxPopupView.getRootView(), point, anchorViewRxCoordinates, rootLocation);
        } else if (rxPopupView.alignedRight()){
            AdjustHorizotalRightAlignmentOutOfBounds(tipView, rxPopupView.getRootView(), point, anchorViewRxCoordinates, rootLocation);
        }
        point.y = anchorViewRxCoordinates.bottom;
        return point;
    }

    private static Point getPositionAbove(TextView tipView, RxPopupView rxPopupView,
                                          RxCoordinates anchorViewRxCoordinates, RxCoordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewRxCoordinates.left + getXOffset(tipView, rxPopupView);
        if (rxPopupView.alignedCenter()) {
            AdjustHorizontalCenteredOutOfBounds(tipView, rxPopupView.getRootView(), point, rootLocation);
        } else if (rxPopupView.alignedLeft()){
            AdjustHorizontalLeftAlignmentOutOfBounds(tipView, rxPopupView.getRootView(), point, anchorViewRxCoordinates, rootLocation);
        } else if (rxPopupView.alignedRight()){
            AdjustHorizotalRightAlignmentOutOfBounds(tipView, rxPopupView.getRootView(), point, anchorViewRxCoordinates, rootLocation);
        }
        point.y = anchorViewRxCoordinates.top - tipView.getMeasuredHeight();
        return point;
    }

    private static void AdjustRightToOutOfBounds(TextView tipView, ViewGroup root, Point point, RxCoordinates anchorViewRxCoordinates, RxCoordinates rootLocation) {
        ViewGroup.LayoutParams params = tipView.getLayoutParams();
        int availableSpace = rootLocation.right - root.getPaddingRight() - anchorViewRxCoordinates.right;
        if (point.x + tipView.getMeasuredWidth() > rootLocation.right - root.getPaddingRight()){
            params.width = availableSpace;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tipView.setLayoutParams(params);
            measureViewWithFixedWidth(tipView, params.width);
        }
    }

    private static void AdjustLeftToOutOfBounds(TextView tipView, ViewGroup root, Point point, RxCoordinates anchorViewRxCoordinates, RxCoordinates rootLocation) {
        ViewGroup.LayoutParams params = tipView.getLayoutParams();
        int rootLeft = rootLocation.left + root.getPaddingLeft();
        if (point.x < rootLeft){
            int availableSpace = anchorViewRxCoordinates.left - rootLeft;
            point.x = rootLeft;
            params.width = availableSpace;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tipView.setLayoutParams(params);
            measureViewWithFixedWidth(tipView, params.width);
        }
    }

    private static void AdjustHorizotalRightAlignmentOutOfBounds(TextView tipView, ViewGroup root,
                                                                 Point point, RxCoordinates anchorViewRxCoordinates,
                                                                 RxCoordinates rootLocation) {
        ViewGroup.LayoutParams params = tipView.getLayoutParams();
        int rootLeft = rootLocation.left + root.getPaddingLeft();
        if (point.x < rootLeft){
            int availableSpace = anchorViewRxCoordinates.right - rootLeft;
            point.x = rootLeft;
            params.width = availableSpace;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tipView.setLayoutParams(params);
            measureViewWithFixedWidth(tipView, params.width);
        }
    }

    private static void AdjustHorizontalLeftAlignmentOutOfBounds(TextView tipView, ViewGroup root,
                                                                 Point point, RxCoordinates anchorViewRxCoordinates,
                                                                 RxCoordinates rootLocation) {
        ViewGroup.LayoutParams params = tipView.getLayoutParams();
        int rootRight = rootLocation.right - root.getPaddingRight();
        if (point.x + tipView.getMeasuredWidth() > rootRight){
            params.width = rootRight - anchorViewRxCoordinates.left;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tipView.setLayoutParams(params);
            measureViewWithFixedWidth(tipView, params.width);
        }
    }

    private static void AdjustHorizontalCenteredOutOfBounds(TextView tipView, ViewGroup root,
                                                            Point point, RxCoordinates rootLocation) {
        ViewGroup.LayoutParams params = tipView.getLayoutParams();
        int rootWidth = root.getWidth() - root.getPaddingLeft() - root.getPaddingRight();
        if (tipView.getMeasuredWidth() > rootWidth) {
            point.x = rootLocation.left + root.getPaddingLeft();
            params.width = rootWidth;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tipView.setLayoutParams(params);
            measureViewWithFixedWidth(tipView, rootWidth);
        }
    }


    private static void measureViewWithFixedWidth(TextView tipView, int width) {
        tipView.measure(View.MeasureSpec.makeMeasureSpec(width,
                View.MeasureSpec.EXACTLY), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * calculate the amount of movement need to be taken inorder to align tip
     * on X axis according to "align" parameter
     * @return int
     */
    private static int getXOffset(View tipView, RxPopupView rxPopupView) {
        int offset;

        switch (rxPopupView.getAlign()) {
            case RxPopupView.ALIGN_CENTER:
                offset = ((rxPopupView.getAnchorView().getWidth() - tipView.getMeasuredWidth()) / 2);
                break;
            case RxPopupView.ALIGN_LEFT:
                offset = 0;
                break;
            case RxPopupView.ALIGN_RIGHT:
                offset = rxPopupView.getAnchorView().getWidth() - tipView.getMeasuredWidth();
                break;
            default:
                offset = 0;
                break;
        }

        return offset;
    }

    /**
     * calculate the amount of movement need to be taken inorder to center tip
     * on Y axis
     * @return int
     */
    private static int getYCenteringOffset(View tipView, RxPopupView rxPopupView) {
        return (rxPopupView.getAnchorView().getHeight() - tipView.getMeasuredHeight()) / 2;
    }

}
