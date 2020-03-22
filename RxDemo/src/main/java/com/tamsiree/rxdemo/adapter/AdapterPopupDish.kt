package com.tamsiree.rxdemo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.interfaces.ShopCartInterface
import com.tamsiree.rxdemo.model.ModelDish
import com.tamsiree.rxdemo.model.ModelShopCart
import java.util.*

/**
 *
 * @author tamsiree
 * @date 16-12-23
 */
class AdapterPopupDish(private val context: Context, private val mModelShopCart: ModelShopCart) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var itemCount: Int
    private val mModelDishList: ArrayList<ModelDish>
    var shopCartInterface: ShopCartInterface? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.right_dish_item1, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dishholder = holder as DishViewHolder?
        val modelDish = getDishByPosition(position)
        dishholder!!.right_dish_name_tv.text = modelDish.dishName
        dishholder.right_dish_price_tv.text = modelDish.dishPrice.toString()
        val num = mModelShopCart.shoppingSingleMap[modelDish]!!
        dishholder.right_dish_account_tv.text = num.toString()
        dishholder.right_dish_add_iv.setOnClickListener { view ->
            if (mModelShopCart.addShoppingSingle(modelDish)) {
                notifyItemChanged(position)
                if (shopCartInterface != null) {
                    shopCartInterface!!.add(view, position)
                }
            }
        }
        dishholder.right_dish_remove_iv.setOnClickListener { view ->
            if (mModelShopCart.subShoppingSingle(modelDish)) {
                mModelDishList.clear()
                mModelDishList.addAll(mModelShopCart.shoppingSingleMap.keys)
                itemCount = mModelShopCart.dishAccount
                notifyDataSetChanged()
                if (shopCartInterface != null) {
                    shopCartInterface!!.remove(view, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    fun getDishByPosition(position: Int): ModelDish {
        return mModelDishList[position]
    }

    private inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val right_dish_name_tv: TextView
        val right_dish_price_tv: TextView
        private val right_dish_layout: LinearLayout
        val right_dish_remove_iv: ImageView
        val right_dish_add_iv: ImageView
        val right_dish_account_tv: TextView

        init {
            right_dish_name_tv = itemView.findViewById<View>(R.id.right_dish_name) as TextView
            right_dish_price_tv = itemView.findViewById<View>(R.id.right_dish_price) as TextView
            right_dish_layout = itemView.findViewById<View>(R.id.right_dish_item) as LinearLayout
            right_dish_remove_iv = itemView.findViewById<View>(R.id.right_dish_remove) as ImageView
            right_dish_add_iv = itemView.findViewById<View>(R.id.right_dish_add) as ImageView
            right_dish_account_tv = itemView.findViewById<View>(R.id.right_dish_account) as TextView
        }
    }

    companion object {
        private const val TAG = "PopupDishAdapter"
    }

    init {
        itemCount = mModelShopCart.dishAccount
        mModelDishList = ArrayList()
        mModelDishList.addAll(mModelShopCart.shoppingSingleMap.keys)
        Log.e(TAG, "PopupDishAdapter: " + itemCount)
    }
}