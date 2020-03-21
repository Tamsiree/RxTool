package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
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
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityDeviceInfo : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.btn_get_phone_info)
    var mBtnGetPhoneInfo: Button? = null

    @JvmField
    @BindView(R.id.tv_device_is_phone)
    var mTvDeviceIsPhone: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_software_phone_type)
    var mTvDeviceSoftwarePhoneType: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_density)
    var mTvDeviceDensity: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_manu_facturer)
    var mTvDeviceManuFacturer: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_width)
    var mTvDeviceWidth: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_height)
    var mTvDeviceHeight: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_version_name)
    var mTvDeviceVersionName: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_version_code)
    var mTvDeviceVersionCode: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_imei)
    var mTvDeviceImei: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_imsi)
    var mTvDeviceImsi: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_software_version)
    var mTvDeviceSoftwareVersion: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_mac)
    var mTvDeviceMac: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_software_mcc_mnc)
    var mTvDeviceSoftwareMccMnc: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_software_mcc_mnc_name)
    var mTvDeviceSoftwareMccMncName: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_software_sim_country_iso)
    var mTvDeviceSoftwareSimCountryIso: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_sim_operator)
    var mTvDeviceSimOperator: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_sim_serial_number)
    var mTvDeviceSimSerialNumber: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_sim_state)
    var mTvDeviceSimState: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_sim_operator_name)
    var mTvDeviceSimOperatorName: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_subscriber_id)
    var mTvDeviceSubscriberId: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_voice_mail_number)
    var mTvDeviceVoiceMailNumber: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_adnroid_id)
    var mTvDeviceAdnroidId: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_build_brand_model)
    var mTvDeviceBuildBrandModel: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_build_manu_facturer)
    var mTvDeviceBuildManuFacturer: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_build_brand)
    var mTvDeviceBuildBrand: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_serial_number)
    var mTvDeviceSerialNumber: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_iso)
    var mTvDeviceIso: TextView? = null

    @JvmField
    @BindView(R.id.tv_device_phone)
    var mTvDevicePhone: TextView? = null

    @JvmField
    @BindView(R.id.ll_info_root)
    var mLlInfoRoot: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
    }

    private val phoneInfo: Unit
        get() {
            if (isPhone(mContext!!)) {
                mTvDeviceIsPhone!!.text = "是"
            } else {
                mTvDeviceIsPhone!!.text = "否"
            }
            mTvDeviceSoftwarePhoneType!!.text = getPhoneType(mContext!!).toString() + ""
            mTvDeviceDensity!!.text = getScreenDensity(mContext!!).toString() + ""
            mTvDeviceManuFacturer!!.text = uniqueSerialNumber + ""
            mTvDeviceWidth!!.text = getScreenWidth(mContext!!).toString() + " "
            mTvDeviceHeight!!.text = getScreenHeight(mContext!!).toString() + " "
            mTvDeviceVersionName!!.text = getAppVersionName(mContext) + ""
            mTvDeviceVersionCode!!.text = getAppVersionNo(mContext!!).toString() + ""
            mTvDeviceImei!!.text = getDeviceIdIMEI(mContext!!) + ""
            mTvDeviceImsi!!.text = getIMSI(mContext!!) + ""
            mTvDeviceSoftwareVersion!!.text = getDeviceSoftwareVersion(mContext!!) + ""
            mTvDeviceMac!!.text = getMacAddress(mContext!!)
            mTvDeviceSoftwareMccMnc!!.text = getNetworkOperator(mContext!!) + ""
            mTvDeviceSoftwareMccMncName!!.text = getNetworkOperatorName(mContext!!) + ""
            mTvDeviceSoftwareSimCountryIso!!.text = getNetworkCountryIso(mContext!!) + ""
            mTvDeviceSimOperator!!.text = getSimOperator(mContext!!) + ""
            mTvDeviceSimSerialNumber!!.text = getSimSerialNumber(mContext!!) + ""
            mTvDeviceSimState!!.text = getSimState(mContext!!).toString() + ""
            mTvDeviceSimOperatorName!!.text = getSimOperatorName(mContext!!) + ""
            mTvDeviceSubscriberId!!.text = getSubscriberId(mContext!!) + ""
            mTvDeviceVoiceMailNumber!!.text = getVoiceMailNumber(mContext!!) + ""
            mTvDeviceAdnroidId!!.text = getAndroidId(mContext!!) + ""
            mTvDeviceBuildBrandModel!!.text = buildBrandModel + ""
            mTvDeviceBuildManuFacturer!!.text = buildMANUFACTURER + ""
            mTvDeviceBuildBrand!!.text = buildBrand + ""
            mTvDeviceSerialNumber!!.text = serialNumber + ""
            mTvDeviceIso!!.text = getNetworkCountryIso(mContext!!) + ""
            mTvDevicePhone!!.text = getLine1Number(mContext!!) + ""
        }

    @OnClick(R.id.btn_get_phone_info)
    fun onViewClicked() {
        mLlInfoRoot!!.visibility = View.VISIBLE
        phoneInfo
        mBtnGetPhoneInfo!!.visibility = View.GONE
    }
}