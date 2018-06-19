package com.vondear.rxui.animation;

import android.animation.ValueAnimator;
import android.graphics.Canvas;

import com.vondear.rxui.view.likeview.tools.RxShineView;
import com.vondear.rxui.view.likeview.tools.ei.RxEase;
import com.vondear.rxui.view.likeview.tools.ei.RxEasingInterpolator;

/**
 * @author vondear
 * @date 2016/7/5 下午5:09
 */
public class RxShineAnimator extends ValueAnimator {

    float MAX_VALUE = 1.5f;
    long ANIM_DURATION = 1500;
    Canvas canvas;

    public RxShineAnimator() {
        setFloatValues(1f, MAX_VALUE);
        setDuration(ANIM_DURATION);
        setStartDelay(200);
        setInterpolator(new RxEasingInterpolator(RxEase.QUART_OUT));
    }

    public RxShineAnimator(long duration, float maxValue, long delay) {
        setFloatValues(1f, maxValue);
        setDuration(duration);
        setStartDelay(delay);
        setInterpolator(new RxEasingInterpolator(RxEase.QUART_OUT));
    }

    public void startAnim(final RxShineView rxShineView, final int centerAnimX, final int centerAnimY) {

        start();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }


}
