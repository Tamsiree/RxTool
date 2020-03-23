/*
 * Copyright 2017 wshunli
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
package com.tamsiree.rxarcgiskit.layer

import com.esri.arcgisruntime.geometry.Point

class RxMapLayerInfo {
    var url: String? = null
    var layerName: String? = null
    var minZoomLevel = 0
    var maxZoomLevel = 17
    private var xMin = 0.0
    private var yMin = 0.0
    private var xMax = 0.0
    private var yMax = 0.0
    var tileWidth = 256
    var tileHeight = 256
    var scales: DoubleArray? = null
    var resolutions: DoubleArray? = null
    var dpi = 96
    var srid = 0
    var origin: Point? = null
    var tileMatrixSet: String? = null
    var cachePathName: String? = null
    var layerType = 0

    fun getxMin(): Double {
        return xMin
    }

    fun setxMin(xMin: Double) {
        this.xMin = xMin
    }

    fun getyMin(): Double {
        return yMin
    }

    fun setyMin(yMin: Double) {
        this.yMin = yMin
    }

    fun getxMax(): Double {
        return xMax
    }

    fun setxMax(xMax: Double) {
        this.xMax = xMax
    }

    fun getyMax(): Double {
        return yMax
    }

    fun setyMax(yMax: Double) {
        this.yMax = yMax
    }


}