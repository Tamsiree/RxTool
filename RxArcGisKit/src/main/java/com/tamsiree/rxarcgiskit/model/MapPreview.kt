/* Copyright 2017 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tamsiree.rxarcgiskit.model

import java.io.Serializable

/**
 * Class which serves as the model in an MVC architecture for setting and getting information
 * related to MapPreviews
 */
class MapPreview : Serializable {
    var mapNum = 0
    var title: String? = null
    private var mTransportNetwork = false
    private var mGeocoding = false
    var desc: String? = null
    var thumbnailByteStream: ByteArray? = null

    fun hasTransportNetwork(): Boolean {
        return mTransportNetwork
    }

    fun setTransportNetwork(transportNetwork: Boolean) {
        mTransportNetwork = transportNetwork
    }

    fun hasGeocoding(): Boolean {
        return mGeocoding
    }

    fun setGeocoding(geocoding: Boolean) {
        mGeocoding = geocoding
    }

}