package com.vondear.tools.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by cheng on 16-11-13.
 */
public class FakeAddImageView extends ImageView {
    private PointF mPointF;

    public FakeAddImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FakeAddImageView(Context context) {
        super(context);
    }

    public FakeAddImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FakeAddImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setMPointF(PointF pointF) {
        setX(pointF.x);
        setY(pointF.y);
    }

    public PointF getmPointF() {
        return mPointF;
    }

    public void setmPointF(PointF mPointF) {
        setX(mPointF.x);
        setY(mPointF.y);
    }
}
