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

package com.vondear.rxarcgiskit.model;

import java.io.Serializable;

/**
 * Class which serves as the model in an MVC architecture for setting and getting information
 * related to MapPreviews
 */
public class MapPreview implements Serializable {

    private int mMapNum;
    private String mTitle;
    private boolean mTransportNetwork = false;
    private boolean mGeocoding = false;
    private String mDesc;
    private byte[] mThumbnailByteStream;

    public int getMapNum() {
        return mMapNum;
    }

    public void setMapNum(int mapNum) {
        mMapNum = mapNum;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mapTitle) {
        mTitle = mapTitle;
    }

    public boolean hasTransportNetwork() {
        return mTransportNetwork;
    }

    public void setTransportNetwork(boolean transportNetwork) {
        mTransportNetwork = transportNetwork;
    }

    public boolean hasGeocoding() {
        return mGeocoding;
    }

    public void setGeocoding(boolean geocoding) {
        mGeocoding = geocoding;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String mapInfo) {
        mDesc = mapInfo;
    }

    public byte[] getThumbnailByteStream() {
        return mThumbnailByteStream;
    }

    public void setThumbnailByteStream(byte[] thumbnailByteStream) {
        mThumbnailByteStream = thumbnailByteStream;
    }
}