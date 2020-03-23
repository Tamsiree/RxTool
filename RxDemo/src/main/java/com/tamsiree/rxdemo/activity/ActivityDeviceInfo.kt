package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.View
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.buildBrand
import com.tamsiree.rxkit.RxDeviceTool.buildBrandModel
import com.tamsiree.rxkit.RxDeviceTool.buildMANUFACTURER
import com.tamsiree.rxkit.RxDeviceTool.getAndroidId
import com.tamsiree.rxkit.RxDeviceTool.getAppVersionName
import com.tamsiree.rxkit.RxDeviceTool.getAppVersionNo
import com.tamsiree.rxkit.RxDeviceTool.getDeviceIdIMEI
import com.tamsiree.rxkit.RxDeviceTool.getDeviceSoftwareVersion
import com.tamsiree.rxkit.RxDeviceTool.getIMSI
import com.tamsiree.rxkit.RxDeviceTool.getLine1Number
import com.tamsiree.rxkit.RxDeviceTool.getMacAddress
import com.tamsiree.rxkit.RxDeviceTool.getNetworkCountryIso
import com.tamsiree.rxkit.RxDeviceTool.getNetworkOperator
import com.tamsiree.rxkit.RxDeviceTool.getNetworkOperatorName
import com.tamsiree.rxkit.RxDeviceTool.getPhoneType
import com.tamsiree.rxkit.RxDeviceTool.getScreenDensity
import com.tamsiree.rxkit.RxDeviceTool.getScreenHeight
import com.tamsiree.rxkit.RxDeviceTool.getScreenWidth
import com.tamsiree.rxkit.RxDeviceTool.getSimOperator
import com.tamsiree.rxkit.RxDeviceTool.getSimOperatorName
import com.tamsiree.rxkit.RxDeviceTool.getSimSerialNumber
import com.tamsiree.rxkit.RxDeviceTool.getSimState
import com.tamsiree.rxkit.RxDeviceTool.getSubscriberId
import com.tamsiree.rxkit.RxDeviceTool.getVoiceMailNumber
import com.tamsiree.rxkit.RxDeviceTool.isPhone
import com.tamsiree.rxkit.RxDeviceTool.serialNumber
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxDeviceTool.uniqueSerialNumber
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_device_info.*

/**
 * @author tamsiree
 */
class ActivityDeviceInfo : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        btn_get_phone_info.setOnClickListener {
            ll_info_root.visibility = View.VISIBLE
            phoneInfo
            btn_get_phone_info.visibility = View.GONE
        }
    }

    override fun initData() {

    }

    private val phoneInfo: Unit
        get() {
            if (isPhone(mContext)) {
                tv_device_is_phone.text = "是"
            } else {
                tv_device_is_phone.text = "否"
            }
            tv_device_software_phone_type.text = getPhoneType(mContext).toString()
            tv_device_density.text = getScreenDensity(mContext).toString()
            tv_device_manu_facturer.text = uniqueSerialNumber
            tv_device_width.text = getScreenWidth(mContext).toString()
            tv_device_height.text = getScreenHeight(mContext).toString()
            tv_device_version_name.text = getAppVersionName(mContext)
            tv_device_version_code.text = getAppVersionNo(mContext).toString()
            tv_device_imei.text = getDeviceIdIMEI(mContext)
            tv_device_imsi.text = getIMSI(mContext)
            tv_device_software_version.text = getDeviceSoftwareVersion(mContext)
            tv_device_mac.text = getMacAddress(mContext)
            tv_device_software_mcc_mnc.text = getNetworkOperator(mContext)
            tv_device_software_mcc_mnc_name.text = getNetworkOperatorName(mContext)
            tv_device_software_sim_country_iso.text = getNetworkCountryIso(mContext)
            tv_device_sim_operator.text = getSimOperator(mContext)
            tv_device_sim_serial_number.text = getSimSerialNumber(mContext)
            tv_device_sim_state.text = getSimState(mContext).toString()
            tv_device_sim_operator_name.text = getSimOperatorName(mContext)
            tv_device_subscriber_id.text = getSubscriberId(mContext)
            tv_device_voice_mail_number.text = getVoiceMailNumber(mContext)
            tv_device_adnroid_id.text = getAndroidId(mContext)
            tv_device_build_brand_model.text = buildBrandModel
            tv_device_build_manu_facturer.text = buildMANUFACTURER
            tv_device_build_brand.text = buildBrand
            tv_device_serial_number.text = serialNumber
            tv_device_iso.text = getNetworkCountryIso(mContext)
            tv_device_phone.text = getLine1Number(mContext)
        }

}