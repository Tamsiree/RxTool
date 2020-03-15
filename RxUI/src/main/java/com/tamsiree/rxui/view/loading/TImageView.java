package com.tamsiree.rxui.view.loading;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.tamsiree.rxui.R;

public class TImageView extends AppCompatImageView implements TLoadingView {

    private TLoadingManager loaderController;
    private int defaultColorResource;

    public TImageView(Context context) {
        super(context);
        init(null);
    }

    public TImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        loaderController = new TLoadingManager(this);
        @SuppressLint("CustomViewStyleable") TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TLoadingView, 0, 0);
        loaderController.setUseGradient(typedArray.getBoolean(R.styleable.TLoadingView_gradient, TLoadingProfile.USE_GRADIENT_DEFAULT));
        loaderController.setCorners(typedArray.getInt(R.styleable.TLoadingView_corners, TLoadingProfile.CORNER_DEFAULT));
        defaultColorResource = typedArray.getColor(R.styleable.TLoadingView_TLoadingColor, ContextCompat.getColor(getContext(), R.color.tloading_default_color));
        typedArray.recycle();
    }

    public void resetLoader() {
        if (getDrawable() != null) {
            super.setImageDrawable(null);
            loaderController.startLoading();
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        loaderController.onSizeChanged();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        loaderController.onDraw(canvas);
    }

    @Override
    public void setRectColor(Paint rectPaint) {
        rectPaint.setColor(defaultColorResource);
    }

    @Override
    public boolean valueSet() {
        return (getDrawable() != null);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        loaderController.stopLoading();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        loaderController.stopLoading();
    }

    @Override
    public void setImageIcon(Icon icon) {
        super.setImageIcon(icon);
        loaderController.stopLoading();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        loaderController.stopLoading();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        loaderController.removeAnimatorUpdateListener();
    }
}
