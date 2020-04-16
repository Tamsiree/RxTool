package com.tamsiree.rxdemo

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.tamsiree.rxdemo.activity.ActivitySVG
import com.tamsiree.rxkit.RxTool
import com.tamsiree.rxkit.activity.ActivityCrash
import com.tamsiree.rxkit.crash.TCrashProfile
import com.tamsiree.rxkit.crash.TCrashTool
import com.tamsiree.rxkit.view.RxToast

/**
 * @author Tamsiree
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
                .crashProfile()//以下为崩溃配置
                .backgroundMode(TCrashProfile.BACKGROUND_MODE_SILENT) //default: TCrashProfile.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(true) //default: true
                .showErrorDetails(true) //default: true
                .showRestartButton(true) //default: true
                .logErrorOnRestart(true) //default: true
                .trackActivities(true) //default: false
                .minTimeBetweenCrashesMs(2000) //default: 3000
                .errorDrawable(R.drawable.crash_logo) //default: bug image
                .restartActivity(ActivitySVG::class.java) //default: null (your app's launch activity)
                .errorActivity(ActivityCrash::class.java) //default: null (default error activity)
                .eventListener(object : TCrashTool.EventListener {
                    override fun onRestartAppFromErrorActivity() {
                        RxToast.normal("onRestartAppFromErrorActivity")
                    }

                    override fun onCloseAppFromErrorActivity() {
                        RxToast.normal("onCloseAppFromErrorActivity")
                    }

                    override fun onLaunchErrorActivity() {
                        RxToast.normal("onLaunchErrorActivity")
                    }

                }) //default: null
                .apply()
    }
}