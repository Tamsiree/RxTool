package com.vondear.rxtools.view.progressing.animation;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Property;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

import com.vondear.rxtools.view.progressing.animation.interpolator.KeyFrameInterpolator;
import com.vondear.rxtools.view.progressing.sprite.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author vondear
 */
public class SpriteAnimatorBuilder {
    private Sprite sprite;
    private List<PropertyValuesHolder> propertyValuesHolders = new ArrayList<>();
    private Interpolator interpolator;
    private int repeatCount = Animation.INFINITE;
    private long duration = 2000;

    public SpriteAnimatorBuilder(Sprite sprite) {
        this.sprite = sprite;
    }

    public SpriteAnimatorBuilder scale(float fractions[], float... scale) {
        holder(fractions, Sprite.SCALE, scale);
        return this;
    }

    public SpriteAnimatorBuilder alpha(float fractions[], int... alpha) {
        holder(fractions, Sprite.ALPHA, alpha);
        return this;
    }

    @SuppressWarnings("unused")
    public SpriteAnimatorBuilder scaleX(float fractions[], float... scaleX) {
        holder(fractions, Sprite.SCALE, scaleX);
        return this;
    }

    public SpriteAnimatorBuilder scaleY(float fractions[], float... scaleY) {
        holder(fractions, Sprite.SCALE_Y, scaleY);
        return this;
    }

    public SpriteAnimatorBuilder rotateX(float fractions[], int... rotateX) {
        holder(fractions, Sprite.ROTATE_X, rotateX);
        return this;
    }

    public SpriteAnimatorBuilder rotateY(float fractions[], int... rotateY) {
        holder(fractions, Sprite.ROTATE_Y, rotateY);
        return this;
    }

    @SuppressWarnings("unused")
    public SpriteAnimatorBuilder translateX(float fractions[], int... translateX) {
        holder(fractions, Sprite.TRANSLATE_X, translateX);
        return this;
    }


    @SuppressWarnings("unused")
    public SpriteAnimatorBuilder translateY(float fractions[], int... translateY) {
        holder(fractions, Sprite.TRANSLATE_Y, translateY);
        return this;
    }


    public SpriteAnimatorBuilder rotate(float fractions[], int... rotate) {
        holder(fractions, Sprite.ROTATE, rotate);
        return this;
    }

    public SpriteAnimatorBuilder translateXPercentage(float fractions[], float... translateXPercentage) {
        holder(fractions, Sprite.TRANSLATE_X_PERCENTAGE, translateXPercentage);
        return this;
    }

    public SpriteAnimatorBuilder translateYPercentage(float[] fractions, float... translateYPercentage) {
        holder(fractions, Sprite.TRANSLATE_Y_PERCENTAGE, translateYPercentage);
        return this;
    }

    private PropertyValuesHolder holder(float[] fractions, Property property, float[] values) {
        ensurePair(fractions.length, values.length);
        Keyframe[] keyframes = new Keyframe[fractions.length];
        for (int i = 0; i < values.length; i++) {
            keyframes[i] = Keyframe.ofFloat(fractions[i], values[i]);
        }
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.
                ofKeyframe(property
                        , keyframes
                );
        propertyValuesHolders.add(valuesHolder);
        return valuesHolder;
    }

    private PropertyValuesHolder holder(float[] fractions, Property property, int[] values) {
        ensurePair(fractions.length, values.length);
        Keyframe[] keyframes = new Keyframe[fractions.length];
        for (int i = 0; i < values.length; i++) {
            keyframes[i] = Keyframe.ofInt(fractions[i], values[i]);
        }
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.
                ofKeyframe(property
                        , keyframes
                );
        propertyValuesHolders.add(valuesHolder);
        return valuesHolder;
    }

    private void ensurePair(int fractionsLength, int valuesLength) {
        if (fractionsLength != valuesLength) {
            throw new IllegalStateException(String.format(
                    Locale.getDefault(),
                    "The fractions.length must equal values.length, " +
                            "fraction.length[%d], values.length[%d]",
                    fractionsLength,
                    valuesLength));
        }
    }


    public SpriteAnimatorBuilder interpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    public SpriteAnimatorBuilder easeInOut(float... fractions) {
        interpolator(KeyFrameInterpolator.easeInOut(
                fractions
        ));
        return this;
    }


    public SpriteAnimatorBuilder duration(long duration) {
        this.duration = duration;
        return this;
    }

    @SuppressWarnings("unused")
    public SpriteAnimatorBuilder repeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }


    public ObjectAnimator build() {
        PropertyValuesHolder[] holders = new PropertyValuesHolder[propertyValuesHolders.size()];
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(sprite,
                propertyValuesHolders.toArray(holders));
        animator.setDuration(duration);
        animator.setRepeatCount(repeatCount);
        animator.setInterpolator(interpolator);
        return animator;
    }

}
