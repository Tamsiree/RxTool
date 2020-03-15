package com.tamsiree.rxui.view.loading;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.tamsiree.rxui.R;

public class TTextView extends AppCompatTextView implements TLoadingView {

    private TLoadingManager loaderController;
    private int defaultColorResource;
    private int darkerColorResource;

    public TTextView(Context context) {
        super(context);
        init(null);
    }

    public TTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        loaderController = new TLoadingManager(this);
        @SuppressLint("CustomViewStyleable") TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TLoadingView, 0, 0);
        loaderController.setWidthWeight(typedArray.getFloat(R.styleable.TLoadingView_width_weight, TLoadingProfile.MAX_WEIGHT));
        loaderController.setHeightWeight(typedArray.getFloat(R.styleable.TLoadingView_height_weight, TLoadingProfile.MAX_WEIGHT));
        loaderController.setUseGradient(typedArray.getBoolean(R.styleable.TLoadingView_gradient, TLoadingProfile.USE_GRADIENT_DEFAULT));
        loaderController.setCorners(typedArray.getInt(R.styleable.TLoadingView_corners, TLoadingProfile.CORNER_DEFAULT));
        defaultColorResource = typedArray.getColor(R.styleable.TLoadingView_TLoadingColor, ContextCompat.getColor(getContext(), R.color.tloading_default_color));
        darkerColorResource = typedArray.getColor(R.styleable.TLoadingView_TLoadingColor, ContextCompat.getColor(getContext(), R.color.tloading_darker_color));
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        loaderController.onSizeChanged();
    }

    public void resetLoader() {
        if (!TextUtils.isEmpty(getText())) {
            super.setText(null);
            loaderController.startLoading();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        loaderController.onDraw(canvas, getCompoundPaddingLeft(),
                getCompoundPaddingTop(),
                getCompoundPaddingRight(),
                getCompoundPaddingBottom());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (loaderController != null) {
            loaderController.stopLoading();
        }
    }

    @Override
    public void setRectColor(Paint rectPaint) {
        final Typeface typeface = getTypeface();
        if (typeface != null && typeface.getStyle() == Typeface.BOLD) {
            rectPaint.setColor(darkerColorResource);
        } else {
            rectPaint.setColor(defaultColorResource);
        }
    }

    @Override
    public boolean valueSet() {
        return !TextUtils.isEmpty(getText());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        loaderController.removeAnimatorUpdateListener();
    }
}
