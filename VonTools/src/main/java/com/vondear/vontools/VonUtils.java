package com.vondear.vontools;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by vondear on 2016/1/24.
 */
public class VonUtils {

    /**
     * 点击隐藏软键盘
     *
     * @param activity
     * @param view
     */
    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 倒计时
     *
     * @param textView 控件
     * @param waitTime 倒计时总时长
     * @param interval 倒计时的间隔时间
     * @param hint     倒计时完毕时显示的文字
     */
    public static void countDown(final TextView textView, long waitTime, long interval, final String hint) {
        textView.setEnabled(false);
        android.os.CountDownTimer timer = new android.os.CountDownTimer(waitTime, interval) {

            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText("剩下 " + (millisUntilFinished / 1000) + " S");
            }

            @Override
            public void onFinish() {
                textView.setEnabled(true);
                textView.setText(hint);
            }
        };
        timer.start();
    }
    //-----------------------------------------Toast 替代方法----------------------------------------

    /**
     * 封装了Toast的方法 :需要等待
     *
     * @param cxt
     * @param str
     * @param isLong
     */
    public static void showToast(Context cxt, String str, boolean isLong) {
        if (isLong) {
            Toast.makeText(cxt, str, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(cxt, str, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     */
    private static Toast mToast;

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param resId    资源ID
     * @param duration 显示时长
     */
    public static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getString(resId), duration);
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param msg      要显示的字符串
     * @param duration 显示时长
     */
    public static void showToast(Context context, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
    //===========================================Toast 替代方法======================================

    /**
     * 手动计算出listView的高度，但是不再具有滚动效果
     *
     * @param listView
     */
    public static void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int index = 0, len = listAdapter.getCount(); index < len; index++) {
            View listViewItem = listAdapter.getView(index, null, listView);
            // 计算子项View 的宽高
            listViewItem.measure(0, 0);
            // 计算所有子项的高度
            totalHeight += listViewItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
