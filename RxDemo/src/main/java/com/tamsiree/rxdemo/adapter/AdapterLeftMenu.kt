package com.tamsiree.rxdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.model.ModelDishMenu
import java.util.*

/**
 *
 * @author tamsiree
 * @date 16-11-10
 */
class AdapterLeftMenu(private val mContext: Context, private val mMenuList: ArrayList<ModelDishMenu>) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var mSelectedNum: Int
    private val mSelectedListenerList: MutableList<onItemSelectedListener>?

    interface onItemSelectedListener {
        fun onLeftItemSelected(postion: Int, menu: ModelDishMenu)
    }

    fun addItemSelectedListener(listener: onItemSelectedListener) {
        mSelectedListenerList?.add(listener)
    }

    fun removeItemSelectedListener(listener: onItemSelectedListener) {
        if (mSelectedListenerList != null && mSelectedListenerList.isNotEmpty()) mSelectedListenerList.remove(listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.left_menu_item, parent, false)
        return LeftMenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val modelDishMenu = mMenuList[position]
        val viewHolder = holder as LeftMenuViewHolder?
        viewHolder!!.menuName.text = modelDishMenu.menuName
        viewHolder.menuLayout.isSelected = mSelectedNum == position
    }

    override fun getItemCount(): Int {
        return mMenuList.size
    }

    var selectedNum: Int
        get() = mSelectedNum
        set(selectedNum) {
            if (selectedNum in 0 until itemCount) {
                mSelectedNum = selectedNum
                notifyDataSetChanged()
            }
        }

    private inner class LeftMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var menuName: TextView = itemView.findViewById(R.id.left_menu_textview)
        var menuLayout: LinearLayout = itemView.findViewById(R.id.left_menu_item)

        init {
            menuLayout.setOnClickListener {
                val clickPosition = adapterPosition
                //                    setSelectedNum(clickPosition);
                notifyItemSelected(clickPosition)
            }
        }
    }

    private fun notifyItemSelected(position: Int) {
        if (mSelectedListenerList != null && mSelectedListenerList.isNotEmpty()) {
            for (listener in mSelectedListenerList) {
                listener.onLeftItemSelected(position, mMenuList[position])
            }
        }
    }

    init {
        mSelectedNum = -1
        mSelectedListenerList = ArrayList()
        if (mMenuList.size > 0) mSelectedNum = 0
    }
}