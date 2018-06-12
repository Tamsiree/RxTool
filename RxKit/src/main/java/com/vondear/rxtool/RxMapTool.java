package com.vondear.rxtool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.vondear.rxtool.model.Gps;


/**
 * @author vondear
 * @date 2018/5/2 14:59:00
 */
public class RxMapTool {

    /**
     * 测试数据
     * Gps gpsFrom = new Gps();
     * gpsFrom.setLongitude(112.938417);
     * gpsFrom.setLatitude(28.115383);
     * <p>
     * Gps gpsTo = new Gps();
     * gpsTo.setLongitude(112.526993);
     * gpsTo.setLatitude(27.72926);
     * <p>
     * 跳转高德/百度 导航功能
     *
     * @param mContext  实体
     * @param gpsFrom   起点经纬度信息
     * @param gpsTo     终点经纬度信息
     * @param storeName 目的地名称
     */
    public static void openMap(Context mContext, Gps gpsFrom, Gps gpsTo, String storeName) {
        //检测设备是否安装高德地图APP
        if (RxPackageManagerTool.haveExistPackageName(mContext, RxConstants.GAODE_PACKAGE_NAME)) {
            RxMapTool.openGaodeMapToGuide(mContext, gpsFrom, gpsTo, storeName);
            //检测设备是否安装百度地图APP
        } else if (RxPackageManagerTool.haveExistPackageName(mContext, RxConstants.BAIDU_PACKAGE_NAME)) {
            RxMapTool.openBaiduMapToGuide(mContext, gpsTo, storeName);
            //检测都未安装时，跳转网页版高德地图
        } else {
            RxMapTool.openBrowserToGuide(mContext, gpsTo, storeName);
        }
    }

    /**
     * 跳转到高德地图 并 导航到目的地
     * @param mContext 实体
     * @param gpsFrom 起点经纬度信息
     * @param gpsTo 终点经纬度信息
     * @param storeName 目的地名称
     */
    public static void openGaodeMapToGuide(Context mContext, Gps gpsFrom, Gps gpsTo, String storeName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        Gps gps = RxLocationTool.GPS84ToGCJ02(gpsFrom.getLongitude(), gpsFrom.getLatitude());
        Gps gps1 = RxLocationTool.GPS84ToGCJ02(gpsTo.getLongitude(), gpsTo.getLatitude());

        String url = "androidamap://route?" +
                "sourceApplication=amap" +
                "&slat=" + gps.getLatitude() +
                "&slon=" + gps.getLongitude() +
                "&dlat=" + gps1.getLatitude() +
                "&dlon=" + gps1.getLongitude() +
                "&dname=" + storeName +
                "&dev=0" +
                "&t=0";
        Uri uri = Uri.parse(url);
        //将功能Scheme以URI的方式传入data
        intent.setData(uri);
        //启动该页面即可
        mContext.startActivity(intent);
    }

    /**
     * 跳转到百度地图 并 导航到目的地
     * @param mContext 实体
     * @param gps 目的地经纬度信息
     * @param storeName 目的地名称
     */
    public static void openBaiduMapToGuide(Context mContext, Gps gps, String storeName) {
        Intent intent = new Intent();
        Gps gps1 = RxLocationTool.GPS84ToGCJ02(gps.getLongitude(), gps.getLatitude());
        Gps gps2 = RxLocationTool.GCJ02ToBD09(gps1.getLongitude(), gps1.getLatitude());
        String url = "baidumap://map/direction?" +
                "destination=name:" + storeName +
                "|latlng:" + gps2.getLatitude() + "," + gps2.getLongitude() +
                "&mode=driving" +
                "&sy=3" +
                "&index=0" +
                "&target=1";
        Uri uri = Uri.parse(url);
        //将功能Scheme以URI的方式传入data
        intent.setData(uri);
        //启动该页面即可
        mContext.startActivity(intent);
    }

    /**
     * 跳转到网页版高德地图 并 导航到目的地
     * @param mContext 实体
     * @param gpsFrom 目的地经纬度信息
     * @param storeName 目的地名称
     */
    public static void openBrowserToGuide(Context mContext, Gps gpsFrom, String storeName) {
        Gps gps = RxLocationTool.GPS84ToGCJ02(gpsFrom.getLongitude(), gpsFrom.getLatitude());
        String url = "http://uri.amap.com/navigation?" +
                "to=" + gps.getLatitude() + "," + gps.getLongitude() + "," + storeName + "" +
                "&mode=car" +
                "&policy=1" +
                "&src=mypage" +
                "&coordinate=gaode" +
                "&callnative=0";
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }


    /**
     * 将实际地理距离转换为屏幕像素值
     *
     * @param distance  实际距离,单位为米
     * @param currScale 当前地图尺寸
     * @param context
     * @return
     */
    public static double metreToScreenPixel(double distance, double currScale,
                                            Context context) {
        float dpi = context.getResources().getDisplayMetrics().densityDpi;
        // 当前地图范围内1像素代表多少地图单位的实际距离
        double resolution = (25.39999918 / dpi)
                * currScale / 1000;
        return distance / resolution;
    }

    /**
     * 将屏幕上对应的像素距离转换为当前显示地图上的地理距离(米)
     *
     * @param pxlength
     * @param currScale
     * @param context
     * @return
     */
    public static double screenPixelToMetre(double pxlength, double currScale,
                                            Context context) {
        float dpi = context.getResources().getDisplayMetrics().densityDpi;
        double resolution = (25.39999918 / dpi)
                * currScale / 1000;
        return pxlength * resolution;
    }

}
