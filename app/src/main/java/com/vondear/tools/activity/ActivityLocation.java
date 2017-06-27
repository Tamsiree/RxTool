package com.vondear.tools.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.vondear.rxtools.RxLocationUtils;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityLocation extends ActivityBase implements LocationListener {//原生的定位 需要手机设备GPS 很好

    @BindView(R.id.tv_about_location)
    TextView mTvAboutLocation;
    @BindView(R.id.layer_change_btn)
    LinearLayout mLayerChangeBtn;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);

        initLocation();
        gpsCheck();


        mTvAboutLocation.setText("lastLatitude: unknown"
                + "\nlastLongitude: unknown"
                + "\nlatitude: unknown"
                + "\nlongitude: unknown"
                + "\ngetCountryName: unknown"
                + "\ngetLocality: unknown"
                + "\ngetStreet: unknown"
        );

    }

    private void initLocation() {
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

    }

    //----------------------------------------------------------------------------------------------检测GPS是否已打开 start
    private void gpsCheck() {
        if (!RxLocationUtils.isGpsEnabled(this)) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            MaterialDialog materialDialog = builder.title("GPS未打开").content("您需要在系统设置中打开GPS方可采集数据").positiveText("去设置")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            RxLocationUtils.openGpsSettings(mContext);
                        }
                    }).build();
            materialDialog.setCanceledOnTouchOutside(false);
            materialDialog.setCancelable(false);
            materialDialog.show();
        } else {
            getLocation();
        }
    }
    //==============================================================================================检测GPS是否已打开 end

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        mTvAboutLocation.setText("经度: " + RxLocationUtils.gpsToDegree(location.getLongitude()) +
                "\n纬度: " + RxLocationUtils.gpsToDegree(location.getLatitude()) +
                "\n精度: " + location.getAccuracy() +
                "\n海拔: " + location.getAltitude() +
                "\n方位: " + location.getBearing() +
                "\n速度: " + location.getSpeed());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
