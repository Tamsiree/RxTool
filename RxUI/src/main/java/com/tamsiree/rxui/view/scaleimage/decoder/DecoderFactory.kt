package com.tamsiree.rxui.view.scaleimage.decoder

/**
 * @author tamsiree
 * @param <T> the class of decoder that will be produced.
 *
 * Interface for decoder (and region decoder) factories.
</T> */
interface DecoderFactory<T> {
    /**
     * Produce a new instance of a decoder with type [T].
     *
     * @return a new instance of your decoder.
     */
    @Throws(IllegalAccessException::class, InstantiationException::class)
    fun make(): T
}