package com.tamsiree.rxui.view.progressing.style

import com.tamsiree.rxui.view.progressing.sprite.Sprite
import com.tamsiree.rxui.view.progressing.sprite.SpriteContainer

/**
 * @author tamsiree
 */
class MultiplePulse : SpriteContainer() {
    override fun onCreateChild(): Array<Sprite?>? {
        return arrayOf(
                Pulse(),
                Pulse(),
                Pulse())
    }

    override fun onChildCreated(vararg sprites: Sprite?) {
        for (i in sprites.indices) {
            sprites[i]?.setAnimationDelay(200 * (i + 1))
        }
    }
}