package com.tamsiree.rxui.model

/**
 * 图片实体类
 * @author Tamsiree
 * @date : 2017/6/12 ${time}
 */
class ModelPicture {
    var id: String? = null
    var longitude: String? = null
    var latitude: String? = null
    var date: String? = null
    var pictureName: String? = null
    var picturePath: String? = null
    var parentId: String? = null

    constructor()
    constructor(id: String?, longitude: String?, latitude: String?, date: String?, pictureName: String?, picturePath: String?, parentId: String?) {
        this.id = id
        this.longitude = longitude
        this.latitude = latitude
        this.date = date
        this.pictureName = pictureName
        this.picturePath = picturePath
        this.parentId = parentId
    }

}