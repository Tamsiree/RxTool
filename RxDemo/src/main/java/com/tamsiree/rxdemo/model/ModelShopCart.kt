package com.tamsiree.rxdemo.model

import android.util.Log
import java.util.*

/**
 * @author tamsiree
 * @date 16-11-12
 */
class ModelShopCart {
    var shoppingAccount = 0 //商品总数 = 0
        private set
    var shoppingTotalPrice = 0.00 //商品总价钱 = 0.0
        private set
    private val shoppingSingle //单个物品的总价价钱
            : MutableMap<ModelDish, Int>

    val shoppingSingleMap: Map<ModelDish, Int>
        get() = shoppingSingle

    fun addShoppingSingle(modelDish: ModelDish): Boolean {
        var remain = modelDish.dishRemain
        if (remain <= 0) return false
        modelDish.dishRemain = --remain
        var num = 0
        if (shoppingSingle.containsKey(modelDish)) {
            num = shoppingSingle[modelDish]!!
        }
        num += 1
        shoppingSingle[modelDish] = num
        Log.e("TAG", "addShoppingSingle: " + shoppingSingle[modelDish])
        shoppingTotalPrice += modelDish.dishPrice
        shoppingAccount++
        return true
    }

    fun subShoppingSingle(modelDish: ModelDish): Boolean {
        var num = 0
        if (shoppingSingle.containsKey(modelDish)) {
            num = shoppingSingle[modelDish]!!
        }
        if (num <= 0) return false
        num--
        var remain = modelDish.dishRemain
        modelDish.dishRemain = ++remain
        shoppingSingle[modelDish] = num
        if (num == 0) shoppingSingle.remove(modelDish)
        shoppingTotalPrice -= modelDish.dishPrice
        shoppingAccount--
        return true
    }

    val dishAccount: Int
        get() = shoppingSingle.size

    fun clear() {
        shoppingAccount = 0
        shoppingTotalPrice = 0.0
        shoppingSingle.clear()
    }

    init {
        shoppingSingle = HashMap()
    }
}