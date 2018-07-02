package com.vondear.rxtool;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author vondear
 * @date 2017/1/13
 */

public class RxConstants {

    public final static int FAST_CLICK_TIME = 100;

    public final static int VIBRATE_TIME = 100;

    //----------------------------------------------------常用链接- start ------------------------------------------------------------

    /**
     * RxTools的Github地址
     */
    public static final String URL_VONTOOLS = "https://github.com/vondear/RxTool";

    /**
     * 百度文字搜索
     */
    public static final String URL_BAIDU_SEARCH = "http://www.baidu.com/s?wd=";

    /**
     * ACFUN
     */
    public static final String URL_ACFUN = "http://www.acfun.tv/";

    public static final String URL_JPG_TO_FONT = "http://ku.cndesign.com/pic/";

    //===================================================常用链接== end ==============================================================
    public static final String URL_BORING_PICTURE = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_pic_comments&page=";
    public static final String URL_PERI_PICTURE = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=";
    public static final String URL_JOKE_MUSIC = "http://route.showapi.com/255-1?type=31&showapi_appid=20569&showapi_sign=0707a6bfb3e842fb8c8aa450012d9756&page=";
    public static final String SP_MADE_CODE = "MADE_CODE";
    //==========================================煎蛋 API end=========================================
    public static final String SP_SCAN_CODE = "SCAN_CODE";
    //微信统一下单接口
    public static final String WX_TOTAL_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //------------------------------------------煎蛋 API start--------------------------------------
    public static String URL_JOKE = "http://ic.snssdk.com/neihan/stream/mix/v1/?" +
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
            "&manifest_version_code=431";


    //高德地图APP 包名
    public static final String GAODE_PACKAGE_NAME = "com.autonavi.minimap";
    //百度地图APP 包名
    public static final String BAIDU_PACKAGE_NAME = "com.baidu.BaiduMap";

    /**
     * 速度格式化
     */
    public static final DecimalFormat FORMAT_ONE = new DecimalFormat("#.#");
    /**
     * 距离格式化
     */
    public static final DecimalFormat FORMAT_TWO = new DecimalFormat("#.##");
    /**
     * 速度格式化
     */
    public static final DecimalFormat FORMAT_THREE = new DecimalFormat("#.###");

    //默认保存下载文件目录
    public static final String DOWNLOAD_DIR = RxFileTool.getRootPath() + File.separator + "Download" + File.separator + RxDeviceTool.getAppPackageName() + File.separator;

    //图片原始路径
    public static final String PICTURE_ORIGIN_PATH = RxFileTool.getRootPath() + File.separator + RxDeviceTool.getAppPackageName() + File.separator + "Picture" + File.separator+ "Origin" + File.separator;

    //图片压缩路径
    public static final String PICTURE_COMPRESS_PATH = RxFileTool.getRootPath() + File.separator + RxDeviceTool.getAppPackageName() + File.separator + "Picture" + File.separator+ "Compress" + File.separator;

    //默认导出文件目录
    public static final String EXPORT_FILE_PATH = RxFileTool.getRootPath() + File.separator + RxDeviceTool.getAppPackageName() + File.separator + "ExportFile" + File.separator;

    //图片名称
    public static final String getPictureName() {
        return RxTimeTool.getCurrentDateTime(DATE_FORMAT_LINK) + "_" + new Random().nextInt(1000) + ".jpg";
    }

    //Date格式
    public static final String DATE_FORMAT_LINK = "yyyyMMddHHmmssSSS";

    //Date格式 常用
    public static final String DATE_FORMAT_DETACH = "yyyy-MM-dd HH:mm:ss";

    //Date格式 带毫秒
    public static final String DATE_FORMAT_DETACH_SSS = "yyyy-MM-dd HH:mm:ss SSS";

    //时间格式 分钟：秒钟 一般用于视频时间显示
    public static final String DATE_FORMAT_MM_SS = "mm:ss";
}
