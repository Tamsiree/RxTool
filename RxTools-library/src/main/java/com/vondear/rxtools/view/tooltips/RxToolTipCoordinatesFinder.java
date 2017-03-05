package com.vondear.rxtools.view.tooltips;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


class RxToolTipCoordinatesFinder {

    /**
     * return the top left coordinates for positioning the tip
     * 
     * @param tipView - the newly created tip view
     * @param tooltip - tool tip object
     * @return point
     */
    static Point getCoordinates(final TextView tipView, RxToolTip tooltip) {
        Point point = new Point();
        final RxCoordinates anchorViewRxCoordinates = new RxCoordinates(tooltip.getAnchorView());
        final RxCoordinates rootRxCoordinates = new RxCoordinates(tooltip.getRootView());

        tipView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (tooltip.getPosition()) {
            case RxToolTip.POSITION_ABOVE:
                point = getPositionAbove(tipView, tooltip,
                        anchorViewRxCoordinates, rootRxCoordinates);
                break;
            case RxToolTip.POSITION_BELOW:
                point = getPositionBelow(tipView, tooltip,
                        anchorViewRxCoordinates, rootRxCoordinates);
                break;
            case RxToolTip.POSITION_LEFT_TO:
                point = getPositionLeftTo(tipView, tooltip,
                        anchorViewRxCoordinates, rootRxCoordinates);
                break;
            case RxToolTip.POSITION_RIGHT_TO:
                point = getPositionRightTo(tipView, tooltip,
                        anchorViewRxCoordinates, rootRxCoordinates);
                break;
        }

        // add user defined offset values
        point.x += RxToolTipUtils.isRtl() ? -tooltip.getOffsetX() : tooltip.getOffsetX();
        point.y += tooltip.getOffsetY();

        // coordinates retrieved are relative to 0,0 of the root layout
        // added view to root is subject to root padding
        // we need to subtract the top and left padding of root from coordinates. to adjust
        // top left tip coordinates
        point.x -= tooltip.getRootView().getPaddingLeft();
        point.y -= tooltip.getRootView().getPaddingTop();

        return point;

    }

    private static Point getPositionRightTo(TextView tipView, RxToolTip rxToolTip, RxCoordinates anchorViewRxCoordinates, RxCoordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewRxCoordinates.right;
        AdjustRightToOutOfBounds(tipView, rxToolTip.getRootView(), point, anchorViewRxCoordinates, rootLocation);
        point.y = anchorViewRxCoordinates.top + getYCenteringOffset(tipView, rxToolTip);
        return point;
    }

    private static Point getPositionLeftTo(TextView tipView, RxToolTip rxToolTip, RxCoordinates anchorViewRxCoordinates, RxCoordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewRxCoordinates.left - tipView.getMeasuredWidth();
        AdjustLeftToOutOfBounds(tipView, rxToolTip.getRootView(), point, anchorViewRxCoordinates, rootLocation);
        point.y = anchorViewRxCoordinates.top + getYCenteringOffset(tipView, rxToolTip);
        return point;
    }

    private static Point getPositionBelow(TextView tipView, RxToolTip rxToolTip, RxCoordinates anchorViewRxCoordinates, RxCoordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewRxCoordinates.left + getXOffset(tipView, rxToolTip);
        if (rxToolTip.alignedCenter()) {
            AdjustHorizontalCenteredOutOfBounds(tipView, rxToolTip.getRootView(), point, rootLocation);
        } else if (rxToolTip.alignedLeft()){
            AdjustHorizontalLeftAlignmentOutOfBounds(tipView, rxToolTip.getRootView(), point, anchorViewRxCoordinates, rootLocation);
        } else if (rxToolTip.alignedRight()){
            AdjustHorizotalRightAlignmentOutOfBounds(tipView, rxToolTip.getRootView(), point, anchorViewRxCoordinates, rootLocation);
        }
        point.y = anchorViewRxCoordinates.bottom;
        return point;
    }

    private static Point getPositionAbove(TextView tipView, RxToolTip rxToolTip,
                                          RxCoordinates anchorViewRxCoordinates, RxCoordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewRxCoordinates.left + getXOffset(tipView, rxToolTip);
        if (rxToolTip.alignedCenter()) {
            AdjustHorizontalCenteredOutOfBounds(tipView, rxToolTip.getRootView(), point, rootLocation);
        } else if (rxToolTip.alignedLeft()){
            AdjustHorizontalLeftAlignmentOutOfBounds(tipView, rxToolTip.getRootView(), point, anchorViewRxCoordinates, rootLocation);
        } else if (rxToolTip.alignedRight()){
            AdjustHorizotalRightAlignmentOutOfBounds(tipView, rxToolTip.getRootView(), point, anchorViewRxCoordinates, rootLocation);
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
    private static int getXOffset(View tipView, RxToolTip rxToolTip) {
        int offset;

        switch (rxToolTip.getAlign()) {
            case RxToolTip.ALIGN_CENTER:
                offset = ((rxToolTip.getAnchorView().getWidth() - tipView.getMeasuredWidth()) / 2);
                break;
            case RxToolTip.ALIGN_LEFT:
                offset = 0;
                break;
            case RxToolTip.ALIGN_RIGHT:
                offset = rxToolTip.getAnchorView().getWidth() - tipView.getMeasuredWidth();
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
    private static int getYCenteringOffset(View tipView, RxToolTip rxToolTip) {
        return (rxToolTip.getAnchorView().getHeight() - tipView.getMeasuredHeight()) / 2;
    }

}
