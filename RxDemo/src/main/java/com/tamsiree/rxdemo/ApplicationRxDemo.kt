package com.tamsiree.rxdemo

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.tamsiree.rxkit.RxTool

/**
 * @author vonde
 * @date 2016/12/23
 */
class ApplicationRxDemo : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        RxTool.init(this)
                .debugLog(true)
                .debugLogFile(false)
                .crashLogFile(true)
    }
}