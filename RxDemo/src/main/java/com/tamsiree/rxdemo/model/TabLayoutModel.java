package com.tamsiree.rxdemo.model;


public class TabLayoutModel implements com.tamsiree.rxui.view.tablayout.listener.TabLayoutModel {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public TabLayoutModel(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
