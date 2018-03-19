package com.vondear.tools.model;

import com.vondear.tools.tools.RxPullXml;

import java.util.List;

/**
 * Created by Android Studio
 * user : Vondear
 * date : 2017/3/14 ${time}
 * desc :
 */
public class CityModel {

    private String cityName;
    private String cityCode;
    private RxPullXml.unitType cityType;
    private List<CityModel> countyList;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public RxPullXml.unitType getCityType() {
        return cityType;
    }

    public void setCityType(RxPullXml.unitType cityType) {
        this.cityType = cityType;
    }

    public List<CityModel> getCountyList() {
        return countyList;
    }

    public void setCountyList(List<CityModel> countyList) {
        this.countyList = countyList;
    }
}
