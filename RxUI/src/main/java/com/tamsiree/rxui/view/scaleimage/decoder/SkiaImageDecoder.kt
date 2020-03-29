package com.tamsiree.rxui.view.scaleimage.decoder

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import java.io.InputStream

/**
 * @author tamsiree
 * Default implementation of [ImageDecoder]
 * using Android's [BitmapFactory], based on the Skia library. This
 * works well in most circumstances and has reasonable performance, however it has some problems
 * with grayscale, indexed and CMYK images.
 */
class SkiaImageDecoder : ImageDecoder {
    @Throws(Exception::class)
    override fun decode(context: Context?, uri: Uri?): Bitmap? {
        val uriString = uri.toString()
        val options = BitmapFactory.Options()
        val bitmap: Bitmap?
        options.inPreferredConfig = Bitmap.Config.RGB_565
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
            bitmap = BitmapFactory.decodeResource(context.resources, id, options)
        } else if (uriString.startsWith(ASSET_PREFIX)) {
            val assetName = uriString.substring(ASSET_PREFIX.length)
            bitmap = BitmapFactory.decodeStream(context!!.assets.open(assetName), null, options)
        } else if (uriString.startsWith(FILE_PREFIX)) {
            bitmap = BitmapFactory.decodeFile(uriString.substring(FILE_PREFIX.length), options)
        } else {
            var inputStream: InputStream? = null
            try {
                val contentResolver = context!!.contentResolver
                inputStream = contentResolver.openInputStream(uri!!)
                bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (e: Exception) {
                    }
                }
            }
        }
        if (bitmap == null) {
            throw RuntimeException("Skia image region decoder returned null bitmap - image format may not be supported")
        }
        return bitmap
    }

    companion object {
        private const val FILE_PREFIX = "file://"
        private const val ASSET_PREFIX = "$FILE_PREFIX/android_asset/"
        private const val RESOURCE_PREFIX = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
    }
}