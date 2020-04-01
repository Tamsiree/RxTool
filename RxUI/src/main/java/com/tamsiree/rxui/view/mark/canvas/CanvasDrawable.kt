package com.tamsiree.rxui.view.mark.canvas

import android.graphics.Canvas
import com.tamsiree.rxui.view.mark.model.ParentMetrics

/**
 * Base interface for all things animatable on a canvas.
 */
internal interface CanvasDrawable {

    /**
     * Callback used to trigger the next frame to be drawn
     */
    var invalidateCallback: () -> Unit

    /**
     * Function invoked to draw an item on the canvas
     */
    fun onDraw(canvas: Canvas, parentMetrics: ParentMetrics, canvasY: Float, proportion: Float)

    /**
     * Function invoked to tell the drawable to clear all state.
     */
    fun reset()
}