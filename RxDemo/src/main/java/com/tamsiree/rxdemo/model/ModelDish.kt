package com.tamsiree.rxdemo.model

/**
 * @author tamsiree
 * @date 16-11-10
 */
class ModelDish(var dishName: String, var dishPrice: Double, var dishAmount: Int) {
    var dishRemain: Int = dishAmount

    override fun hashCode(): Int {
        return dishName.hashCode() + dishPrice.toInt()
    }

    override fun equals(other: Any?): Boolean {
        return if (other === this) true else other is ModelDish && dishName == other.dishName && dishPrice == other.dishPrice && dishAmount == other.dishAmount && dishRemain == other.dishRemain
    }

}