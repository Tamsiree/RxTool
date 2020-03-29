package com.tamsiree.rxui.view.scaleimage.decoder

import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.*
import android.net.Uri
import android.text.TextUtils
import java.io.InputStream

/**
 * @author tamsiree
 * Default implementation of [com.tamsiree.rxui.view.scaleimage.decoder.ImageRegionDecoder]
 * using Android's [BitmapRegionDecoder], based on the Skia library. This
 * works well in most circumstances and has reasonable performance due to the cached decoder instance,
 * however it has some problems with grayscale, indexed and CMYK images.
 */
class SkiaImageRegionDecoder : ImageRegionDecoder {
    private val decoderLock = Any()
    private var decoder: BitmapRegionDecoder? = null

    @Throws(Exception::class)
    override fun init(context: Context?, uri: Uri?): Point? {
        val uriString = uri.toString()
        if (uriString.startsWith(RESOURCE_PREFIX)) {
            val res: Resources
            val packageName = uri!!.authority
            res = if (context!!.packageName == packageName) {
                context.resources
            } else {
                val pm = context.packageManager
                pm.getResourcesForApplication(packageName)
            }
            var id = 0
            val segments = uri.pathSegments
            val size = segments.size
            if (size == 2 && segments[0] == "drawable") {
                val resName = segments[1]
                id = res.getIdentifier(resName, "drawable", packageName)
            } else if (size == 1 && TextUtils.isDigitsOnly(segments[0])) {
                try {
                    id = segments[0].toInt()
                } catch (ignored: NumberFormatException) {
                }
            }
            decoder = BitmapRegionDecoder.newInstance(context.resources.openRawResource(id), false)
        } else if (uriString.startsWith(ASSET_PREFIX)) {
            val assetName = uriString.substring(ASSET_PREFIX.length)
            decoder = BitmapRegionDecoder.newInstance(context!!.assets.open(assetName, AssetManager.ACCESS_RANDOM), false)
        } else if (uriString.startsWith(FILE_PREFIX)) {
            decoder = BitmapRegionDecoder.newInstance(uriString.substring(FILE_PREFIX.length), false)
        } else {
            var inputStream: InputStream? = null
            try {
                val contentResolver = context!!.contentResolver
                inputStream = contentResolver.openInputStream(uri!!)
                decoder = BitmapRegionDecoder.newInstance(inputStream, false)
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (e: Exception) {
                    }
                }
            }
        }
        return Point(decoder!!.width, decoder!!.height)
    }

    override fun decodeRegion(sRect: Rect?, sampleSize: Int): Bitmap? {
        synchronized(decoderLock) {
            val options = BitmapFactory.Options()
            options.inSampleSize = sampleSize
            options.inPreferredConfig = Bitmap.Config.RGB_565
            return decoder!!.decodeRegion(sRect, options)
                    ?: throw RuntimeException("Skia image decoder returned null bitmap - image format may not be supported")
        }
    }

    override val isReady: Boolean
        get() = decoder != null && !decoder!!.isRecycled

    override fun recycle() {
        decoder!!.recycle()
    }

    companion object {
        private const val FILE_PREFIX = "file://"
        private const val ASSET_PREFIX = "$FILE_PREFIX/android_asset/"
        private const val RESOURCE_PREFIX = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
    }
}