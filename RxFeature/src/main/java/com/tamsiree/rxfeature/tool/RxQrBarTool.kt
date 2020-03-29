package com.tamsiree.rxfeature.tool

import android.graphics.Bitmap
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.tamsiree.rxfeature.scaner.BitmapLuminanceSource
import com.tamsiree.rxkit.RxImageTool.zoomBitmap
import java.util.*

/**
 * @author Tamsiree
 * @date 2017/10/11
 */
object RxQrBarTool {
    /**
     * 解析图片中的 二维码 或者 条形码
     *
     * @param photo 待解析的图片
     * @return Result 解析结果，解析识别时返回NULL
     */
    @JvmStatic
    fun decodeFromPhoto(photo: Bitmap?): Result? {
        var rawResult: Result? = null
        if (photo != null) {
            val smallBitmap = zoomBitmap(photo, photo.width / 2, photo.height / 2) // 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
            photo.recycle() // 释放原始图片占用的内存，防止out of memory异常发生
            val multiFormatReader = MultiFormatReader()

            // 解码的参数
            val hints = Hashtable<DecodeHintType, Any?>(2)
            // 可以解析的编码类型
            var decodeFormats = Vector<BarcodeFormat?>()
            if (decodeFormats.isEmpty()) {
                decodeFormats = Vector()
                val PRODUCT_FORMATS = Vector<BarcodeFormat?>(5)
                PRODUCT_FORMATS.add(BarcodeFormat.UPC_A)
                PRODUCT_FORMATS.add(BarcodeFormat.UPC_E)
                PRODUCT_FORMATS.add(BarcodeFormat.EAN_13)
                PRODUCT_FORMATS.add(BarcodeFormat.EAN_8)
                // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
                val ONE_D_FORMATS = Vector<BarcodeFormat?>(PRODUCT_FORMATS.size + 4)
                ONE_D_FORMATS.addAll(PRODUCT_FORMATS)
                ONE_D_FORMATS.add(BarcodeFormat.CODE_39)
                ONE_D_FORMATS.add(BarcodeFormat.CODE_93)
                ONE_D_FORMATS.add(BarcodeFormat.CODE_128)
                ONE_D_FORMATS.add(BarcodeFormat.ITF)
                val QR_CODE_FORMATS = Vector<BarcodeFormat?>(1)
                QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE)
                val DATA_MATRIX_FORMATS = Vector<BarcodeFormat?>(1)
                DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX)

                // 这里设置可扫描的类型，我这里选择了都支持
                decodeFormats.addAll(ONE_D_FORMATS)
                decodeFormats.addAll(QR_CODE_FORMATS)
                decodeFormats.addAll(DATA_MATRIX_FORMATS)
            }
            hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats
            // 设置继续的字符编码格式为UTF8
            // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
            // 设置解析配置参数
            multiFormatReader.setHints(hints)

            // 开始对图像资源解码
            try {
                rawResult = multiFormatReader.decodeWithState(BinaryBitmap(HybridBinarizer(BitmapLuminanceSource(smallBitmap))))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return rawResult
    }
}