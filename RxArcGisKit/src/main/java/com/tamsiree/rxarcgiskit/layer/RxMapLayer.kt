package com.tamsiree.rxarcgiskit.layer

import com.esri.arcgisruntime.arcgisservices.TileInfo
import com.esri.arcgisruntime.data.TileKey
import com.esri.arcgisruntime.geometry.Envelope
import com.esri.arcgisruntime.layers.ImageTiledLayer
import com.tamsiree.rxarcgiskit.layer.RxLayerInfoFactory.getLayerUrl
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class RxMapLayer : ImageTiledLayer {
    private var layerInfo: RxMapLayerInfo
    private var cachePath: String?

    constructor(layerInfo: RxMapLayerInfo, tileInfo: TileInfo?, fullExtent: Envelope?) : super(tileInfo, fullExtent) {
        this.layerInfo = layerInfo
        cachePath = null
    }

    constructor(layerInfo: RxMapLayerInfo, cachePath: String, tileInfo: TileInfo?, fullExtent: Envelope?) : super(tileInfo, fullExtent) {
        this.layerInfo = layerInfo
        this.cachePath = cachePath + "/" + layerInfo.cachePathName + "/"
    }

    override fun getTile(tileKey: TileKey): ByteArray {
        val level = tileKey.level
        val col = tileKey.column
        val row = tileKey.row
        if (level > layerInfo.maxZoomLevel || level < layerInfo.minZoomLevel) {
            return ByteArray(0)
        }
        var bytes: ByteArray? = null
        if (cachePath != null) {
            bytes = getOfflineCacheFile(cachePath!!, level, col, row)
        }
        if (bytes == null) {
            val layerUrl = getLayerUrl(layerInfo, level, col, row)
            try {
                val url = URL(layerUrl)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 5000
                //获取服务器返回回来的流
                val `is` = conn.inputStream
                bytes = getBytes(`is`)
                if (cachePath != null) {
                    bytes = AddOfflineCacheFile(cachePath!!, level, col, row, bytes)
                }
            } catch (e: Exception) {
                bytes = ByteArray(0)
            }
        }
        return bytes!!
    }

    @Throws(Exception::class)
    private fun getBytes(`is`: InputStream): ByteArray {
        val bos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = 0
        while (`is`.read(buffer).also { len = it } != -1) {
            bos.write(buffer, 0, len)
        }
        `is`.close()
        bos.flush()
        val result = bos.toByteArray()
        println(String(result))
        return result
    }

    // 将图片保存到本地 目录结构可以随便定义 只要你找得到对应的图片
    private fun AddOfflineCacheFile(cachePath: String, level: Int, col: Int, row: Int, bytes: ByteArray?): ByteArray? {
        val file = File(cachePath)
        if (!file.exists()) {
            file.mkdirs()
        }
        val levelfile = File("$cachePath/$level")
        if (!levelfile.exists()) {
            levelfile.mkdirs()
        }
        val rowfile = File(cachePath + "/" + level + "/" + col + "x" + row
                + ".cmap")
        if (!rowfile.exists()) {
            try {
                val out = FileOutputStream(rowfile)
                out.write(bytes)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return bytes
    }

    // 从本地获取图片
    private fun getOfflineCacheFile(cachePath: String, level: Int, col: Int, row: Int): ByteArray? {
        var bytes: ByteArray? = null
        val rowfile = File(cachePath + "/" + level + "/" + col + "x" + row
                + ".cmap")
        if (rowfile.exists()) {
            try {
                bytes = CopySdcardbytes(rowfile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            bytes = null
        }
        return bytes
    }

    // 读取本地图片流
    @Throws(IOException::class)
    private fun CopySdcardbytes(file: File): ByteArray {
        val `in` = FileInputStream(file)
        val out = ByteArrayOutputStream(1024)
        val temp = ByteArray(1024)
        var size = 0
        while (`in`.read(temp).also { size = it } != -1) {
            out.write(temp, 0, size)
        }
        `in`.close()
        return out.toByteArray()
    }
}