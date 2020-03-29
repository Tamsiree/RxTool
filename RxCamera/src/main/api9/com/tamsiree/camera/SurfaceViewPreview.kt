package com.tamsiree.camera

import android.content.Context
import android.view.*
import androidx.core.view.ViewCompat

internal class SurfaceViewPreview(context: Context?, parent: ViewGroup?) : PreviewImpl() {
    val mSurfaceView: SurfaceView
    public override fun getSurface(): Surface {
        return surfaceHolder.surface
    }

    public override fun getSurfaceHolder(): SurfaceHolder {
        return mSurfaceView.holder
    }

    public override fun getView(): View {
        return mSurfaceView
    }

    public override fun getOutputClass(): Class<*> {
        return SurfaceHolder::class.java
    }

    public override fun setDisplayOrientation(displayOrientation: Int) {}
    public override fun isReady(): Boolean {
        return width != 0 && height != 0
    }

    init {
        val view = View.inflate(context, R.layout.surface_view, parent)
        mSurfaceView = view.findViewById<View>(R.id.surface_view) as SurfaceView
        val holder = mSurfaceView.holder
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(h: SurfaceHolder) {}
            override fun surfaceChanged(h: SurfaceHolder, format: Int, width: Int, height: Int) {
                setSize(width, height)
                if (!ViewCompat.isInLayout(mSurfaceView)) {
                    dispatchSurfaceChanged()
                }
            }

            override fun surfaceDestroyed(h: SurfaceHolder) {
                setSize(0, 0)
            }
        })
    }
}