package com.vondear.rxtools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by vondear on 2016/1/24.
 */
public class RxIntentUtils {

    /**
     * 获取安装App(支持7.0)的意图
     * @param context
     * @param filePath
     * @param fileProvide
     * @return
     */
    public static Intent getInstallAppIntent(Context context, String filePath, String fileProvide) {
        //apk文件的本地路径
        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri contentUri;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            contentUri = FileProvider.getUriForFile(context, fileProvide, apkfile);
        } else {
            contentUri = Uri.fromFile(apkfile);

        }
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 获取卸载App的意图
     *
     * @param packageName 包名
     * @return 意图
     */
    public static Intent getUninstallAppIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取打开App的意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 意图
     */
    public static Intent getLaunchAppIntent(Context context, String packageName) {
        return getIntentByPackageName(context, packageName);
    }

    /**
     * 根据包名获取意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 意图
     */
    private static Intent getIntentByPackageName(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    /**
     * 获取App信息的意图
     *
     * @param packageName 包名
     * @return 意图
     */
    public static Intent getAppInfoIntent(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        return intent.setData(Uri.parse("package:" + packageName));
    }

    /**
     * 获取App信息分享的意图
     *
     * @param info 分享信息
     * @return 意图
     */
    public static Intent getShareInfoIntent(String info) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        return intent.putExtra(Intent.EXTRA_TEXT, info);
    }

    /**
     * 获取其他应用的Intent
     *
     * @param packageName 包名
     * @param className   全类名
     * @return 意图
     */
    public static Intent getComponentNameIntent(String packageName, String className) {
        return getComponentNameIntent(packageName, className, null);
    }

    /**
     * 获取其他应用的Intent
     *
     * @param packageName 包名
     * @param className   全类名
     * @return 意图
     */
    public static Intent getComponentNameIntent(String packageName, String className, Bundle bundle) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (bundle != null) intent.putExtras(bundle);
        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取App具体设置的意图
     *
     * @param packageName 包名
     * @return intent
     */
    public static Intent getAppDetailsSettingsIntent(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
