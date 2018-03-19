package com.vondear.tools.model;

/**
 * @author vondear
 * @date 2016/11/13
 */

public class ModelMainItem {

    private String name;

    private int image;

    private Class activity;

    public ModelMainItem(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public ModelMainItem(String name, int image, Class activity) {
        this.name = name;
        this.image = image;
        this.activity = activity;
    }

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
