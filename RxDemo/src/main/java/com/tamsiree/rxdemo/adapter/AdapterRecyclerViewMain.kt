package com.tamsiree.rxdemo.adapter

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.model.ModelDemo

/**
 * @author tamsiree
 * @date 2016/11/13
 */
class AdapterRecyclerViewMain(data: List<ModelDemo?>?, private val mOnClickListener: ContentListener) : BaseQuickAdapter<ModelDemo, BaseViewHolder>(R.layout.item_recyclerview_main, data) {
    override fun convert(helper: BaseViewHolder, item: ModelDemo) {
        helper.setText(R.id.tv_name, item.name)
        val imageView = helper.getView<ImageView>(R.id.imageView)
        Glide.with(mContext).load(item.image).thumbnail(0.5f).into(imageView)
        helper.setOnClickListener(R.id.ll_root) { v: View? -> mOnClickListener.setListener(helper.position) }
    }

    interface ContentListener {
        fun setListener(position: Int)
    }

}