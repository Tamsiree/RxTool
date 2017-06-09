package com.vondear.rxtools.view.progress.sprite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;

/**
 * Created by ybq.
 */
public abstract class ShapeSprite extends Sprite {

    private Paint mPaint;
    private int mUseColor;
    private int mBaseColor;

    public ShapeSprite() {
        setColor(Color.WHITE);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mUseColor);
    }

    @Override
    public void setColor(int color) {
        mBaseColor = color;
        updateUseColor();
    }

    @Override
    public int getColor() {
        return mBaseColor;
    }

    @SuppressWarnings("unused")
    public int getUseColor() {
        return mUseColor;
    }

    @Override
    public void setAlpha(int alpha) {
        super.setAlpha(alpha);
        updateUseColor();
    }

    private void updateUseColor() {
        int alpha = getAlpha();
        alpha += alpha >> 7;
        final int baseAlpha = mBaseColor >>> 24;
        final int useAlpha = baseAlpha * alpha >> 8;
        mUseColor = (mBaseColor << 8 >>> 8) | (useAlpha << 24);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    protected final void drawSelf(Canvas canvas) {
        mPaint.setColor(mUseColor);
        drawShape(canvas, mPaint);
    }

    public abstract void drawShape(Canvas canvas, Paint paint);
}
