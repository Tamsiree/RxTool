package com.tamsiree.rxui.view.tablayout.listener;


import androidx.annotation.DrawableRes;

public interface TabLayoutModel {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}