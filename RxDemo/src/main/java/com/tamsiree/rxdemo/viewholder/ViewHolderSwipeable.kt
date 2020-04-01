package com.tamsiree.rxdemo.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.viewholder_swipeable.view.*

class ViewHolderSwipeable(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    fun bind() {
        Glide.with(itemView)
                .load("http://i.imgur.com/34vQcpZ.png")
                .apply(RequestOptions.circleCropTransform())
                .into(itemView.profileImageView)
    }
}