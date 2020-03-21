package com.tamsiree.rxkit

import android.location.Location
import android.media.ExifInterface
import com.orhanobut.logger.Logger
import java.io.File

/**
 *
 * @author Tamsiree
 * @date 2017/7/28
 */
object RxExifTool {
    /**
     * 将经纬度信息写入JPEG图片文件里
     *
     * @param picPath JPEG图片文件路径
     * @param dLat    纬度
     * @param dLon    经度
     */
    @JvmStatic
    fun writeLatLonIntoJpeg(picPath: String?, dLat: Double, dLon: Double) {
        val file = File(picPath)
        if (file.exists()) {
            try {
                val exif = ExifInterface(picPath)
                val tagLat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                val tagLon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                if (tagLat == null && tagLon == null) { // 无经纬度信息
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, gpsInfoConvert(dLat))
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, if (dLat > 0) "N" else "S") // 区分南北半球
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, gpsInfoConvert(dLon))
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, if (dLon > 0) "E" else "W") // 区分东经西经
                    exif.saveAttributes()
                }
                exif.saveAttributes()
                Logger.d("""
    ${exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)}
    ${exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)}
    ${exif.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD)}
    ${exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)}
    ${exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)}
    """.trimIndent())
            } catch (e: Exception) {
            }
        }
    }

    private fun gpsInfoConvert(gpsInfo: Double): String {
        var gpsInfo = gpsInfo
        gpsInfo = Math.abs(gpsInfo)
        val dms = Location.convert(gpsInfo, Location.FORMAT_SECONDS)
        val splits = dms.split(":").toTypedArray()
        val secnds = splits[2].split("\\.").toTypedArray()
        val seconds: String
        seconds = if (secnds.size == 0) {
            splits[2]
        } else {
            secnds[0]
        }
        return splits[0] + "/1," + splits[1] + "/1," + seconds + "/1"
    }
}