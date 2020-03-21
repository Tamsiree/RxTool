package com.tamsiree.rxkit

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tamsiree.rxkit.RxLocationTool.GCJ02ToBD09
import com.tamsiree.rxkit.RxLocationTool.GPS84ToGCJ02
import com.tamsiree.rxkit.model.Gps

/**
 * @author tamsiree
 * @date 2018/5/2 14:59:00
 */
object RxMapTool {
    /**
     * 测试数据
     * Gps gpsFrom = new Gps();
     * gpsFrom.setLongitude(112.938417);
     * gpsFrom.setLatitude(28.115383);
     *
     *
     * Gps gpsTo = new Gps();
     * gpsTo.setLongitude(112.526993);
     * gpsTo.setLatitude(27.72926);
     *
     *
     * 跳转高德/百度 导航功能
     *
     * @param mContext  实体
     * @param gpsFrom   起点经纬度信息
     * @param gpsTo     终点经纬度信息
     * @param storeName 目的地名称
     */
    @JvmStatic
    fun openMap(mContext: Context, gpsFrom: Gps, gpsTo: Gps, storeName: String) {
        //检测设备是否安装高德地图APP
        if (RxPackageManagerTool.haveExistPackageName(mContext, RxConstants.GAODE_PACKAGE_NAME)) {
            openGaodeMapToGuide(mContext, gpsFrom, gpsTo, storeName)
            //检测设备是否安装百度地图APP
        } else if (RxPackageManagerTool.haveExistPackageName(mContext, RxConstants.BAIDU_PACKAGE_NAME)) {
            openBaiduMapToGuide(mContext, gpsTo, storeName)
            //检测都未安装时，跳转网页版高德地图
        } else {
            openBrowserToGuide(mContext, gpsTo, storeName)
        }
    }

    /**
     * 跳转到高德地图 并 导航到目的地
     *
     * @param mContext  实体
     * @param gpsFrom   起点经纬度信息
     * @param gpsTo     终点经纬度信息
     * @param storeName 目的地名称
     */
    @JvmStatic
    fun openGaodeMapToGuide(mContext: Context, gpsFrom: Gps, gpsTo: Gps, storeName: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val gps = GPS84ToGCJ02(gpsFrom.longitude, gpsFrom.latitude)
        val gps1 = GPS84ToGCJ02(gpsTo.longitude, gpsTo.latitude)
        val url = "androidamap://route?" +
                "sourceApplication=amap" +
                "&slat=" + gps!!.latitude +
                "&slon=" + gps.longitude +
                "&dlat=" + gps1!!.latitude +
                "&dlon=" + gps1.longitude +
                "&dname=" + storeName +
                "&dev=0" +
                "&t=0"
        val uri = Uri.parse(url)
        //将功能Scheme以URI的方式传入data
        intent.data = uri
        //启动该页面即可
        mContext.startActivity(intent)
    }

    /**
     * 跳转到百度地图 并 导航到目的地
     *
     * @param mContext  实体
     * @param gps       目的地经纬度信息
     * @param storeName 目的地名称
     */
    @JvmStatic
    fun openBaiduMapToGuide(mContext: Context, gps: Gps, storeName: String) {
        val intent = Intent()
        val gps1 = GPS84ToGCJ02(gps.longitude, gps.latitude)
        val gps2 = GCJ02ToBD09(gps1!!.longitude, gps1.latitude)
        val url = "baidumap://map/direction?" +
                "destination=name:" + storeName +
                "|latlng:" + gps2.latitude + "," + gps2.longitude +
                "&mode=driving" +
                "&sy=3" +
                "&index=0" +
                "&target=1"
        val uri = Uri.parse(url)
        //将功能Scheme以URI的方式传入data
        intent.data = uri
        //启动该页面即可
        mContext.startActivity(intent)
    }

    /**
     * 跳转到网页版高德地图 并 导航到目的地
     *
     * @param mContext  实体
     * @param gpsFrom   目的地经纬度信息
     * @param storeName 目的地名称
     */
    @JvmStatic
    fun openBrowserToGuide(mContext: Context, gpsFrom: Gps, storeName: String) {
        val gps = GPS84ToGCJ02(gpsFrom.longitude, gpsFrom.latitude)
        val url = "http://uri.amap.com/navigation?" +
                "to=" + gps!!.latitude + "," + gps.longitude + "," + storeName + "" +
                "&mode=car" +
                "&policy=1" +
                "&src=mypage" +
                "&coordinate=gaode" +
                "&callnative=0"
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        mContext.startActivity(intent)
    }

    /**
     * 将实际地理距离转换为屏幕像素值
     *
     * @param distance  实际距离,单位为米
     * @param currScale 当前地图尺寸
     * @param context
     * @return
     */
    @JvmStatic
    fun metreToScreenPixel(distance: Double, currScale: Double, context: Context): Double {
        val dpi = context.resources.displayMetrics.densityDpi.toFloat()
        // 当前地图范围内1像素代表多少地图单位的实际距离
        val resolution = 25.39999918 / dpi * currScale / 1000
        return distance / resolution
    }

    /**
     * 将屏幕上对应的像素距离转换为当前显示地图上的地理距离(米)
     *
     * @param pxlength
     * @param currScale
     * @param context
     * @return
     */
    @JvmStatic
    fun screenPixelToMetre(pxlength: Double, currScale: Double, context: Context): Double {
        val dpi = context.resources.displayMetrics.densityDpi.toFloat()
        val resolution = 25.39999918 / dpi * currScale / 1000
        return pxlength * resolution
    }
}