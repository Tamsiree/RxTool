package com.vondear.tools;

import android.app.Application;

import com.vondear.vontools.VonUtils;

/**
 * Created by vonde on 2016/12/23.
 */

public class ApplicationVon extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VonUtils.init(this);
    }
}
