package com.tamsiree.rxdemo.model;

/**
 * @author tamsiree
 * @date 2016/11/13
 */

public class ModelDemo {

    private String name;

    private int image;

    private Class activity;

    public ModelDemo(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public ModelDemo(String name, int image, Class activity) {
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
