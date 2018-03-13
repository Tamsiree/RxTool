package com.vondear.tools;

import android.app.Application;
import android.content.Context;

import com.vondear.rxtools.RxTool;

/**
 * Created by vonde on 2016/12/23.
 */

public class ApplicationRxTools extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
    }

}
