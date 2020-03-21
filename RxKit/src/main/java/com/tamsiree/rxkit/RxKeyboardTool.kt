package com.tamsiree.rxkit

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 *
 * @author tamsiree
 * @date 2016/1/24
 */
object RxKeyboardTool {
    /**
     * 避免输入法面板遮挡
     *
     * 在manifest.xml中activity中设置
     *
     * android:windowSoftInputMode="stateVisible|adjustResize"
     */
    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    @JvmStatic
    fun hideSoftInput(activity: Activity) {
        val view = activity.window.peekDecorView()
        if (view != null) {
            val inputmanger = activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputmanger.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * 点击隐藏软键盘
     *
     * @param view
     */
    @JvmStatic
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 动态隐藏软键盘
     *
     * @param edit    输入框
     */
    @JvmStatic
    fun hideSoftInput(edit: EditText) {
        edit.clearFocus()
        val inputmanger = edit.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputmanger.hideSoftInputFromWindow(edit.windowToken, 0)
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法1）
     *
     * 在onTouch中处理，未获焦点则隐藏
     *
     * 参照以下注释代码
     */
    @JvmStatic
    fun clickBlankArea2HideSoftInput0() {
        Log.i("tips", "U should copy the following code.")
        /*
        @Override
        public boolean onTouchEvent (MotionEvent event){
            if (null != this.getCurrentFocus()) {
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
            return super.onTouchEvent(event);
        }
        */
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法2）
     *
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
     *
     * 需重写dispatchTouchEvent
     *
     * 参照以下注释代码
     */
    @JvmStatic
    fun clickBlankArea2HideSoftInput1() {
        Log.i("tips", "U should copy the following code.")
        /*
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    hideKeyboard(v.getWindowToken());
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
        private boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }

        // 获取InputMethodManager，隐藏软键盘
        private void hideKeyboard(IBinder token) {
            if (token != null) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        */
    }

    /**
     * 动态显示软键盘
     *
     * @param context 上下文
     * @param edit    输入框
     */
    @JvmStatic
    fun showSoftInput(context: Context, edit: EditText) {
        edit.isFocusable = true
        edit.isFocusableInTouchMode = true
        edit.requestFocus()
        val inputManager = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(edit, 0)
    }

    /**
     * 切换键盘显示与否状态
     *
     * @param context 上下文
     * @param edit    输入框
     */
    @JvmStatic
    fun toggleSoftInput(context: Context, edit: EditText) {
        edit.isFocusable = true
        edit.isFocusableInTouchMode = true
        edit.requestFocus()
        val inputManager = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}