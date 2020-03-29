package com.tamsiree.rxui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.tamsiree.rxui.R
import com.tamsiree.rxui.model.ModelPicture
import com.tamsiree.rxui.view.RxTextAutoZoom
import com.tamsiree.rxui.view.cardstack.RxCardStackView
import com.tamsiree.rxui.view.cardstack.tools.RxAdapterStack
import java.io.File

class AdapterCardViewModelPicture(context: Context) : RxAdapterStack<ModelPicture>(context) {
    override fun bindView(data: ModelPicture, position: Int, holder: RxCardStackView.ViewHolder) {
        if (holder is ColorItemViewHolder) {
            val h = holder
            h.onBind(data, position)
            if (itemCount < 2) {
                h.mRxTextAutoZoom.visibility = View.GONE
            } else {
                h.mRxTextAutoZoom.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RxCardStackView.ViewHolder {
        val view = layoutInflater.inflate(R.layout.card_item_picture, parent, false)
        return ColorItemViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.card_item_picture
    }

    internal class ColorItemViewHolder(view: View) : RxCardStackView.ViewHolder(view) {
        var mContainerContent: View
        var mIvPicture: ImageView
        var mTvPointLo: TextView
        var mTvPointLa: TextView
        var mTvCollectDate: TextView
        var mRxTextAutoZoom: RxTextAutoZoom
        override fun onItemExpand(b: Boolean) {
            mContainerContent.visibility = if (b) View.VISIBLE else View.GONE
        }

        fun onBind(data: ModelPicture, position: Int) {
            Glide.with(context).load(File(data.picturePath)).thumbnail(0.5f).into(mIvPicture)
            mTvCollectDate.text = data.date
            mTvPointLo.text = data.longitude
            mTvPointLa.text = data.latitude
            mRxTextAutoZoom.setText("第 " + (position + 1) + " 张")
        }

        init {
            mIvPicture = view.findViewById(R.id.iv_picture)
            mContainerContent = view.findViewById(R.id.container_list_content)
            mTvPointLo = view.findViewById(R.id.tv_point_lo)
            mTvPointLa = view.findViewById(R.id.tv_point_la)
            mTvCollectDate = view.findViewById(R.id.tv_collect_date)
            mRxTextAutoZoom = view.findViewById(R.id.tv_number)
        }
    }

}