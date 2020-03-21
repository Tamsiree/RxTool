package com.tamsiree.rxkit.photomagic

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import com.tamsiree.rxkit.photomagic.Checker.isJPG
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

/**
 * Responsible for starting compress and managing active and cached resources.
 */
internal class Engine(srcImg: String, tagImg: File) {
    private var srcExif: ExifInterface? = null
    private val srcImg: String
    private val tagImg: File
    private var srcWidth: Int
    private var srcHeight: Int
    private fun computeSize(): Int {
        srcWidth = if (srcWidth % 2 == 1) srcWidth + 1 else srcWidth
        srcHeight = if (srcHeight % 2 == 1) srcHeight + 1 else srcHeight
        val longSide = max(srcWidth, srcHeight)
        val shortSide = min(srcWidth, srcHeight)
        val scale = shortSide.toFloat() / longSide
        return if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                2
            } else if (longSide in 1664..4989) {
                4
            } else if (longSide in 4991..10239) {
                8
            } else {
                if (longSide / 1280 == 0) 1 + 1 else longSide / 1280 + 1
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            if (longSide / 1280 == 0) 1 + 1 else longSide / 1280 + 1
        } else {
            ceil(longSide / (1280.0 / scale)).toInt() + 1
        }
    }

    private fun rotatingImage(bitmap: Bitmap): Bitmap {
        if (srcExif == null) {
            return bitmap
        }
        val matrix = Matrix()
        var angle = 0
        when (srcExif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> angle = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> angle = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> angle = 270
            else -> {
            }
        }
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    @Throws(IOException::class)
    fun compress(): File {
        val options = BitmapFactory.Options()
        options.inSampleSize = computeSize()
        var tagBitmap = BitmapFactory.decodeFile(srcImg, options)
        val stream = ByteArrayOutputStream()
        tagBitmap = rotatingImage(tagBitmap)
        tagBitmap.compress(Bitmap.CompressFormat.JPEG, 35, stream)
        tagBitmap.recycle()
        val fos = FileOutputStream(tagImg)
        fos.write(stream.toByteArray())
        fos.flush()
        fos.close()
        stream.close()
        return tagImg
    }

    init {
        if (isJPG(srcImg)) {
            srcExif = ExifInterface(srcImg)
        }
        this.tagImg = tagImg
        this.srcImg = srcImg
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        options.inSampleSize = 1
        BitmapFactory.decodeFile(srcImg, options)
        srcWidth = options.outWidth
        srcHeight = options.outHeight
    }
}