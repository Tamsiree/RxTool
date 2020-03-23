package com.tamsiree.rxkit

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.exifinterface.media.ExifInterface
import com.tamsiree.rxkit.RxDataTool.Companion.isNullString
import com.tamsiree.rxkit.RxFileTool.Companion.closeIO
import com.tamsiree.rxkit.RxFileTool.Companion.createOrExistsFile
import com.tamsiree.rxkit.RxFileTool.Companion.getFileByPath
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author tamsiree
 * @date 2016/1/24
 * 图像工具类
 */
object RxImageTool {
    /**
     * dip转px
     *
     * @param dpValue dp值
     * @return px值
     */
    @JvmStatic
    fun dip2px(dpValue: Float): Int {
        return dp2px(dpValue)
    }

    @JvmStatic
    fun dip2px(mContext: Context, dpValue: Float): Int {
        return dp2px(mContext, dpValue)
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    @JvmStatic
    fun dp2px(dpValue: Float): Int {
        return dp2px(RxTool.getContext(), dpValue)
    }

    @JvmStatic
    fun dp2px(mContext: Context, dpValue: Float): Int {
        val scale = mContext.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px转dip
     *
     * @param pxValue px值
     * @return dip值
     */
    @JvmStatic
    fun px2dip(pxValue: Float): Int {
        return px2dp(pxValue)
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    @JvmStatic
    fun px2dp(pxValue: Float): Int {
        return px2dp(RxTool.getContext(), pxValue)
    }

    @JvmStatic
    fun px2dp(mContext: Context, pxValue: Float): Int {
        val scale = mContext.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    @JvmStatic
    fun sp2px(spValue: Float): Int {
        val fontScale = RxTool.getContext().resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    @JvmStatic
    fun px2sp(pxValue: Float): Int {
        val fontScale = RxTool.getContext().resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
     *
     *
     * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
     *
     *
     * B.本地路径:url="file://mnt/sdcard/photo/image.png";
     *
     *
     * C.支持的图片格式 ,png, jpg,bmp,gif等等
     *
     * @param url
     * @return
     */
    @JvmStatic
    fun GetLocalOrNetBitmap(url: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var `in`: InputStream? = null
        var out: BufferedOutputStream? = null
        return try {
            `in` = BufferedInputStream(URL(url).openStream(), 1024)
            val dataStream = ByteArrayOutputStream()
            out = BufferedOutputStream(dataStream, 1024)
            copy(`in`, out)
            out.flush()
            var data = dataStream.toByteArray()
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            data = null
            bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @Throws(IOException::class)
    private fun copy(`in`: InputStream, out: OutputStream) {
        val b = ByteArray(1024)
        var read: Int
        while (`in`.read(b).also { read = it } != -1) {
            out.write(b, 0, read)
        }
    }

    @JvmStatic
    fun getColorByInt(colorInt: Int): Int {
        return colorInt or -16777216
    }

    /**
     * 修改颜色透明度
     *
     * @param color
     * @param alpha
     * @return
     */
    @JvmStatic
    fun changeColorAlpha(color: Int, alpha: Int): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    @JvmStatic
    fun getAlphaPercent(argb: Int): Float {
        return Color.alpha(argb) / 255f
    }

    @JvmStatic
    fun alphaValueAsInt(alpha: Float): Int {
        return Math.round(alpha * 255)
    }

    @JvmStatic
    fun adjustAlpha(alpha: Float, color: Int): Int {
        return alphaValueAsInt(alpha) shl 24 or (0x00ffffff and color)
    }

    @JvmStatic
    fun colorAtLightness(color: Int, lightness: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] = lightness
        return Color.HSVToColor(hsv)
    }

    @JvmStatic
    fun lightnessOfColor(color: Int): Float {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return hsv[2]
    }

    @JvmStatic
    fun getHexString(color: Int, showAlpha: Boolean): String {
        val base = if (showAlpha) -0x1 else 0xFFFFFF
        val format = if (showAlpha) "#%08X" else "#%06X"
        return String.format(format, base and color).toUpperCase()
    }

    /**
     * bitmap转byteArr
     *
     * @param bitmap bitmap对象
     * @param format 格式
     * @return 字节数组
     */
    @JvmStatic
    fun bitmap2Bytes(bitmap: Bitmap, format: CompressFormat?): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(format, 100, baos)
        return baos.toByteArray()
    }

    /**
     * byteArr转bitmap
     *
     * @param bytes 字节数组
     * @return bitmap对象
     */
    @JvmStatic
    fun bytes2Bitmap(bytes: ByteArray): Bitmap? {
        return if (bytes.size != 0) {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } else {
            null
        }
    }

    /**
     * drawable转bitmap
     *
     * @param drawable drawable对象
     * @return bitmap对象
     */
    @JvmStatic
    fun drawable2Bitmap(drawable: Drawable): Bitmap {
        // 取 drawable 的长宽
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight

        // 取 drawable 的颜色格式
        val config = if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        // 建立对应 bitmap
        val bitmap = Bitmap.createBitmap(w, h, config)
        // 建立对应 bitmap 的画布
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        // 把 drawable 内容画到画布中
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * bitmap转drawable
     *
     * @param res    resources对象
     * @param bitmap bitmap对象
     * @return drawable对象
     */
    @JvmStatic
    fun bitmap2Drawable(res: Resources?, bitmap: Bitmap?): Drawable {
        return BitmapDrawable(res, bitmap)
    }

    @JvmStatic
    fun bitmap2Drawable(bitmap: Bitmap?): Drawable {
        return BitmapDrawable(bitmap)
    }

    /**
     * drawable转byteArr
     *
     * @param drawable drawable对象
     * @param format   格式
     * @return 字节数组
     */
    @JvmStatic
    fun drawable2Bytes(drawable: Drawable, format: CompressFormat?): ByteArray {
        val bitmap = drawable2Bitmap(drawable)
        return bitmap2Bytes(bitmap, format)
    }

    /**
     * byteArr转drawable
     *
     * @param res   resources对象
     * @param bytes 字节数组
     * @return drawable对象
     */
    @JvmStatic
    fun bytes2Drawable(res: Resources?, bytes: ByteArray): Drawable {
        val bitmap = bytes2Bitmap(bytes)
        return bitmap2Drawable(res, bitmap)
    }

    @JvmStatic
    fun bytes2Drawable(bytes: ByteArray): Drawable {
        val bitmap = bytes2Bitmap(bytes)
        return bitmap2Drawable(bitmap)
    }

    /**
     * 计算采样大小
     *
     * @param options   选项
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return 采样大小
     */
    private fun calculateInSampleSize(options: BitmapFactory.Options, maxWidth: Int, maxHeight: Int): Int {
        if (maxWidth == 0 || maxHeight == 0) {
            return 1
        }
        var height = options.outHeight
        var width = options.outWidth
        var inSampleSize = 1
        while (1.let { height = height shr it; height } >= maxHeight && 1.let { width = width shr it; width } >= maxWidth) {
            inSampleSize = inSampleSize shl 1
        }
        return inSampleSize
    }

    /**
     * 计算inSampleSize
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private fun calculateInSampleSize2(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        var reqWidth = reqWidth
        var reqHeight = reqHeight
        val height = options.outHeight
        val width = options.outWidth
        val MIN_WIDTH = 100
        var inSampleSize = 1
        return if (width < MIN_WIDTH) {
            inSampleSize
        } else {
            var heightRatio: Int
            if (width > height && reqWidth < reqHeight || width < height && reqWidth > reqHeight) {
                heightRatio = reqWidth
                reqWidth = reqHeight
                reqHeight = heightRatio
            }
            if (height > reqHeight || width > reqWidth) {
                heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = Math.max(heightRatio, widthRatio)
            }
            inSampleSize
        }
    }

    /**
     * 获取bitmap
     *
     * @param file 文件
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(file: File?): Bitmap? {
        if (file == null) {
            return null
        }
        var `is`: InputStream? = null
        return try {
            `is` = BufferedInputStream(FileInputStream(file))
            BitmapFactory.decodeStream(`is`)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        } finally {
            closeIO(`is`)
        }
    }

    /**
     * 获取bitmap
     *
     * @param file      文件
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(file: File?, maxWidth: Int, maxHeight: Int): Bitmap? {
        if (file == null) {
            return null
        }
        var `is`: InputStream? = null
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            `is` = BufferedInputStream(FileInputStream(file))
            BitmapFactory.decodeStream(`is`, null, options)
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
            options.inJustDecodeBounds = false
            BitmapFactory.decodeStream(`is`, null, options)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        } finally {
            closeIO(`is`)
        }
    }

    /**
     * 获取bitmap
     *
     * @param filePath 文件路径
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(filePath: String?): Bitmap? {
        return if (isNullString(filePath)) {
            null
        } else BitmapFactory.decodeFile(filePath)
    }

    /**
     * 获取bitmap
     *
     * @param filePath  文件路径
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(filePath: String?, maxWidth: Int, maxHeight: Int): Bitmap? {
        if (isNullString(filePath)) {
            return null
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    /**
     * 获取bitmap
     *
     * @param is        输入流
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(`is`: InputStream?, maxWidth: Int, maxHeight: Int): Bitmap? {
        if (`is` == null) {
            return null
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(`is`, null, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeStream(`is`, null, options)
    }

    /**
     * 获取bitmap
     *
     * @param data      数据
     * @param offset    偏移量
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(data: ByteArray, offset: Int, maxWidth: Int, maxHeight: Int): Bitmap? {
        if (data.size == 0) {
            return null
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(data, offset, data.size, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(data, offset, data.size, options)
    }

    /**
     * 获取bitmap
     *
     * @param resId 资源id
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(resId: Int): Bitmap? {
        val `is` = RxTool.getContext().resources.openRawResource(resId)
        return BitmapFactory.decodeStream(`is`)
    }

    /**
     * 获取bitmap
     *
     * @param resId     资源id
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(resId: Int, maxWidth: Int, maxHeight: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val `is` = RxTool.getContext().resources.openRawResource(resId)
        BitmapFactory.decodeStream(`is`, null, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeStream(`is`, null, options)
    }

    /**
     * 获取bitmap
     *
     * @param res 资源对象
     * @param id  资源id
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(res: Resources?, id: Int): Bitmap? {
        return if (res == null) {
            null
        } else BitmapFactory.decodeResource(res, id)
    }

    /**
     * 获取bitmap
     *
     * @param res       资源对象
     * @param id        资源id
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(res: Resources?, id: Int, maxWidth: Int, maxHeight: Int): Bitmap? {
        if (res == null) {
            return null
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, id, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, id, options)
    }

    /**
     * 获取bitmap
     *
     * @param fd 文件描述
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(fd: FileDescriptor?): Bitmap? {
        return if (fd == null) {
            null
        } else BitmapFactory.decodeFileDescriptor(fd)
    }

    /**
     * 获取bitmap
     *
     * @param fd        文件描述
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(fd: FileDescriptor?, maxWidth: Int, maxHeight: Int): Bitmap? {
        if (fd == null) {
            return null
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(fd, null, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFileDescriptor(fd, null, options)
    }
    /**
     * 缩放图片
     *
     * @param src       源图片
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @param recycle   是否回收
     * @return 缩放后的图片
     */
    /**
     * 缩放图片
     *
     * @param src       源图片
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @return 缩放后的图片
     */
    @JvmOverloads
    @JvmStatic
    fun scale(src: Bitmap, newWidth: Int, newHeight: Int, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        val ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true)
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }
    /**
     * 缩放图片
     *
     * @param src         源图片
     * @param scaleWidth  缩放宽度倍数
     * @param scaleHeight 缩放高度倍数
     * @param recycle     是否回收
     * @return 缩放后的图片
     */
    /**
     * 缩放图片
     *
     * @param src         源图片
     * @param scaleWidth  缩放宽度倍数
     * @param scaleHeight 缩放高度倍数
     * @return 缩放后的图片
     */
    @JvmOverloads
    fun scale(src: Bitmap, scaleWidth: Float, scaleHeight: Float, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        val matrix = Matrix()
        matrix.setScale(scaleWidth, scaleHeight)
        val ret = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }
    /**
     * 裁剪图片
     *
     * @param src     源图片
     * @param x       开始坐标x
     * @param y       开始坐标y
     * @param width   裁剪宽度
     * @param height  裁剪高度
     * @param recycle 是否回收
     * @return 裁剪后的图片
     */
    /**
     * 裁剪图片
     *
     * @param src    源图片
     * @param x      开始坐标x
     * @param y      开始坐标y
     * @param width  裁剪宽度
     * @param height 裁剪高度
     * @return 裁剪后的图片
     */
    @JvmOverloads
    @JvmStatic
    fun clip(src: Bitmap, x: Int, y: Int, width: Int, height: Int, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        val ret = Bitmap.createBitmap(src, x, y, width, height)
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }

    /**
     * 倾斜图片
     *
     * @param src     源图片
     * @param kx      倾斜因子x
     * @param ky      倾斜因子y
     * @param recycle 是否回收
     * @return 倾斜后的图片
     */
    @JvmStatic
    fun skew(src: Bitmap, kx: Float, ky: Float, recycle: Boolean): Bitmap? {
        return skew(src, kx, ky, 0f, 0f, recycle)
    }

    /**
     * 倾斜图片
     *
     * @param src 源图片
     * @param kx  倾斜因子x
     * @param ky  倾斜因子y
     * @param px  平移因子x
     * @param py  平移因子y
     * @return 倾斜后的图片
     */
    @JvmStatic
    fun skew(src: Bitmap, kx: Float, ky: Float, px: Float, py: Float): Bitmap? {
        return skew(src, kx, ky, 0f, 0f, false)
    }
    /**
     * 倾斜图片
     *
     * @param src     源图片
     * @param kx      倾斜因子x
     * @param ky      倾斜因子y
     * @param px      平移因子x
     * @param py      平移因子y
     * @param recycle 是否回收
     * @return 倾斜后的图片
     */
    /**
     * 倾斜图片
     *
     * @param src 源图片
     * @param kx  倾斜因子x
     * @param ky  倾斜因子y
     * @return 倾斜后的图片
     */
    @JvmStatic
    fun skew(src: Bitmap, kx: Float, ky: Float, px: Float = 0f, py: Float = 0f, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        val matrix = Matrix()
        matrix.setSkew(kx, ky, px, py)
        val ret = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }
    /**
     * 旋转图片
     *
     * @param src     源图片
     * @param degrees 旋转角度
     * @param px      旋转点横坐标
     * @param py      旋转点纵坐标
     * @param recycle 是否回收
     * @return 旋转后的图片
     */
    /**
     * 旋转图片
     *
     * @param src     源图片
     * @param degrees 旋转角度
     * @param px      旋转点横坐标
     * @param py      旋转点纵坐标
     * @return 旋转后的图片
     */
    @JvmStatic
    fun rotate(src: Bitmap, degrees: Int, px: Float, py: Float, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        if (degrees == 0) {
            return src
        }
        val matrix = Matrix()
        matrix.setRotate(degrees.toFloat(), px, py)
        val ret = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }

    /**
     * 获取图片旋转角度
     *
     * @param filePath 文件路径
     * @return 旋转角度
     */
    @JvmStatic
    fun getRotateDegree(filePath: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(filePath)
            val orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
            degree = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 90
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }
    /**
     * 转为圆形图片
     *
     * @param src     源图片
     * @param recycle 是否回收
     * @return 圆形图片
     */
    /**
     * 转为圆形图片
     *
     * @param src 源图片
     * @return 圆形图片
     */
    @JvmOverloads
    @JvmStatic
    fun toRound(src: Bitmap, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        val width = src.width
        val height = src.height
        val radius = Math.min(width, height) shr 1
        val ret = src.copy(src.config, true)
        val paint = Paint()
        val canvas = Canvas(ret)
        val rect = Rect(0, 0, width, height)
        paint.isAntiAlias = true
        paint.color = Color.TRANSPARENT
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle((width shr 1.toFloat().toInt()).toFloat(), (height shr 1.toFloat().toInt()).toFloat(), radius.toFloat(), paint)
        canvas.drawBitmap(src, rect, rect, paint)
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }
    /**
     * 转为圆角图片
     *
     * @param src     源图片
     * @param radius  圆角的度数
     * @param recycle 是否回收
     * @return 圆角图片
     */
    /**
     * 转为圆角图片
     *
     * @param src    源图片
     * @param radius 圆角的度数
     * @return 圆角图片
     */
    @JvmOverloads
    @JvmStatic
    fun toRoundCorner(src: Bitmap?, radius: Float, recycle: Boolean = false): Bitmap? {
        if (null == src) {
            return null
        }
        val width = src.width
        val height = src.height
        val ret = src.copy(src.config, true)
        val bitmapShader = BitmapShader(src,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val paint = Paint()
        val canvas = Canvas(ret)
        val rectf = RectF(0f, 0f, width.toFloat(), height.toFloat())
        paint.isAntiAlias = true
        paint.shader = bitmapShader
        canvas.drawRoundRect(rectf, radius, radius, paint)
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }

    /**
     * 添加颜色边框
     *
     * @param src         源图片
     * @param borderWidth 边框宽度
     * @param color       边框的颜色值
     * @return 带颜色边框图
     */
    @JvmStatic
    fun addFrame(src: Bitmap?, borderWidth: Int, color: Int): Bitmap {
        return addFrame(src, borderWidth, color)
    }

    /**
     * 添加颜色边框
     *
     * @param src         源图片
     * @param borderWidth 边框宽度
     * @param color       边框的颜色值
     * @param recycle     是否回收
     * @return 带颜色边框图
     */
    @JvmStatic
    fun addFrame(src: Bitmap, borderWidth: Int, color: Int, recycle: Boolean): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        val newWidth = src.width + borderWidth shr 1
        val newHeight = src.height + borderWidth shr 1
        val ret = Bitmap.createBitmap(newWidth, newHeight, src.config)
        val canvas = Canvas(ret)
        val rec = canvas.clipBounds
        val paint = Paint()
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth.toFloat()
        canvas.drawRect(rec, paint)
        canvas.drawBitmap(src, borderWidth.toFloat(), borderWidth.toFloat(), null)
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }
    /**
     * 添加倒影
     *
     * @param src              源图片的
     * @param reflectionHeight 倒影高度
     * @param recycle          是否回收
     * @return 带倒影图片
     */
    /**
     * 添加倒影
     *
     * @param src              源图片的
     * @param reflectionHeight 倒影高度
     * @return 带倒影图片
     */
    @JvmOverloads
    @JvmStatic
    fun addReflection(src: Bitmap, reflectionHeight: Int, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        val REFLECTION_GAP = 0
        val srcWidth = src.width
        val srcHeight = src.height
        if (0 == srcWidth || srcHeight == 0) {
            return null
        }
        val matrix = Matrix()
        matrix.preScale(1f, -1f)
        val reflectionBitmap = Bitmap.createBitmap(src, 0, srcHeight - reflectionHeight,
                srcWidth, reflectionHeight, matrix, false)
                ?: return null
        val ret = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, src.config)
        val canvas = Canvas(ret)
        canvas.drawBitmap(src, 0f, 0f, null)
        canvas.drawBitmap(reflectionBitmap, 0f, srcHeight + REFLECTION_GAP.toFloat(), null)
        val paint = Paint()
        paint.isAntiAlias = true
        val shader = LinearGradient(0f, srcHeight * 1f, 0f, ret.height + REFLECTION_GAP * 1f, 0x70FFFFFF, 0x00FFFFFF, Shader.TileMode.MIRROR)
        paint.shader = shader
        paint.xfermode = PorterDuffXfermode(
                PorterDuff.Mode.DST_IN)
        canvas.save()
        canvas.drawRect(0f, srcHeight.toFloat(), srcWidth.toFloat(),
                ret.height + REFLECTION_GAP.toFloat(), paint)
        canvas.restore()
        if (!reflectionBitmap.isRecycled) {
            reflectionBitmap.recycle()
        }
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }
    /**
     * 添加文字水印
     *
     * @param src      源图片
     * @param content  水印文本
     * @param textSize 水印字体大小
     * @param color    水印字体颜色
     * @param alpha    水印字体透明度
     * @param x        起始坐标x
     * @param y        起始坐标y
     * @param recycle  是否回收
     * @return 带有文字水印的图片
     */
    /**
     * 添加文字水印
     *
     * @param src      源图片
     * @param content  水印文本
     * @param textSize 水印字体大小
     * @param color    水印字体颜色
     * @param alpha    水印字体透明度
     * @param x        起始坐标x
     * @param y        起始坐标y
     * @return 带有文字水印的图片
     */
    @JvmOverloads
    @JvmStatic
    fun addTextWatermark(src: Bitmap, content: String?, textSize: Int, color: Int, alpha: Int, x: Float, y: Float, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src) || content == null) {
            return null
        }
        val ret = src.copy(src.config, true)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val canvas = Canvas(ret)
        paint.alpha = alpha
        paint.color = color
        paint.textSize = textSize.toFloat()
        val bounds = Rect()
        paint.getTextBounds(content, 0, content.length, bounds)
        canvas.drawText(content, x, y, paint)
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }
    /**
     * 添加图片水印
     *
     * @param src       源图片
     * @param watermark 图片水印
     * @param x         起始坐标x
     * @param y         起始坐标y
     * @param alpha     透明度
     * @param recycle   是否回收
     * @return 带有图片水印的图片
     */
    /**
     * 添加图片水印
     *
     * @param src       源图片
     * @param watermark 图片水印
     * @param x         起始坐标x
     * @param y         起始坐标y
     * @param alpha     透明度
     * @return 带有图片水印的图片
     */
    @JvmOverloads
    @JvmStatic
    fun addImageWatermark(src: Bitmap, watermark: Bitmap?, x: Int, y: Int, alpha: Int, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        val ret = src.copy(src.config, true)
        if (!isEmptyBitmap(watermark)) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val canvas = Canvas(ret)
            paint.alpha = alpha
            canvas.drawBitmap(watermark!!, x.toFloat(), y.toFloat(), paint)
        }
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }

    /**
     * 转为alpha位图
     *
     * @param src 源图片
     * @return alpha位图
     */
    @JvmStatic
    fun toAlpha(src: Bitmap?): Bitmap {
        return toAlpha(src)
    }

    /**
     * 转为alpha位图
     *
     * @param src     源图片
     * @param recycle 是否回收
     * @return alpha位图
     */
    @JvmStatic
    fun toAlpha(src: Bitmap, recycle: Boolean): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        val ret = src.extractAlpha()
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return ret
    }

    /**
     * 可以对该图的非透明区域着色
     *
     *
     * 有多种使用场景，常见如 Button 的 pressed 状态，View 的阴影状态等
     *
     * @param iv
     * @param src
     * @param radius
     * @param color
     * @return
     */
    private fun getDropShadow(iv: ImageView, src: Bitmap, radius: Float, color: Int): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        val width = src.width
        val height = src.height
        val dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(dest)
        val alpha = src.extractAlpha()
        canvas.drawBitmap(alpha, 0f, 0f, paint)
        val filter = BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER)
        paint.maskFilter = filter
        canvas.drawBitmap(alpha, 0f, 0f, paint)
        iv.setImageBitmap(dest)
        return dest
    }
    /**
     * 转为灰度图片
     *
     * @param src     源图片
     * @param recycle 是否回收
     * @return 灰度图
     */
    /**
     * 转为灰度图片
     *
     * @param src 源图片
     * @return 灰度图
     */
    @JvmOverloads
    @JvmStatic
    fun toGray(src: Bitmap, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        val grayBitmap = Bitmap.createBitmap(src.width,
                src.height, Bitmap.Config.RGB_565)
        val canvas = Canvas(grayBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorMatrixColorFilter
        canvas.drawBitmap(src, 0f, 0f, paint)
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return grayBitmap
    }

    /**
     * 保存图片
     *
     * @param src      源图片
     * @param filePath 要保存到的文件路径
     * @param format   格式
     * @return `true`: 成功<br></br>`false`: 失败
     */
    @JvmStatic
    fun save(src: Bitmap, filePath: String?, format: CompressFormat?): Boolean {
        return save(src, getFileByPath(filePath), format, false)
    }

    /**
     * 保存图片
     *
     * @param src      源图片
     * @param filePath 要保存到的文件路径
     * @param format   格式
     * @param recycle  是否回收
     * @return `true`: 成功<br></br>`false`: 失败
     */
    @JvmStatic
    fun save(src: Bitmap, filePath: String?, format: CompressFormat?, recycle: Boolean): Boolean {
        return save(src, getFileByPath(filePath), format, recycle)
    }
    /**
     * 保存图片
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return `true`: 成功<br></br>`false`: 失败
     */
    /**
     * 保存图片
     *
     * @param src    源图片
     * @param file   要保存到的文件
     * @param format 格式
     * @return `true`: 成功<br></br>`false`: 失败
     */
    @JvmOverloads
    @JvmStatic
    fun save(src: Bitmap, file: File?, format: CompressFormat?, recycle: Boolean = false): Boolean {
        if (isEmptyBitmap(src) || !createOrExistsFile(file)) {
            return false
        }
        println(src.width.toString() + ", " + src.height)
        var os: OutputStream? = null
        var ret = false
        try {
            os = BufferedOutputStream(FileOutputStream(file))
            ret = src.compress(format, 100, os)
            if (recycle && !src.isRecycled) {
                src.recycle()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            closeIO(os)
        }
        return ret
    }

    /**
     * 根据文件名判断文件是否为图片
     *
     * @param file 　文件
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isImage(file: File?): Boolean {
        return file != null && isImage(file.path)
    }

    /**
     * 根据文件名判断文件是否为图片
     *
     * @param filePath 　文件路径
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isImage(filePath: String): Boolean {
        val path = filePath.toUpperCase()
        return (path.endsWith(".PNG") || path.endsWith(".JPG")
                || path.endsWith(".JPEG") || path.endsWith(".BMP")
                || path.endsWith(".GIF"))
    }

    /**
     * 获取图片类型
     *
     * @param filePath 文件路径
     * @return 图片类型
     */
    @JvmStatic
    fun getImageType(filePath: String?): String? {
        return getImageType(getFileByPath(filePath))
    }

    /**
     * 获取图片类型
     *
     * @param file 文件
     * @return 图片类型
     */
    @JvmStatic
    fun getImageType(file: File?): String? {
        if (file == null) return null
        var `is`: InputStream? = null
        return try {
            `is` = FileInputStream(file)
            getImageType(`is`)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            closeIO(`is`)
        }
    }

    /**
     * 流获取图片类型
     *
     * @param is 图片输入流
     * @return 图片类型
     */
    @JvmStatic
    fun getImageType(`is`: InputStream?): String? {
        return if (`is` == null) null else try {
            val bytes = ByteArray(8)
            if (`is`.read(bytes, 0, 8) != -1) getImageType(bytes) else null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取图片类型
     *
     * @param bytes bitmap的前8字节
     * @return 图片类型
     */
    @JvmStatic
    fun getImageType(bytes: ByteArray): String? {
        if (isJPEG(bytes)) {
            return "JPEG"
        }
        if (isGIF(bytes)) {
            return "GIF"
        }
        if (isPNG(bytes)) {
            return "PNG"
        }
        return if (isBMP(bytes)) {
            "BMP"
        } else null
    }

    @JvmStatic
    fun isJPEG(b: ByteArray): Boolean {
        return b.size >= 2 && b[0] == 0xFF.toByte() && b[1] == 0xD8.toByte()
    }

    @JvmStatic
    fun isGIF(b: ByteArray): Boolean {
        return b.size >= 6 && b[0] == 'G'.toByte() && b[1] == 'I'.toByte() && b[2] == 'F'.toByte() && b[3] == '8'.toByte() && (b[4] == '7'.toByte() || b[4] == '9'.toByte()) && b[5] == 'a'.toByte()
    }

    @JvmStatic
    fun isPNG(b: ByteArray): Boolean {
        return (b.size >= 8
                && b[0] == 137.toByte() && b[1] == 80.toByte() && b[2] == 78.toByte() && b[3] == 71.toByte() && b[4] == 13.toByte() && b[5] == 10.toByte() && b[6] == 26.toByte() && b[7] == 10.toByte())
    }

    @JvmStatic
    fun isBMP(b: ByteArray): Boolean {
        return b.size >= 2 && b[0].toInt() == 0x42 && b[1].toInt() == 0x4d
    }

    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isEmptyBitmap(src: Bitmap?): Boolean {
        return src == null || src.width == 0 || src.height == 0
    }

    /**
     * 按缩放压缩
     *
     * @param src       源图片
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @return 缩放压缩后的图片
     */
    @JvmStatic
    fun compressByScale(src: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        return scale(src, newWidth, newHeight, false)
    }

    /**
     * 按缩放压缩
     *
     * @param src       源图片
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @param recycle   是否回收
     * @return 缩放压缩后的图片
     */
    @JvmStatic
    fun compressByScale(src: Bitmap, newWidth: Int, newHeight: Int, recycle: Boolean): Bitmap? {
        return scale(src, newWidth, newHeight, recycle)
    }
    /******************************~~~~~~~~~ 下方和压缩有关 ~~~~~~~~~ */
    /**
     * 按缩放压缩
     *
     * @param src         源图片
     * @param scaleWidth  缩放宽度倍数
     * @param scaleHeight 缩放高度倍数
     * @return 缩放压缩后的图片
     */
    @JvmStatic
    fun compressByScale(src: Bitmap, scaleWidth: Float, scaleHeight: Float): Bitmap? {
        return scale(src, scaleWidth, scaleHeight, false)
    }

    /**
     * 按缩放压缩
     *
     * @param src         源图片
     * @param scaleWidth  缩放宽度倍数
     * @param scaleHeight 缩放高度倍数
     * @param recycle     是否回收
     * @return 缩放压缩后的图片
     */
    @JvmStatic
    fun compressByScale(src: Bitmap, scaleWidth: Float, scaleHeight: Float, recycle: Boolean): Bitmap? {
        return scale(src, scaleWidth, scaleHeight, recycle)
    }
    /**
     * 按质量压缩
     *
     * @param src     源图片
     * @param quality 质量
     * @param recycle 是否回收
     * @return 质量压缩后的图片
     */
    /**
     * 按质量压缩
     *
     * @param src     源图片
     * @param quality 质量
     * @return 质量压缩后的图片
     */
    @JvmOverloads
    @JvmStatic
    fun compressByQuality(src: Bitmap, quality: Int, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src) || quality < 0 || quality > 100) {
            return null
        }
        val baos = ByteArrayOutputStream()
        src.compress(CompressFormat.JPEG, quality, baos)
        val bytes = baos.toByteArray()
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
    /**
     * 按质量压缩
     *
     * @param src         源图片
     * @param maxByteSize 允许最大值字节数
     * @param recycle     是否回收
     * @return 质量压缩压缩过的图片
     */
    /**
     * 按质量压缩
     *
     * @param src         源图片
     * @param maxByteSize 允许最大值字节数
     * @return 质量压缩压缩过的图片
     */
    @JvmOverloads
    @JvmStatic
    fun compressByQuality(src: Bitmap, maxByteSize: Long, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src) || maxByteSize <= 0) {
            return null
        }
        val baos = ByteArrayOutputStream()
        var quality = 100
        src.compress(CompressFormat.JPEG, quality, baos)
        while (baos.toByteArray().size > maxByteSize && quality >= 0) {
            baos.reset()
            src.compress(CompressFormat.JPEG, 5.let { quality -= it; quality }, baos)
        }
        if (quality < 0) {
            return null
        }
        val bytes = baos.toByteArray()
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
    /**
     * 按采样大小压缩
     *
     * @param src        源图片
     * @param sampleSize 采样率大小
     * @param recycle    是否回收
     * @return 按采样率压缩后的图片
     */
    /**
     * 按采样大小压缩
     *
     * @param src        源图片
     * @param sampleSize 采样率大小
     * @return 按采样率压缩后的图片
     */
    @JvmOverloads
    @JvmStatic
    fun compressBySampleSize(src: Bitmap, sampleSize: Int, recycle: Boolean = false): Bitmap? {
        if (isEmptyBitmap(src)) {
            return null
        }
        val options = BitmapFactory.Options()
        options.inSampleSize = sampleSize
        val baos = ByteArrayOutputStream()
        src.compress(CompressFormat.JPEG, 100, baos)
        val bytes = baos.toByteArray()
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }

    /**
     * 缩略图工具类，
     * 可以根据本地视频文件源、
     * Bitmap 对象生成缩略图
     *
     * @param filePath
     * @param kind
     * @return
     */
    @JvmStatic
    fun getThumb(filePath: String?, kind: Int): Bitmap {
        return ThumbnailUtils.createVideoThumbnail(filePath, kind)
    }

    @JvmStatic
    fun getThumb(source: Bitmap?, width: Int, height: Int): Bitmap {
        return ThumbnailUtils.extractThumbnail(source, width, height)
    }

    @JvmStatic
    fun zoomImage(bgimage: Bitmap, newWidth: Double, newHeight: Double): Bitmap {
        // 获取到bitmap的宽
        val width = bgimage.width.toFloat()
        val height = bgimage.height.toFloat()
        //
        val matrix = Matrix()
        // 设置尺寸
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        matrix.postScale(scaleWidth, scaleHeight)
        val bitmap = Bitmap.createBitmap(bgimage, 0, 0, width.toInt(),
                height.toInt(), matrix, true)
        Log.e("tag", (bitmap.height + bitmap.width).toString() + "d")
        return bitmap
    }

    /**
     * Resize the bitmap
     *
     * @param bitmap 图片引用
     * @param width  宽度
     * @param height 高度
     * @return 缩放之后的图片引用
     */
    @JvmStatic
    fun zoomBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val matrix = Matrix()
        val scaleWidth = width.toFloat() / w
        val scaleHeight = height.toFloat() / h
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true)
    }

    /**
     * 获取bitmap
     *
     * @param is 输入流
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(`is`: InputStream?): Bitmap? {
        return if (`is` == null) {
            null
        } else BitmapFactory.decodeStream(`is`)
    }

    /**
     * 获取bitmap
     *
     * @param data   数据
     * @param offset 偏移量
     * @return bitmap
     */
    @JvmStatic
    fun getBitmap(data: ByteArray, offset: Int): Bitmap? {
        return if (data.isEmpty()) {
            null
        } else BitmapFactory.decodeByteArray(data, offset, data.size)
    }

    /**
     * 从网络上加载Bitmap
     *
     * @param imgUrl
     * @return
     */
    @JvmStatic
    fun getBitmapFromNet(imgUrl: String?): Bitmap? {
        var inputStream: InputStream? = null
        var outputStream: ByteArrayOutputStream? = null
        var url: URL? = null
        try {
            url = URL(imgUrl)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.readTimeout = 2000
            httpURLConnection.connect()
            if (httpURLConnection.responseCode == 200) {
                //网络连接成功
                inputStream = httpURLConnection.inputStream
                outputStream = ByteArrayOutputStream()
                val buffer = ByteArray(1024 * 8)
                var len = -1
                while (inputStream.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                }
                val bu = outputStream.toByteArray()
                return BitmapFactory.decodeByteArray(bu, 0, bu.size)
            } else {
                RxLogTool.d("网络连接失败----" + httpURLConnection.responseCode)
            }
        } catch (e: Exception) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {

                    e.printStackTrace()
                }
            }
        }
        return null
    }

    /**
     * 绘制 9Path
     *
     * @param c
     * @param bmp
     * @param rect
     */
    @JvmStatic
    fun drawNinePath(c: Canvas?, bmp: Bitmap, rect: Rect?) {
        val patch = NinePatch(bmp, bmp.ninePatchChunk, null)
        patch.draw(c, rect)
    }

    /**
     * 创建的包含文字的图片，背景为透明
     *
     * @param source              图片
     * @param txtSize             文字大小
     * @param innerTxt            显示的文字
     * @param textColor           文字颜色Color.BLUE
     * @param textBackgroundColor 文字背景板颜色 Color.parseColor("#FFD700")
     * @return
     */
    @JvmStatic
    fun createTextImage(source: Bitmap, txtSize: Int, innerTxt: String, textColor: Int, textBackgroundColor: Int): Bitmap {
        val bitmapWidth = source.width
        val bitmapHeight = source.height
        val textWidth = txtSize * innerTxt.length
        val textHeight = txtSize
        val width: Int
        width = if (bitmapWidth > textWidth) {
            bitmapWidth + txtSize * innerTxt.length
        } else {
            txtSize * innerTxt.length
        }
        val height = bitmapHeight + txtSize

        //若使背景为透明，必须设置为Bitmap.Config.ARGB_4444
        val bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bm)
        //把图片画上来
        val bitmapPaint = Paint()
        canvas.drawBitmap(source, (width - bitmapWidth) / 2.toFloat(), 0f, bitmapPaint)
        val paint = Paint()
        paint.color = textColor
        paint.textSize = txtSize.toFloat()
        paint.isAntiAlias = true


        //计算得出文字的绘制起始x、y坐标
        val posX = (width - txtSize * innerTxt.length) / 2
        val posY = height / 2
        val textX = posX + txtSize * innerTxt.length / 4
        val paint1 = Paint()
        paint1.color = textBackgroundColor
        paint1.strokeWidth = 3f
        paint1.style = Paint.Style.FILL_AND_STROKE
        val r1 = RectF()
        r1.left = posX.toFloat()
        r1.right = posX + txtSize * innerTxt.length.toFloat()
        r1.top = posY.toFloat()
        r1.bottom = posY + txtSize.toFloat()
        canvas.drawRoundRect(r1, 10f, 10f, paint1)
        canvas.drawText(innerTxt, textX.toFloat(), posY + txtSize - 2.toFloat(), paint)
        return bm
    }

    /**
     * 获得屏幕的分辨率
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getScreenResolution(context: Context): IntArray {
        val scrennResolution = IntArray(2)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        val display = wm.defaultDisplay
        display.getMetrics(dm)
        scrennResolution[0] = dm.widthPixels
        scrennResolution[1] = dm.heightPixels
        return scrennResolution
    }

    @JvmStatic
    fun getDensity(context: Context): Float {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        val display = wm.defaultDisplay
        display.getMetrics(dm)
        return dm.density
    }

    /**
     * 按最大边按一定大小缩放图片
     *
     * @param resources
     * @param resId
     * @param maxSize   压缩后最大长度
     * @return
     */
    @JvmStatic
    fun scaleImage(resources: Resources?, resId: Int, maxSize: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)
        options.inSampleSize = calculateInSampleSize2(options, maxSize, maxSize)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, resId, options)
    }
}