package com.vondear.rxtools;

import android.location.Location;
import android.media.ExifInterface;

import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * Created by Vondear on 2017/7/28.
 *
 */

public class RxExifTool {
    /**
     * 将经纬度信息写入JPEG图片文件里
     *
     * @param picPath JPEG图片文件路径
     * @param dLat    纬度
     * @param dLon    经度
     */
    public static void writeLatLonIntoJpeg(String picPath, double dLat, double dLon) {
        File file = new File(picPath);
        if (file.exists()) {
            try {
                ExifInterface exif = new ExifInterface(picPath);
                String tagLat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                String tagLon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                if (tagLat == null && tagLon == null) {// 无经纬度信息
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, gpsInfoConvert(dLat));
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, dLat > 0 ? "N" : "S"); // 区分南北半球
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, gpsInfoConvert(dLon));
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, dLon > 0 ? "E" : "W"); // 区分东经西经
                    exif.saveAttributes();
                }
                exif.saveAttributes();

                Logger.d(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) + "\n"
                        + exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) + "\n"
                        + exif.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD) + "\n"
                        + exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) + "\n"
                        + exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
            } catch (Exception e) {

            }
        }
    }

    private static String gpsInfoConvert(double gpsInfo){
        gpsInfo = Math.abs(gpsInfo);
        String dms = Location.convert(gpsInfo, Location.FORMAT_SECONDS);
        String[] splits = dms.split(":");
        String[] secnds = (splits[2]).split("\\.");
        String seconds;
        if (secnds.length == 0) {
            seconds = splits[2];
        } else {
            seconds = secnds[0];
        }
        return  splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
    }
}
