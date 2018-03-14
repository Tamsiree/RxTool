package com.vondear.tools;

import android.app.Application;
import android.content.Context;

import com.vondear.rxtools.RxTool;

/**
 * @author vonde
 * @date 2016/12/23
 */

public class ApplicationRxTools extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
    }

}
