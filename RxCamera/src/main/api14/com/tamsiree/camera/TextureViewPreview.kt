package com.tamsiree.camera

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.view.ViewGroup

@TargetApi(14)
internal class TextureViewPreview(context: Context?, parent: ViewGroup?) : PreviewImpl() {
    private val mTextureView: TextureView
    private var mDisplayOrientation = 0

    // This method is called only from Camera2.
    @TargetApi(15)
    public override fun setBufferSize(width: Int, height: Int) {
        mTextureView.surfaceTexture.setDefaultBufferSize(width, height)
    }

    public override fun getSurface(): Surface {
        return Surface(mTextureView.surfaceTexture)
    }

    public override fun getSurfaceTexture(): SurfaceTexture {
        return mTextureView.surfaceTexture
    }

    public override fun getView(): View {
        return mTextureView
    }

    public override fun getOutputClass(): Class<*> {
        return SurfaceTexture::class.java
    }

    public override fun setDisplayOrientation(displayOrientation: Int) {
        mDisplayOrientation = displayOrientation
        configureTransform()
    }

    public override fun isReady(): Boolean {
        return mTextureView.surfaceTexture != null
    }

    /**
     * Configures the transform matrix for TextureView based on [.mDisplayOrientation] and
     * the surface size.
     */
    fun configureTransform() {
        val matrix = Matrix()
        if (mDisplayOrientation % 180 == 90) {
            val width = width
            val height = height
            // Rotate the camera preview when the screen is landscape.
            matrix.setPolyToPoly(floatArrayOf(
                    0f, 0f,  // top left
                    width.toFloat(), 0f,  // top right
                    0f, height.toFloat(),  // bottom left
                    width.toFloat(), height.toFloat()), 0,
                    if (mDisplayOrientation == 90) floatArrayOf(
                            0f, height.toFloat(),  // top left
                            0f, 0f,  // top right
                            width.toFloat(), height.toFloat(),  // bottom left
                            width.toFloat(), 0f) else floatArrayOf(
                            width.toFloat(), 0f,  // top left
                            width.toFloat(), height.toFloat(),  // top right
                            0f, 0f,  // bottom left
                            0f, height.toFloat()), 0,
                    4)
        } else if (mDisplayOrientation == 180) {
            matrix.postRotate(180f, width / 2.toFloat(), height / 2.toFloat())
        }
        mTextureView.setTransform(matrix)
    }

    init {
        val view = View.inflate(context, R.layout.texture_view, parent)
        mTextureView = view.findViewById<View>(R.id.texture_view) as TextureView
        mTextureView.surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                setSize(width, height)
                configureTransform()
                dispatchSurfaceChanged()
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                setSize(width, height)
                configureTransform()
                dispatchSurfaceChanged()
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                setSize(0, 0)
                return true
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
        }
    }
}