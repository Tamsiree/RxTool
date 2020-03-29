package com.tamsiree.rxui.view.ticker

import android.graphics.Paint
import java.util.*

/**
 * @author tamsiree
 * This wrapper class represents some core drawing metrics that [RxTickerView] and
 * [RxTickerColumnManager] require to calculate the positions and offsets for rendering
 * the text onto the canvas.
 */
internal class RxTickerDrawMetrics(private val textPaint: Paint) {

    // These are attributes on the text paint used for measuring and drawing the text on the
    // canvas. These attributes are reset whenever anything on the text paint changes.
    private val charWidths: MutableMap<Char, Float> = HashMap(256)
    var charHeight = 0f
        private set
    var charBaseline = 0f
        private set

    fun invalidate() {
        charWidths.clear()
        val fm = textPaint.fontMetrics
        charHeight = fm.bottom - fm.top
        charBaseline = -fm.top
    }

    fun getCharWidth(character: Char): Float {
        if (character == RxTickerUtils.EMPTY_CHAR) {
            return 0f
        }

        // This method will lazily initialize the char width map.
        val value = charWidths[character]
        return if (value != null) {
            value
        } else {
            val width = textPaint.measureText(Character.toString(character))
            charWidths[character] = width
            width
        }
    }

    init {
        invalidate()
    }
}