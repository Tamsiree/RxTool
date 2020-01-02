package com.vondear.rxui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author vondear
 * @date 2016/6/28
 */
public class RxRunTextView extends AppCompatTextView {
    public RxRunTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RxRunTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxRunTextView(Context context) {
        super(context);
    }

    /**
     * 当前并没有焦点，我只是欺骗了Android系统
     */
    @Override
    @ViewDebug.ExportedProperty(category = "focus")
    public boolean isFocused() {
        return true;
    }
}
