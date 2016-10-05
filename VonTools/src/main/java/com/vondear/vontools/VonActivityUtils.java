package com.vondear.vontools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * Created by vondear on 2016/1/24.
 */
public class VonActivityUtils {

    /**
     * 判断是否存在指定Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isExistActivity(Context context, String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return !(context.getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(context.getPackageManager()) == null ||
                context.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    /**
     * 打开指定的Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   全类名
     */
    public static void launchActivity(Context context, String packageName, String className) {
        launchActivity(context, packageName, className, null);
    }

    /**
     * 打开指定的Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   全类名
     * @param bundle      bundle
     */
    public static void launchActivity(Context context, String packageName, String className, Bundle bundle) {
        context.startActivity(VonIntentUtils.getComponentNameIntent(packageName, className, bundle));
    }


    /**
     * Activity 跳转
     *
     * @param context
     * @param goal
     */
    public static void skipActivityAndFinish(Context context, Class<?> goal, Bundle bundle) {
        Intent intent = new Intent(context, goal);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    /**
     * Activity 跳转
     *
     * @param context
     * @param goal
     */
    public static void skipActivityAndFinish(Context context, Class<?> goal) {
        Intent intent = new Intent(context, goal);
        context.startActivity(intent);
        ((Activity) context).finish();
    }


    /**
     * Activity 跳转
     *
     * @param context
     * @param goal
     */
    public static void skipActivity(Context context, Class<?> goal) {
        Intent intent = new Intent(context, goal);
        context.startActivity(intent);
    }

    /**
     * Activity 跳转
     *
     * @param context
     * @param goal
     */
    public static void skipActivity(Context context, Class<?> goal, Bundle bundle) {
        Intent intent = new Intent(context, goal);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
