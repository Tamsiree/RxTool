package com.vondear.tools.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.tools.R;
import com.vondear.rxtools.RxDeviceUtils;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDeviceInfo extends ActivityBase {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.btn_get_phone_info)
    Button mBtnGetPhoneInfo;
    @BindView(R.id.tv_device_is_phone)
    TextView mTvDeviceIsPhone;
    @BindView(R.id.tv_device_software_phone_type)
    TextView mTvDeviceSoftwarePhoneType;
    @BindView(R.id.tv_device_density)
    TextView mTvDeviceDensity;
    @BindView(R.id.tv_device_manu_facturer)
    TextView mTvDeviceManuFacturer;
    @BindView(R.id.tv_device_width)
    TextView mTvDeviceWidth;
    @BindView(R.id.tv_device_height)
    TextView mTvDeviceHeight;
    @BindView(R.id.tv_device_version_name)
    TextView mTvDeviceVersionName;
    @BindView(R.id.tv_device_version_code)
    TextView mTvDeviceVersionCode;
    @BindView(R.id.tv_device_imei)
    TextView mTvDeviceImei;
    @BindView(R.id.tv_device_imsi)
    TextView mTvDeviceImsi;
    @BindView(R.id.tv_device_software_version)
    TextView mTvDeviceSoftwareVersion;
    @BindView(R.id.tv_device_mac)
    TextView mTvDeviceMac;
    @BindView(R.id.tv_device_software_mcc_mnc)
    TextView mTvDeviceSoftwareMccMnc;
    @BindView(R.id.tv_device_software_mcc_mnc_name)
    TextView mTvDeviceSoftwareMccMncName;
    @BindView(R.id.tv_device_software_sim_country_iso)
    TextView mTvDeviceSoftwareSimCountryIso;
    @BindView(R.id.tv_device_sim_operator)
    TextView mTvDeviceSimOperator;
    @BindView(R.id.tv_device_sim_serial_number)
    TextView mTvDeviceSimSerialNumber;
    @BindView(R.id.tv_device_sim_state)
    TextView mTvDeviceSimState;
    @BindView(R.id.tv_device_sim_operator_name)
    TextView mTvDeviceSimOperatorName;
    @BindView(R.id.tv_device_subscriber_id)
    TextView mTvDeviceSubscriberId;
    @BindView(R.id.tv_device_voice_mail_number)
    TextView mTvDeviceVoiceMailNumber;
    @BindView(R.id.tv_device_adnroid_id)
    TextView mTvDeviceAdnroidId;
    @BindView(R.id.tv_device_build_brand_model)
    TextView mTvDeviceBuildBrandModel;
    @BindView(R.id.tv_device_build_manu_facturer)
    TextView mTvDeviceBuildManuFacturer;
    @BindView(R.id.tv_device_build_brand)
    TextView mTvDeviceBuildBrand;
    @BindView(R.id.tv_device_serial_number)
    TextView mTvDeviceSerialNumber;
    @BindView(R.id.tv_device_iso)
    TextView mTvDeviceIso;
    @BindView(R.id.tv_device_phone)
    TextView mTvDevicePhone;
    @BindView(R.id.ll_info_root)
    LinearLayout mLlInfoRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
    }

    private void getPhoneInfo() {
        if (RxDeviceUtils.isPhone(mContext)) {
            mTvDeviceIsPhone.setText("是");
        } else {
            mTvDeviceIsPhone.setText("否");
        }

        mTvDeviceSoftwarePhoneType.setText(RxDeviceUtils.getPhoneType(mContext) + "");
        mTvDeviceDensity.setText(RxDeviceUtils.getScreenDensity(mContext) + "");
        mTvDeviceManuFacturer.setText(RxDeviceUtils.getUniqueSerialNumber() + "");
        mTvDeviceWidth.setText(RxDeviceUtils.getScreenWidth(mContext) + " ");
        mTvDeviceHeight.setText(RxDeviceUtils.getScreenHeight(mContext) + " ");
        mTvDeviceVersionName.setText(RxDeviceUtils.getAppVersionName(mContext) + "");
        mTvDeviceVersionCode.setText(RxDeviceUtils.getAppVersionNo(mContext) + "");
        mTvDeviceImei.setText(RxDeviceUtils.getDeviceIdIMEI(mContext) + "");
        mTvDeviceImsi.setText(RxDeviceUtils.getIMSI(mContext) + "");
        mTvDeviceSoftwareVersion.setText(RxDeviceUtils.getDeviceSoftwareVersion(mContext) + "");
        mTvDeviceMac.setText(RxDeviceUtils.getMacAddress(mContext));
        mTvDeviceSoftwareMccMnc.setText(RxDeviceUtils.getNetworkOperator(mContext) + "");
        mTvDeviceSoftwareMccMncName.setText(RxDeviceUtils.getNetworkOperatorName(mContext) + "");
        mTvDeviceSoftwareSimCountryIso.setText(RxDeviceUtils.getNetworkCountryIso(mContext) + "");
        mTvDeviceSimOperator.setText(RxDeviceUtils.getSimOperator(mContext) + "");
        mTvDeviceSimSerialNumber.setText(RxDeviceUtils.getSimSerialNumber(mContext) + "");
        mTvDeviceSimState.setText(RxDeviceUtils.getSimState(mContext) + "");
        mTvDeviceSimOperatorName.setText(RxDeviceUtils.getSimOperatorName(mContext) + "");
        mTvDeviceSubscriberId.setText(RxDeviceUtils.getSubscriberId(mContext) + "");
        mTvDeviceVoiceMailNumber.setText(RxDeviceUtils.getVoiceMailNumber(mContext) + "");
        mTvDeviceAdnroidId.setText(RxDeviceUtils.getAndroidId(mContext) + "");
        mTvDeviceBuildBrandModel.setText(RxDeviceUtils.getBuildBrandModel() + "");
        mTvDeviceBuildManuFacturer.setText(RxDeviceUtils.getBuildMANUFACTURER() + "");
        mTvDeviceBuildBrand.setText(RxDeviceUtils.getBuildBrand() + "");
        mTvDeviceSerialNumber.setText(RxDeviceUtils.getSerialNumber() + "");
        mTvDeviceIso.setText(RxDeviceUtils.getNetworkCountryIso(mContext) + "");
        mTvDevicePhone.setText(RxDeviceUtils.getLine1Number(mContext) + "");
    }

    @OnClick(R.id.btn_get_phone_info)
    public void onViewClicked() {
        mLlInfoRoot.setVisibility(View.VISIBLE);
        getPhoneInfo();
        mBtnGetPhoneInfo.setVisibility(View.GONE);
    }
}
