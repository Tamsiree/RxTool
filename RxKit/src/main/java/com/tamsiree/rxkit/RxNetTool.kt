package com.tamsiree.rxkit

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.tamsiree.rxkit.view.RxToast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * @author tamsiree
 * @date 2016/1/29
 */
object RxNetTool {
    /**
     * no network
     */
    const val NETWORK_NO = -1

    /**
     * wifi network
     */
    const val NETWORK_WIFI = 1

    /**
     * "2G" networks
     */
    const val NETWORK_2G = 2

    /**
     * "3G" networks
     */
    const val NETWORK_3G = 3

    /**
     * "4G" networks
     */
    const val NETWORK_4G = 4

    /**
     * unknown network
     */
    const val NETWORK_UNKNOWN = 5
    private const val NETWORK_TYPE_GSM = 16
    private const val NETWORK_TYPE_TD_SCDMA = 17
    private const val NETWORK_TYPE_IWLAN = 18

    /**
     * 需添加权限
     *
     * @param context 上下文
     * @return 网络类型
     * @code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
     *
     *
     * 它主要负责的是
     * 1 监视网络连接状态 包括（Wi-Fi, 2G, 3G, 4G）
     * 2 当网络状态改变时发送广播通知
     * 3 网络连接失败尝试连接其他网络
     * 4 提供API，允许应用程序获取可用的网络状态
     *
     *
     * netTyped 的结果
     * @link #NETWORK_NO      = -1; 当前无网络连接
     * @link #NETWORK_WIFI    =  1; wifi的情况下
     * @link #NETWORK_2G      =  2; 切换到2G环境下
     * @link #NETWORK_3G      =  3; 切换到3G环境下
     * @link #NETWORK_4G      =  4; 切换到4G环境下
     * @link #NETWORK_UNKNOWN =  5; 未知网络
     */
    @JvmStatic
    fun getNetWorkType(context: Context): Int {
        // 获取ConnectivityManager
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo // 获取当前网络状态
        var netType = NETWORK_NO
        if (ni != null && ni.isConnectedOrConnecting) {
            when (ni.type) {
                ConnectivityManager.TYPE_WIFI -> {
                    netType = NETWORK_WIFI
                    RxToast.success("切换到wifi环境下")
                }
                ConnectivityManager.TYPE_MOBILE -> when (ni.subtype) {
                    NETWORK_TYPE_GSM, TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> {
                        netType = NETWORK_2G
                        RxToast.info("切换到2G环境下")
                    }
                    TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP, NETWORK_TYPE_TD_SCDMA -> {
                        netType = NETWORK_3G
                        RxToast.info("切换到3G环境下")
                    }
                    TelephonyManager.NETWORK_TYPE_LTE, NETWORK_TYPE_IWLAN -> {
                        netType = NETWORK_4G
                        RxToast.info("切换到4G环境下")
                    }
                    else -> {
                        val subtypeName = ni.subtypeName
                        netType = if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                                || subtypeName.equals("WCDMA", ignoreCase = true)
                                || subtypeName.equals("CDMA2000", ignoreCase = true)) {
                            NETWORK_3G
                        } else {
                            NETWORK_UNKNOWN
                        }
                        RxToast.normal("未知网络")
                    }
                }
                else -> {
                    netType = 5
                    RxToast.normal("未知网络")
                }
            }
        } else {
            netType = NETWORK_NO
            RxToast.error(context, "当前无网络连接")?.show()
        }
        return netType
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     *
     * 依赖上面的方法
     *
     * @param context 上下文
     * @return 网络类型名称
     *
     *  * NETWORK_WIFI
     *  * NETWORK_4G
     *  * NETWORK_3G
     *  * NETWORK_2G
     *  * NETWORK_UNKNOWN
     *  * NETWORK_NO
     *
     */
    @JvmStatic
    fun getNetWorkTypeName(context: Context): String {
        return when (getNetWorkType(context)) {
            NETWORK_WIFI -> "NETWORK_WIFI"
            NETWORK_4G -> "NETWORK_4G"
            NETWORK_3G -> "NETWORK_3G"
            NETWORK_2G -> "NETWORK_2G"
            NETWORK_NO -> "NETWORK_NO"
            else -> "NETWORK_UNKNOWN"
        }
    }

    /**
     * 判断网络连接是否可用
     */
    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            val info = cm.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * 判断网络是否可用
     * 需添加权限
     *
     * @code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
     */
    @JvmStatic
    fun isAvailable(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isAvailable
    }

    /**
     * 判断网络是否连接
     * 需添加权限
     *
     * @code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
     */
    @JvmStatic
    fun isConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     * 不要在主线程使用，会阻塞线程
     */
    @JvmStatic
    fun ping(): Boolean {
        var result: String? = null
        try {
            val ip = "www.baidu.com" // ping 的地址，可以换成任何一种可靠的外网
            val p = Runtime.getRuntime().exec("ping -c 3 -w 100 $ip") // ping网址3次
            // 读取ping的内容，可以不加
            val input = p.inputStream
            val `in` = BufferedReader(InputStreamReader(input))
            val stringBuffer = StringBuffer()
            var content: String? = ""
            while (`in`.readLine().also { content = it } != null) {
                stringBuffer.append(content)
            }
            //            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            val status = p.waitFor()
            if (status == 0) {
                result = "success"
                return true
            } else {
                result = "failed"
            }
        } catch (e: IOException) {
            result = "IOException"
        } catch (e: InterruptedException) {
            result = "InterruptedException"
        } finally {
//            Log.d("----result---", "result = " + result);
        }
        return false
    }

    /**
     * ping IP
     * 不要在主线程使用，会阻塞线程
     */
    @JvmStatic
    fun ping(ip: String): Boolean {
        var result: String? = null
        try {
            // ping网址3次
            val p = Runtime.getRuntime().exec("ping -c 3 -w 100 $ip")
            // 读取ping的内容，可以不加
            val input = p.inputStream
            val `in` = BufferedReader(InputStreamReader(input))
            val stringBuffer = StringBuffer()
            var content: String? = ""
            while (`in`.readLine().also { content = it } != null) {
                stringBuffer.append(content)
            }
            //Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            val status = p.waitFor()
            if (status == 0) {
                result = "success"
                return true
            } else {
                result = "failed"
            }
        } catch (e: IOException) {
            result = "IOException"
        } catch (e: InterruptedException) {
            result = "InterruptedException"
        } finally {
            //Log.d("----result---", "result = " + result);
        }
        return false
    }

    /**
     * 判断WIFI是否打开
     */
    @JvmStatic
    fun isWifiEnabled(context: Context): Boolean {
        val mgrConn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mgrTel = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return ((mgrConn.activeNetworkInfo != null
                && mgrConn.activeNetworkInfo.state == NetworkInfo.State.CONNECTED)
                || mgrTel.networkType == TelephonyManager.NETWORK_TYPE_UMTS)
    }

    /**
     * 判断网络连接方式是否为WIFI
     */
    @JvmStatic
    fun isWifi(context: Context): Boolean {
        val networkINfo = getActiveNetworkInfo(context)
        return networkINfo != null && networkINfo.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 判断wifi是否连接状态
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @param context 上下文
     * @return `true`: 连接<br></br>`false`: 未连接
     */
    @JvmStatic
    fun isWifiConnected(context: Context): Boolean {
        val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm != null && cm.activeNetworkInfo != null && cm.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 判断是否为3G网络
     */
    @JvmStatic
    fun is3rd(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkINfo = cm.activeNetworkInfo
        return (networkINfo != null
                && networkINfo.type == ConnectivityManager.TYPE_MOBILE)
    }

    /**
     * 判断网络是否是4G
     * 需添加权限
     *
     * @code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
     */
    @JvmStatic
    fun is4G(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isAvailable && info.subtype == TelephonyManager.NETWORK_TYPE_LTE
    }

    /**
     * GPS是否打开
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun isGpsEnabled(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val accessibleProviders = lm.getProviders(true)
        return accessibleProviders != null && accessibleProviders.size > 0
    }
    /*
     * 下面列举几个可直接跳到联网设置的意图,供大家学习
     *
     *      startActivity(new Intent(android.provider.Settings.ACTION_APN_SETTINGS));
     *      startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
     *
     *  用下面两种方式设置网络
     *
     *      startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
     *      startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
     */
    /**
     * 打开网络设置界面
     *
     * 3.0以下打开设置界面
     *
     * @param context 上下文
     */
    @JvmStatic
    fun openWirelessSettings(context: Context) {
        if (Build.VERSION.SDK_INT > 10) {
            context.startActivity(Intent(Settings.ACTION_SETTINGS))
        } else {
            context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        }
    }

    /**
     * 获取活动网络信息
     *
     * @param context 上下文
     * @return NetworkInfo
     */
    private fun getActiveNetworkInfo(context: Context): NetworkInfo {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * 获取移动网络运营商名称
     *
     * 如中国联通、中国移动、中国电信
     *
     * @param context 上下文
     * @return 移动网络运营商名称
     */
    @JvmStatic
    fun getNetworkOperatorName(context: Context): String? {
        val tm = context
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkOperatorName
    }

    /**
     * 获取移动终端类型
     *
     * @param context 上下文
     * @return 手机制式
     *
     *  * [TelephonyManager.PHONE_TYPE_NONE] : 0 手机制式未知
     *  * [TelephonyManager.PHONE_TYPE_GSM] : 1 手机制式为GSM，移动和联通
     *  * [TelephonyManager.PHONE_TYPE_CDMA] : 2 手机制式为CDMA，电信
     *  * [TelephonyManager.PHONE_TYPE_SIP] : 3
     *
     */
    @JvmStatic
    fun getPhoneType(context: Context): Int {
        val tm = context
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.phoneType
    }
}