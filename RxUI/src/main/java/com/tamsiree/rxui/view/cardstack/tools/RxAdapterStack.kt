package com.tamsiree.rxui.view.cardstack.tools

import android.content.Context
import android.view.LayoutInflater
import com.tamsiree.rxui.view.cardstack.RxCardStackView
import java.util.*

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
abstract class RxAdapterStack<T>(val context: Context) : RxCardStackView.Adapter<RxCardStackView.ViewHolder>() {
    val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mData: MutableList<T>

    fun updateData(data: List<T>?) {
        setData(data)
        notifyDataSetChanged()
    }

    fun setData(data: List<T>?) {
        mData.clear()
        if (data != null) {
            mData.addAll(data)
//            itemCount=mData.size
        }
    }

    override fun onBindViewHolder(holder: RxCardStackView.ViewHolder, position: Int) {
        val data = getItem(position)
        bindView(data, position, holder)
    }

    abstract fun bindView(data: T, position: Int, holder: RxCardStackView.ViewHolder)

/*    override fun getItemCount(): Int {
        return mData.size
    }*/

    fun getItem(position: Int): T {
        return mData[position]
    }

    init {
        mData = ArrayList()
    }

    override val itemCount: Int
        get() = mData.size


}