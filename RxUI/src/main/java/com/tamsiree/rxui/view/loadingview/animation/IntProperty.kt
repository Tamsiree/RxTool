package com.tamsiree.rxui.view.loadingview.animation

import android.util.Property

/**
 * @author tamsiree
 * An implementation of [android.util.Property] to be used specifically with fields of type
 * `int`. This type-specific subclass enables performance benefit by allowing
 * calls to a [set()][.set] function that takes the primitive
 * `int` type and avoids autoboxing and other overhead associated with the
 * `Integer` class.
 *
 * @param <T> The class on which the Property is declared.
</T> */
abstract class IntProperty<T>(name: String?) : Property<T, Int>(Int::class.java, name) {
    /**
     * A type-specific override of the [.set] that is faster when dealing
     * with fields of type `int`.
     */
    abstract fun setValue(`object`: T, value: Int)
    override fun set(`object`: T, value: Int) {
        setValue(`object`, value)
    }
}