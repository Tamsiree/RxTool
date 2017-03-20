package com.vondear.tools.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.service.ServiceLocation;
import com.vondear.tools.R;

public class ActivityLocation extends ActivityBase {//原生的定位 不好用 因为调用的是Google地图，然而Google被墙，所以你懂的！

    private TextView tvAboutLocation;
    private ServiceLocation mLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        tvAboutLocation = (TextView) findViewById(R.id.tv_about_location);

        tvAboutLocation.setText("lastLatitude: unknown"
                + "\nlastLongitude: unknown"
                + "\nlatitude: unknown"
                + "\nlongitude: unknown"
                + "\ngetCountryName: unknown"
                + "\ngetLocality: unknown"
                + "\ngetStreet: unknown"
        );

        bindService(new Intent(this, ServiceLocation.class), conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLocationService = ((ServiceLocation.LocationBinder) service).getService();
            mLocationService.setOnGetLocationListener(new ServiceLocation.OnGetLocationListener() {
                @Override
                public void getLocation(final String lastLatitude, final String lastLongitude, final String latitude, final String longitude, final String country, final String locality, final String street) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvAboutLocation.setText("lastLatitude: " + lastLatitude +
                                    "\nlastLongitude: " + lastLongitude +
                                    "\nlatitude: " + latitude +
                                    "\nlongitude: " + longitude +
                                    "\ngetCountryName: " + country +
                                    "\ngetLocality: " + locality +
                                    "\ngetStreet: " + street
                            );
                        }
                    });
                }
            });
        }
    };
}
