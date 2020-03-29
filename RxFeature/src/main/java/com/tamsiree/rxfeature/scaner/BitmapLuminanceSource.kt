package com.tamsiree.rxfeature.scaner

import android.graphics.Bitmap
import com.google.zxing.LuminanceSource

/**
 * @author Tamsiree
 * @date 16/7/27
 * 自定义解析Bitmap LuminanceSource
 */
class BitmapLuminanceSource(bitmap: Bitmap) : LuminanceSource(bitmap.width, bitmap.height) {
    private val bitmapPixels: ByteArray
    override fun getMatrix(): ByteArray {
        // 返回我们生成好的像素数据
        return bitmapPixels
    }

    override fun getRow(y: Int, row: ByteArray): ByteArray {
        // 这里要得到指定行的像素数据
        System.arraycopy(bitmapPixels, y * width, row, 0, width)
        return row
    }

    init {

        // 首先，要取得该图片的像素数组内容
        val data = IntArray(bitmap.width * bitmap.height)
        bitmapPixels = ByteArray(bitmap.width * bitmap.height)
        bitmap.getPixels(data, 0, width, 0, 0, width, height)

        // 将int数组转换为byte数组，也就是取像素值中蓝色值部分作为辨析内容
        for (i in data.indices) {
            bitmapPixels[i] = data[i].toByte()
        }
    }
}