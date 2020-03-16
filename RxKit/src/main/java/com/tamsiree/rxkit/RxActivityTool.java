package com.tamsiree.rxkit;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import java.util.List;
import java.util.Stack;


/**
 * @author tamsiree
 * @date 2016/1/24
 * <p>
 * 封装Activity相关工具类
 */
public class RxActivityTool {

    private static Stack<Activity> activityStack;

    /**
     * 添加Activity 到栈
     *
     * @param activity Activity
     */
    public static void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前的Activity（堆栈中最后一个压入的)
     */
    public static Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishActivity() {
        Activity activity = activityStack.lastElement();
        activity.finish();
    }

    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有的Activity
     */
    public static void finishAllActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public static void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

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
        context.startActivity(RxIntentTool.getComponentNameIntent(packageName, className, bundle));
    }

    /**
     * 要求最低API为11
     * Activity 跳转
     * 跳转后Finish之前所有的Activity
     *
     * @param context Context
     * @param goal    Activity
     */
    public static void skipActivityAndFinishAll(Context context, Class<?> goal, Bundle bundle, boolean isFade) {
        Intent intent = new Intent(context, goal);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        finishActivity(context, false);
        if (isFade) {
            fadeTransition(context);
        }
    }

    /**
     * 要求最低API为11
     * Activity 跳转
     * 跳转后Finish之前所有的Activity
     *
     * @param context Context
     * @param goal    Activity
     */
    public static void skipActivityAndFinishAll(Context context, Class<?> goal, boolean isFade) {
        skipActivityAndFinishAll(context, goal, null, isFade);
    }

    /**
     * 要求最低API为11
     * Activity 跳转
     * 跳转后Finish之前所有的Activity
     *
     * @param context Context
     * @param goal    Activity
     */
    public static void skipActivityAndFinishAll(Context context, Class<?> goal) {
        skipActivityAndFinishAll(context, goal, null, false);
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    public static void skipActivityAndFinish(Context context, Class<?> goal, Bundle bundle, boolean isFade, boolean isTransition) {
        skipActivity(context, goal, bundle, isFade);
        finishActivity(context, isTransition);
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    public static void skipActivityAndFinish(Context context, Class<?> goal, Bundle bundle) {
        skipActivity(context, goal, bundle, false);
        finishActivity(context, false);
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    public static void skipActivityAndFinish(Context context, Class<?> goal) {
        skipActivity(context, goal, null, false);
        finishActivity(context, false);
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    public static void skipActivityAndFinish(Context context, Class<?> goal, boolean isFade, boolean isTransition) {
        skipActivity(context, goal, isFade);
        finishActivity(context, isTransition);
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    public static void skipActivity(Context context, Class<?> goal) {
        skipActivity(context, goal, null, false);
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    public static void skipActivity(Context context, Class<?> goal, boolean isFade) {
        skipActivity(context, goal, null, isFade);
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    public static void skipActivity(Context context, Class<?> goal, Bundle bundle, boolean isFade) {
        Intent intent = new Intent(context, goal);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        if (isFade) {
            fadeTransition(context);
        }
    }

    public static void skipActivityForResult(Activity context, Class<?> goal, int requestCode) {
        skipActivityForResult(context, goal, null, requestCode);
    }

    public static void skipActivityForResult(Activity context, Class<?> goal, Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, goal);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivityForResult(intent, requestCode);
    }

    public static void skipActivityOnTransitions(Context mContext, Class<?> goal, Bundle bundle, Pair<View, String>... pairs) {
        Intent intent = new Intent(mContext, goal);
        Bundle bundle1 = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, pairs).toBundle();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ActivityCompat.startActivity(mContext, intent, bundle1);
    }

    public static void skipActivityTransition(Context mContext, Class<?> goal, Bundle bundle, View view, String elementName) {
        Intent intent = new Intent(mContext, goal);
        Bundle bundle1 = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, view, elementName).toBundle();
        intent.putExtras(bundle);
        mContext.startActivity(intent, bundle1);
    }

    /**
     * 获取launcher activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @return launcher activity
     */
    public static String getLauncherActivity(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo info : infos) {
            if (info.activityInfo.packageName.equals(packageName)) {
                return info.activityInfo.name;
            }
        }
        return "no " + packageName;
    }

    public static void finishActivity(Context mContext, boolean isTransition) {
        if (isTransition) {
            ((Activity) mContext).onBackPressed();
        } else {
            ((Activity) mContext).finish();
        }
    }

    public static void fadeTransition(Context mContext) {
        ((Activity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
