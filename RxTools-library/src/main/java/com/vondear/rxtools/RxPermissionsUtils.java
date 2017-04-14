package com.vondear.rxtools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.vondear.rxtools.interfaces.onRequestListener;

/**
 * Created by Administrator on 2017/3/10.
 */

public class RxPermissionsUtils {

    //请求Camera权限
    public static void requestCamera(Context mContext, onRequestListener onRequestListener){
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, 1);
            onRequestListener.onRequestBefore();
        }else{
            onRequestListener.onRequestLater();
        }
    }

    public static void requestCall(Context mContext, onRequestListener onRequestListener){
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, 1);
            onRequestListener.onRequestBefore();
        }else{
            onRequestListener.onRequestLater();
        }
    }

}
