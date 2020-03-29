package com.tamsiree.rxui.view.progressing.animation

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.util.Property
import android.view.animation.Animation
import android.view.animation.Interpolator
import com.tamsiree.rxui.view.progressing.animation.interpolator.KeyFrameInterpolator
import com.tamsiree.rxui.view.progressing.sprite.Sprite
import java.util.*

/**
 * @author tamsiree
 */
class SpriteAnimatorBuilder(private val sprite: Sprite) {
    private val propertyValuesHolders: MutableList<PropertyValuesHolder> = ArrayList()
    private var interpolator: Interpolator? = null
    private var repeatCount = Animation.INFINITE
    private var duration: Long = 2000
    fun scale(fractions: FloatArray, vararg scale: Float): SpriteAnimatorBuilder {
        holder(fractions, Sprite.SCALE, scale)
        return this
    }

    fun alpha(fractions: FloatArray, vararg alpha: Int): SpriteAnimatorBuilder {
        holder(fractions, Sprite.ALPHA, alpha)
        return this
    }

    fun scaleX(fractions: FloatArray, vararg scaleX: Float): SpriteAnimatorBuilder {
        holder(fractions, Sprite.SCALE, scaleX)
        return this
    }

    fun scaleY(fractions: FloatArray, vararg scaleY: Float): SpriteAnimatorBuilder {
        holder(fractions, Sprite.SCALE_Y, scaleY)
        return this
    }

    fun rotateX(fractions: FloatArray, vararg rotateX: Int): SpriteAnimatorBuilder {
        holder(fractions, Sprite.ROTATE_X, rotateX)
        return this
    }

    fun rotateY(fractions: FloatArray, vararg rotateY: Int): SpriteAnimatorBuilder {
        holder(fractions, Sprite.ROTATE_Y, rotateY)
        return this
    }

    fun translateX(fractions: FloatArray, vararg translateX: Int): SpriteAnimatorBuilder {
        holder(fractions, Sprite.TRANSLATE_X, translateX)
        return this
    }

    fun translateY(fractions: FloatArray, vararg translateY: Int): SpriteAnimatorBuilder {
        holder(fractions, Sprite.TRANSLATE_Y, translateY)
        return this
    }

    fun rotate(fractions: FloatArray, vararg rotate: Int): SpriteAnimatorBuilder {
        holder(fractions, Sprite.ROTATE, rotate)
        return this
    }

    fun translateXPercentage(fractions: FloatArray, vararg translateXPercentage: Float): SpriteAnimatorBuilder {
        holder(fractions, Sprite.TRANSLATE_X_PERCENTAGE, translateXPercentage)
        return this
    }

    fun translateYPercentage(fractions: FloatArray, vararg translateYPercentage: Float): SpriteAnimatorBuilder {
        holder(fractions, Sprite.TRANSLATE_Y_PERCENTAGE, translateYPercentage)
        return this
    }

    private fun holder(fractions: FloatArray, property: Property<*, *>, values: FloatArray): PropertyValuesHolder {
        ensurePair(fractions.size, values.size)
        val keyframes = arrayOfNulls<Keyframe>(fractions.size)
        for (i in values.indices) {
            keyframes[i] = Keyframe.ofFloat(fractions[i], values[i])
        }
        val valuesHolder = PropertyValuesHolder.ofKeyframe(property
                , *keyframes
        )
        propertyValuesHolders.add(valuesHolder)
        return valuesHolder
    }

    private fun holder(fractions: FloatArray, property: Property<*, *>, values: IntArray): PropertyValuesHolder {
        ensurePair(fractions.size, values.size)
        val keyframes = arrayOfNulls<Keyframe>(fractions.size)
        for (i in values.indices) {
            keyframes[i] = Keyframe.ofInt(fractions[i], values[i])
        }
        val valuesHolder = PropertyValuesHolder.ofKeyframe(property
                , *keyframes
        )
        propertyValuesHolders.add(valuesHolder)
        return valuesHolder
    }

    private fun ensurePair(fractionsLength: Int, valuesLength: Int) {
        check(fractionsLength == valuesLength) {
            String.format(
                    Locale.getDefault(),
                    "The fractions.length must equal values.length, " + "fraction.length[%d], values.length[%d]",
                    fractionsLength,
                    valuesLength)
        }
    }

    fun interpolator(interpolator: Interpolator?): SpriteAnimatorBuilder {
        this.interpolator = interpolator
        return this
    }

    fun easeInOut(vararg fractions: Float): SpriteAnimatorBuilder {
        interpolator(KeyFrameInterpolator.easeInOut(
                *fractions
        ))
        return this
    }

    fun duration(duration: Long): SpriteAnimatorBuilder {
        this.duration = duration
        return this
    }

    fun repeatCount(repeatCount: Int): SpriteAnimatorBuilder {
        this.repeatCount = repeatCount
        return this
    }

    fun build(): ObjectAnimator {
        val holders = arrayOfNulls<PropertyValuesHolder>(propertyValuesHolders.size)

        val proArray: Array<PropertyValuesHolder> = propertyValuesHolders.toTypedArray()

        val animator = ObjectAnimator.ofPropertyValuesHolder(sprite, *proArray)
        animator.duration = duration
        animator.repeatCount = repeatCount
        animator.interpolator = interpolator
        return animator
    }

}