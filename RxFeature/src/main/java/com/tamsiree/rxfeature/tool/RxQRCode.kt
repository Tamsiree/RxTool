package com.tamsiree.rxfeature.tool

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.tamsiree.rxkit.RxImageTool.drawable2Bitmap
import java.util.*

/**
 * @author tamsiree
 * @date 2017/2/17.
 */
object RxQRCode {
    /**
     * 获取建造者
     *
     * @param text 样式字符串文本
     * @return [RxQRCode.Builder]
     */
    fun builder(text: CharSequence): Builder {
        return Builder(text)
    }

    //----------------------------------------------------------------------------------------------以下为生成二维码算法
    @JvmOverloads
    fun creatQRCode(content: CharSequence?, QR_WIDTH: Int = 800, QR_HEIGHT: Int = 800, border: Int = 1, backgroundColor: Int = -0x1, codeColor: Int = -0x1000000): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            // 判断URL合法性
            if (content == null || "" == content || content.length < 1) {
                return null
            }
            val hints = Hashtable<EncodeHintType, String?>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            hints[EncodeHintType.MARGIN] = border.toString() + ""
            // 图像数据转换，使用了矩阵转换
            val bitMatrix = QRCodeWriter().encode(content.toString() + "", BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints)
            val pixels = IntArray(QR_WIDTH * QR_HEIGHT)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until QR_HEIGHT) {
                for (x in 0 until QR_WIDTH) {
                    if (bitMatrix[x, y]) {
                        pixels[y * QR_WIDTH + x] = codeColor
                    } else {
                        pixels[y * QR_WIDTH + x] = backgroundColor
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun creatQRCode(content: CharSequence?, logo: Bitmap?): Bitmap? {
        return creatQRCode(content, 800, 800, 1, -0x1, -0x1000000, logo)
    }

    //==============================================================================================二维码算法结束
    fun creatQRCode(content: CharSequence?, QR_WIDTH: Int, QR_HEIGHT: Int, border: Int, backgroundColor: Int, codeColor: Int, logo: Bitmap?): Bitmap? {
        try {
            // 判断URL合法性
            if (content == null || "" == content || content.length < 1) {
                return null
            }
            val scaleLogo = getScaleLogo(logo, QR_WIDTH, QR_HEIGHT)
            var offsetX = QR_WIDTH / 2
            var offsetY = QR_HEIGHT / 2
            var scaleWidth = 0
            var scaleHeight = 0
            if (scaleLogo != null) {
                scaleWidth = scaleLogo.width
                scaleHeight = scaleLogo.height
                offsetX = (QR_WIDTH - scaleWidth) / 2
                offsetY = (QR_HEIGHT - scaleHeight) / 2
            }
            val hints = Hashtable<EncodeHintType, Any?>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            //设置空白边距的宽度
            hints[EncodeHintType.MARGIN] = border
            //容错级别
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            val bitMatrix = QRCodeWriter().encode(content.toString(), BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints)
            val pixels = IntArray(QR_WIDTH * QR_HEIGHT)
            for (y in 0 until QR_HEIGHT) {
                for (x in 0 until QR_WIDTH) {
                    if (x >= offsetX && x < offsetX + scaleWidth && y >= offsetY && y < offsetY + scaleHeight) {
                        var pixel = scaleLogo!!.getPixel(x - offsetX, y - offsetY)
                        if (pixel == 0) {
                            pixel = if (bitMatrix[x, y]) {
                                codeColor
                            } else {
                                backgroundColor
                            }
                        }
                        pixels[y * QR_WIDTH + x] = pixel
                    } else {
                        if (bitMatrix[x, y]) {
                            pixels[y * QR_WIDTH + x] = codeColor
                        } else {
                            pixels[y * QR_WIDTH + x] = backgroundColor
                        }
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT)
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * @param content   需要转换的字符串
     * @param QR_WIDTH  二维码的宽度
     * @param QR_HEIGHT 二维码的高度
     * @param iv_code   图片空间
     */
    fun createQRCode(content: String?, QR_WIDTH: Int, QR_HEIGHT: Int, iv_code: ImageView) {
        iv_code.setImageBitmap(creatQRCode(content, QR_WIDTH, QR_HEIGHT))
    }

    /**
     * QR_WIDTH  二维码的宽度
     * QR_HEIGHT 二维码的高度
     *
     * @param content 需要转换的字符串
     * @param iv_code 图片空间
     */
    fun createQRCode(content: String?, iv_code: ImageView) {
        iv_code.setImageBitmap(creatQRCode(content))
    }

    private fun getScaleLogo(logo: Bitmap?, w: Int, h: Int): Bitmap? {
        if (logo == null) return null
        val matrix = Matrix()
        val scaleFactor = Math.min(w * 1.0f / 5 / logo.width, h * 1.0f / 5 / logo.height)
        matrix.postScale(scaleFactor, scaleFactor)
        return Bitmap.createBitmap(logo, 0, 0, logo.width, logo.height, matrix, true)
    }

    class Builder(private val content: CharSequence) {
        private var backgroundColor = -0x1
        private var codeColor = -0x1000000
        private var codeSide = 800
        private var codeBorder = 1
        private var bitmapLogo: Bitmap? = null
        fun backColor(backgroundColor: Int): Builder {
            this.backgroundColor = backgroundColor
            return this
        }

        fun codeColor(codeColor: Int): Builder {
            this.codeColor = codeColor
            return this
        }

        fun codeSide(codeSide: Int): Builder {
            this.codeSide = codeSide
            return this
        }

        fun codeLogo(bitmap: Bitmap?): Builder {
            bitmapLogo = bitmap
            return this
        }

        fun codeLogo(drawable: Drawable?): Builder {
            bitmapLogo = drawable2Bitmap(drawable!!)
            return this
        }

        fun codeBorder(codeBorder: Int): Builder {
            this.codeBorder = codeBorder
            return this
        }

        fun into(imageView: ImageView?): Bitmap? {
            val bitmap = creatQRCode(content, codeSide, codeSide, codeBorder, backgroundColor, codeColor, bitmapLogo)
            imageView?.setImageBitmap(bitmap)
            return bitmap
        }

    }
}