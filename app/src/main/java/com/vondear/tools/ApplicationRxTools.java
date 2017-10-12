package com.vondear.tools;

import android.app.Application;

import com.vondear.rxtools.RxTool;

/**
 * Created by vonde on 2016/12/23.
 */

public class ApplicationRxTools extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
    }

}
