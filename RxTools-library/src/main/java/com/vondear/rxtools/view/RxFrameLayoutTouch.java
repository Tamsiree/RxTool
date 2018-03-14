package com.vondear.rxtools.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author vondear
 * @date 2018/3/5
 * 解决滑动冲突
 */

public class RxFrameLayoutTouch extends FrameLayout {

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);//拦截父类事件

        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        return super.dispatchTouchEvent(ev);
    }

    public RxFrameLayoutTouch(Context context) {
        super(context);
    }

    public RxFrameLayoutTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxFrameLayoutTouch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
