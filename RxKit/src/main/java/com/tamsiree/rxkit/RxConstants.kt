package com.tamsiree.rxkit

import java.io.File
import java.text.DecimalFormat
import java.util.*

/**
 * @author tamsiree
 * @date 2017/1/13
 */
object RxConstants {
    const val FAST_CLICK_TIME = 100
    const val VIBRATE_TIME = 100
    //----------------------------------------------------常用链接- start ------------------------------------------------------------
    /**
     * RxTool的Github地址
     */
    const val URL_RXTOOL = "https://github.com/tamsiree/RxTool"

    /**
     * 百度文字搜索
     */
    const val URL_BAIDU_SEARCH = "http://www.baidu.com/s?wd="

    /**
     * ACFUN
     */
    const val URL_ACFUN = "http://www.acfun.tv/"
    const val URL_JPG_TO_FONT = "http://ku.cndesign.com/pic/"

    //===================================================常用链接== end ==============================================================
    const val URL_BORING_PICTURE = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_pic_comments&page="
    const val URL_PERI_PICTURE = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page="
    const val URL_JOKE_MUSIC = "http://route.showapi.com/255-1?type=31&showapi_appid=20569&showapi_sign=0707a6bfb3e842fb8c8aa450012d9756&page="
    const val SP_MADE_CODE = "MADE_CODE"

    //==========================================煎蛋 API end=========================================
    const val SP_SCAN_CODE = "SCAN_CODE"

    //微信统一下单接口
    const val WX_TOTAL_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder"

    //------------------------------------------煎蛋 API start--------------------------------------
    var URL_JOKE = "http://ic.snssdk.com/neihan/stream/mix/v1/?" +
            "mpic=1&essence=1" +
            "&content_type=-102" +
            "&message_cursor=-1" +
            "&bd_Stringitude=113.369569" +
            "&bd_latitude=23.149678" +
            "&bd_city=%E5%B9%BF%E5%B7%9E%E5%B8%82" +
            "&am_Stringitude=113.367846" +
            "&am_latitude=23.149878" +
            "&am_city=%E5%B9%BF%E5%B7%9E%E5%B8%82" +
            "&am_loc_time=1465213692154&count=30" +
            "&min_time=1465213700&screen_width=720&iid=4512422578" +
            "&device_id=17215021497" +
            "&ac=wifi" +
            "&channel=NHSQH5AN" +
            "&aid=7" +
            "&app_name=joke_essay" +
            "&version_code=431" +
            "&device_platform=android" +
            "&ssmix=a" +
            "&device_type=6s+Plus" +
            "&os_api=19" +
            "&os_version=4.4.2" +
            "&uuid=864394108025091" +
            "&openudid=80FA5B208E050000" +
            "&manifest_version_code=431"

    //高德地图APP 包名
    const val GAODE_PACKAGE_NAME = "com.autonavi.minimap"

    //百度地图APP 包名
    const val BAIDU_PACKAGE_NAME = "com.baidu.BaiduMap"

    /**
     * 速度格式化
     */
    val FORMAT_ONE = DecimalFormat("#.#")

    /**
     * 距离格式化
     */
    @JvmField
    val FORMAT_TWO = DecimalFormat("#.##")

    /**
     * 速度格式化
     */
    val FORMAT_THREE = DecimalFormat("#.###")

    //默认保存下载文件目录
    val DOWNLOAD_DIR = RxFileTool.rootPath.toString() + File.separator + "Download" + File.separator + RxDeviceTool.appPackageName + File.separator

    //图片缓存路径
    @JvmField
    val PICTURE_CACHE_PATH = RxFileTool.getCacheFolder(RxTool.getContext()).toString() + File.separator + "Picture" + File.separator + "Cache" + File.separator

    //图片原始路径
    val PICTURE_ORIGIN_PATH = RxFileTool.rootPath.toString() + File.separator + RxDeviceTool.appPackageName + File.separator + "Picture" + File.separator + "Origin" + File.separator

    //图片压缩路径
    val PICTURE_COMPRESS_PATH = RxFileTool.rootPath.toString() + File.separator + RxDeviceTool.appPackageName + File.separator + "Picture" + File.separator + "Compress" + File.separator

    //默认导出文件目录
    val EXPORT_FILE_PATH = RxFileTool.rootPath.toString() + File.separator + RxDeviceTool.appPackageName + File.separator + "ExportFile" + File.separator

    //图片名称
    val pictureName: String
        get() = RxTimeTool.getCurrentDateTime(DATE_FORMAT_LINK) + "_" + Random().nextInt(1000) + ".jpg"

    //Date格式
    const val DATE_FORMAT_LINK = "yyyyMMddHHmmssSSS"

    //Date格式 常用
    const val DATE_FORMAT_DETACH = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT_DETACH_CN = "yyyy年MM月dd日 HH:mm:ss"
    const val DATE_FORMAT_DETACH_CN_SSS = "yyyy年MM月dd日 HH:mm:ss SSS"

    //Date格式 带毫秒
    const val DATE_FORMAT_DETACH_SSS = "yyyy-MM-dd HH:mm:ss SSS"

    //时间格式 分钟：秒钟 一般用于视频时间显示
    const val DATE_FORMAT_MM_SS = "mm:ss"

    // Performance testing notes (JDK 1.4, Jul03, scolebourne)
    // Whitespace:
    // Character.isWhitespace() is faster than WHITESPACE.indexOf()
    // where WHITESPACE is a string of all whitespace characters
    //
    // Character access:
    // String.charAt(n) versus toCharArray(), then array[n]
    // String.charAt(n) is about 15% worse for a 10K string
    // They are about equal for a length 50 string
    // String.charAt(n) is about 4 times better for a length 3 string
    // String.charAt(n) is best bet overall
    //
    // Append:
    // String.concat about twice as fast as StringBuffer.append
    // (not sure who tested this)
    /**
     * A String for a space character.
     *
     * @since 3.2
     */
    const val SPACE = " "

}