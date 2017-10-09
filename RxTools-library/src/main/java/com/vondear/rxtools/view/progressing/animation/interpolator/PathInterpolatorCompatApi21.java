/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vondear.rxtools.view.progressing.animation.interpolator;

import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

/**
 * API 21+ implementation for path interpolator compatibility.
 */
class PathInterpolatorCompatApi21 {

    private PathInterpolatorCompatApi21() {
        // prevent instantiation
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Interpolator create(Path path) {
        return new PathInterpolator(path);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Interpolator create(float controlX, float controlY) {
        return new PathInterpolator(controlX, controlY);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Interpolator create(float controlX1, float controlY1,
                                      float controlX2, float controlY2) {
        return new PathInterpolator(controlX1, controlY1, controlX2, controlY2);
    }
}
