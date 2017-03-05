package com.vondear.rxtools.view.tooltips;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.vondear.rxtools.R;


class RxToolTipBackgroundConstructor {

    /**
     * Select which background will be assign to the tip view
     */
    static void setBackground(View tipView, RxToolTip rxToolTip) {

        // show tool tip without arrow. no need to continue
        if (rxToolTip.hideArrow()) {
            setToolTipNoArrowBackground(tipView, rxToolTip.getBackgroundColor());
            return;
        }

        // show tool tip according to requested position
        switch (rxToolTip.getPosition()) {
            case RxToolTip.POSITION_ABOVE:
                setToolTipAboveBackground(tipView, rxToolTip);
                break;
            case RxToolTip.POSITION_BELOW:
                setToolTipBelowBackground(tipView, rxToolTip);
                break;
            case RxToolTip.POSITION_LEFT_TO:
                setToolTipLeftToBackground(tipView, rxToolTip.getBackgroundColor());
                break;
            case RxToolTip.POSITION_RIGHT_TO:
                setToolTipRightToBackground(tipView, rxToolTip.getBackgroundColor());
                break;
        }

    }

    private static void setToolTipAboveBackground(View tipView, RxToolTip rxToolTip) {
        switch (rxToolTip.getAlign()) {
            case RxToolTip.ALIGN_CENTER:
                setTipBackground(tipView, R.drawable.tooltip_arrow_down, rxToolTip.getBackgroundColor());
                break;
            case RxToolTip.ALIGN_LEFT:
                setTipBackground(tipView,
                        !RxToolTipUtils.isRtl() ?
                                R.drawable.tooltip_arrow_down_left :
                                R.drawable.tooltip_arrow_down_right
                        , rxToolTip.getBackgroundColor());
                break;
            case RxToolTip.ALIGN_RIGHT:
                setTipBackground(tipView,
                        !RxToolTipUtils.isRtl() ?
                                R.drawable.tooltip_arrow_down_right :
                                R.drawable.tooltip_arrow_down_left
                        , rxToolTip.getBackgroundColor());
                break;
        }
    }

    private static void setToolTipBelowBackground(View tipView, RxToolTip rxToolTip) {

        switch (rxToolTip.getAlign()) {
            case RxToolTip.ALIGN_CENTER:
                setTipBackground(tipView, R.drawable.tooltip_arrow_up, rxToolTip.getBackgroundColor());
                break;
            case RxToolTip.ALIGN_LEFT:
                setTipBackground(tipView,
                        !RxToolTipUtils.isRtl() ?
                                R.drawable.tooltip_arrow_up_left :
                                R.drawable.tooltip_arrow_up_right
                        , rxToolTip.getBackgroundColor());
                break;
            case RxToolTip.ALIGN_RIGHT:
                setTipBackground(tipView,
                        !RxToolTipUtils.isRtl() ?
                                R.drawable.tooltip_arrow_up_right :
                                R.drawable.tooltip_arrow_up_left
                        , rxToolTip.getBackgroundColor());
                break;
        }

    }

    private static void setToolTipLeftToBackground(View tipView, int color) {
        setTipBackground(tipView, !RxToolTipUtils.isRtl() ?
                        R.drawable.tooltip_arrow_right : R.drawable.tooltip_arrow_left,
                color);
    }

    private static void setToolTipRightToBackground(View tipView, int color) {
        setTipBackground(tipView, !RxToolTipUtils.isRtl() ?
                        R.drawable.tooltip_arrow_left : R.drawable.tooltip_arrow_right,
                color);
    }

    private static void setToolTipNoArrowBackground(View tipView, int color) {
        setTipBackground(tipView, R.drawable.tooltip_no_arrow, color);
    }

    private static void setTipBackground(View tipView, int drawableRes, int color) {
        Drawable paintedDrawable = getTintedDrawable(tipView.getContext(),
                drawableRes, color);
        setViewBackground(tipView, paintedDrawable);
    }

    private static void setViewBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    private static Drawable getTintedDrawable(Context context, int drawableRes, int color) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getResources().getDrawable(drawableRes, null);
            if (drawable != null) {
                drawable.setTint(color);
            }
        } else {
            drawable = context.getResources().getDrawable(drawableRes);
            if (drawable != null) {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }

        return drawable;
    }

}
