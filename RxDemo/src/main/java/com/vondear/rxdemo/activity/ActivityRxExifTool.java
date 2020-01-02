package com.vondear.rxdemo.activity;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.vondear.camera.RxCameraView;
import com.vondear.camera.tool.RxCameraTool;
import com.vondear.rxdemo.R;
import com.vondear.rxtool.RxBarTool;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxFileTool;
import com.vondear.rxtool.RxLocationTool;
import com.vondear.rxtool.RxPermissionsTool;
import com.vondear.rxtool.RxTimeTool;
import com.vondear.rxtool.RxTool;
import com.vondear.rxtool.interfaces.OnRxCamera;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.activity.ActivityBaseLocation;
import com.vondear.rxui.view.dialog.RxDialogScaleView;

import java.io.File;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author vondear
 */
public class ActivityRxExifTool extends ActivityBaseLocation {

    @BindView(R.id.camera)
    RxCameraView mCameraView;
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
        mTvGps.setText(String.format("经度: %s  纬度: %s\n精度: %s  方位: %s", RxLocationTool.gpsToDegree(location.getLongitude()), RxLocationTool.gpsToDegree(location.getLatitude()), location.getAccuracy(), location.getBearing()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        RxBarTool.setTransparentStatusBar(this);
        setContentView(R.layout.activity_rx_exif_tool);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);

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
        mCameraView.addCallback(new RxCameraView.Callback() {
            @Override
            public void onCameraOpened(RxCameraView cameraView) {
                super.onCameraOpened(cameraView);
            }

            @Override
            public void onCameraClosed(RxCameraView cameraView) {
                super.onCameraClosed(cameraView);
            }

            @Override
            public void onPictureTaken(RxCameraView cameraView, final byte[] data) {
                super.onPictureTaken(cameraView, data);
                initCameraEvent(data);
            }
        });
    }

    private void initCameraEvent(final byte[] data) {
        String fileDir = RxFileTool.getRootPath().getAbsolutePath() + File.separator + "RoadExcel" + File.separator + "picture";
        String fileName = RxTimeTool.getCurrentDateTime("yyyyMMddHHmmss") + "_" + new Random().nextInt(1000) + ".jpg";
        RxCameraTool.initCameraEvent(mContext, mCameraView, data, fileDir, fileName, mLongitude, mLatitude, false, new OnRxCamera() {
            @Override
            public void onBefore() {
                mTvState.setText("拍照成功,开始压缩\n");
            }

            @Override
            public void onSuccessCompress(File file) {
                mTvState.setText(String.format("%s图片压缩成功\n", mTvState.getText()));
                photo = file;
                mIvPic.setImageURI(RxFileTool.getImageContentUri(mContext, photo));
            }

            @Override
            public void onSuccessExif(File filePhoto) {
                mTvState.setText(String.format("%s地理位置信息写入图片成功\n", mTvState.getText()));
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
                if (RxTool.isFastClick(1000)) {
                    RxToast.normal("请不要重复点击拍照按钮");
                    return;
                } else {
                    RxCameraTool.takePic(mContext, mCameraView);
                }
                break;
            case R.id.iv_pic:
                if (photo == null) {
                    RxToast.normal("请先拍照");
                } else {
                    RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(mContext);
                    rxDialogScaleView.setImage(photo.getAbsolutePath(), false);
                    rxDialogScaleView.show();
                }
                break;
            default:
                break;
        }
    }
}
