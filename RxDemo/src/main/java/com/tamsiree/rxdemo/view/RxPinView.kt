package com.tamsiree.rxdemo.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.tamsiree.rxdemo.R
import com.tamsiree.rxui.view.scaleimage.RxScaleImageView

/**
 * @author tamsiree
 */
class RxPinView @JvmOverloads constructor(context: Context?, attr: AttributeSet? = null) : RxScaleImageView(context, attr) {
    private var sPin: PointF? = null
    private lateinit var pin: Bitmap
    private lateinit var paint: Paint
    fun getPin(): PointF? {
        return sPin
    }

    fun setPin(sPin: PointF?) {
        this.sPin = sPin
        initialise()
        invalidate()
    }

    private fun initialise() {
        val density = resources.displayMetrics.densityDpi.toFloat()
        pin = BitmapFactory.decodeResource(resources, R.drawable.pushpin_blue)
        val w = (density / 420f) * pin.width
        val h = density / 420f * pin.height
        pin = Bitmap.createScaledBitmap(pin, w.toInt(), h.toInt(), true)
        paint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady) {
            return
        }

        paint.isAntiAlias = true
        if (sPin != null) {
            val vPin = sourceToViewCoord(sPin)
            val vX = vPin.x - pin.width / 2
            val vY = vPin.y - pin.height
            canvas.drawBitmap(pin, vX, vY, paint)
        }
    }

    init {
        initialise()
    }
}