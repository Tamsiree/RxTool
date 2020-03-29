package com.tamsiree.rxui.view.colorpicker

import android.graphics.Color

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class ColorCircle(x: Float, y: Float, hsv: FloatArray) {
    var x = 0f
        private set
    var y = 0f
        private set
    val hsv = FloatArray(3)
    private var hsvClone: FloatArray? = null
    var color = 0
        private set

    fun sqDist(x: Float, y: Float): Double {
        val dx = this.x - x.toDouble()
        val dy = this.y - y.toDouble()
        return dx * dx + dy * dy
    }

    fun getHsvWithLightness(lightness: Float): FloatArray? {
        if (hsvClone == null) {
            hsvClone = hsv.clone()
        }
        hsvClone!![0] = hsv[0]
        hsvClone!![1] = hsv[1]
        hsvClone!![2] = lightness
        return hsvClone
    }

    operator fun set(x: Float, y: Float, hsv: FloatArray) {
        this.x = x
        this.y = y
        this.hsv[0] = hsv[0]
        this.hsv[1] = hsv[1]
        this.hsv[2] = hsv[2]
        color = Color.HSVToColor(this.hsv)
    }

    init {
        set(x, y, hsv)
    }
}