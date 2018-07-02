package com.vondear.rxui.model;

/**
 * 图片实体类
 * @author Vondear
 * @date : 2017/6/12 ${time}
 */


public class ModelPicture {

    private String id;
    private String longitude;
    private String latitude;
    private String date;
    private String pictureName;
    private String picturePath;
    private String parentId;

    public ModelPicture() {
    }

    public ModelPicture(String id, String longitude, String latitude, String date, String pictureName, String picturePath, String parentId) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.pictureName = pictureName;
        this.picturePath = picturePath;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
