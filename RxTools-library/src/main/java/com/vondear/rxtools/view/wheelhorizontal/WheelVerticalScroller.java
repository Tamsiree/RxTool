package com.vondear.rxtools.view.wheelhorizontal;

import android.content.Context;
import android.view.MotionEvent;

/**
 * @author vondear
 * Scroller class handles scrolling events and updates the 
 */
public class WheelVerticalScroller extends WheelScroller {

    /**
     * Constructor
     * @param context the current context
     * @param listener the scrolling listener
     */
    public WheelVerticalScroller(Context context, ScrollingListener listener) {
        super(context, listener);
    }

    @Override
    protected int getCurrentScrollerPosition() {
        return scroller.getCurrY();
    }

    @Override
    protected int getFinalScrollerPosition() {
        return scroller.getFinalY();
    }

    @Override
    protected float getMotionEventPosition(MotionEvent event) {
        // should be overriden
        return event.getY();
    }

    @Override
    protected void scrollerStartScroll(int distance, int time) {
        scroller.startScroll(0, 0, 0, distance, time);
    }

    @Override
    protected void scrollerFling(int position, int velocityX, int velocityY) {
        final int maxPosition = 0x7FFFFFFF;
        final int minPosition = -maxPosition;
        scroller.fling(0, position, 0, -velocityY, 0, 0, minPosition, maxPosition);
    }
}
