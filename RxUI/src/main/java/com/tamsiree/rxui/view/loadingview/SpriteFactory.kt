package com.tamsiree.rxui.view.loadingview

import com.tamsiree.rxui.view.loadingview.sprite.Sprite
import com.tamsiree.rxui.view.loadingview.style.*

/**
 * @author tamsiree
 */
object SpriteFactory {
    @JvmStatic
    fun create(style: Style?): Sprite? {
        var sprite: Sprite? = null
        when (style) {
            Style.ROTATING_PLANE -> sprite = RotatingPlane()
            Style.DOUBLE_BOUNCE -> sprite = DoubleBounce()
            Style.WAVE -> sprite = Wave()
            Style.WANDERING_CUBES -> sprite = WanderingCubes()
            Style.PULSE -> sprite = Pulse()
            Style.CHASING_DOTS -> sprite = ChasingDots()
            Style.THREE_BOUNCE -> sprite = ThreeBounce()
            Style.CIRCLE -> sprite = Circle()
            Style.CUBE_GRID -> sprite = CubeGrid()
            Style.FADING_CIRCLE -> sprite = FadingCircle()
            Style.FOLDING_CUBE -> sprite = FoldingCube()
            Style.ROTATING_CIRCLE -> sprite = RotatingCircle()
            Style.MULTIPLE_PULSE -> sprite = MultiplePulse()
            Style.PULSE_RING -> sprite = PulseRing()
            Style.MULTIPLE_PULSE_RING -> sprite = MultiplePulseRing()
        }
        return sprite
    }
}