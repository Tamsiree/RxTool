package com.tamsiree.rxui.view.colorpicker.builder

import android.graphics.*

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
object PaintBuilder {
    fun newPaint(): PaintHolder {
        return PaintHolder()
    }

    fun createAlphaPatternShader(size: Int): Shader {
        var size = size
        size /= 2
        size = Math.max(8, size * 2)
        return BitmapShader(createAlphaBackgroundPattern(size), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
    }

    private fun createAlphaBackgroundPattern(size: Int): Bitmap {
        val alphaPatternPaint = newPaint().build()
        val bm = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val s = Math.round(size / 2f)
        for (i in 0..1) {
            for (j in 0..1) {
                if ((i + j) % 2 == 0) {
                    alphaPatternPaint.color = -0x1
                } else {
                    alphaPatternPaint.color = -0x2f2f30
                }
                c.drawRect(i * s.toFloat(), j * s.toFloat(), (i + 1) * s.toFloat(), (j + 1) * s.toFloat(), alphaPatternPaint)
            }
        }
        return bm
    }

    class PaintHolder {
        private val paint: Paint
        fun color(color: Int): PaintHolder {
            paint.color = color
            return this
        }

        fun antiAlias(flag: Boolean): PaintHolder {
            paint.isAntiAlias = flag
            return this
        }

        fun style(style: Paint.Style?): PaintHolder {
            paint.style = style
            return this
        }

        fun mode(mode: PorterDuff.Mode?): PaintHolder {
            paint.xfermode = PorterDuffXfermode(mode)
            return this
        }

        fun stroke(width: Float): PaintHolder {
            paint.strokeWidth = width
            return this
        }

        fun xPerMode(mode: PorterDuff.Mode?): PaintHolder {
            paint.xfermode = PorterDuffXfermode(mode)
            return this
        }

        fun shader(shader: Shader?): PaintHolder {
            paint.shader = shader
            return this
        }

        fun build(): Paint {
            return paint
        }

        init {
            paint = Paint(Paint.ANTI_ALIAS_FLAG)
        }
    }
}