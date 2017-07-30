package com.vondear.tools.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.cameraview.CameraView;
import com.vondear.rxtools.RxExifTool;
import com.vondear.rxtools.RxFileUtils;
import com.vondear.rxtools.RxLocationUtils;
import com.vondear.rxtools.RxTimeUtils;
import com.vondear.rxtools.RxUtils;
import com.vondear.rxtools.RxVibrateUtils;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.tools.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityRxExifTool extends ActivityBase {

    @BindView(R.id.camera)
    CameraView mCamera;
    @BindView(R.id.btn_take_camera)
    Button mBtnTakeCamera;
    @BindView(R.id.tv_gps)
    TextView mTvGps;
    @BindView(R.id.tv_state)
    TextView mTvState;
    @BindView(R.id.iv_pic)
    ImageView mIvPic;

    private LocationManager mLocationManager;//定位管理器
    private double mLongitude = 112.915353;//经度
    private double mLatitude = 28.208398;//纬度

    private File cachefile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_exif_tool);
        ButterKnife.bind(this);

        initGPS();//初始化GPS
        gpsCheck();//GPS开启状态检测

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE}, 1);
        }

        mCamera.addCallback(new CameraView.Callback() {
            @Override
            public void onCameraOpened(CameraView cameraView) {
                super.onCameraOpened(cameraView);
            }

            @Override
            public void onCameraClosed(CameraView cameraView) {
                super.onCameraClosed(cameraView);
            }

            @Override
            public void onPictureTaken(CameraView cameraView, final byte[] data) {
                super.onPictureTaken(cameraView, data);
                initCameraEvent(data);
                mTvState.setText("拍照成功，经纬度信息写入图片成功");
                if (cachefile != null) {
                    mIvPic.setImageURI(RxFileUtils.getImageContentUri(mContext,cachefile));
                }
            }
        });
    }

    private void initGPS() {
        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
    }

    //----------------------------------------------------------------------------------------------检测GPS是否已打开 start
    private void gpsCheck() {
        if (!RxLocationUtils.isGpsEnabled(this)) {
            final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(mContext);
            rxDialogSureCancel.getIv_logo().setBackgroundDrawable(null);
            rxDialogSureCancel.setTitle("GPS未打开");
            rxDialogSureCancel.getIv_logo().setTextSize(16f);
            rxDialogSureCancel.getIv_logo().setTextColor(Color.BLACK);
            rxDialogSureCancel.setContent("您需要在系统设置中打开GPS方可采集数据");
            rxDialogSureCancel.getTvSure().setText("去设置");
            rxDialogSureCancel.getTvCancel().setText("知道了");

            rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxLocationUtils.openGpsSettings(mContext);
                    rxDialogSureCancel.cancel();
                }
            });

            rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogSureCancel.cancel();
                }
            });
            rxDialogSureCancel.setCanceledOnTouchOutside(false);
            rxDialogSureCancel.setCancelable(false);
            rxDialogSureCancel.show();
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
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLongitude = location.getLongitude();
                mLatitude = location.getLatitude();
                mTvGps.setText("经度: " + RxLocationUtils.gpsToDegree(location.getLongitude()) +
                        "  纬度: " + RxLocationUtils.gpsToDegree(location.getLatitude()) +
                        "\n精度: " + location.getAccuracy() +
                        "  方位: " + location.getBearing());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status) {
                    //GPS状态为可见时
                    case LocationProvider.AVAILABLE:

                        break;
                    //GPS状态为服务区外时
                    case LocationProvider.OUT_OF_SERVICE:
                        RxToast.normal("当前GPS信号弱");
                        break;
                    //GPS状态为暂停服务时
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:

                        break;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                RxToast.normal("当前GPS设备已打开");
                RxVibrateUtils.vibrateOnce(mContext, 800);
            }

            @Override
            public void onProviderDisabled(String provider) {
                RxToast.normal("当前GPS设备已关闭");
                RxVibrateUtils.vibrateOnce(mContext, 800);
                gpsCheck();
            }
        });
    }

    private Handler mBackgroundHandler;//拍照后台线程

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private void initCameraEvent(final byte[] data) {
        getBackgroundHandler().post(new Runnable() {
            @Override
            public void run() {
                final String fileTempName = RxTimeUtils.getCurrentDateTime("yyyyMMddHHmmss") + "_" + new Random().nextInt(1000);
                final String fileName = fileTempName + ".jpg";

                File cacheParent = new File(RxFileUtils.getRootPath().getAbsolutePath() + File.separator + "RxTools" + File.separator + "picture");
                if (!cacheParent.exists()) {
                    cacheParent.mkdirs();
                }
                cachefile = new File(cacheParent, fileName);
                OutputStream os = null;
                try {
                    os = new FileOutputStream(cachefile);
                    os.write(data);
                    os.close();

                    RxExifTool.writeLatLonIntoJpeg(cachefile.getAbsolutePath(), mLatitude, mLongitude);
                   /* mTvState.setText("拍照成功，经纬度信息写入图片成功");

                    mIvPic.setImageURI(RxFileUtils.getImageContentUri(mContext,cachefile));*/

                } catch (IOException e) {
                    Log.w("onPictureTaken", "Cannot write to " + cachefile, e);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            // Ignore
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera.start();
    }

    @Override
    protected void onPause() {
        mCamera.stop();
        super.onPause();
    }

    @OnClick(R.id.btn_take_camera)
    public void onViewClicked() {
        if (RxUtils.isFastClick(2000)) {
            RxToast.normal("请不要重复点击");
            return;
        } else {
            mCamera.takePicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (String permission : permissions) {
            if (permission.equals(Manifest.permission.CAMERA)) {
                mCamera.start();
            }
        }
    }
}
