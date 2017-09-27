package com.vondear.rxtools.model;

/**
 * Created by Vondear on 2017/9/27.
 */

public class ModelSpider {

    private String spiderName;
    private float spiderLevel;

    public ModelSpider(String spiderName, float spiderLevel) {
        this.spiderName = spiderName;
        this.spiderLevel = spiderLevel;
    }

    public String getSpiderName() {
        return spiderName;
    }

    public void setSpiderName(String spiderName) {
        this.spiderName = spiderName;
    }

    public float getSpiderLevel() {
        return spiderLevel;
    }

    public void setSpiderLevel(float spiderLevel) {
        this.spiderLevel = spiderLevel;
    }
}
