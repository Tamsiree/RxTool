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
package com.tamsiree.rxarcgiskit.layer;

import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.data.TileKey;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.ImageTiledLayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RxMapLayer extends ImageTiledLayer {

    private RxMapLayerInfo layerInfo;
    private String cachePath;

    public RxMapLayer(RxMapLayerInfo layerInfo, TileInfo tileInfo, Envelope fullExtent) {
        super(tileInfo, fullExtent);
        this.layerInfo = layerInfo;
        this.cachePath = null;
    }

    public RxMapLayer(RxMapLayerInfo layerInfo, String cachePath, TileInfo tileInfo, Envelope fullExtent) {
        super(tileInfo, fullExtent);
        this.layerInfo = layerInfo;
        this.cachePath = cachePath + "/" + layerInfo.getCachePathName() + "/";
    }


    @Override
    protected byte[] getTile(TileKey tileKey) {
        int level = tileKey.getLevel();
        int col = tileKey.getColumn();
        int row = tileKey.getRow();

        if (level > layerInfo.getMaxZoomLevel() || level < layerInfo.getMinZoomLevel()) {
            return new byte[0];
        }
        byte[] bytes = null;
        if (cachePath != null) {
            bytes = getOfflineCacheFile(cachePath, level, col, row);
        }
        if (bytes == null) {
            String layerUrl = RxLayerInfoFactory.getLayerUrl(layerInfo, level, col, row);

            try {
                URL url = new URL(layerUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                //获取服务器返回回来的流
                InputStream is = conn.getInputStream();
                bytes = getBytes(is);
                if (cachePath != null) {
                    bytes = AddOfflineCacheFile(cachePath, level, col, row, bytes);
                }
            } catch (Exception e) {
                bytes = new byte[0];
            }
        }
        return bytes;
    }

    private byte[] getBytes(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        is.close();
        bos.flush();
        byte[] result = bos.toByteArray();
        System.out.println(new String(result));
        return result;
    }

    // 将图片保存到本地 目录结构可以随便定义 只要你找得到对应的图片
    private byte[] AddOfflineCacheFile(String cachePath, int level, int col, int row, byte[] bytes) {

        File file = new File(cachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File levelfile = new File(cachePath + "/" + level);
        if (!levelfile.exists()) {
            levelfile.mkdirs();
        }
        File rowfile = new File(cachePath + "/" + level + "/" + col + "x" + row
                + ".cmap");
        if (!rowfile.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(rowfile);
                out.write(bytes);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return bytes;

    }

    // 从本地获取图片
    private byte[] getOfflineCacheFile(String cachePath, int level, int col, int row) {
        byte[] bytes = null;
        File rowfile = new File(cachePath + "/" + level + "/" + col + "x" + row
                + ".cmap");
        if (rowfile.exists()) {
            try {
                bytes = CopySdcardbytes(rowfile);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            bytes = null;
        }
        return bytes;
    }

    // 读取本地图片流
    private byte[] CopySdcardbytes(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] temp = new byte[1024];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        in.close();
        byte[] bytes = out.toByteArray();
        return bytes;
    }


}
