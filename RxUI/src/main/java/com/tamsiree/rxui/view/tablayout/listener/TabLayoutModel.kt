package com.tamsiree.rxui.view.tablayout.listener

import androidx.annotation.DrawableRes

interface TabLayoutModel {
    val tabTitle: String?

    @get:DrawableRes
    val tabSelectedIcon: Int

    @get:DrawableRes
    val tabUnselectedIcon: Int
}