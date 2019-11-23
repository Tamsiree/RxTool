package com.vondear.rxui.view.scaleimage.decoder;


import androidx.annotation.NonNull;

/**
 * @param <T> The base type of the decoder this factory will produce.
 *            <p>
 *            Compatibility factory to instantiate decoders with empty public constructors.
 * @author vondear
 */
public class CompatDecoderFactory<T> implements DecoderFactory<T> {
    private Class<? extends T> clazz;

    public CompatDecoderFactory(@NonNull Class<? extends T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T make() throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }
}
