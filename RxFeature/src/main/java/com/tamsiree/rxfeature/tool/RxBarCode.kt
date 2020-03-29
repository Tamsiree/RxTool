package com.tamsiree.rxfeature.tool

import android.graphics.Bitmap
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

/**
 * @author tamsiree
 * @date 2017/2/17
 */
object RxBarCode {
    /**
     * 获取建造者
     *
     * @param text 样式字符串文本
     * @return [RxBarCode.Builder]
     */
    fun builder(text: CharSequence): Builder {
        return Builder(text)
    }

    //----------------------------------------------------------------------------------------------以下为生成二维码算法
    fun createBarCode(content: CharSequence, BAR_WIDTH: Int, BAR_HEIGHT: Int, backgroundColor: Int, codeColor: Int): Bitmap {
        /**
         * 条形码的编码类型
         */
        val barcodeFormat = BarcodeFormat.CODE_128
        val writer = MultiFormatWriter()
        var result: BitMatrix? = null
        try {
            result = writer.encode(content.toString() + "", barcodeFormat, BAR_WIDTH, BAR_HEIGHT, null)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        val width = result!!.width
        val height = result.height
        val pixels = IntArray(width * height)
        // All are 0, or black, by default
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (result[x, y]) codeColor else backgroundColor
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }
    //----------------------------------------------------------------------------------------------生成条形码开始
    /**
     * 生成条形码
     *
     * @param contents      需要生成的内容
     * @param desiredWidth  生成条形码的宽带
     * @param desiredHeight 生成条形码的高度
     * @return
     */
    /**
     * 生成条形码
     * desiredWidth  生成条形码的宽带
     * desiredHeight 生成条形码的高度
     *
     * @param contents 需要生成的内容
     * @return 条形码的Bitmap
     */
    @JvmOverloads
    fun createBarCode(contents: String, desiredWidth: Int = 1000, desiredHeight: Int = 300): Bitmap {
        return createBarCode(contents, desiredWidth, desiredHeight, -0x1000000, -0x1)
    }

    fun createBarCode(content: String, codeWidth: Int, codeHeight: Int, iv_code: ImageView) {
        iv_code.setImageBitmap(createBarCode(content, codeWidth, codeHeight))
    }

    fun createBarCode(content: String, iv_code: ImageView) {
        iv_code.setImageBitmap(createBarCode(content))
    } //==============================================================================================生成条形码结束

    class Builder(private val content: CharSequence) {
        private var backgroundColor = -0x1
        private var codeColor = -0x1000000
        private var codeWidth = 1000
        private var codeHeight = 300
        fun backColor(backgroundColor: Int): Builder {
            this.backgroundColor = backgroundColor
            return this
        }

        fun codeColor(codeColor: Int): Builder {
            this.codeColor = codeColor
            return this
        }

        fun codeWidth(codeWidth: Int): Builder {
            this.codeWidth = codeWidth
            return this
        }

        fun codeHeight(codeHeight: Int): Builder {
            this.codeHeight = codeHeight
            return this
        }

        fun into(imageView: ImageView?): Bitmap {
            val bitmap = createBarCode(content, codeWidth, codeHeight, backgroundColor, codeColor)
            imageView?.setImageBitmap(bitmap)
            return bitmap
        }

    }
}