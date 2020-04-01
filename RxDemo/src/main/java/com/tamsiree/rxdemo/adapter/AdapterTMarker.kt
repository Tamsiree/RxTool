package com.tamsiree.rxdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.viewholder.ViewHolderSwipeable
import com.tamsiree.rxui.view.mark.TMarker


class AdapterTMarker(
        private val covert: TMarker
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            ViewHolderSwipeable(
                    itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.viewholder_swipeable, viewGroup, false))


    override fun getItemCount(): Int = 50

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, index: Int) {
        covert.drawCornerFlag(viewHolder)

        (viewHolder as? ViewHolderSwipeable)?.apply(ViewHolderSwipeable::bind)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        // The following is an optimisation for Covert, allowing us to skip re-binding of
        // ViewHolders if only drawing the child
        if (!payloads.contains(TMarker.SKIP_FULL_BIND_PAYLOAD)) {
            onBindViewHolder(holder, position)
        }
    }
}
