package com.tamsiree.camera

import android.content.Context
import android.util.SparseIntArray
import android.view.Display
import android.view.OrientationEventListener
import android.view.Surface

/**
 * Monitors the value returned from [Display.getRotation].
 */
internal abstract class DisplayOrientationDetector(context: Context?) {
    private val mOrientationEventListener: OrientationEventListener

    companion object {
        /** Mapping from Surface.Rotation_n to degrees.  */
        val DISPLAY_ORIENTATIONS = SparseIntArray()

        init {
            DISPLAY_ORIENTATIONS.put(Surface.ROTATION_0, 0)
            DISPLAY_ORIENTATIONS.put(Surface.ROTATION_90, 90)
            DISPLAY_ORIENTATIONS.put(Surface.ROTATION_180, 180)
            DISPLAY_ORIENTATIONS.put(Surface.ROTATION_270, 270)
        }
    }

    var mDisplay: Display? = null
    var lastKnownDisplayOrientation = 0
        private set

    fun enable(display: Display) {
        mDisplay = display
        mOrientationEventListener.enable()
        // Immediately dispatch the first callback
        dispatchOnDisplayOrientationChanged(DISPLAY_ORIENTATIONS[display.rotation])
    }

    fun disable() {
        mOrientationEventListener.disable()
        mDisplay = null
    }

    fun dispatchOnDisplayOrientationChanged(displayOrientation: Int) {
        lastKnownDisplayOrientation = displayOrientation
        onDisplayOrientationChanged(displayOrientation)
    }

    /**
     * Called when display orientation is changed.
     *
     * @param displayOrientation One of 0, 90, 180, and 270.
     */
    abstract fun onDisplayOrientationChanged(displayOrientation: Int)

    init {
        mOrientationEventListener = object : OrientationEventListener(context) {
            /** This is either Surface.Rotation_0, _90, _180, _270, or -1 (invalid).  */
            private var mLastKnownRotation = -1
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN ||
                        mDisplay == null) {
                    return
                }
                val rotation = mDisplay!!.rotation
                if (mLastKnownRotation != rotation) {
                    mLastKnownRotation = rotation
                    dispatchOnDisplayOrientationChanged(DISPLAY_ORIENTATIONS[rotation])
                }
            }
        }
    }
}