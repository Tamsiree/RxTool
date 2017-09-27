package com.vondear.rxtools.module.wechat.share;

/**
 * Created by Administrator on 2017/4/20.
 */

public class WechatShareModel {
    private String url;
    private String title;
    private String description;
    private byte[] thumbData;

    public WechatShareModel(String url, String title, String description, byte[] thumbData) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.thumbData = thumbData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getThumbData() {
        return thumbData;
    }

    public void setThumbData(byte[] thumbData) {
        this.thumbData = thumbData;
    }
}
