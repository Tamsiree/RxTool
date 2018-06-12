package com.vondear.rxdemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.vondear.rxtool.RxTool;

/**
 * @author vonde
 * @date 2016/12/23
 */

public class ApplicationRxTools extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
    }

}
