package com.vondear.rxtools.view.likeview.ei;

import android.animation.TimeInterpolator;
import android.support.annotation.NonNull;

/**
 * The Easing class provides a collection of ease functions. It does not use the standard 4 param
 * ease signature. Instead it uses a single param which indicates the current linear ratio (0 to 1) of the tween.
 */
public class EasingInterpolator implements TimeInterpolator {

    private final Ease ease;

    public EasingInterpolator(@NonNull Ease ease) {
        this.ease = ease;
    }

    @Override
    public float getInterpolation(float input) {
        return EasingProvider.get(this.ease, input);
    }

    public Ease getEase() {
        return ease;
    }
}
