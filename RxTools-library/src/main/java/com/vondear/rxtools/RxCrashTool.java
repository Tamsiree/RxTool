package com.vondear.rxtools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.vondear.rxtools.view.RxToast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vondear on 2016/12/21.
 *
 */

public class RxCrashTool implements Thread.UncaughtExceptionHandler {

    private volatile static RxCrashTool mInstance;

    private UncaughtExceptionHandler mHandler;
    private boolean mInitialized;
    private String mCrashDirPath;
    private String mVersionName;
    private int mVersionCode;

    private Context mContext;

    private RxCrashTool(Context context) {
        this.mContext = context;
    }

    /**
     * 获取单例
     * <p>在Application中初始化{@code RxCrashTool.getInstance().init(this);}</p>
     *
     * @return 单例
     */
    public static RxCrashTool getInstance(Context context) {
        if (mInstance == null) {
            synchronized (RxCrashTool.class) {
                if (mInstance == null) {
                    mInstance = new RxCrashTool(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean init() {
        if (mInitialized) return true;

        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            String name = mContext.getResources().getString(labelRes);
            mCrashDirPath = RxFileTool.getRootPath() + File.separator + name + File.separator + "crash" + File.separator;
        } catch (Exception e) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                mCrashDirPath = mContext.getExternalCacheDir().getPath() + File.separator + "crash" + File.separator;
            } else {
                mCrashDirPath = mContext.getCacheDir().getPath() + File.separator + "crash" + File.separator;
            }
        }

        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            mVersionName = pi.versionName;
            mVersionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return mInitialized = true;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        final String fullPath = mCrashDirPath + now + ".txt";
        if (!RxFileTool.createOrExistsFile(fullPath)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(fullPath, false));
                    pw.write(getCrashHead());
                    throwable.printStackTrace(pw);
                    Throwable cause = throwable.getCause();
                    while (cause != null) {
                        cause.printStackTrace(pw);
                        cause = cause.getCause();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    RxFileTool.closeIO(pw);
                }
            }
        }).start();

        if (!handleException(throwable) && mHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mHandler.uncaughtException(thread, throwable);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(mContext.getPackageName(), "error : ", e);
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                RxToast.error("很抱歉,程序出现异常,即将退出.");
                Looper.loop();
            }
        }.start();
        return true;
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead() {
        return "\n************* Crash Log Head ****************" +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                "\nDevice Model       : " + Build.MODEL +// 设备型号
                "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                "\nApp VersionName    : " + mVersionName +
                "\nApp VersionCode    : " + mVersionCode +
                "\n************* Crash Log Head ****************\n\n";
    }
}