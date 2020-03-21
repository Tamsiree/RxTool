package com.tamsiree.rxdemo.interfaces

import android.view.View

/**
 *
 * @author tamsiree
 * @date 16-11-13
 */
interface ShopCartInterface {
    fun add(view: View?, position: Int)
    fun remove(view: View?, position: Int)
}