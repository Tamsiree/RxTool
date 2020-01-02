package com.vondear.rxtool.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;


import androidx.annotation.Nullable;

import com.vondear.rxtool.RxLocationTool;
import com.vondear.rxtool.view.RxToast;

/**
 * @author vondear
 */
public class RxServiceLocation extends Service {

    private boolean isSuccess;
    private String lastLatitude  = "loading...";
    private String lastLongitude = "loading...";
    private String latitude      = "loading...";
    private String longitude     = "loading...";
    private String country       = "loading...";
    private String locality      = "loading...";
    private String street        = "loading...";
    private OnGetLocationListener mOnGetLocationListener;
    private RxLocationTool.OnLocationChangeListener mOnLocationChangeListener = new RxLocationTool.OnLocationChangeListener() {
        @Override
        public void getLastKnownLocation(Location location) {
            lastLatitude = String.valueOf(location.getLatitude());
            lastLongitude = String.valueOf(location.getLongitude());
            if (mOnGetLocationListener != null) {
                mOnGetLocationListener.getLocation(lastLatitude, lastLongitude, latitude, longitude, country, locality, street);
            }
        }

        @Override
        public void onLocationChanged(final Location location) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            if (mOnGetLocationListener != null) {
                mOnGetLocationListener.getLocation(lastLatitude, lastLongitude, latitude, longitude, country, locality, street);
            }
            country = RxLocationTool.getCountryName(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            locality = RxLocationTool.getLocality(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            street = RxLocationTool.getStreet(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            if (mOnGetLocationListener != null) {
                mOnGetLocationListener.getLocation(lastLatitude, lastLongitude, latitude, longitude, country, locality, street);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    public void setOnGetLocationListener(OnGetLocationListener onGetLocationListener) {
        mOnGetLocationListener = onGetLocationListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                isSuccess = RxLocationTool.registerLocation(getApplicationContext(), 0, 0, mOnLocationChangeListener);
                if (isSuccess) RxToast.success("init success");
                Looper.loop();
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocationBinder();
    }

    @Override
    public void onDestroy() {
        RxLocationTool.unRegisterLocation();
        // 一定要制空，否则内存泄漏
        mOnGetLocationListener = null;
        super.onDestroy();
    }

    /**
     * 获取位置监听器
     */
    public interface OnGetLocationListener {
        void getLocation(
                String lastLatitude, String lastLongitude,
                String latitude, String longitude,
                String country, String locality, String street
        );
    }

    public class LocationBinder extends Binder {
        public RxServiceLocation getService() {
            return RxServiceLocation.this;
        }
    }
}
