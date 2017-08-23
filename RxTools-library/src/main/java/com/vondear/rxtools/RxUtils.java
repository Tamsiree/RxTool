package com.vondear.rxtools;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vondear on 2016/1/24.
 * RxTools的常用工具类
 */
public class RxUtils {

    private static Context context;

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        RxUtils.context = context.getApplicationContext();
        RxCrashUtils.getInstance(context).init();
    }

    /**
     * 在某种获取不到 Context 的情况下，即可以使用才方法获取 Context
     * <p>
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("请先调用init()方法");
    }

    public static void delayToDo(final DelayListener delayListener, long delayTime) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                delayListener.doSomething();
            }
        }, delayTime);
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
    //==============================================================================================延时任务封装 end

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

    /**
     * 生成MD5加密32位字符串
     *
     * @param MStr :需要加密的字符串
     * @return
     */
    public static String Md5(String MStr) {
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(MStr.getBytes());
            return bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(MStr.hashCode());
        }
    }

    //---------------------------------------------MD5加密-------------------------------------------

    // MD5内部算法---------------不能修改!
    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 根据资源名称获取资源 id
     * <p>
     * 不提倡使用这个方法获取资源,比其直接获取ID效率慢
     * <p>
     * 例如
     * getResources().getIdentifier("ic_launcher", "drawable", getPackageName());
     *
     * @param context
     * @param name
     * @param defType
     * @return
     */
    public static final int getResIdByName(Context context, String name, String defType) {
        return context.getResources().getIdentifier("ic_launcher", "drawable", context.getPackageName());
    }
    //============================================MD5加密============================================

    //----------------------------------------------------------------------------------------------延时任务封装 start
    public interface DelayListener {
        void doSomething();
    }

    private static long lastClickTime;

    public static boolean isFastClick(int millisecond) {
        long curClickTime = System.currentTimeMillis();
        long interval = (curClickTime - lastClickTime);

        if (0 < interval && interval < millisecond) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            return true;
        }
        lastClickTime = curClickTime;
        return false;
    }

    /**
     * Edittext 首位小数点自动加零，最多两位小数
     *
     * @param editText
     */
    public static void setEdTwoDecimal(EditText editText) {
        setEdDecimal(editText, 3);
    }

    public static void setEdDecimal(EditText editText, int count) {
        if (count < 1) {
            count = 1;
        }

        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

        //设置字符过滤
        final int finalCount = count;
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == finalCount) {
                        return "";
                    }
                }
                return null;
            }
        }});
    }

    public static void initEditNumberPrefix(final EditText edSerialNumber, final int number) {
        edSerialNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String s = edSerialNumber.getText().toString();
                    String temp = "";
                    for (int i = s.length(); i < number; i++) {
                        s = "0" + s;
                    }

                    for (int i = 0; i < number; i++) {
                        temp += "0";
                    }
                    if (s.equals(temp)) {
                        s = temp.substring(1) + "1";
                    }
                    edSerialNumber.setText(s);
                }
            }
        });
    }

    public static Handler getBackgroundHandler() {
        HandlerThread thread = new HandlerThread("background");
        thread.start();
        Handler mBackgroundHandler = new Handler(thread.getLooper());
        return mBackgroundHandler;
    }
}
