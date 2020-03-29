package com.tamsiree.rxui.view.wavesidebar.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tamsiree.rxui.R
import com.tamsiree.rxui.model.ModelContactCity

/**
 * 此适配器仅供参考借鉴
 * @author tamsiree
 */
class AdapterContactCity : BaseWaveSideAdapter<ModelContactCity?, BaseViewHolder?> {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, data: List<ModelContactCity?>?) : super(context, data)

    override fun getDefItemViewType(position: Int): Int {
        val city = getItem(position)
        return city!!.type
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == 0) {
            CityHolder(inflateItemView(R.layout.item_wave_contact, parent))
        } else {
            PinnedHolder(inflateItemView(R.layout.item_pinned_header, parent))
        }
    }

    override fun convert(holder: BaseViewHolder?, item: ModelContactCity?) {
        if (holder is CityHolder) {
            holder.city_name!!.text = item?.name
        } else {
            val letter = item?.pys!!.substring(0, 1)
            (holder as PinnedHolder).city_tip!!.text = letter
        }
    }

    fun getLetterPosition(letter: String): Int {
        for (i in data.indices) {
            if (data[i]!!.type == 1 && data[i]!!.pys == letter) {
                return i
            }
        }
        return -1
    }

    internal inner class CityHolder(view: View?) : BaseViewHolder(view) {
        var city_name: TextView? = findViewById<TextView>(R.id.tv_contact_name)

    }

    internal inner class PinnedHolder(view: View?) : BaseViewHolder(view) {
        var city_tip: TextView? = findViewById<TextView>(R.id.city_tip)

    }
}