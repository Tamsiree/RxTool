package com.vondear.rxtools.view.progressing.style;

import android.animation.ValueAnimator;

import com.vondear.rxtools.view.progressing.animation.SpriteAnimatorBuilder;
import com.vondear.rxtools.view.progressing.animation.interpolator.KeyFrameInterpolator;
import com.vondear.rxtools.view.progressing.sprite.RingSprite;


/**
 * @author vondear
 */
public class PulseRing extends RingSprite {

    public PulseRing() {
        setScale(0f);
    }

    @Override
    public ValueAnimator onCreateAnimation() {
        float fractions[] = new float[]{0f, 0.7f, 1f};
        return new SpriteAnimatorBuilder(this).
                scale(fractions, 0f, 1f, 1f).
                alpha(fractions, 255, (int) (255 * 0.7), 0).
                duration(1000).
                interpolator(KeyFrameInterpolator.pathInterpolator(0.21f, 0.53f, 0.56f, 0.8f, fractions)).
                build();
    }
}
