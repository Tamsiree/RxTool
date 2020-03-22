package com.tamsiree.rxui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.tamsiree.rxkit.RxLocationTool.isGpsEnabled
import com.tamsiree.rxkit.RxVibrateTool.vibrateOnce
import com.tamsiree.rxkit.model.Gps
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.view.dialog.RxDialogGPSCheck
import com.tamsiree.rxui.view.dialog.RxDialogTool

/**
 * @author tamsiree
 */
abstract class ActivityBaseLocation : ActivityBase() {
    //经度
    var mLongitude = 0.0

    //纬度
    var mLatitude = 0.0

    //GPS信息
    var mGps: Gps? = null
    var mLocationManager: LocationManager? = null

    private var mLocationListener: LocationListener? = null
    abstract fun setGpsInfo(location: Location?)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化GPS
        initGPS()
        //GPS开启状态检测
        gpsCheck()
    }

    private fun initGPS() {
        mLocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    //----------------------------------------------------------------------------------------------检测GPS是否已打开 start
    protected fun gpsCheck() {
        if (!isGpsEnabled(this)) {
            val rxDialogGPSCheck = RxDialogGPSCheck(mContext)
            rxDialogGPSCheck.show()
        } else {
            location
        }
    }

    //==============================================================================================检测GPS是否已打开 end
    @get:SuppressLint("MissingPermission")
    private val location: Unit
        get() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContext, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                RxDialogTool.initDialogSurePermission(mContext, "请先打开GPS定位权限")
                return
            }
            mLocationListener = object : LocationListener {
                @SuppressLint("MissingPermission")
                override fun onLocationChanged(location: Location) {
                    mLongitude = location.longitude
                    mLatitude = location.latitude
                    mGps = Gps(mLongitude, mLatitude)
                    setGpsInfo(location)
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                    when (status) {
                        LocationProvider.AVAILABLE -> {
                        }
                        LocationProvider.OUT_OF_SERVICE -> {
                            RxToast.normal("当前GPS信号弱")
                            vibrateOnce(mContext, 3000)
                        }
                        LocationProvider.TEMPORARILY_UNAVAILABLE -> RxToast.normal("当前GPS已暂停服务")
                        else -> {
                        }
                    }
                }

                override fun onProviderEnabled(provider: String) {
                    RxToast.normal("当前GPS设备已打开")
                    vibrateOnce(mContext, 800)
                }

                override fun onProviderDisabled(provider: String) {
                    RxToast.normal("当前GPS设备已关闭")
                    vibrateOnce(mContext, 800)
                    gpsCheck()
                }
            }
            mLocationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, mLocationListener)
        }

    override fun onDestroy() {
        super.onDestroy()
        if (mLocationListener != null) {
            mLocationManager!!.removeUpdates(mLocationListener)
        }
    }
}