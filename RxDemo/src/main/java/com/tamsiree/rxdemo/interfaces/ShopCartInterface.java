package com.tamsiree.rxdemo.interfaces;

import android.view.View;

/**
 *
 * @author tamsiree
 * @date 16-11-13
 */
public interface ShopCartInterface {
    void add(View view, int postion);
    void remove(View view, int postion);
}
