package com.tamsiree.rxkit.photomagic

import android.text.TextUtils
import java.io.File
import java.util.*

internal object Checker {
    private val format: MutableList<String> = ArrayList()
    private const val JPG = "jpg"
    private const val JPEG = "jpeg"
    private const val PNG = "png"
    private const val WEBP = "webp"
    private const val GIF = "gif"

    @JvmStatic
    fun isImage(path: String): Boolean {
        if (TextUtils.isEmpty(path)) {
            return false
        }
        val suffix = path.substring(path.lastIndexOf(".") + 1)
        return format.contains(suffix.toLowerCase(Locale.ROOT))
    }

    @JvmStatic
    fun isJPG(path: String): Boolean {
        if (TextUtils.isEmpty(path)) {
            return false
        }
        val suffix = path.substring(path.lastIndexOf(".")).toLowerCase(Locale.ROOT)
        return suffix.contains(JPG) || suffix.contains(JPEG)
    }

    @JvmStatic
    fun checkSuffix(path: String): String {
        return if (TextUtils.isEmpty(path)) {
            ".jpg"
        } else path.substring(path.lastIndexOf("."))
    }

    @JvmStatic
    fun isNeedCompress(leastCompressSize: Int, path: String): Boolean {
        if (leastCompressSize > 0) {
            val source = File(path)
            return if (!source.exists()) {
                false
            } else source.length() > leastCompressSize shl 10
        }
        return true
    }

    init {
        format.add(JPG)
        format.add(JPEG)
        format.add(PNG)
        format.add(WEBP)
        format.add(GIF)
    }
}