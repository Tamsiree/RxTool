package com.vondear.camera.tool;

import android.content.Context;
import android.hardware.Camera;
import android.os.SystemClock;
import android.util.Log;

import com.vondear.camera.RxCameraView;
import com.vondear.rxtool.RxConstants;
import com.vondear.rxtool.RxExifTool;
import com.vondear.rxtool.RxFileTool;
import com.vondear.rxtool.RxVibrateTool;
import com.vondear.rxtool.interfaces.OnRxCamera;
import com.vondear.rxtool.module.photomagic.OnCompressListener;
import com.vondear.rxtool.module.photomagic.RxMagic;
import com.vondear.rxtool.view.RxToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Vondear
 * @date 2017/9/22
 */

public class RxCameraTool {

    private static Camera camera;

    /**
     * 打开闪光灯
     */
    public static void openFlashLight() {
        try {
            if (camera == null) {
                camera = Camera.open();
                camera.startPreview();
            }
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭闪光灯
     *
     */
    public static void closeFlashLight() {
        try {
            if (camera == null) {

            } else {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void takePic(Context mContext, final RxCameraView mCameraView) {
        try {
            if (mCameraView.isCameraOpened()) {
                RxVibrateTool.vibrateOnce(mContext, 150);
                RxToast.normal("正在拍照..");
                try {
                    mCameraView.takePicture();
                } catch (Exception var2) {
                    RxToast.normal("你碰到问题咯");
                }
            } else {
                mCameraView.start();
                RxVibrateTool.vibrateOnce(mContext, 150);
                RxToast.normal("正在拍照..");
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Integer> e) {
                        //延时
                        SystemClock.sleep(500);
                        e.onNext(1);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        // 第三步：订阅
                        .subscribe(new Observer<Integer>() {

                            // 第二步：初始化Observer
                            private Disposable mDisposable;

                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                mDisposable = d;
                            }

                            @Override
                            public void onNext(@NonNull Integer integer) {
                                switch (integer) {
                                    case 1:
                                        try {
                                            mCameraView.takePicture();
                                        } catch (Exception var2) {
                                            RxToast.normal("你碰到问题咯");
                                        }
                                        break;
                                    default:
                                        break;
                                }

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        } catch (Exception var3) {
            RxToast.normal("你碰到了问题咯");
        }
    }

    public static void initCameraEvent(final Context mContext,
                                       final RxCameraView mCameraView,
                                       final byte[] data,
                                       final String fileDir,
                                       final String picName,
                                       final double mLongitude,
                                       final double mLatitude,
                                       final boolean isEconomize,
                                       final OnRxCamera onRxCamera) {
        onRxCamera.onBefore();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Integer> e) {

                File fileParent = new File(fileDir);
                File cacheParent = new File(RxConstants.PICTURE_CACHE_PATH);
                if (!cacheParent.exists()) {
                    cacheParent.mkdirs();
                }
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }

                final File cachefile = new File(cacheParent, picName);
                final File compressFile = new File(fileParent, picName);
                OutputStream os = null;
                try {
                    os = new FileOutputStream(cachefile);
                    os.write(data);
                    os.close();

                    RxMagic.createBuilder(mContext).
                            load(cachefile).
                            setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                    Log.d("图片压缩", "开始压缩");
                                }

                                @Override
                                public void onSuccess(File file) {
                                    if (RxFileTool.copyOrMoveFile(file, compressFile, true)) {
                                        Log.d("图片压缩", "压缩完成");
                                        onRxCamera.onSuccessCompress(compressFile);
                                        if (mLongitude != 0 || mLatitude != 0) {
                                            RxExifTool.writeLatLonIntoJpeg(compressFile.getAbsolutePath(), mLatitude, mLongitude);
                                            onRxCamera.onSuccessExif(compressFile);
                                            e.onNext(2);
                                        } else {
                                            e.onNext(3);
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.d("图片压缩", "压缩异常");
//                                    Logger.d(e);
                                }
                            }).launch();

                } catch (IOException e1) {
                    Log.w("onPictureTaken", "Cannot write to " + compressFile, e1);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                            e.onNext(1);
                        } catch (IOException e2) {
                            // Ignore
                        }
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                // 第三步：订阅
                .subscribe(new Observer<Integer>() {

                    // 第二步：初始化Observer
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        switch (integer) {
                            case 1:
                                try {
                                    if (isEconomize) {
                                        mCameraView.stop();
                                    }
                                } catch (Exception e) {

                                }
                                break;
                            case 2:
                                RxToast.normal("拍照成功");
                                break;
                            case 3:
                                RxToast.error("请先获取定位信息");
                                break;
                            default:
                                break;
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
