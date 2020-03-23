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
import com.esri.arcgisruntime.geometry.SpatialReference
import java.util.*

object RxLayerInfoFactory {
    private const val TAG = "RxLayerInfoFactory"
    private const val AMAP_VECTOR_NAME = "7"
    private const val AMAP_IMAGE_NAME = "6"
    private const val AMAP_ROAD_NAME = "8"
    private const val BAIDU_MAP_VECTOR_NAME = "pl"
    private const val BAIDU_MAP_ROAD_NAME = "sl"
    private const val TENCENT_MAP_VECTOR_NAME = "0"
    private const val TENCENT_MAP_VECTOR_NIGHT_NAME = "1"
    private const val TENCENT_MAP_ROAD_NAME = "3"
    private const val TENCENT_MAP_IMAGE_NAME = "sateTiles"
    private const val TENCENT_MAP_TERRAIN_NAME = "demTiles"
    private const val GOOGLE_MAP_VECTOR_NAME = "m"
    private const val GOOGLE_MAP_IMAGE_IMAGE = "s,h"
    private const val GOOGLE_MAP_TERRAIN_NAME = "t,r"
    private val ORIGIN = Point(-20037508.342787, 20037508.342787, SpatialReference.create(102113))
    private const val SRID = 102113
    private const val X_MIN = -22041257.773878
    private const val Y_MIN = -32673939.6727517
    private const val X_MAX = 22041257.773878
    private const val Y_MAX = 20851350.0432886
    private val SCALES = doubleArrayOf(591657527.591555,
            295828763.79577702, 147914381.89788899, 73957190.948944002,
            36978595.474472001, 18489297.737236001, 9244648.8686180003,
            4622324.4343090001, 2311162.217155, 1155581.108577, 577790.554289,
            288895.277144, 144447.638572, 72223.819286, 36111.909643,
            18055.954822, 9027.9774109999998, 4513.9887049999998, 2256.994353,
            1128.4971760000001)
    private val RESOLUTIONS = doubleArrayOf(156543.03392800014,
            78271.516963999937, 39135.758482000092, 19567.879240999919,
            9783.9396204999593, 4891.9698102499797, 2445.9849051249898,
            1222.9924525624949, 611.49622628138, 305.748113140558,
            152.874056570411, 76.4370282850732, 38.2185141425366,
            19.1092570712683, 9.55462853563415, 4.7773142679493699,
            2.3886571339746849, 1.1943285668550503, 0.59716428355981721,
            0.29858214164761665)

    fun getLayerInfo(layerType: Int): RxMapLayerInfo {
        val layerInfo = RxMapLayerInfo()
        layerInfo.layerType = layerType
        handleLayerInfo(layerInfo)
        when (layerType) {
            RxMapLayerTypes.AMAP_VECTOR -> {
                layerInfo.layerName = AMAP_VECTOR_NAME
                layerInfo.cachePathName = "AMAP_VECTOR"
            }
            RxMapLayerTypes.AMAP_IMAGE -> {
                layerInfo.layerName = AMAP_IMAGE_NAME
                layerInfo.cachePathName = "AMAP_IMAGE"
            }
            RxMapLayerTypes.AMAP_ROAD -> {
                layerInfo.layerName = AMAP_ROAD_NAME
                layerInfo.cachePathName = "AMAP_ROAD"
            }
            RxMapLayerTypes.AMAP_TRAFFIC -> layerInfo.cachePathName = "AMAP_TRAFFIC"
            RxMapLayerTypes.BAIDU_MAP_VECTOR -> {
                layerInfo.layerName = BAIDU_MAP_VECTOR_NAME
                layerInfo.cachePathName = "BAIDU_MAP_VECTOR"
                layerInfo.minZoomLevel = 3
                layerInfo.maxZoomLevel = 19
            }
            RxMapLayerTypes.BAIDU_MAP_IMAGE -> {
                layerInfo.cachePathName = "BAIDU_MAP_IMAGE"
                layerInfo.minZoomLevel = 3
                layerInfo.maxZoomLevel = 19
            }
            RxMapLayerTypes.BAIDU_MAP_ROAD -> {
                layerInfo.layerName = BAIDU_MAP_ROAD_NAME
                layerInfo.cachePathName = "BAIDU_MAP_ROAD"
                layerInfo.minZoomLevel = 3
                layerInfo.maxZoomLevel = 19
            }
            RxMapLayerTypes.BAIDU_MAP_TRAFFIC -> {
                layerInfo.cachePathName = "BAIDU_MAP_TRAFFIC"
                layerInfo.minZoomLevel = 3
                layerInfo.maxZoomLevel = 19
            }
            RxMapLayerTypes.TENCENT_MAP_VECTOR -> {
                layerInfo.layerName = TENCENT_MAP_VECTOR_NAME
                layerInfo.cachePathName = "TENCENT_MAP_VECTOR"
            }
            RxMapLayerTypes.TENCENT_MAP_VECTOR_NIGHT -> {
                layerInfo.layerName = TENCENT_MAP_VECTOR_NIGHT_NAME
                layerInfo.cachePathName = "TENCENT_MAP_VECTOR_NIGHT"
            }
            RxMapLayerTypes.TENCENT_MAP_IMAGE -> {
                layerInfo.layerName = TENCENT_MAP_IMAGE_NAME
                layerInfo.cachePathName = "TENCENT_MAP_IMAGE"
            }
            RxMapLayerTypes.TENCENT_MAP_TERRAIN -> {
                layerInfo.layerName = TENCENT_MAP_TERRAIN_NAME
                layerInfo.cachePathName = "TENCENT_MAP_TERRAIN"
            }
            RxMapLayerTypes.TENCENT_MAP_ROAD -> {
                layerInfo.layerName = TENCENT_MAP_ROAD_NAME
                layerInfo.cachePathName = "TENCENT_MAP_ROAD"
            }
            RxMapLayerTypes.GOOGLE_MAP_VECTOR -> {
                layerInfo.layerName = GOOGLE_MAP_VECTOR_NAME
                layerInfo.cachePathName = "GOOGLE_MAP_VECTOR"
                layerInfo.maxZoomLevel = 21
            }
            RxMapLayerTypes.GOOGLE_MAP_IMAGE -> {
                layerInfo.layerName = GOOGLE_MAP_IMAGE_IMAGE
                layerInfo.cachePathName = "GOOGLE_MAP_IMAGE"
                layerInfo.maxZoomLevel = 21
            }
            RxMapLayerTypes.GOOGLE_MAP_TERRAIN -> {
                layerInfo.layerName = GOOGLE_MAP_TERRAIN_NAME
                layerInfo.cachePathName = "GOOGLE_MAP_TERRAIN"
                layerInfo.maxZoomLevel = 21
            }
            else -> {
            }
        }
        return layerInfo
    }

    @JvmStatic
    fun getLayerUrl(layerInfo: RxMapLayerInfo, level: Int, col: Int, row: Int): String? {
        var row = row
        var layerUrl: String? = null
        when (layerInfo.layerType) {
            RxMapLayerTypes.AMAP_VECTOR, RxMapLayerTypes.AMAP_IMAGE, RxMapLayerTypes.AMAP_ROAD -> layerUrl = ("http://webst0" + ((col + row) % 4 + 1) + ".is.autonavi.com/appmaptile?style="
                    + layerInfo.layerName
                    + "&x=" + col + "&y=" + row + "&z=" + level)
            RxMapLayerTypes.AMAP_TRAFFIC -> {
                val calendar = Calendar.getInstance()
                calendar.timeZone = TimeZone.getTimeZone("GMT+8:00")
                val day = calendar[Calendar.DAY_OF_WEEK]
                val hh = calendar[Calendar.HOUR_OF_DAY]
                val mm = calendar[Calendar.MINUTE]
                layerUrl = ("http://history.traffic.amap.com/traffic?type=2"
                        + "&day=" + day + "&hh=" + hh + "&mm=" + mm
                        + "&x=" + col + "&y=" + row + "&z=" + level)
            }
            RxMapLayerTypes.BAIDU_MAP_VECTOR, RxMapLayerTypes.BAIDU_MAP_ROAD -> {
                val offsetV = Math.pow(2.0, level - 1.toDouble()).toInt()
                layerUrl = ("http://online" + ((col + row) % 8 + 1) + ".map.bdimg.com/onlinelabel/?qt=tile"
                        + "&x=" + (col - offsetV) + "&y=" + (offsetV - row - 1) + "&z=" + level
                        + "&styles=" + layerInfo.layerName)
            }
            RxMapLayerTypes.BAIDU_MAP_IMAGE -> {
                val offsetI = Math.pow(2.0, level - 1.toDouble()).toInt()
                layerUrl = ("http://shangetu" + ((col + row) % 8 + 1) + ".map.bdimg.com/it/u="
                        + "x=" + (col - offsetI) + ";y=" + (offsetI - row - 1) + ";z=" + level
                        + ";v=009;type=sate&fm=46")
            }
            RxMapLayerTypes.BAIDU_MAP_TRAFFIC -> {
                val offsetT = Math.pow(2.0, level - 1.toDouble()).toInt()
                layerUrl = ("http://its.map.baidu.com:8002/traffic/TrafficTileService?"
                        + "level=" + level + "&x=" + (col - offsetT) + "&y=" + (offsetT - row - 1)
                        + "&time=" + System.currentTimeMillis())
            }
            RxMapLayerTypes.TENCENT_MAP_VECTOR, RxMapLayerTypes.TENCENT_MAP_VECTOR_NIGHT, RxMapLayerTypes.TENCENT_MAP_ROAD -> {
                row = Math.pow(2.0, level.toDouble()).toInt() - 1 - row
                layerUrl = ("http://rt" + col % 4 + ".map.gtimg.com/tile?"
                        + "z=" + level + "&x=" + col + "&y=" + row
                        + "&type=vector&styleid=" + layerInfo.layerName)
            }
            RxMapLayerTypes.TENCENT_MAP_IMAGE, RxMapLayerTypes.TENCENT_MAP_TERRAIN -> {
                row = Math.pow(2.0, level.toDouble()).toInt() - 1 - row
                layerUrl = ("http://p" + col % 4 + ".map.gtimg.com/"
                        + layerInfo.layerName
                        + "/" + level + "/" + Math.floor(col / 16.toDouble()).toInt() + "/" + Math.floor(row / 16.toDouble()).toInt()
                        + "/" + col + "_" + row + ".jpg")
            }
            RxMapLayerTypes.GOOGLE_MAP_VECTOR, RxMapLayerTypes.GOOGLE_MAP_IMAGE, RxMapLayerTypes.GOOGLE_MAP_TERRAIN -> {
                row = Math.pow(2.0, level.toDouble()).toInt() - 1 - row
                layerUrl = ("http://mt" + col % 4 + ".google.cn/vt/lyrs="
                        + layerInfo.layerName + "&hl=zh-CN&gl=cn&"
                        + "x=" + col + "&" + "y=" + row + "&" + "z=" + level)
            }
            else -> {
            }
        }
        return layerUrl
    }

    private fun handleLayerInfo(layerInfo: RxMapLayerInfo) {
        layerInfo.origin = ORIGIN
        layerInfo.scales = SCALES
        layerInfo.srid = SRID
        layerInfo.setxMin(X_MIN)
        layerInfo.setyMin(Y_MIN)
        layerInfo.setxMax(X_MAX)
        layerInfo.setyMax(Y_MAX)
        layerInfo.resolutions = RESOLUTIONS
    }
}