package com.tamsiree.rxdemo.model

import android.app.Activity

/**
 * @author tamsiree
 * @date 2016/11/13
 */
class ModelDemo {
    var name: String
    var image: Int
    var activity: Class<out Activity>? = null

    constructor(name: String, image: Int) {
        this.name = name
        this.image = image
    }

    constructor(name: String, image: Int, activity: Class<out Activity>?) {
        this.name = name
        this.image = image
        this.activity = activity
    }


}