package com.tamsiree.rxdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.view.tcardgralleryview.CardAdapterHelper

/**
 * Created by Tamsiree on 8/30/16.
 */
internal class AdapterCardGallery(private var mList: MutableList<Int>) : RecyclerView.Adapter<AdapterCardGallery.ViewHolder>() {
    private val mCardAdapterHelper = CardAdapterHelper()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.view_card_item, parent, false)
        mCardAdapterHelper.onCreateViewHolder(parent, itemView)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, itemCount)
        holder.mImageView.setImageResource(mList[position])
        holder.mImageView.setOnClickListener { RxToast.normal(holder.mImageView.context, "" + position) }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImageView: ImageView = itemView.findViewById<View>(R.id.imageView) as ImageView

    }

}