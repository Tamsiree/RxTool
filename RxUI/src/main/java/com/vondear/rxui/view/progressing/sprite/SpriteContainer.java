package com.vondear.rxui.view.progressing.sprite;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;


/**
 * @author vondear
 */
@SuppressWarnings("WeakerAccess")
public abstract class SpriteContainer extends Sprite {

    private Sprite[] sprites;

    private int color;

    public SpriteContainer() {
        sprites = onCreateChild();
        initCallBack();
        onChildCreated(sprites);
    }

    private void initCallBack() {
        if (sprites != null) {
            for (Sprite sprite : sprites) {
                sprite.setCallback(this);
            }
        }
    }

    public void onChildCreated(Sprite... sprites) {

    }

    public int getChildCount() {
        return sprites == null ? 0 : sprites.length;
    }

    public Sprite getChildAt(int index) {
        return sprites == null ? null : sprites[index];
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setColor(color);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawChild(canvas);
    }

    public void drawChild(Canvas canvas) {
        if (sprites != null) {
            for (Sprite sprite : sprites) {
                int count = canvas.save();
                sprite.draw(canvas);
                canvas.restoreToCount(count);
            }
        }
    }

    @Override
    protected void drawSelf(Canvas canvas) {
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        for (Sprite sprite : sprites) {
            sprite.setBounds(bounds);
        }
    }

    @Override
    public void start() {
        super.start();
        start(sprites);
    }

    @Override
    public void stop() {
        super.stop();
        stop(sprites);
    }

    @Override
    public boolean isRunning() {
        return isRunning(sprites) || super.isRunning();
    }

    public abstract Sprite[] onCreateChild();

    @Override
    public ValueAnimator onCreateAnimation() {
        return null;
    }

    public static void start(Sprite... sprites) {
        for (Sprite sprite : sprites) {
            sprite.start();
        }
    }

    public static void stop(Sprite... sprites) {
        for (Sprite sprite : sprites) {
            sprite.stop();
        }
    }

    public static boolean isRunning(Sprite... sprites) {
        for (Sprite sprite : sprites) {
            if (sprite.isRunning()) {
                return true;
            }
        }
        return false;
    }
}
