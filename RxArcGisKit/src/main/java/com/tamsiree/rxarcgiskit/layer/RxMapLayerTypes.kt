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

interface RxMapLayerTypes {
    companion object {
        /**
         * 高德矢量图层（含路网，含注记）
         */
        const val AMAP_VECTOR = 101

        /**
         * 高德影像图层（不含路网，不含注记）
         */
        const val AMAP_IMAGE = 102

        /**
         * 高德路网图层（含路网，含注记）
         */
        const val AMAP_ROAD = 103

        /**
         * 高德实时交通图层
         */
        const val AMAP_TRAFFIC = 104

        /**
         * 百度矢量图层（含路网，含注记）
         */
        const val BAIDU_MAP_VECTOR = 201

        /**
         * 百度影像图层（不含路网，不含注记）
         */
        const val BAIDU_MAP_IMAGE = 202

        /**
         * 百度路网图层（含路网，含注记）
         */
        const val BAIDU_MAP_ROAD = 203

        /**
         * 百度实时交通图层
         */
        const val BAIDU_MAP_TRAFFIC = 204

        /**
         * 腾讯矢量图层（含路网，含注记）
         */
        const val TENCENT_MAP_VECTOR = 301

        /**
         * 腾讯矢量图层（夜间，含路网，含注记）
         */
        const val TENCENT_MAP_VECTOR_NIGHT = 305

        /**
         * 腾讯影像图层（不含路网，不含注记）
         */
        const val TENCENT_MAP_IMAGE = 302

        /**
         * 腾讯地形图层（不含路网，不含注记）
         */
        const val TENCENT_MAP_TERRAIN = 303

        /**
         * 腾讯路网图层（含路网，含注记）
         */
        const val TENCENT_MAP_ROAD = 304

        /**
         * 谷歌矢量地图服务
         */
        const val GOOGLE_MAP_VECTOR = 401

        /**
         * 谷歌影像地图服务
         */
        const val GOOGLE_MAP_IMAGE = 402

        /**
         * 谷歌地形地图服务
         */
        const val GOOGLE_MAP_TERRAIN = 403
    }
}