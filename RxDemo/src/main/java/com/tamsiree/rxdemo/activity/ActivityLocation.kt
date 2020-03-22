package com.tamsiree.rxdemo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.GpsStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxLocationTool.gpsToDegree
import com.tamsiree.rxkit.RxLocationTool.isGpsEnabled
import com.tamsiree.rxkit.RxLocationTool.openGpsSettings
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_location.*

/**
 * 原生的定位 需要手机设备GPS 很好
 * @author tamsiree
 */
class ActivityLocation : ActivityBase(), LocationListener {

    private var locationManager: LocationManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        setPortrait(this)

    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        initLocation()
        gpsCheck()
        tv_about_location.text = """
            lastLatitude: unknown
            lastLongitude: unknown
            latitude: unknown
            longitude: unknown
            getCountryName: unknown
            getLocality: unknown
            getStreet: unknown
            """.trimIndent()
    }

    private fun initLocation() {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    //----------------------------------------------------------------------------------------------检测GPS是否已打开 start
    private fun gpsCheck() {
        if (!isGpsEnabled(this)) {
            val builder = MaterialDialog.Builder(this)
            val materialDialog = builder.title("GPS未打开").content("您需要在系统设置中打开GPS方可采集数据").positiveText("去设置")
                    .onPositive { dialog, which -> openGpsSettings(mContext) }.build()
            materialDialog.setCanceledOnTouchOutside(false)
            materialDialog.setCancelable(false)
            materialDialog.show()
        } else {
            location
        }
    }

    //==============================================================================================检测GPS是否已打开 end
    private val location: Unit
        private get() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContext, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                return
            }
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, this)
            locationManager!!.addGpsStatusListener(GpsStatus.Listener { event ->
                when (event) {
                    GpsStatus.GPS_EVENT_STARTED -> {
                        println("GPS_EVENT_STARTED")
                        gps_count.text = "0"
                    }
                    GpsStatus.GPS_EVENT_FIRST_FIX -> println("GPS_EVENT_FIRST_FIX")
                    GpsStatus.GPS_EVENT_SATELLITE_STATUS -> {
                        println("GPS_EVENT_SATELLITE_STATUS")
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return@Listener
                        }
                        val gpsStatus = locationManager!!.getGpsStatus(null)
                        val gpsSatellites = gpsStatus.satellites
                        var count = 0
                        val iterator: Iterator<*> = gpsSatellites.iterator()
                        while (iterator.hasNext()) {
                            count++
                            iterator.next()
                        }
                        gps_count.text = count.toString() + ""
                    }
                    GpsStatus.GPS_EVENT_STOPPED -> println("GPS_EVENT_STOPPED")
                }
            })
        }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
        tv_about_location.text = """
            经度: ${gpsToDegree(location.longitude)}
            纬度: ${gpsToDegree(location.latitude)}
            精度: ${location.accuracy}
            海拔: ${location.altitude}
            方位: ${location.bearing}
            速度: ${location.speed}
            """.trimIndent()
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}