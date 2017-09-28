/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vondear.rxtools.view.heart.tools;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import java.util.concurrent.atomic.AtomicInteger;

public class RxPathAnimator extends RxAbstractPathAnimator {
    private final AtomicInteger mCounter = new AtomicInteger(0);
    private Handler mHandler;

    public RxPathAnimator(Config config) {
        super(config);
        mHandler = new Handler(Looper.getMainLooper());
    }

    private static float scale(double a, double b, double c, double d, double e) {
        return (float) ((a - b) / (c - b) * (e - d) + d);
    }

    @Override
    public void start(final View child, final ViewGroup parent) {
        parent.addView(child, new ViewGroup.LayoutParams(mConfig.heartWidth, mConfig.heartHeight));
        FloatAnimation anim = new FloatAnimation(createPath(mCounter, parent, 2), randomRotation(), parent, child);
        anim.setDuration(mConfig.animDuration);
        anim.setInterpolator(new LinearInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        parent.removeView(child);
                    }
                });
                mCounter.decrementAndGet();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {
                mCounter.incrementAndGet();
            }
        });
        anim.setInterpolator(new LinearInterpolator());
        child.startAnimation(anim);
    }

    static class FloatAnimation extends Animation {
        private PathMeasure mPm;
        private View mView;
        private float mDistance;
        private float mRotation;

        public FloatAnimation(Path path, float rotation, View parent, View child) {
            mPm = new PathMeasure(path, false);
            mDistance = mPm.getLength();
            mView = child;
            mRotation = rotation;
            parent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        @Override
        protected void applyTransformation(float factor, Transformation transformation) {
            Matrix matrix = transformation.getMatrix();
            mPm.getMatrix(mDistance * factor, matrix, PathMeasure.POSITION_MATRIX_FLAG);
            mView.setRotation(mRotation * factor);
            float scale = 1F;
            if (3000.0F * factor < 200.0F) {
                scale = scale(factor, 0.0D, 0.06666667014360428D, 0.20000000298023224D, 1.100000023841858D);
            } else if (3000.0F * factor < 300.0F) {
                scale = scale(factor, 0.06666667014360428D, 0.10000000149011612D, 1.100000023841858D, 1.0D);
            }
            mView.setScaleX(scale);
            mView.setScaleY(scale);
            transformation.setAlpha(1.0F - factor);
        }
    }
}

