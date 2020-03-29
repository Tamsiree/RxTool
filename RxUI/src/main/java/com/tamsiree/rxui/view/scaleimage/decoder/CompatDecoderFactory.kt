package com.tamsiree.rxui.view.scaleimage.decoder

/**
 * @param <T> The base type of the decoder this factory will produce.
 *
 *
 * Compatibility factory to instantiate decoders with empty public constructors.
 * @author tamsiree
</T> */
class CompatDecoderFactory<T>(private val clazz: Class<out T>) : DecoderFactory<T> {
    @Throws(IllegalAccessException::class, InstantiationException::class)
    override fun make(): T {
        return clazz.newInstance()
    }

}