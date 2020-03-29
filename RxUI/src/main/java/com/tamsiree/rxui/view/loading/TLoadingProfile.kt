package com.tamsiree.rxui.view.loading

import android.graphics.Color

/*
 * Copyright 2016 Elye Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
internal object TLoadingProfile {
    @JvmField
    val COLOR_DEFAULT_GRADIENT = Color.rgb(245, 245, 245)
    const val MIN_WEIGHT = 0.0f
    const val MAX_WEIGHT = 1.0f
    const val CORNER_DEFAULT = 0
    const val USE_GRADIENT_DEFAULT = false
}