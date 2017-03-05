package com.vondear.rxtools.view.ticker;

import android.graphics.Paint;

import java.util.HashMap;
import java.util.Map;

/**
 * This wrapper class represents some core drawing metrics that {@link RxTickerView} and
 * {@link RxTickerColumnManager} require to calculate the positions and offsets for rendering
 * the text onto the canvas.
 */
class RxTickerDrawMetrics {
    private final Paint textPaint;

    // These are attributes on the text paint used for measuring and drawing the text on the
    // canvas. These attributes are reset whenever anything on the text paint changes.
    private final Map<Character, Float> charWidths = new HashMap<>(256);
    private float charHeight, charBaseline;

    RxTickerDrawMetrics(Paint textPaint) {
        this.textPaint = textPaint;
        invalidate();
    }

    void invalidate() {
        charWidths.clear();
        final Paint.FontMetrics fm = textPaint.getFontMetrics();
        charHeight = fm.bottom - fm.top;
        charBaseline = -fm.top;
    }

    float getCharWidth(char character) {
        if (character == RxTickerUtils.EMPTY_CHAR) {
            return 0;
        }

        // This method will lazily initialize the char width map.
        final Float value = charWidths.get(character);
        if (value != null) {
            return value;
        } else {
            final float width = textPaint.measureText(Character.toString(character));
            charWidths.put(character, width);
            return width;
        }
    }

    float getCharHeight() {
        return charHeight;
    }

    float getCharBaseline() {
        return charBaseline;
    }
}
