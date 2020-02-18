package com.tamsiree.rxui.view.progressing.style;


import com.tamsiree.rxui.view.progressing.sprite.Sprite;
import com.tamsiree.rxui.view.progressing.sprite.SpriteContainer;

/**
 * @author tamsiree
 */
public class MultiplePulse extends SpriteContainer {
    @Override
    public Sprite[] onCreateChild() {
        return new Sprite[]{
                new Pulse(),
                new Pulse(),
                new Pulse(),
        };
    }

    @Override
    public void onChildCreated(Sprite... sprites) {
        for (int i = 0; i < sprites.length; i++) {
            sprites[i].setAnimationDelay(200 * (i + 1));
        }
    }
}
