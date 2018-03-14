package com.vondear.rxtools.view.scaleimage.decoder;

/**
 * @author vondear
 * @param <T> the class of decoder that will be produced.
 *
 * Interface for decoder (and region decoder) factories.
 */
public interface DecoderFactory<T> {
    /**
     * Produce a new instance of a decoder with type {@link T}.
     *
     * @return a new instance of your decoder.
     */
    T make() throws IllegalAccessException, InstantiationException;
}
