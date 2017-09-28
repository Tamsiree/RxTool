package com.vondear.rxtools.view.colorpicker.slider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vondear.rxtools.R;


public abstract class AbsCustomSlider extends View {
    protected Bitmap bitmap;
    protected Canvas bitmapCanvas;
    protected Bitmap bar;
    protected Canvas barCanvas;
    protected OnValueChangedListener onValueChangedListener;
    protected int barOffsetX;
    protected int handleRadius = 20;
    protected int barHeight = 5;
    protected float value = 1;

    public AbsCustomSlider(Context context) {
        super(context);
    }

    public AbsCustomSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsCustomSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void updateBar() {
        handleRadius = getDimension(R.dimen.default_slider_handler_radius);
        barHeight = getDimension(R.dimen.default_slider_bar_height);
        barOffsetX = handleRadius;

        if (bar == null)
            createBitmaps();
        drawBar(barCanvas);
        invalidate();
    }

    protected void createBitmaps() {
        int width = getWidth();
        int height = getHeight();
        bar = Bitmap.createBitmap(width - barOffsetX * 2, barHeight, Bitmap.Config.ARGB_8888);
        barCanvas = new Canvas(bar);

        if (bitmap == null || bitmap.getWidth() != width || bitmap.getHeight() != height) {
            if (bitmap != null) bitmap.recycle();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bar != null && bitmapCanvas != null) {
            bitmapCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            bitmapCanvas.drawBitmap(bar, barOffsetX, (getHeight() - bar.getHeight()) / 2, null);

            float x = handleRadius + value * (getWidth() - handleRadius * 2);
            float y = getHeight() / 2f;
            drawHandle(bitmapCanvas, x, y);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    protected abstract void drawBar(Canvas barCanvas);

    protected abstract void onValueChanged(float value);

    protected abstract void drawHandle(Canvas canvas, float x, float y);

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateBar();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = 0;
        if (widthMode == MeasureSpec.UNSPECIFIED)
            width = widthMeasureSpec;
        else if (widthMode == MeasureSpec.AT_MOST)
            width = MeasureSpec.getSize(widthMeasureSpec);
        else if (widthMode == MeasureSpec.EXACTLY)
            width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        if (heightMode == MeasureSpec.UNSPECIFIED)
            height = heightMeasureSpec;
        else if (heightMode == MeasureSpec.AT_MOST)
            height = MeasureSpec.getSize(heightMeasureSpec);
        else if (heightMode == MeasureSpec.EXACTLY)
            height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                if (bar != null) {
                    value = (event.getX() - barOffsetX) / bar.getWidth();
                    value = Math.max(0, Math.min(value, 1));
                    onValueChanged(value);
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                onValueChanged(value);
                if (onValueChangedListener != null)
                    onValueChangedListener.onValueChanged(value);
                invalidate();
            }
        }
        return true;
    }

    protected int getDimension(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }
}