package com.tamsiree.rxui.view.progressing.animation

import android.util.Property

/**
 * @author tamsiree
 * An implementation of [android.util.Property] to be used specifically with fields of type
 * `float`. This type-specific subclass enables performance benefit by allowing
 * calls to a [set()][.set] function that takes the primitive
 * `float` type and avoids autoboxing and other overhead associated with the
 * `Float` class.
 *
 * @param <T> The class on which the Property is declared.
</T> */
abstract class FloatProperty<T>(name: String?) : Property<T, Float>(Float::class.java, name) {
    /**
     * A type-specific override of the [.set] that is faster when dealing
     * with fields of type `float`.
     */
    abstract fun setValue(`object`: T, value: Float)
    override fun set(`object`: T, value: Float) {
        setValue(`object`, value)
    }
}