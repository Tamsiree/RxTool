package com.vondear.tools.activity;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.cameraview.CameraView;
import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.RxFileTool;
import com.vondear.rxtools.RxLocationTool;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.activity.ActivityBaseLocation;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogScaleView;
import com.vondear.tools.R;
import com.vondear.tools.interfaces.onRxCamera;
import com.vondear.tools.tools.RxCameraTool;

import java.io.File;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityRxExifTool extends ActivityBaseLocation {

    @BindView(R.id.camera)
    CameraView mCameraView;
    @BindView(R.id.btn_take_camera)
    Button mBtnTakeCamera;
    @BindView(R.id.tv_gps)
    TextView mTvGps;
    @BindView(R.id.tv_state)
    TextView mTvState;
    @BindView(R.id.iv_pic)
    ImageView mIvPic;

    private File photo;

    @Override
    public void setGpsInfo(Location location) {
        mTvGps.setText("经度: " + RxLocationTool.gpsToDegree(location.getLongitude()) +
                "  纬度: " + RxLocationTool.gpsToDegree(location.getLatitude()) +
                "\n精度: " + location.getAccuracy() +
                //"  海拔: " + altitudeDf.format(location.getAltitude()) +
                "  方位: " + location.getBearing());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        RxBarTool.setTransparentStatusBar(this);
        setContentView(R.layout.activity_rx_exif_tool);
        ButterKnife.bind(this);

        RxPermissionsTool.
                with(mContext).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.READ_PHONE_STATE).
                initPermission();

        initCamera();//初始化相机
    }

    private void initCamera() {
        mCameraView.addCallback(new CameraView.Callback() {
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
            }
        });
    }

    private void initCameraEvent(final byte[] data) {
        String fileDir = RxFileTool.getRootPath().getAbsolutePath() + File.separator + "RoadExcel" + File.separator + "picture";
        String fileName = RxTimeTool.getCurrentDateTime("yyyyMMddHHmmss") + "_" + new Random().nextInt(1000) + ".jpg";
        RxCameraTool.initCameraEvent(mContext, mCameraView, data, fileDir, fileName, mLongitude, mLatitude, false, new onRxCamera() {
            @Override
            public void onBefore() {
                mTvState.setText("拍照成功,开始压缩\n");
            }

            @Override
            public void onSuccessCompress(File file) {
                mTvState.setText(mTvState.getText() + "图片压缩成功\n");
                photo = file;
                mIvPic.setImageURI(RxFileTool.getImageContentUri(mContext, photo));
            }

            @Override
            public void onSuccessExif(File filePhoto) {
                mTvState.setText(mTvState.getText() + "地理位置信息写入图片成功\n");
                photo = filePhoto;
                mIvPic.setImageURI(RxFileTool.getImageContentUri(mContext, photo));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (String permission : permissions) {
            if (permission.equals(Manifest.permission.CAMERA)) {
                mCameraView.start();
            }
        }
    }

    @OnClick({R.id.btn_take_camera, R.id.iv_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_take_camera:
                RxCameraTool.takePic(mContext, mCameraView);
                break;
            case R.id.iv_pic:
                if (photo == null) {
                    RxToast.normal("请先拍照");
                } else {
                    RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(mContext);
                    rxDialogScaleView.setImagePath(photo.getAbsolutePath());
                    rxDialogScaleView.show();
                }
                break;
        }
    }
}
