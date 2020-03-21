package com.tamsiree.rxdemo.model

import java.util.*

/**
 * @author tamsiree
 * @date 16-11-10
 */
class ModelDishMenu {
    var menuName: String? = null
    var modelDishList: ArrayList<ModelDish>? = null

    constructor()
    constructor(menuName: String?, dishList: ArrayList<ModelDish>?) {
        this.menuName = menuName
        modelDishList = dishList
    }

}