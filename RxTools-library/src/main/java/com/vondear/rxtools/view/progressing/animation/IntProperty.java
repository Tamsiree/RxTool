package com.vondear.rxtools.view.progressing.animation;

import android.util.Property;

/**
 * @author vondear
 * An implementation of {@link android.util.Property} to be used specifically with fields of type
 * <code>int</code>. This type-specific subclass enables performance benefit by allowing
 * calls to a {@link #set(Object, Integer) set()} function that takes the primitive
 * <code>int</code> type and avoids autoboxing and other overhead associated with the
 * <code>Integer</code> class.
 *
 * @param <T> The class on which the Property is declared.
 */
public abstract class IntProperty<T> extends Property<T, Integer> {

    public IntProperty(String name) {
        super(Integer.class, name);
    }

    /**
     * A type-specific override of the {@link #set(Object, Integer)} that is faster when dealing
     * with fields of type <code>int</code>.
     */
    public abstract void setValue(T object, int value);

    @Override
    final public void set(T object, Integer value) {
        setValue(object, value);
    }

}