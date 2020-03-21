package com.tamsiree.rxdemo.model

import com.tamsiree.rxdemo.tools.RxPullXml.unitType

/**
 * Created by Android Studio
 * user : Tamsiree
 * date : 2017/3/14 ${time}
 * desc :
 */
class CityModel {

    var cityName: String? = null
    var cityCode: String? = null
    var cityType: unitType? = null
    var countyList: MutableList<CityModel>? = null


    constructor()

    constructor(cityName: String?, cityCode: String?, cityType: unitType?, countyList: MutableList<CityModel>?) {
        this.cityName = cityName
        this.cityCode = cityCode
        this.cityType = cityType
        this.countyList = countyList
    }


}