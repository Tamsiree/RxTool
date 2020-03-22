package com.tamsiree.rxdemo.adapter

import android.content.Context
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
import com.tamsiree.rxdemo.model.ModelDishMenu
import com.tamsiree.rxdemo.model.ModelShopCart
import java.util.*

/**
 * @author tamsiree
 * @date 16-11-10
 */
class AdapterRightDish(private val mContext: Context, private val mMenuList: ArrayList<ModelDishMenu>, modelShopCart: ModelShopCart) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val MENU_TYPE = 0
    private val DISH_TYPE = 1
    private val HEAD_TYPE = 2
    private var mItemCount: Int
    private val mModelShopCart: ModelShopCart
    var shopCartInterface: ShopCartInterface? = null
    override fun getItemViewType(position: Int): Int {
        var sum = 0
        for (menu in mMenuList) {
            if (position == sum) {
                return MENU_TYPE
            }
            sum += menu.modelDishList!!.size + 1
        }
        return DISH_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MENU_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.right_menu_item, parent, false)
            MenuViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.right_dish_item1, parent, false)
            DishViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MENU_TYPE) {
            val menuholder = holder as MenuViewHolder?
            if (menuholder != null) {
                menuholder.right_menu_title.text = getMenuByPosition(position)!!.menuName
                menuholder.right_menu_layout.contentDescription = position.toString() + ""
            }
        } else {
            val dishholder = holder as DishViewHolder?
            if (dishholder != null) {
                val modelDish = getDishByPosition(position)
                dishholder.right_dish_name_tv.text = modelDish!!.dishName
                dishholder.right_dish_price_tv.text = modelDish.dishPrice.toString() + ""
                dishholder.right_dish_layout.contentDescription = position.toString() + ""
                var count = 0
                if (mModelShopCart.shoppingSingleMap.containsKey(modelDish)) {
                    count = mModelShopCart.shoppingSingleMap[modelDish]!!
                }
                if (count <= 0) {
                    dishholder.right_dish_remove_iv.visibility = View.GONE
                    dishholder.right_dish_account_tv.visibility = View.GONE
                } else {
                    dishholder.right_dish_remove_iv.visibility = View.VISIBLE
                    dishholder.right_dish_account_tv.visibility = View.VISIBLE
                    dishholder.right_dish_account_tv.text = count.toString() + ""
                }
                dishholder.right_dish_add_iv.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View) {
                        if (mModelShopCart.addShoppingSingle(modelDish)) {
                            notifyItemChanged(position)
                            if (shopCartInterface != null) {
                                shopCartInterface!!.add(view, position)
                            }
                        }
                    }
                })
                dishholder.right_dish_remove_iv.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View) {
                        if (mModelShopCart.subShoppingSingle(modelDish)) {
                            notifyItemChanged(position)
                            if (shopCartInterface != null) {
                                shopCartInterface!!.remove(view, position)
                            }
                        }
                    }
                })
            }
        }
    }

    fun getMenuByPosition(position: Int): ModelDishMenu? {
        var sum = 0
        for (menu in mMenuList) {
            if (position == sum) {
                return menu
            }
            sum += menu.modelDishList!!.size + 1
        }
        return null
    }

    fun getDishByPosition(position: Int): ModelDish? {
        var position = position
        for (menu in mMenuList) {
            position -= if (position > 0 && position <= menu.modelDishList!!.size) {
                return menu.modelDishList!![position - 1]
            } else {
                menu.modelDishList!!.size + 1
            }
        }
        return null
    }

    fun getMenuOfMenuByPosition(position: Int): ModelDishMenu? {
        var position = position
        for (menu in mMenuList) {
            if (position == 0) {
                return menu
            }
            position -= if (position > 0 && position <= menu.modelDishList!!.size) {
                return menu
            } else {
                menu.modelDishList!!.size + 1
            }
        }
        return null
    }

    override fun getItemCount(): Int {
        return mItemCount
    }

    private inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val right_menu_layout: LinearLayout
        val right_menu_title: TextView

        init {
            right_menu_layout = itemView.findViewById<View>(R.id.right_menu_item) as LinearLayout
            right_menu_title = itemView.findViewById<View>(R.id.right_menu_tv) as TextView
        }
    }

    private inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val right_dish_name_tv: TextView
        val right_dish_price_tv: TextView
        val right_dish_layout: LinearLayout
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

    init {
        mItemCount = mMenuList.size
        mModelShopCart = modelShopCart
        for (menu in mMenuList) {
            mItemCount += menu.modelDishList!!.size
        }
    }


}