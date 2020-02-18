package com.tamsiree.rxui.view.progressing.animation.interpolator;

import android.view.animation.Interpolator;

/**
 * @author tamsiree
 */
public class Ease {
    public static Interpolator inOut() {
        return PathInterpolatorCompat.create(0.42f, 0f, 0.58f, 1f);
    }
}
