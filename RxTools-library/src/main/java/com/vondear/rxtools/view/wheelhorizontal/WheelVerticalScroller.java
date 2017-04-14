/*
 * android-spinnerwheel
 * https://github.com/ai212983/android-spinnerwheel
 *
 * based on
 *
 * Android Wheel Control.
 * https://code.google.com/p/android-wheel/
 *
 * Copyright 2011 Yuri Kanivets
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vondear.rxtools.view.wheelhorizontal;

import android.content.Context;
import android.view.MotionEvent;

/**
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
