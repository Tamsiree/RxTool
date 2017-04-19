package com.vondear.rxtools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.vondear.rxtools.interfaces.onRequestPermissionsListener;

/**
 * Created by Administrator on 2017/3/10.
 */

public class RxPermissionsUtils {

    //请求Camera权限
    public static void requestCamera(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }

    public static void requestCall(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }


    public static void requestWriteExternalStorage(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }

    public static void requestReadExternalStorage(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }
}
