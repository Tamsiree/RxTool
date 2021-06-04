package com.tamsiree.rxkit

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tamsiree.rxkit.model.Gps
import com.tamsiree.rxkit.view.RxToast
import java.io.IOException
import java.text.DecimalFormat
import java.util.*
import kotlin.math.floor

/**
 * @author tamsiree
 * @date 2016/11/13
 * @desc 定位相关工具类
 */
object RxLocationTool {
    //圆周率
    const val pi = 3.1415926535897932384626

    //Krasovsky 1940 (北京54)椭球长半轴
    const val a = 6378245.0

    //椭球的偏心率
    const val ee = 0.00669342162296594323
    private var mListener: OnLocationChangeListener? = null
    private var myLocationListener: MyLocationListener? = null
    private var mLocationManager: LocationManager? = null

    /**
     * 判断Gps是否可用
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isGpsEnabled(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 判断定位是否可用
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isLocationEnabled(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 打开Gps设置界面
     */
    @JvmStatic
    fun openGpsSettings(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    /**
     * 注册
     *
     * 使用完记得调用[.unRegisterLocation]
     *
     * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>`
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>`
     *
     * 如果`minDistance`为0，则通过`minTime`来定时更新；
     *
     * `minDistance`不为0，则以`minDistance`为准；
     *
     * 两者都为0，则随时刷新。
     *
     * @param minTime     位置信息更新周期（单位：毫秒）
     * @param minDistance 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息（单位：米）
     * @param listener    位置刷新的回调接口
     * @return `true`: 初始化成功<br></br>`false`: 初始化失败
     */
    @JvmStatic
    fun registerLocation(context: Context, minTime: Long, minDistance: Long, listener: OnLocationChangeListener?): Boolean {
        if (listener == null) {
            return false
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((context as Activity), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return false
        }
        mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mListener = listener
        if (!isLocationEnabled(context)) {
            RxToast.showToast(context, "无法定位，请打开定位服务", 500)
            return false
        }
        val provider = mLocationManager!!.getBestProvider(criteria, true)
        val location = mLocationManager!!.getLastKnownLocation(provider!!)
        if (location != null) {
            listener.getLastKnownLocation(location)
        }
        if (myLocationListener == null) {
            myLocationListener = MyLocationListener()
        }
        mLocationManager!!.requestLocationUpdates(provider, minTime, minDistance.toFloat(), myLocationListener)
        return true
    }

    /**
     * 注销
     */
    @JvmStatic
    fun unRegisterLocation() {
        if (mLocationManager != null) {
            if (myLocationListener != null) {
                mLocationManager!!.removeUpdates(myLocationListener)
                myLocationListener = null
            }
            mLocationManager = null
        }
    }//设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
    //设置是否要求速度
    // 设置是否允许运营商收费
    //设置是否需要方位信息
    //设置是否需要海拔信息
    // 设置对电源的需求

    /**
     * 设置定位参数
     *
     * @return [Criteria]
     */
    private val criteria: Criteria
        private get() {
            val criteria = Criteria()
            //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
            criteria.accuracy = Criteria.ACCURACY_FINE
            //设置是否要求速度
            criteria.isSpeedRequired = true
            // 设置是否允许运营商收费
            criteria.isCostAllowed = true
            //设置是否需要方位信息
            criteria.isBearingRequired = true
            //设置是否需要海拔信息
            criteria.isAltitudeRequired = true
            // 设置对电源的需求
            criteria.powerRequirement = Criteria.POWER_LOW
            return criteria
        }

    /**
     * 根据经纬度获取地理位置
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return [Address]
     */
    @JvmStatic
    fun getAddress(context: Context?, latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.size > 0) {
                return addresses[0]
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 根据经纬度获取所在国家
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在国家
     */
    @JvmStatic
    fun getCountryName(context: Context?, latitude: Double, longitude: Double): String {
        val address = getAddress(context, latitude, longitude)
        return if (address == null) "unknown" else address.countryName
    }

    /**
     * 根据经纬度获取所在地
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在地
     */
    @JvmStatic
    fun getLocality(context: Context?, latitude: Double, longitude: Double): String {
        val address = getAddress(context, latitude, longitude)
        return if (address == null) "unknown" else address.locality
    }

    /**
     * 根据经纬度获取所在街道
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在街道
     */
    @JvmStatic
    fun getStreet(context: Context?, latitude: Double, longitude: Double): String {
        val address = getAddress(context, latitude, longitude)
        return if (address == null) "unknown" else address.getAddressLine(0)
    }
    //------------------------------------------坐标转换工具start--------------------------------------
    /**
     * GPS坐标 转换成 角度
     * 例如 113.202222 转换成 113°12′8″
     *
     * @param location
     * @return
     */
    @JvmStatic
    fun gpsToDegree(location: Double): String {
        val degree = floor(location)
        val minute_temp = (location - degree) * 60
        val minute = floor(minute_temp)
        //        double second = Math.floor((minute_temp - minute)*60);
        val second = DecimalFormat("#.##").format((minute_temp - minute) * 60)
        return (degree.toInt()).toString() + "°" + minute.toInt() + "′" + second + "″"
    }

    /**
     * 国际 GPS84 坐标系
     * 转换成
     * [国测局坐标系] 火星坐标系 (GCJ-02)
     *
     *
     * World Geodetic System ==> Mars Geodetic System
     *
     * @param lon 经度
     * @param lat 纬度
     * @return GPS实体类
     */
    @JvmStatic
    fun GPS84ToGCJ02(lon: Double, lat: Double): Gps? {
        if (outOfChina(lon, lat)) {
            return null
        }
        var dLat = transformLat(lon - 105.0, lat - 35.0)
        var dLon = transformLon(lon - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * pi
        var magic = Math.sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = Math.sqrt(magic)
        dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
        dLon = dLon * 180.0 / (a / sqrtMagic * Math.cos(radLat) * pi)
        val mgLat = lat + dLat
        val mgLon = lon + dLon
        return Gps(mgLon, mgLat)
    }

    /**
     * [国测局坐标系] 火星坐标系 (GCJ-02)
     * 转换成
     * 国际 GPS84 坐标系
     *
     * @param lon 火星经度
     * @param lat 火星纬度
     */
    @JvmStatic
    fun GCJ02ToGPS84(lon: Double, lat: Double): Gps {
        val gps = transform(lon, lat)
        val lontitude = lon * 2 - gps.longitude
        val latitude = lat * 2 - gps.latitude
        return Gps(lontitude, latitude)
    }

    /**
     * 火星坐标系 (GCJ-02)
     * 转换成
     * 百度坐标系 (BD-09)
     *
     * @param ggLon 经度
     * @param ggLat 纬度
     */
    @JvmStatic
    fun GCJ02ToBD09(ggLon: Double, ggLat: Double): Gps {
        val z = Math.sqrt(ggLon * ggLon + ggLat * ggLat) + 0.00002 * Math.sin(ggLat * pi)
        val theta = Math.atan2(ggLat, ggLon) + 0.000003 * Math.cos(ggLon * pi)
        val bdLon = z * Math.cos(theta) + 0.0065
        val bdLat = z * Math.sin(theta) + 0.006
        return Gps(bdLon, bdLat)
    }

    /**
     * 百度坐标系 (BD-09)
     * 转换成
     * 火星坐标系 (GCJ-02)
     *
     * @param bdLon 百度*经度
     * @param bdLat 百度*纬度
     * @return GPS实体类
     */
    @JvmStatic
    fun BD09ToGCJ02(bdLon: Double, bdLat: Double): Gps {
        val x = bdLon - 0.0065
        val y = bdLat - 0.006
        val z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi)
        val theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi)
        val ggLon = z * Math.cos(theta)
        val ggLat = z * Math.sin(theta)
        return Gps(ggLon, ggLat)
    }

    /**
     * 百度坐标系 (BD-09)
     * 转换成
     * 国际 GPS84 坐标系
     *
     * @param bdLon 百度*经度
     * @param bdLat 百度*纬度
     * @return GPS实体类
     */
    @JvmStatic
    fun BD09ToGPS84(bdLon: Double, bdLat: Double): Gps {
        val gcj02 = BD09ToGCJ02(bdLon, bdLat)
        return GCJ02ToGPS84(gcj02.longitude, gcj02.latitude)
    }

    /**
     * 国际 GPS84 坐标系
     * 转换成
     * 百度坐标系 (BD-09)
     *
     * @param gpsLon  国际 GPS84 坐标系下 的经度
     * @param gpsLat  国际 GPS84 坐标系下 的纬度
     * @return 百度GPS坐标
     */
    @JvmStatic
    fun GPS84ToBD09(gpsLon: Double, gpsLat: Double): Gps {
        val gcj02 = GPS84ToGCJ02(gpsLon, gpsLat)
        return GCJ02ToBD09(gcj02!!.longitude, gcj02.latitude)
    }

    /**
     * 不在中国范围内
     *
     * @param lon 经度
     * @param lat 纬度
     * @return boolean值
     */
    @JvmStatic
    fun outOfChina(lon: Double, lat: Double): Boolean {
        return lon < 72.004 || lon > 137.8347 || lat < 0.8293 || lat > 55.8271
    }

    /**
     * 转化算法
     *
     * @param lon 经度
     * @param lat 纬度
     * @return  GPS信息
     */
    private fun transform(lon: Double, lat: Double): Gps {
        if (outOfChina(lon, lat)) {
            return Gps(lon, lat)
        }
        var dLat = transformLat(lon - 105.0, lat - 35.0)
        var dLon = transformLon(lon - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * pi
        var magic = Math.sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = Math.sqrt(magic)
        dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
        dLon = dLon * 180.0 / (a / sqrtMagic * Math.cos(radLat) * pi)
        val mgLat = lat + dLat
        val mgLon = lon + dLon
        return Gps(mgLon, mgLat)
    }

    /**
     * 纬度转化算法
     *
     * @param x x坐标
     * @param y y坐标
     * @return  纬度
     */
    private fun transformLat(x: Double, y: Double): Double {
        var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x))
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0
        return ret
    }

    /**
     * 经度转化算法
     *
     * @param x  x坐标
     * @param y  y坐标
     * @return 经度
     */
    private fun transformLon(x: Double, y: Double): Double {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + (0.1
                * Math.sqrt(Math.abs(x)))
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0
        return ret
    }

    //===========================================坐标转换工具end====================================
    @JvmStatic
    fun isMove(location: Location, preLocation: Location?): Boolean {
        val isMove: Boolean
        if (preLocation != null) {
            val speed = location.speed * 3.6
            val distance = location.distanceTo(preLocation).toDouble()
            val compass = Math.abs(preLocation.bearing - location.bearing).toDouble()
            val angle: Double
            angle = if (compass > 180) {
                360 - compass
            } else {
                compass
            }
            isMove = if (speed != 0.0) {
                if (speed < 35 && distance > 3 && distance < 10) {
                    angle > 10
                } else {
                    speed < 40 && distance > 10 && distance < 100 ||
                            speed < 50 && distance > 10 && distance < 100 ||
                            speed < 60 && distance > 10 && distance < 100 ||
                            speed < 9999 && distance > 100
                }
            } else {
                false
            }
        } else {
            isMove = true
        }
        return isMove
    }

    interface OnLocationChangeListener {
        /**
         * 获取最后一次保留的坐标
         *
         * @param location 坐标
         */
        fun getLastKnownLocation(location: Location)

        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        fun onLocationChanged(location: Location)

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) //位置状态发生改变
    }

    private class MyLocationListener : LocationListener {
        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        override fun onLocationChanged(location: Location) {
            if (mListener != null) {
                mListener!!.onLocationChanged(location)
            }
        }

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            if (mListener != null) {
                mListener!!.onStatusChanged(provider, status, extras)
            }
            when (status) {
                LocationProvider.AVAILABLE -> TLog.d("onStatusChanged", "当前GPS状态为可见状态")
                LocationProvider.OUT_OF_SERVICE -> TLog.d("onStatusChanged", "当前GPS状态为服务区外状态")
                LocationProvider.TEMPORARILY_UNAVAILABLE -> TLog.d("onStatusChanged", "当前GPS状态为暂停服务状态")
                else -> {
                }
            }
        }

        /**
         * provider被enable时触发此函数，比如GPS被打开
         */
        override fun onProviderEnabled(provider: String) {}

        /**
         * provider被disable时触发此函数，比如GPS被关闭
         */
        override fun onProviderDisabled(provider: String) {}
    }
}